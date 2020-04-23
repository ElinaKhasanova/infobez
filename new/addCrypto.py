def pkcs7_pad(message, block_size):

    if len(message) == block_size:
        return message

    ch = block_size - len(message) % block_size
    return message + bytes([ch] * ch)

def pkcs7_unpad(data):
    if len(data) == 0:
        raise Exception("The input data must contain at least one byte")

    if not is_pkcs7_padded(data):
        return data

    padding_len = data[len(data) - 1]
    return data[:-padding_len]

def is_pkcs7_padded(binary_data):

    padding = binary_data[-binary_data[-1]:]

    return all(padding[b] == len(padding) for b in range(0, len(padding)))

def xor_data(binary_data_1, binary_data_2):
    return bytes([b1 ^ b2 for b1, b2 in zip(binary_data_1, binary_data_2)])

def aes_ecb_encrypt(data, key): 
    cipher = AES.new(key, AES.MODE_ECB)
    return cipher.encrypt(pkcs7_pad(data, AES.block_size))

def aes_ecb_decrypt(data, key):
    cipher = AES.new(key, AES.MODE_ECB)
    return pkcs7_unpad(cipher.decrypt(data))

def aes_cbc_encrypt(data, key, iv):
    ciphertext = b''
    prev = iv

    for i in range(0, len(data), AES.block_size):

        curr_plaintext_block = pkcs7_pad(data[i:i + AES.block_size], AES.block_size)
        block_cipher_input = xor_data(curr_plaintext_block, prev)
        encrypted_block = aes_ecb_encrypt(block_cipher_input, key)
        ciphertext += encrypted_block
        prev = encrypted_block

    return ciphertext


def aes_cbc_decrypt(data, key, iv, unpad=True):
    plaintext = b''
    prev = iv

    for i in range(0, len(data), AES.block_size):
        curr_ciphertext_block = data[i:i + AES.block_size]
        decrypted_block = aes_ecb_decrypt(curr_ciphertext_block, key)
        plaintext += xor_data(prev, decrypted_block)
        prev = curr_ciphertext_block

    return pkcs7_unpad(plaintext) if unpad else plaintext

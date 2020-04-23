from addCrypto import is_pkcs7_padded, pkcs7_unpad, aes_cbc_encrypt, aes_cbc_decrypt
from addHash import sha1
from Crypto import Random
from Crypto.Cipher import AES
from 33 import DiffieHellman
from binascii import unhexlify

def malicious_g_attack():
    p = DiffieHellman.DEFAULT_P

    for g in [1, p, p - 1]:

        alice = DiffieHellman()
        bob = DiffieHellman(g=g)

        A = alice.get_public_key()

        B = bob.get_public_key()

        _msg = b'Hello, how are you?'
        _a_key = unhexlify(sha1(str(alice.get_shared_secret_key(B)).encode()))[:16]
        _a_iv = Random.new().read(AES.block_size)
        a_question = aes_cbc_encrypt(_msg, _a_key, _a_iv) + _a_iv

        mitm_a_iv = a_question[-AES.block_size:]

        if g == 1:
            mitm_hacked_key = unhexlify(sha1(b'1').encode())[:16]
            mitm_hacked_message = aes_cbc_decrypt(a_question[:-AES.block_size], mitm_hacked_key, mitm_a_iv)

        elif g == p:
            mitm_hacked_key = unhexlify(sha1(b'0').encode())[:16]
            mitm_hacked_message = aes_cbc_decrypt(a_question[:-AES.block_size], mitm_hacked_key, mitm_a_iv)

        else:

            for candidate in [str(1).encode(), str(p - 1).encode()]:
                mitm_hacked_key = unhexlify(sha1(candidate).encode())[:16]
                mitm_hacked_message = aes_cbc_decrypt(a_question[:-AES.block_size], mitm_hacked_key,
                                                      mitm_a_iv, unpad=False)

                if is_pkcs7_padded(mitm_hacked_message):
                    mitm_hacked_message = pkcs7_unpad(mitm_hacked_message)
                    break

        assert _msg == mitm_hacked_message


def main():
    malicious_g_attack()


if __name__ == '__main__':
    main()

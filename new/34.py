from addCrypto import aes_cbc_encrypt, aes_cbc_decrypt
from addHash import sha1
from Crypto import Random
from Crypto.Cipher import AES
from 33 import DiffieHellman
from binascii import unhexlify

def parameter_injection_attack(alice, bob):

    A = alice.get_public_key()

    A = alice.p

    B = bob.get_public_key()

    B = bob.p

    _msg = b'Hello, how are you?'
    _a_key = unhexlify(sha1(str(alice.get_shared_secret_key(B)).encode()))[:16]
    _a_iv = Random.new().read(AES.block_size)
    a_question = aes_cbc_encrypt(_msg, _a_key, _a_iv) + _a_iv

    _b_key = unhexlify(sha1(str(bob.get_shared_secret_key(A)).encode()))[:16]
    _a_iv = a_question[-AES.block_size:]
    _a_message = aes_cbc_decrypt(a_question[:-AES.block_size], _b_key, _a_iv)
    _b_iv = Random.new().read(AES.block_size)
    b_answer = aes_cbc_encrypt(_a_message, _b_key, _b_iv) + _b_iv

    mitm_hacked_key = unhexlify(sha1(b'0').encode())[:16]

    mitm_a_iv = a_question[-AES.block_size:]
    mitm_hacked_message_a = aes_cbc_decrypt(a_question[:-AES.block_size], mitm_hacked_key, mitm_a_iv)

    mitm_b_iv = b_answer[-AES.block_size:]
    mitm_hacked_message_b = aes_cbc_decrypt(b_answer[:-AES.block_size], mitm_hacked_key, mitm_b_iv)

    assert _msg == mitm_hacked_message_a == mitm_hacked_message_b


def main():
    alice = DiffieHellman()
    bob = DiffieHellman()
    parameter_injection_attack(alice, bob)


if __name__ == '__main__':
    main()

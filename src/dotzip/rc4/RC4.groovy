package dotzip.rc4

class RC4 {
    private static def message = "HELLO, IT IS RC4!"
    private static final def SBLOCK_LENGTH = 256
    private static final def KEY_MIN_LENGTH = 5
    private byte[] sBlock
    private def i = 0, j = 0

    RC4(byte[] key) {
        if (!(key.length >= KEY_MIN_LENGTH && key.length < SBLOCK_LENGTH)){
            println "Invalid key length! It must be between $KEY_MIN_LENGTH and ${SBLOCK_LENGTH - 1}"
        } else {
            sBlock = initSBlock(key)
        }
    }

    private byte[] initSBlock(byte[] key) {
        sBlock = new byte[SBLOCK_LENGTH]
        for (i = 0; i < SBLOCK_LENGTH; i++) { sBlock[i] = i.byteValue() }

        for (i = 0; i < SBLOCK_LENGTH; i++) {
            j = (j + sBlock[i] + key[i % key.length] + SBLOCK_LENGTH) % SBLOCK_LENGTH
            replace(i, j, sBlock)
        }

        return sBlock
    }

    byte[] encrypt(final byte[] message){
        byte[] code = new byte[message.length];
        for (int k = 0; k < message.length; k++) {
            i = (i + 1) % SBLOCK_LENGTH;
            j = (j + sBlock[i]) % SBLOCK_LENGTH;
            replace(i, j, sBlock)
            int rand = sBlock[(sBlock[i] + sBlock[j]) % SBLOCK_LENGTH]
            code[k] = (byte) (rand ^ (int) message[k])
        }
        return code
    }

    byte[] decrypt(final byte[] message){
        return encrypt(message)
    }

    private void replace(int i, int j, byte[] sBlock) {
        def temp = sBlock[i]; sBlock[i] = sBlock[j]; sBlock[j] = temp
    }

    //private static byte[] asHex(String key){ return key.bytes.encodeHex().toString().bytes }


    static void main(String[] args) {
        try {
            Keygen keygen = new Keygen()
            String key = keygen.getKey(25)

            println "Original message: ${message}"

            //encrypt
            RC4 rce = new RC4(key.bytes)
            byte[] result = rce.encrypt(message.bytes)
            println "Encrypted message: ${result}"

            //decrypt
            RC4 rcd = new RC4(key.bytes)
            def originalMessage = new String(rcd.decrypt(result))
            assert originalMessage == message: "Messages does not matches!"
            println "Decrypted message: $originalMessage"

        } catch (Exception ex){
            println "Something went wrong: ${ex.printStackTrace()}"
        }
    }
}

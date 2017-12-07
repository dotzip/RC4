package dotzip.rc4

def getKey(int keySize){
    def symbols = ['a'..'z', 'A'..'Z', 0..9].each { it.toArray() }.flatten()
    Random rand = new Random()

    def key = new String[keySize]
    for (int i = 0; i < key.size(); i++) {
        key[i] = symbols[rand.nextInt(symbols.size())]
    }
    println "Key: ${key.join()}"
    println "Key size: ${key.size()}"
    return key.join()
}
/**
 * Class to create a dictionary that can hold object with a key/value pair
 */
public class HashDict<K, V> implements IDict<K, V>{
    //private fields
    private HashEntry<K, V>[] bucketArray;
    private int size;
    private static final int DEFAULT_BUCKETS = 16;

    /**
     *Constructor for a HashDict object with a user desired capacity
     * @param intialCapacity The total capacity for the number of buckets available
     */
    public HashDict(int intialCapacity){
        bucketArray = new HashEntry[intialCapacity];
        size = 0;
    }

    /**
     * Constructor for a HashDict object with a default capacity of 16 buckets
     */
    public HashDict(){
        this(DEFAULT_BUCKETS);
    }

    /**
     * Return the size of the number of objects currently held in the dictionary
     * @return Int - The number of objects that are currently stored in the dictionary
     */
    public int size(){
        return size;
    }

    /**
     * Check to see if there are any objects held in the dictionary
     * @return Boolean - True if there are no objects currently held, otherwise false
     */
    public boolean isEmpty(){
        return size == 0;
    }

    /**
     * Get the value currently associated with the input key without removing the value
     * @param key The key to be searched for
     * @return V - The value that is associated with the input key, if key is not present return null
     */
    public V get(K key){
        if(!containsKey(key)){
            return null;
        } else{
            int bucket = hashFunction(key);
            HashEntry<K, V> current = bucketArray[bucket];
            while(!current.key.equals(key)){
                current = current.next;
            }
            return current.value;
        }
    }

    /**
     * Add a key value pair to the dictionary. If the key already exists replace its current value with the input value
     * @param key The key to be searched for
     * @param value The value to be placed with the associated key
     * @return V - The value that was previously held at the associated key, if there was no key previously there return null
     */
    public V put(K key, V value){
        int bucket = hashFunction(key);
        HashEntry<K, V> current = bucketArray[bucket];
        //If there is no value currently in the bucket
        if(current == null){
            bucketArray[bucket] = new HashEntry<K, V>(key, value); size++;
            return null;
        }
        //If the key already exists within the dictionary
        if(containsKey(key)){
            while(!current.key.equals(key)){
                current = current.next;
            }
            V tempValue = current.value;
            current.value = value;
            return tempValue;
        //If the key doesn't already exist and there is something held in the current bucket
        } else{
            bucketArray[bucket] = new HashEntry<K, V>(key, value, current);
            size++;
            return null;
        }
    }

    /**
     * Find the currently matching key/value pair and replace the value held with the input value
     * @param key The key to be searched for
     * @param value The value to replace the current value held at the input key
     * @return V - The value that was previously held at the input key, return null if the key wasn't in the dictionary
     */
    public V replace(K key, V value){
        if(!containsKey(key)){
            return null;
        } else {
            int bucket = hashFunction(key);
            HashEntry<K, V> current = bucketArray[bucket];
            while (current != null) {
                if (current.key.equals(key)) {
                    V returnValue = current.value;
                    current.value = value;
                    return returnValue;
                }
                current = current.next;
            }
        }
        return null;
    }

    /**
     * Find the object that has the associated key and remove it from the dictionary
     * @param key The key to be searched for within the dictionary
     * @return V - The value that was held at the input key, return null if the input key was not in the dictionary
     */
    public V remove(K key){
        V returnValue = null;
        //If the key is not within the dictionary
        if(!containsKey(key)){
            return null;
        } else{
            HashEntry<K, V> temp = null;
            int bucket = hashFunction(key);
            //If the first value in the bucket matches our input key
            if(bucketArray[bucket].key.equals(key)){
                returnValue = bucketArray[bucket].value;
                bucketArray[bucket] = bucketArray[bucket].next;
                size--;
                return returnValue;
            } else{
                //Search the associated bucket for the matching key
                HashEntry<K, V> current = bucketArray[bucket];
                while (current.next != null && !current.next.key.equals(key)){
                    current = current.next;
                }
                if(current.next != null && current.next.key.equals(key)){
                    temp = current.next;
                    returnValue = temp.value;
                    System.out.println("Return value: " + returnValue);
                    current.next = current.next.next;
                    size--;
                }
            }
        }
        return returnValue;
    }

    /**
     * Remove a specific key/value pair, don't remove if the key is within the dictionary but doesn't have the matching value
     * @param key The key to be searched for
     * @param value The value to be checked to see if the key is holding on to
     * @return Boolean - Return true if the key/value pair was removed, otherwise false
     */
    public boolean remove(K key, V value){
        //If the key isn't held in the dictionary return false
        if(!containsKey(key)){
            return false;
        }
        int bucket = hashFunction(key);
        //If the first key/value held in the bucket is the ones we're looking for
        if(bucketArray[bucket].key.equals(key)) {
            if (bucketArray[bucket].value.equals(value)) {
                bucketArray[bucket] = bucketArray[bucket].next;
                size--;
                return true;
            }
        }else {
            //Search the bucket for the matching key/value pair
            HashEntry<K, V> current = bucketArray[bucket];
            while (current.next != null && !current.next.key.equals(key)) {
                current = current.next;
            }
            if (current.next != null && current.next.key.equals(key) && current.next.value.equals(value)) {
                HashEntry temp = current.next;
                current.next = current.next.next;
                size--;
                return true;
            }
        }
        return false;
    }

    /**
     * Check to see if the input value is currently held by any key
     * @param value The value to check if held within any key in the dictionary
     * @return Boolean - Return true if the value is held in any key, otherwise false
     */
    public boolean containsValue(V value){
        for(int i = 0; i < bucketArray.length; i++){
            HashEntry current = bucketArray[i];
            while(current != null){
                if(current.value.equals(value)){
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    /**
     * Check to see if the input key is currently held anywhere within the dictionary
     * @param key The key to check if held within the dictionary
     * @return Boolean - Return true if the key is currently held within the dictionary, otherwise false
     */
    public boolean containsKey(K key){
        int bucket = hashFunction(key);
        HashEntry<K, V> current = bucketArray[bucket];
        while (current != null){
            if(current.key.equals(key)){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Clear the entired contents of the dictionary and reset the size to 0
     */
    public void clear(){
        for(int i = 0; i < bucketArray.length; i++){
            bucketArray[i] = null;
        }
        size = 0;
    }

    /**
     * A string representation of the dictionary in the form of { key:value }
     * @return String - A string representation of the contents of the dictionary
     */
    public String toString(){
        String dataEntries = "{ ";
        for(int i = 0; i < bucketArray.length; i++){
            HashEntry current = bucketArray[i];
            while(current != null){
                dataEntries += current.key + ":" + current.value + " ";
                current = current.next;
            }
        }
        return dataEntries += "}";
    }

    /**
     * Method that gives the proper placement of which bucket to place a specified key using Java's hashCode() method
     * @param key The key to be evaluated and given a proper bucket
     * @return Int - The proper bucket to place the input key
     */
    public int hashFunction(Object key){
        return Math.abs(key.hashCode()) % bucketArray.length;
    }

    /**
     * Private class to instantiate a HashEntry object that acts as a Linked list to be placed in buckets
     * @param <K> The key to be associated with this object
     * @param <V> The value to be associated with this object
     */
    private class HashEntry<K, V>{
        //Private fields
        private K key;
        private V value;
        private HashEntry next;

        /**
         * Constructor for a HashEntry object
         * @param key The key to be associated with this object
         * @param value The value to be associated with this object
         * @param next The HashEntry object to be linked to this HashEntry
         */
        public HashEntry(K key, V value, HashEntry next){
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * Contructor for HashEntry object that takes in a key and value but sets next to null
         * @param key The key to be associated with this object
         * @param value The value to be associated with this object
         */
        public HashEntry(K key, V value){
            this(key, value, null);
        }
    }
}
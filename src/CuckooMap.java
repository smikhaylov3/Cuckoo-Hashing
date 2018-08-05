/**
 * Created by Sergey Mikhaylov.`
 */
import java.util.*;
/** Class instantiating Cuckoo Hash table */
public class CuckooMap<K extends Hashable, V> implements Map<K, V> {
    private class Bucket<K extends Comparable, V> implements Map.Entry<K, V>, Comparable {
        private K key;
        private V value;
        public Bucket(K kee, V val) {
            key = kee;
            value = val;
        }
        public K getKey() {
            return key;
        }
        public V getValue() {
            return value;
        }
        public V setValue(V newVal) {
            V temp = value;
            value = newVal;
            return temp;
        }
        public int compareTo(Object b ) {
            Bucket d = (Bucket)b;
            return (this.key.compareTo(d.key));
        }
        public boolean equals(Bucket b) {
            if (b == null) {
                return false;
            }
            if (!(b instanceof Bucket)) {
                return false;
            } else  {
                if (this.key.equals(b.key)) {
                    if (this.value.equals(b.value)) {
                        return true;
                    }
                }
                return false;
            }
        }
        public int hashCode() {
            return 17 * key.hashCode() + 31 * value.hashCode();
        }
    }
    private Random rand = new Random();
    private int[] primes = {11, 23, 53, 97, 193, 389, 769, 1543, 3079, 6151, 12289, 24593, 49157,
            98317, 196613};
    private int r1;
    private int r2;
    private int numberOfRegrows;
    private Bucket[] tableOne;
    private Bucket[] tableTwo;
    private double loadFactor = .5;
    private int count = 0;
    /** Constuctor for Cuckoo Hash Map */
    public CuckooMap() {
        r1 = rand.nextInt();
        r2 = rand.nextInt();
        numberOfRegrows = 0;
        tableOne = new Bucket[primes[numberOfRegrows]];
        tableTwo = new Bucket[primes[numberOfRegrows]];
    }
    private void regrow() {
        numberOfRegrows++;
        Bucket[] tempTableOne = new Bucket[tableOne.length];
        Bucket[] tempTableTwo = new Bucket[tableTwo.length];
        for (int i = 0; i < tableOne.length; i++) {
            tempTableOne[i] = tableOne[i];
            tempTableTwo[i] = tableTwo[i];
        }
        tableOne = new Bucket[primes[numberOfRegrows]];
        tableTwo = new Bucket[primes[numberOfRegrows]];
        for (int i = 0; i < tempTableOne.length; i++) {
            if (tempTableOne[i] != null) {
                count = count - 1;
                put((K) tempTableOne[i].key, (V) tempTableOne[i].value);
            }
            if (tempTableTwo[i] != null) {
                count = count  - 1;
                put((K) tempTableTwo[i].key, (V) tempTableTwo[i].value);
            }
        }

    }
    private double loadFactor() {
        return count / (double) tableOne.length;
    }

    /**
     * Puts key and value pair into map;
     * @param key Key for map
     * @param value Value for map
     * @return Previous value of occupied slot, null of none
     */
    public V put(K key, V value) {
        if (loadFactor() > loadFactor) {
            regrow();
        }
        if (containsKey(key)) {
            if (tableOne[h1(key)].key.equals(key)) {
                V oldVal = (V) tableOne[h1(key)].value;
                tableOne[h1(key)].value = value;
                return oldVal;
            } else {
                V oldVal = (V) tableTwo[h2(key)].value;
                tableTwo[h2(key)].value = value;
                return oldVal;
            }
        }
        if (key == null || value == null) {
            return null;
        }
        Bucket newBucket = new Bucket(key,value);
        if (tableOne[h1(key)] == null) {
            tableOne[h1(key)] = newBucket;
            count++;
            return null;
        } else {
            Bucket temp;
            if (tableTwo[h2(key)] == null) {
                tableTwo[h2(key)] = newBucket;
                count++;
                return null;
            }
            if (tableOne[h1(key)].getValue().equals(value) && tableOne[h1(key)].getValue() != null) {
                return (V)tableOne[h1(key)].getValue();
            }
            if (tableTwo[h2(key)].getValue().equals(value) && tableTwo[h2(key)].getValue() != null) {
                return (V)tableTwo[h2(key)].getValue();
            } else {
                int x = 0;
                while (x < 2*tableOne.length) {
                    if (tableOne[h1(key)] == null) {
                        tableOne[h1(key)] = newBucket;
                        count++;
                        return (V)tableOne[h1(key)].getValue();
                    }
                    if (tableTwo[h2(key)] == null) {
                        tableTwo[h2(key)] = newBucket;
                        count++;
                        return (V)tableTwo[h2(key)].getValue();
                    } else {
                        temp = tableOne[h1(key)];
                        tableOne[h1(key)] = tableTwo[h2(key)];
                        tableTwo[h2(key)] = temp;
                    }
                    x++;
                }
                regrow();
                return put(key, value);
            }
        }
    }

    /**
     * Copies all values from different map into this.
     * @param m map to copy from
     */
    public void putAll(Map m) {
        Set<Entry> s = m.entrySet();
        for (Entry e : s) {
            put((K)e.getKey(), (V)e.getValue());
        }
    }

    /**
     * Returns set of all map values
     * @return hashset of all map values
     */
    public Set<K> keySet() {
        HashSet keys = new HashSet();
        for(Bucket bucket : tableOne) {
            if (bucket != null) {
                keys.add(bucket.key);
            }
        }
        for(Bucket bucket : tableTwo) {
            if (bucket !=null) {
                keys.add(bucket.key);
            }
        }
        return keys;
    }

    /**
     * Tests if key is in map
     * @param kee Key to be tested
     * @return True if key is in map, false otherwise
     */
    public boolean containsKey(Object kee) {
        K key = (K) kee;
        if (tableOne[h1(key)] != null && tableOne[h1(key)].getKey().equals(key)) {
            return true;
        } else if (tableTwo[h2(key)] != null && tableTwo[h2(key)].getKey().equals(key)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests if value is in map
     * @param value calue to be tested
     * @return True if value is in map, false otherwise
     */
    public boolean containsValue(Object value) {
        ArrayList list = new ArrayList();
        for(Bucket bucket : tableOne) {
            if (bucket != null && bucket.value != null) {
                list.add(bucket.value);
            }
        }
        for(Bucket bucket : tableTwo) {
            if (bucket != null && bucket.value != null) {
                list.add(bucket.value);
            }
        }
        return list.contains(value);
    }

    /**
     * Removes all elements from map
     */
    public void clear() {
        for(int i = 0; i < tableOne.length; i++) {
            tableOne[i] = null;
        }
        for(int i = 0; i < tableOne.length; i++) {
            tableTwo[i] = null;
        }
        count = 0;
    }

    /**
     * Return set of all entries in map
     * @return HashSet of all entries in map
     */
    public Set entrySet() {
        HashSet ents = new HashSet();
        for(Bucket bucket : tableOne) {
            if (bucket != null) {
                ents.add(bucket);
            }
        }
        for(Bucket bucket : tableTwo) {
            if (bucket != null) {
                ents.add(bucket);
            }
        }
        return ents;
    }

    /**
     * Returns list of all values in map
     * @return ArrayList with all values in map
     */
    public ArrayList values() {
        ArrayList list = new ArrayList();
        for(Bucket bucket : tableOne) {
            if (bucket != null) {
                list.add(bucket.value);
            }
        }
        for(Bucket bucket : tableTwo) {
            if (bucket != null) {
                list.add(bucket.value);
            }
        }
        return list;
    }

    /**
     * Checks whether map is empty;
     * @return True if empty, false otherwise
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Removes key t from map
     * @param kee
     * @return Value removed, null if not found
     */
    public V remove(Object kee) {
        if (kee == null) {
            return null;
        } else {
            K key = (K) kee;
            int hash1 = h1(key);
            int hash2 = h2(key);
            if (tableOne[hash1] != null) {
                V val = (V)tableOne[hash1].value;
                tableOne[hash1] = null;
                return val;
            } else if (tableTwo[hash2] != null) {
                V val = (V)tableTwo[hash2].value;
                tableTwo[hash2] = null;
                return val;
            }
            else {
                return null;
            }
        }
    }

    /**
     * Finds and returns value corresponding to key
     * @param kee Key to find value of
     * @return Value corresponding to key, null if not found
     */
    public V get(Object kee) {
        if (kee == null) {
            return null;
        } else {
            K key = (K) kee;
            int hash1 = h1(key);
            int hash2 = h2(key);
            if (tableOne[hash1] != null) {
                return (V)tableOne[hash1].value;
            } else if (tableTwo[hash2] != null) {
                return (V)tableTwo[hash2].value;
            }
            else {
                return null;
            }
        }
    }

    /**
     * Returns number of elements in map
     * @return Integer representing number of elements in map
     */
    public int size() {
        return count;
    }
    private int h1(K key) {
        return key.hash1() % primes[numberOfRegrows];
    }
    private int h2(K key) {
        return key.hash2() % primes[numberOfRegrows];
    }


}

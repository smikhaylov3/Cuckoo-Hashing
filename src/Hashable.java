/**
 * Interface for classes that will be put into a
 * Cuckoo hashtable.  This allows us to ensure that
 * there are 2 hash functions available.
 *
 */
public interface Hashable extends Comparable {
    /**
     * The first hash function to use.  Normally this is a straight
     * wrapper for the .hashCode() function in Java
     *
     * @return the hash of this class using algorithm 1
     */
   int hash1();

    /**
     * The second hash function to use.  This should be an additional hashing function
     * for information so that a different number is generated.
     *
     * @return the hash of this class using algorithm 2
     */
   int hash2();
}

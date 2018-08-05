/**
 * Basically this is just a wrapper class for the Java Integer class
 * It supports two different hash functions and caches the hash values for
 * performance.
 */
public class MyInteger implements Hashable, Comparable {
	int cache1 = 0;
	int cache2 = 0;
	
	Integer myVal;
	
	public MyInteger(Integer i) {
		myVal = i;
	}

	@Override
	public int hash1() {
		cache1 = (cache1 == 0) ? hashCode() : cache1;
		return cache1;
	}

	@Override
	public int hash2() {
		cache2 = (cache2 == 0) ? hashCode() * 893 : cache2;
		return cache2;
	}
	
	@Override
	public int hashCode() {
		return myVal.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		MyInteger other = (MyInteger)o;
		return myVal.equals(other.myVal);
	}
	
	public String toString() {
		return myVal.toString();
	}

	@Override
	public int compareTo(Object o) {
		MyInteger mi = (MyInteger) o;
		return myVal.compareTo(mi.myVal);
	}

}

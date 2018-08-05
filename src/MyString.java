/**
 * Basically this is just a wrapper for the String class so that we can insure that
 * we have 2 hash functions.   It also caches the hash values for future use.
 */
public class MyString implements Hashable {
    String myString;
    int cache1 = 0;
    int cache2 = 0;
    
    public MyString(String s) {
    	myString = s;
    }
    
    @Override
    public int hashCode() {
    	return myString.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
    	MyString s = (MyString) o;
    	return myString.equals(s.myString);
    }
    
	@Override
	public int hash1() {
		cache1 = (cache1 == 0) ? hashCode() : cache1;
		return cache1;
	}

	@Override
	public int hash2() {
		if (cache2 == 0) {
		    String temp = myString + myString;
		    cache2 = temp.hashCode();
		}
		return cache2;
	}
	
	public String toString() {
		return myString.toString();
	}

	@Override
	public int compareTo(Object o) {
		MyString other = (MyString) o;
		return myString.compareTo(other.myString);
	}

}

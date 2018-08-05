import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class CuckooMapTest {
	
	CuckooMap<MyInteger, MyString> map;
	List<MyInteger> intList = new ArrayList<MyInteger>();
	List<MyString> stringList = new ArrayList<MyString>();

    /**
     * We are going to make a couple of arrays of values to use for the map.
     * Default checks are the key is the simple integer and the value is the letter A concatenated to the int
     * for example:   0 -> A0   15 -> A15
     */
	public CuckooMapTest() {
		for (int i = 0; i < 100 ; ++i) {
			intList.add(new MyInteger(i));
			stringList.add(new MyString("A" + i));
		}
	}
	
	@Before
	public void setup () {
		map = new CuckooMap<>();
	}

    /**************
     * size()
     */
	@Test
	public void testSizeEmpty() {
		assertEquals("Size of empty map incorrect", 0, map.size());
	}

    /*******************
     * isEmpty()
     */
	@Test
	public void testIsEmptyEmpty() {
		assertTrue("isEmpty thinks something is there for empty map", map.isEmpty());
	}

    /*********************
     * containsKey()
     */
	@Test
	public void testContainsKeySimple() {
		insertSomeElements(7);
		assertTrue("containsKey did not find entry 4 that was there", map.containsKey(intList.get(4)));
		assertFalse("containsKey found a key (40) that was not there", map.containsKey(intList.get(40)));
		
	}

    /**********************
     * containsValue
     */
	@Test
	public void testContainsValue() {
		insertSomeElements(7);
		assertTrue("containsValue did not find value that was there", map.containsValue(stringList.get(3)));
	}

    /**********************
     * get()
     */
	@Test
	public void testGet() {
		insertSomeElements(7);
		//map.dumpTables();
		assertSame("get did not return correct value for key", stringList.get(5), map.get(intList.get(5)));
		
	}

    /*******************
     * put()
     */
	@Test
	public void testPutOneElement() {
		assertNull("put did not return null for unique entry", map.put(intList.get(0),  stringList.get(0)));
		//map.dumpTables();	
	}

	@Test
	public void testPutMultipleUniqueNoRegrow() {
		insertSomeElements(8);
		assertEquals("put multiple items had wrong size", 8, map.size());
		//map.dumpTables();
	}
	
	@Test
	public void testPutWithDuplicates() {
		insertSomeElements(17);
		assertEquals("put size wrong after multiple puts", 17, map.size());
		MyString rep = new MyString("New Value");
		assertEquals("put did not return old value for insert", stringList.get(10), map.put(intList.get(10), rep));
		assertEquals("put did not replace old value", rep, map.get(intList.get(10)));
		assertEquals("put changed the size when overwriting old value", 17, map.size());
		//map.dumpTables();
	}
	
	@Test
	public void testPutWithRegrow() {
		insertSomeElements(100);
		assertEquals("put size wrong after regrowing", 100, map.size());
		//map.dumpTables();
		
	}

    /**********************
     * remove()
     */
	@Test
	public void testRemoveSimple() {
		insertSomeElements(7);
		assertSame("remove, initial check for item there failed", stringList.get(5), map.get(intList.get(5)));
		assertSame("remove did not return value for removed key", stringList.get(5), map.remove(intList.get(5)));
		assertNull("remove found a removed key", map.get(intList.get(5)));
		
	}

    /***********************
     * putAll()
     */
	@Test
	public void testPutAllSimple() {
		HashMap<MyInteger, MyString> set = new HashMap<>();
		for (int i = 0; i < 7 ; ++i) {
			set.put(intList.get(i), stringList.get(i));
		}
		map.putAll(set);
		//System.out.println(map.keySet());
		assertTrue("putAll a key was missing that should have been there", map.containsKey(intList.get(3)));
		assertTrue("putAll a value was missing that should have been there", map.containsValue(stringList.get(4)));
		
	}

    /*****************
     * clear()
     */
	@Test
	public void testClear() {
		insertSomeElements(7);
		map.clear();
		assertTrue("Clear = map not empty after clear", map.isEmpty());
		assertEquals("Clear = size not zero after clear", 0, map.size());
	}

    /**********************
     * keySet()
     */
	@Test
	public void testKeySetSimple() {
		insertSomeElements(7);
		Set<MyInteger> s = map.keySet();
		//System.out.println(s);
		for (int i = 0; i < 7; ++i) {
			assertTrue("keySet = a key is missing", s.contains(intList.get(i)));
		}
		
	}

    /*************************
     * values()
     */
	@Test
	public void testValuesSimple() {
		insertSomeElements(7);
		Collection<MyString> col = map.values();
		//System.out.println(col);
		for (int i = 0; i < 7; ++i) {
			assertTrue("values = a value is missing", col.contains(stringList.get(i)));
		}
	}

    /*************************
     * entrySet()
     */
	@Test
	public void testEntrySetSimple() {
		insertSomeElements(7);
		Set<Entry<MyInteger, MyString>> s = map.entrySet();
		int index = 0;
		//System.out.println(s);
		for (Entry<MyInteger, MyString> entry : s) {
			assertEquals(intList.get(index), entry.getKey());
			assertEquals(stringList.get(index), entry.getValue());
			++index;
		}
	}

    /***********************
     * A utility function to put some elements into the map
     * @param count the number of items (0 - 100) to insert into the map
     */
	
	private void insertSomeElements(int count) {
		for (int i = 0; i < count ; ++i) {
			map.put(intList.get(i), stringList.get(i));
		}
	}

}

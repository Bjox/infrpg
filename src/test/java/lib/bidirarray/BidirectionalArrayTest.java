package lib.bidirarray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javafx.util.Pair;
import lib.bidirarray.BidirectionalArray;
import static lib.util.Util.zip;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class BidirectionalArrayTest {
	
	private int arraySize = 5;
	private BidirectionalArray<String> array;
	
	public BidirectionalArrayTest() {
	}
	
	@Before
	public void setUp() {
		array = new BidirectionalArray<>(arraySize);
	}
	
	@After
	public void tearDown() {
	}
	
	@Test
	public void testGetSize() {
		IntStream.range(0, 10).forEach(i -> {
			int size = new BidirectionalArray<String>(i).getSize();
			assertEquals(i, size);
		});
	}
	
	@Test
	public void testGetAndSetElements() {
		array.setElement(-arraySize, "negative_end");
		array.setElement(0, "zero");
		array.setElement(arraySize, "positive_end");
		
		String n = array.getElement(-arraySize);
		String z = array.getElement(0);
		String p = array.getElement(arraySize);
		
		assertEquals("negative_end", n);
		assertEquals("zero", z);
		assertEquals("positive_end", p);
	}

	@Test(expected = NegativeArraySizeException.class)
	public void testNegativeInitialSize() {
		new BidirectionalArray<>(-1);
	}
	
	@Test
	public void testForEachElement() {
		List<String> elements = Arrays.asList("a", "b", "c", "d", "e");
		List<Integer> indexes = Arrays.asList(-arraySize, -1, 0, 3, arraySize);
		
		zip(elements, indexes).forEach(e -> array.setElement(e.e2, e.e1));
		
		List<String> checkedElements = new ArrayList<>();
		array.forEachElement((i, e) -> {
			assertTrue(elements.contains(e));
			assertEquals((long)indexes.get(elements.indexOf(e)), i);
			checkedElements.add(e);
		});
		
		assertEquals(elements.size(), checkedElements.size());
	}
	
	@Test
	public void testEnsureCapacity() {
		arraySize *= 2;
		array.ensureCapacity(arraySize);
		
		assertEquals(arraySize, array.getSize());
		testGetAndSetElements();
		testForEachElement();
	}
	
	@Test
	public void testGrow() {
		array.grow(3);
		arraySize += 3;
		
		assertEquals(arraySize, array.getSize());
		testGetAndSetElements();
		testForEachElement();
	}
	
	@Test
	public void testFactory() {
		array = new BidirectionalArray<>(arraySize, i -> String.valueOf(i));
		
		for (int i = -arraySize; i <= arraySize; i++) {
			String v = array.getElement(i);
			assertEquals(String.valueOf(i), v);
		}
	}
	
	@Test
	public void testFactoryGrow() {
		array = new BidirectionalArray<>(arraySize, i -> String.valueOf(i));
		array.grow(3);
		arraySize += 3;
		
		for (int i = -arraySize; i <= arraySize; i++) {
			String v = array.getElement(i);
			assertEquals(String.valueOf(i), v);
		}
	}
	
}

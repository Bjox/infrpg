package test.lib.di;

import lib.di.Container;
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
public class ContainerTests {
	
	public Container container;
	
	public ContainerTests() {
	}
	
	@Before
	public void setUp() {
		container = new Container();
	}
	
	@Test
	public void testRegisterAndResolveInstance() {
		TestClassA testInstance = new TestClassA("val1", "val2");
		container.registerInstance(testInstance);
		
		TestClassA resolvedInstance = container.resolve(TestClassA.class);
		assertNotNull(resolvedInstance);
		assertSame(testInstance, resolvedInstance);
	}
	
	@Test
	public void testRegisterAndResolveType() {
		container.registerType(TestInterfaceB.class, TestClassB.class);
		
		TestInterfaceB resolvedType = container.resolve(TestInterfaceB.class);
		assertNotNull(resolvedType);
		assertTrue(resolvedType instanceof TestClassB);
	}
	
	@Test
	public void testResolveUnregisteredType() {
		String strValue = "test";
		container.registerInstance(strValue);
		
		TestClassA resolvedType = container.resolve(TestClassA.class);
		assertNotNull(resolvedType);
		assertEquals(strValue + strValue, resolvedType.getValue());
	}
	
	@Test
	public void testRegisterAndResolveNestedType() {
		String strValue = "test";
		container.registerInstance(strValue);
		container.registerType(TestInterfaceA.class, TestClassA.class);
		container.registerType(TestInterfaceB.class, TestClassB.class);
		
		TestClassC resolvedType = container.resolve(TestClassC.class);
		assertNotNull(resolvedType);
		assertSame(strValue, resolvedType.str);
		assertNotNull(resolvedType.b);
		assertNotNull(resolvedType.a);
		assertEquals(strValue + strValue, resolvedType.a.getValue());
	}
	
	@Test
	public void testRegisterAndResolveGenericInstance() {
		String str = "test";
		TestClassD<String> testInstance = new TestClassD<>(str);
		container.registerInstance(testInstance);
		
		TestClassD<String> resolvedInstance = container.resolve(TestClassD.class);
		assertNotNull(resolvedInstance);
		assertSame(str, resolvedInstance.value);
	}
	
	@Test
	public void testResolveAndRegisterInstance() {
		String str = "test";
		container.registerInstance(str);
		
		TestClassA resolvedInstance = container.resolveAndRegisterInstance(TestClassA.class);
		assertNotNull(resolvedInstance);
		assertEquals(str + str, resolvedInstance.getValue());
		
		TestClassA resolvedInstance2 = container.resolve(TestClassA.class);
		assertSame(resolvedInstance, resolvedInstance2);
	}
	
	public static interface TestInterfaceA {
		String getValue();
	}
	
	public static class TestClassA implements TestInterfaceA {
		public String value1;
		public String value2;
		
		public TestClassA(String value1, String value2) {
			this.value1 = value1;
			this.value2 = value2;
		}

		@Override
		public String getValue() {
			return value1 + value2;
		}
	}
	
	public static interface TestInterfaceB {
		// Empty
	}
	
	public static class TestClassB implements TestInterfaceB {
		public TestClassB() {
			// Empty
		}
	}
	
	public static class TestClassC {
		public TestInterfaceA a;
		public TestInterfaceB b;
		public String str;
		
		public TestClassC(TestInterfaceA a, TestInterfaceB b, String str) {
			this.a = a;
			this.b = b;
			this.str = str;
		}
	}
	
	public static class TestClassD<T> {
		public T value;
		
		public TestClassD(T value) {
			this.value = value;
		}
	}
}

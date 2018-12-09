package lib.di;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ContainerTest
{

	public Container container;

	public ContainerTest()
	{
	}

	@Before
	public void setUp()
	{
		container = new Container();
	}

	@Test
	public void testRegisterAndResolveInstance()
	{
		TestClassA testInstance = new TestClassA("val1", "val2");
		container.registerInstance(testInstance);

		TestClassA resolvedInstance = container.resolve(TestClassA.class);
		assertNotNull(resolvedInstance);
		assertSame(testInstance, resolvedInstance);
	}

	@Test
	public void testRegisterAndResolveType()
	{
		container.registerType(TestInterfaceB.class, TestClassB.class);

		TestInterfaceB resolvedType = container.resolve(TestInterfaceB.class);
		assertNotNull(resolvedType);
		assertTrue(resolvedType instanceof TestClassB);
	}

	@Test
	public void testResolveUnregisteredType()
	{
		String strValue = "test";
		container.registerInstance(strValue);

		TestClassA resolvedType = container.resolve(TestClassA.class);
		assertNotNull(resolvedType);
		assertEquals(strValue + strValue, resolvedType.getValue());
	}

	@Test
	public void testRegisterAndResolveNestedType()
	{
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
	@SuppressWarnings("unchecked")
	public void testRegisterAndResolveGenericInstance()
	{
		String str = "test";
		TestClassD<String> testInstance = new TestClassD<>(str);
		container.registerInstance(testInstance);

		TestClassD<String> resolvedInstance = container.resolve(TestClassD.class);
		assertNotNull(resolvedInstance);
		assertSame(str, resolvedInstance.value);
	}

	@Test
	public void testResolveAndRegisterInstance()
	{
		String str = "test";
		container.registerInstance(str);

		TestClassA resolvedInstance = container.resolveAndRegisterInstance(TestClassA.class);
		assertNotNull(resolvedInstance);
		assertEquals(str + str, resolvedInstance.getValue());

		TestClassA resolvedInstance2 = container.resolve(TestClassA.class);
		assertSame(resolvedInstance, resolvedInstance2);
	}

	@Test
	public void testResolveTypeWithMultipleConstructors()
	{
		container.registerType(TestInterfaceB.class, TestClassB.class);

		TestClassE resolvedInstance = container.resolve(TestClassE.class);
		assertNotNull(resolvedInstance);
		assertNotNull(resolvedInstance.b);
		assertTrue(resolvedInstance.b instanceof TestClassB);
	}

	@Test
	public void testRegisterSingleton()
	{
		String str = "test";
		container.registerInstance(str);
		container.registerSingleton(TestInterfaceA.class, TestClassA.class);

		TestInterfaceA resolvedInstance = container.resolve(TestInterfaceA.class);
		assertNotNull(resolvedInstance);
		assertEquals(str + str, resolvedInstance.getValue());

		TestInterfaceA anotherResolvedInstance = container.resolve(TestInterfaceA.class);
		assertNotNull(anotherResolvedInstance);
		assertSame(resolvedInstance, anotherResolvedInstance);
	}

	@Test
	public void testRegisterSingletonFromInstance()
	{
		String str = "test";
		TestClassA instance = new TestClassA(str, str);
		container.registerSingleton(TestInterfaceA.class, instance);

		TestInterfaceA resolvedInstance = container.resolve(TestInterfaceA.class);
		assertNotNull(resolvedInstance);
		assertEquals(str + str, resolvedInstance.getValue());
		assertSame(instance, resolvedInstance);

		TestInterfaceA anotherResolvedInstance = container.resolve(TestInterfaceA.class);
		assertNotNull(anotherResolvedInstance);
		assertEquals(str + str, resolvedInstance.getValue());
		assertSame(instance, anotherResolvedInstance);
	}

	@Test
	public void testRegisterTypeAndInstance()
	{
		String str = "test";
		TestClassA instance = new TestClassA(str, str);
		container.registerType(TestInterfaceA.class, TestClassA.class);
		container.registerInstance(instance);

		TestInterfaceA resolvedInstance = container.resolve(TestInterfaceA.class);
		assertNotNull(resolvedInstance);
		assertEquals(str + str, resolvedInstance.getValue());
		assertSame(instance, resolvedInstance);
	}

	@Test
	public void testRegisterSingletonAndInstance()
	{
		String str = "test";
		TestClassA instance = new TestClassA(str, str);
		container.registerSingleton(TestInterfaceA.class, TestClassA.class);
		container.registerInstance(instance);

		TestInterfaceA resolvedInstance = container.resolve(TestInterfaceA.class);
		assertNotNull(resolvedInstance);
		assertEquals(str + str, resolvedInstance.getValue());
		assertSame(instance, resolvedInstance);
	}

	@Test
	public void testRegisterInstanceAndSingleton()
	{
		String str = "test";
		TestClassA instance = new TestClassA(str, str);
		container.registerInstance(instance);
		container.registerSingleton(TestInterfaceA.class, TestClassA.class);

		TestInterfaceA resolvedInstance = container.resolve(TestInterfaceA.class);
		assertNotNull(resolvedInstance);
		assertEquals(str + str, resolvedInstance.getValue());
		assertSame(instance, resolvedInstance);
	}

	@Test
	public void testRegisterSingletonFromType()
	{
		String str = "test";
		container.registerInstance(str);
		container.registerSingleton(TestClassA.class);

		TestClassA resolvedInstance = container.resolve(TestClassA.class);
		assertNotNull(resolvedInstance);
		assertEquals(str + str, resolvedInstance.getValue());

		TestClassA anotherResolvedInstance = container.resolve(TestClassA.class);
		assertNotNull(anotherResolvedInstance);
		assertEquals(str + str, anotherResolvedInstance.getValue());
		assertTrue(resolvedInstance == anotherResolvedInstance);
	}

//	@Test
//	public void testSingletonWithMultipleInterfaces()
//	{
//		String str = "test";
//		container.registerInstance(str);
//		container.registerSingleton(TestInterfaceA.class, TestClassF.class);
//		container.registerSingleton(TestInterfaceB.class, TestClassF.class);
//		
//		TestInterfaceA a = container.resolve(TestInterfaceA.class);
//		assertNotNull(a);
//		assertEquals(str + str, a.getValue());
//		
//		TestInterfaceB b = container.resolve(TestInterfaceB.class);
//		assertNotNull(b);
//		assertTrue(a == b);
//	}

	/*
	Test interfaces and classes
	 */
	public static interface TestInterfaceA
	{
		String getValue();
	}

	public static class TestClassA implements TestInterfaceA
	{
		public String value1;
		public String value2;

		@Inject
		public TestClassA(String value1, String value2)
		{
			this.value1 = value1;
			this.value2 = value2;
		}

		@Override
		public String getValue()
		{
			return value1 + value2;
		}
	}

	public static interface TestInterfaceB
	{
		// Empty
	}

	public static class TestClassB implements TestInterfaceB
	{
		@Inject
		public TestClassB()
		{
			// Empty
		}
	}

	public static class TestClassC
	{
		public TestInterfaceA a;
		public TestInterfaceB b;
		public String str;

		@Inject
		public TestClassC(TestInterfaceA a, TestInterfaceB b, String str)
		{
			this.a = a;
			this.b = b;
			this.str = str;
		}
	}

	public static class TestClassD<T>
	{
		public T value;

		@Inject
		public TestClassD(T value)
		{
			this.value = value;
		}
	}

	public static class TestClassE
	{
		public TestInterfaceB b;

		public TestClassE()
		{
		}

		public TestClassE(int v)
		{
		}

		@Inject
		public TestClassE(TestInterfaceB b)
		{
			this.b = b;
		}

		public TestClassE(String v)
		{
		}
	}
	
	public static class TestClassF implements TestInterfaceA, TestInterfaceB
	{
		public String value1;
		public String value2;

		@Inject
		public TestClassF(String value1, String value2)
		{
			this.value1 = value1;
			this.value2 = value2;
		}
		
		@Override
		public String getValue()
		{
			return value1 + value2;
		}
		
	}
}

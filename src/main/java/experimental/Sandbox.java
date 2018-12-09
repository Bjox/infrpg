package experimental;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Sandbox
{
	public static void main(String[] args)
	{
		for (int i = 2; i <= 4; i++)
		{
			int base = Integer.parseInt("1111", i);
			for (int j = 0; j < i; j++)
			{
				System.out.print(base * j + ", ");
			}
			System.out.println();
		}
	}
}

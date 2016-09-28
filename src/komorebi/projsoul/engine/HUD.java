package komorebi.projsoul.engine;
public class HUD implements Renderable
{
	public int baseHealth = 100;
	//Max health is 250
	public int health = 6;
	public int hundreds = health/100;
	public int tens = (health - hundreds * 100)/ 10; 
	public int ones = (health - (hundreds * 100) - (tens * 10));
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		//Health bar
		Draw.rect(5, 205, health, 10, 53, 24, 54, 25, 2);
		//Border
		Draw.rect(5, 204, baseHealth+2, 2, 39, 46, 40, 47, 2);
		Draw.rect(5, 215, baseHealth+2, 2, 39, 46, 40, 47, 2);
		Draw.rect(5, 205, 2, 10, 39, 46, 40, 47, 2);
		Draw.rect(baseHealth+5, 205, 2, 10, 39, 46, 40, 47, 2);
		// When Player levels up, health must be added to baseHealth as well!
		//Drawing Text
		/*Draw.rect(5, 207, 8, 7, 80, 16, 86, 24, 5);
		Draw.rect(10, 207, 8, 7, 64, 16, 69, 24, 5);
		Draw.rect(17, 207, 8, 7, 64, 16, 69, 24, 5);*/
		if (health>99)
		{
			this.displayHealthHundreds();
		}
		else if (health > 9)
		{
			this.displayHealthTens();
		}
		else
		{
			this.displayHealthOnes();
		}
		
		
	
	}
	public void displayHealthHundreds()
	{
		if (hundreds == 1)
		{
			Draw.rect(7, 207, 8, 7, 80, 16, 86, 24, 5);			
		}
		else if (hundreds == 2)
		{
			Draw.rect(7, 207, 8, 7, 96, 16, 101, 24, 5);
		}
		if (tens == 0)
		{
			Draw.rect(14, 207, 8, 7, 64, 16, 69, 24, 5);
		}
		else if (tens == 1)
		{
			Draw.rect(14, 207, 8, 7, 80, 16, 86, 24, 5);
		}
		else if (tens == 2)
		{
			Draw.rect(14, 207, 8, 7, 96, 16, 101, 24, 5);
		}
		else if (tens == 3)
		{
			Draw.rect(14, 207, 8, 7, 112, 16, 117, 24, 5);
		}
		else if (tens == 4)
		{
			Draw.rect(14, 207, 8, 7, 128, 16, 133, 24, 5);
		}
		else if (tens == 5)
		{
			Draw.rect(14, 207, 8, 7, 144, 16, 149, 24, 5);
		}
		else if (tens == 6)
		{
			Draw.rect(14, 207, 8, 7, 160, 16, 165, 24, 5);
		}
		else if (tens == 7)
		{
			Draw.rect(14, 207, 8, 7, 176, 16, 181, 24, 5);
		}
		else if (tens == 8)
		{
			Draw.rect(14, 207, 8, 7, 0, 32, 5, 40, 5);
		}
		else if (tens == 9)
		{
			Draw.rect(14, 207, 8, 7, 16, 32, 21, 40, 5);
		}
		
		if (ones == 0)
		{
			Draw.rect(21, 207, 8, 7, 64, 16, 69, 24, 5);
		}
		else if (ones == 1)
		{
			Draw.rect(21, 207, 8, 7, 80, 16, 86, 24, 5);
		}
		else if (ones == 2)
		{
			Draw.rect(21, 207, 8, 7, 96, 16, 101, 24, 5);
		}
		else if (ones == 3)
		{
			Draw.rect(21, 207, 8, 7, 112, 16, 117, 24, 5);
		}
		else if (ones == 4)
		{
			Draw.rect(21, 207, 8, 7, 128, 16, 133, 24, 5);
		}
		else if (ones == 5)
		{
			Draw.rect(21, 207, 8, 7, 144, 16, 149, 24, 5);
		}
		else if (ones == 6)
		{
			Draw.rect(21, 207, 8, 7, 160, 16, 165, 24, 5);
		}
		else if (ones == 7)
		{
			Draw.rect(21, 207, 8, 7, 176, 16, 181, 24, 5);
		}
		else if (ones == 8)
		{
			Draw.rect(21, 207, 8, 7, 0, 32, 5, 40, 5);
		}
		else if (ones == 9)
		{
			Draw.rect(21, 207, 8, 7, 16, 32, 21, 40, 5);
		}
		
	}
	public void displayHealthTens()
	{
	
		if (tens == 1)
		{
			Draw.rect(7, 207, 8, 7, 80, 16, 86, 24, 5);
		}
		else if (tens == 2)
		{
			Draw.rect(7, 207, 8, 7, 96, 16, 101, 24, 5);
		}
		else if (tens == 3)
		{
			Draw.rect(7, 207, 8, 7, 112, 16, 117, 24, 5);
		}
		else if (tens == 4)			
		{
			Draw.rect(7, 207, 8, 7, 128, 16, 133, 24, 5);
		}
		else if (tens == 5)
		{
			Draw.rect(7, 207, 8, 7, 144, 16, 149, 24, 5);
		}
		else if (tens == 6)
		{
			Draw.rect(7, 207, 8, 7, 160, 16, 165, 24, 5);
		}
		else if (tens == 7)
		{
			Draw.rect(7, 207, 8, 7, 176, 16, 181, 24, 5);
		}
		else if (tens == 8)
		{
			Draw.rect(7, 207, 8, 7, 0, 32, 5, 40, 5);
		}
		else if (tens == 9)
		{
			Draw.rect(7, 207, 8, 7, 16, 32, 21, 40, 5);
		}
		if (ones == 0)
		{
			Draw.rect(14, 207, 8, 7, 64, 16, 69, 24, 5);
		}
		else if (ones == 1)
		{
			Draw.rect(14, 207, 8, 7, 80, 16, 86, 24, 5);
		}
		else if (ones == 2)
		{
			Draw.rect(14, 207, 8, 7, 96, 16, 101, 24, 5);
		}
		else if (ones == 3)
		{
			Draw.rect(14, 207, 8, 7, 112, 16, 117, 24, 5);
		}
		else if (ones == 4)
		{
			Draw.rect(14, 207, 8, 7, 128, 16, 133, 24, 5);
		}
		else if (ones == 5)
		{
			Draw.rect(14, 207, 8, 7, 144, 16, 149, 24, 5);
		}
		else if (ones == 6)
		{
			Draw.rect(14, 207, 8, 7, 160, 16, 165, 24, 5);
		}
		else if (ones == 7)
		{
			Draw.rect(14, 207, 8, 7, 176, 16, 181, 24, 5);
		}
		else if (ones == 8)
		{
			Draw.rect(14, 207, 8, 7, 0, 32, 5, 40, 5);
		}
		else if (ones == 9)
		{
			Draw.rect(14, 207, 8, 7, 16, 32, 21, 40, 5);
		}
	}
	public void displayHealthOnes()
	{
		if (ones == 0)
		{
			Draw.rect(7, 207, 8, 7, 64, 16, 69, 24, 5);
		}
		else if (ones == 1)
		{
			Draw.rect(7, 207, 8, 7, 80, 16, 86, 24, 5);
		}
		else if (ones == 2)
		{
			Draw.rect(7, 207, 8, 7, 96, 16, 101, 24, 5);
		}
		else if (ones == 3)
		{
			Draw.rect(7, 207, 8, 7, 112, 16, 117, 24, 5);
		}
		else if (ones == 4)
		{
			Draw.rect(7, 207, 8, 7, 128, 16, 133, 24, 5);
		}
		else if (ones == 5)
		{
			Draw.rect(7, 207, 8, 7, 144, 16, 149, 24, 5);
		}
		else if (ones == 6)
		{
			Draw.rect(7, 207, 8, 7, 160, 16, 165, 24, 5);
		}
		else if (ones == 7)
		{
			Draw.rect(7, 207, 8, 7, 176, 16, 181, 24, 5);
		}
		else if (ones == 8)
		{
			Draw.rect(7, 207, 8, 7, 0, 32, 5, 40, 5);
		}
		else if (ones == 9)
		{
			Draw.rect(7, 207, 8, 7, 16, 32, 21, 40, 5);
		}
		
	}
		
		
		
		
		
		
}
	
	
		
	


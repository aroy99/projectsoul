
package komorebi.projsoul.engine;

public class HUD implements Renderable
{
  public int baseHealth;
  //Max health is 250?
  public int health;
  public int hundreds;
  public int tens; 
  public int ones;
  private double healthProportion;


  public HUD(int initHealth)
  {
    baseHealth = health = initHealth; 
  }

  @Override
  public void update() {
    // TODO Auto-generated method stub
    //Updates the player's health as it is lost or gained
    if (health>99)
    {
      this.displayHealthHundreds();
    }
    else if (health > 9)
    {
      this.displayHealthTens();
    }
    else 
      this.displayHealthOnes(); 
  }
  @Override
  public void render() {
    // TODO Auto-generated method stub
    //Health bar
    if (health > 0)
    {
      Draw.rect(5, 205, (int) (health*(100 / (double) baseHealth)), 10, 53, 24, 54, 25, 2);
    }
    //Border
    Draw.rect(5, 204, 102, 2, 39, 46, 40, 47, 2);
    Draw.rect(5, 215, 102, 2, 39, 46, 40, 47, 2);
    Draw.rect(5, 205, 2, 10, 39, 46, 40, 47, 2);
    Draw.rect(105, 205, 2, 10, 39, 46, 40, 47, 2);
    // When Player levels up, health must be added to baseHealth as well!
    //Drawing Text
    if (health>99)
    {
      this.displayHealthHundreds();
    }
    else if (health>9)
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
    hundreds = health / 100;
    tens = (health - (hundreds * 100)) / 10;
    ones = health - ((hundreds * 100) + (tens * 10));
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
    tens = health/10;
    ones = health - (tens * 10); 
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
    ones = health;
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






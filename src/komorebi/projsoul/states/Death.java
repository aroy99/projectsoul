package komorebi.projsoul.states;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.gameplay.HUD;
import komorebi.projsoul.map.Map;
import java.util.Timer;
import java.util.TimerTask;


public class Death extends State
{
	public static boolean playable = true;
	public static boolean death;
	private Animation deathAnimation;
	private Animation deathAni;
	
	public Death()
	{
		deathAnimation = new Animation(2,30,11,false);
		deathAnimation.add(30,245,33,14,1,false);
	}
	@Override
	public void getInput() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub
	}
	@Override
	public void render()
	{
		// TODO Auto-generated method stub
		if (Map.allPlayersDead())
		{
			playable = false;
			death = true;
		}
		if(death)
		{
			deathAnimation.playCam(Map.getPlayer().getX(),Map.getPlayer().getY());
			//Index out of bounds? Need to find out how textIDs work
			
			//Draw.rect((float)0, (float)0, (float)256, (float)244, 0, 0, 256, 244, 0, 13);
			//Draw.rect(5, 150, (int) 20, 15, 53, 24, 54, 25, 0, 2);
			//After 3 seconds this will display the "You died" Graphic
			/*Timer timer = new Timer();
			timer.schedule(new TimerTask(){
				@Override
				public void run()
				{
					Draw.rect(0, 0, 256, 150, 0, 0, 410, 501, 0, 18);
				}
			}, 3000);		   
			   */
		}
	}
}
			
		
				
				
	 

	



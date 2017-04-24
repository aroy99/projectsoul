/**
 * Player.java       May 15, 2016, 11:58:06 PM
 */
package komorebi.projsoul.entities;

import java.awt.Rectangle;
import java.lang.Thread.State;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Keyboard;

import komorebi.projsoul.attack.Attack;
import komorebi.projsoul.attack.AttackInstance;
import komorebi.projsoul.attack.ProjectileAttack;
import komorebi.projsoul.attack.RingOfFire;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Camera;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.HUD;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.Execution;
import komorebi.projsoul.script.Lock;
import komorebi.projsoul.states.Game;
import komorebi.projsoul.states.State.States;

/**
 * @author Aaron Roy
 * @author Andrew Faulkenberry
 */
public abstract class Player extends Entity implements Playable{

	public abstract void levelUp();

	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean run;
	private boolean pause;
	private boolean guiding;
	public static boolean deathStuff = false;

	private boolean dying;
	private boolean dead; 

	public Characters character;

	public boolean isAttacking;

	protected boolean canMove = true;
	protected float dx;
	protected float dy;

	private int framesToGo;
	private boolean hasInstructions;

	public Animation upAni;
	public Animation downAni;
	public Animation leftAni;
	public Animation rightAni;

	public Animation hurtLeftAni;
	public Animation hurtRightAni;
	public Animation hurtUpAni;
	public Animation hurtDownAni;
	public Animation deathAni;
	public Animation characterDeathAni;


	private int hurtCount;

	private Rectangle area;
	protected boolean invincible;
	private boolean restoreMvmtX;
	private boolean restoreMvmtY;

	private static final float SPEED = 1;

	public Face dir = Face.DOWN;    
	private Execution ex;

	private Lock lock;

	public Rectangle future;
	
	public HUD health;

	protected boolean noContact;

	public Attack<? extends AttackInstance> attack1, attack2, attack3;

	/**
	 * @param x x pos, from left
	 * @param y y pos from bottom
	 */
	public Player(float x, float y) {
		super(x, y, 16, 24);
		ent = Entities.CLYDE;

		restoreMvmtX = true;
		restoreMvmtY = true;

		area = new Rectangle((int) x, (int) y, 16, 24);
		future = new Rectangle((int) x, (int) y, 16, 24);

		deathAni = new Animation(4,8,16,21,11,false);
		deathAni.add(0, 57);
		deathAni.add(0, 82);
		deathAni.add(0, 103);
		deathAni.add(0, 124);
	}
	/**
	 * @see komorebi.projsoul.engine.Playable#update()
	 */
	public void getInput(){

		if (canMove)
		{
			up =    Keyboard.isKeyDown(Keyboard.KEY_UP) && 
					!Keyboard.isKeyDown(Keyboard.KEY_DOWN);
			down =  Keyboard.isKeyDown(Keyboard.KEY_DOWN) && 
					!Keyboard.isKeyDown(Keyboard.KEY_UP);
			left =  Keyboard.isKeyDown(Keyboard.KEY_LEFT) && 
					!Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
			right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && 
					!Keyboard.isKeyDown(Keyboard.KEY_LEFT);

			run = KeyHandler.keyDown(Key.Z);
		}

	}

	/**
	 * @see komorebi.projsoul.engine.Renderable#update()
	 */
	@Override
	public void update() {
		health.update();
		if(health.health<=0)
		{
			deathStuff = true;
			if(Map.allPlayersDead())
			{
				GameHandler.switchState(States.DEATH);
			}
		}
		int aniSpeed = 8;
		if(!deathStuff)
		{
			if (canMove) {

				if (!guiding && restoreMvmtX && restoreMvmtY)
				{
					if(left){
						dx = -SPEED;

						if (!isAttacking)
						{
							dir = Face.LEFT;
							leftAni.resume();
						}
					}
					if(right){
						dx = SPEED;
						if (!isAttacking)
						{
							dir = Face.RIGHT;
							rightAni.resume();
						}
					}
					if(!(left || right)){
						dx = 0;
						leftAni.hStop();
						rightAni.hStop();
					}

					if(down){
						dy = -SPEED;
						if (!isAttacking)
						{
							dir = Face.DOWN;
							downAni.resume();
						}
					}
					if(up){
						dy = SPEED;
						if (!isAttacking)
						{
							dir = Face.UP;
							upAni.resume();
						}
					}

					if(!(up || down)){
						dy = 0;
						downAni.hStop();
						upAni.hStop();
					}

					if(run){
						dx *=2;
						dy *=2;
						aniSpeed /=2;
					}
				}
			}

			if (blockedByNpc()[0] || blockedByEnemy()[0])
			{
				dx = 0;
			} 

			if (blockedByNpc()[1] || blockedByEnemy()[1])
			{
				dy = 0;
			}
		}

			/*
      if((up && (left || right)) || (down && (left || right))){
        dx *= Math.sqrt(2)/2;
        dy *= Math.sqrt(2)/2;
        speed = (int)Math.round(speed / (Math.sqrt(2)/2));
      }
			 */



			upAni.setSpeed(aniSpeed);
			downAni.setSpeed(aniSpeed);
			leftAni.setSpeed(aniSpeed);
			rightAni.setSpeed(aniSpeed);
			if(!deathStuff)
			{
				if (invincible)
				{
					hurtCount--;

					if (!restoreMvmtX)
					{
						if (Math.abs(dx)<=0.5 && Math.abs(dx)>=0)
						{
							dx = 0;
							restoreMvmtX = true;
						}
						if (dx>0) dx-=0.5;
						if (dx<0) dx+=0.5;
					}

					if (!restoreMvmtY)
					{
						if (Math.abs(dy)<=0.5 && Math.abs(dy)>=0)
						{
							dy = 0;
							restoreMvmtY = true;
						}
						if (dy>0) dy-=0.5;
						if (dy<0) dy+=0.5;
					}

					if (hurtCount<=0)
					{
						invincible = false;

						switch (dir)
						{
						case DOWN:
							hurtDownAni.hStop();
							downAni.resume();
							break;
						case LEFT:
							hurtLeftAni.hStop();
							leftAni.resume();
							break;
						case RIGHT:
							hurtRightAni.hStop();
							rightAni.resume();
							break;
						case UP:
							hurtUpAni.hStop();
							upAni.resume();
							break;
						default:
							break;

						}
					}
				}
			

			//TODO Debug
			if(!KeyHandler.keyDown(Key.G)){
				Game.getMap().guidePlayer(x, y, dx, dy);
				boolean[] col = Game.getMap().checkCollisions(x,y,dx,dy);

				if(!col[0] || !col[2]){
					dy=0;
					dx*=.75f;
				}
				if(!col[1] || !col[3]){
					dx=0;
					dy*=.75f;
				}
			}

			if(KeyHandler.keyClick(Key.R)){
				x = 100;
				y = 100;
				Camera.center(x, y);
			}

		}else {
			upAni.hStop();
			downAni.hStop();
			leftAni.hStop();
			rightAni.hStop();
		}

		Camera.move(dx, dy);
		x += dx;
		y += dy;

		area.x += dx;
		area.y += dy;

		if (hasInstructions)
		{
			if (dx!=0) 
			{
				framesToGo-=Math.abs(dx);
			} else if (dy != 0)
			{
				framesToGo-=Math.abs(dy);
			} else if (pause)
			{
				framesToGo--;
			}
		}

		//TODO Debug
		if(KeyHandler.keyClick(Key.L)){
			System.out.println("x: "+x+", y: "+y);
		}

		if (hasInstructions&&framesToGo<=0)
		{
			hasInstructions=false;
			dx=0;
			dy=0;
			left = false;
			right = false;
			down = false;
			up = false;
			lock.resumeThread();
		}

		area.x = (int) x;
		area.y = (int) y;

		future.x = (int) x;
		future.y = (int) y;

		guiding = false;

		health.update();
		if(!deathStuff)
		{
			ProjectileAttack.update();
			RingOfFire.updateAll();
		}

		//TODO: Auto-level up
		if (KeyHandler.controlDown() && KeyHandler.keyClick(Key.PLUS))
		{
			levelUp();
		}

		//TODO Stat Dump
		if (KeyHandler.controlDown() && KeyHandler.keyClick(Key.S))
		{
			for (Characters c: Characters.values())
			{
				System.out.println(c + ": ");
				System.out.println("Att: " + Player.getAttack(c)
				+ "\tDef: " + Player.getDefense(c));
				System.out.println("Mag: " + Player.getMaxMagic(c)
				+ "\tHth: " + Player.getMaxHealth(c));
				System.out.println("XP: " + Player.getXP(c) + " / " + 
						Player.getXPToNextLevel(c) + "\tLevel " + Player.getLevel(c)
						+ "\n");
			}
		}

		if (KeyHandler.keyClick(Key.A))
		{
			switchAttack(false);
		} else if (KeyHandler.keyClick(Key.S))
		{
			switchAttack(true);
		}

	}

	/**
	 * @see komorebi.projsoul.engine.Renderable#render()
	 */
	@Override
	public void render() {
		
		if(!deathStuff)
		{
			if (!invincible)
			{
				if (!isAttacking) {
					playWalk();
				} else
				{
					renderAttack();
				}
			} else
			{
				switch (dir)
				{
				case DOWN:
					hurtDownAni.playCam(x, y);
					break;
				case LEFT:
					hurtLeftAni.playCam(x, y);
					break;
				case RIGHT:
					hurtRightAni.playCam(x, y);
					break;
				case UP:
					hurtUpAni.playCam(x, y);
					break;
				default:
					break;
				}
			}

			ProjectileAttack.play();
			RingOfFire.play();
		}
		else
		{
			characterDeathAni.playCam(Map.getPlayer().getX(),Map.getPlayer().getY());
			//This still bombs it out, possible bug with frames? But why collision?
			//Because it returns true, it tries to switch characters outside the bounds of the map.
			//If I put a stop before it checks, no crash but the character still flies off the screen.
			//characterDeathAni.stop();
			//if(characterDeathAni.lastFrame())
			{
				Map.switchPlayer();
				deathStuff = false;	 
			}

		}
	}

	public void pause(int frames, Lock lock)
	{
		framesToGo = frames;

		pause = true;
		hasInstructions = true;

		this.lock = lock;
		this.lock.pauseThread();
	}

	public void walk(Face dir, int tiles)
	{

		hasInstructions=true;
		framesToGo = tiles*16;
		//isMoving=true;
		//isRunning=false;
		this.dir = dir;

		if(!deathStuff)
		{
			switch (dir)
			{
			case DOWN:
				down = true;
				break;
			case LEFT:
				left = true;
				break;
			case RIGHT:
				right = true;
				break;
			case UP:
				up = true;
				break;
			default:
				break;
			}
		}

	}

	public void walk(Face dir, int tiles, Execution ex)
	{
		walk(dir, tiles);
		this.ex = ex;
		this.ex.getLock().pauseThread();    
	}

	public void walk(Face dir, int tiles, Lock lock)
	{
		this.lock = lock;
		walk(dir,tiles);
		this.lock.pauseThread();
	}

	public void align(Face dir, Lock lock)
	{
		this.lock = lock;
		hasInstructions=true;

		this.dir = dir;

		switch (dir)
		{
		case DOWN:
			framesToGo = (int) this.ry - 16*getTileY();
			down = true;
			break;
		case LEFT:
			framesToGo = (int) this.rx - 16*getTileX();
			left = true;
			break;
		case RIGHT:
			framesToGo = (int) (16*getTileX() + 16 - this.rx);
			right = true;
			break;
		case UP:
			framesToGo = (int) (16*getTileY() + 16 - this.ry);
			up = true;
			break;
		default:
			break;
		}

		this.lock.pauseThread();
	}

	public void align(NPC npc, Lock lock)
	{
		Rectangle r = npc.intersectedHitbox(area);
		goToPixX(r.x, lock);
		goToPixY(r.y, lock);
		dir = npc.faceMe(area);
	}

	public void goToPixX(int goTo, Lock lock)
	{
		int distance = goTo - (int) x;
		framesToGo = Math.abs(distance);
		hasInstructions = true;

		if (distance<0)
		{
			left = true;
			dir = Face.LEFT;
			dx = -1;
		} else if (distance>0)
		{
			right = true;
			dir = Face.RIGHT;
			dx = 1;
		}

		this.lock = lock;
		lock.pauseThread();
	}

	public void goToPixY(int goTo, Lock lock)
	{

		int distance = goTo - (int) y;
		framesToGo = Math.abs(distance);
		hasInstructions = true;

		if (distance<0)
		{
			down = true;
			dir = Face.DOWN;
			dy = -1;
		} else if (distance>0)
		{
			up = true;
			dir = Face.UP;
			dy = 1;
		}

		this.lock = lock;
		lock.pauseThread();
	}

	public void turn(Face dir)
	{
		this.dir = dir;
	}

	public void lock(){
		canMove=false;
	}

	public void unlock(){
		canMove=true;
	}

	public int getTileX(){
		return  (int) (x/16);
	}

	public int getTileY(){
		return  (int) (y/16);
	}

	public Face getDirection(){
		return dir;
	}

	public void goTo(boolean horizontal, int tx, Lock lock)
	{
		System.out.println(tx*16 + ", x = " + x);


		if (horizontal)
		{
			if (x>tx*16)
			{
				align(Face.LEFT, lock);
				walk(Face.LEFT, getTileX()-tx);
			} else if (rx<tx*16)
			{
				align(Face.RIGHT, lock);
				walk(Face.RIGHT, tx-getTileX(), lock);
			}
		} else
		{
			if (y>tx*16)
			{
				align(Face.DOWN, lock);
				walk(Face.DOWN, getTileY()-tx, lock);
			} else if (ry<tx*16)
			{
				align(Face.UP, lock);
				walk(Face.UP, tx-getTileY(), lock);
			}
		}

	}

	public void stop()
	{
		dx=0;
		dy=0;
	}

	public Rectangle getArea()
	{
		return area;
	}

	public boolean[] blockedByNpc()
	{
		boolean[] get = new boolean[2];

		future.x += dx;

		for (NPC npc: Game.getMap().getNPCs())
		{
			if (npc.getArea().intersects(future))
			{
				get[0] = true;
			}
		}

		future.x -= dx;
		future.y += dy;

		for (NPC npc: Game.getMap().getNPCs())
		{
			if (npc.getArea().intersects(future))
			{
				get[1] = true;
			}
		}

		future.y -= dy;

		return get;

	}

	public boolean[] blockedByEnemy()
	{
		boolean[] get = new boolean[2];

		future.x += dx;

		for (Enemy enemy: Game.getMap().getEnemies())
		{
			if (enemy.getHitBox().intersects(future))
			{
				get[0] = true;
			}
		}

		future.x -= dx;
		future.y += dy;

		for (Enemy enemy: Game.getMap().getEnemies())
		{
			if (enemy.getHitBox().intersects(future))
			{
				get[1] = true;
			}
		}

		future.y -= dy;

		return get;

	}

	public void guide(int dx, int dy)
	{    
		this.dx = dx;
		this.dy = dy;
		guiding = true;
	}

	public boolean isAttacking()
	{
		return isAttacking;
	}



	public void setDelta(float dx, float dy)
	{
		this.dx = dx;
		this.dy = dy;
	}

	public void overrideImproperMovements()
	{
		if (x+dx<0)
		{
			x = 0;
			dx = 0;
		} else if (x+dx>Game.getMap().getWidth()*16 - sx)
		{
			dx = 0;
			x = Game.getMap().getHeight() * 16 - sx;
		}

		if (y+dy<0)
		{
			dy = 0;
			y = 0;
		} else if (y+dy>Game.getMap().getHeight()*16 - sy)
		{
			dy = 0;
			y = Game.getMap().getHeight() * 16 - sy;
		}
	}

	public Rectangle getHitBox()
	{
		return area;
	}

	public void inflictPain(int attack, float dx, float dy)
	{
		invincible = true;
		restoreMvmtX = false;
		restoreMvmtY = false;

		hurtCount = 40;
		if(!deathStuff)
		{
			switch (dir)
			{
			case DOWN:
				hurtDownAni.resume();
				break;
			case LEFT:
				hurtLeftAni.resume();
				break;
			case RIGHT:
				hurtRightAni.resume();
				break;
			case UP:
				hurtUpAni.resume();
				break;
			default:
				break;

			}

			//System.out.println("Damage = " + attack + " - " + 
			// getDefense(character) + "/2");

			health.health -= (int) (attack - (getDefense(character)/2));

			//Kills the enemy
			if (health.health<=0)
			{
				deathAni.resume();
				dying = true;	
			}

			this.dx = dx;
			this.dy = dy;
		}
	}

	public boolean invincible()
	{
		return invincible;
	}

	public Characters getCharacter()
	{
		return character;
	}

	public void setLocation(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public void renderHUD()
	{
		health.render();
	}

	public abstract void renderAttack();

	public static int getAttack(Characters c)
	{
		switch (c)
		{
		case CASPIAN:
			return Caspian.attack;
		case FLANNERY:
			return Flannery.attack;
		case SIERRA:
			return Sierra.attack;
		case BRUNO:
			return Bruno.attack;
		}

		return 0;
	}

	public static int getDefense(Characters c)
	{
		switch (c)
		{
		case CASPIAN:
			return Caspian.defense;
		case FLANNERY:
			return Flannery.defense;
		case SIERRA:
			return Sierra.defense;
		case BRUNO:
			return Bruno.defense;
		}

		return 0;
	}

	public static int getMaxMagic(Characters c)
	{
		switch (c)
		{
		case CASPIAN:
			return Caspian.maxMagic;
		case FLANNERY:
			return Flannery.maxMagic;
		case SIERRA:
			return Sierra.maxMagic;
		case BRUNO:
			return Bruno.maxMagic;
		}

		return 0;  
	}

	public static int getMaxHealth(Characters c)
	{
		switch (c)
		{
		case CASPIAN:
			return Caspian.maxHealth;
		case FLANNERY:
			return Flannery.maxHealth;
		case SIERRA:
			return Sierra.maxHealth;
		case BRUNO:
			return Bruno.maxHealth;
		}

		return 0;  
	}

	public static int getXP(Characters c)
	{
		switch (c)
		{
		case CASPIAN:
			return Caspian.xp;
		case FLANNERY:
			return Flannery.xp;
		case SIERRA:
			return Sierra.xp;
		case BRUNO:
			return Bruno.xp;
		}

		return 0;  
	}

	public static int getXPToNextLevel(Characters c)
	{
		switch (c)
		{
		case CASPIAN:
			return Caspian.nextLevelUp;
		case FLANNERY:
			return Flannery.nextLevelUp;
		case SIERRA:
			return Sierra.nextLevelUp;
		case BRUNO:
			return Bruno.nextLevelUp;
		}

		return 0;  
	}

	public static int getLevel(Characters c)
	{
		switch (c)
		{
		case CASPIAN:
			return Caspian.level;
		case FLANNERY:
			return Flannery.level;
		case SIERRA:
			return Sierra.level;
		case BRUNO:
			return Bruno.level;
		}

		return 0;  
	}
	public HUD getCharacterHUD(Characters c)
	{
		switch (c)
		{
		case CASPIAN:
			return health;
		case FLANNERY:
			return health;
		case SIERRA:
			return health;
		case BRUNO:
			return health;
		}
		return health;	
	}

	public abstract void giveXP(int xp);

	public void switchAttack(boolean fwd)
	{
		Attack<? extends AttackInstance> temp = attack1;

		if (fwd)
		{
			attack1 = attack2;
			attack2 = attack3;
			attack3 = temp;
		} else
		{
			attack1 = attack3;
			attack3 = attack2;
			attack2 = temp;
		}
	}
	public void playWalk()
	{
		if(!deathStuff)
		{
			switch (dir) {
			case DOWN:
				downAni.playCam(x,y);
				break;
			case UP:
				upAni.playCam(x,y);
				break;
			case LEFT:
				leftAni.playCam(x,y);
				break;
			case RIGHT:
				rightAni.playCam(x,y);
				break;
			default:
				break;
			}
		}
	}

	public int getHealth()
	{
		return health.getHealth();
	}
	


}
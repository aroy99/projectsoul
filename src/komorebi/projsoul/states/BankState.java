package komorebi.projsoul.states;

import komorebi.projsoul.engine.Bank;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.EarthboundFont;
import komorebi.projsoul.script.TextHandler;

public class BankState extends State
{
	private TextHandler text = new TextHandler();
	private Characters character;
	private int count, count2, count3, count4, count5, count6;
	private int hunThou, tenThou, thou, hun, ten;
	private int amount, cash;
	private int index = 0;
	boolean deposit, withdraw = false;
	boolean wrongNums;
	public static Bank bank = new Bank(1000);
	EarthboundFont font = new EarthboundFont(1);
	private int[] nums = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

	public BankState()
	{

	}
	@Override
	public void getInput() 
	{
		
	}

	@Override
	public void update() 
	{
		text.clear();
		amount = ((nums[count]*100000) + (nums[count2]*10000)+ (nums[count3]*1000) + (nums[count4]*100) + (nums[count5]*10) + (nums[count6]));
		character = Map.currentPlayer();
		cash = Map.getPlayer().getCharacterHUD(character).getMoney();
		
		if(!deposit && !withdraw)
		{
			text.write("Bank", 115, 155, font);
			text.write("Would you like to make a Withdrawal or a Deposit?", 20, 140, font);
			text.write("Withdrawal", 40, 110, font);
			text.write("Deposit", 170, 110, font);
			text.write("Exit", 117, 90, font);
		}
		
		if(deposit)
		{
			text.write("Deposit:", 74, 135, font);
			text.write("Deposit", 74, 82, font);
			if(amount > cash)
			{
				text.write("You're trying to deposit more than you have.", 30, 100, font);
				wrongNums = true;
			}
			else
			{
				wrongNums = false;
			}
		}
		else if(withdraw)
		{
			text.write("Withdraw:", 64, 135, font);	
			text.write("Withdraw", 64, 82, font);
			if(amount > bank.getBalance())
			{
				text.write("You don't have enough money to withdraw.", 35, 100, font);	
				wrongNums = true;
			}
			else
			{
				wrongNums = false;
			}
		}
		
		if(deposit || withdraw)
		{
			text.write("Balance:", 70, 115, font);
			text.write("Exit", 225, 82, font);
			text.write("On-Hand:", 70, 150, font);
			//Perhaps implement loops here so this is more efficient?
			//Numbers
			//Balance
			text.write(String.valueOf(hunThou = bank.getBalance()/100000), 111, 115, font);
			text.write(String.valueOf(tenThou = (bank.getBalance()- (hunThou*100000))/10000), 120, 115, font);
			text.write(String.valueOf(thou = (bank.getBalance()- (hunThou*100000) - (tenThou*10000))/1000), 129, 115, font);
			text.write(String.valueOf(hun = (bank.getBalance()- (hunThou*100000) - (tenThou*10000) - (thou*1000))/100), 138, 115, font);				
			text.write(String.valueOf(ten = (bank.getBalance()- (hunThou*100000) - (tenThou*10000) - (thou*1000) - (hun*100))/10), 147, 115, font);
			text.write(String.valueOf(bank.getBalance() - (hunThou*100000) - (tenThou*10000) - (thou*1000) - (hun*100) - (ten*10)), 156, 115, font);
			//On Hand
			text.write(String.valueOf(hunThou = cash/100000), 111, 150, font);
			text.write(String.valueOf(tenThou = (cash- (hunThou*100000))/10000), 120, 150, font);
			text.write(String.valueOf(thou = (cash- (hunThou*100000) - (tenThou*10000))/1000), 129, 150, font);
			text.write(String.valueOf(hun = (cash- (hunThou*100000) - (tenThou*10000) - (thou*1000))/100), 138, 150, font);				
			text.write(String.valueOf(ten = (cash- (hunThou*100000) - (tenThou*10000) - (thou*1000) - (hun*100))/10), 147, 150, font);
			text.write(String.valueOf(cash - (hunThou*100000) - (tenThou*10000) - (thou*1000) - (hun*100) - (ten*10)), 156, 150, font);
			//Deposit/Withdraw
			text.write(String.valueOf(nums[count]), 111, 135, font);
			text.write(String.valueOf(nums[count2]), 120, 135, font);
			text.write(String.valueOf(nums[count3]), 129, 135, font);
			text.write(String.valueOf(nums[count4]), 138, 135, font);
			text.write(String.valueOf(nums[count5]), 147, 135, font);
			text.write(String.valueOf(nums[count6]), 156, 135, font);
		}
		
		if(KeyHandler.keyClick(Key.UP))  
		{
			if(index == 0)if(count != 9)count++;else count = 0;
			else if(index == 1)if(count2 != 9)count2++;else count2 = 0;
			else if(index == 2)if(count3 != 9)count3++;else count3 = 0;
			else if(index == 3)if(count4 != 9)count4++;else count4 = 0;
			else if(index == 4)if(count5 != 9)count5++;else count5 = 0;
			else if(index == 5)if(count6 != 9)count6++;else count6 = 0;
		}
		if(KeyHandler.keyClick(Key.DOWN))
		{
			if(index == 0)if(count != 0)count--;else count = 9;
			else if(index == 1)if(count2 != 0)count2--;else count2 = 9;
			else if(index == 2)if(count3 != 0)count3--;else count3 = 9;
			else if(index == 3)if(count4 != 0)count4--;else count4 = 9;
			else if(index == 4)if(count5 != 0)count5--;else count5 = 9;
			else if(index == 5)if(count6 != 0)count6--;else count6 = 9;
		}
		if(KeyHandler.keyClick(Key.RIGHT))
		{
			index++;
		}
		if(KeyHandler.keyClick(Key.LEFT))
		{
			index--;
		}

		if(KeyHandler.keyClick(Key.ENTER))
		{
			if(!deposit && !withdraw)
			{
				switch(index)
				{
				case 0:
					withdraw = true;
					break;
				case 1:
					deposit = true;
					break;
				case 2:
					deposit = false;
					withdraw = false;
					wrongNums = false;
					count = count2 = count3 = count4 = count5 = count6 = 0;
					GameHandler.switchState(States.GAME);
					break;
				default:
					break;	
				}
				index = 0;
			}
			
			if(deposit || withdraw)
			{
				if(index == 7)
				{
					deposit = false;
					withdraw = false;
					wrongNums = false;
					count = count2 = count3 = count4 = count5 = count6 = 0;
				}
				else if (index == 6)
				{
					if(!wrongNums)
					{
						if(deposit)
						{
							bank.deposit(amount);
							count = count2 = count3 = count4 = count5 = count6 = 0;
						}
						else if(withdraw)
						{
							bank.withdraw(amount);
							count = count2 = count3 = count4 = count5 = count6 = 0;
						}
					}
				}
			}
		}
			//Goes back to 1st menu, QUIT button on menu to go back to game
			//After any transaction go back to menu
	}
	@Override
	public void render() 
	{
		//Draws the border
		Draw.rect(10, 75, 240, 100, 0, 0, 220, 59, 6);
		if(!deposit && !withdraw)
		{
			//Index marker
			if(index == 0)Draw.rect(37, 113, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 1)Draw.rect(167, 113, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 2)Draw.rect(114, 93, 2, 2, 48, 3, 50, 5,5);

			//Check
			if(index > 2)index = 0;
			else if(index < 0) index = 2;
		}
		//Arrows
		if(withdraw)
		{
			Draw.rect(56, 132, 7, 12, 0, 0, 7, 12,16);
			Draw.rect(164, 132, 7, 12, 0, 0, 7, 12,16);
			if(index == 6)Draw.rect(61, 85, 2, 2, 48, 3, 50, 5, 5);
		}
		if(deposit)
		{
			Draw.rect(70, 146, 7, 12, 0, 0, 7, 12, 2, 16);
			Draw.rect(171, 146, 7, 12, 0, 0, 7, 12, 2, 16);
			if(index == 6)Draw.rect(71, 85, 2, 2, 48, 3, 50, 5, 5);
		}
		
		if(deposit || withdraw)
		{
			//Boxes
			//(Deposit/Withdraw)
			for (int i = 110; i<156; i+=9)
			{
				Draw.rect(i, 133, 8, 12, 0, 0, 8, 9, 15);
			}
			//(Balance)
			for (int i = 110; i<156; i+=9)
			{
				Draw.rect(i, 113, 8, 12, 0, 0, 8, 9, 15);
			}
			//(On-Hand)
			for (int i = 110; i<156; i+=9)
			{
				Draw.rect(i, 148, 8, 12, 0, 0, 8, 9, 15);
			}
			
			//Index markers
			if(index == 0)Draw.rect(113, 130, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 1)Draw.rect(122, 130, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 2)Draw.rect(131, 130, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 3)Draw.rect(140, 130, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 4)Draw.rect(149, 130, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 5)Draw.rect(158, 130, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 7)Draw.rect(222, 85, 2, 2, 48, 3, 50, 5,5);
			
			//Line
			Draw.rect(60, 127, 110, 1, 48, 3, 49, 3, 5);
			
			//Check
			if(index > 7)index = 0;
			else if(index < 0)index = 7;
		}
		text.render();
	}

}

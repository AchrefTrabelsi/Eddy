package gameobjects;


import definitions.Definitions;
import gamedata.GameData;
import utilities.Clock;

public class DeceivingBomb extends Bomb {
	private final int animationtime = 100;
	private Clock animationtimer = new Clock();
	private boolean deceiving = false;

	public DeceivingBomb()
	{
		animationtimer.Start();
	}
	public void update()
	{
		if(rect.getY()>Definitions.HEIGHT*1/5)
		{
			deceiving = true;
		}
		if(deceiving)
		{
			if(!GameobjectsState.frozen)
			{
				if(animationtimer.ElapsedTimeinMilliSeconds()>animationtime)
				{
					animationtimer.reset();
					animationtimer.Start();
					animation= (animation+1)%8;
				}
				rect.setY(rect.getY()+speed);
			}
			if(rect.getY()>Definitions.HEIGHT-bombw-10&&!startdecay)
			{
				decaytimer.Start();
				speed=0;
				startdecay=true;
			}
			if(startdecay && decaytimer.ElapsedTimeinMilliSeconds()>decaytime)
			{
				decaytimer.reset();
				decaytimer.Start();
				opacitycounter++;
				alpha-=decayrate;
			}
		}
		else super.update();
		
	}

	public void draw(GameData data) {
		if(deceiving)
		{
			if(opacitycounter<10)
			{
				data.screen.AddImage(Money.images.get(opacitycounter), rect,animation*43,0,42,42);
				if(GameobjectsState.frozen)
					data.screen.AddImage(data.assets.GetImage("Ice"), rect);
			}
		}
		else
		{
			super.draw(data);
		}
		
	}
}


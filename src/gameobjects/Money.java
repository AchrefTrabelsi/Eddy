package gameobjects;

import java.util.LinkedList;
import java.util.Random;

import definitions.Definitions;
import gamedata.GameData;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import utilities.Clock;

public class Money {
	private Rectangle rect;
	private static final Random r = new Random();
	private double speed ;
	public static double ispeed =1.5;
	private final double decayrate =0.1;
	private int opacitycounter =0;
	private double alpha =1;
	private boolean startdecay=false;
	private int animation =0;
	private Clock animationtimer = new Clock();
	private Clock decaytimer = new Clock();
	private final int animationtime = 100;
	private final int decaytime = 80;
	public static final int moneyw=42,moneyh=42;
	public static LinkedList<Image> images = new LinkedList<Image>();
	public Money()
	{
		speed=ispeed;
		rect=new Rectangle(r.nextDouble()*(Definitions.WIDTH-moneyw),-moneyh,moneyw,moneyh);
		animationtimer.Start();
	}
	public void update()
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
		if(rect.getY()>Definitions.HEIGHT-moneyh-10&&!startdecay)
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
	public double getalpha()
	{
		return alpha;
	}
	public boolean hit(Rectangle r)
	{
		if(!startdecay)
		{
			if(r.intersects(rect.getBoundsInParent()))
			{
				decaytimer.Start();
				speed = 0;
				startdecay=true;
				return true;
			}
		}
		return false;
	}

	public Rectangle getrect()
	{
		return rect;
	}
	public void draw(GameData data) {
		if(opacitycounter<10)
		{
			data.screen.AddImage(images.get(opacitycounter), rect,animation*43,0,42,42);
			if(GameobjectsState.frozen)
				data.screen.AddImage(data.assets.GetImage("Ice"), rect);
		}
		
	}
}

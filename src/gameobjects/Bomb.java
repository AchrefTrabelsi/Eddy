package gameobjects;

import java.util.LinkedList;
import java.util.Random;

import definitions.Definitions;
import gamedata.GameData;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import utilities.Clock;

public class Bomb {
	protected Rectangle rect;
	private static final Random r = new Random();
	protected double speed ;
	public static  double ispeed =1.5;
	protected final double decayrate =0.1;
	protected int opacitycounter =0;
	protected double alpha =1;
	protected boolean startdecay=false;
	protected int animation =0;
	protected Clock decaytimer = new Clock();
	protected final int decaytime = 80;
	public static final int bombw=42,bombh=46;
	public static LinkedList<Image> images = new LinkedList<Image>();
	public Bomb()
	{
		speed=ispeed;
		rect=new Rectangle(r.nextDouble()*(Definitions.WIDTH-bombw),-bombh,bombw,bombw);
	}
	public void update()
	{
		if(!GameobjectsState.frozen)
			rect.setY(rect.getY()+speed);
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
			data.screen.AddImage(images.get(opacitycounter),rect.getX(),rect.getY(),bombw,bombh,0,0,bombw,bombh);
			if(GameobjectsState.frozen)
				data.screen.AddImage(data.assets.GetImage("Ice"), rect.getX(),rect.getY()+4);
		}

		
	}






}

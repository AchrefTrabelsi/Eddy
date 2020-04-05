package states;

import java.util.Random;
import java.util.Vector;

import definitions.Definitions;
import gamedata.GameData;
import gameobjects.Bomb;
import gameobjects.DeceivingBomb;
import gameobjects.GameobjectsState;
import gameobjects.Ice;
import gameobjects.Money;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import utilities.Clock;
public class GameState extends State{
	
	private class animation
	{
		private long earlier =0;
		private long duration =0;
		private final double animation = 1000000000/Definitions.FPS;
		private double x =-1;
		private final double dx =0.007;
		private AnimationTimer stageanimation;
		animation()
		{		
			stageanimation = new AnimationTimer() {
				@Override
				public void handle(long arg0) {
					if(earlier==0)
					{
						earlier=arg0;
						return;
					}
					duration += arg0-earlier;
					earlier=arg0;
					while(duration>=animation)
					{
						duration-=animation;
						x+=dx;
					}
					if(x>=1)
					{
						this.stop();
						return;
					}
					double p = 1-x*x;
					data.screen.GetGfx().setFill(new Color(1,1,1,p));
					data.screen.GetGfx().setFont(new Font(60));
					data.screen.GetGfx().setTextAlign(TextAlignment.CENTER);
					data.screen.GetGfx().fillText("Level : "+Level, Definitions.WIDTH/2,60);
	
				}
			};
		}
		public void Init()
		{
			earlier =0;
			duration =0;
			x=-1;
		}
		public void Start()
		{
			stageanimation.start();
		}
		public void Stop()
		{
			stageanimation.stop();
		}
	}
	
	private static int Level=0;
	private static int laststagescore = 0;
	private static int newstagescore = 1000;
	private static double increaserate =0.1;
	private GameData data;
	private Rectangle rect;
	private Random MoneyorBomb = new Random();
	private Random BombType = new Random();
	private Random Perk = new Random();
	private static double bombprobabilty =0.15;
	private static double deceivingbombprobabilty =0.2;
	private static double iceprobabilty =0.05;
	private Clock frozentimer = new Clock();
	private final double frozentime = 2;
	private final int floor =(int) (Definitions.HEIGHT*7/8);
	private final Rectangle floorrect = new Rectangle(0,floor,Definitions.WIDTH,Definitions.HEIGHT-floor);
	private double vx,vy;
	private Vector<Ice> VI = new Vector<Ice>();
	private Vector<Bomb> VB = new Vector<Bomb>();
	private Vector<Money> VM = new Vector<Money>();
	private final double jump = 10;
	private final double Fontsize = 20;
	private boolean candouble=false;
	private final double textposx = Definitions.WIDTH-100, textposy =40;
	private final  double  gravity = jump/25;
	private final double speed =4;
	private final double doublespeed = 500;
	private boolean  up=false;
	private boolean right=false;
	private int direction =1;
	private boolean left=false;
	private boolean down=false;
	private int Score=0;
	private int jumpnbr =0;
	private double Spawnrate =1;
	private int animation =1;
	private Clock spawnclock = new Clock();
	private  Clock keyheld = new Clock();
	private final double playerwidth = 14*3*1.2;
	private final double playerheight = 36*3*1.2;
	private Clock animationtimer = new Clock();
	private final int animationtime = 65;
	private Rectangle hitbox = new Rectangle(Definitions.WIDTH/2,floor-playerheight,playerwidth,playerheight);
	private animation st = new animation();

	public GameState(GameData data) {
		GameobjectsState.frozen=false;
		this.data=data;
		rect= new Rectangle(Definitions.WIDTH/2,floor-playerheight,playerwidth,playerheight);
		spawnclock.Start();
		Definitions.stage.addEventFilter(KeyEvent.ANY,e -> {
			if(e.getEventType()==KeyEvent.KEY_PRESSED)
			{
				if(e.getCode()==KeyCode.UP &&!up)
				{
					up=true;
				}
				else if(e.getCode()==KeyCode.LEFT && !left)
				{
					keyheld.reset();
					keyheld.Start();
					left=true;
					direction =-1;
				}
				else if(e.getCode()==KeyCode.RIGHT &&!right)
				{
					keyheld.reset();
					keyheld.Start();
					right=true;
					direction =1;
				}
				else if(e.getCode()==KeyCode.DOWN && !down)
				{
					down=true;
				}
			}
			else if(e.getEventType()==KeyEvent.KEY_RELEASED)
			{
				if(e.getCode()==KeyCode.UP && up)
				{
					up=false;
					candouble=true;
				}

				if(e.getCode()==KeyCode.LEFT && left)
				{
					left=false;
				}
				else if(e.getCode()==KeyCode.RIGHT&& right)
				{
					right=false;
				}

			}

		});
		animationtimer.Start();
	}
	private void Spawn()
	{
		if(spawnclock.ElapsedTimeinSeconds()*Spawnrate>1)
		{
			if(Perk.nextDouble()<iceprobabilty)
			{
				VI.add(new Ice());
			}
			if(MoneyorBomb.nextDouble()>bombprobabilty)
				VM.add(new Money());
			else
			{
				if(BombType.nextDouble()>deceivingbombprobabilty)
					VB.add(new Bomb());
				else
					VB.add(new DeceivingBomb());
			}
			spawnclock.reset();
			spawnclock.Start();
		}

	}
	private void Collision()
	{
		for(int i=0;i<VB.size();i++)
		{
			VB.elementAt(i).update();
			if(VB.elementAt(i).hit(hitbox))
			{
				Score-=500;
				if(Score<0)
					Score=0;
			}
			if(VB.elementAt(i).getalpha()<=0)
			{
				VB.remove(i);
				i--;
			}
		}
		for(int i=0;i<VM.size();i++)
		{
			VM.elementAt(i).update();
			if(VM.elementAt(i).hit(hitbox))
			{
				Score+=100;
			}
			if(VM.elementAt(i).getalpha()<=0)
			{
				VM.remove(i);
				i--;
			}
		}
		for(int i=0;i<VI.size();i++)
		{
			VI.elementAt(i).update();
			if(VI.elementAt(i).hit(hitbox))
			{
				GameobjectsState.frozen=true;
				Score+=1000;
				frozentimer.Start();
			}
			if(VI.elementAt(i).getalpha()<=0)
			{
				VI.remove(i);
				i--;
			}
		}
		if(frozentimer.ElapsedTimeinSeconds()>=frozentime)
		{
			frozentimer.reset();
			GameobjectsState.frozen=false;
		}

	}

	@Override
	public void Update() {
		difficultyupdate();
		vx=0;
		Collision();
		if(!GameobjectsState.frozen)
			Spawn();

		if(up && jumpnbr==0)
		{
			hitbox.setHeight(hitbox.getHeight()*11/12);
			jumpnbr=1;
			vy=-jump;
		}
		if(up && jumpnbr==1 && (vy>=0 || candouble))
		{
			jumpnbr=2;
			vy=-jump;
		}
		if(down)
		{
			vy+=1;
			jumpnbr=2;
		}
		if(left && vx>=0)
		{
			vx=-speed;
		}
		if(right && vx<=0)
		{
			vx=speed;
		}
		if(keyheld.ElapsedTimeinMilliSeconds()>=doublespeed)
		{
			vx+=vx;
		}
		rect.setX(rect.getX()+vx);
		rect.setY(rect.getY()+vy);
		
		if(rect.getY()<floor-rect.getHeight())
		{
			vy+=gravity;
		}
		if(vy>0 && rect.getY()>=floor-rect.getHeight())
		{
			rect.setY(floor-rect.getHeight());
			vy=0;
			jumpnbr=0;
			down=false;
			candouble=false;
			hitbox.setHeight(rect.getHeight());

		}
		if(rect.getX()>Definitions.WIDTH-rect.getWidth())
		{
			rect.setX(Definitions.WIDTH-rect.getWidth());
		}
		else if(rect.getX()<0)
		{
			rect.setX(0);
		}
		hitbox.setX(rect.getX());
		hitbox.setY(rect.getY());
		playeranimation();
		
	}
	void playeranimation()
	{
		if(animationtimer.ElapsedTimeinMilliSeconds()>this.animationtime || vx*animation<0)
		{
			animationtimer.reset();
			animationtimer.Start();
			if(vx>0)
			{
				if(animation<0)
				{
					animation =1;
				}
				else
				{
					animation = (animation+1)%5+1;
				}
			}
			else if(vx<0)
			{
				if(animation >0)
				{
					animation =-1;
				}
				else
				{
					animation = (animation-1)%5-1;
				}
			}
		}

	}

	@Override
	public void Draw() {
		data.screen.ClearScreen();
		data.screen.GetGfx().setFill(Color.AQUA);
		data.screen.GetGfx().fillRect(0, 0, Definitions.WIDTH, Definitions.HEIGHT);
		data.screen.AddImage(data.assets.GetImage("Floor"), floorrect);
		for(Ice m : VI)
		{
			m.draw(data);
		}
		for(Money m : VM)
		{
			m.draw(data);
		}
		for(Bomb m : VB)
		{
			m.draw(data);
		}
		drawplayer();
		data.screen.GetGfx().setFill(Color.BLACK);
		data.screen.GetGfx().setFont(new Font(Fontsize));
		data.screen.GetGfx().setTextAlign(TextAlignment.LEFT);
		data.screen.GetGfx().fillText(""+Score, textposx, textposy);
	}

	@Override
	public void Init() {
	}
	private void drawplayer()
	{
		if(jumpnbr==1)
		{
			
			if(direction==1)
			{
				data.screen.AddImage(data.assets.GetImage("Playerjumpright"), rect);

			}
			else
			{
				data.screen.AddImage(data.assets.GetImage("Playerjumpleft"), rect);
			}
		}
		else if(jumpnbr==2)
		{
			
			if(direction==1)
			{
				data.screen.AddImage(data.assets.GetImage("Playerjumpright2"), rect);

			}
			else
			{
				data.screen.AddImage(data.assets.GetImage("Playerjumpleft2"), rect);
			}
		}

		else
		{
			if(vx>0)
			{
				data.screen.AddImage(data.assets.GetImage("Playerright"), rect,new Rectangle(animation*8*6,0,7*6,18*6) );
			}
			else if(vx<0)
			{
				data.screen.AddImage(data.assets.GetImage("Playerleft"), rect,new Rectangle(animation*8*6+40*6,0,7*6,18*6) );
			}
			else
			{
				if(animation>0)
					data.screen.AddImage(data.assets.GetImage("Playerright"), rect,new Rectangle(0,0,7*6,18*6) );
				else
					data.screen.AddImage(data.assets.GetImage("Playerleft"), rect,new Rectangle(40*6,0,7*6,18*6) );
	
			}
		}

	}
	private int calculate(int x)
	{
		int s=0;
		for(int i=1;x>0;i++)
		{
			for(int j=0;j<i && x>0;j++)
			{
				s+=i;
				x--;
			}
		}
		return s;
	}
	private void difficultyupdate()
	{
		if(Score>=newstagescore)
		{
			st.Stop();
			st.Init();
			st.Start();
			Level++;
			laststagescore=newstagescore;
			newstagescore=calculate(Level+1)*1000;
			bombprobabilty =bombprobabilty*(1+increaserate);
			deceivingbombprobabilty = deceivingbombprobabilty*(1+increaserate*2);
			iceprobabilty =iceprobabilty*(1+increaserate/2);
			Spawnrate=Spawnrate*(1+increaserate);
			Ice.ispeed=Ice.ispeed*(1+increaserate);
			Bomb.ispeed=Bomb.ispeed*(1+increaserate);
			Money.ispeed=Money.ispeed*(1+increaserate);
		}
		else if(Score<laststagescore)
		{
			st.Stop();
			st.Init();
			st.Start();
			Level--;
			newstagescore=laststagescore;
			laststagescore=calculate(Level)*1000;
			bombprobabilty =bombprobabilty/(1+increaserate);
			deceivingbombprobabilty = deceivingbombprobabilty/(1+increaserate*2);
			iceprobabilty =iceprobabilty/(1+increaserate/2);
			Spawnrate=Spawnrate/(1+increaserate);
			Ice.ispeed=Ice.ispeed/(1+increaserate);
			Bomb.ispeed=Bomb.ispeed/(1+increaserate);
			Money.ispeed=Money.ispeed/(1+increaserate);

		}
	}

}

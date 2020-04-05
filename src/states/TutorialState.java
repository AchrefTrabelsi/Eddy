package states;


import definitions.Definitions;
import gamedata.GameData;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import utilities.Clock;
public class TutorialState extends State{
	
	private GameData data;
	private Rectangle rect;
	private final int floor =(int) (Definitions.HEIGHT*7/8);
	private final Rectangle floorrect = new Rectangle(0,floor,Definitions.WIDTH,Definitions.HEIGHT-floor);
	private double vx,vy;
	private final double jump = 10;
	private boolean candouble = false;
	private final  double  gravity = jump/25;
	private final double speed =4;
	private final double doublespeed = 500;
	private boolean  up=false;
	private boolean right=false;
	private int direction =1;
	private boolean left=false;
	private boolean down=false;
	private int jumpnbr =0;
	private int tright =0;
	private int tleft =0;

	private Clock Arrowstimer = new Clock();
	private boolean arrow = true;
	private final int arrowtime = 600;
	private int animation =1;
	private  Clock keyheld = new Clock();
	private final double playerwidth = 14*3*1.2;
	private final double playerheight = 36*3*1.2;
	private Clock animationtimer = new Clock();
	private final int animationtime = 65;
	private final int font = 30;
	private int t =0;
	private EventHandler<KeyEvent> evthandler;
	private final String tuto[]= {
			"Press right and left arrow\n keys to move left and right",
			"Press up arrow key to jump \n Press it twice/hold it to double jump",
			"Press down arrow key to fall down faster"
	};

	public TutorialState(GameData data) {
		this.data=data;
		data.screen.GetGfx().setFont(new Font(font));
		data.screen.GetGfx().setTextAlign(TextAlignment.CENTER);
		rect= new Rectangle(Definitions.WIDTH/2,floor-playerheight,playerwidth,playerheight);
		evthandler= new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if(e.getEventType()==KeyEvent.KEY_PRESSED)
				{
					if(e.getCode()==KeyCode.ENTER && t==3)
					{
						Definitions.stage.removeEventFilter(KeyEvent.ANY, this);
						data.machine.AddState(new GameState(data));
					}
				}
				if(e.getEventType()==KeyEvent.KEY_PRESSED)
				{
					if(e.getCode()==KeyCode.UP &&!up)
					{
						up=true;
					}
					else if(e.getCode()==KeyCode.LEFT && !left)
					{
						tleft=1;
						keyheld.reset();
						keyheld.Start();
						left=true;
						direction =-1;
					}
					else if(e.getCode()==KeyCode.RIGHT &&!right)
					{
						tright=1;
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
			}
		};
		Definitions.stage.addEventFilter(KeyEvent.ANY,evthandler);
		animationtimer.Start();
		Arrowstimer.Start();
	}
	@Override
	public void Update() {
		if(t==0&& tright==1 && tleft ==1 )
		{
			t=1;
		}
		vx=0;
		if(up && jumpnbr==0)
		{
			jumpnbr=1;
			vy=-jump;
		}
		if(up && jumpnbr==1 && (vy>=0 || candouble))
		{
			if(t==1)
				t=2;
			jumpnbr=2;
			vy=-jump;
		}
		if(down )
		{
			if(t==2 && jumpnbr!=0)
				t=3;
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

		}
		if(rect.getX()>Definitions.WIDTH-rect.getWidth())
		{
			rect.setX(Definitions.WIDTH-rect.getWidth());
		}
		else if(rect.getX()<0)
		{
			rect.setX(0);
		}
		playeranimation();
		if(Arrowstimer.ElapsedTimeinMilliSeconds()>arrowtime)
		{
			arrow=!arrow;
			Arrowstimer.reset();
			Arrowstimer.Start();
		}
		
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
		drawplayer();
		if(t<3)
		{
			if(arrow)
				data.screen.AddImage(data.assets.GetImage("ArrowKeys"+t),Definitions.WIDTH/2-80,40);
			else
				data.screen.AddImage(data.assets.GetImage("ArrowKeys"),Definitions.WIDTH/2-80,40);
			data.screen.GetGfx().setFill(Color.BLACK);
			data.screen.GetGfx().fillText(tuto[t], Definitions.WIDTH/2, 40+104+font+10);

		}
		else
		{
			data.screen.GetGfx().setFill(Color.BLACK);
			data.screen.GetGfx().fillText("CONGRATULATIONS \n You finished the tutorial \n Press Enter to continue", Definitions.WIDTH/2, 40+104+font+10);

		}
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

}

package states;
import definitions.Definitions;
import gamedata.GameData;
import gameobjects.Bomb;
import gameobjects.Ice;
import gameobjects.Money;
import utilities.Clock;

public class SplashState extends State {
	private GameData data;
	private boolean loaded = false;
	public SplashState(GameData data)
	{
		this.data = data;
		data.assets.LoadImage("Logo", Definitions.PATH_LOGO);
	}

	@Override
	public void Update() {
		if(!loaded)
		{
			Clock timer = new Clock();
			timer.Start();
			data.assets.LoadImage("ArrowKeys", Definitions.PATH_ARROWKEYS);
			data.assets.LoadImage("ArrowKeys0", Definitions.PATH_ARROWKEYS0);
			data.assets.LoadImage("ArrowKeys1", Definitions.PATH_ARROWKEYS1);
			data.assets.LoadImage("ArrowKeys2", Definitions.PATH_ARROWKEYS2);
			data.assets.LoadImage("Ice", Definitions.PATH_ICE);
			data.assets.LoadImage("Floor", Definitions.PATH_FLOOR);
			data.assets.LoadImage("Playerright", Definitions.PATH_PLAYER_RIGHT);
			data.assets.LoadImage("Playerleft", Definitions.PATH_PLAYER_LEFT);
			data.assets.LoadImage("Playerjumpleft", Definitions.PATH_PLAYER_JUMP_LEFT);
			data.assets.LoadImage("Playerjumpright", Definitions.PATH_PLAYER_JUMP_RIGHT);
			data.assets.LoadImage("Playerjumpleft2", Definitions.PATH_PLAYER_JUMP_LEFT2);
			data.assets.LoadImage("Playerjumpright2", Definitions.PATH_PLAYER_JUMP_RIGHT2);
			data.assets.LoadImage("Ice0", Definitions.PATH_ICE0);
			data.assets.LoadImage("Ice1", Definitions.PATH_ICE0_09);
			data.assets.LoadImage("Ice2", Definitions.PATH_ICE0_08);
			data.assets.LoadImage("Ice3", Definitions.PATH_ICE0_07);
			data.assets.LoadImage("Ice4", Definitions.PATH_ICE0_06);
			data.assets.LoadImage("Ice5", Definitions.PATH_ICE0_05);
			data.assets.LoadImage("Ice6", Definitions.PATH_ICE0_04);
			data.assets.LoadImage("Ice7", Definitions.PATH_ICE0_03);
			data.assets.LoadImage("Ice8", Definitions.PATH_ICE0_02);
			data.assets.LoadImage("Ice9", Definitions.PATH_ICE0_01);
			data.assets.LoadImage("Money0", Definitions.PATH_MONEY);
			data.assets.LoadImage("Money1", Definitions.PATH_MONEY_09);
			data.assets.LoadImage("Money2", Definitions.PATH_MONEY_08);
			data.assets.LoadImage("Money3", Definitions.PATH_MONEY_07);
			data.assets.LoadImage("Money4", Definitions.PATH_MONEY_06);
			data.assets.LoadImage("Money5", Definitions.PATH_MONEY_05);
			data.assets.LoadImage("Money6", Definitions.PATH_MONEY_04);
			data.assets.LoadImage("Money7", Definitions.PATH_MONEY_03);
			data.assets.LoadImage("Money8", Definitions.PATH_MONEY_02);
			data.assets.LoadImage("Money9", Definitions.PATH_MONEY_01);
			data.assets.LoadImage("Bomb0", Definitions.PATH_BOMB);
			data.assets.LoadImage("Bomb1", Definitions.PATH_BOMB_09);
			data.assets.LoadImage("Bomb2", Definitions.PATH_BOMB_08);
			data.assets.LoadImage("Bomb3", Definitions.PATH_BOMB_07);
			data.assets.LoadImage("Bomb4", Definitions.PATH_BOMB_06);
			data.assets.LoadImage("Bomb5", Definitions.PATH_BOMB_05);
			data.assets.LoadImage("Bomb6", Definitions.PATH_BOMB_04);
			data.assets.LoadImage("Bomb7", Definitions.PATH_BOMB_03);
			data.assets.LoadImage("Bomb8", Definitions.PATH_BOMB_02);
			data.assets.LoadImage("Bomb9", Definitions.PATH_BOMB_01);

			for(int i=0;i<10;i++)
			{			
				Bomb.images.add(data.assets.GetImage("Bomb"+i));
				Money.images.add(data.assets.GetImage("Money"+i));
				Ice.images.add(data.assets.GetImage("Ice"+i));
			}

			timer.Pause();
			double time = timer.ElapsedTimeinSeconds();
			if(time<Definitions.SPLASH_STATE_TIME)
			{
				try {
					Thread.sleep((long) ((Definitions.SPLASH_STATE_TIME-time)*1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			data.machine.AddState(new TutorialState(data));
			loaded=true;
		}
		
	}

	@Override
	public void Draw() {
		data.screen.ClearScreen();
		data.screen.GetGfx().fillRect(0, 0, 800, 600);
		data.screen.AddImage(data.assets.GetImage("Logo"), 0, 0, 800, 600);
		
	}

	@Override
	public void Init() {
		
	}

}

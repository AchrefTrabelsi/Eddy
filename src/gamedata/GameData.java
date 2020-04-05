package gamedata;


public class GameData {
	public  Display screen;
	public AssetManager assets;
	public StateMachine machine;
	public GameData(int width,int height)
	{
		screen = new Display(width,height);
		assets= new AssetManager();
		machine = new StateMachine();

	}

}

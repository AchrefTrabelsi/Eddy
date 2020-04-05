package gamedata;

import java.util.HashMap;

import javafx.scene.image.Image;

public class AssetManager {
	private HashMap<String,Image> Assets;
	public AssetManager()
	{
		Assets = new HashMap<String,Image>();
	}
	public void LoadImage(String name,String Path)
	{
		Assets.put(name, new Image(AssetManager.class.getResourceAsStream("/textures/"+Path)));
	}
	public Image GetImage(String name)
	{
		return Assets.get(name);
	}
}

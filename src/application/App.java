package application;
	
import definitions.Definitions;
import game.Game;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class App extends Application {
	private Game game;
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{	
			primaryStage.setTitle("Eddy");
			primaryStage.setResizable(false);
			Definitions.stage=primaryStage;
			BorderPane root = new BorderPane();
			Definitions.root=root;
			game = new Game((int)Definitions.WIDTH,(int)Definitions.HEIGHT);
			Scene scene = new Scene(root,Definitions.WIDTH,Definitions.HEIGHT);
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.setX((Screen.getPrimary().getVisualBounds().getWidth()-800)/2);
			primaryStage.setY((Screen.getPrimary().getVisualBounds().getHeight()-600)/2);
			primaryStage.show();
			game.start();
		} catch(Exception e) 
		{
			e.printStackTrace();
			stop();
		}
	}
	@Override
	public void stop(){
		System.exit(80);
	}

	
	public static void main(String[] args) {
		launch(args);

	}
}

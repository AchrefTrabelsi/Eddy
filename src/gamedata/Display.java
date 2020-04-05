package gamedata;




import java.util.concurrent.CountDownLatch;

import definitions.Definitions;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

public class Display {
	private  Canvas canvas;
	private  double Width,Height;
	private  GraphicsContext  Gfx;
	public Display(double width , double height)
	{
		
		this.Width = width;
		this.Height = height;
		this.CreateDisplay();
		Gfx = canvas.getGraphicsContext2D();
	}
	private void CreateDisplay()
	{
		canvas = new Canvas(Width,Height);
		Definitions.root.getChildren().add(canvas);
	}
	public void AddImage(Image img , double posx, double posy,double width,double height)
	{
		Gfx.drawImage(img, posx, posy,width,height);
	}
	public void AddImage(Image img , double posx, double posy)
	{
		Gfx.drawImage(img, posx, posy);
	}

	public void AddImage(Image img , Rectangle rect)
	{
		
		Gfx.drawImage(img, rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
	}	
	public void AddImage(Image img , Rectangle rectdestination,Rectangle rectsource)
	{
		
		Gfx.drawImage(img,rectsource.getX(),rectsource.getY(),rectsource.getWidth(),rectsource.getHeight(),
				rectdestination.getX(),rectdestination.getY(),rectdestination.getWidth(),rectdestination.getHeight());
	}


	public void ClearScreen()
	{
		Gfx.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
	}
	public void Show()
	{
	}
	public GraphicsContext GetGfx()
	{
		return Gfx;
	}
	public Canvas GetCanvas()
	{
		return canvas;
	}
	public void center()
	{
		Definitions.stage.setX((Screen.getPrimary().getVisualBounds().getWidth()-Definitions.stage.getWidth())/2);
		Definitions.stage.setY((Screen.getPrimary().getVisualBounds().getHeight()-Definitions.stage.getHeight())/2);
		
	}
	public void Resize(double width,double height)
	{
		CountDownLatch donesignal = new CountDownLatch (1);
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				Definitions.stage.setIconified(true);
				ClearScreen();
				canvas.setHeight(height);
				canvas.setWidth(width);
				Definitions.stage.setHeight(height+38);
				Definitions.stage.setWidth(width+14);
				Width = width;
				Height = height;
				center();
				Definitions.stage.setIconified(false);
				Definitions.stage.toFront();
				donesignal.countDown();
			}
		});
		try {
			donesignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}
	public void AddImage(Image img, Rectangle rectdestination, double x, double y, double width, double height) 
	{
		Gfx.drawImage(img,x,y,width,height,rectdestination.getX(),rectdestination.getY(),
				rectdestination.getWidth(),rectdestination.getHeight());
	}
	public void AddImage(Image image, double dx, double dy, double dw, double dh, double sx, double sy, double sw, double sh) {
		Gfx.drawImage(image,   sx,  sy,  sw,  sh,  dx,  dy,  dw,  dh);
	}

}

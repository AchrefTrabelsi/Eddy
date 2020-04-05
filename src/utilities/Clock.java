package utilities;

public class Clock {
	private long start;
	private long time;
	private int Working ;
	public Clock()
	{
		Working = 0;
		time =0;
		start=0;
	}
	public void Start()
	{
		start=System.currentTimeMillis();
		Working = 1;
	}
	public void Resume()
	{
		if(Working == 0)
		{
			start=System.currentTimeMillis();
			Working = 1;
		}

	}

	public void Pause()
	{
		if(Working == 1)
		{
			time += System.currentTimeMillis()-start;
			start = 0;
			Working =0;
		}

	}
	public double ElapsedTimeinSeconds()
	{
		if(Working == 1)
			return ((double)System.currentTimeMillis()-start+time)/1000;
		else 
			return ((double)time)/1000;
	}
	public double ElapsedTimeinMilliSeconds()
	{
		if(Working == 1)
			return System.currentTimeMillis()-start+time;
		else 
			return time;

	}
	public void reset()
	{
		Working = 0;
		time =0;
		start=0;
	}


}

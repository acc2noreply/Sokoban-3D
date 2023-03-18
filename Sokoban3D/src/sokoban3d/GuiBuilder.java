package sokoban3d;

/**
 * 
 * This class used fr starting the game
 */

public class GuiBuilder 
{
	private Music music, music0, music1;

	/**
	 * class constructor 
	 */
	  public GuiBuilder()
	  {
	     music = new Music();
	     music0 = new Music();
	     music1 = new Music();
	     Gui g = new Gui(music, music0, music1);
	     g.createSound();
	     g.startCreate();
	  }

	/**
	 * the main method of the application
	 * @param args array of string arguments
	 */
	public static void main(String[] args)
	{
		GuiBuilder gB = new GuiBuilder();

	}

}

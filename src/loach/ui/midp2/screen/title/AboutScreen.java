/**
 * 
 */
package loach.ui.midp2.screen.title;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;

import loach.ui.midp2.MIDP2LoachUI;
import loach.ui.midp2.util.MIDP2Utils;

/**
 * LoachM - TitleScreen
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class AboutScreen extends Form implements CommandListener {
	private final TitleScreen titleScreen;
	
	private final MIDP2LoachUI midp2LoachUI;
	private final Display display;
	
	/**
	 * @param titleScreen The title screen.
	 */
	public AboutScreen(final TitleScreen titleScreen) {
		super("About " + MIDP2LoachUI.SHORT_TITLE);
		
		this.titleScreen = titleScreen;
		
		midp2LoachUI = titleScreen.getMIDP2LoachUI();
		display = midp2LoachUI.getDisplay();
		
		final StringItem aboutStringItem = new StringItem(
			null,
			MIDP2LoachUI.LONG_TITLE +
			"\n\n" +
			"Overlord Loach Mobile was created by Chris Dennett. " +
			"(Dessimat0r@ntlworld.com)" +
			"\n\n" +
			"Official site: http://codeknight.net/LoachM/" +
			"\n\n" +
			"Greets to: Max, timecop, supers, sam, rkz, Paul, " +
			"dsp / theo, Jmax, jax, popeye, falso, the people in #lwjgl and " +
			"#opengl, as well as many others." +
			"\n\n" +
			"Thanks to everyone who tested it for me on their mobile " +
			"phones -- their input has been most helpful in helping me " +
			"debug, and improve various aspects of the game." +
			"\n\n" +
			"The biggest thanks, of course, go to Paul Burkey (shoecake), " +
			"creator of Sneech, upon which this game is obviously based. I " +
			"thank him for creating such a wonderful Amiga game, which " +
			"I still enjoy to play even today." +
			"\n\n" +
			"Please donate some money if you like playing Loach Mobile! " +
			"(there is a Paypal link on the official homepage)" +
			"\n"
		);

		append(aboutStringItem);
		
		final ImageItem authorPic = new ImageItem(
			"The man himself!",
			MIDP2LoachUI.AUTHOR_BW_IMAGE,
			ImageItem.LAYOUT_CENTER,
			"An image of the author."
		);

		append(authorPic);
		
		setCommandListener(this);
		addCommand(MIDP2Utils.SCREEN_BACK_COMMAND);
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(final Command c, final Displayable d) {
		if (d == this && c.getCommandType() == Command.BACK)
			display.setCurrent(titleScreen);
	}
}
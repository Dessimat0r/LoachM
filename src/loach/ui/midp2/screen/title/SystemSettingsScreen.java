/**
 * 
 */
package loach.ui.midp2.screen.title;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import loach.ui.midp2.MIDP2LoachUI;
import loach.ui.midp2.db.LoachDB;
import loach.ui.midp2.screen.title.settings.SystemSettings;
import loach.ui.midp2.util.MIDP2Utils;
import util.Util;

/**
 * LoachM - LeagueSettingsScreen
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class SystemSettingsScreen extends Form
implements CommandListener
{
	private final TitleScreen titleScreen;
	private final SystemSettings systemSettings;
	private final LoachDB loachDB;
	
	private final MIDP2LoachUI midp2LoachUI;
	private final Display display;
	
	
	private static final String[]
		FRAME_SKIP_TEXTS = new String[] {
			"Draw every frame", "Draw every other frame",
			"Draw every 2nd frame", "Draw every 4th frame",
			"Draw every 6th frame", "Draw every 8th frame"		
		},
		THREAD_SLEEP_TEXTS = new String[] {
			"Don't sleep", "0 ms (yield)", "10 ms",
			"25 ms", "50 ms", "100 ms", "150 ms",
			"200 ms"
		}
	;
	
	private static final int[]
		FRAME_SKIP_CHOICES = {0, 1, 2, 4, 6, 8},
		THREAD_SLEEP_CHOICES = {-1, 0, 10, 25, 50, 100, 150, 200}
	;
	
	private final ChoiceGroup frameskipChoiceGroup, threadsleepChoiceGroup;
	
	/**
	 * @param titleScreen
	 * @param systemSettings
	 */
	public SystemSettingsScreen(
		final TitleScreen titleScreen, final SystemSettings systemSettings
	) {
		super("System Settings");
		
		this.titleScreen = titleScreen;
		this.systemSettings = systemSettings;
		midp2LoachUI = titleScreen.getMIDP2LoachUI();
		loachDB = midp2LoachUI.getLoachDB();
		display = midp2LoachUI.getDisplay();
		
		int index;
		
		frameskipChoiceGroup = new ChoiceGroup(
			"Frame skip: ", Choice.POPUP, FRAME_SKIP_TEXTS, null
		);
		
		index = Util.findIndexOfIntElementInArray(
			systemSettings.getFrameskip(), FRAME_SKIP_CHOICES
		);
		
		frameskipChoiceGroup.setSelectedIndex(index, true);
		
		append(frameskipChoiceGroup);
		
		threadsleepChoiceGroup = new ChoiceGroup(
			"Thread sleep: ", Choice.POPUP, THREAD_SLEEP_TEXTS, null
		);
		
		index = Util.findIndexOfIntElementInArray(
			systemSettings.getThreadSleep(), THREAD_SLEEP_CHOICES
		);
		
		threadsleepChoiceGroup.setSelectedIndex(index, true);
		
		append(threadsleepChoiceGroup);
		
		addCommand(MIDP2Utils.SCREEN_ACCEPT_COMMAND);
		addCommand(MIDP2Utils.SCREEN_CANCEL_COMMAND);
		
		setCommandListener(this);
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(final Command c, final Displayable d) {
		if (d == this) {
			switch (c.getCommandType()) {
				case Command.SCREEN: accept(); break;
				case Command.BACK: back();
			}
		}
	}
	
	/**
	 * 
	 */
	private void accept() {
		systemSettings.setFrameskip(
			FRAME_SKIP_CHOICES[frameskipChoiceGroup.getSelectedIndex()]
		);
		systemSettings.setThreadSleep(
			THREAD_SLEEP_CHOICES[threadsleepChoiceGroup.getSelectedIndex()]
		);
		systemSettings.push(loachDB);
		loachDB.commit();
		
		display.setCurrent(titleScreen);
	}
	
	private void back() {
		display.setCurrent(titleScreen);
	}	
}
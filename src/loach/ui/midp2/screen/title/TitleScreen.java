/**
 * 
 */
package loach.ui.midp2.screen.title;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;

import loach.model.player.Player;
import loach.ui.midp2.MIDP2LoachUI;
import loach.ui.midp2.db.LoachDB;
import loach.ui.midp2.screen.match.MatchScreen;
import loach.ui.midp2.screen.title.settings.MatchSettings;
import loach.ui.midp2.screen.title.settings.PlayerSettings;
import loach.ui.midp2.screen.title.settings.SystemSettings;
import loach.ui.midp2.util.MIDP2Utils;
import loach.ui.midp2.viewinfo.WormViewInfo;
import util.Util;

/**
 * LoachM - TitleScreen
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class TitleScreen extends Form
implements CommandListener, ItemCommandListener
{
	private static final int MAX_PLAYERS = 6;
	
	private final MIDP2LoachUI midp2LoachUI;
	
	private final ImageItem loachLogo;
	
	private final StringItem
		startMatchStringItem,
		configMatchStringItem,
		configSystemStringItem,
		aboutScreenStringItem,
		quitStringItem
	;
	
	private final StringItem[]
		configPlayerStringItems = new StringItem[MAX_PLAYERS]
	 ;
	private final PlayerSettings[]
		playerSettings = new PlayerSettings[MAX_PLAYERS]
	;
	
	private final MatchSettings matchSettings;
	private final SystemSettings systemSettings;
	
	private final LoachDB loachDB;
	
	private final Display display;
	
	/**
	 * @param midp2LoachUI The MIDP2LoachUI.
	 */
	public TitleScreen(final MIDP2LoachUI midp2LoachUI) {
		super(MIDP2LoachUI.SHORT_TITLE);
		
		if (midp2LoachUI == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		this.midp2LoachUI = midp2LoachUI;
		
		display = midp2LoachUI.getDisplay();		
		loachDB = midp2LoachUI.getLoachDB();
		
		matchSettings = new MatchSettings();
		matchSettings.pull(loachDB);
		
		systemSettings = new SystemSettings();
		systemSettings.pull(loachDB);
		
		PlayerSettings ps;
		for (int i = 0; i < MAX_PLAYERS; i++) {
			ps = new PlayerSettings(i + 1);
			playerSettings[i] = ps;
			ps.pull(loachDB);
		}
		
		loachLogo = new ImageItem(
			null,
			MIDP2LoachUI.TITLE_SCREEN_LOGO_IMAGE,
			ImageItem.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2,
			"Loach Mobile logo"
		);
		
		startMatchStringItem = new StringItem(
			null, "Start match\n"
		);
		configMatchStringItem = new StringItem(
			null, "Configure match\n"
		);
		configSystemStringItem = new StringItem(
			null, "Configure system\n"
		);
		aboutScreenStringItem = new StringItem(
			null, "About Overlord Loach...\n"
		);
		quitStringItem = new StringItem(
			null, "Quit"
		);
		
		startMatchStringItem.setDefaultCommand(
			MIDP2Utils.SCREEN_SELECT_COMMAND
		);
		configMatchStringItem.setDefaultCommand(
			MIDP2Utils.SCREEN_SELECT_COMMAND
		);
		configSystemStringItem.setDefaultCommand(
			MIDP2Utils.SCREEN_SELECT_COMMAND
		);
		aboutScreenStringItem.setDefaultCommand(
			MIDP2Utils.SCREEN_SELECT_COMMAND
		);
		quitStringItem.setDefaultCommand(
			MIDP2Utils.SCREEN_SELECT_COMMAND
		);
		
		startMatchStringItem.setItemCommandListener(this);
		configMatchStringItem.setItemCommandListener(this);
		configSystemStringItem.setItemCommandListener(this);
		aboutScreenStringItem.setItemCommandListener(this);
		quitStringItem.setItemCommandListener(this);
		
		append(loachLogo);
		append(startMatchStringItem);
		append(configMatchStringItem);
		
		StringItem si;
		for (int i = 0; i < MAX_PLAYERS; i++) {
			si = new StringItem(
				null, createPlayerConfigRowText(playerSettings[i])
			);
			configPlayerStringItems[i] = si;
			
			si.setDefaultCommand(MIDP2Utils.SCREEN_SELECT_COMMAND);
			si.setItemCommandListener(this);
			append(si);
		}
		
		append(configSystemStringItem);
		append(aboutScreenStringItem);
		append(quitStringItem);
		
		addCommand(MIDP2Utils.SCREEN_QUIT_COMMAND);
		setCommandListener(this);
		
		System.gc();
	}
	
	/**
	 * @return the midp2LoachUI
	 */
	public MIDP2LoachUI getMIDP2LoachUI() {
		return midp2LoachUI;
	}
	
	private String createPlayerConfigRowText(final PlayerSettings ps) {
		final StringBuffer sb = new StringBuffer();
		
		final WormViewInfo wvi = ps.getWormViewInfo();
		final int
			type = ps.getPlayerType(),
			num = ps.getPlayerNo()
		;
		
		sb.append("Configure ");
		
		if (type == PlayerSettings.PLAYER_TYPE_HUMAN)
			sb.append(ps.getHumanName());
		else if (type == PlayerSettings.PLAYER_TYPE_CPU)
			sb.append(wvi.getWormInfo().getCPUPlayerName());
		else {
			sb.append("Player ");
			sb.append(num);
		}
		
		sb.append(" (");
		
		final String typeStr;
		
		switch (type) {
			case PlayerSettings.PLAYER_TYPE_DISABLED:
				typeStr = "Disabled"; break;
			case PlayerSettings.PLAYER_TYPE_HUMAN: typeStr = "Human"; break;
			case PlayerSettings.PLAYER_TYPE_CPU: typeStr = "CPU"; break;
			default: typeStr = "Unknown!";
		}
		
		sb.append(typeStr);
		sb.append(")\n");
			
		return sb.toString();
	}
	
	public void updatePlayerConfigRowTextForPlayer(final int playerNo) {
		configPlayerStringItems[playerNo - 1].setText(
			createPlayerConfigRowText(playerSettings[playerNo - 1])
		);
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(final Command c, final Displayable d) {
		if (d == this && c.getCommandType() == Command.BACK) quit();
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.ItemCommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Item)
	 */
	public void commandAction(final Command c, final Item item) {
		if (c.getCommandType() == Command.SCREEN) {
			if (item == startMatchStringItem) startMatch();
			else if (item == configMatchStringItem) openLeagueSettingsScreen();
			else if (item == configSystemStringItem) openSystemSettingsScreen();
			else if (item == aboutScreenStringItem) openAboutScreen();
			else if (item == quitStringItem) quit();
			else {
				for (int i = 0; i < MAX_PLAYERS; i++) {
					if (configPlayerStringItems[i] == item)
						openPlayerSettingsScreen(i + 1);
				}
			}
		}
	}
	
	private void quit() {
		midp2LoachUI.quit();
	}
	
	private void openLeagueSettingsScreen() {
		final MatchSettingsScreen lss = new MatchSettingsScreen(
			this, matchSettings
		);
		display.setCurrent(lss);
	}
	
	
	private PlayerSettings obtainHumanPlayerSettings() {
		PlayerSettings ps;
		for (int i = 0; i < playerSettings.length; i++) {
			ps = playerSettings[i];
			if (ps.getPlayerType() == PlayerSettings.PLAYER_TYPE_HUMAN)
				return ps;
		}
		return null;
	}
	
	private void openPlayerSettingsScreen(final int playerNo) {
		final PlayerSettings
			hps = obtainHumanPlayerSettings(),
			ps = playerSettings[playerNo - 1]
		;
		final PlayerSettingsScreen pss = new PlayerSettingsScreen(
			this, ps, hps != null && hps != ps
		);
		display.setCurrent(pss);	
	}
	
	private void openSystemSettingsScreen() {
		final SystemSettingsScreen sss = new SystemSettingsScreen(
			this, systemSettings
		);
		display.setCurrent(sss);
	}	
	
	private void openAboutScreen() {
		final AboutScreen as = new AboutScreen(this);
		midp2LoachUI.getDisplay().setCurrent(as);
	}
		
	private void startMatch() {		
		final Vector tempPlayers = new Vector(playerSettings.length);
		Player humanPlayer = null;
		
		int playerType;
		
		Player player;
		PlayerSettings ps;
		for (int i = 0; i < playerSettings.length; i++) {
			ps = playerSettings[i];
			playerType = ps.getPlayerType();
			if (playerType != PlayerSettings.PLAYER_TYPE_DISABLED) {
				player = ps.createPlayer();				
				if (playerType == PlayerSettings.PLAYER_TYPE_HUMAN) {
					if (humanPlayer == null) humanPlayer = player;
				}
				tempPlayers.addElement(player);
			}
		}
		
		if (humanPlayer != null) {
			final Player[] players = new Player[tempPlayers.size()];
			tempPlayers.copyInto(players);
			
			final MatchScreen matchScreen = new MatchScreen(
				midp2LoachUI,
				matchSettings.createMatch(players),
				humanPlayer,
				systemSettings.getFrameskip(),
				systemSettings.getThreadSleep()
			);
			
			display.setCurrent(matchScreen);
			final Thread t = new Thread(matchScreen);
			t.start();
		} else {
			final Alert alert = new Alert(
				"Errors detected!",
				"You can only have one human player.",
				null, AlertType.ERROR
			);
			midp2LoachUI.getDisplay().setCurrent(alert);
		}
	}
}
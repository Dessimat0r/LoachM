/**
 * 
 */
package loach.ui.midp2.screen.title;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import loach.model.player.Player;
import loach.ui.midp2.MIDP2LoachUI;
import loach.ui.midp2.db.LoachDB;
import loach.ui.midp2.screen.title.settings.PlayerSettings;
import loach.ui.midp2.util.MIDP2Utils;
import loach.ui.midp2.viewinfo.WormViewInfo;
import util.Util;

/**
 * LoachM - PlayerSettingsScreen
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class PlayerSettingsScreen extends Form
implements CommandListener, ItemStateListener {	
	private final LoachDB loachDB;
	
	private final MIDP2LoachUI midp2LoachUI;
	private final Display display;
	
	private final boolean disableHuman;
	
	private final PlayerSettings playerSettings;
	private final TitleScreen titleScreen;
	private final ChoiceGroup wormTypeInput, playerTypeInput;
	private final ImageItem wormSegmentDisplay;
	private final TextField humanPlayerNameInput;
	private final StringItem cpuPlayerNameDisplay;
	
	private static final String[]
		WORM_INFO_NAMES = new String[WormViewInfo.WORM_VIEW_INFOS.length],
		NORMAL_PLAYER_TYPE_LIST_STRINGS = new String[] {
			"Disabled", "Human", "CPU"
		},
		DISABLE_HUMAN_PLAYER_TYPE_LIST_STRINGS = new String[] {
			"Disabled", "CPU"
		}
	;
	
	private static final int[]
		NORMAL_PLAYER_TYPE_LIST_TYPES = new int[] {
			PlayerSettings.PLAYER_TYPE_DISABLED,
			PlayerSettings.PLAYER_TYPE_HUMAN,
			PlayerSettings.PLAYER_TYPE_CPU
		},
		DISABLE_HUMAN_PLAYER_TYPE_LIST_TYPES = new int[] {
			PlayerSettings.PLAYER_TYPE_DISABLED,
			PlayerSettings.PLAYER_TYPE_CPU
		}
	;
	
	private int currentPlayerType;
	private WormViewInfo wormViewInfo;
	
	static {
		for (int i = 0; i < WormViewInfo.WORM_VIEW_INFOS.length; i++)
			WORM_INFO_NAMES[i] =
				WormViewInfo.WORM_VIEW_INFOS[i].getWormInfo().getName()
			;
	}
	
	/**
	 * @param titleScreen 
	 * @param playerSettings 
	 * @param disableHuman 
	 */
	public PlayerSettingsScreen(
		final TitleScreen titleScreen,
		final PlayerSettings playerSettings,
		final boolean disableHuman
	) {
		super("Settings for player " + playerSettings.getPlayerNo());
		
		this.titleScreen = titleScreen;
		this.playerSettings = playerSettings;
		this.disableHuman = disableHuman;
		
		midp2LoachUI = titleScreen.getMIDP2LoachUI();
		display = midp2LoachUI.getDisplay();
		loachDB = midp2LoachUI.getLoachDB();
		wormViewInfo = playerSettings.getWormViewInfo();
		currentPlayerType = playerSettings.getPlayerType();
		
		playerTypeInput = new ChoiceGroup(
			"Type: ",
			Choice.POPUP,
			(
				!disableHuman ?
				NORMAL_PLAYER_TYPE_LIST_STRINGS :
				DISABLE_HUMAN_PLAYER_TYPE_LIST_STRINGS
			),
			null
		);
		
		playerTypeInput.setSelectedIndex(
			Util.findIndexOfIntElementInArray(
				currentPlayerType,
				(
					!disableHuman ?
					NORMAL_PLAYER_TYPE_LIST_TYPES :
					DISABLE_HUMAN_PLAYER_TYPE_LIST_TYPES
				)
			),
			true
		);
		
		append(playerTypeInput);
		
		humanPlayerNameInput = new TextField(
			"Name: ",
			playerSettings.getHumanName(),
			10,
			Item.PLAIN
		);
		
		cpuPlayerNameDisplay = new StringItem(
			"Name: ",
			wormViewInfo.getWormInfo().getCPUPlayerName()
		);
		
		wormTypeInput = new ChoiceGroup(
			"Worm: ",
			Choice.POPUP,
			WORM_INFO_NAMES,
			null
		);
		
		wormTypeInput.setSelectedIndex(
			Util.findIndexOfObjElementInArray(
				wormViewInfo,
				WormViewInfo.WORM_VIEW_INFOS
			),
			true
		);
		
		wormSegmentDisplay = new ImageItem(
			"Worm segment:",
			wormViewInfo.getSegmentImage(),
			ImageItem.LAYOUT_DEFAULT,
			"Worm segment for " +
			wormViewInfo.getWormInfo().getName() +
			" worm"
		);
		
		wormSegmentDisplay.setImage(wormViewInfo.getSegmentImage());
		
		cpuPlayerNameDisplay.setText(
			wormViewInfo.getWormInfo().getCPUPlayerName()
		);
		
		addItems(currentPlayerType);
		
		addCommand(MIDP2Utils.SCREEN_ACCEPT_COMMAND);
		addCommand(MIDP2Utils.SCREEN_CANCEL_COMMAND);
		
		setCommandListener(this);
		setItemStateListener(this);
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(final Command c, final Displayable d) {
		if (d == this) {
			switch (c.getCommandType()) {
				case Command.BACK: back(); break;
				case Command.SCREEN: accept();
			}
		}
	}
	
	private void back() {
		display.setCurrent(titleScreen);
	}
	
	private void accept() {
		final String humanName = humanPlayerNameInput.getString();
		
		final boolean
			nameTooShort = Player.nameTooShort(humanName),
			nameTooLong = Player.nameTooLong(humanName)
		;
		
		if (nameTooShort || nameTooLong) {
			final String alertMsg =
				"The following errors were detected:\n\n" + (
					nameTooShort ?
					"The name entered was too short. It should not be " +
					"less than " + Player.MIN_NAME_LENGTH + " characters " +
					"long.\n" :
					""
				) + (
					nameTooLong ?
					"The name entered was too long. It should not be " +
					"more than " + Player.MAX_NAME_LENGTH + " characters " +
					"long.\n\n" :
					""
				) + "Please correct these errors and try again."
			;
			
			final Alert alert = new Alert(
				"Errors detected!", alertMsg, null, AlertType.ERROR
			);
			display.setCurrent(alert);
			
			humanPlayerNameInput.setString(playerSettings.getHumanName());
		} else {
			/* save everything into playerSettings */
			playerSettings.setPlayerType(currentPlayerType);
			playerSettings.setHumanName(humanName);
			
			playerSettings.setWormViewInfo(wormViewInfo);
			playerSettings.push(loachDB);
			loachDB.commit();
			
			titleScreen.updatePlayerConfigRowTextForPlayer(
				playerSettings.getPlayerNo()
			);
			display.setCurrent(titleScreen);
		}
	}
	
	private void changeWVI(final WormViewInfo wormViewInfo) {
		wormSegmentDisplay.setImage(wormViewInfo.getSegmentImage());
		
		cpuPlayerNameDisplay.setText(
			wormViewInfo.getWormInfo().getCPUPlayerName()
		);
		
		this.wormViewInfo = wormViewInfo;
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.ItemStateListener#itemStateChanged(javax.microedition.lcdui.Item)
	 */
	public void itemStateChanged(final Item item) {
		if (item == playerTypeInput)
			changeMode(
				currentPlayerType,
				(
					!disableHuman ?
					NORMAL_PLAYER_TYPE_LIST_TYPES :
					DISABLE_HUMAN_PLAYER_TYPE_LIST_TYPES
				)[playerTypeInput.getSelectedIndex()]
			);
		else if (item == wormTypeInput)
			changeWVI(
				WormViewInfo.WORM_VIEW_INFOS[wormTypeInput.getSelectedIndex()]
			);
	}
	
	private void changeMode(final int prevPlayerType, final int playerType) {
		clearItems(prevPlayerType);
		addItems(playerType);
		
		currentPlayerType = playerType;
	}
	
	private void clearItems(final int prevPlayerMode) {
		switch (prevPlayerMode) {
			case PlayerSettings.PLAYER_TYPE_HUMAN:
			case PlayerSettings.PLAYER_TYPE_CPU:
				delete(3);
				delete(2);
				delete(1);
		}
	}
	
	private void addItems(final int playerMode) {
		switch (playerMode) {
			case PlayerSettings.PLAYER_TYPE_HUMAN:
				append(humanPlayerNameInput);
				append(wormTypeInput);
				append(wormSegmentDisplay);
				break;
			case PlayerSettings.PLAYER_TYPE_CPU:
				append(cpuPlayerNameDisplay);
				append(wormTypeInput);
				append(wormSegmentDisplay);
		}
	}	
}
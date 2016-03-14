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
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;

import loach.model.field.Field;
import loach.model.match.Match;
import loach.model.match.MatchPeriodInfo;
import loach.ui.midp2.MIDP2LoachUI;
import loach.ui.midp2.db.LoachDB;
import loach.ui.midp2.screen.title.settings.MatchSettings;
import loach.ui.midp2.util.MIDP2Utils;

/**
 * LoachM - LeagueSettingsScreen
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class MatchSettingsScreen extends Form
implements CommandListener, ItemStateListener
{
	private final TitleScreen titleScreen;
	private final MatchSettings matchSettings;
	
	private final MIDP2LoachUI midp2LoachUI;
	private final Display display;
	
	private final LoachDB loachDB;
	
	private static final String[]
		DOOR_POLICY_TEXTS = {"By key", "Always", "Never"},
		MATCH_LENGTH_TEXTS = {
			"Short (" +
			MatchSettings.MATCH_PERIOD_INFO_SHORT.getTotalLengthMS() / 1000 +
			" secs)",
			
			"Medium (" +
			MatchSettings.MATCH_PERIOD_INFO_MEDIUM.getTotalLengthMS() / 1000 +
			" secs)",
			
			"Long (" +
			MatchSettings.MATCH_PERIOD_INFO_LONG.getTotalLengthMS() / 1000 +
			" secs)",
		},
		WORM_GROWTH_TYPE_TEXTS = {
			"Auto", "Parts"
		}
	;
	
	private final ChoiceGroup
		doorPolicyInputItem,
		matchLengthInputItem,
		wormGrowthTypeInputItem
	;
	
	/**
	 * @param titleScreen
	 * @param matchSettings
	 */
	public MatchSettingsScreen(
		final TitleScreen titleScreen, final MatchSettings matchSettings
	) {
		super("Match Settings");
		
		this.titleScreen = titleScreen;
		this.matchSettings = matchSettings;
		
		midp2LoachUI = titleScreen.getMIDP2LoachUI();
		display = midp2LoachUI.getDisplay();
		
		loachDB = midp2LoachUI.getLoachDB();
		
		matchLengthInputItem = new ChoiceGroup(
			"Length: ", Choice.POPUP, MATCH_LENGTH_TEXTS, null
		);
		
		doorPolicyInputItem = new ChoiceGroup(
			"Doors open: ", Choice.POPUP, DOOR_POLICY_TEXTS, null
		);
		
		wormGrowthTypeInputItem = new ChoiceGroup(
			"Worm growth: ", Choice.POPUP, WORM_GROWTH_TYPE_TEXTS, null
		);
		
		int selIndex = 0;
		
		switch (matchSettings.getDoorPolicy()) {
			case Field.DOORS_OPEN_BY_KEY: selIndex = 0; break;
			case Field.DOORS_ALWAYS_OPEN: selIndex = 1; break;
			case Field.DOORS_NEVER_OPEN: selIndex = 2;
		}
		
		doorPolicyInputItem.setSelectedIndex(selIndex, true);
		
		selIndex = 0;
		
		final MatchPeriodInfo mpi = matchSettings.getMatchPeriodInfo();
		
		if (mpi == MatchSettings.MATCH_PERIOD_INFO_SHORT) selIndex = 0;
		else if (mpi == MatchSettings.MATCH_PERIOD_INFO_MEDIUM) selIndex = 1;
		else if (mpi == MatchSettings.MATCH_PERIOD_INFO_LONG) selIndex = 2;
		
		matchLengthInputItem.setSelectedIndex(selIndex, true);
		
		selIndex = 0;
		
		switch (matchSettings.getWormGrowthType()) {
			case Match.WORM_GROWTH_TYPE_AUTO: selIndex = 0; break;
			case Match.WORM_GROWTH_TYPE_PARTS: selIndex = 1;
		}
		
		wormGrowthTypeInputItem.setSelectedIndex(selIndex, true);
		
		append(matchLengthInputItem);
		append(doorPolicyInputItem);
		append(wormGrowthTypeInputItem);
		
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
				case Command.SCREEN: accept(); break;
				case Command.BACK: back();
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.ItemStateListener#itemStateChanged(javax.microedition.lcdui.Item)
	 */
	public void itemStateChanged(final Item item) {}
	
	private void back() {
		display.setCurrent(titleScreen);
	}
	
	/**
	 * 
	 */
	private void accept() {
		switch (matchLengthInputItem.getSelectedIndex()) {
			case 0:
				matchSettings.setMatchPeriodInfo(
					MatchSettings.MATCH_PERIOD_INFO_SHORT
				);
				break;
			case 1:
				matchSettings.setMatchPeriodInfo(
					MatchSettings.MATCH_PERIOD_INFO_MEDIUM
				);
				break;
			case 2:
				matchSettings.setMatchPeriodInfo(
					MatchSettings.MATCH_PERIOD_INFO_LONG
				);
		}
		
		switch (doorPolicyInputItem.getSelectedIndex()) {
			case 0: matchSettings.setDoorPolicy(Field.DOORS_OPEN_BY_KEY); break;
			case 1: matchSettings.setDoorPolicy(Field.DOORS_ALWAYS_OPEN); break;
			case 2: matchSettings.setDoorPolicy(Field.DOORS_NEVER_OPEN);
		}
		
		switch (wormGrowthTypeInputItem.getSelectedIndex()) {
			case 0:
				matchSettings.setWormGrowthType(Match.WORM_GROWTH_TYPE_AUTO);
				break;
			case 1:
				matchSettings.setWormGrowthType(Match.WORM_GROWTH_TYPE_PARTS);
		}
		
		matchSettings.push(loachDB);
		loachDB.commit();
		
		display.setCurrent(titleScreen);
	}
}
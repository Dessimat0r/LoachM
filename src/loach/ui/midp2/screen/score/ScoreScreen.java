/**
 * 
 */
package loach.ui.midp2.screen.score;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import loach.model.match.Match;
import loach.model.match.worm.Worm;
import loach.model.player.Player;
import loach.ui.midp2.MIDP2LoachUI;
import loach.ui.midp2.screen.title.TitleScreen;
import loach.ui.midp2.util.MIDP2Utils;

/**
 * LoachM - ScoreScreen
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class ScoreScreen extends Form implements CommandListener {
	private final MIDP2LoachUI midp2LoachUI;
	private final StringItem[] wormResultsStringItems;
	private final Match match;
	
	/**
	 * @param midp2LoachUI The MIDP2LoachUI.
	 * @param match 
	 */
	public ScoreScreen(
		final MIDP2LoachUI midp2LoachUI,
		final Match match
	) {
		super("Match Results");
		
		this.midp2LoachUI = midp2LoachUI;
		this.match = match;
		
		final Player[] players = match.getPlayers();
		final Worm[] worms = new Worm[players.length];
		
		for (int i = 0; i < players.length; i++)
			worms[i] = match.obtainWormForPlayer(players[i]);
		
		Worm worm, tempWinner = null;
		int currWormPoints, tempWinnerPoints = -1;
		
		for (int i = 0; i < worms.length; i++) {
			worm = worms[i];
			
			currWormPoints = (worm.getKills().size() * 300) + worm.getMoney();
			
			if (
				tempWinner == null || (
					worm.isAlive() &&
					(currWormPoints > tempWinnerPoints)
				)
			) {
				tempWinner = worm;
				tempWinnerPoints = currWormPoints;
			}
		}
		
		final Worm winner = tempWinner;
		final int winnerPoints = tempWinnerPoints;
		
		StringItem si;
		
		if (winner != null) {
			si = new StringItem(
				null,
				"The winner is " + winner.getPlayer().getName() +
				" with " + winnerPoints + " points!\n"
			);
			append(si);
		} else {
			si = new StringItem(
				null,
				"There was no winner in this game.\n"
			);
			append(si);
		}
		
		/* we know that each player maps to a single worm in the match,
		 * therefore this can be used for ordering the player results. */
		Player player;
		StringBuffer sb;
		
		wormResultsStringItems = new StringItem[worms.length];
		
		for (int i = 0; i < worms.length; i++) {
			worm = worms[i];
			player = worm.getPlayer();
			
			sb = new StringBuffer();
			
			sb.append(player.getName() + ": ");
			sb.append("Money: " + "$" + worm.getMoney() + ", ");
			sb.append("Kills: " + worm.getKills().size());
			if (!worm.isAlive()) sb.append(" (dead)");
			sb.append("\n");
			
			si = new StringItem(null, sb.toString());
			
			wormResultsStringItems[i] = si;
			append(si);
		}
		
		addCommand(MIDP2Utils.SCREEN_NEXT_COMMAND);
		setCommandListener(this);
	}
	
	private void next() {
		final TitleScreen ts = new TitleScreen(midp2LoachUI);
		midp2LoachUI.getDisplay().setCurrent(ts);
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(final Command c, final Displayable d) {
		if (d == this && c.getCommandType() == Command.SCREEN) next();
	}
}
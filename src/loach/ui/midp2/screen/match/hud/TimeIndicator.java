/**
 * 
 */
package loach.ui.midp2.screen.match.hud;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

import loach.model.match.Match;
import loach.model.match.MatchPeriodInfo;
import loach.ui.midp2.MIDP2LoachUI;
import loach.ui.midp2.screen.match.MatchScreen;

/**
 * LoachM - TimeIndicator
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class TimeIndicator extends HUDElement {
	private static final int
		TI_WIDTH = 8,
		TI_HEIGHT = 120,
		
		X_OFFSET = 5,
		
		STATE_FOLDING_OUT = 0,
		STATE_NORMAL = 1
	;
	
	private static final long
		FOLDING_OUT_LENGTH_MS = MatchScreen.TIME_UNTIL_MATCH_START_MS - 1000,
		FOLDING_OUT_SLICE_MS = FOLDING_OUT_LENGTH_MS / TI_HEIGHT
	;
	
	private final MIDP2LoachUI midp2LoachUI;
	private final MatchScreen matchScreen;
	private final Match match;
	private final MatchPeriodInfo matchPeriodInfo;
	
	private final long
		msInEachSlice,
		matchTotalLengthMS,
		timePerSliceMS,
		timeInitMS
	;
	
	private long foldingOutTimeMS, updateStartTimeMS;
	
	/**
	 * @param hud 
	 * @param timePerSliceMS 
	 */
	public TimeIndicator(final HUD hud, final int timePerSliceMS) {
		super(hud);
		
		this.timePerSliceMS = timePerSliceMS;
		
		matchScreen = hud.getMatchScreen();
		midp2LoachUI = matchScreen.getMIDP2LoachUI();
		
		match = matchScreen.getMatch();
		matchPeriodInfo = match.getMatchPeriodInfo();
		matchTotalLengthMS = matchPeriodInfo.getTotalLengthMS();
		
		msInEachSlice = matchTotalLengthMS / TI_HEIGHT;
		timeInitMS = matchScreen.getTimeInitMS();
	}
	
	public void update() {}

	/* (non-Javadoc)
	 * @see loach.ui.midp2.screen.match.hud.HUDElement#paint(javax.microedition.lcdui.Graphics)
	 */
	public void paint(final Graphics g) {
		final int
			matchState = match.getMatchState(),
			matchScreenHeight = matchScreen.getHeight(),
			matchScreenHeightCenter = matchScreen.getHeight() / 2
		;
		
		if (matchState == Match.MATCH_STATE_STARTING) {			
			int foldingOutHeight = (int)(
				(hud.getMatchScreen().getUpdateStartTimeMS() - timeInitMS) /
				FOLDING_OUT_SLICE_MS
			);
			
			if (foldingOutHeight > TI_HEIGHT) foldingOutHeight = TI_HEIGHT;
			
			final int foldingOutHeightCenter = foldingOutHeight / 2;
			
			if (foldingOutHeight > 0)
				g.drawRegion(
					MIDP2LoachUI.TIME_INDICATOR_IMAGE,
					0, (TI_HEIGHT / 2) - foldingOutHeightCenter,
					TI_WIDTH, foldingOutHeight,
					Sprite.TRANS_NONE,
					X_OFFSET,
					matchScreenHeightCenter - foldingOutHeightCenter,
					Graphics.TOP | Graphics.LEFT
				);
		} else if (matchState == Match.MATCH_STATE_STARTED) {
			final int
				tiHeight = (int)(match.getTimeRemainingMS() / msInEachSlice),
				tiHeightCenter = tiHeight / 2;
			;
			if (tiHeight > 0)
				g.drawRegion(
					MIDP2LoachUI.TIME_INDICATOR_IMAGE,
					0, TI_HEIGHT - tiHeight,
					TI_WIDTH, tiHeight,
					Sprite.TRANS_NONE,
					X_OFFSET,
					matchScreenHeightCenter - tiHeightCenter,
					Graphics.TOP | Graphics.LEFT
				);
		}
	}
}
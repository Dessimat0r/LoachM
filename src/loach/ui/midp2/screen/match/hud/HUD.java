/**
 * 
 */
package loach.ui.midp2.screen.match.hud;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.LayerManager;

import loach.ui.midp2.screen.match.MatchScreen;

/**
 * LoachM - HUD
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class HUD {
	private final MatchScreen matchScreen;
	private final HUDElement[] hudElements;
	private final LayerManager hudLayer; 
	
	/**
	 * @param matchScreen 
	 */
	public HUD(final MatchScreen matchScreen) {
		this.matchScreen = matchScreen;
		this.hudLayer = matchScreen.getHUDLayer();
		
		hudElements = new HUDElement[] {
			new TimeIndicator(this, 1000),
			new FPSDisplay(this)
		};
	}
	
	/* (non-Javadoc)
	 * @see loach.ui.midp2.util.Paintable#paint(javax.microedition.lcdui.Graphics)
	 */
	public void paint(final Graphics g) {
		for (int i = 0; i < hudElements.length; i++) hudElements[i].paint(g);
	}
	
	/**
	 * @return the matchScreen
	 */
	public MatchScreen getMatchScreen() {
		return matchScreen;
	}
	
	public void update() {
		for (int i = 0; i < hudElements.length; i++) hudElements[i].update();
	}
}

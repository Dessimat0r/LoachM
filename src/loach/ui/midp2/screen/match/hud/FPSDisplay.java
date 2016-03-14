/**
 * 
 */
package loach.ui.midp2.screen.match.hud;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import loach.ui.midp2.MIDP2LoachUI;

/**
 * LoachM - FPSDisplay
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class FPSDisplay extends HUDElement {
	private static final Font FONT = Font.getFont(
		Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL
	);
	private static final int HUD_OFFSET_X = 5, HUD_OFFSET_Y = 5;
	private final MIDP2LoachUI midp2LoachUI;
	
	/**
	 * @param hud 
	 */
	public FPSDisplay(final HUD hud) {
		super(hud);
		midp2LoachUI = hud.getMatchScreen().getMIDP2LoachUI();
	}

	/* (non-Javadoc)
	 * @see loach.ui.midp2.screen.match.hud.HUDElement#paint(javax.microedition.lcdui.Graphics)
	 */
	public void paint(final Graphics g) {
		paintFPS(g, hud.getMatchScreen().getCurrentFPS());
	}
	
	protected void paintFPS(final Graphics g, final int fps) {		
		g.setColor(255, 255, 255);
		g.setFont(FONT);
		
		g.drawString(
			Integer.toString(fps) + " FPS", HUD_OFFSET_X, HUD_OFFSET_Y,
			Graphics.TOP | Graphics.LEFT
		);
	}

	/* (non-Javadoc)
	 * @see loach.ui.midp2.screen.match.hud.HUDElement#update()
	 */
	public void update() {
		// TODO Auto-generated method stub
	}
}
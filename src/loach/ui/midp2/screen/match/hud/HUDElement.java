/**
 * 
 */
package loach.ui.midp2.screen.match.hud;

import javax.microedition.lcdui.Graphics;

/**
 * LoachM - HUDElement
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public abstract class HUDElement {
	protected final HUD hud;
	
	/**
	 * @param hud 
	 */
	public HUDElement(final HUD hud) {
		this.hud = hud;
	}
	
	/**
	 * @return the HUD
	 */
	public final HUD getHUD() {
		return hud;
	}
	
	/* (non-Javadoc)
	 * @see loach.ui.midp2.util.Paintable#paint(javax.microedition.lcdui.Graphics)
	 */
	public abstract void paint(final Graphics g);
	
	/**
	 * 
	 */
	public abstract void update();
}
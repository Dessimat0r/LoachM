/**
 * 
 */
package loach.ui.midp2.screen.match.worm;

import loach.model.match.worm.Worm;

/**
 * LoachM - WormView
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class WormView {
	private final Worm worm;
	
	/**
	 * @param worm The Worm that this WormView is attached to.
	 */
	public WormView(final Worm worm) {
		this.worm = worm;
	}
	
	/**
	 * @return the worm
	 */
	public Worm getWorm() {
		return worm;
	}
	
	public void addSegment() {
		
	}
	
	public void removeSegment() {
		
	}
}
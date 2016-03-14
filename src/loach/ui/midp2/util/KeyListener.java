/**
 * 
 */
package loach.ui.midp2.util;

/**
 * LoachM - KeyListener
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public interface KeyListener {	
	public void onKeyPress(final int key);
	public void onKeyRelease(final int key);
	public void onKeyRepeat(final int key);
}

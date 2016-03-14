/**
 * 
 */
package loach.ui.midp2.db;

/**
 * LoachM - LoachDAO
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public interface Persistent {
	/**
	 * @param loachDB
	 */
	public void push(final LoachDB loachDB);
	
	/**
	 * @param loachDB
	 */
	public void pull(final LoachDB loachDB);
}
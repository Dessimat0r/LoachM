/**
 * 
 */
package loach.model.player;

import loach.model.info.WormInfo;
import util.Util;

/**
 * LoachM - Player
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class Player {
	public static final int MIN_NAME_LENGTH = 1;
	public static final int MAX_NAME_LENGTH = 10;
	
	private final String name;
	private final WormInfo wormInfo;
	
	/**
	 * @param name 
	 * @param wormInfo 
	 */
	public Player(final String name, final WormInfo wormInfo) {
		if (name == null || wormInfo == null)
			throw Util.NULL_ARGUMENT_EXCEPTION;
		
		if (nameTooShort(name)) throw new IllegalArgumentException(
			"The player name must not be less than " + MIN_NAME_LENGTH + " " +
			"characters long."
		);
		
		if (nameTooLong(name)) throw new IllegalArgumentException(
			"The player name must not be more than " + MAX_NAME_LENGTH + " " +
			"characters long."
		);
		
		this.name = name;
		this.wormInfo = wormInfo;
	}
	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return
	 */
	public WormInfo getWormInfo() {
		return wormInfo;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public static boolean nameTooLong(final String name) {
		return name.length() > MAX_NAME_LENGTH;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public static boolean nameTooShort(final String name) {
		return name.length() < MIN_NAME_LENGTH;
	}	
}
/**
 * 
 */
package loach.model.info;

import util.Util;


/**
 * LoachM - WormInfo
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class WormInfo {
	/**
	 * Worm information for the blue worm. 
	 */
	public static final WormInfo
		BLUE_WORM_INFO = new WormInfo("Blue worm", "Mr. Blue")
	;

	/**
	 * Worm information for the bogey worm. 
	 */
	public static final WormInfo
		BOGEY_WORM_INFO = new WormInfo("Bogey worm", "Mr. Bogey")
	;

	/**
	 * Worm information for the black worm. 
	 */
	public static final WormInfo
		GREEN_WORM_INFO = new WormInfo("Green worm", "Mr. Green")
	;

	/**
	 * Worm information for the bogey worm. 
	 */
	public static final WormInfo
		GREY_WORM_INFO = new WormInfo("Grey worm", "Mr. Grey")
	;

	/**
	 * Worm information for the bogey worm. 
	 */
	public static final WormInfo
		RED_WORM_INFO = new WormInfo("Red worm", "Mr. Red")
	;

	/**
	 * Worm information for the bogey worm. 
	 */
	public static final WormInfo
		WHITE_WORM_INFO = new WormInfo("White worm", "Mr. White")
	;
	
	/**
	 * Worm information for the banana worm. 
	 */
	public static final WormInfo
		BANANA_WORM_INFO = new WormInfo("Banana worm", "Mr. Banana")
	;

	public static final WormInfo[] WORM_INFOS = new WormInfo[] {
		BLUE_WORM_INFO, BOGEY_WORM_INFO, GREEN_WORM_INFO,
		GREY_WORM_INFO, RED_WORM_INFO, WHITE_WORM_INFO,
		BANANA_WORM_INFO
	};
	
	private final String name, cpuPlayerName;
	
	/**
	 * @param name 
	 * @param cpuPlayerName 
	 */
	public WormInfo(final String name, final String cpuPlayerName) {
		if (cpuPlayerName == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		this.name = name;
		this.cpuPlayerName = cpuPlayerName;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the cpu player name
	 */
	public String getCPUPlayerName() {
		return cpuPlayerName;
	}
}
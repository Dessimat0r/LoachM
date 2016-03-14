/**
 * 
 */
package loach.ui.midp2.screen.title.settings;

import loach.model.info.WormInfo;
import loach.model.player.Player;
import loach.ui.midp2.db.LoachDB;
import loach.ui.midp2.db.Persistent;
import loach.ui.midp2.viewinfo.WormViewInfo;

/**
 * LoachM - PlayerSettingss
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class PlayerSettings implements Persistent {
	public static final int PLAYER_TYPE_HUMAN = 0;
	public static final int PLAYER_TYPE_CPU = 1;
	public static final int PLAYER_TYPE_DISABLED = 2;
	
	private final int playerNo;
	
	private WormViewInfo wormViewInfo;
	private int playerType;
	
	private String humanName;
	
	private final String
		playerTypeDBLabel,
		wormViewInfoDBLabel,
		humanNameDBLabel
	;
	
	
	/**
	 * @param playerNo 
	 */
	public PlayerSettings(final int playerNo) {		
		this.playerNo = playerNo;
		this.playerType = getDefaultPlayerType(playerNo);
		this.wormViewInfo = WormViewInfo.WORM_VIEW_INFOS[playerNo - 1];
		
		humanName = "Player " + playerNo;
		
		final String dbLabelPrefix = "player[" + playerNo + "]_";
		
		playerTypeDBLabel = dbLabelPrefix + "playertype";
		wormViewInfoDBLabel = dbLabelPrefix + "wormviewinfo";
		humanNameDBLabel = dbLabelPrefix + "humanname";
		
	}
	
	/**
	 * @param playerType the playerType to set
	 */
	public void setPlayerType(final int playerType) {
		this.playerType = playerType;
	}
	
	/**
	 * @param humanName the human name to set
	 */
	public void setHumanName(final String humanName) {
		this.humanName = humanName;
	}
	
	/**
	 * @return the human name
	 */
	public String getHumanName() {
		return humanName;
	}
	
	/**
	 * @param wormViewInfo the wormViewInfo to set
	 */
	public void setWormViewInfo(final WormViewInfo wormViewInfo) {
		this.wormViewInfo = wormViewInfo;
	}
	
	/**
	 * @return the playerNo
	 */
	public int getPlayerNo() {
		return playerNo;
	}
	
	/**
	 * @return the playerType
	 */
	public int getPlayerType() {
		return playerType;
	}
	
	/**
	 * @return the wormViewInfo
	 */
	public WormViewInfo getWormViewInfo() {
		return wormViewInfo;
	}
	 
	/**
	 * @param loachDB
	 */
	public void push(final LoachDB loachDB) {
		loachDB.put(wormViewInfoDBLabel, String.valueOf(wormViewInfo.getID()));
		loachDB.put(playerTypeDBLabel, String.valueOf(playerType));
		loachDB.put(humanNameDBLabel, humanName);
	}
	
	/**
	 * @param loachDB
	 */
	public void pull(final LoachDB loachDB) {
		String tmpVal;
		
		tmpVal = loachDB.get(wormViewInfoDBLabel);
		if (tmpVal != null)
			wormViewInfo = WormViewInfo.findWormViewInfoWithID(
				Integer.parseInt(tmpVal)
			);
		
		tmpVal = loachDB.get(humanNameDBLabel);
		if (tmpVal != null) humanName = tmpVal;
		
		tmpVal = loachDB.get(playerTypeDBLabel);
		if (tmpVal != null) playerType = Integer.parseInt(tmpVal);		
		
	}
	
	private static int getDefaultPlayerType(final int playerNo) {
		if (playerNo == 1) return PLAYER_TYPE_HUMAN;
		else if (playerNo > 1 && playerNo <= 4) return PLAYER_TYPE_CPU;
		else return PLAYER_TYPE_DISABLED;
	}
	
	/**
	 * @return
	 */
	public Player createPlayer() {
		if (playerType == PlayerSettings.PLAYER_TYPE_DISABLED) return null;
		
		final WormInfo wi = wormViewInfo.getWormInfo();
		
		return new Player(
			playerType == PlayerSettings.PLAYER_TYPE_HUMAN ?
			humanName :
			wi.getCPUPlayerName(),
			wi
		);
	}
}
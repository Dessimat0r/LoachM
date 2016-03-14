/**
 * 
 */
package loach.ui.midp2.screen.title.settings;

import loach.model.field.Field;
import loach.model.match.Match;
import loach.model.match.MatchPeriodInfo;
import loach.model.player.Player;
import loach.ui.midp2.db.LoachDB;
import loach.ui.midp2.db.Persistent;

/**
 * LoachM - MatchSettings
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class MatchSettings implements Persistent {
	private static final String
		DOOR_POLICY_DB_LABEL = "match_doorpolicy",
		MATCH_LENGTH_DB_LABEL = "match_timelength",
		WORM_GROWTH_TYPE_DB_LABEL = "match_wormgrowthtype"
	;
	
	private static final int
		MATCH_LENGTH_SHORT = 0,
		MATCH_LENGTH_MEDIUM = 1,
		MATCH_LENGTH_LONG = 2
	;
	
	private int
		doorPolicy = Field.DOORS_OPEN_BY_KEY,
		wormGrowthType = Match.WORM_GROWTH_TYPE_AUTO
	;
	
	private MatchPeriodInfo matchPeriodInfo = MATCH_PERIOD_INFO_SHORT;
	
	/**
	 * Short match length match period info.
	 */
	public static final MatchPeriodInfo
		MATCH_PERIOD_INFO_SHORT = new MatchPeriodInfo(5000, 105000, 10000)
	;
	/**
	 * Medium match length match period info.
	 */
	public static final MatchPeriodInfo
		MATCH_PERIOD_INFO_MEDIUM = new MatchPeriodInfo(10000, 210000, 20000)
	;
	/**
	 * Long match length match period info.
	 */
	public static final MatchPeriodInfo
		MATCH_PERIOD_INFO_LONG = new MatchPeriodInfo(15000, 315000, 30000)
	;
	
	private static final MatchPeriodInfo[]
		MATCH_PERIOD_INFOS = new MatchPeriodInfo[] {
			MATCH_PERIOD_INFO_SHORT,
			MATCH_PERIOD_INFO_MEDIUM,
			MATCH_PERIOD_INFO_LONG
		}
	;
	
	/**
	 * @param doorPolicy the doorPolicy to set
	 */
	public void setDoorPolicy(final int doorPolicy) {
		this.doorPolicy = doorPolicy;
	}
	
	/**
	 * @return the doorPolicy
	 */
	public final int getDoorPolicy() {
		return doorPolicy;
	}
	
	/**
	 * @param loachDB
	 */
	public void push(final LoachDB loachDB) {
		loachDB.put(DOOR_POLICY_DB_LABEL, String.valueOf(doorPolicy));
		
		final int matchLength;
		
		if (matchPeriodInfo == MATCH_PERIOD_INFO_SHORT)
			matchLength = MATCH_LENGTH_SHORT;
		else if (matchPeriodInfo == MATCH_PERIOD_INFO_MEDIUM)
			matchLength = MATCH_LENGTH_MEDIUM;
		else if (matchPeriodInfo == MATCH_PERIOD_INFO_LONG)
			matchLength = MATCH_LENGTH_LONG;
		else matchLength = MATCH_LENGTH_SHORT;

		loachDB.put(MATCH_LENGTH_DB_LABEL, String.valueOf(matchLength));
		loachDB.put(DOOR_POLICY_DB_LABEL, String.valueOf(doorPolicy));
		loachDB.put(WORM_GROWTH_TYPE_DB_LABEL, String.valueOf(wormGrowthType));
	}
	
	/**
	 * @param loachDB
	 */
	public void pull(final LoachDB loachDB) {
		final String doorPolicyStr = loachDB.get(DOOR_POLICY_DB_LABEL);
		if (doorPolicyStr != null) doorPolicy = Integer.parseInt(doorPolicyStr);
		
		final String matchLengthStr = loachDB.get(MATCH_LENGTH_DB_LABEL);
		if (matchLengthStr != null) {
			final int matchLength = Integer.parseInt(matchLengthStr);
			switch (matchLength) {
				case MATCH_LENGTH_SHORT:
					matchPeriodInfo = MATCH_PERIOD_INFO_SHORT; break;
				case MATCH_LENGTH_MEDIUM:
					matchPeriodInfo = MATCH_PERIOD_INFO_MEDIUM; break;
				case MATCH_LENGTH_LONG:
					matchPeriodInfo = MATCH_PERIOD_INFO_LONG; break;
			}
		}
		
		final String wormGrowthTypeStr = loachDB.get(WORM_GROWTH_TYPE_DB_LABEL);
		if (wormGrowthTypeStr != null)
			wormGrowthType = Integer.parseInt(wormGrowthTypeStr);
	}
	
	/**
	 * @param matchPeriodInfo the matchPeriodInfo to set
	 */
	public void setMatchPeriodInfo(
		final MatchPeriodInfo matchPeriodInfo
	) {
		this.matchPeriodInfo = matchPeriodInfo;
	}
	
	/**
	 * @return the matchPeriodInfo
	 */
	public MatchPeriodInfo getMatchPeriodInfo() {
		return matchPeriodInfo;
	}
	
	/**
	 * @return the wormGrowthType
	 */
	public int getWormGrowthType() {
		return wormGrowthType;
	}
	
	/**
	 * @param wormGrowthType the wormGrowthType to set
	 */
	public void setWormGrowthType(int wormGrowthType) {
		this.wormGrowthType = wormGrowthType;
	}
	
	/**
	 * @param players
	 * @return
	 */
	public final Match createMatch(final Player[] players) {
		return new Match(players, doorPolicy, matchPeriodInfo, wormGrowthType);	
	}
}
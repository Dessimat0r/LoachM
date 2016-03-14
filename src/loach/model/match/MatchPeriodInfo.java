/**
 * 
 */
package loach.model.match;

/**
 * LoachM - MatchPeriodSettings
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class MatchPeriodInfo {
	public static final int PERIOD_START_OFF = 0;
	public static final int PERIOD_GAME_TIME = 1;
	public static final int PERIOD_EXTRA_TIME = 2;
	public static final int PERIOD_KILL_TIME = 3;
	
	public static final int[] PERIODS = {
		PERIOD_START_OFF,
		PERIOD_GAME_TIME,
		PERIOD_EXTRA_TIME,
		PERIOD_KILL_TIME
	};
	
	private final long
		startOffPeriodLengthMS,
		gameTimePeriodLengthMS,
		extraTimePeriodLengthMS
	;
	
	private long
		startOffPeriodStartMS,
		gameTimePeriodStartMS,
		extraTimePeriodStartMS,
		killTimePeriodStartMS
	;
	
	private final long totalLengthMS;
	
	/**
	 * @param startOffPeriodLengthMS 
	 * @param gameTimeLengthMS 
	 * @param extraTimeLengthMS 
	 */
	public MatchPeriodInfo(
		final long startOffPeriodLengthMS,
		final long gameTimeLengthMS,
		final long extraTimeLengthMS
	) {
		this.startOffPeriodLengthMS = startOffPeriodLengthMS;
		this.gameTimePeriodLengthMS = gameTimeLengthMS;
		this.extraTimePeriodLengthMS = extraTimeLengthMS;
		
		totalLengthMS =
			startOffPeriodLengthMS +
			gameTimeLengthMS +
			extraTimeLengthMS
		;
		
		startOffPeriodStartMS = 0;
		gameTimePeriodStartMS = startOffPeriodStartMS + startOffPeriodLengthMS;
		extraTimePeriodStartMS = gameTimePeriodStartMS + gameTimeLengthMS;
		killTimePeriodStartMS = extraTimePeriodStartMS + extraTimeLengthMS;		
	}
	
	/**
	 * @return the startOffPeriodLengthMS
	 */
	public long getStartOffPeriodLengthMS() {
		return startOffPeriodLengthMS;
	}
	
	/**
	 * @return the gameTimePeriodLengthMS
	 */
	public long getGameTimePeriodLengthMS() {
		return gameTimePeriodLengthMS;
	}
	
	/**
	 * @return the extraTimePeriodLengthMS
	 */
	public long getExtraTimePeriodLengthMS() {
		return extraTimePeriodLengthMS;
	}
	
	/**
	 * @return the startOffPeriodStartMS
	 */
	public long getStartOffPeriodStartMS() {
		return startOffPeriodStartMS;
	}
	
	/**
	 * @return the gameTimePeriodStartMS
	 */
	public long getGameTimePeriodStartMS() {
		return gameTimePeriodStartMS;
	}
	
	/**
	 * @return the extraTimePeriodStartMS
	 */
	public long getExtraTimePeriodStartMS() {
		return extraTimePeriodStartMS;
	}
	
	/**
	 * @return the killTimePeriodStartMS
	 */
	public long getKillTimePeriodStartMS() {
		return killTimePeriodStartMS;
	}
	
	/**
	 * @return the totalLengthMS
	 */
	public long getTotalLengthMS() {
		return totalLengthMS;
	}
	
	/**
	 * Gives the period that encapsulates the given time MS.
	 * @param timeMS 
	 * @return
	 */
	public int obtainPeriodForTimeMS(final long timeMS) {
		/* check time used against time period boundaries */
		if (
			timeMS >= startOffPeriodStartMS &&
			timeMS < gameTimePeriodStartMS
		) return PERIOD_START_OFF;
		else if (
			timeMS >= gameTimePeriodStartMS &&
			timeMS < extraTimePeriodStartMS
		) return PERIOD_GAME_TIME;
		else if (
			timeMS >= extraTimePeriodStartMS &&
			timeMS < killTimePeriodStartMS
		) return PERIOD_EXTRA_TIME;
		else return PERIOD_KILL_TIME;
	}
}
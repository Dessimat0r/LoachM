/**
 * 
 */
package loach.model.match;

import java.util.Hashtable;

import loach.model.field.Field;
import loach.model.field.object.Block;
import loach.model.field.object.Pickup;
import loach.model.info.BlockInfo;
import loach.model.info.item.LoachItem;
import loach.model.match.worm.Worm;
import loach.model.player.Player;
import util.Util;

/**
 * LoachM - Match
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class Match {	
	private boolean
		activated = false, disposed = false,
		playerExiting = false
	;
	
	private final Worm exitingWorm = null;
	
	private static final int[][] WORM_START_POSITIONS = new int[][]{
		{10, 5}, {10, 10}, {10, 15}, {10, 20},
		{20, 10}, {20, 15}, {20, 20}, {20, 25}
	};
	
	private static final int
		KEY_SPAWN_PROBABILITY = 3000,
		BLOCK_SPAWN_PROBABILITY = 2000
	;
	
	private MatchEventListener matchEventListener = null;
	
	private static final long
		PICKUP_SPAWN_ROLL_DELAY_MS = 5100,
		BLOCK_SPAWN_ROLL_DELAY_MS = 5200,
		KEY_SPAWN_ROLL_DELAY_MS = 5300
	;
	
	private long
		lastPickupSpawnRollMS = 0,
		lastBlockSpawnRollMS = 0,
		lastKeySpawnRollMS = 0,
		lastExitBlockSpawnRollMS = 0,
		initTimeMS,
		startTimeMS,
		timeUsedMS = 0,
		updateStartTimeMS
	;
	
	/** Auto worm growth */
	public static final int	WORM_GROWTH_TYPE_AUTO = 0;
	
	/** Partitioned worm growth */
	public static final int WORM_GROWTH_TYPE_PARTS = 1;
	
	private int frames = 0;
	
	public static final int
		MAX_MATCH_PLAYERS = 8,
	
		MATCH_STATE_STARTING = 0,
		MATCH_STATE_STARTED = 1,
		MATCH_STATE_FINISHED = 2
	;
	
	private static final int
		WORM_DEATH_MONEY_EXPLOSION_AREA_W = 7,
		WORM_DEATH_MONEY_EXPLOSION_AREA_H = 7
	;	
	
	private int
		currentPeriod = MatchPeriodInfo.PERIOD_START_OFF,
		matchState = MATCH_STATE_STARTING
	;
	
	private final Field field;
	
	private final Player[] players;
	private final Worm[] worms;
	private final Hashtable playerToWormMap;
	
	private final MatchPeriodInfo matchPeriodInfo;

	private final int wormGrowthType;
	
	private final int[]
		foundPos = new int[2],
		explosionArea = new int[4]
	;
	
	/**
	 * @param players 
	 * @param doorPolicy 
	 * @param matchPeriodInfo 
	 * @param wormGrowthType 
	 */
	public Match(
		final Player[] players,
		final int doorPolicy,
		final MatchPeriodInfo matchPeriodInfo,
		final int wormGrowthType
	) {
		if (players == null || matchPeriodInfo == null)
			throw Util.NULL_ARGUMENT_EXCEPTION;
		
		if (players.length < 1) throw new IllegalArgumentException(
			"A match must have one or more players."
		);
		
		this.players = players;
		this.matchPeriodInfo = matchPeriodInfo;
		this.wormGrowthType = wormGrowthType;
		
		initTimeMS = System.currentTimeMillis();
		
		field = new Field(
			doorPolicy,
			Util.getRandomInt(0, Field.TILE_COLS - 1) * Field.FLOOR_TILE_WIDTH,
			Util.getRandomInt(0, Field.TILE_ROWS - 1) * Field.FLOOR_TILE_HEIGHT
		);
		
		worms = new Worm[players.length];
		
		playerToWormMap = new Hashtable(players.length);
		
		Player player;
		Worm worm;
		
		for (int i = 0; i < players.length; i++) {
			player = players[i];
			worm = new Worm(
				this, player,
				WORM_START_POSITIONS[i][0],
				WORM_START_POSITIONS[i][1],
				50
			);
			worms[i] = worm;
			playerToWormMap.put(player, worm);
		}
	}
	
	/**
	 * @return the matchPeriodInfo
	 */
	public MatchPeriodInfo getMatchPeriodInfo() {
		return matchPeriodInfo;
	}
	
	private void rollKeySpawn() {
		if (Util.rollUltraDie() < KEY_SPAWN_PROBABILITY) {
			if (field.findFreeRandomAreaPos(
				1, 1, 0, 0, Field.WIDTH, Field.HEIGHT,
				0, 0, 1, 1, false, foundPos
			)) field.spawnKey(foundPos[0], foundPos[1]);
		}
	}
	
	private void rollBlockSpawn() {
		if (Util.rollUltraDie() < BLOCK_SPAWN_PROBABILITY) {
			//final BlockInfo bi;
			final int rand = Util.getRandomInt(0, 1);
			
			randomlySpawnRegularBlock(
				rand == 0 ?
				BlockInfo.NORMAL_BLOCK_INFO :
				BlockInfo.SMALL_BLOCK_INFO
			);
		}
	}
	
	private void rollPickupSpawn(final LoachItem item) {
		if (item == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		final int rand = Util.rollUltraDie();
		if (rand < item.getSpawnProbability()) randomlySpawnPickup(item);
	}
	
	private Pickup randomlySpawnPickup(final LoachItem item) {
		if (item == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		if (field.findFreeRandomAreaPos(
			item.getFieldWidth(),
			item.getFieldHeight(),
			0, 0,
			Field.WIDTH, Field.HEIGHT,
			0, 0,
			1, 1,
			false,
			foundPos
		)) return spawnPickup(item, foundPos[0], foundPos[1]);
		
		return null;
	}
	
	private Block randomlySpawnRegularBlock(final BlockInfo blockInfo) {
		if (blockInfo == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		final int
			stepX = Field.FLOOR_TILE_WIDTH,
			stepY = Field.FLOOR_TILE_HEIGHT,
			offsetX,
			offsetY,
			blockWidth = blockInfo.getFieldWidth(),
			blockHeight = blockInfo.getFieldHeight()
		;
		
		switch (blockInfo.getSpawnPosStrategy()) {
			case Field.AREA_SEARCH_STRATEGY_TILE_TOP_LEFT:
				offsetX = 0;
				offsetY = 0;
				break;
			case Field.AREA_SEARCH_STRATEGY_TILE_CENTERED:
				offsetX = (Field.FLOOR_TILE_WIDTH / 2) - (blockWidth / 2);
				offsetY = (Field.FLOOR_TILE_HEIGHT / 2) - (blockHeight / 2);
				break;
			default:
				throw new IllegalArgumentException();
		}
		
		if (field.findFreeRandomAreaPos(
			blockWidth,
			blockHeight,
			0, 0,
			Field.WIDTH, Field.HEIGHT,
			offsetX, offsetY,
			stepX, stepY,
			true,
			foundPos
		)) return spawnBlock(blockInfo, foundPos[0], foundPos[1]);
		return null;
	}
	
	/**
	 * @param matchEventListener the matchEventListener to set
	 */
	public void setMatchEventListener(
		final MatchEventListener matchEventListener
	) {
		if (activated) throw Util.NEEDS_UNACTIVATED_STATE_EXCEPTION;
		
		this.matchEventListener = matchEventListener;
	}
	
	/**
	 * 
	 */
	public void start() {
		if (!activated) throw Util.NEEDS_ACTIVATED_STATE_EXCEPTION;
		
		if (matchState != MATCH_STATE_STARTING)
			throw new IllegalStateException("Cannot restart a match.");
		
		startTimeMS = System.currentTimeMillis();
		matchState = MATCH_STATE_STARTED;
	}
	
	/**
	 * 
	 */
	public void activate() {
		if (activated) throw Util.NEEDS_UNACTIVATED_STATE_EXCEPTION;
		
		/* need to activate field before worms! */
		field.activate();
		
		/* activate all worms */
		for (int i = 0; i < worms.length; i++) worms[i].activate();
		
		activated = true;
	}

	/**
	 * @return
	 */
	public int obtainCurrentPeriod() {
		return matchPeriodInfo.obtainPeriodForTimeMS(timeUsedMS);
	}
	
	/**
	 * @param player
	 * @return The worm allocated to the given match player.
	 */
	public Worm obtainWormForPlayer(final Player player) {
		if (player == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		final Worm worm = (Worm)playerToWormMap.get(player);
		
		if (worm == null) return null;
		
		return worm;
	}
	
	public void update() {
		if (!activated) throw Util.NEEDS_ACTIVATED_STATE_EXCEPTION;
		
		updateStartTimeMS = System.currentTimeMillis();
		
		if (
			matchState == MATCH_STATE_STARTED && (
				updateStartTimeMS >
				(startTimeMS + matchPeriodInfo.getTotalLengthMS())
			)
		) dispose();
		else {
			if (matchState == MATCH_STATE_STARTED) {
				timeUsedMS = updateStartTimeMS - startTimeMS;
				
				currentPeriod = obtainCurrentPeriod();
				updateWorms();
				field.update();
				
				if (currentPeriod == MatchPeriodInfo.PERIOD_GAME_TIME) {
					if (
						field.getDoorPolicy() != Field.DOORS_OPEN_BY_KEY ||
						field.doorsOpen() ||
						matchState != MATCH_STATE_STARTED ||
						currentPeriod != MatchPeriodInfo.PERIOD_GAME_TIME ||
						field.hasKey()
					) return;
					
					if (
						(lastKeySpawnRollMS + KEY_SPAWN_ROLL_DELAY_MS) <
						updateStartTimeMS
					) {
						rollKeySpawn();
						lastKeySpawnRollMS = updateStartTimeMS;
					}
					if (
						matchState != MATCH_STATE_STARTED ||
						currentPeriod != MatchPeriodInfo.PERIOD_GAME_TIME
					) return;
					
					if (
						(lastPickupSpawnRollMS + PICKUP_SPAWN_ROLL_DELAY_MS) <
						updateStartTimeMS
					) {
						LoachItem item;
						for (int i = 0; i < LoachItem.NORMAL_ITEMS.length; i++)
						{
							item = LoachItem.NORMAL_ITEMS[i];
							rollPickupSpawn(item);
						}
						
						lastPickupSpawnRollMS = updateStartTimeMS;
					}
					if (
						matchState != MATCH_STATE_STARTED ||
						currentPeriod != MatchPeriodInfo.PERIOD_GAME_TIME
					) return;
					
					if (
						(lastBlockSpawnRollMS + BLOCK_SPAWN_ROLL_DELAY_MS) <
						updateStartTimeMS
					) {
						rollBlockSpawn();
						lastBlockSpawnRollMS = updateStartTimeMS;
					}				
				} else if (currentPeriod == MatchPeriodInfo.PERIOD_EXTRA_TIME) {
					if (field.hasKey()) field.getKey().dispose();
					if (
						!field.hasExitBlock() &&
						field.getNoOfExitBlockOccupiers() == 0
					) spawnExitBlock();
				}
				
				if (
					currentPeriod == MatchPeriodInfo.PERIOD_EXTRA_TIME &&
					!field.hasExitBlock() &&
					field.getNoOfExitBlockOccupiers() == 0
				) spawnExitBlock();
			}
		}
	}
	
	/**
	 * @return
	 */
	public int getNoOfPlayers() {
		return players.length;
	}
	
	/**
	 * 
	 */
	public void spawnExitBlock() {
		field.spawnExitBlock();
	}
	
	/**
	 * @return
	 * @see loach.model.field.Field#hasExitBlock()
	 */
	public boolean hasExitBlock() {
		return field.hasExitBlock();
	}

	/**
	 * @param x
	 * @param y
	 */
	public void spawnKey(final int x, final int y) {
		field.spawnKey(x, y);
	}
	
	/**
	 * @return the updateStartTimeMS
	 */
	public long getUpdateStartTimeMS() {
		return updateStartTimeMS;
	}
	
	/**
	 * @return the activated
	 */
	public boolean isActivated() {
		return activated;
	}
	
	private void updateWorms() {
		for (int i = 0; i < worms.length; i++) worms[i].update();		
	}

	/**
	 * @return
	 */
	public long getTimeUsedMS() {
		return timeUsedMS;
	}
	
	/**
	 * @return
	 */
	public Field getField() {
		return field;
	}
	
	private Pickup spawnPickup(
		final LoachItem item, final int pX, final int pY
	) {
		if (item == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		final Pickup pickup = new Pickup(
			field,
			item,
			pX, pY,
			20000
		);
		pickup.activate();
		
		return pickup;
		
	}
	
	private Block spawnBlock(
		final BlockInfo blockInfo, final int x, final int y
	) {
		if (blockInfo == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		final Block block = new Block(field, blockInfo, x, y);
		block.activate();
		
		return block;
	}
	
	/**
	 * @return the currentPeriod
	 */
	public int getCurrentPeriod() {
		return currentPeriod;
	}
	
	/**
	 * @return the timeRemainingMS
	 */
	public long getTimeRemainingMS() {
		return matchPeriodInfo.getTotalLengthMS() - timeUsedMS;
	}
	
	/**
	 * @param cX
	 * @param cY
	 */
	public void createWormDeathMoneyExplosion(final int cX, final int cY) {
		explosionArea[0] = cX - (WORM_DEATH_MONEY_EXPLOSION_AREA_W / 2);
		explosionArea[1] = cY - (WORM_DEATH_MONEY_EXPLOSION_AREA_H / 2);
		explosionArea[2] = WORM_DEATH_MONEY_EXPLOSION_AREA_W;
		explosionArea[3] = WORM_DEATH_MONEY_EXPLOSION_AREA_H;
		
		Util.clipRectAgainstRect(explosionArea,	Field.AREA);
		
		createMoneyExplosion(
			explosionArea[0], explosionArea[1],
			explosionArea[2], explosionArea[3],
			3
		);
	}
	
	/**
	 * @param aX 
	 * @param aY 
	 * @param aW 
	 * @param aH 
	 * @param maxTokens 
	 */
	private void createMoneyExplosion(
		final int aX, final int aY,
		final int aW, final int aH,
		final int maxTokens
	) {
		final int[] spawnPos = new int[2];
		int spawns = 0;
		LoachItem moneyItem;
		
		for (int i = 0; i < 10; i++) {
			moneyItem = LoachItem.MONEY_ITEMS[
				Util.getRandomInt(0, LoachItem.MONEY_ITEMS.length - 1)
			];
			if (!field.findFreeRandomAreaPos(
				1, 1, aX, aY, aW, aH, 0, 0, 1, 1, false, spawnPos
			)) continue;
			spawnPickup(moneyItem, spawnPos[0], spawnPos[1]);
			
			if (++spawns >= maxTokens) return;
		}
	}

	/**
	 * 
	 */
	public void dispose() {
		if (disposed) throw Util.NEEDS_UNDISPOSED_STATE_EXCEPTION;
		disposed = true;
	}

	/**
	 * @return
	 */
	public boolean isDisposed() {
		return disposed;
	}
	
	/**
	 * @return the wormGrowthType
	 */
	public int getWormGrowthType() {
		return wormGrowthType;
	}
	
	/**
	 * @return the worms
	 */
	public Worm[] getWorms() {
		return worms;
	}
	
	/**
	 * @return the players
	 */
	public Player[] getPlayers() {
		return players;
	}
	
	/**
	 * @return the matchState
	 */
	public int getMatchState() {
		return matchState;
	}
	
	/**
	 * @return the initTimeMS
	 */
	public long getInitTimeMS() {
		return initTimeMS;
	}
}
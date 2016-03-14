/**
 * 
 */
package loach.model.match.worm;

import java.util.Vector;

import loach.model.field.Field;
import loach.model.field.object.FieldObject;
import loach.model.field.object.Pickup;
import loach.model.field.object.WormSegment;
import loach.model.info.WormInfo;
import loach.model.info.item.LoachItem;
import loach.model.match.Match;
import loach.model.player.Player;
import util.Util;
import util.struct.Deque;
import util.struct.NewStack;

/**
 * LoachM - Worm
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class Worm {
	public static final int MAX_NO_OF_SEGMENTS = 200;
	
	public final static int NOT_COLLIDING = 0;
	public final static int COLLIDING_WITH_WALL = 1;
	public final static int COLLIDING_WITH_FIELD_OBJECT = 2;
	
	private static final int WORM_CRASH_SEGMENT_DECIMATION_AMOUNT = 3;
	private final Match match;
	private final Deque segments = new Deque(MAX_NO_OF_SEGMENTS);
	
	public static final IllegalArgumentException
		ILLEGAL_STARTING_POSITION_EXCEPTION = new IllegalArgumentException(
			"Could not spawn worm at starting position."
		)
	;
	
	public static final IllegalStateException
		NEEDS_WORM_ALIVE_STATE_EXCEPTION = new IllegalStateException(
			"Worm needs to be alive."
		)
	;
	
	public static final IllegalStateException
		NEEDS_WORM_DEAD_STATE_EXCEPTION = new IllegalStateException(
			"Worm needs to be dead."
		)
	;	

	/** Levels of the standard delay in ms between each growth and shrinkage. */
	public static final int[] SPEED_LEVELS = {180, 140, 100, 60}; 
	
	private static final long
		GROWTH_PERIOD_DELAY_MS = 1000,
		START_GROWTH_PERIOD_MS = 100,
		GROWTH_PERIOD_MS_INJ = 5000,
		NORMAL_GROWTH_DELAY_MS = 15000
	;
	
	private final int bounty;
	
	private int
		direction = Util.DIRECTION_NONE,
		heading = Util.DIRECTION_EAST,
		speedLevel = 0,
		headX, headY,
		money
	;
	
	private final int startX, startY;
	
	private final WormSegment firstSegment;
	
	private long
		growthPeriodLengthMS = GROWTH_PERIOD_MS_INJ,
		growthPeriodStartMS = 0,
		lastMovTimeMS = 0,
		lastNormalGrowthTimeMS = 0
	;
	
	private final Player player;
	private final WormInfo wormInfo;
	
	private final Field field;
	
	private final NewStack kills;
	
	private boolean
		alive = true,
		activated = false,
		disposed = false,
		exited = false
	;
	
	private Vector tmpWormEventListeners = new Vector(5);
	private WormEventListener[] wormEventListeners;
	
	/**
	 * 
	 */
	public void addWormEventListener(final WormEventListener wel) {
		if (wel == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		if (activated) throw new IllegalStateException(
			"Cannot add worm event listener after activation."
		);
		
		tmpWormEventListeners.addElement(wel);
	}
	
	/**
	 * Creates a new Worm object.
	 * 
	 * @param match
	 * @param player 
	 * @param startX 
	 * @param startY 
	 * @param bounty The bounty for destroying this worm.
	 */
	public Worm(
		final Match match, final Player player,
		final int startX, final int startY,
		final int bounty
	) {		
		if (match == null || player == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		this.match = match;
		this.player = player;
		this.bounty = bounty;
		this.startX = startX;
		this.startY = startY;
		
		wormInfo = player.getWormInfo();
		field = match.getField();
		
		kills = new NewStack(match.getNoOfPlayers() - 1);
		
		/*
		
		final Object[]
			startAreaStatus = field.obtainAreaStatus(startX, startY, 1, 1)
		;
		 		
		if (startAreaStatus[0] != Field.AREA_STATUS_EMPTY_OBJ)
			throw ILLEGAL_STARTING_POSITION_EXCEPTION;
		
		*/
		
		firstSegment = new WormSegment(this, startX, startY, heading);
		segments.addFirst(firstSegment);
		
		headX = startX;
		headY = startY;
	}
	
	/**
	 * Checks whether the worm has the maximum number of segments.
	 * @return Returns true if the worm has the maximum number of segments.
	 */
	public boolean hasMaxSegments() {
		return segments.size() == MAX_NO_OF_SEGMENTS;
	}
	
	//protected getSpeedLevelGenDelayTicks(int speedLevel) {
	//	return speedLevel
	//}
	
	/**
	 */
	public void activate() {
		if (activated) throw Util.NEEDS_UNACTIVATED_STATE_EXCEPTION;
		
		/* transfer worm event listeners to array */
		wormEventListeners =
			new WormEventListener[tmpWormEventListeners.size()]
		;
		tmpWormEventListeners.copyInto(wormEventListeners);
		tmpWormEventListeners = null;
		
		activated = true;
		firstSegment.activate();
	}
	
	/**
	 * @return the wormInfo
	 */
	public WormInfo getWormInfo() {
		return wormInfo;
	}
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * 
	 */
	public void update() {
		if (!activated) throw Util.NEEDS_ACTIVATED_STATE_EXCEPTION;
		
		if (alive && !exited) {
			if (
				match.getMatchState() == Match.MATCH_STATE_STARTED &&
				heading != Util.DIRECTION_NONE
			) {
				final long matchUpdateStartTime = match.getUpdateStartTimeMS();
				
				if (
					(lastNormalGrowthTimeMS + NORMAL_GROWTH_DELAY_MS) <
					matchUpdateStartTime
				) {
					grow(GROWTH_PERIOD_DELAY_MS);
					lastNormalGrowthTimeMS = matchUpdateStartTime;
				}
				
				if (
					alive && (
						(lastMovTimeMS + SPEED_LEVELS[speedLevel]) <
						matchUpdateStartTime
					)
				) {
					move();
					lastMovTimeMS = matchUpdateStartTime;
				}
			} else {
				if (direction != Util.DIRECTION_NONE) heading = direction;
				direction = Util.DIRECTION_NONE;
			}
		}
	}
	
	private void move() {
		if (direction != Util.DIRECTION_NONE) heading = direction;
		direction = Util.DIRECTION_NONE;
		
		if (hasMaxSegments()) decimateTail(1);
		if (alive) {
			spawnNextSegment();
			if (
				alive && (
					growthPeriodStartMS == 0 || (
						(growthPeriodStartMS + growthPeriodLengthMS) <
						match.getUpdateStartTimeMS()
					)
				)
			) {
				growthPeriodStartMS = 0;
				growthPeriodLengthMS = 0;
				
				if (alive) decimateTail(1);
			}			
		}
	}
	
	/**
	 * @return the heading
	 */
	public int getHeading() {
		return heading;
	}
	
	/**
	 * @param pos 
	 * @return
	 */
	public boolean calculateNextSegmentPos(final int[] pos) {
		switch (heading) {
			case Util.DIRECTION_NORTH:
				pos[0] = headX; pos[1] = headY - 1; return true;
			case Util.DIRECTION_EAST:
				pos[0] = headX + 1;  pos[1] = headY; return true;
			case Util.DIRECTION_SOUTH:
				pos[0] = headX;  pos[1] = headY + 1; return true;
			case Util.DIRECTION_WEST:
				pos[0] = headX - 1; pos[1] = headY; return true;
		}
		return false;
	}
	
	/**
	 * @return Returns the head segment of the worm.
	 */
	public WormSegment getHeadSegment() {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		return (WormSegment)segments.getFirst();
	}
	
	/**
	 * @return Returns the tail segment of the worm.
	 */
	public WormSegment getTailSegment() {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		return (WormSegment)segments.getLast();
	}
	
	private Object[] spawnNextSegment() {
		final int[] nextSegPos = new int[2];
		calculateNextSegmentPos(nextSegPos);
		return spawnAtHead(nextSegPos[0], nextSegPos[1]);
	}
	
	private Object[] spawnAtHead(final int x, final int y) {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		Object[] areaStatus = field.obtainAreaStatus(x, y, 1, 1);
		Object areaStatusMainObj = areaStatus[0];
		
		if (areaStatusMainObj == Field.AREA_STATUS_INTERSECTS_FO_OBJ) {
			final FieldObject fo = (FieldObject)areaStatus[1];
			dispatchFieldObjectCollisionEvent(this, fo);
			crash();		
		} else if (
			areaStatusMainObj == Field.AREA_STATUS_INTERSECTS_PICKUP_OBJ
		) {
			final Pickup pickup = (Pickup)areaStatus[1];
			final LoachItem loachItem = pickup.getItem();
			
			switch (loachItem.getLoachItemID()) {
				case LoachItem.LOACH_ITEM_ID_GROWTH_PILL:
					grow(100);
					break;
				case LoachItem.LOACH_ITEM_ID_SPEEDUP_PILL:
					{
						int speedupAmount = 1;
						
						if ((speedLevel + speedupAmount) > SPEED_LEVELS.length)
							speedupAmount = SPEED_LEVELS.length - speedLevel;
						
						speedup(speedupAmount);
					}
					break;
				case LoachItem.LOACH_ITEM_ID_SLOWDOWN_PILL:
					{
						int slowdownAmount = 1;
						
						if ((speedLevel - slowdownAmount) < 0)
							slowdownAmount = speedLevel;
						
						slowdown(slowdownAmount);
					}
					break;
				case LoachItem.LOACH_ITEM_ID_MONEY_100:
					addMoney(100);
					break;
				case LoachItem.LOACH_ITEM_ID_MONEY_200:
					addMoney(200);
					break;					
				case LoachItem.LOACH_ITEM_ID_MONEY_300:
					addMoney(300);
					break;
				case LoachItem.LOACH_ITEM_ID_MONEY_400:
					addMoney(400);
					break;
				case LoachItem.LOACH_ITEM_ID_KEY:
					if (field.getDoorPolicy() == Field.DOORS_OPEN_BY_KEY)
						field.openDoors();
			}
			
			pickup.dispose();
			createWormSegmentAtHead(x, y);
		} else if (areaStatusMainObj == Field.AREA_STATUS_INTERSECTS_WALL_OBJ) {
			dispatchFieldWallCollisionEvent(this);
			crash();			
		} else if (areaStatusMainObj == Field.AREA_STATUS_INTERSECTS_DOOR_OBJ) {
			if (field.doorsOpen()) {
				final int[] area = new int[] {x, y, 1, 1};
				field.normaliseArea(area);
				spawnAtHead(area[0], area[1]);
			} else {
				dispatchFieldWallCollisionEvent(this);
				crash();
			}
		} else if (areaStatusMainObj == Field.AREA_STATUS_EMPTY_OBJ)
			createWormSegmentAtHead(x, y);
		else throw new Error(
				"Could not handle given area status."
			);
		
		return new Object[]{ areaStatus }; /* spawn status */
	}
	
	private WormSegment createWormSegmentAtHead(final int x, final int y) {
		final WormSegment segment = new WormSegment(this, x, y, heading);
		
		segments.addFirst(segment);
		headX = x;
		headY = y;
		
		dispatchWormSegmentAdditionEvent(segment);
		segment.activate();
		
		return segment;
	}

	/**
	 * @return
	 */
	public boolean hasSegments() {
		return !segments.isEmpty();
	}

	/**
	 * Decimates the worm by a given number of segments.
	 * @param amount The number of segments to remove.
	 */
	public void decimateTail(final int amount) {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		if (amount > segments.size()) throw new IllegalArgumentException(
			"Cannot remove more segments than the worm has."
		);
		
		WormSegment ws;
		for (int i = 0; i < amount; i++) {
			ws = (WormSegment)segments.removeLast();
			dispatchWormSegmentSubtractionEvent(ws);
			ws.dispose();
		}
		
		if (segments.size() <= 1) die();
	}
	
	/**
	 * Returns the number of segments.
	 * @return Returns the number of segments this worm has.
	 */
	public int length() {
		return segments.size();
	}
	
	/**
	 * @return
	 */
	public boolean isAlive() {
		return alive;
	}

	/* (non-Javadoc)
	 * @see util.sys.Disposable#dispose()
	 */
	public void dispose() {
		if (!activated) throw Util.NEEDS_ACTIVATED_STATE_EXCEPTION;
		if (disposed) throw Util.NEEDS_UNDISPOSED_STATE_EXCEPTION;
		
		disposed = true;
	}

	/* (non-Javadoc)
	 * @see util.sys.Disposable#isDisposed()
	 */
	public boolean isDisposed() {
		return disposed;
	}
	
	/**
	 * @return the match
	 */
	public Match getMatch() {
		return match;
	}
	
	/**
	 * @param direction
	 */
	public void setDirection(final int direction) {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		if (!isValidDirection(direction))
			throw Util.UNEXPECTED_ARGUMENT_EXCEPTION;
		
		this.direction = direction;
	}
	
	/**
	 * 
	 */
	public void stop() {
		direction = Util.DIRECTION_NONE;
	}
	
	/**
	 * @param mDirection
	 * @return
	 */
	public boolean isValidDirection(final int mDirection) {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		return
			mDirection == Util.DIRECTION_NONE ||
			Util.obtainReverseDirection(mDirection) != heading
		;	
	}
	
	/**
	 * @param growthPeriodMSInj 
	 */
	public void grow(final long growthPeriodMSInj) {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		if (growthPeriodStartMS == 0)
			growthPeriodStartMS = match.getUpdateStartTimeMS();
		growthPeriodLengthMS += growthPeriodMSInj;
	}
	
	/**
	 * @param speedLevelInj
	 */
	public void speedup(final int speedLevelInj) {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		if ((speedLevel + speedLevelInj) >= SPEED_LEVELS.length)
			throw new IllegalArgumentException(
				"Given speed level increase exceeds maximum speed level (" +
				SPEED_LEVELS.length + ")."
			);
		
		speedLevel += speedLevelInj;
	}
	
	/**
	 * @param amount
	 */
	public void slowdown(final int amount) {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		if ((speedLevel - amount) < 0)
			throw new IllegalArgumentException(
				"Given speed level decrease is less than minimum speed level " +
				"(0)."
			);
		
		speedLevel -= amount;
	}
	
	/**
	 * @return
	 */
	public int getCurrentSpeedLevel() {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		return speedLevel;
	}
	
	/**
	 * @param speedLevel the speedLevel to set
	 */
	public void setSpeedLevel(final int speedLevel) {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		if (speedLevel < 0 || speedLevel >= SPEED_LEVELS.length)
			throw new IllegalArgumentException();
		
		this.speedLevel = speedLevel;
	}
	
	/**
	 * @return
	 */
	private int getCurrentSpeed() {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		return SPEED_LEVELS[speedLevel];
	}
	
	/**
	 * @return
	 */
	public boolean isAtMaxSpeedLevel() {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		return speedLevel == (SPEED_LEVELS.length - 1);
	}
	
	/**
	 * @return
	 */
	public boolean isAtMinSpeedLevel() {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		return speedLevel <= 0;
	}
	
	/**
	 * @param wormSegment
	 */
	public void dispatchWormSegmentAdditionEvent(
		final WormSegment wormSegment
	) {
		if (!activated) throw Util.NEEDS_ACTIVATED_STATE_EXCEPTION;
		
		for (int i = 0; i < wormEventListeners.length; i++)
			wormEventListeners[i].onWormSegmentAddition(wormSegment);
	}
	
	/**
	 * @param wormSegment
	 */
	public void dispatchWormSegmentSubtractionEvent(
		final WormSegment wormSegment
	) {
		if (!activated) throw Util.NEEDS_ACTIVATED_STATE_EXCEPTION;
		
		for (int i = 0; i < wormEventListeners.length; i++)
			wormEventListeners[i].onWormSegmentSubtraction(wormSegment);
	}
	
	/**
	 * @param worm
	 */
	public void dispatchWormDeathEvent(final Worm worm) {
		if (!activated) throw Util.NEEDS_ACTIVATED_STATE_EXCEPTION;
		
		for (int i = 0; i < wormEventListeners.length; i++)
			wormEventListeners[i].onWormDeath(this);
	}
	
	/**
	 * @param worm 
	 * @param fieldObject 
	 */
	public void dispatchFieldObjectCollisionEvent(
		final Worm worm, final FieldObject fieldObject
	) {
		if (!activated) throw Util.NEEDS_ACTIVATED_STATE_EXCEPTION;
		
		for (int i = 0; i < wormEventListeners.length; i++)
			wormEventListeners[i].onWormFieldObjectCollision(this, fieldObject);
	}
	
	/**
	 * @param worm 
	 */
	public void dispatchFieldWallCollisionEvent(final Worm worm) {
		if (!activated) throw Util.NEEDS_ACTIVATED_STATE_EXCEPTION;
		
		for (int i = 0; i < wormEventListeners.length; i++)
			wormEventListeners[i].onWormFieldWallCollision(this);
	}
	
	/**
	 * @return the direction
	 */
	public int getDirection() {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		return direction;
	}
	
	/**
	 * Causes the worm to die.
	 */
	public void die() {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		if (hasSegments()) decimateTail(segments.size());
		
		alive = false;
		dispatchWormDeathEvent(this);
		match.createWormDeathMoneyExplosion(headX, headY);
	}
	
	/**
	 * @return the headX
	 */
	public int getHeadX() {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		return headX;
	}
	
	/**
	 * @return the headY
	 */
	public int getHeadY() {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		return headY;
	}
	
	/**
	 * @return
	 */
	public int getNoOfKills() {		
		return kills.size();
	}
	
	/**
	 * @param moneyInj
	 */
	public void addMoney(final int moneyInj) {
		money += moneyInj;
	}
	
	/**
	 * @return the money
	 */
	public int getMoney() {
		return money;
	}
	
	/**
	 * @return the kills
	 */
	public NewStack getKills() {
		return kills;
	}
	
	/**
	 * 
	 */
	public void crash() {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		int
			length = segments.size(),
			amount = WORM_CRASH_SEGMENT_DECIMATION_AMOUNT
		;
	
		if ((length - amount) < 0) amount = length;
		if (amount > 0) decimateTail(amount);
	}
	
	/**
	 * @return
	 */
	public boolean isMoving() {
		if (!alive) throw NEEDS_WORM_ALIVE_STATE_EXCEPTION;
		
		return heading != Util.DIRECTION_NONE;
	}
	
	/**
	 * @return
	 */
	public boolean getExited() {
		return exited;
	}
	
	/**
	 * @return the bounty
	 */
	public int getBounty() {
		return bounty;
	}
}
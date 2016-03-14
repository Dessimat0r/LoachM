/**
 * 
 */
package loach.model.field;

import java.util.Vector;

import loach.model.field.object.ExitBlock;
import loach.model.field.object.FieldObject;
import loach.model.field.object.Pickup;
import loach.model.info.item.LoachItem;
import util.Util;
import util.struct.Grid;

/**
 * LoachM - Field
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class Field {
	public static final int H_DOOR_WIDTH = 16;
	public static final int V_DOOR_HEIGHT = 16;
	
	public static final int AREA_STATUS_EMPTY = 0;
	public static final int AREA_STATUS_INTERSECTS_FO = 1;
	public static final int AREA_STATUS_INTERSECTS_WALL = 2;
	public static final int AREA_STATUS_INTERSECTS_DOOR = 3;
	public static final int AREA_STATUS_INTERSECTS_PICKUP = 4;
	
	public static final int DOORS_OPEN_BY_KEY = 0;
	public static final int DOORS_NEVER_OPEN = 1;
	public static final int DOORS_ALWAYS_OPEN = 2;
	
	public static final int AREA_SEARCH_STRATEGY_ANYWHERE = 0;
	public static final int AREA_SEARCH_STRATEGY_TILE_TOP_LEFT = 1;
	public static final int AREA_SEARCH_STRATEGY_TILE_CENTERED = 2;
	
	public static final Integer
		AREA_STATUS_EMPTY_OBJ = new Integer(AREA_STATUS_EMPTY),
		AREA_STATUS_INTERSECTS_FO_OBJ = new Integer(AREA_STATUS_INTERSECTS_FO),
		
		AREA_STATUS_INTERSECTS_WALL_OBJ = new Integer(
			AREA_STATUS_INTERSECTS_WALL
		),
		AREA_STATUS_INTERSECTS_DOOR_OBJ = new Integer(
			AREA_STATUS_INTERSECTS_DOOR
		),
		AREA_STATUS_INTERSECTS_PICKUP_OBJ = new Integer(
			AREA_STATUS_INTERSECTS_PICKUP
		)
	;
	
	private final Grid grid;
	//protected final Tile[][] tileGrid;
	private boolean doorsOpen;
	private final int doorPolicy, exitBlockX, exitBlockY;
	private FieldEventListener fieldEventListener = null;
	
	private ExitBlock exitBlock = null;
	private Pickup key = null;
	
	private final Vector pickups = new Vector(20);
	
	public static final int FLOOR_TILE_WIDTH = 8;
	public static final int FLOOR_TILE_HEIGHT = 8;
	
	public static final int WIDTH = FLOOR_TILE_WIDTH * 6;
	public static final int HEIGHT = FLOOR_TILE_HEIGHT * 6;
	
	public static final int[] AREA = new int[] {
		0, 0, Field.WIDTH, Field.HEIGHT
	};
	
	public static final int H_WALL_WIDTH = (WIDTH - H_DOOR_WIDTH) / 2;
	public static final int V_WALL_HEIGHT = (HEIGHT - V_DOOR_HEIGHT) / 2;
	
	public static final int H_DOOR_X = (WIDTH / 2) - (H_DOOR_WIDTH / 2);
	public static final int V_DOOR_Y = (HEIGHT / 2) - (V_DOOR_HEIGHT / 2);
	
	public static final int H_WEST_WALL_X = 0;
	public static final int
		H_EAST_WALL_X =	H_WEST_WALL_X + H_WALL_WIDTH + H_DOOR_WIDTH
	;
	
	public static final int V_NORTH_WALL_Y = 0;
	public static final int
		V_SOUTH_WALL_Y = V_NORTH_WALL_Y + V_WALL_HEIGHT + V_DOOR_HEIGHT
	;
	
	public static final int TILE_ROWS = HEIGHT / FLOOR_TILE_HEIGHT;
	public static final int TILE_COLS = WIDTH / FLOOR_TILE_WIDTH;
	
	public static final int EXIT_BLOCK_WIDTH = FLOOR_TILE_WIDTH;
	public static final int EXIT_BLOCK_HEIGHT = FLOOR_TILE_HEIGHT;
	
	private static final int
		MAX_FREE_AREA_POS_SEARCH_ATTEMPTS = 3,
		MAX_FREE_POSITION_SEARCH_ATTEMPTS = 3
	;
	
	public static final IllegalArgumentException
		OUT_OF_FIELD_BOUNDS_EXCEPTION = new IllegalArgumentException(
			"Given field reference was outside field bounds."
		)
	;
	
	public static final IllegalArgumentException
		FIELD_AREA_OCCUPIED_EXCEPTION = new IllegalArgumentException(
			"The given area is occupied."
		)
	;
	
	private int noOfExitBlockAreaOccupiers = 0, noOfFieldObjects = 0;
	
	private boolean activated = false;
	
	/**
	 * @param doorPolicy The door policy to use.
	 * @param exitBlockX 
	 * @param exitBlockY 
	 */
	public Field(
		final int doorPolicy,
		final int exitBlockX, final int exitBlockY
	) {
		this.doorPolicy = doorPolicy;		
		this.exitBlockX = exitBlockX;
		this.exitBlockY = exitBlockY;
		
		doorsOpen = (doorPolicy == DOORS_ALWAYS_OPEN);
		
		grid = new Grid(HEIGHT, WIDTH);
		
		/*
		tileGrid = new Tile[tileCols][tileRows];
		
		Tile tile;
		for (int row = 0; row < tileRows; row++) {
			for (int col = 0; col < tileCols; col++) {
				tile = new Tile(
					this, new Point(col, row),
					Game.REGULAR_TILE_TILE_INFO
				);
				tileGrid[row][col] = tile;
			}
		}
		*/
	}
	
	/**
	 * @return the exitBlockX
	 */
	public int getExitBlockX() {
		return exitBlockX;
	}
	
	/**
	 * @return the exitBlockY
	 */
	public int getExitBlockY() {
		return exitBlockY;
	}
	
	/**
	 * Opens the field doors.
	 */
	public void openDoors() {
		if (doorPolicy == DOORS_NEVER_OPEN) throw new IllegalStateException(
			"The current door policy prevents the doors being opened."
		);
		if (doorsOpen) throw new IllegalStateException(
			"The doors are already open."
		);
		
		doorsOpen = true;
		dispatchDoorsOpenEvent();
	}
	
	/**
	 * @return Returns whether the doors are open or closed.
	 */
	public boolean doorsOpen() {
		return doorsOpen;
	}
	
	/**
	 * @param fieldEventListener the fieldEventListener to set
	 */
	public void setFieldEventListener(
		final FieldEventListener fieldEventListener
	) {
		if (activated) throw Util.NEEDS_UNACTIVATED_STATE_EXCEPTION;
		
		this.fieldEventListener = fieldEventListener;
	}
	
	/**
	 * 
	 */
	public void activate() {
		if (activated) throw Util.NEEDS_UNACTIVATED_STATE_EXCEPTION;
		
		activated = true;
	}
	
	/**
	 * Normalises the given area in this field.
	 * 
	 * @param area
	 */
	public void normaliseArea(final int[] area) {
		if (area == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		area[0] = (area[0] + WIDTH) % WIDTH;
		area[1] = (area[1] + HEIGHT) % HEIGHT;
	}
	
	/**
	 * @param fo
	 */
	public void dispatchFieldObjectCreationEvent(final FieldObject fo) {
		if (fo == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		if (!activated) throw Util.NEEDS_ACTIVATED_STATE_EXCEPTION;
		
		if (fieldEventListener != null)
			fieldEventListener.onFieldObjectCreation(fo);
	}
	
	/**
	 * @param fo
	 */
	public void dispatchFieldObjectDisposalEvent(final FieldObject fo) {
		if (fo == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		if (!activated) throw Util.NEEDS_ACTIVATED_STATE_EXCEPTION;
		
		if (fieldEventListener != null)
			fieldEventListener.onFieldObjectDisposal(fo);
	}
	
	/**
	 * @param fo
	 */
	public void addFieldObject(final FieldObject fo) {
		if (fo == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		final int
			foX = fo.getX(),
			foY = fo.getY(),
			foW = fo.getWidth(),
			foH = fo.getHeight()
		;
		
		grid.setInRowsCols(foY, foX, foH, foW, fo);
		
		if (fo instanceof Pickup) pickups.addElement(fo);
		
		if (areaIntersectsExitBlockArea(foX, foY, foW, foH))
			noOfExitBlockAreaOccupiers++;
		
		noOfFieldObjects++;
	}
	
	/**
	 * @param fo
	 */
	public void removeFieldObject(final FieldObject fo) {
		if (fo == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		final int
			foX = fo.getX(),
			foY = fo.getY(),
			foW = fo.getWidth(),
			foH = fo.getHeight()
		;
		
		if (areaIntersectsExitBlockArea(foX, foY, foW, foH))
			noOfExitBlockAreaOccupiers--;
		
		noOfFieldObjects--;
		
		if (fo.getFoID() == FieldObject.FO_ID_PICKUP) {
			final Pickup pickup = (Pickup)fo;
			pickups.removeElement(pickup);
			LoachItem item = pickup.getItem();
			if (item.getLoachItemID() == LoachItem.LOACH_ITEM_ID_KEY) {
				if (key != null && pickup == key) key = null;
			}
		}
		
		grid.setInRowsCols(foY, foX, foH, foW, null);
	}
	
	/**
	 * Obtains the status of a given area upon the field.
	 * 
	 * @param aX 
	 * @param aY 
	 * @param aW 
	 * @param aH 
	 * 
	 * @return
	 *   An array of Object. The first index is the status code, an Integer
	 *   object which can be either:
	 *     * Field.AREA_STATUS_INTERSECTS_WALL_OBJ,
	 *     * Field.AREA_STATUS_INTERSECTS_FO_OBJ,
	 *     * Field.AREA_STATUS_INTERSECTS_DOOR_OBJ,
	 *     * ..or Field.AREA_STATUS_EMPTY_OBJ
	 */
	public Object[] obtainAreaStatus(
		final int aX, final int aY, final int aW, final int aH
	) {
		/* check that area is in bounds. */
		if (!areaIsInBounds(aX, aY, aW, aH)) {			
			if (doorsOpen && areaIntersectsDoorArea(aX, aY, aW, aH)) {
				return new Object[] { AREA_STATUS_INTERSECTS_DOOR_OBJ };
			}
			return new Object[]{ AREA_STATUS_INTERSECTS_WALL_OBJ };
		};			
		
		final FieldObject fo = getFirstOccupierInArea(aX, aY, aW, aH);
		
		if (fo != null) {
			if (fo.getFoID() == FieldObject.FO_ID_PICKUP) {
				return new Object[] {
					AREA_STATUS_INTERSECTS_PICKUP_OBJ,
					fo
				};
			}
			return new Object[] {
				AREA_STATUS_INTERSECTS_FO_OBJ,
				fo
			};
		}

		return new Object[]{ AREA_STATUS_EMPTY_OBJ };
	}
	
	/**
	 * @param aX
	 * @param aY
	 * @param aW
	 * @param aH
	 * 
	 * @return
	 */
	public boolean areaIntersectsDoorArea(
		final int aX, final int aY,
		final int aW, final int aH
	) {
		return (
			((aY < 0) || (aY > aH)) &&
			((aX >= H_DOOR_X) && (aX < (H_DOOR_X + H_DOOR_WIDTH)))
		) || (
			((aX < 0) || (aX > aW)) &&
			((aY >= V_DOOR_Y) && (aY < (V_DOOR_Y + V_DOOR_HEIGHT)))
		);
	}
	
	/**
	 * @param aX
	 * @param aY
	 * @param aW
	 * @param aH
	 * 
	 * @return
	 */
	public boolean areaIsInBounds(
		final int aX, final int aY, final int aW, final int aH
	) {
		return Util.rectContainsRect(0, 0, WIDTH, HEIGHT, aX, aY, aW, aH);		
	}
	
	/**
	 * @param aX
	 * @param aY
	 * @param aW
	 * @param aH
	 * 
	 * @return
	 */
	public FieldObject getFirstOccupierInArea(
		final int aX, final int aY,
		final int aW, final int aH
	) {
		if (!areaIsInBounds(aY, aX, aH, aW))
			throw OUT_OF_FIELD_BOUNDS_EXCEPTION;
		
		return (FieldObject)grid.getFirstInRowsCols(aY, aX, aH, aW);
	}
	
	/**
	 * Finds a free random area with given size within the field
	 * 
	 * @param aW 
	 * @param aH 
	 * @param cX 
	 * @param cY 
	 * @param cW 
	 * @param cH 
	 * @param offsetX 
	 * @param offsetY 
	 * @param stepX 
	 * @param stepY 
	 * @param avoidExitBlockArea 
	 * @param foundPos
	 * 
	 * @return 
	 */
	
	public boolean findFreeRandomAreaPos(
		final int aW, final int aH,
		final int cX, final int cY,
		final int cW, final int cH,
		final int offsetX, final int offsetY,
		final int stepX, final int stepY,
		final boolean avoidExitBlockArea,
		final int[] foundPos
	) {		
		int
			xRandMax = (cW / stepX) - aW - 1,
			yRandMax = (cH / stepY) - aH - 1
		;
		
		if (xRandMax <= 0 || yRandMax <= 0) return false;
		
		int randX, randY, i = 0;
		
		while (i < MAX_FREE_AREA_POS_SEARCH_ATTEMPTS) {
			randX = cX + offsetX + (Util.getRandomInt(0, xRandMax) * stepX);
			randY = cY + offsetY + (Util.getRandomInt(0, yRandMax) * stepY);
			
			if (
				!avoidExitBlockArea ||
				!areaIntersectsDoorArea(randX, randY, aW, aH)
			) {
				if (getFirstOccupierInArea(randX, randY, aW, aH) == null) {
					foundPos[0] = randX;
					foundPos[1] = randY;
					return true;
				}
			}
			i++;
		}
		return false;
		
		/*
		throw new RuntimeException(
			"Couldn't find a free field area for block spawning after " +
			MAX_FREE_AREA_POS_SEARCH_ATTEMPTS + " attempt(s)."
		);
		*/
	}
	
	/**
	 * @param aX
	 * @param aY
	 * @param aW 
	 * @param aH 
	 * 
	 * @return
	 */
	public boolean areaIntersectsExitBlockArea(
		final int aX, final int aY, final int aW, final int aH
	) {
		return Util.rectIntersectsRect(
			aX, aY, aW, aH,
			exitBlockX, exitBlockY, EXIT_BLOCK_WIDTH, EXIT_BLOCK_WIDTH
		);		
	}
	
	/**
	 * @return the doorPolicy
	 */
	public int getDoorPolicy() {
		return doorPolicy;
	}
	
	/**
	 * 
	 */
	public void dispatchDoorsOpenEvent() {
		if (fieldEventListener != null)
			fieldEventListener.onFieldDoorsOpen(this);
	}
	
	/**
	 * 
	 */
	public void dispatchDoorsCloseEvent() {
		if (fieldEventListener != null)
			fieldEventListener.onFieldDoorsClose(this);
	}
	
	/**
	 * @return the pickups
	 */
	public Vector getPickups() {
		return pickups;
	}
	
	/**
	 * @return
	 */
	public boolean hasPickups() {
		return !pickups.isEmpty();
	}
	
	/**
	 * @return
	 */
	public int getNoOfPickups() {
		return pickups.size();
	}
	
	/**
	 * Updates this field.
	 */
	public void update() {
		Pickup pickup;
		int i = 0, noOfPickups = pickups.size();
		while (i < noOfPickups) {
			pickup = (Pickup)pickups.elementAt(i);
			if (pickup.hasExpired()) {
				pickups.removeElementAt(i);
				pickup.dispose();
				noOfPickups--;
			} else i++;
		}
	}
	
	/**
	 * Says whether this field currently has an exit block spawned.
	 * @return Returns true if the exit block has been spawned, false otherwise.
	 */
	public boolean hasExitBlock() {
		return exitBlock != null;
	}
	
	/**
	 * @return
	 */
	public int getNoOfExitBlockOccupiers() {
		return noOfExitBlockAreaOccupiers;
	}
	
	/**
	 * @return
	 */
	public int getNoOfFieldObjects() {
		return noOfFieldObjects;
	}
	
	public void spawnExitBlock() {
		if (exitBlock != null) throw new IllegalStateException(
			"Exit block already exists."
		);
		
		exitBlock = new ExitBlock(this, exitBlockX, exitBlockY);
		exitBlock.activate();
	}
	
	public boolean hasKey() {
		return key != null;
	}
	
	/**
	 * @return the key
	 */
	public Pickup getKey() {
		return key;
	}
	
	/**
	 * @return the exitBlock
	 */
	public ExitBlock getExitBlock() {
		return exitBlock;
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public void spawnKey(final int x, final int y) {
		if (key != null) throw new IllegalStateException(
			"A key already exists on the field."
		);
		key = new Pickup(this, LoachItem.KEY_ITEM, x, y, 0);
		key.activate();
	}
}
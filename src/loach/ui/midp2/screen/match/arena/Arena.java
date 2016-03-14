/**
 * 
 */
package loach.ui.midp2.screen.match.arena;

import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

import loach.model.field.Field;
import loach.model.field.FieldEventListener;
import loach.model.field.object.Block;
import loach.model.field.object.ExitBlock;
import loach.model.field.object.FieldObject;
import loach.model.field.object.Pickup;
import loach.model.field.object.WormSegment;
import loach.model.info.item.LoachItem;
import loach.model.match.worm.Worm;
import loach.model.match.worm.WormEventListener;
import loach.ui.midp2.MIDP2LoachUI;
import loach.ui.midp2.screen.match.MatchScreen;
import loach.ui.midp2.screen.match.arena.object.ArenaObject;
import loach.ui.midp2.screen.match.arena.object.BlockArenaObj;
import loach.ui.midp2.screen.match.arena.object.ExitBlockArenaObj;
import loach.ui.midp2.screen.match.arena.object.PickupArenaObj;
import loach.ui.midp2.screen.match.arena.object.WormSegmentArenaObj;
import util.Util;

/**
 * LoachM - Arena
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class Arena
implements FieldEventListener, WormEventListener
{	
	public static final int POINT_WIDTH = 4;
	public static final int POINT_HEIGHT = 4;
	
	public static final int	H_WALL_HEIGHT = 32;
	public static final int	V_WALL_WIDTH = 32;
		
	public static final int	WALL_CORNER_WIDTH = V_WALL_WIDTH;
	public static final int	WALL_CORNER_HEIGHT = H_WALL_HEIGHT;
		
	public static final int	PLAY_AREA_WIDTH = Field.WIDTH * POINT_WIDTH;
	public static final int	PLAY_AREA_HEIGHT = Field.HEIGHT * POINT_HEIGHT;
		
	public static final int
		AREA_WIDTH = PLAY_AREA_WIDTH + (WALL_CORNER_WIDTH * 2)
	;
	public static final int
		AREA_HEIGHT = PLAY_AREA_HEIGHT + (WALL_CORNER_HEIGHT * 2)
	;
		
	public static final int	PLAY_AREA_X = WALL_CORNER_WIDTH - 1;
	public static final int	PLAY_AREA_Y = WALL_CORNER_HEIGHT - 1;
		
	public static final int
		FLOOR_TILE_WIDTH = Field.FLOOR_TILE_WIDTH * POINT_WIDTH
	;
	
	public static final int
		FLOOR_TILE_HEIGHT = Field.FLOOR_TILE_HEIGHT * POINT_HEIGHT
	;
		
	public static final int H_WALL_WIDTH = Field.H_WALL_WIDTH * POINT_WIDTH;
	public static final int	V_WALL_HEIGHT = Field.V_WALL_HEIGHT * POINT_HEIGHT;
		
	public static final int	H_DOOR_WIDTH = Field.H_DOOR_WIDTH * POINT_WIDTH;
	public static final int	V_DOOR_HEIGHT = Field.V_DOOR_HEIGHT * POINT_HEIGHT;
		
	public static final int
		H_DOOR_X = WALL_CORNER_WIDTH + (Field.H_DOOR_X * POINT_WIDTH)
	;
	public static final int
		V_DOOR_Y = WALL_CORNER_HEIGHT + (Field.V_DOOR_Y * POINT_HEIGHT)
	;
		
	public static final int	NW_WALL_CORNER_X = 0;
	public static final int	NW_WALL_CORNER_Y = 0;
		
	public static final int
		NE_WALL_CORNER_X = AREA_WIDTH - 1 - WALL_CORNER_WIDTH
	;
	public static final int	NE_WALL_CORNER_Y = 0;
		
	public static final int
		SE_WALL_CORNER_X = 0 + AREA_WIDTH - 1 - WALL_CORNER_WIDTH
	;
	public static final int
		SE_WALL_CORNER_Y = 0 + AREA_HEIGHT - 1 - WALL_CORNER_HEIGHT
	;	
		
	public static final int	SW_WALL_CORNER_X = 0;
	public static final int
		SW_WALL_CORNER_Y = 0 + AREA_HEIGHT - 1 - WALL_CORNER_HEIGHT
	;			
	
	public static final int	NORTH_DOOR_X = H_DOOR_X;
	public static final int	NORTH_DOOR_Y = 0;
		
	public static final int	EAST_DOOR_X = 0 + AREA_WIDTH - 1;
	public static final int	EAST_DOOR_Y = V_DOOR_Y;
		
	public static final int	SOUTH_DOOR_X = H_DOOR_X;
	public static final int	SOUTH_DOOR_Y = AREA_HEIGHT - 1;
		
	public static final int WEST_DOOR_X = 0;
	public static final int WEST_DOOR_Y = V_DOOR_Y;
		
	public static final int NNW_WALL_X = 0 + WALL_CORNER_WIDTH;
	public static final int NNW_WALL_Y = 0;
		
	public static final int	NNE_WALL_X = NORTH_DOOR_X + 64;
	public static final int	NNE_WALL_Y = 0;
		
	public static final int ENE_WALL_X = AREA_WIDTH - 1 - WALL_CORNER_WIDTH;
	public static final int	ENE_WALL_Y = WALL_CORNER_HEIGHT;
		
	public static final int
		ESE_WALL_X = AREA_WIDTH - 1 - WALL_CORNER_WIDTH
	;
	public static final int	ESE_WALL_Y = EAST_DOOR_Y + 64;
		
	public static final int	WNW_WALL_X = 0;
	public static final int	WNW_WALL_Y = WALL_CORNER_HEIGHT;
		
	public static final int	WSW_WALL_X = 0;
	public static final int	WSW_WALL_Y = WEST_DOOR_Y + 64;
		
	public static final int SSW_WALL_X = WALL_CORNER_WIDTH;
	public static final int
		SSW_WALL_Y = AREA_HEIGHT - 1 - WALL_CORNER_HEIGHT
	;
		
	public static final int	SSE_WALL_X = SOUTH_DOOR_X + 64;
	public static final int
		SSE_WALL_Y = AREA_HEIGHT - 1 - WALL_CORNER_HEIGHT
	;
	
	private final int doorPolicy;
	private boolean doorsOpen;
	
	private static final int
		DOOR_SPRITE_FRAME_CLOSED = 0,
		DOOR_SPRITE_FRAME_OPEN = 1,
		
		DOOR_SPRITE_WIDTH = 32,
		DOOR_SPRITE_HEIGHT = 32,
		
		FLOOR_TILE_1_SPRITE_INDEX = 1,
		FLOOR_TILE_2_SPRITE_INDEX = 2
	;
	
	private final MatchScreen matchScreen;
	private final Hashtable fieldObjectToArenaObjectMap = new Hashtable(20);
	
	private final Field field;
	private final TiledLayer arenaFloor;
	
	private final LayerManager
		arenaFloorLayer,
		arenaWallLayer,
		arenaObjectLayer,
		lesserArenaHUDLayer,
		arenaHUDLayer
	;
	
	private final Sprite
		northDoorSprite1,
		northDoorSprite2,
		eastDoorSprite1,
		eastDoorSprite2,
		southDoorSprite1,
		southDoorSprite2,
		westDoorSprite1,
		westDoorSprite2
	;
	
	private final Sprite[] doorSprites;
	
	private int viewportX, viewportY;
	
	private final MIDP2LoachUI midp2LoachUI;
	
	/**
	 * @param matchScreen
	 * @param field 
	 */
	public Arena(final MatchScreen matchScreen, final Field field) {
		if (matchScreen == null || field == null)
			throw Util.NULL_ARGUMENT_EXCEPTION;
		
		this.matchScreen = matchScreen;
		this.field = field;
		this.doorPolicy = field.getDoorPolicy();
		
		midp2LoachUI = matchScreen.getMIDP2LoachUI();
		
		arenaFloorLayer = matchScreen.getArenaFloorLayer();
		arenaObjectLayer = matchScreen.getArenaObjectLayer();
		arenaWallLayer = matchScreen.getArenaWallLayer();
		lesserArenaHUDLayer = matchScreen.getLesserArenaHUDLayer();
		arenaHUDLayer = matchScreen.getHUDLayer();
		
		field.setFieldEventListener(this);
		
		final LayerManager arenaFloorLayer = matchScreen.getArenaFloorLayer();
		arenaFloor = new TiledLayer(
			Field.TILE_COLS, Field.TILE_ROWS,
			MIDP2LoachUI.ARENA_FLOOR_TILE_SET_IMAGE,
			FLOOR_TILE_WIDTH, FLOOR_TILE_HEIGHT
		);
		arenaFloor.setPosition(PLAY_AREA_X, PLAY_AREA_Y);
		
		int currRowStartTImgIndex = FLOOR_TILE_1_SPRITE_INDEX, currTImgIndex;
		
		int j;
		for (int i = 0; i < Field.TILE_ROWS; i++) {
			currTImgIndex = currRowStartTImgIndex;
			for (j = 0; j < Field.TILE_COLS; j++) {
				arenaFloorLayer.append(arenaFloor);
				arenaFloor.setCell(j, i, currTImgIndex);
				
				currTImgIndex = 
					currTImgIndex == FLOOR_TILE_1_SPRITE_INDEX ?
					FLOOR_TILE_2_SPRITE_INDEX : FLOOR_TILE_1_SPRITE_INDEX
				;
			}
			
			currRowStartTImgIndex = 
				currRowStartTImgIndex == FLOOR_TILE_1_SPRITE_INDEX ?
				FLOOR_TILE_2_SPRITE_INDEX : FLOOR_TILE_1_SPRITE_INDEX
			;
		}
		
		northDoorSprite1 = new Sprite(
			MIDP2LoachUI.ARENA_DOOR_SET_IMAGE,
			DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT
		);		
		northDoorSprite2 = new Sprite(
			MIDP2LoachUI.ARENA_DOOR_SET_IMAGE,
			DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT
		);
		
		northDoorSprite1.setPosition(NORTH_DOOR_X, NORTH_DOOR_Y);
		northDoorSprite2.setPosition(NORTH_DOOR_X + 64 - 1, NORTH_DOOR_Y);
		northDoorSprite2.setTransform(Sprite.TRANS_MIRROR);
		
		eastDoorSprite1 = new Sprite(
			MIDP2LoachUI.ARENA_DOOR_SET_IMAGE,
			DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT
		);		
		eastDoorSprite2 = new Sprite(
			MIDP2LoachUI.ARENA_DOOR_SET_IMAGE,
			DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT
		);
		
		eastDoorSprite1.setPosition(EAST_DOOR_X - 1, EAST_DOOR_Y);
		eastDoorSprite1.setTransform(Sprite.TRANS_ROT90);
		eastDoorSprite2.setPosition(EAST_DOOR_X - 1, EAST_DOOR_Y + 64 - 1);
		eastDoorSprite2.setTransform(Sprite.TRANS_MIRROR_ROT90);
		
		southDoorSprite1 = new Sprite(
			MIDP2LoachUI.ARENA_DOOR_SET_IMAGE,
			DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT
		);		
		southDoorSprite2 = new Sprite(
			MIDP2LoachUI.ARENA_DOOR_SET_IMAGE,
			DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT
		);
		
		southDoorSprite1.setPosition(SOUTH_DOOR_X + 64 - 1, SOUTH_DOOR_Y - 1);
		southDoorSprite1.setTransform(Sprite.TRANS_ROT180);
		southDoorSprite2.setPosition(SOUTH_DOOR_X, SOUTH_DOOR_Y - 1);
		southDoorSprite2.setTransform(Sprite.TRANS_MIRROR_ROT180);
		
		westDoorSprite1 = new Sprite(
			MIDP2LoachUI.ARENA_DOOR_SET_IMAGE,
			DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT
		);		
		westDoorSprite2 = new Sprite(
			MIDP2LoachUI.ARENA_DOOR_SET_IMAGE,
			DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT
		);
		
		westDoorSprite1.setPosition(WEST_DOOR_X, WEST_DOOR_Y + 64 - 1);
		westDoorSprite1.setTransform(Sprite.TRANS_ROT270);
		westDoorSprite2.setPosition(WEST_DOOR_X, WEST_DOOR_Y);
		westDoorSprite2.setTransform(Sprite.TRANS_MIRROR_ROT270);		
		
		doorSprites = new Sprite[] {
			northDoorSprite1, northDoorSprite2,
			eastDoorSprite1, eastDoorSprite2,
			southDoorSprite1, southDoorSprite2,
			westDoorSprite1, westDoorSprite2
		};
		
		for (int i = 0; i < doorSprites.length; i++)
			arenaWallLayer.append(doorSprites[i]);
		
		Sprite s1;
		
		s1 = new Sprite(MIDP2LoachUI.ARENA_WALL_SET_1_IMAGE);
		s1.setPosition(NNW_WALL_X, NNW_WALL_Y);	
		arenaWallLayer.append(s1);
		
		s1 = new Sprite(MIDP2LoachUI.ARENA_WALL_SET_1_IMAGE);
		s1.setTransform(Sprite.TRANS_MIRROR);
		s1.setPosition(NNE_WALL_X, NNE_WALL_Y);	
		arenaWallLayer.append(s1);
		
		s1 = new Sprite(MIDP2LoachUI.ARENA_WALL_SET_1_IMAGE);
		s1.setTransform(Sprite.TRANS_ROT270);
		s1.setPosition(WSW_WALL_X, WSW_WALL_Y);	
		arenaWallLayer.append(s1);
		
		s1 = new Sprite(MIDP2LoachUI.ARENA_WALL_SET_1_IMAGE);
		s1.setTransform(Sprite.TRANS_MIRROR_ROT270);
		s1.setPosition(WNW_WALL_X, WNW_WALL_Y);	
		arenaWallLayer.append(s1);
		
		s1 = new Sprite(MIDP2LoachUI.ARENA_WALL_SET_1_IMAGE);
		s1.setTransform(Sprite.TRANS_ROT90);
		s1.setPosition(ENE_WALL_X, ENE_WALL_Y);	
		arenaWallLayer.append(s1);
		
		s1 = new Sprite(MIDP2LoachUI.ARENA_WALL_SET_1_IMAGE);
		s1.setTransform(Sprite.TRANS_MIRROR_ROT90);
		s1.setPosition(ESE_WALL_X, ESE_WALL_Y);	
		arenaWallLayer.append(s1);
		
		s1 = new Sprite(MIDP2LoachUI.ARENA_WALL_SET_1_IMAGE);
		s1.setTransform(Sprite.TRANS_ROT180);
		s1.setPosition(SSE_WALL_X, SSE_WALL_Y);
		arenaWallLayer.append(s1);
		
		s1 = new Sprite(MIDP2LoachUI.ARENA_WALL_SET_1_IMAGE);
		s1.setTransform(Sprite.TRANS_MIRROR_ROT180);
		s1.setPosition(SSW_WALL_X, SSW_WALL_Y);
		arenaWallLayer.append(s1);
		
		s1 = new Sprite(MIDP2LoachUI.ARENA_WALL_CORNER_IMAGE);
		s1.setPosition(NW_WALL_CORNER_X, NW_WALL_CORNER_Y);
		arenaWallLayer.append(s1);
		
		s1 = new Sprite(MIDP2LoachUI.ARENA_WALL_CORNER_IMAGE);
		s1.setTransform(Sprite.TRANS_MIRROR);
		s1.setPosition(NE_WALL_CORNER_X, NE_WALL_CORNER_Y);
		arenaWallLayer.append(s1);
		
		s1 = new Sprite(MIDP2LoachUI.ARENA_WALL_CORNER_IMAGE);
		s1.setTransform(Sprite.TRANS_MIRROR_ROT90);
		s1.setPosition(SE_WALL_CORNER_X, SE_WALL_CORNER_Y);
		arenaWallLayer.append(s1);
		
		s1 = new Sprite(MIDP2LoachUI.ARENA_WALL_CORNER_IMAGE);
		s1.setTransform(Sprite.TRANS_ROT270);
		s1.setPosition(SW_WALL_CORNER_X, SW_WALL_CORNER_Y);
		arenaWallLayer.append(s1);
		
		if (field.doorsOpen()) openDoors(); else closeDoors();
		
		final Worm humanPlayerWorm = matchScreen.getHumanPlayerWorm();
		
		lookAt(
			PLAY_AREA_X + (humanPlayerWorm.getHeadX() * Arena.POINT_WIDTH),
			PLAY_AREA_Y + (humanPlayerWorm.getHeadY() * Arena.POINT_HEIGHT)
		);
	}
	
	/**
	 * @param ao
	 */
	public void addArenaObject(final ArenaObject ao) {
		fieldObjectToArenaObjectMap.put(ao.getFieldObject(), ao);
	}
	
	/**
	 * @param ao
	 */
	public void removeArenaObject(final ArenaObject ao) {		
		fieldObjectToArenaObjectMap.remove(ao.getFieldObject());
	}
	
	/**
	 * 
	 */
	public void update() {
	}
	
	/**
	 * @return the matchScreen
	 */
	public MatchScreen getMatchScreen() {
		return matchScreen;
	}
	
	/**
	 * @param fo
	 * @return
	 */
	public ArenaObject obtainArenaObject(final FieldObject fo) {
		final ArenaObject ao = (ArenaObject)fieldObjectToArenaObjectMap.get(fo);
		return ao;
	}

	/* (non-Javadoc)
	 * @see loach.model.match.field.FieldEventListener#onFieldObjectCreation(loach.model.match.field.object.FieldObject)
	 */
	public void onFieldObjectCreation(final FieldObject fo) {
		createArenaObjectForFieldObject(fo);
	}

	/* (non-Javadoc)
	 * @see loach.model.match.field.FieldEventListener#onFieldObjectDisposal(loach.model.match.field.object.FieldObject)
	 */
	public void onFieldObjectDisposal(final FieldObject fo) {
		obtainArenaObject(fo).dispose();
	}
	
	/**
	 * @return the field
	 */
	public Field getField() {
		return field;
	}
	
	/**
	 * @return the arenaFloor
	 */
	public TiledLayer getArenaFloor() {
		return arenaFloor;
	}

	/* (non-Javadoc)
	 * @see loach.ui.midp2.util.Paintable#paint(javax.microedition.lcdui.Graphics)
	 */
	public void paint(final Graphics g) {		
		final int
			paintX = -viewportX,
			paintY = -viewportY
		;
		
		arenaFloorLayer.paint(g, paintX, paintY);
		arenaWallLayer.paint(g, paintX, paintY);
		arenaObjectLayer.paint(g, paintX, paintY);
		lesserArenaHUDLayer.paint(g, paintX, paintY);
		arenaHUDLayer.paint(g, paintX, paintY);
	}
	
	/**
	 * @return the viewportX
	 */
	public int getViewportX() {
		return viewportX;
	}
	
	/**
	 * @return the viewportY
	 */
	public int getViewportY() {
		return viewportY;
	}
	
	///**
	// * Obtains the ideal arena size for the given display size.
	// * 
	// * @param displaySize
	// * @return
	// */
	/*
	public static final Dimension findIdealArenaSize(
		final Dimension displaySize
	) {
		final int
			displayWidth = displaySize.getWidth(),
			displayHeight = displaySize.getHeight(),
			displayWidthPoints = displayWidth / POINT_WIDTH,
			displayHeightPoints = displayHeight / POINT_HEIGHT
		;
		
		int i = 0, t, bestWidth, bestHeight;
		
		while (true) {
			t = i + FLOOR_TILE_WIDTH;
			if (t > displayWidth) break;
			i = t;
		}
		
		bestWidth = i;
		
		i = 0;
		
		while (true) {
			t = i + FLOOR_TILE_HEIGHT;
			if (t > displayHeight) break;
			i = t;
		}
		
		bestHeight = i;
		
		return new Dimension(bestWidth, bestHeight);
	}
	*/
	
	/**
	 * @param fieldObject
	 * @return
	 */
	public ArenaObject createArenaObjectForFieldObject(
		final FieldObject fieldObject
	) {
		if (fieldObject == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		switch (fieldObject.getFoID()) {
			case FieldObject.FO_ID_WORM_SEGMENT:
				return new WormSegmentArenaObj(this, (WormSegment)fieldObject);
			case FieldObject.FO_ID_PICKUP:
				return new PickupArenaObj(this, (Pickup)fieldObject);
			case FieldObject.FO_ID_BLOCK:
				return new BlockArenaObj(this, (Block)fieldObject);
			case FieldObject.FO_ID_EXIT_BLOCK:
				return new ExitBlockArenaObj(this, (ExitBlock)fieldObject);
		}

		return null;
	}
	
	private void lookAt(final int cX, final int cY) {
		final int
			viewportWidth = matchScreen.getWidth(),
			viewportHeight = matchScreen.getHeight()
		;
			
		viewportX = cX - (viewportWidth / 2);
		viewportY = cY - (viewportHeight / 2);
		
		if (viewportX < 0) viewportX = 0;
		if (viewportY < 0) viewportY = 0;
		
		if ((viewportX + viewportWidth) >= AREA_WIDTH)
			viewportX = AREA_WIDTH - viewportWidth;
		
		if ((viewportY + viewportHeight) >= AREA_HEIGHT)
			viewportY = AREA_HEIGHT - viewportHeight;
	}
	
	/* (non-Javadoc)
	 * @see loach.model.match.worm.WormEventListener#onWormSegmentAddition(loach.model.match.field.object.WormSegment)
	 */
	public void onWormSegmentAddition(final WormSegment wormSegment) {
		final Worm worm = wormSegment.getWorm();
		
		if (worm != matchScreen.getHumanPlayerWorm()) return;			
		lookAt(
			PLAY_AREA_X + (wormSegment.getX() * Arena.POINT_WIDTH),
			PLAY_AREA_Y + (wormSegment.getY() * Arena.POINT_HEIGHT)
		);
	}

	/* (non-Javadoc)
	 * @see loach.model.match.worm.WormEventListener#onWormSegmentSubtraction(loach.model.match.field.object.WormSegment)
	 */
	public void onWormSegmentSubtraction(final WormSegment wormSegment) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see loach.model.match.worm.WormEventListener#onWormDeath(loach.model.match.worm.Worm)
	 */
	public void onWormDeath(final Worm worm) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * 
	 */
	private void closeDoors() {		
		for (int i = 0; i < doorSprites.length; i++)
			doorSprites[i].setFrame(DOOR_SPRITE_FRAME_CLOSED);
		
		doorsOpen = false;
	}
	
	/**
	 * 
	 */
	private void openDoors() {		
		for (int i = 0; i < doorSprites.length; i++)
			doorSprites[i].setFrame(DOOR_SPRITE_FRAME_OPEN);
		
		doorsOpen = true;
	}

	/* (non-Javadoc)
	 * @see loach.model.match.field.FieldEventListener#onFieldDoorsOpen(loach.model.match.field.Field)
	 */
	public void onFieldDoorsOpen(final Field field) {
		openDoors();
	}

	/* (non-Javadoc)
	 * @see loach.model.match.field.FieldEventListener#onFieldDoorsClose(loach.model.match.field.Field)
	 */
	public void onFieldDoorsClose(final Field field) {
		closeDoors();
	}
	
	public boolean viewportIsFilledByArena() {
		return Util.rectContainsRect(
			viewportX, viewportY,
			matchScreen.getWidth(), matchScreen.getHeight(),
			0, 0,
			AREA_WIDTH, AREA_HEIGHT
		);
	}

	/* (non-Javadoc)
	 * @see loach.model.match.worm.WormEventListener#onWormFieldObjectCollision(loach.model.match.worm.Worm, loach.model.field.object.FieldObject)
	 */
	public void onWormFieldObjectCollision(
		final Worm worm, final FieldObject fieldObject
	) {
		
	}

	/* (non-Javadoc)
	 * @see loach.model.match.worm.WormEventListener#onWormFieldWallCollision(loach.model.match.worm.Worm)
	 */
	public void onWormFieldWallCollision(final Worm worm) {
		
	}

	/* (non-Javadoc)
	 * @see loach.model.match.worm.WormEventListener#onWormItemUse(loach.model.match.worm.Worm, loach.model.info.item.LoachItem)
	 */
	public void onWormItemUse(final Worm worm, final LoachItem item) {
		
	}
}
/**
 * 
 */
package loach.ui.midp2.screen.match;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

import loach.model.field.Field;
import loach.model.field.object.FieldObject;
import loach.model.field.object.WormSegment;
import loach.model.info.item.LoachItem;
import loach.model.match.Match;
import loach.model.match.MatchEventListener;
import loach.model.match.worm.Worm;
import loach.model.match.worm.WormEventListener;
import loach.model.match.worm.controller.SimpleWormController;
import loach.model.match.worm.controller.WormController;
import loach.model.player.Player;
import loach.ui.midp2.MIDP2LoachUI;
import loach.ui.midp2.screen.match.arena.Arena;
import loach.ui.midp2.screen.match.hud.HUD;
import loach.ui.midp2.screen.score.ScoreScreen;
import loach.ui.midp2.screen.title.TitleScreen;
import loach.ui.midp2.screen.title.settings.PlayerSettings;
import loach.ui.midp2.util.MIDP2Utils;
import util.Util;

/**
 * LoachM - GameScreen
 * 
 * This represents the match screen, where the human-controlled worm and
 * CPU worms duke it out on the battlefield.
 * 
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class MatchScreen extends GameCanvas
implements
	CommandListener, MatchEventListener, WormEventListener,
	Runnable
{	
	private static final int
		COUNTDOWN_SEGMENTS = 4,
		START_PANEL_SPACING = 5;
	;
	
	public static final long TIME_UNTIL_MATCH_START_MS = 6000;
	
	private static final int
		START_PANEL_WIDTH = 51,
		START_PANEL_HEIGHT = 20
	;
	
	private static final long
		COUNTDOWN_SEGMENT_LENGTH_MS =
			TIME_UNTIL_MATCH_START_MS /
			COUNTDOWN_SEGMENTS,
		COUNTDOWN_4_END_MS = COUNTDOWN_SEGMENT_LENGTH_MS,
		COUNTDOWN_3_END_MS = COUNTDOWN_SEGMENT_LENGTH_MS * 2,
		COUNTDOWN_2_END_MS = COUNTDOWN_SEGMENT_LENGTH_MS * 3,
		COUNTDOWN_1_END_MS = COUNTDOWN_SEGMENT_LENGTH_MS * 4,
		
		START_PANEL_FOLD_UP_START_MS = 5000, 
		START_PANEL_FOLD_UP_LENGTH_MS = 1000,
		
		START_PANEL_FOLD_UP_SLICE_MS =
			START_PANEL_FOLD_UP_LENGTH_MS / START_PANEL_HEIGHT
		;
	;
	
	private static final String
		COUNTDOWN_4_TEXT = "4",
		COUNTDOWN_3_TEXT = "3",
		COUNTDOWN_2_TEXT = "2",
		COUNTDOWN_1_TEXT = "1"
	;
	
	private final long timeInitMS;

	public static final int[] BG_COLOUR = {12, 52, 118};
	
	private final LayerManager
		arenaFloorLayer = new LayerManager(),
		arenaWallLayer = new LayerManager(),
		arenaObjectLayer = new LayerManager(),
		lesserArenaHUDLayer = new LayerManager(),
		arenaHUDLayer = new LayerManager(),
		hudLayer = new LayerManager()
	;
	
	private final LayerManager[] cameraLayers = {
		arenaFloorLayer, arenaWallLayer, arenaObjectLayer, lesserArenaHUDLayer,
		arenaHUDLayer, arenaHUDLayer, hudLayer
	};
	
	private final int frameSkip, threadSleep;
	
	private final MIDP2LoachUI midp2LoachUI;
	private final Display display;
	private final Arena arena;
	private final Match match;
	private final Player humanPlayer;
	private final Worm humanPlayerWorm;
	private final WormController[] cpuWormControllers;
	private final HUD hud;
	private final Field field;
	
	private String message;
	
	private int repaintTicks = 0, frames = 0, currentFPS = 0, countdown = -1;
	private long lastSecondMS = 0, updateStartTimeMS = -1;
	
	private Image[] startPanelImages = null;
	
	private Sprite[] startIndicatorSprites = null;
	
	private boolean startStuffCreated = false;
	
	private static final int[] START_PANEL_POSITIONS = new int[] {
		Util.POSITION_LEFT,
		Util.POSITION_RIGHT,
		Util.POSITION_LEFT,
		Util.POSITION_RIGHT,
		Util.POSITION_LEFT,
		Util.POSITION_RIGHT
	};
	
	private int[][] startPanelPositions;
	
	/**
	 * @return the midp2LoachUI
	 */
	public MIDP2LoachUI getMIDP2LoachUI() {
		return midp2LoachUI;
	}
	
	/**
	 * @param gameAction
	 * @return
	 */
	private static int getDirectionForGameAction(final int gameAction) {
		switch (gameAction) {
			case Canvas.UP: return Util.DIRECTION_NORTH;
			case Canvas.DOWN: return Util.DIRECTION_SOUTH;
			case Canvas.LEFT: return Util.DIRECTION_WEST;
			case Canvas.RIGHT: return Util.DIRECTION_EAST;
		}
		return -1;
	}
	
	/**
	 * @param midp2LoachUI The view kickstart.
	 * @param match The Match to which this MatchScreen is linked.
	 * @param humanPlayer
	 * @param frameSkip 
	 * @param threadSleep 
	 */
	public MatchScreen(
		final MIDP2LoachUI midp2LoachUI,
		final Match match,
		final Player humanPlayer,
		final int frameSkip,
		final int threadSleep
	) {
		super(false);
		
		if (midp2LoachUI == null || match == null || humanPlayer == null)
			throw Util.NULL_ARGUMENT_EXCEPTION;
		
		this.midp2LoachUI = midp2LoachUI;
		this.humanPlayer = humanPlayer;
		this.match = match;
		this.frameSkip = frameSkip;
		this.threadSleep = threadSleep;
		
		timeInitMS = System.currentTimeMillis();
		
		/*
		try {
			InputStream is = getClass().getResourceAsStream(
				"s5.wav"
			);
			Player player = Manager.createPlayer(
				is, "audio/x-wav"
			);
			player.start();
			player.close();
			player.prefetch();
		} catch (IOException e) {
			System.out.println("what " + e.toString());
		} catch (MediaException e) {
			System.out.println("noes " + e.toString());
		}
		*/
		
		display = midp2LoachUI.getDisplay();
		
		/* set up human player and worm associations -- we need control */
		humanPlayerWorm = match.obtainWormForPlayer(humanPlayer);
		
		arena = new Arena(this, match.getField());
		field = match.getField();
		
		final Worm[] worms = match.getWorms();
		
		cpuWormControllers = new WormController[worms.length - 1];
		
		int i;
		
		Worm worm;
		int j = 0;
		for (i = 0; i < worms.length; i++) {
			worm = worms[i];
			if (worm != humanPlayerWorm)
				cpuWormControllers[j++] = new SimpleWormController(worm);
		}
		
		humanPlayerWorm.addWormEventListener(this);
		hud = new HUD(this);
		
		setCommandListener(this);
		setFullScreenMode(true);
		
		addCommand(MIDP2Utils.SCREEN_BACK_COMMAND);
		addCommand(MIDP2Utils.SCREEN_NEXT_COMMAND);
		
		match.activate();
		
		System.gc();
	}
	
	/**
	 * @return the timeInitMS
	 */
	public long getTimeInitMS() {
		return timeInitMS;
	}
	
	protected void keyPressed(final int key) {
		final int gameAction = getGameAction(key);
		if (gameAction != 0) onGameActionPress(gameAction);
	}
	
	protected void keyReleased(final int key) {}
	protected void keyRepeated(final int key) {}
	
	protected void sizeChanged(final int width, final int height) {
	}
	
	/**
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(final Command c, final Displayable d) {
		if (d == this)
			switch (c.getCommandType()) {
				case Command.SCREEN: next(); break;
				case Command.BACK: back();
			}
	}
	
	private void back() {
		match.dispose();
		
		final TitleScreen ts = new TitleScreen(midp2LoachUI);
		display.setCurrent(ts);
	}

	/**
	 * @return the arena
	 */
	public Arena getArena() {
		return arena;
	}
	
	/**
	 * @return the arenaFloorLayer
	 */
	public LayerManager getArenaFloorLayer() {
		return arenaFloorLayer;
	}
	
	/**
	 * @return the arenaObjectLayer
	 */
	public LayerManager getArenaObjectLayer() {
		return arenaObjectLayer;
	}
	
	/**
	 * @return the arenaWallLayer
	 */
	public LayerManager getArenaWallLayer() {
		return arenaWallLayer;
	}
	
	/**
	 * @return the HUD
	 */
	public HUD getHUD() {
		return hud;
	}
	
	/**
	 * @return the hudLayer
	 */
	public LayerManager getHUDLayer() {
		return hudLayer;
	}
	
	/**
	 * @return the match
	 */
	public Match getMatch() {
		return match;
	}

	/**
	 * @param gameAction
	 */
	private void onGameActionPress(final int gameAction) {
		final int direction = getDirectionForGameAction(gameAction);
		if (direction != -1) onHumanWormDirection(direction);
	}
	
	/**
	 * @param direction
	 */
	private void onHumanWormDirection(final int direction) {
		if (
			humanPlayerWorm.isAlive() &&
			humanPlayerWorm.isValidDirection(direction)
		) humanPlayerWorm.setDirection(direction);
	}

	/**
	 * 
	 */
	private void onUseKeyPress() {
	}

	/* (non-Javadoc)
	 * @see loach.model.match.MatchEventListener#onWormDeath(loach.model.match.worm.Worm)
	 */
	public void onWormDeath(final Worm worm) {
		arena.onWormDeath(worm);		
	}

	/* (non-Javadoc)
	 * @see loach.model.match.worm.WormEventListener#onWormFieldObjectCollision(loach.model.match.worm.Worm, loach.model.field.object.FieldObject)
	 */
	public void onWormFieldObjectCollision(
		final Worm worm, final FieldObject fieldObject
	) {
		arena.onWormFieldObjectCollision(worm, fieldObject);
	}

	/* (non-Javadoc)
	 * @see loach.model.match.worm.WormEventListener#onWormFieldWallCollision(loach.model.match.worm.Worm)
	 */
	public void onWormFieldWallCollision(final Worm worm) {
		arena.onWormFieldWallCollision(worm);
	}
	
	/* (non-Javadoc)
	 * @see loach.model.match.MatchEventListener#onWormItemUse(loach.model.match.worm.Worm, loach.model.item.LoachItem)
	 */
	public void onWormItemUse(final Worm worm, final LoachItem item) {
		arena.onWormItemUse(worm, item);
		
	}
	
	/* (non-Javadoc)
	 * @see loach.model.match.MatchEventListener#onWormSegmentAddition(loach.model.match.worm.Worm, loach.model.match.field.object.WormSegment)
	 */
	public void onWormSegmentAddition(final WormSegment wormSegment) {
		arena.onWormSegmentAddition(wormSegment);
	}
	
	/* (non-Javadoc)
	 * @see loach.model.match.MatchEventListener#onWormSegmentSubtraction(loach.model.match.worm.Worm, loach.model.match.field.object.WormSegment)
	 */
	public void onWormSegmentSubtraction(final WormSegment wormSegment) {
		arena.onWormSegmentSubtraction(wormSegment);	
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.game.GameCanvas#paint(javax.microedition.lcdui.Graphics)
	 */
	public void paint(final Graphics g) {
		final int width = getWidth(), height = getHeight();
		
		g.setClip(0, 0, width, height);
		
		if (!arena.viewportIsFilledByArena()) {
			g.setColor(0x000000);
			g.fillRect(0, 0, width, height);
		}
		
		arena.paint(g);
		
		if (startStuffCreated) {
			final long matchInitTimeMS = match.getInitTimeMS();
			
			final int
				viewportX = arena.getViewportX(),
				viewportY = arena.getViewportY()
			;
			
			/* paint start panels */
			if (
				updateStartTimeMS <
				(matchInitTimeMS + START_PANEL_FOLD_UP_START_MS)
			) {
				for (int i = 0; i < startPanelImages.length; i++) {
					g.drawImage(
						startPanelImages[i],
						
						-viewportX +
						Arena.PLAY_AREA_X +
						startPanelPositions[i][0],
						
						-viewportY + startPanelPositions[i][1],
						
						Graphics.TOP | Graphics.LEFT
					);
				}
			} else {
				final int
					foldingUpHeight = (int)(
						(START_PANEL_HEIGHT -
							(updateStartTimeMS - (
								matchInitTimeMS + START_PANEL_FOLD_UP_START_MS)
							) /	START_PANEL_FOLD_UP_SLICE_MS
						)
					)
				;
				if (foldingUpHeight > 0) {
					for (int i = 0; i < startPanelImages.length; i++) {
						/*
						System.out.println(
							"y: " + ((START_PANEL_HEIGHT / 2) -
							(foldingUpHeight / 2)) + ", fuh: " +
							foldingUpHeight + ", sppx: " +
							startPanelPositions[i][0] +
							", sppy: " +
							(startPanelPositions[i][1] - (foldingUpHeight / 2))
						);
						*/
						
						g.drawRegion(
							startPanelImages[i],
							
							0,
							(START_PANEL_HEIGHT / 2) - (foldingUpHeight / 2),
							
							START_PANEL_WIDTH,
							foldingUpHeight,
							Sprite.TRANS_NONE,
							
							-viewportX +
							Arena.PLAY_AREA_X +
							startPanelPositions[i][0],
							
							-viewportY +
							Arena.PLAY_AREA_Y +
							startPanelPositions[i][1] -
							(foldingUpHeight / 2),
							
							Graphics.TOP | Graphics.LEFT
						);
					}
				}
			}
		}
		
		hud.paint(g);
		
		if (message != null) {
			g.setColor(0xFFFFFF);
			g.drawString(
				message,
				width / 2, height / 2,
				Graphics.TOP | Graphics.LEFT
			);
		}
		
		/*
		MIDP2Utils.drawTextWithFontSheet(
			"testing 1.. 2.. 3..", 20, 20,
			g, 0
		);
		*/
	}
	
	/**
	 * @return the humanPlayerWorm, null if no such worm exists.
	 */
	public Worm getHumanPlayerWorm() {
		return humanPlayerWorm;
	}
	
	/**
	 * @return the humanPlayer
	 */
	public Player getHumanPlayer() {
		return humanPlayer;
	}

	/* (non-Javadoc)
	 * @see loach.ui.midp2.screen.MIDP2Screen#update()
	 */
	private void update() {
		updateStartTimeMS = System.currentTimeMillis();
		
		if (!match.isDisposed()) {
			for (int i = 0; i < cpuWormControllers.length; i++)
				cpuWormControllers[i].update();
			
			if (match.getMatchState() == Match.MATCH_STATE_STARTING) {
				if (!startStuffCreated) {
					final Worm[] worms = match.getWorms();
					
					startPanelImages = new Image[worms.length];
					startIndicatorSprites = new Sprite[worms.length];
					startPanelPositions = new int[worms.length][2];
					
					WormSegment ws;
					Image img;
					Worm w;
					Player p;
					Sprite s;
					int headWS_X, headWS_Y, pos;
					
					for (int i = 0; i < worms.length; i++) {
						w = worms[i];
						p = w.getPlayer();
						
						ws = w.getHeadSegment();
						
						headWS_X = ws.getX();
						headWS_Y = ws.getY();
						
						img = createStartPanelImage(
							p.getName(),
							String.valueOf(w.getBounty()),
							PlayerSettings.PLAYER_TYPE_DISABLED
						);
						
						final int x, y;
						switch (START_PANEL_POSITIONS[i]) {
							case Util.POSITION_LEFT:
								x =
									Arena.PLAY_AREA_X +
									(headWS_X * Arena.POINT_WIDTH) -
									START_PANEL_WIDTH - START_PANEL_SPACING
								;
								y =
									Arena.PLAY_AREA_Y +
									(headWS_Y * Arena.POINT_HEIGHT) +
									2 - (START_PANEL_HEIGHT / 2)
								;
								break;
							case Util.POSITION_RIGHT:
								x =
									Arena.PLAY_AREA_X +
									(headWS_X * Arena.POINT_WIDTH) + 4 +
									START_PANEL_SPACING
								;
								y =
									Arena.PLAY_AREA_Y +
									(headWS_Y * Arena.POINT_HEIGHT) +
									2 - (START_PANEL_HEIGHT / 2)
								;
								break;
							case Util.POSITION_TOP:
								x =
									Arena.PLAY_AREA_X +
									(headWS_X * Arena.POINT_WIDTH) +
									2 - (START_PANEL_WIDTH / 2)
								;
								y = Arena.PLAY_AREA_Y +
									(headWS_Y * Arena.POINT_HEIGHT) -
									START_PANEL_HEIGHT - START_PANEL_SPACING
								;
								break;
							case Util.POSITION_BOTTOM:
								x =
									Arena.PLAY_AREA_X +
									(headWS_X * Arena.POINT_WIDTH) +
									2 - (START_PANEL_WIDTH / 2)
								;
								y =
									Arena.PLAY_AREA_Y +
									(headWS_Y * Arena.POINT_HEIGHT) + 4 +
									START_PANEL_SPACING
								;
								break;
							default:
								throw new Error();
						}
						
						/*
						s.setPosition(
							Arena.PLAY_AREA_X + (
								headWS_X * Arena.POINT_WIDTH
							) + 5,
							Arena.PLAY_AREA_Y + (headWS_Y * Arena.POINT_HEIGHT)
						);
						
						*/
						
						startPanelPositions[i][0] = x;
						startPanelPositions[i][1] = y;
						
						startPanelImages[i] = img;
						
						s = new Sprite(MIDP2LoachUI.START_INDICATOR_IMAGE);
						s.setPosition(
							Arena.PLAY_AREA_X + (
								headWS_X * Arena.POINT_WIDTH
							) - 4,
							Arena.PLAY_AREA_Y + (
								headWS_Y * Arena.POINT_HEIGHT
							) - 4
						);
						
						lesserArenaHUDLayer.append(s);
						
						startIndicatorSprites[i] = s;
					}
					
					startStuffCreated = true;
				}
				
				if (
					countdown != 4 &&
					(updateStartTimeMS >= timeInitMS) &&
					(updateStartTimeMS < (timeInitMS + COUNTDOWN_4_END_MS))
				) {
					countdown = 4;
					message = COUNTDOWN_4_TEXT;
					//midp2LoachUI.playSound(MIDP2LoachUI.SOUND_ID_FOUR, true);
				}
				else if (
					countdown != 3 &&
					(updateStartTimeMS >= (timeInitMS + COUNTDOWN_4_END_MS)) &&
					(updateStartTimeMS < (timeInitMS + COUNTDOWN_3_END_MS))
				) {
					countdown = 3;
					message = COUNTDOWN_3_TEXT;
					//midp2LoachUI.playSound(MIDP2LoachUI.SOUND_ID_THREE, true);
				}
				else if (
					countdown != 2 &&
					(updateStartTimeMS >= (timeInitMS + COUNTDOWN_3_END_MS)) &&
					(updateStartTimeMS < (timeInitMS + COUNTDOWN_2_END_MS))
				) {
					countdown = 2;
					message = COUNTDOWN_2_TEXT;
					//midp2LoachUI.playSound(MIDP2LoachUI.SOUND_ID_TWO, true);
				}
				else if (
					countdown != 1 &&
					(updateStartTimeMS >= (timeInitMS + COUNTDOWN_2_END_MS)) &&
					(updateStartTimeMS < (timeInitMS + COUNTDOWN_1_END_MS))
				) {
					countdown = 1;
					message = COUNTDOWN_1_TEXT;
					//midp2LoachUI.playSound(MIDP2LoachUI.SOUND_ID_ONE, true);
				}
				
				if (
					updateStartTimeMS >
					(timeInitMS + TIME_UNTIL_MATCH_START_MS)
				) {
					countdown = -1;
					message = null;
					
					/*
					MIDP2LoachUI.SOUND_FOUR.close();
					MIDP2LoachUI.SOUND_THREE.close();
					MIDP2LoachUI.SOUND_TWO.close();
					MIDP2LoachUI.SOUND_ONE.close();
					
					*/
					
					match.start();
				}
			}
			
			if (match.getMatchState() == Match.MATCH_STATE_STARTED) {
				if (startStuffCreated) {
					final Worm[] worms = match.getWorms();
					
					Sprite s;
					for (int i = 0; i < worms.length; i++) {
						s = startIndicatorSprites[i];
						lesserArenaHUDLayer.remove(s);
					}
					startPanelImages = null;
					startIndicatorSprites = null;					
					startStuffCreated = false;
				}
			}
			
			match.update();
			
			if (match.isDisposed()) {
				final ScoreScreen
					scoreScreen = new ScoreScreen(midp2LoachUI, match)
				;
				display.setCurrent(scoreScreen);
			} else {
				arena.update();
				hud.update();
				
				if (repaintTicks > 0) repaintTicks--;
				else {
					repaint();
					repaintTicks = frameSkip;
				}
				
				if ((lastSecondMS + 1000) < updateStartTimeMS) {
					lastSecondMS = updateStartTimeMS;
					currentFPS = frames;
					frames = 0;
				}
				frames++;	
			}
		}
	}
	
	/**
	 * @return the currentFPS
	 */
	public int getCurrentFPS() {
		return currentFPS;
	}
	
	private void next() {
		match.dispose();
		
		final ScoreScreen scoreScreen = new ScoreScreen(midp2LoachUI, match);
		display.setCurrent(scoreScreen);
	}
	
	/**
	 * @return the updateStartTimeMS
	 */
	public long getUpdateStartTimeMS() {
		return updateStartTimeMS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (!match.isDisposed()) {
			if (threadSleep >= 0) {
				try {
					Thread.sleep(threadSleep);
				} catch (InterruptedException e) { }
			}
			update();
		}
	}
	
	private Image createStartPanelImage(
		final String playerName, final String bountyText,
		final int playerType
	) {		
		final Image image = Image.createImage(
			START_PANEL_WIDTH,
			START_PANEL_HEIGHT
		);
		final Graphics g = image.getGraphics();
		
		g.drawImage(
			MIDP2LoachUI.START_PANEL_IMAGE,
			0, 0,
			Graphics.TOP | Graphics.LEFT
		);
		MIDP2Utils.drawTextWithFontSheet(playerName, 1, 1, g, 0);
		MIDP2Utils.drawTextWithFontSheet(
			bountyText,
			35, 10,
			g, 0
		);
		return image;
	}
	
	/**
	 * @return the lesserArenaHUDLayer
	 */
	public LayerManager getLesserArenaHUDLayer() {
		return lesserArenaHUDLayer;
	}
	
	/**
	 * @return the arenaHUDLayer
	 */
	public LayerManager getArenaHUDLayer() {
		return arenaHUDLayer;
	}
}
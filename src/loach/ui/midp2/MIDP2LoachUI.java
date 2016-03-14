/**
 * 
 */
package loach.ui.midp2;

import java.io.IOException;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import loach.ui.midp2.db.LoachDB;
import loach.ui.midp2.screen.title.TitleScreen;

/**
 * Loach Mobile - MIDP2LoachUI
 * 
 * Loach Mobile is ruled by the view, rather than the model. Therefore,
 * this enables you to have the view as the kickstart, starting up
 * various parts of the model as the view needs to, and then listening
 * in to broadcasted events.
 * 
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class MIDP2LoachUI extends MIDlet {
	//private static final DumbClass DUMB_OBJ = new DumbClass();
	
	private final Display display;
	
	public static final String TINY_NAME = "LoachM";
	public static final String SHORT_NAME = "Loach Mobile";
	public static final String LONG_NAME = "Overlord Loach Mobile";
	public static final String VERSION = "0.91.1d";
	
	public static final String LONG_TITLE = LONG_NAME + " " + VERSION;
	public static final String SHORT_TITLE = SHORT_NAME + " " + VERSION;
	
	private final LoachDB loachDB;
	
	private static final String
		TITLE_SCREEN_LOGO_IMG_PATH = "/ts_logo.png",
		GREEN_WORM_SEGMENT_IMG_PATH = "/green2.png",
		BLUE_WORM_SEGMENT_IMG_PATH = "/blue2.png",
		RED_WORM_SEGMENT_IMG_PATH = "/red2.png",
		BOGEY_WORM_SEGMENT_IMG_PATH = "/bogey1.png",
		WHITE_WORM_SEGMENT_IMG_PATH = "/white1.png",
		GREY_WORM_SEGMENT_IMG_PATH = "/grey1.png",
		BANANA_WORM_SEGMENT_IMG_PATH = "/banana1.png",
		GROWTH_PILL_IMG_PATH = "/growthpill.png",
		SPEEDUP_PILL_IMG_PATH = "/speeduppill.png",
		SLOWDOWN_PILL_IMG_PATH = "/slowdownpill.png",
		KEY_IMG_PATH = "/key1.png",
		//BOMB_IMG_PATH = "/bomb1.png",
		MONEY_IMG_PATH = "/money1.png",
		ARENA_DOOR_SET_IMG_PATH = "/arenadoorset1.png",
		ARENA_WALL_SET_1_IMG_PATH = "/arenawallset1.png",
		ARENA_WALL_CORNER_IMG_PATH = "/arenawallcorner1.png",
		ARENA_FLOOR_TILE_SET_IMG_PATH = "/arenafloortileset.png",
		//TIME_SEGMENT_SET_IMG_PATH = "/timesegmentset1.png",
		TIME_INDICATOR_IMG_PATH = "/timeindicator.png",
		NORMAL_BLOCK_IMG_PATH = "/block1.png",
		SMALL_BLOCK_IMG_PATH = "/block2.png",
		EXIT_BLOCK_IMG_PATH = "/exitblock1.png",
		START_PANEL_IMG_PATH = "/startpanel.png",
		START_INDICATOR_IMG_PATH = "/startindicator.png",
		
		AUTHOR_BW_IMG_PATH = "/authorbw.png",
		
		FONT_SHEET_NORMAL_PATH = "/fontsheet2.png"
		
		// ONE_SND_PATH = "/one.amr",
		// TWO_SND_PATH = "/two.amr",
		// THREE_SND_PATH = "/three.amr",
		// FOUR_SND_PATH = "/four.amr",
		
		// AMR_CONTENT_TYPE = "audio/amr"
	;
	
	/** Green worm segment image. */
	public static final Image GREEN_WORM_SEGMENT_IMAGE;
	
	/** Red worm segment image. */
	public static final Image RED_WORM_SEGMENT_IMAGE;
	
	/** Blue worm segment image. */
	public static final Image BLUE_WORM_SEGMENT_IMAGE;
	
	/** Bogey worm segment image. */
	public static final Image BOGEY_WORM_SEGMENT_IMAGE;
	
	/** White worm segment image. */
	public static final Image WHITE_WORM_SEGMENT_IMAGE;
	
	/** Grey worm segment image. */
	public static final Image GREY_WORM_SEGMENT_IMAGE;
	
	/** Banana worm segment image. */
	public static final Image BANANA_WORM_SEGMENT_IMAGE;
	
	/** Growth pill image. */
	public static final Image GROWTH_PILL_IMAGE;
	
	/** Speedup pill image. */
	public static final Image SPEEDUP_PILL_IMAGE;
	
	/** Slowdown pill image. */
	public static final Image SLOWDOWN_PILL_IMAGE;
	
	/** Money image. */
	public static final Image MONEY_IMAGE;
	
	///** Bomb image. */
	//public static final Image BOMB_IMAGE;
	
	/** key image. */
	public static final Image KEY_IMAGE;
	
	/** Title screen logo image. */
	public static final Image TITLE_SCREEN_LOGO_IMAGE;
	
	/** Arena floor tile set image. */
	public static final Image ARENA_FLOOR_TILE_SET_IMAGE;
	
	/** Match screen time indicator image. */
	public static final Image TIME_INDICATOR_IMAGE;
	
	/** Arena door image. */
	public static final Image ARENA_DOOR_SET_IMAGE;
	
	/** Arena wall set 1 image. */
	public static final Image ARENA_WALL_SET_1_IMAGE;
	
	/** Arena wall corner image. */
	public static final Image ARENA_WALL_CORNER_IMAGE;
	
	/** Small block image. */
	public static final Image SMALL_BLOCK_IMAGE;
	
	/** Normal block image. */
	public static final Image NORMAL_BLOCK_IMAGE;
	
	/** Exit block image. */
	public static final Image EXIT_BLOCK_IMAGE;
	
	/** Author black and white image. */
	public static final Image AUTHOR_BW_IMAGE;
	
	/** Normal font sheet image. */
	public static final Image FONT_SHEET_NORMAL_IMAGE;
	
	/** Start panel image. */
	public static final Image START_PANEL_IMAGE;
	
	/** Start indicator image. */
	public static final Image START_INDICATOR_IMAGE;
	
	static {
		try {
			GREEN_WORM_SEGMENT_IMAGE = Image.createImage(
				GREEN_WORM_SEGMENT_IMG_PATH
			);
			RED_WORM_SEGMENT_IMAGE = Image.createImage(
				RED_WORM_SEGMENT_IMG_PATH
			);
			BLUE_WORM_SEGMENT_IMAGE = Image.createImage(
				BLUE_WORM_SEGMENT_IMG_PATH
			);
			BOGEY_WORM_SEGMENT_IMAGE = Image.createImage(
				BOGEY_WORM_SEGMENT_IMG_PATH
			);
			WHITE_WORM_SEGMENT_IMAGE = Image.createImage(
				WHITE_WORM_SEGMENT_IMG_PATH
			);
			GREY_WORM_SEGMENT_IMAGE = Image.createImage(
				 GREY_WORM_SEGMENT_IMG_PATH
			);
			BANANA_WORM_SEGMENT_IMAGE = Image.createImage(
				 BANANA_WORM_SEGMENT_IMG_PATH
			);
			GROWTH_PILL_IMAGE = Image.createImage(
				 GROWTH_PILL_IMG_PATH
			);
			SPEEDUP_PILL_IMAGE = Image.createImage(
				 SPEEDUP_PILL_IMG_PATH
			);
			SLOWDOWN_PILL_IMAGE = Image.createImage(
				 SLOWDOWN_PILL_IMG_PATH
			);
			MONEY_IMAGE = Image.createImage(
				 MONEY_IMG_PATH
			);
			//BOMB_IMAGE = Image.createImage(
			//	 BOMB_IMG_PATH
			//);
			KEY_IMAGE = Image.createImage(
				 KEY_IMG_PATH
			);			
			TITLE_SCREEN_LOGO_IMAGE = Image.createImage(
				 TITLE_SCREEN_LOGO_IMG_PATH
			);
			ARENA_FLOOR_TILE_SET_IMAGE = Image.createImage(
				 ARENA_FLOOR_TILE_SET_IMG_PATH
			);
			TIME_INDICATOR_IMAGE = Image.createImage(
				 TIME_INDICATOR_IMG_PATH
			);
			ARENA_DOOR_SET_IMAGE = Image.createImage(
				ARENA_DOOR_SET_IMG_PATH
			);
			ARENA_WALL_SET_1_IMAGE = Image.createImage(
				ARENA_WALL_SET_1_IMG_PATH
			);
			ARENA_WALL_CORNER_IMAGE = Image.createImage(
				ARENA_WALL_CORNER_IMG_PATH
			);
			SMALL_BLOCK_IMAGE = Image.createImage(
				SMALL_BLOCK_IMG_PATH
			);
			NORMAL_BLOCK_IMAGE = Image.createImage(
				NORMAL_BLOCK_IMG_PATH
			);
			EXIT_BLOCK_IMAGE = Image.createImage(
				EXIT_BLOCK_IMG_PATH
			);
			AUTHOR_BW_IMAGE = Image.createImage(
				AUTHOR_BW_IMG_PATH
			);
			FONT_SHEET_NORMAL_IMAGE = Image.createImage(
				FONT_SHEET_NORMAL_PATH
			);
			START_PANEL_IMAGE = Image.createImage(
				START_PANEL_IMG_PATH
			);
			START_INDICATOR_IMAGE = Image.createImage(
				START_INDICATOR_IMG_PATH
			);			
		} catch (final IOException e) {
			throw new Error(
				"Couldn't load image resources: " + e.toString()
			);
		}
	}
	
	/*
	
	public static final int
		SOUND_ID_FOUR = 0,
		SOUND_ID_THREE = 1,
		SOUND_ID_TWO = 2,
		SOUND_ID_ONE = 3
	;
	
	private Player currentPlayer = null;
	*/
	
	// /**
	//  * @param id
	//  * @param forcePlay
	// * 
	// * @return Returns true if sound was played, false if not.
	// */
	/*
	public boolean playSound(final int id, final boolean forcePlay) {
		if (currentPlayer != null) {
			if (currentPlayer.getState() == Player.STARTED) {
				try {
					currentPlayer.stop();
				} catch (final MediaException e) {}
			}
			currentPlayer.close();
		}
		
		/*
		
		if (
			currentPlayer != null &&
			currentPlayer.getState() == Player.STARTED
		) {
			if (forcePlay) {
				try {
					currentPlayer.stop();
					currentPlayer.close();
				} catch (final MediaException e) {}
			} else return false;
		}
		}
		
		/*
		
		final String path;
		
		switch (id) {
			case SOUND_ID_FOUR: path = FOUR_SND_PATH; break;
			case SOUND_ID_THREE: path = THREE_SND_PATH; break;
			case SOUND_ID_TWO: path = TWO_SND_PATH; break;
			case SOUND_ID_ONE: path = ONE_SND_PATH; break;
			default: throw Util.UNMAPPED_ARGUMENT_EXCEPTION;
		}
		
		try {
			currentPlayer = Manager.createPlayer(
				getClass().getResourceAsStream(path),
				AMR_CONTENT_TYPE
			);
			
			currentPlayer.realize();
			currentPlayer.prefetch();
			currentPlayer.start();
			
			return true;
		} catch (final IOException e) {
			System.out.println("playing failed - " + e.toString());
		} catch (final MediaException e) {
			System.out.println("playing failed - " + e.toString());
		}
		
		return false;
	}
	*/
	
	/**
	 *
	 */
	public MIDP2LoachUI() {
		loachDB = new LoachDB();
		display = Display.getDisplay(this);
	}
	
	/**
	 * @return the display
	 */
	public Display getDisplay() {
		return display;
	}

	/* (non-Javadoc)
	 * @see loach.ui.LoachUI#dispose()
	 */
	public void quit() {
		loachDB.dispose();
		
		try {
			destroyApp(true);
		} catch (MIDletStateChangeException e) {}
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		final TitleScreen titleScreen = new TitleScreen(this);
		display.setCurrent(titleScreen);		
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {
		notifyPaused();
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(final boolean unconditional)
	throws MIDletStateChangeException {
		notifyDestroyed();
	}
	
	/**
	 * @return the loachDB
	 */
	public LoachDB getLoachDB() {
		return loachDB;
	}
}
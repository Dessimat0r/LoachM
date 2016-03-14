/**
 * 
 */
package loach.ui.midp2.util;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

import loach.ui.midp2.MIDP2LoachUI;

/**
 * LoachM - MIDP2Utils
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class MIDP2Utils {	
	public static final Command
		SCREEN_ACCEPT_COMMAND = new Command("Accept", 1, Command.SCREEN)
	;
	public static final Command
		SCREEN_CANCEL_COMMAND = new Command("Cancel", 2, Command.BACK)
	;
	public static final Command
		SCREEN_SELECT_COMMAND = new Command("Select", 1, Command.SCREEN)
	;
	public static final Command
		SCREEN_QUIT_COMMAND = new Command("Quit", 2, Command.BACK)
	;
	public static final Command
		SCREEN_BACK_COMMAND = new Command("Back", 2, Command.BACK)
	;
	public static final Command
		SCREEN_NEXT_COMMAND = new Command("Next", 1, Command.SCREEN)
	;
	
	private static final int
		TEXT_CHAR_WIDTH = 5,
		TEXT_CHAR_HEIGHT = 8,
		FONT_SHEET_ROWS = 4,
		FONT_SHEET_COLS = 25,
		FONT_SHEET_CHARS = 94
	;
	
	private static final char FONT_SHEET_START_CHAR = '!';
	
	/**
	 * @param text
	 * @param x
	 * @param y
	 * @param g
	 * @param spacingX 
	 */
	public static void drawTextWithFontSheet(
		final String text,
		final int x, final int y,
		final Graphics g,
		final int spacingX
	) {
		int row, col;
		char character;
		
		for (int i = 0; i < text.length(); i++) {
			character = text.charAt(i);
			
			if (character == ' ') continue;
			
			row = (character - FONT_SHEET_START_CHAR) / FONT_SHEET_COLS;
			col = (character - FONT_SHEET_START_CHAR) % FONT_SHEET_COLS;
			
			//System.out.println("char: " + character + " charvalue: " + Character.character + ", row: " + row + ", col: " + col);
			
			g.drawRegion(
				MIDP2LoachUI.FONT_SHEET_NORMAL_IMAGE,
				
				col * TEXT_CHAR_WIDTH,
				row * TEXT_CHAR_HEIGHT,
				TEXT_CHAR_WIDTH,
				TEXT_CHAR_HEIGHT,
				
				Sprite.TRANS_NONE,
				
				x + ((TEXT_CHAR_WIDTH + spacingX) * i),
				y,
				
				Graphics.TOP | Graphics.LEFT
			);
		}
	}
}
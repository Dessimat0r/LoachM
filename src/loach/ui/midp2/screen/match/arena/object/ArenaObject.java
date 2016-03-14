/**
 * 
 */
package loach.ui.midp2.screen.match.arena.object;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import loach.model.field.object.FieldObject;
import loach.ui.midp2.screen.match.arena.Arena;
import util.Util;


/**
 * LoachM - ArenaObject
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public abstract class ArenaObject {
	protected final Arena arena;
	protected final FieldObject fieldObject;
	
	protected final Sprite sprite;
	
	protected final int x, y, width, height;
	
	protected boolean disposed = false;
	
	/**
	 * @param arena 
	 * @param fieldObject
	 * @param image 
	 */
	public ArenaObject(
		final Arena arena,
		final FieldObject fieldObject,
		final Image image
	) {
		if (arena == null || fieldObject == null || image == null)
			throw Util.NULL_ARGUMENT_EXCEPTION;
		
		this.arena = arena;
		this.fieldObject = fieldObject;
		
		x = fieldObject.getX() * Arena.POINT_WIDTH;
		y = fieldObject.getY() * Arena.POINT_HEIGHT;
		width = fieldObject.getWidth() * Arena.POINT_WIDTH;
		height = fieldObject.getWidth() * Arena.POINT_HEIGHT;
		
		/* add sprite */
		sprite = new Sprite(image);
		sprite.setPosition(Arena.PLAY_AREA_X + x, Arena.PLAY_AREA_Y + y);
		
		arena.getMatchScreen().getArenaObjectLayer().append(sprite);
		arena.addArenaObject(this);
	}
	
	/**
	 * @return the x
	 */
	public final int getX() {
		return x;
	}
	
	/**
	 * @return the y
	 */
	public final int getY() {
		return y;
	}
	
	/**
	 * @return the width
	 */
	public final int getWidth() {
		return width;
	}
	
	/**
	 * @return the height
	 */
	public final int getHeight() {
		return height;
	}
	
	/**
	 * @return Returns the FieldObject to which this ArenaObject is attached.
	 */
	public final FieldObject getFieldObject() {
		return fieldObject;
	}
	
	/* (non-Javadoc)
	 * @see util.sys.Disposable#dispose()
	 */
	public void dispose() {
		if (disposed) throw new IllegalStateException(
			"Cannot redispose an arena object"
		);
		arena.getMatchScreen().getArenaObjectLayer().remove(sprite);
		arena.removeArenaObject(this);
		disposed = true;
	}

	/* (non-Javadoc)
	 * @see util.sys.Disposable#isDisposed()
	 */
	public final boolean isDisposed() {
		return disposed;
	}
}
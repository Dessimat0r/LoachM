/**
 * 
 */
package loach.model.field.object;

import loach.model.field.Field;
import util.Util;

/**
 * Loach V (Java) - ArenaObject
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public abstract class FieldObject {	
	public static final int FO_ID_WORM_SEGMENT = 0;
	public static final int FO_ID_PICKUP = 1;
	public static final int FO_ID_BLOCK = 2;
	public static final int FO_ID_EXIT_BLOCK = 3;
	
	protected final Field field;
	protected final int foID, x, y, width, height;
	protected final String name;
	
	protected boolean activated = false, disposed = false;
	
	protected final long spawnTimeMS;
	
	/**
	 * Creates a new FieldObject.
	 * 
	 * @param field The field that this FieldObject exists within.
	 * @param name The name given to this type of FieldObject.
	 * @param foID The FieldObject ID.
	 * @param x The x coordinate of the FieldObject.
	 * @param y The y coordinate of the FieldObject.
	 * @param width The width of the FieldObject.
	 * @param height The height of the FieldObject.
	 */
	public FieldObject(
		final Field field,
		final String name,
		final int foID,
		final int x, final int y,
		final int width, final int height
	) {
		if (field == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		this.field = field;
		this.name = name;
		this.foID = foID;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		spawnTimeMS = System.currentTimeMillis();
		field.addFieldObject(this);
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
	 * @return the name
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * @return Returns true if this object is activated, false otherwise.
	 */
	public final boolean isActivated() {
		return activated;
	}
	
	/**
	 * @return Returns true if this object is disposed, false otherwise.
	 */
	public final boolean isDisposed() {
		return disposed;
	}
	
	/* (non-Javadoc)
	 * @see util.sys.Disposable#dispose()
	 */
	public void dispose() {
		if (disposed) throw Util.NEEDS_UNDISPOSED_STATE_EXCEPTION;
		
		field.dispatchFieldObjectDisposalEvent(this);
		field.removeFieldObject(this);
		disposed = true;
	}
	
	/**
	 * @return Returns the Field that this FieldObject belongs to.
	 */
	public final Field getField() {
		return field;
	}
	
	/**
	 * @return Returns when this FieldObject was spawned in millseconds.
	 */
	public final long getSpawnTimeMS() {
		return spawnTimeMS;
	}
	
	/**
	 * 
	 */
	public void activate() {
		if (activated) throw Util.NEEDS_UNACTIVATED_STATE_EXCEPTION;
		
		field.dispatchFieldObjectCreationEvent(this);
	}
	
	/**
	 * @return the foID
	 */
	public final int getFoID() {
		return foID;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
}
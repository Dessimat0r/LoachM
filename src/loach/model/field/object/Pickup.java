/**
 * 
 */
package loach.model.field.object;

import loach.model.field.Field;
import loach.model.info.item.LoachItem;

/**
 * LoachM - Pickup
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class Pickup extends FieldObject {
	private final LoachItem item;
	private final long lifetimeMS;
	
	/**
	 * @param field
	 * @param item 
	 * @param x 
	 * @param y 
	 * @param lifetimeMS 
	 */
	public Pickup(
		final Field field, final LoachItem item,
		final int x, final int y,
		final long lifetimeMS
	) {
		super(
			field,
			"Pickup (" + item.getName() + ")",
			FO_ID_PICKUP,
			x, y,
			item.getFieldWidth(), item.getFieldHeight()
		);
		
		this.item = item;
		this.lifetimeMS = lifetimeMS;
	}
	
	/**
	 * @return the item
	 */
	public LoachItem getItem() {
		return item;
	}
	
	/**
	 * @return
	 */
	public boolean hasExpired() {
		/* pickups are removed from the arena after 20 seconds */
		return
			lifetimeMS != 0 &&
			((spawnTimeMS + lifetimeMS) < System.currentTimeMillis())
		;
	}
	
	/**
	 * @return the timeToExpiryMS
	 */
	public long getLifetimeMS() {
		return lifetimeMS;
	}
}
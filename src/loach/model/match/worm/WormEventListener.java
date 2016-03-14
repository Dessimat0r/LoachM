/**
 * 
 */
package loach.model.match.worm;

import loach.model.field.object.FieldObject;
import loach.model.field.object.WormSegment;
import loach.model.info.item.LoachItem;

/**
 * LoachM - WormEventListener
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public interface WormEventListener {
	/**
	 * @param wormSegment
	 */
	public void onWormSegmentAddition(final WormSegment wormSegment);
	
	/**
	 * @param wormSegment
	 */
	public void onWormSegmentSubtraction(final WormSegment wormSegment);
	
	/**
	 * @param worm
	 */
	public void onWormDeath(final Worm worm);
	
	/**
	 * @param worm 
	 * @param fieldObject
	 */
	public void onWormFieldObjectCollision(
		final Worm worm, final FieldObject fieldObject
	);
	
	/**
	 * @param worm
	 */
	public void onWormFieldWallCollision(final Worm worm);
	
	/**
	 * @param worm
	 * @param item 
	 */
	public void onWormItemUse(final Worm worm, final LoachItem item);	
}
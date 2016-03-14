/**
 * 
 */
package loach.model.field.object;

import loach.model.info.WormInfo;
import loach.model.match.worm.Worm;
import util.Util;

/**
 * LoachM - WormSegment
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class WormSegment extends FieldObject {	
	private final Worm worm;
	private final WormInfo wormInfo;
	private final int heading;

	/**
	 * @param worm 
	 * @param x 
	 * @param y 
	 * @param heading 
	 */
	public WormSegment(
		final Worm worm,
		final int x, final int y,
		final int heading
	) {
		super(
			worm.getMatch().getField(),
			"Worm Segment (" + worm.getWormInfo().getName() + ")",
			FO_ID_WORM_SEGMENT,
			x, y,
			1, 1
		);
		
		if (worm == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		this.worm = worm;
		this.heading = heading;
		
		wormInfo = worm.getWormInfo();
	}
	
	/**
	 * @return the wormInfo
	 */
	public WormInfo getWormInfo() {
		return wormInfo;
	}
	
	/**
	 * @return the worm
	 */
	public Worm getWorm() {
		return worm;
	}
}
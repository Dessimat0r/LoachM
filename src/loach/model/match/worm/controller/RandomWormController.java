/**
 * 
 */
package loach.model.match.worm.controller;

import loach.model.match.worm.Worm;
import util.Util;

/**
 * LoachM - RandomWormController
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class RandomWormController implements WormController {
	private final Worm worm;
	
	/**
	 * @param worm
	 */
	public RandomWormController(final Worm worm) {
		if (worm == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		this.worm = worm;
	}
	
	/**
	 * 
	 */
	public void update() {
		if (worm.isAlive()) {
			final int direction;
			switch (Util.getRandomInt(0, 3)) {
				case 0: direction = Util.DIRECTION_NORTH; break;
				case 1: direction = Util.DIRECTION_EAST; break;
				case 2: direction = Util.DIRECTION_SOUTH; break;
				case 3: direction = Util.DIRECTION_WEST; break;
				default: throw new RuntimeException();
			}
			if (worm.isValidDirection(direction)) worm.setDirection(direction);
		}
	}
}
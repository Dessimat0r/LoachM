/**
 * 
 */
package loach.model.match.worm.controller;

import loach.model.field.Field;
import loach.model.field.object.FieldObject;
import loach.model.field.object.Pickup;
import loach.model.match.worm.Worm;
import util.Util;
/**
 * LoachM - SimpleWormController
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class SimpleWormController implements WormController {
	private final Field field;
	private final Worm worm;
	
	private final int[] nextSegPosArea = new int[4];
	private FieldObject occ = null;
	private Pickup pickupOcc = null;

	/**
	 * @param worm
	 */
	public SimpleWormController(final Worm worm) {
		if (worm == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		this.worm = worm;
		field = worm.getMatch().getField();
	}

	/* (non-Javadoc)
	 * @see loach.model.worm.controller.WormController#update()
	 */
	public void update() {
		if (worm.isAlive() && worm.getDirection() == Util.DIRECTION_NONE) {
			if (worm.calculateNextSegmentPos(nextSegPosArea)) {
				if (
					field.doorsOpen() &&
					field.areaIntersectsDoorArea(
						nextSegPosArea[0], nextSegPosArea[1],
						1, 1
					)
				) field.normaliseArea(nextSegPosArea);
				
				if (field.areaIsInBounds(
					nextSegPosArea[0], nextSegPosArea[1], 1, 1
				)) {
					occ = field.getFirstOccupierInArea(
						nextSegPosArea[0], nextSegPosArea[1], 1, 1
					);
					if (occ != null) {
						if (occ.getFoID() == FieldObject.FO_ID_PICKUP) {
							pickupOcc = (Pickup)occ;
							if (pickupOcc.getItem().getDesirability() <= 0) {
								final int rhDir = Util.obtainRightHandDirection(
									worm.getHeading()
								);
								if (worm.isValidDirection(rhDir)) 
									worm.setDirection(rhDir);
							}
						} else {
							final int rhDir = Util.obtainRightHandDirection(
								worm.getHeading()
							);
							if (worm.isValidDirection(rhDir)) 
								worm.setDirection(rhDir);
						}
					}
				} else {
					final int rhDir = Util.obtainRightHandDirection(
						worm.getHeading()
					);
					if (worm.isValidDirection(rhDir)) worm.setDirection(rhDir);
				}
			}
		}
		
		/*
		// get most desirable pickup
		final Pickup mostDesirablePickup = field.getMostDesirablePickup();
		if (mostDesirablePickup.getItem().getDesirability() > 0) {
			
		}
		*/
	}
	
	private void process(final int direction) {
		
	}
	
	/*
	
	private Point findTarget() {
		if (field.hasPickups()) {
			Pickup mostDesirablePickup = getMostDesirablePickup();
			
			if (mostDesirablePickup != null)
				return mostDesirablePickup.getArea().getPosition();
		}
		return field.findFreeRandomPoint(); // may be null!
	}
	
	*/
	
	/*
	*
	 * Obtains the most desirable pickup on the field.
	 * @return Returns the most desirable pickup on the field.
	
	private final Pickup getMostDesirablePickup() {
		if (!field.hasPickups()) throw new IllegalStateException(
			"Cannot obtain most desirable pickup -- " +
			"the field contains no pickups."
		);
		final Vector pickups = field.getPickups();
		
		final int noOfPickups = pickups.size();
		Pickup currPickup, mostDesirablePickup = null;
		
		int mostDesirablePickupDesirability = -1;
		LoachItem mostDesirablePickupItem = null;
		
		for (int i = 0; i < noOfPickups; i++) {
			currPickup = (Pickup)pickups.elementAt(i);
			if (
				mostDesirablePickup != null ||
				currPickup.getItem().getDesirability() >
				mostDesirablePickupDesirability
			) {
				mostDesirablePickup = currPickup;
				mostDesirablePickupItem = currPickup.getItem();
				mostDesirablePickupDesirability =
					mostDesirablePickupItem.getDesirability()
				;
			}
		}
		return mostDesirablePickup;
	}
	
	*/
}
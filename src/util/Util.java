/**
 * 
 */
package util;

import java.util.Random;

/**
 * LoachM - Util
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class Util {
	/**
	 * 
	 */
	public static final Random RANDOM = new Random();
	
	/**
	 * No direction.
	 */
	public static final int DIRECTION_NONE = 0;
	
	/**
	 * North direction.
	 */
	public static final int DIRECTION_NORTH = 1 << 0;
	
	/**
	 * East direction.
	 */
	public static final int DIRECTION_EAST = 1 << 1;
	
	/**
	 * South direction.
	 */
	public static final int DIRECTION_SOUTH = 1 << 2;
	
	
	/**
	 * West direction.
	 */
	public static final int DIRECTION_WEST = 1 << 3;
	
	/**
	 * Center position.
	 */
	public static final int POSITION_CENTER = 0;
	
	/**
	 * Top position.
	 */
	public static final int POSITION_TOP = 1 << 0;
	
	/**
	 * Left position.
	 */
	public static final int POSITION_LEFT = 1 << 1;
	
	/**
	 * Right position.
	 */
	public static final int POSITION_RIGHT = 1 << 2;
	
	/**
	 * Bottom position.
	 */
	public static final int POSITION_BOTTOM = 1 << 3;	
	
	
	public static final int ULTRA_DIE_MAX_PROBABILITY = 10000;
	
	public static int rollUltraDie() {
		return Util.getRandomInt(0, ULTRA_DIE_MAX_PROBABILITY - 1);
	}
	
	/**
	 * Obtains the reverse direction of the given direction. For example,
	 * DIRECTION_NORTH will return DIRECTION_SOUTH.
	 * 
	 * @param direction
	 * @return
	 */
	public static int obtainReverseDirection(final int direction) {
		switch (direction) {
			case DIRECTION_NORTH: return DIRECTION_SOUTH;
			case DIRECTION_EAST: return DIRECTION_WEST;
			case DIRECTION_SOUTH: return DIRECTION_NORTH;
			case DIRECTION_WEST: return DIRECTION_EAST;
		}
		throw Util.UNMAPPED_ARGUMENT_EXCEPTION;
	}
	
	/**
	 * Obtains the reverse direction of the given direction. For example,
	 * DIRECTION_NORTH will return DIRECTION_SOUTH.
	 * 
	 * @param direction
	 * @return
	 */
	public static int obtainRightHandDirection(final int direction) {
		switch (direction) {
			case DIRECTION_NORTH: return DIRECTION_EAST;
			case DIRECTION_EAST: return DIRECTION_SOUTH;
			case DIRECTION_SOUTH: return DIRECTION_WEST;
			case DIRECTION_WEST: return DIRECTION_NORTH;
		}
		throw Util.UNMAPPED_ARGUMENT_EXCEPTION;
	}	
	
	public static final IllegalArgumentException
		NULL_ARGUMENT_EXCEPTION = new IllegalArgumentException("Null argument.")
	;
	public static final IllegalArgumentException
		UNEXPECTED_ARGUMENT_EXCEPTION = new IllegalArgumentException(
			"Unexpected argument."
		)
	;
	public static final IllegalStateException
		NEEDS_DISPOSED_STATE_EXCEPTION = new IllegalStateException(
			"Cannot call this method in this state, needs disposed state."
		)
	;
	public static final IllegalStateException
		NEEDS_UNDISPOSED_STATE_EXCEPTION = new IllegalStateException(
			"Cannot call this method in this state, needs undisposed state."
		)
	;
	public static final IllegalStateException
		NEEDS_ACTIVATED_STATE_EXCEPTION = new IllegalStateException(
			"Cannot call this method in this state, needs activated state."
		)
	;
	public static final IllegalStateException
		NEEDS_UNACTIVATED_STATE_EXCEPTION = new IllegalStateException(
			"Cannot call this method in this state, needs unactivated state."
		)
	;
	public static final IllegalStateException
		TOO_MANY_ELEMENTS_EXCEPTION = new IllegalStateException(
			"Too many elements."
		)
	;
	public static final IllegalStateException
			NO_ELEMENTS_EXCEPTION = new IllegalStateException(
				"No elements."
			)
	;
	public static final IllegalArgumentException
		UNMAPPED_ARGUMENT_EXCEPTION = new IllegalArgumentException(
			"Given element does not map to any other element."
		)
	;

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static int obtainPointToPointDistance(
		final int x1, final int y1,
		final int x2, final int y2
	) {
		return
			Util.difference(x1, x2) + 
			Util.difference(y2, y2)
		;
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param xFactor
	 * @param yFactor
	 * @param scaledRect 
	 */
	public static void scaleRect(
		final int x, final int y, final int width, final int height,
		final int xFactor, final int yFactor,
		final int[] scaledRect
	) {
		scaledRect[0] = x * xFactor;
		scaledRect[1] = y * yFactor;
		scaledRect[2] = width * xFactor;
		scaledRect[3] = height * yFactor;
	}

	/**
	 * Obtains the distance between rect1 and rect2.
	 * 
	 * @param x1
	 * @param y1
	 * @param width1
	 * @param height1
	 * 
	 * @param x2
	 * @param y2
	 * @param width2
	 * @param height2
	 * 
	 * @return
	 */
	public static int obtainRectToRectDistance(
		final int x1, final int y1, final int width1, final int height1,
		final int x2, final int y2, final int width2, final int height2
	) {
		final int oct;
	
		if ((x1 + width1) <= x2) {
			if ((y1 + width1) <= y2) oct = 0;
			else if (y1 >= (y2 + height2)) oct = 6;
			else oct = 7;
		}
		else if (x1 >= (x2 + width2)) {
			if ((y1 + height1) <= y2) oct = 2;
			else if (y1 >= (y2 + height2)) oct = 4;
			else  oct = 3;
		}
		else if ((y1 + height1) <= y2) oct = 1;
		else if (y1 >= (y2 + height2)) oct = 5;
		else return 0;
	
		switch (oct) {
			case 0:
				return -(((x1 + width1)  - x2) + ((y1 + height1) - y2));
			case 1:
				return -((y1 + height1) - y2);
			case 2:
				return -(
					((x2 + width2) - x1) + ((y1 + height1) - y2)
				);
			case 3:
				return -((x2 + width2) - x1);
			case 4:
				return -(
					((y2 + height2) - y1) + 
					((y2 + height2) - y1)
				);
			case 5:
				return -((y2 + height2) - y1);
			case 6:
				return -(
					((x1 + width1) - x2) + ((y2 + height2) - y1)
				);
			case 7:
				return -((x1 + width1) - x2);
		}
	
		throw new Error("There was a problem with the octal.");
	}

	/**
	 * Test for intersection.
	 * 
	 * @param aX Rect a, top-left x.
	 * @param aY Rect a, top-left y.
	 * @param aW Rect a, width.
	 * @param aH Rect a, height.
	 * 
	 * @param bX Rect b, top-left x.
	 * @param bY Rect b, top-left y.
	 * @param bW Rect b, width.
	 * @param bH Rect b, height.
	 * 
	 * @return Returns true if intersection occurs, false if not.
	 */
	public static boolean rectIntersectsRect(
		final int aX, final int aY, final int aW, final int aH, 
		final int bX, final int bY, final int bW, final int bH
	) {
		final int
			aLRX = aX + aW,
			aLRY = aY + aH,
			bLRX = bX + bW,
			bLRY = bY + bH
		;
		
		return (
			(
				((aX < bLRX) && (aY < bLRY) && (aLRX > bX) && (aLRY > bY)) ||
				((bX < aLRX) && (bY < aLRY) && (bLRX > aX) && (bLRY > aY))
			)
		);
	}

	/**
	 * Check for containment of rect b in rect a.
	 * 
	 * @param aX Rect a, top-left x.
	 * @param aY Rect a, top-left y.
	 * @param aW Rect a, width.
	 * @param aH Rect a, height.
	 * 
	 * @param bX Rect b, top-left x.
	 * @param bY Rect b, top-left y.
	 * @param bW Rect b, width.
	 * @param bH Rect b, height.
	 * 
	 * @return Returns true if rect a contains rect b, false if not.
	 */
	public static boolean rectContainsRect(
		final int aX, final int aY, final int aW, final int aH, 
		final int bX, final int bY, final int bW, final int bH
	) {
		return (
			((bX >= aX) && (bY >= aY)) &&
			(((bX + bW) <= (aX + aW)) && ((bY + bH) <= (aY + aH)))
		);
	}

	/**
	 * Check for containment of point b in rect a.
	 * 
	 * @param aX Rect a, top-left x.
	 * @param aY Rect a, top-left y.
	 * @param aW Rect a, width.
	 * @param aH Rect a, height.
	 * 
	 * @param bX Point b, x.
	 * @param bY Point b, y.
	 * 
	 * @return Returns true if rect a contains point b, false if not.
	 */
	public static boolean rectContainsPoint(
		final int aX, final int aY, final int aW, final int aH, 
		final int bX, final int bY
	) {
		return (
			(bX >= aX) && (bY >= aY) &&
			(bX <= (aX + aW)) && (bY <= (aY + aH))
		);
	}

	/**
	 * Clips rect against container.
	 * 
	 * @param rect
	 * @param container 
	 */
	public static void clipRectAgainstRect(
		final int[] rect,
		final int[] container
	) {
		if (rect == null || container == null)
			throw Util.NULL_ARGUMENT_EXCEPTION;
		
		if (rect[0] < container[0]) {
			final int dX = container[0] - rect[0];
			rect[0] = container[0];
			rect[2] -= dX;
		}
		
		if (rect[1] < container[1]) {
			final int dY = container[1] - rect[1];
			rect[1] = container[1];
			rect[3] -= dY;
		}
		
		if ((rect[0] + rect[2]) > (container[0] + container[2])) {
			final int dX = (rect[0] + rect[2]) - (container[0] + container[2]);
			rect[2] -= dX;
		}
		
		if ((rect[1] + rect[3]) > (container[1] + container[3])) {
			final int dY = (rect[1] + rect[3]) - (container[1] + container[3]);
			rect[3] -= dY;
		}
	}

	/**
	 * Obtains the difference between two integers.
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	public static int difference(final int one, final int two) {
		if (one < two) return two - one;
		return one - two;
	}

	/**
	 * Will return a number between the given startint and the maxint
	 * @param lower
	 * @param upper
	 * @return
	 */
	// Will return a number between lower and upper, inclusive
	public static int getRandomInt(final int lower, final int upper) {
		return
			lower + (Math.abs(RANDOM.nextInt() >>> 1) % ((upper - lower) + 1))
		;
	}

	/**
	 * @param fps
	 * @param ms
	 * @return
	 */
	public static int msToTicks(final int fps, final long ms) {
		return (int)((ms / fps) / 10);
	}
	
	/**
	 * @param element 
	 * @param array 
	 * @return 
	 */
	public static int findIndexOfObjElementInArray(
		final Object element, final Object[] array
	) {
		if (element == null || array == null)
			throw Util.NULL_ARGUMENT_EXCEPTION;
		
		for (int i = 0; i < array.length; i++)
			if (array[i] == element) return i;
		
		return -1;
	}
	
	/**
	 * @param element 
	 * @param array 
	 * @return 
	 */
	public static int findIndexOfIntElementInArray(
		final int element, final int[] array
	) {
		if (array == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		for (int i = 0; i < array.length; i++)
			if (array[i] == element) return i;
		
		return -1;
	}
}
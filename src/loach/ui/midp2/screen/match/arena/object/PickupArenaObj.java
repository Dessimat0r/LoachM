/**
 * 
 */
package loach.ui.midp2.screen.match.arena.object;

import loach.model.field.object.Pickup;
import loach.ui.midp2.screen.match.arena.Arena;
import loach.ui.midp2.viewinfo.ItemViewInfo;

/**
 * LoachM - PickupArenaObj
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class PickupArenaObj extends ArenaObject {
	/**
	 * @param arena
	 * @param pickup
	 */
	public PickupArenaObj(final Arena arena, final Pickup pickup) {
		super(
			arena, pickup,
			ItemViewInfo.obtainItemViewInfo(pickup.getItem()).getItemImage()
		);
	}
}
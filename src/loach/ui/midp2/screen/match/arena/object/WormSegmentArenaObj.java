/**
 * 
 */
package loach.ui.midp2.screen.match.arena.object;

import loach.model.field.object.WormSegment;
import loach.ui.midp2.screen.match.arena.Arena;
import loach.ui.midp2.viewinfo.WormViewInfo;

/**
 * LoachM - WormSegmentArenaObj
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class WormSegmentArenaObj extends ArenaObject {
	/**
	 * @param arena
	 * @param wormSegment
	 */
	public WormSegmentArenaObj(
		final Arena arena, final WormSegment wormSegment
	) {
		super(
			arena,
			wormSegment,
			WormViewInfo.obtainWormViewInfo(
				wormSegment.getWormInfo()
			).getSegmentImage()
		);
	}
}
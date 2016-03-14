/**
 * 
 */
package loach.ui.midp2.screen.match.arena.object;

import loach.model.field.object.Block;
import loach.ui.midp2.screen.match.arena.Arena;
import loach.ui.midp2.viewinfo.BlockViewInfo;

/**
 * LoachM - BlockArenaObj
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class BlockArenaObj extends ArenaObject {
	/**
	 * @param arena
	 * @param block
	 */
	public BlockArenaObj(final Arena arena, final Block block) {
		super(
			arena, block,
			BlockViewInfo.obtainBlockViewInfo(
				block.getBlockInfo()
			).getBlockImage()
		);
	}
}
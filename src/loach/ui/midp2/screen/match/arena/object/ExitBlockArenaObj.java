/**
 * 
 */
package loach.ui.midp2.screen.match.arena.object;

import loach.model.field.object.ExitBlock;
import loach.ui.midp2.MIDP2LoachUI;
import loach.ui.midp2.screen.match.arena.Arena;

/**
 * LoachM - ExitBlockArenaObj
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class ExitBlockArenaObj extends ArenaObject {
	/**
	 * @param arena
	 * @param exitBlock
	 */
	public ExitBlockArenaObj(final Arena arena, final ExitBlock exitBlock) {
		super(arena, exitBlock, MIDP2LoachUI.EXIT_BLOCK_IMAGE);
	}
}

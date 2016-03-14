/**
 * 
 */
package loach.model.field.object;

import loach.model.field.Field;
import loach.model.info.BlockInfo;

/**
 * LoachM - Block
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class Block extends FieldObject {	
	private final BlockInfo blockInfo;
	
	/**
	 * @param field
	 * @param blockInfo 
	 * @param x
	 * @param y
	 */
	public Block(
		final Field field, final BlockInfo blockInfo,
		final int x, final int y
	) {
		super(
			field,
			"Regular Block (" + blockInfo.getName() + ")",
			FO_ID_BLOCK,
			x,
			y,
			blockInfo.getFieldWidth(),
			blockInfo.getFieldHeight()
		);			
		this.blockInfo = blockInfo;
	}
	
	/**
	 * @return The block info that defines this block.
	 */
	public BlockInfo getBlockInfo() {
		return blockInfo;
	}
}
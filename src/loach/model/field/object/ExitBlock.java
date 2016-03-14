/**
 * 
 */
package loach.model.field.object;

import loach.model.field.Field;

/**
 * LoachM - ExitBlock
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class ExitBlock extends FieldObject {
	/**
	 * @param field
	 * @param x
	 * @param y
	 */
	public ExitBlock(final Field field, final int x, final int y) {
		super(
			field,
			"Exit block",
			FO_ID_EXIT_BLOCK,
			x,
			y,
			Field.EXIT_BLOCK_WIDTH,
			Field.EXIT_BLOCK_HEIGHT
		);
	}
}
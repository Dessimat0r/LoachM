/**
 * 
 */
package loach.model.info;

import loach.model.field.Field;

/**
 * LoachM - BlockInfo
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class BlockInfo {
	public static final BlockInfo NORMAL_BLOCK_INFO =
		new BlockInfo(
			"Normal block",
			Field.FLOOR_TILE_WIDTH,
			Field.FLOOR_TILE_HEIGHT,
			Field.AREA_SEARCH_STRATEGY_TILE_TOP_LEFT
		)
	;
	
	public static final BlockInfo SMALL_BLOCK_INFO =
		new BlockInfo(
			"Small block",
			Field.FLOOR_TILE_WIDTH / 2,
			Field.FLOOR_TILE_HEIGHT / 2,
			Field.AREA_SEARCH_STRATEGY_TILE_CENTERED
		)
	;
	
	public static final BlockInfo[] BLOCK_INFOS = new BlockInfo[] {
		NORMAL_BLOCK_INFO, SMALL_BLOCK_INFO
	};
	
	private final String name;
	private final int fieldWidth, fieldHeight, spawnPosStrategy;
	
	/**
	 * @param name 
	 * @param fieldWidth 
	 * @param fieldHeight
	 * @param spawnPosStrategy 
	 */
	public BlockInfo(
		final String name, final int fieldWidth, final int fieldHeight,
		final int spawnPosStrategy
	) {
		this.name = name;
		this.fieldWidth = fieldWidth;
		this.fieldHeight = fieldHeight;
		this.spawnPosStrategy = spawnPosStrategy;
	}
	
	/**
	 * @return the fieldWidth
	 */
	public int getFieldWidth() {
		return fieldWidth;
	}
	
	/**
	 * @return the fieldHeight
	 */
	public int getFieldHeight() {
		return fieldHeight;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the spawnPosStrategy
	 */
	public int getSpawnPosStrategy() {
		return spawnPosStrategy;
	}
}
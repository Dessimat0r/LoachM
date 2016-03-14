/**
 * 
 */
package loach.model.info.item;

/**
 * LoachM - MatchItem
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class LoachItem {
	public static final int LOACH_ITEM_ID_GROWTH_PILL = 0;
	public static final int LOACH_ITEM_ID_SPEEDUP_PILL = 1;
	public static final int LOACH_ITEM_ID_SLOWDOWN_PILL = 2;
	public static final int LOACH_ITEM_ID_MONEY_100 = 3;
	public static final int LOACH_ITEM_ID_MONEY_200 = 4;
	public static final int LOACH_ITEM_ID_MONEY_300 = 5;
	public static final int LOACH_ITEM_ID_MONEY_400 = 6;
	public static final int LOACH_ITEM_ID_KEY = 7;
	
	public static final LoachItem GROWTH_PILL_ITEM = new LoachItem(
		LOACH_ITEM_ID_GROWTH_PILL, "Growth Pill", 1, 1, 20, 300
	);
	public static final LoachItem SPEEDUP_PILL_ITEM = new LoachItem(
		LOACH_ITEM_ID_SPEEDUP_PILL, "Speedup Pill", 1, 1, 20, 500
	);
	public static final LoachItem SLOWDOWN_PILL_ITEM = new LoachItem(
		LOACH_ITEM_ID_SLOWDOWN_PILL, "Slowdown Pill", 1, 1, -20, 600
	);
	public static final LoachItem MONEY_100_ITEM = new LoachItem(
		LOACH_ITEM_ID_MONEY_100, "Money (100)", 1, 1, 30, 50
	);
	public static final LoachItem MONEY_200_ITEM = new LoachItem(
		LOACH_ITEM_ID_MONEY_200, "Money (200)", 1, 1, 40, 50
	);
	public static final LoachItem MONEY_300_ITEM = new LoachItem(
		LOACH_ITEM_ID_MONEY_300, "Money (300)", 1, 1, 50, 50
	);
	public static final LoachItem MONEY_400_ITEM = new LoachItem(
		LOACH_ITEM_ID_MONEY_400, "Money (400)", 1, 1, 60, 50
	);
	public static final LoachItem KEY_ITEM = new LoachItem(
		LOACH_ITEM_ID_KEY, "Key", 2, 2, 50, 0
	);
	
	public static final LoachItem[] ITEMS = new LoachItem[] {
		GROWTH_PILL_ITEM, SPEEDUP_PILL_ITEM, SLOWDOWN_PILL_ITEM,
		MONEY_100_ITEM, MONEY_200_ITEM, MONEY_300_ITEM, MONEY_400_ITEM,
		KEY_ITEM
	};
	
	public static final LoachItem[] NORMAL_ITEMS = new LoachItem[] {
		GROWTH_PILL_ITEM, SPEEDUP_PILL_ITEM, SLOWDOWN_PILL_ITEM,
		MONEY_100_ITEM, MONEY_200_ITEM, MONEY_300_ITEM, MONEY_400_ITEM,
	};
	
	public static final LoachItem[] SPECIAL_ITEMS = new LoachItem[] {
		KEY_ITEM
	};	
	
	public static final LoachItem[] MONEY_ITEMS = new LoachItem[] {
		MONEY_100_ITEM, MONEY_200_ITEM, MONEY_300_ITEM, MONEY_400_ITEM
	};
	
	/**
	 * @param loachItemID
	 * @return
	 */
	public static LoachItem obtainLoachItemForLoachItemID(final int loachItemID)
	{
		return ITEMS[loachItemID]; // using id as hashing function!
	}
	
	private final String name;
	private final int
		loachItemID,
		desirability, spawnProbability,
		fieldWidth, fieldHeight
	;
	
	/**
	 * @param loachItemID 
	 * @param name 
	 * @param fieldWidth
	 * @param fieldHeight
	 * @param desirability 
	 * @param spawnProbability 
	 */
	public LoachItem(
		final int loachItemID,
		final String name, final int fieldWidth, final int fieldHeight,
		final int desirability, final int spawnProbability
	) {
		this.loachItemID = loachItemID;
		this.name = name;
		this.fieldWidth = fieldWidth;
		this.fieldHeight = fieldHeight;
		this.desirability = desirability;
		this.spawnProbability = spawnProbability;
	}
	
	/**
	 * @return the loachItemID
	 */
	public int getLoachItemID() {
		return loachItemID;
	}
	
	/**
	 * Gets the name of this item.
	 * @return Returns the name of this item.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The desirability of this item, in relation to other items.
	 * 
	 * @return The desirability value.
	 */
	public int getDesirability() {
		return desirability;
	}
	
	/**
	 * Obtains the spawn probability for this item. Should be thought of
	 * as a percentage out of 10000%, where if an item has a 10000% spawn
	 * probability, it will be spawned every tick.
	 * 
	 * @return
	 */
	public int getSpawnProbability() {
		return spawnProbability;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getClass().getName() + "[" + name + "]";
	}
}
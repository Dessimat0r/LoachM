/**
 * 
 */
package loach.ui.midp2.viewinfo;

import java.util.Hashtable;

import javax.microedition.lcdui.Image;

import loach.model.info.item.LoachItem;
import loach.ui.midp2.MIDP2LoachUI;
import util.Util;

/**
 * LoachM - ItemViewInfo
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class ItemViewInfo {
	public static final ItemViewInfo MONEY_100_ITEM_VIEW_INFO =
	new ItemViewInfo(LoachItem.MONEY_100_ITEM, MIDP2LoachUI.MONEY_IMAGE);
	
	public static final ItemViewInfo MONEY_200_ITEM_VIEW_INFO =
	new ItemViewInfo(LoachItem.MONEY_200_ITEM, MIDP2LoachUI.MONEY_IMAGE);
	
	public static final ItemViewInfo MONEY_300_ITEM_VIEW_INFO =
	new ItemViewInfo(LoachItem.MONEY_300_ITEM, MIDP2LoachUI.MONEY_IMAGE);
	
	public static final ItemViewInfo MONEY_400_ITEM_VIEW_INFO =
	new ItemViewInfo(LoachItem.MONEY_400_ITEM, MIDP2LoachUI.MONEY_IMAGE);
	
	public static final ItemViewInfo GROWTH_PILL_ITEM_VIEW_INFO =
	new ItemViewInfo(
		LoachItem.GROWTH_PILL_ITEM,
		MIDP2LoachUI.GROWTH_PILL_IMAGE
	);
	
	public static final ItemViewInfo SPEEDUP_PILL_ITEM_VIEW_INFO =
		new ItemViewInfo(
			LoachItem.SPEEDUP_PILL_ITEM,
			MIDP2LoachUI.SPEEDUP_PILL_IMAGE
		)
	;
	
	public static final ItemViewInfo SLOWDOWN_PILL_ITEM_VIEW_INFO =
		new ItemViewInfo(
			LoachItem.SLOWDOWN_PILL_ITEM,
			MIDP2LoachUI.SLOWDOWN_PILL_IMAGE
		)
	;

	public static final ItemViewInfo KEY_ITEM_VIEW_INFO = new ItemViewInfo(
		LoachItem.KEY_ITEM, MIDP2LoachUI.KEY_IMAGE
	);	
	
	public static final ItemViewInfo[] ITEM_VIEW_INFOS = {
		GROWTH_PILL_ITEM_VIEW_INFO, SPEEDUP_PILL_ITEM_VIEW_INFO,
		SLOWDOWN_PILL_ITEM_VIEW_INFO, MONEY_100_ITEM_VIEW_INFO,
		MONEY_200_ITEM_VIEW_INFO, MONEY_300_ITEM_VIEW_INFO,
		MONEY_400_ITEM_VIEW_INFO, KEY_ITEM_VIEW_INFO
	};
	
	private static final Hashtable
		LOACH_ITEM_TO_ITEM_VIEW_INFO_MAP = new Hashtable(
			ITEM_VIEW_INFOS.length
		)
	;
	
	static {
		for (int i = 0; i < ITEM_VIEW_INFOS.length; i++)
			LOACH_ITEM_TO_ITEM_VIEW_INFO_MAP.put(
				ITEM_VIEW_INFOS[i].getItem(), ITEM_VIEW_INFOS[i]
			);
	}
	
	private final LoachItem item;
	private final Image itemImage;
	
	/**
	 * @param item
	 * @return Returns the corresponding item view info.
	 */
	public static ItemViewInfo obtainItemViewInfo(final LoachItem item) {
		if (item == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		final ItemViewInfo itemViewInfo =
			(ItemViewInfo)LOACH_ITEM_TO_ITEM_VIEW_INFO_MAP.get(item)
		;
		
		if (itemViewInfo != null) return itemViewInfo;
		return null;
	}
	
	/**
	 * @param item 
	 * @param itemImage 
	 */
	public ItemViewInfo(final LoachItem item, final Image itemImage) {
		this.item = item;
		this.itemImage = itemImage;
	}
	
	/**
	 * @return the itemImage
	 */
	public Image getItemImage() {
		return itemImage;
	}
	
	/**
	 * @return the item
	 */
	public LoachItem getItem() {
		return item;
	}
}
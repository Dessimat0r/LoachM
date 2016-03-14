/**
 * 
 */
package loach.ui.midp2.viewinfo;

import java.util.Hashtable;

import javax.microedition.lcdui.Image;

import loach.model.info.BlockInfo;
import loach.ui.midp2.MIDP2LoachUI;
import util.Util;

/**
 * LoachM - BlockViewInfo
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class BlockViewInfo {
	public static final BlockViewInfo SMALL_BLOCK_VIEW_INFO =
		new BlockViewInfo(
			BlockInfo.SMALL_BLOCK_INFO, MIDP2LoachUI.SMALL_BLOCK_IMAGE
		)
	;
	
	public static final BlockViewInfo NORMAL_BLOCK_VIEW_INFO =
		new BlockViewInfo(
			BlockInfo.NORMAL_BLOCK_INFO,
			MIDP2LoachUI.NORMAL_BLOCK_IMAGE
		)
	;
	
	public static final BlockViewInfo[] BLOCK_VIEW_INFOS = {
		SMALL_BLOCK_VIEW_INFO, NORMAL_BLOCK_VIEW_INFO,
	};	
	
	private static final Hashtable
		BLOCK_INFO_TO_BLOCK_VIEW_INFO_MAP = new Hashtable(
			BLOCK_VIEW_INFOS.length
		)
	;
	
	static {
		for (int i = 0; i < BLOCK_VIEW_INFOS.length; i++)
			BLOCK_INFO_TO_BLOCK_VIEW_INFO_MAP.put(
				BLOCK_VIEW_INFOS[i].getBlockInfo(), BLOCK_VIEW_INFOS[i]
			);
	}
	
	/**
	 * @param blockInfo
	 * @return Returns the corresponding item view info.
	 */
	public static BlockViewInfo obtainBlockViewInfo(
		final BlockInfo blockInfo
	) {
		if (blockInfo == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		final BlockViewInfo blockViewInfo =
			(BlockViewInfo)BLOCK_INFO_TO_BLOCK_VIEW_INFO_MAP.get(blockInfo)
		;
		
		if (blockViewInfo != null) return blockViewInfo;
		return null;
	}
	
	private final BlockInfo blockInfo;
	private final Image blockImage;
	
	/**
	 * @param blockInfo 
	 * @param blockImage 
	 */
	public BlockViewInfo(final BlockInfo blockInfo, final Image blockImage) {
		this.blockInfo = blockInfo;
		this.blockImage = blockImage;
	}
	
	/**
	 * @return the blockImage
	 */
	public Image getBlockImage() {
		return blockImage;
	}
	
	/**
	 * @return the blockInfo
	 */
	public BlockInfo getBlockInfo() {
		return blockInfo;
	}
}
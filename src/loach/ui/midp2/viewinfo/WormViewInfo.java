/**
 * 
 */
package loach.ui.midp2.viewinfo;

import java.util.Hashtable;

import javax.microedition.lcdui.Image;

import loach.model.info.WormInfo;
import loach.ui.midp2.MIDP2LoachUI;
import util.Util;

/**
 * LoachM - WormViewInfo
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class WormViewInfo {
	public static final WormViewInfo GREEN_WORM_VIEW_INFO = new WormViewInfo(
		0,
		WormInfo.GREEN_WORM_INFO,
		MIDP2LoachUI.GREEN_WORM_SEGMENT_IMAGE
	);
	
	public static final WormViewInfo BLUE_WORM_VIEW_INFO = new WormViewInfo(
		1,
		WormInfo.BLUE_WORM_INFO,
		MIDP2LoachUI.BLUE_WORM_SEGMENT_IMAGE
	);
	
	public static final WormViewInfo BOGEY_WORM_VIEW_INFO = new WormViewInfo(
		2,
		WormInfo.BOGEY_WORM_INFO,
		MIDP2LoachUI.BOGEY_WORM_SEGMENT_IMAGE
	);
	
	public static final WormViewInfo RED_WORM_VIEW_INFO = new WormViewInfo(
		3,
		WormInfo.RED_WORM_INFO,
		MIDP2LoachUI.RED_WORM_SEGMENT_IMAGE
	);
	
	public static final WormViewInfo WHITE_WORM_VIEW_INFO = new WormViewInfo(
		4,
		WormInfo.WHITE_WORM_INFO,
		MIDP2LoachUI.WHITE_WORM_SEGMENT_IMAGE
	);
	
	public static final WormViewInfo GREY_WORM_VIEW_INFO = new WormViewInfo(
		5,
		WormInfo.GREY_WORM_INFO,
		MIDP2LoachUI.GREY_WORM_SEGMENT_IMAGE
	);
	
	public static final WormViewInfo BANANA_WORM_VIEW_INFO = new WormViewInfo(
		6,
		WormInfo.BANANA_WORM_INFO,
		MIDP2LoachUI.BANANA_WORM_SEGMENT_IMAGE
	);
	
	public static final WormViewInfo[] WORM_VIEW_INFOS = {
		GREEN_WORM_VIEW_INFO, BLUE_WORM_VIEW_INFO,
		BOGEY_WORM_VIEW_INFO, RED_WORM_VIEW_INFO,
		WHITE_WORM_VIEW_INFO, GREY_WORM_VIEW_INFO,
		BANANA_WORM_VIEW_INFO
	};
	
	private static final Hashtable WORM_INFO_TO_WORM_VIEW_INFO_MAP =
		new Hashtable(WORM_VIEW_INFOS.length)
	;
	
	static {
		for (int i = 0; i < WORM_VIEW_INFOS.length; i++)
			WORM_INFO_TO_WORM_VIEW_INFO_MAP.put(
				WORM_VIEW_INFOS[i].getWormInfo(), WORM_VIEW_INFOS[i]
			);
	}
	
	private final int id;	
	private final WormInfo wormInfo;
	private final Image segmentImage;
	
	/**
	 * @param wormInfo
	 * @return Returns the corresponding worm view info.
	 */
	public static WormViewInfo obtainWormViewInfo(
		final WormInfo wormInfo
	) {
		if (wormInfo == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		final WormViewInfo wormViewInfo =
			(WormViewInfo)WORM_INFO_TO_WORM_VIEW_INFO_MAP.get(wormInfo)
		;
		
		if (wormViewInfo != null) return wormViewInfo;
		return null;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static WormViewInfo findWormViewInfoWithID(final int id) {
		for (int i = 0; i < WORM_VIEW_INFOS.length; i++) {
			if (WORM_VIEW_INFOS[i].getID() == id) return WORM_VIEW_INFOS[i];
		}
		return null;
	}
	
	/**
	 * @param id 
	 * @param wormInfo 
	 * @param segmentImage 
	 */
	public WormViewInfo(
		final int id, final WormInfo wormInfo,
		final Image segmentImage
	) {
		this.id = id;
		this.wormInfo = wormInfo;
		this.segmentImage = segmentImage;
	}
	
	/**
	 * @return the segmentImage
	 */
	public Image getSegmentImage() {
		return segmentImage;
	}
	
	/**
	 * @return the wormInfo
	 */
	public WormInfo getWormInfo() {
		return wormInfo;
	}
	
	/**
	 * @return the id
	 */
	public int getID() {
		return id;
	}
}
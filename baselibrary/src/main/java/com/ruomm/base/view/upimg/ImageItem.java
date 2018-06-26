package com.ruomm.base.view.upimg;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 */
public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;

	@Override
	public String toString() {
		return "ImageItem [imageId=" + imageId + ", thumbnailPath=" + thumbnailPath + ", imagePath=" + imagePath + "]";
	}

}

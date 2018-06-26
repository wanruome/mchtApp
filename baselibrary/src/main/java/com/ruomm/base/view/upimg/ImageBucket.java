package com.ruomm.base.view.upimg;

import java.io.Serializable;
import java.util.List;

/**
 * 一个目录的相册对象
 * 
 * @author Administrator
 */
public class ImageBucket implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1102492930385592149L;
	public int count = 0;
	public String bucketName;
	public List<ImageItem> imageList;

	@Override
	public String toString() {
		return "ImageBucket [count=" + count + ", bucketName=" + bucketName + ", imageList=" + imageList + "]";
	}

}

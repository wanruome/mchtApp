/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月12日 下午1:14:41 
 */
package com.ruomm.base.view.upimg;

public class UpImgBean {
	public String imagePath;
	public String compressPath;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (imagePath == null ? 0 : imagePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UpImgBean other = (UpImgBean) obj;
		if (imagePath == null) {
			if (other.imagePath != null) {
				return false;
			}
		}
		else if (!imagePath.equals(other.imagePath)) {
			return false;
		}
		return true;
	}

}

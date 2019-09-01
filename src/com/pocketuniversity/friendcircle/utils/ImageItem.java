package com.pocketuniversity.friendcircle.utils;

import java.io.Serializable;

/**
 * �?个图片对�?
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;
}

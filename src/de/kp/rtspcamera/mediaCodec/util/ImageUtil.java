package de.kp.rtspcamera.mediaCodec.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * CameraActivity.java
 * <p/>
 * Created by x.q. on 2016/12/30.
 * <p/>
 * Copyright (c) 2016 x.q.
 */

public class ImageUtil {
	/**
	 * ��תBitmap
	 * @param b
	 * @param rotateDegree
	 * @return
	 */
	public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
		Matrix matrix = new Matrix();
		matrix.postRotate((float)rotateDegree);
		Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
		return rotaBitmap;
	}
}

package de.kp.rtspcamera.poker;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;


public class NativePoker {
	
	public static native void DecodePicture(byte[] indata,int img_w,int img_h,int customerPeopleNum);//ͼ��ʶ����,����YUV����
	
}

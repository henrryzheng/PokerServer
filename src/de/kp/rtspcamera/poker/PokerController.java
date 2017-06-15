package de.kp.rtspcamera.poker;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import de.kp.net.rtsp.RtspConstants;
import de.kp.rtspcamera.MediaConstants;
import de.kp.rtspcamera.mediaCodec.camera.PeerCamera;
import de.kp.rtspcamera.poker.algo.AlgoFactory;
import de.kp.rtspcamera.poker.data.PokerCustomerSetting;
import de.kp.rtspcamera.poker.utils.SoundUtils;

public class PokerController {
	private final static String TAG = "PokerController";
	
	private static HandlerThread handlerThread;

	private static Handler threadHandler;
	
	private static final int MESSAGE_DETECTION = 1;
	private static final String DETECTIONSTRING = "detection";
	
	private static boolean bturn = true;
	
	private static boolean isStart = false;
	
//	private static byte[] mPreviewCallbackData;
	//
	static int srcwidth= MediaConstants.PreviewWidth;
	static int srcheight= MediaConstants.PreviewHeight;//
	static int srclen=srcwidth*srcheight*3/2;//
	
	static byte[] srcBuff = null;
	static byte[] mysrc = null;
	
	private static boolean isAlgoStart = false;
	static int mNumOfPeople;
	static int[][] mPokerResults;
	
	
	public static void startPokerDetectionThread(){
		Log.d(TAG,"startPokerDetectionThread");
		mNumOfPeople = 0;
		mPokerResults = new int[54][54];
//		srcwidth =  PeerCamera.IMAGE_WIDTH;
//		srcheight=  PeerCamera.IMAGE_HEIGHT;//////
//		
//		srclen = srcwidth*srcheight*3/2;//////
		srcBuff = new byte[srclen];//
		mysrc = new byte[srclen];
		//
		Thread mDec_thread	= new Thread(Dec_thread);//通信线程
//		mDec_thread.setPriority(Thread.MAX_PRIORITY);
		mDec_thread.start();//启动识别线程
		
		Thread mAlgo_thread = new Thread(Algo_thread);
		mAlgo_thread.start();
		
//		handlerThread = new HandlerThread(DETECTIONSTRING);
////		handlerThread.setPriority(Thread.MAX_PRIORITY);
//		handlerThread.start();
//		threadHandler = new Handler(handlerThread.getLooper()) {
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				Log.d(TAG, "handleMessage msg.what = " + msg.what);
//				switch (msg.what) {
//					case MESSAGE_DETECTION:
////						synchronized(mPreviewCallbackData){
//							if(bturn)
//							{
//								for(int i=0;i<srcheight;i++)
//								{
//									System.arraycopy(mPreviewCallbackData, (srcheight-1-i)*srcwidth, srcBuff, i*srcwidth, srcwidth);
//								}
//								int h = srcheight>>1;
//								int w = srcwidth>>1;
//								int w1 = srcwidth;
//								
//								int spos = srcwidth*srcheight;
//								for(int i=0;i<h;i++)//
//								{
//									System.arraycopy(mPreviewCallbackData, (h-1-i)*w+srcheight*srcwidth, srcBuff, (i*w)+srcheight*srcwidth, w);
//									System.arraycopy(mPreviewCallbackData, (h-1-i)*w+srcheight*srcwidth+w*h, srcBuff, (i*w)+srcheight*srcwidth+w*h, w);
//								}	
//							}else 
//								System.arraycopy(mPreviewCallbackData, 0, srcBuff, 0, srclen);
////						}
////						byte[] mysrc = new byte[srclen];//
//						Log.d(TAG,"run isStart = "+isStart);
//						if(isStart){
////							synchronized(mPreviewCallbackData){
////								System.arraycopy(srcBuff, 0, mysrc, 0, srclen);///
//								
//								try
//								{
//									NativePoker.DecodePicture(srcBuff,srcwidth,srcheight);
//									
//								}
//								catch(Exception e){
//								}
////							}
//						}
//						break;
//					default:
//						break;
//				}
//			}
//		};
	}
	
	//识别函数调用线程
		static Runnable Dec_thread = new Runnable() {  //
//			@Override 
			public void run() ////
			{
//				byte[] mysrc = new byte[srclen];//
//				Log.d(TAG,"run isStart = "+isStart);

				while(true)
				{
					if(isStart){
						
//						byte[] mysrc = new byte[srclen];//
						
						synchronized(srcBuff){
//							System.arraycopy(srcBuff, 0, mysrc, 0, srclen);///
							try
							{
								Log.d(TAG,"doNativeAlgo Start");
								NativePoker.DecodePicture(srcBuff,srcwidth,srcheight,PokerCustomerSetting.peopleNums);
								Log.d(TAG,"doNativeAlgo end");
							}
							catch(Exception e){
							}
						}
					}
					
					
				}
			} 
		};	
		
		static Runnable Algo_thread = new Runnable() {  //
//			@Override 
			public void run() ////
			{

				while(true)
				{					
					if(isAlgoStart){
						AlgoFactory.getInstance().createAlgo().doAlgo(mNumOfPeople, mPokerResults);
						isAlgoStart = false;
					}
				}
			} 
		};	
		
	private static byte[] YV12toYUV420(byte[] input, byte[] output, int width, int height) {
        /* 
         * COLOR_FormatYUV420Planar is I420 which is like YV12, but with U and V reversed.
         * So we just have to reverse U and V.
         */
        final int frameSize = width * height;
        final int qFrameSize = frameSize/4;

        System.arraycopy(input, 0, output, 0, frameSize); // Y
        for (int i = 0; i < qFrameSize; i++) {
            output[frameSize + i*2+1] = input[frameSize + i + qFrameSize]; // Cb (U)
            output[frameSize + i*2] = input[frameSize + i]; // Cr (V)
        }
        return output;
	}
	
	public static void doPreviewCallbackDetection(byte[] previewCallbackData){	
		Log.d(TAG,"doPreviewCallbackDetection ");
		isStart = true;
		
//		synchronized(previewCallbackData){
			if(bturn)
			{
				for(int i=0;i<srcheight;i++)
				{
					System.arraycopy(previewCallbackData, (srcheight-1-i)*srcwidth, srcBuff, i*srcwidth, srcwidth);
				}
				int h = srcheight>>1;
				int w = srcwidth>>1;
				int w1 = srcwidth;
				
				int spos = srcwidth*srcheight;
				for(int i=0;i<h;i++)//
				{
					System.arraycopy(previewCallbackData, (h-1-i)*w+srcheight*srcwidth, srcBuff, (i*w)+srcheight*srcwidth, w);
					System.arraycopy(previewCallbackData, (h-1-i)*w+srcheight*srcwidth+w*h, srcBuff, (i*w)+srcheight*srcwidth+w*h, w);
				}	
			}else 
				System.arraycopy(previewCallbackData, 0, srcBuff, 0, srclen);
//		}
//		Message msg = threadHandler.obtainMessage();
//		msg.what = MESSAGE_DETECTION;
//		msg.sendToTarget();
		
		
		
	}
	//
	//Jni返回结果
	public static void onCallbackPlayDanPai(int newestCard){
		AlgoFactory.getInstance().createAlgo().playDanPai(newestCard);
	}
	//
	public static void onCallbackDoAlgo(int numOfPeople, int[][] pokerResults){
		mNumOfPeople = numOfPeople;
		mPokerResults = pokerResults;
		isAlgoStart = true;
	}
	
}

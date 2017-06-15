package  de.kp.rtspcamera.mediaCodec.camera;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;
import de.kp.net.rtsp.RtspConstants;
import de.kp.rtspcamera.MyApplication;
import de.kp.rtspcamera.mediaCodec.encode.VideoEncoder;
import de.kp.rtspcamera.mediaCodec.socket.RtspRecorder;
import de.kp.rtspcamera.poker.NativePoker;
import de.kp.rtspcamera.poker.PokerController;
import de.kp.rtspcamera.poker.algo.AlgoFactory;
import de.kp.rtspcamera.poker.utils.SettingUtils;

/**
 * CameraActivity.java
 * <p/>
 * Created by x.q. on 2016/12/30.
 * <p/>
 * Copyright (c) 2016 x.q.
 */

public class PeerCamera {
	private static final String TAG = "peer/Camera";
	private Camera mCamera;
	private Camera.Parameters mCameraParamters;
	private boolean isPreviewing = false;

	private CameraPreviewCallback mCameraPreviewCallback;
	public static final int IMAGE_HEIGHT = Integer.valueOf(RtspConstants.HEIGHT);
	public static final int IMAGE_WIDTH = Integer.valueOf(RtspConstants.WIDTH);
	private static PeerCamera mPeerCamera;
    private static Boolean mIsShouldSendVideo = false;
    private int dropFrame = 0;
    private YuvImage mImage;
    private String mFileName;
	
//	private byte[] mImageCallbackBuffer = new byte[PeerCamera.IMAGE_WIDTH
//			* PeerCamera.IMAGE_HEIGHT * 3 / 2];
	private byte[] mEncoderH264Buf;
//	private byte[] realEncodeData;
	public interface CamOpenOverCallback{
		public void cameraHasOpened();
	}

	private PeerCamera(){
		mEncoderH264Buf = new byte[460800];
//		realEncodeData = new byte[10240];
	}
	public static synchronized PeerCamera getInstance(){
		if(mPeerCamera == null){
			mPeerCamera = new PeerCamera();
		}
		return mPeerCamera;
	}
	/**��Camera
	 * @param callback
	 */
	public int doOpenCamera(CamOpenOverCallback callback){
		Log.i(TAG, "Camera open....");
		mCamera = null;
		try {
			mCamera = Camera.open();
			Log.i(TAG, "Camera open over....");

		}catch (Exception e){
			Log.e(TAG, " do open camera failed : " + e);
			Toast.makeText(MyApplication.getAppContext(), "摄像头被占用，关闭摄像头重新打开应用!!!",
					Toast.LENGTH_LONG).show();
			return -1;
		}

		callback.cameraHasOpened();
		return 0;
	}

    public synchronized void setSendVideoFlag (boolean flag) {

        Log.d(TAG,"setSendVideoFlag "+ flag);
        this.mIsShouldSendVideo = flag;
    }

	/**ʹ��Surfaceview����Ԥ��
	 * @param holder
	 * @param previewRate
	 */
	public void doStartPreview(SurfaceHolder holder, float previewRate){
		Log.i(TAG, "doStartPreview...");
		if(isPreviewing){
			mCamera.stopPreview();
			return;
		}
		if(mCamera != null){
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initCamera(previewRate);
		}


	}
	/**ʹ��TextureViewԤ��Camera
	 * @param surface
	 * @param previewRate
	 */
	public void doStartPreview(SurfaceTexture surface, float previewRate){
		Log.i(TAG, "doStartPreview...");
		if(isPreviewing){
			mCamera.stopPreview();
			return;
		}
		if(mCamera != null){
			try {
				mCamera.setPreviewTexture(surface);
				Log.i(TAG, "doStartPreview finish");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initCamera(previewRate);
		}
		
	}
	/**
	 * ֹͣԤ�����ͷ�Camera
	 */
	public void doStopCamera(){
		if(null != mCamera)
		{
			Log.d(TAG, "doStopCamera");
			mIsShouldSendVideo = false;
			mCameraPreviewCallback.close();
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			isPreviewing = false;
			mCamera.release();
			mCamera = null;     
		}
	}

	private void initCamera(float previewRate){
		if(mCamera != null){

			this.mCameraParamters = this.mCamera.getParameters();
			this.mCameraParamters.setPreviewFormat(ImageFormat.NV21);
//            this.mCameraParamters.setPreviewFpsRange(4000,60000);
            this.mCameraParamters.setFlashMode("off");
//            this.mCameraParamters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
//            this.mCameraParamters.setSceneMode(Camera.Parameters.SCENE_MODE_NIGHT);
//            this.mCameraParamters.setAntibanding(Camera.Parameters.ANTIBANDING_50HZ);
            Log.d(TAG, "initCamera setEV = "+SettingUtils.getEV(MyApplication.getAppContext()));
            this.mCameraParamters.setExposureCompensation(SettingUtils.getEV(MyApplication.getAppContext()));
            this.mCameraParamters.setPreviewSize(IMAGE_WIDTH, IMAGE_HEIGHT);
//            for(int i = 0; i<this.mCameraParamters.getSupportedPictureSizes().size();i++){
//            	Log.d(TAG," getSupportPreviewSize : width = "+ this.mCameraParamters.getSupportedPictureSizes().get(i).width 
//            			+ " height = "+this.mCameraParamters.getSupportedPictureSizes().get(i).height);
//            }
//            
			this.mCamera.setDisplayOrientation(90);
			try {
				mCameraPreviewCallback = new CameraPreviewCallback();
//				mCamera.addCallbackBuffer(mImageCallbackBuffer);
				mCamera.setPreviewCallback(mCameraPreviewCallback);
			} catch (Exception e){
				Log.e(TAG," setCameraPreViewCallback error : "+e);
				Toast.makeText(MyApplication.getAppContext(), "生产视频错误" + e + "!!!",
						Toast.LENGTH_LONG).show();
			}
//			List<String> focusModes = this.mCameraParamters.getSupportedFocusModes();
//			if (focusModes.contains("continuous-video")) {
//				this.mCameraParamters
//						.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//			}
//			else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//				this.mCameraParamters
//				.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//			}
			this.mCamera.setParameters(this.mCameraParamters);
			this.mCamera.startPreview();

			this.isPreviewing = true;
		}
	}
	
	public void setEV(int ev){
		if(mCamera != null && mCameraParamters != null){
			Log.d(TAG, "setEV = "+ev);
			this.mCameraParamters.setExposureCompensation(ev);
			this.mCamera.setParameters(this.mCameraParamters);
		}
	}

	class CameraPreviewCallback implements Camera.PreviewCallback {
		private static final String TAG = "CameraPreviewCallback";
//		private VideoEncoderFromBuffer videoEncoder = null;
		private VideoEncoder videoEncoder = null;

		private CameraPreviewCallback() throws IOException {
//			videoEncoder = new VideoEncoderFromBuffer(PeerCamera.IMAGE_WIDTH,
//					PeerCamera.IMAGE_HEIGHT);
			videoEncoder = VideoEncoder.getInstance();
			
		}

		void close() {
			videoEncoder.close();
		}

		@Override
		public synchronized void onPreviewFrame(byte[] data, Camera camera) {
//			Log.i(TAG, "onPreviewFrame");
			long startTime = System.currentTimeMillis();
//			dropFrame++;
//			if(dropFrame==3){
				PokerController.doPreviewCallbackDetection(data);
//				dropFrame = 0;
//			}
			
	//		PokerController.doPreviewCallbackDetection(data); //should optimize
			if (mIsShouldSendVideo) {
				Log.d("xxx", "mIsShouldSendVideo is true");
				//videoEncoder.onFrame(data, 0, data.length, 0);
//				for(int i=0;i<mEncoderH264Buf.length;i++) //should optimize
//		        	mEncoderH264Buf[i]=0;                 //should optimize
				int encoderRet = videoEncoder.encodeData(data,mEncoderH264Buf);
				if (encoderRet > 0) {
//					Log.d("xxx"," sendData --- : encoderRet =  "+encoderRet +" length = "+mEncoderH264Buf.length);
					byte[] realEncodeData = new byte[encoderRet]; //should optimize
					System.arraycopy(mEncoderH264Buf, 0, realEncodeData,0,encoderRet); //should optimize
//					RtspRecorder.getInstance().sendData(mEncoderH264Buf);
					RtspRecorder.getInstance().sendData(realEncodeData);
				}
			}else {
				Log.d("xxx", "mIsShouldSendVideo is false");
			}
			long endTime = System.currentTimeMillis();
			Log.i(TAG, Integer.toString((int)(endTime-startTime)) + "ms");
			
//			camera.addCallbackBuffer(mImageCallbackBuffer);
			
//			//获取内部存储状态  
//			String state = Environment.getExternalStorageState();  
//			//如果状态不是mounted，无法读写  
//			if (!state.equals(Environment.MEDIA_MOUNTED)) {  
//			   return;  
//			}  
//			mFileName = dropFrame + ".jpg"; 
//			mImage = new YuvImage(data, ImageFormat.NV21, IMAGE_WIDTH, IMAGE_HEIGHT, null);            //ImageFormat.NV21  640 480  
//		    
//			new Thread(saveFileRunnable).start();
		}
	}
	
	private Runnable saveFileRunnable = new Runnable(){
        @Override
        public void run() {
            try {
                saveFile(mImage, mFileName);
                Log.d("xxx", "图片保存成功！");
            } catch (IOException e) {
            	Log.d("xxx", "图片保存失败！");
                e.printStackTrace();
            }
//            messageHandler.sendMessage(messageHandler.obtainMessage());
        }

    };
    
    /**
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public void saveFile(YuvImage imag, String fileName) throws IOException {
    	//保存一张照片  
    	File sdRoot = Environment.getExternalStorageDirectory();    //系统路径  
    	String dir = "/pokerPicture/";   //文件夹名  
    	File mkDir = new File(sdRoot, dir);       
    	if (!mkDir.exists())  
    	{  
    	   mkDir.mkdirs();   //目录不存在，则创建  
    	}  
    	  
    	  
    	File pictureFile = new File(sdRoot, dir + fileName);  
    	if (!pictureFile.exists()) {  
    	   try {  
    	       pictureFile.createNewFile();  
    	  
    	       FileOutputStream filecon = new FileOutputStream(pictureFile);  
    	  
   
    	       //图像压缩  
    	       mImage.compressToJpeg(  
    	               new Rect(0, 0, mImage.getWidth(), mImage.getHeight()),  
    	               70, filecon);   // 将NV21格式图片，以质量70压缩成Jpeg，并得到JPEG数据流  
    	  
    	   }catch (IOException e)  
    	   {  
    	       e.printStackTrace();  
    	   }  
    	}  
    }

	public void onFrame (byte[] buf, int offset, int length, int flag) {
//		Log.d("xxx"," onFrame --- : "+buf.length);
//		RtspRecorder.getInstance().sendData(buf);
        
	}

}

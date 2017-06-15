/*******************************************************************************
 * Software Name : RCS IMS Stack
 *
 * Copyright (C) 2010 France Telecom S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.kp.net.rtp.recorder;

import com.orangelabs.core.ims.protocol.rtp.MediaRegistry;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h263.H263Config;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h263.encoder.NativeH263Encoder;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h263.encoder.NativeH263EncoderParams;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h264.H264Config;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h264.encoder.NativeH264Encoder;
import com.orangelabs.core.ims.protocol.rtp.format.video.H263VideoFormat;
import com.orangelabs.core.ims.protocol.rtp.format.video.H264VideoFormat;
import com.orangelabs.core.ims.protocol.rtp.format.video.VideoFormat;
import com.orangelabs.core.ims.protocol.rtp.media.MediaException;
import com.orangelabs.core.ims.protocol.rtp.media.MediaInput;
import com.orangelabs.core.ims.protocol.rtp.media.MediaSample;
import com.orangelabs.service.api.client.media.IMediaEventListener;
import com.orangelabs.service.api.client.media.IMediaPlayer;
import com.orangelabs.service.api.client.media.MediaCodec;
import com.orangelabs.service.api.client.media.video.VideoCodec;
import com.orangelabs.utils.FifoBuffer;
import com.orangelabs.utils.logger.Logger;

import de.kp.rtspcamera.MediaConstants;
import de.kp.rtspcamera.poker.NativePoker;
import de.kp.rtspcamera.poker.PokerController;
import de.kp.rtspcamera.poker.algo.AlgoFactory;
import de.kp.rtspcamera.poker.data.PokerCustomerSetting;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

/**
 * Live RTP video player. Supports only H.263 and H264 QCIF formats.
 */
public class RtspVideoRecorder extends IMediaPlayer.Stub implements Camera.PreviewCallback,AlgoFactory.onResultListener {

    /**
     * List of supported video codecs
     */
    public static MediaCodec[] supportedMediaCodecs = {
            new VideoCodec(H264Config.CODEC_NAME, H264VideoFormat.PAYLOAD, H264Config.CLOCK_RATE, H264Config.CODEC_PARAMS,
                    H264Config.FRAME_RATE, H264Config.BIT_RATE, H264Config.VIDEO_WIDTH,
                    H264Config.VIDEO_HEIGHT).getMediaCodec(),
            new VideoCodec(H263Config.CODEC_NAME, H263VideoFormat.PAYLOAD, H263Config.CLOCK_RATE, H263Config.CODEC_PARAMS,
                    H263Config.FRAME_RATE, H263Config.BIT_RATE, H263Config.VIDEO_WIDTH,
                    H263Config.VIDEO_HEIGHT).getMediaCodec()
    };

    /**
     * Selected video codec
     */
    private VideoCodec selectedVideoCodec = null;

    /**
     * Video format
     */
    private VideoFormat videoFormat;

    /**
     * Local RTP port
     */
    private int localRtpPort;

    /**
     * RTP sender session
     */
    private MediaRtpSender rtpMediaSender = null;

    /**
     * RTP media input
     */
    private MediaRtpInput rtpInput = null;

    /**
     * Last video frame
     */
    private CameraBuffer frameBuffer = null;
    private Boolean mIsShouldEncoder = false;
    /**
     * Is player opened
     */
    private boolean opened = false;

    /**
     * Is player started
     */
    private boolean started = false;

    /**
     * Video start time
     */
    private long videoStartTime = 0L;
    
    private int srcwidth= MediaConstants.PreviewWidth;
    private int srcheight= MediaConstants.PreviewHeight;//
    private int srclen=srcwidth*srcheight*3/2;//
    
    private byte[] srcBuff = null;
    static byte[] mysrc = null;

    /**
     * Media event listeners
     */
    private Vector<IMediaEventListener> listeners = new Vector<IMediaEventListener>();

    /**
     * The logger
     */
    private Logger logger = Logger.getLogger(this.getClass().getName());

	private String TAG = "RtspVideoRecorder";
	public static RtspVideoRecorder instance;
	
	private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
        	
        	switch (msg.what) {
        	case 1:
        		resetEncoder();
        		break;
        	case 2:
        		Log.d("xxx", "handleMessage begin encoder");
        		mIsShouldEncoder = true;
        		break;
        	}
        	mIsShouldEncoder = true;
        }

    };
	
	public static RtspVideoRecorder getInstance() {
		
		
		if (instance == null) {
			instance = new RtspVideoRecorder("h263-2000");
		}
		
		return instance;
	}

    /**
     * Constructor
t     */
    private RtspVideoRecorder() {
    }

    /**
     * Constructor. Force a video codec.
     *
     * @param codec Video codec
     */
    private RtspVideoRecorder(VideoCodec codec) {
        // Set the media codec
        setMediaCodec(codec.getMediaCodec());
    }

    /**
     * Constructor. Force a video codec.
     *
     * @param codec Video codec name
     */
    private RtspVideoRecorder(String codec) {
        // Set the media codec
        for (int i = 0; i < supportedMediaCodecs.length ; i++) {
            if (codec.toLowerCase().contains(supportedMediaCodecs[i].getCodecName().toLowerCase())) {
                setMediaCodec(supportedMediaCodecs[i]);
                break;
            }
        }
    }

    /**
     * Returns the local RTP port
     *
     * @return Port
     */
    public int getLocalRtpPort() {
        return localRtpPort;
    }

    /**
     * Return the video start time
     *
     * @return Milliseconds
     */
    public long getVideoStartTime() {
        return videoStartTime;
    }

    /**
     * Is player opened
     *
     * @return Boolean
     */
    public boolean isOpened() {
        return opened;
    }

    /**
     * Is player started
     *
     * @return Boolean
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Open the player
     *
     * @param remoteHost Remote host
     * @param remotePort Remote port
     */
    public void open(String remoteHost, int remotePort) {
    	// This is an interface method, that is no longer
    	// used with the actual context
    }
    
    public void open() {

    	if (opened) {
            // Already opened
            return;
        }

        // Check video codec
        if (selectedVideoCodec == null) {
        	
            if (logger.isActivated()) {
                logger.debug("Player error: Video Codec not selected");
            }

            return;

        }

       

        // Init the RTP layer
        try {

        	rtpInput = new MediaRtpInput();
            rtpInput.open();
            
        	rtpMediaSender = new MediaRtpSender(videoFormat);            
            rtpMediaSender.prepareSession(rtpInput);
        
        } catch (Exception e) {
        	
            if (logger.isActivated()) {
                logger.debug("Player error: " + e.getMessage());
            }
        	
            return;
        }

        // Player is opened
        opened = true;

    }

    /**
     * Close the player
     */
    public void close() {
        if (!opened) {
            // Already closed
            return;
        }
        // Close the RTP layer
        rtpInput.close();
        rtpMediaSender.stopSession();

        try {
            // Close the video encoder
            if (selectedVideoCodec.getCodecName().equalsIgnoreCase(H264Config.CODEC_NAME)) {
                NativeH264Encoder.DeinitEncoder();

            } else if (selectedVideoCodec.getCodecName().equalsIgnoreCase(H263Config.CODEC_NAME)) {
                NativeH263Encoder.DeinitEncoder();
            }
        
        } catch (UnsatisfiedLinkError e) {
            if (logger.isActivated()) {
                logger.error("Can't close correctly the video encoder", e);
            }
        }

        // Player is closed
        opened = false;

    }

    /**
     * Start the player
     */
    public synchronized void start() {
		Log.d(TAG , "start");
   	
        if ((opened == false) || (started == true)) {
            return;
        }
        startResizeThread();
        started = true;

        // Start RTP layer
        rtpMediaSender.startSession();
        // Init video encoder
        try {
            if (selectedVideoCodec.getCodecName().equalsIgnoreCase(H264Config.CODEC_NAME)) {
                // H264
            	Log.d("init", " InitEncoder getWidth = "+selectedVideoCodec.getWidth() + " getHeight "+selectedVideoCodec.getFramerate());
                NativeH264Encoder.InitEncoder(selectedVideoCodec.getWidth(), selectedVideoCodec.getHeight(), selectedVideoCodec.getFramerate());

            } else if (selectedVideoCodec.getCodecName().equalsIgnoreCase(H263Config.CODEC_NAME)) {
                // Default H263
                NativeH263EncoderParams params = new NativeH263EncoderParams();
            
                params.setEncFrameRate(selectedVideoCodec.getFramerate());
                params.setBitRate(selectedVideoCodec.getBitrate());

                // set width/height parameters for native encoding, too
                params.setEncHeight(selectedVideoCodec.getHeight());
                params.setEncWidth(selectedVideoCodec.getWidth());
                
                params.setTickPerSrc(params.getTimeIncRes() / selectedVideoCodec.getFramerate());
                params.setIntraPeriod(-1);
                params.setNoFrameSkipped(false);
                
                int result = NativeH263Encoder.InitEncoder(params);
                
                if (result != 1) {
                	
                    if (logger.isActivated()) {
                        logger.debug("Player error: Encoder init failed with error code " + result);
                    }

                    return;

                }
            }
        
        } catch (UnsatisfiedLinkError e) {

        	if (logger.isActivated()) {
                logger.debug("Player error: " + e.getMessage());
            }

//            return;

        }
        // Start capture
        captureThread.start();
        
        // Player is started
        videoStartTime = SystemClock.uptimeMillis();

		AlgoFactory.getInstance().setListener(this);
    }
    
    public void startResizeThread(){
		Log.d(TAG,"startResizeThread");

		srcBuff = new byte[srclen];//
		mysrc = new byte[srclen];
		//
		
		resizeThread.start();
    }

    /**
     * Stop the player
     */
    public void stop() {
        
    	if ((opened == false) || (started == false)) { 
            return;
        }

        // Stop capture
        try {
        	
        	byte[] stopCommand = new byte[5];
        	stopCommand[0] = -2;
        	stopCommand[1] = -2;
        	stopCommand[2] = -2;
        	stopCommand[3] = -2;
        	stopCommand[4] = -2;
        	
        	rtpInput.addFrame(stopCommand, 100);
        	
            captureThread.interrupt();
            resizeThread.interrupt();

        } catch (Exception e) {
        }

        // Player is stopped
        videoStartTime = 0L;
        started = false;

    }

    /**
     * Add a media event listener
     *
     * @param listener Media event listener
     */
    public void addListener(IMediaEventListener listener) {
        listeners.addElement(listener);
    }

    /**
     * Remove all media event listeners
     */
    public void removeAllListeners() {
        listeners.removeAllElements();
    }

    /**
     * Get supported media codecs
     *
     * @return media Codecs list
     */
    public MediaCodec[] getSupportedMediaCodecs() {
        return supportedMediaCodecs;
    }

    /**
     * Get media codec
     *
     * @return Media Codec
     */
    public MediaCodec getMediaCodec() {
        if (selectedVideoCodec == null)
            return null;
        else
            return selectedVideoCodec.getMediaCodec();
    }

    /**
     * Set media codec
     *
     * @param mediaCodec Media codec
     */
    public void setMediaCodec(MediaCodec mediaCodec) {
       
    	if (VideoCodec.checkVideoCodec(supportedMediaCodecs, new VideoCodec(mediaCodec))) {
        
    		selectedVideoCodec = new VideoCodec(mediaCodec);
            videoFormat = (VideoFormat) MediaRegistry.generateFormat(mediaCodec.getCodecName());

            // Initialize frame buffer
            if (frameBuffer == null) {
                frameBuffer = new CameraBuffer();
            }

        } else {

            if (logger.isActivated()) {
                logger.debug("Player error: Codec not supported");
            }

        }
    }

    /**
     * Preview frame from the camera
     *
     * @param data Frame
     * @param camera Camera
     */
    public void onPreviewFrame(byte[] data, Camera camera) {
    	long startTime = System.currentTimeMillis();
    	
    	PokerController.doPreviewCallbackDetection(data);
    	if(started){
    		System.arraycopy(data, 0, srcBuff, 0, srclen);
    	}
    	
    	long endTime = System.currentTimeMillis();
    	
		Log.i(TAG, "onPreviewFrame resizeBuffer time = " + Integer.toString((int)(endTime-startTime)) + "ms");
        
        //
        
    }
    
    private byte[] resizeBuffer(byte[] data){
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, MediaConstants.PreviewWidth, MediaConstants.PreviewHeight, null);
    	yuvImage.compressToJpeg(new Rect(0, 0, MediaConstants.PreviewWidth, MediaConstants.PreviewHeight), 50, out);
    	byte[] imageBytes = out.toByteArray();
    	Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    	Bitmap resized = Bitmap.createScaledBitmap(image, selectedVideoCodec.getWidth(), selectedVideoCodec.getHeight(), true);
    	return getNV21(selectedVideoCodec.getWidth(),selectedVideoCodec.getHeight(),resized);
    }
    
    byte [] getNV21(int inputWidth, int inputHeight, Bitmap scaled) {

        int [] argb = new int[inputWidth * inputHeight];

        scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);

        byte [] yuv = new byte[inputWidth*inputHeight*3/2];
        encodeYUV420SP(yuv, argb, inputWidth, inputHeight);

        scaled.recycle();

        return yuv;
    }

    void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
        final int frameSize = width * height;

        int yIndex = 0;
        int uvIndex = frameSize;

        int a, R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
                R = (argb[index] & 0xff0000) >> 16;
                G = (argb[index] & 0xff00) >> 8;
                B = (argb[index] & 0xff) >> 0;

                // well known RGB to YUV algorithm
                Y = ( (  66 * R + 129 * G +  25 * B + 128) >> 8) +  16;
                U = ( ( -38 * R -  74 * G + 112 * B + 128) >> 8) + 128;
                V = ( ( 112 * R -  94 * G -  18 * B + 128) >> 8) + 128;

                // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
                //    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
                //    pixel AND every other scanline.
                yuv420sp[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
                if (j % 2 == 0 && index % 2 == 0) { 
                    yuv420sp[uvIndex++] = (byte)((V<0) ? 0 : ((V > 255) ? 255 : V));
                    yuv420sp[uvIndex++] = (byte)((U<0) ? 0 : ((U > 255) ? 255 : U));
                }

                index ++;
            }
        }
    }
    
    
    public synchronized void setSendVideoFlag (boolean flag) {

        Log.d(TAG,"setSendVideoFlag "+ flag);
        this.mIsShouldEncoder = !flag;
        videoStartTime = 0;
        mHandler.sendEmptyMessageDelayed(1, 800);
        mHandler.sendEmptyMessageDelayed(2, 1200);
    }
    
    void resetEncoder() {
      Log.d(TAG,"resetEncoder  ----");
      frameBuffer.clear();
      try {
          // Close the video encoder
          if (selectedVideoCodec.getCodecName().equalsIgnoreCase(H264Config.CODEC_NAME)) {
              NativeH264Encoder.DeinitEncoder();

          } else if (selectedVideoCodec.getCodecName().equalsIgnoreCase(H263Config.CODEC_NAME)) {
              NativeH263Encoder.DeinitEncoder();
          }
      
      } catch (UnsatisfiedLinkError e) {
          if (logger.isActivated()) {
              logger.error("Can't close correctly the video encoder", e);
          }
      }
//      videoStartTime = System.currentTimeMillis();
      try {
          if (selectedVideoCodec.getCodecName().equalsIgnoreCase(H264Config.CODEC_NAME)) {
              // H264
          	Log.d("init", " InitEncoder getWidth = "+selectedVideoCodec.getWidth() + " getHeight "+selectedVideoCodec.getFramerate());
              NativeH264Encoder.InitEncoder(selectedVideoCodec.getWidth(), selectedVideoCodec.getHeight(), selectedVideoCodec.getFramerate());

          } else if (selectedVideoCodec.getCodecName().equalsIgnoreCase(H263Config.CODEC_NAME)) {
              // Default H263
              NativeH263EncoderParams params = new NativeH263EncoderParams();
          
              params.setEncFrameRate(selectedVideoCodec.getFramerate());
              params.setBitRate(selectedVideoCodec.getBitrate());

              // set width/height parameters for native encoding, too
              params.setEncHeight(selectedVideoCodec.getHeight());
              params.setEncWidth(selectedVideoCodec.getWidth());
              
              params.setTickPerSrc(params.getTimeIncRes() / selectedVideoCodec.getFramerate());
              params.setIntraPeriod(-1);
              params.setNoFrameSkipped(false);
              
              int result = NativeH263Encoder.InitEncoder(params);
              
              if (result != 1) {
              	
                  if (logger.isActivated()) {
                      logger.debug("Player error: Encoder init failed with error code " + result);
                  }

                  return;

              }
          }
      
      } catch (UnsatisfiedLinkError e) {

      	if (logger.isActivated()) {
              logger.debug("Player error: " + e.getMessage());
          }

//          return;

      }
    }

    /**
     * Camera buffer
     */
    private class CameraBuffer {
        /**
         * YUV frame where frame size is always (videoWidth*videoHeight*3)/2
         */
        private byte frame[] = new byte[(selectedVideoCodec.getWidth()
                * selectedVideoCodec.getHeight() * 3) / 2];

        /**
         * Set the last captured frame
         *
         * @param frame Frame
         */
        public void setFrame(byte[] frame) {
            this.frame = frame;
        }

        /**
         * Return the last captured frame
         *
         * @return Frame
         */
        public byte[] getFrame() {
            return frame;
        }
        
        public void clear() {
        	this.frame = new byte[(selectedVideoCodec.getWidth()
                    * selectedVideoCodec.getHeight() * 3) / 2];
        }
    }

    /**
     * Video capture thread
     */
    private Thread captureThread = new Thread() {
        /**
         * Timestamp
         */
        private long timeStamp = 0;

        /**
         * Processing
         */
        public void run() {
//            if (rtpInput == null) {
//                return;
//            }

            int timeToSleep = 1000 / selectedVideoCodec.getFramerate();
            int timestampInc = 90000 / selectedVideoCodec.getFramerate();
            byte[] frameData;
            byte[] encodedFrame;
            long encoderTs = 0;
            long oldTs = System.currentTimeMillis();

            while (started ) {
                // Set timestamp
            	if (mIsShouldEncoder) {
	                long time = System.currentTimeMillis();
	                encoderTs = encoderTs + (time - oldTs);
	                videoStartTime += 100;	
	                // Get data to encode
	                frameData = frameBuffer.getFrame();
	                //Log.d(TAG, "time = "+time +" videoStartTime = "+videoStartTime+ " encoderTs = "+encoderTs + " frameData.length"+frameData.length);
	                // Encode frame
	                int encodeResult;
	                if (selectedVideoCodec.getCodecName().equalsIgnoreCase(H264Config.CODEC_NAME)) {
	                    encodedFrame = NativeH264Encoder.EncodeFrame(frameData, encoderTs);
	                    encodeResult = NativeH264Encoder.getLastEncodeStatus();
	                } else {
	                    encodedFrame = NativeH263Encoder.EncodeFrame(frameData, videoStartTime);
	                    encodeResult = 0;
	                }
	
	        		System.out.println("RtpVideoRecorder: captureThread: encodeResult == " + encodeResult);
	
	        		/*
	        		 * accept additional status 
	        		 * EAVCEI_MORE_NAL     --  there is more NAL to be retrieved
	        		 */
	                if ((encodeResult == 0 || encodeResult == 6) && encodedFrame.length > 0) {
	                	
	                	if (encodeResult == 6)
	                		System.out.println("RtpVideoRecorder: captureThread: Status == EAVCEI_MORE_NAL");
	                	
	                    // Send encoded frame
	                	System.out.println("encodedFrame: encodedFrame length" + encodedFrame.length);
	                    rtpInput.addFrame(encodedFrame, timeStamp += timestampInc);
	                }
	
	                // Sleep between frames if necessary
	                long delta = System.currentTimeMillis() - time;
	                if (delta < timeToSleep) {
	                    try {
	                        Thread.sleep((timeToSleep - delta) - (((timeToSleep - delta) * 10) / 100));
	                    } catch (InterruptedException e) {
	                    }
	                }
	
	                // Update old timestamp
	                oldTs = time;
	            }
            }
        }
    };
    
    
    /**
     * frame resize thread
     */
    private Thread resizeThread = new Thread() {

        /**
         * Processing
         */
        public void run() {

        	while(true)
			{
				if(started){
//					byte[] mysrc = new byte[srclen];//
					
					synchronized(srcBuff){
//						System.arraycopy(srcBuff, 0, mysrc, 0, srclen);///
						if (frameBuffer != null){
				        	long startTime = System.currentTimeMillis();
				        	frameBuffer.setFrame(resizeBuffer(srcBuff));
				        	long endTime = System.currentTimeMillis();
							Log.i(TAG, "resizeThread resizeBuffer time = " + Integer.toString((int)(endTime-startTime)) + "ms");
				        }
					}
				}
			}
        }
    };

    /**
     * Media RTP input
     */
    private static class MediaRtpInput implements MediaInput {
        /**
         * Received frames
         */
        private FifoBuffer fifo = null;

        /**
         * Constructor
         */
        public MediaRtpInput() {
        }

        /**
         * Add a new video frame
         *
         * @param data Data
         * @param timestamp Timestamp
         */
        public void addFrame(byte[] data, long timestamp) {
            if (fifo != null) {
                fifo.addObject(new MediaSample(data, timestamp));
            }
        }

        /**
         * Open the player
         */
        public void open() {
            fifo = new FifoBuffer();
        }

        /**
         * Close the player
         */
        public void close() {
            if (fifo != null) {
                fifo.close();
                fifo = null;
            }
        }

        /**
         * Read a media sample (blocking method)
         *
         * @return Media sample
         * @throws MediaException
         */
        public MediaSample readSample() throws MediaException {
            try {
                if (fifo != null) {
             //   	Log.d("xxx", "readSample : "+(MediaSample)fifo.getObject());
                    return (MediaSample)fifo.getObject();
                } else {
                    throw new MediaException("Media input not opened");
                }
            } catch (Exception e) {
                throw new MediaException("Can't read media sample");
            }
        }
    }

	@Override
	public void resultBack(byte[] result) {
		// TODO Auto-generated method stub
		
		if (rtpInput != null) {
			Log.d("xxx", " resultBack ---- result[0] = "+result[0]+ "result[1] = "+result[1]+ "result[2] = "+result[2]+ "result[3] = "+result[3]);
			rtpInput.addFrame(result, 1000);
		}
	}
}

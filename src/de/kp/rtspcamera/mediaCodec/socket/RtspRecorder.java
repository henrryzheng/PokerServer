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

package de.kp.rtspcamera.mediaCodec.socket;

import com.orangelabs.core.ims.protocol.rtp.MediaRegistry;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h263.H263Config;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h263.encoder.NativeH263Encoder;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h263.encoder.NativeH263EncoderParams;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h264.H264Config;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h264.encoder.NativeH264Encoder;
import com.orangelabs.core.ims.protocol.rtp.format.video.H263VideoFormat;
import com.orangelabs.core.ims.protocol.rtp.format.video.H264VideoFormat;
import com.orangelabs.core.ims.protocol.rtp.format.video.MediaCodecFormat;
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

import de.kp.net.rtp.recorder.MediaRtpSender;
import de.kp.rtspcamera.mediaCodec.camera.PeerCamera;
import de.kp.rtspcamera.poker.PokerController;
import de.kp.rtspcamera.poker.algo.AlgoFactory;
import android.hardware.Camera;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Live RTP video player. Supports only H.263 and H264 QCIF formats.
 */
public class RtspRecorder implements AlgoFactory.onResultListener{


    /**
     * Local RTP port
     */
    private int localRtpPort;

    private CameraBuffer frameBuffer = null;
//    private VideoEncoderFromBuffer videoEncoder = null;
    /**
     * RTP sender session
     */
    private MediaRtpSender rtpMediaSender = null;

    /**
     * RTP media input
     */
    private MediaRtpInput rtpInput = null;
    
    private static RtspRecorder mInstance;

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

    /**
     * Media event listeners
     */
    private Vector<IMediaEventListener> listeners = new Vector<IMediaEventListener>();

    /**
     * The logger
     */
    private Logger logger = Logger.getLogger(this.getClass().getName());

	private String TAG = "RtspRecorder";
	
	int sendtimeDebug = 0;

    /**
     * Constructor
     */
    private RtspRecorder() {
    	try {
//			videoEncoder = new VideoEncoderFromBuffer(PeerCamera.IMAGE_WIDTH,
//					PeerCamera.IMAGE_HEIGHT);
			AlgoFactory.getInstance().setListener(this);
			 // Initialize frame buffer
            if (frameBuffer == null) {
                frameBuffer = new CameraBuffer();
            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("xxx", "new videoEncoder error");
		}
    }
    
    public static RtspRecorder getInstance() {
    	
    	if (mInstance == null) {
    		mInstance = new RtspRecorder();
    	}
    	return mInstance;
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

    	Log.d(TAG, "open");
        // Init the RTP layer
        try {

        	rtpInput = new MediaRtpInput();
            rtpInput.open();
            
        	rtpMediaSender = new MediaRtpSender(new MediaCodecFormat());            
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
        Log.d("xxx", "rtsp close");
        // Close the RTP layer
//        videoEncoder.close();
        rtpInput.close();
        rtpMediaSender.stopSession();

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

        started = true;

        // Start RTP layer
//        if () {
        	rtpMediaSender.startSession();
        
        	// Start capture
        	captureThread.start();
        	// Player is started
        	videoStartTime = SystemClock.uptimeMillis();
        
        	AlgoFactory.getInstance().setListener(this);
//        }
        
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
    
    public void sendData(byte[] data) {
    	if (frameBuffer != null) {
    		frameBuffer.setFrame(data);
    	}
    }
    
    /**
     * Camera buffer
     */
    private class CameraBuffer {
        /**
         * YUV frame where frame size is always (videoWidth*videoHeight*3)/2
         */
//        private byte frame[] = new byte[(PeerCamera.IMAGE_WIDTH
//                * PeerCamera.IMAGE_HEIGHT * 3) / 2];
    	private List<byte[]> frames = new LinkedList<>();

        /**
         * Set the last captured frame
         *
         * @param frame Frame
         */
        public void setFrame(byte[] frame) {
            this.frames.add(frame);
        }

        /**
         * Return the last captured frame
         *
         * @return Frame
         */
        public byte[] getFrame() {
            if (this.frames.size() > 0) {
            	return this.frames.remove(0);
            }
            return null;
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

            int timeToSleep = 1000 / 15;
            int timestampInc = 90000 / 15;
            byte[] frameData;
//            byte[] encodedFrame;
            
            long encoderTs = 0;
            long oldTs = System.currentTimeMillis();

            while (started) {
                // Set timestamp
                long time = System.currentTimeMillis();
                encoderTs = encoderTs + (time - oldTs);

                // Get data to encode
                frameData = frameBuffer.getFrame();

        		/*
        		 * accept additional status 
        		 * EAVCEI_MORE_NAL     --  there is more NAL to be retrieved
        		 */
                if (frameData != null && frameData.length > 0 && frameData.length < 65535) {		
                	
                    // Send encoded frame                	
                    rtpInput.addFrame(frameData, timeStamp += timestampInc);
                } else {
                //	Log.d("xxx","RtpRecorder: captureThread: Status == EAVCEI_MORE_NAL");
                }

                // Sleep between frames if necessary
//                long delta = System.currentTimeMillis() - time;
//                if (delta < timeToSleep) {
//                    try {
//                    	Log.d("xxx","sleep: "+((timeToSleep - delta) - (((timeToSleep - delta) * 10) / 100)));
//                        Thread.sleep((timeToSleep - delta) - (((timeToSleep - delta) * 10) / 100));
//                    } catch (InterruptedException e) {
//                    }
//                }

//                 Update old timestamp
 //               oldTs = time;
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

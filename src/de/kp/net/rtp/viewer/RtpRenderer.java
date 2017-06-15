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

package de.kp.net.rtp.viewer;

import com.orangelabs.core.ims.protocol.rtp.MediaRegistry;
import com.orangelabs.core.ims.protocol.rtp.MediaRtpReceiver;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h263.H263Config;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h263.decoder.NativeH263Decoder;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h264.H264Config;
import com.orangelabs.core.ims.protocol.rtp.codec.video.h264.decoder.NativeH264Decoder;
import com.orangelabs.core.ims.protocol.rtp.format.video.H263VideoFormat;
import com.orangelabs.core.ims.protocol.rtp.format.video.H264VideoFormat;
import com.orangelabs.core.ims.protocol.rtp.format.video.MediaCodecFormat;
import com.orangelabs.core.ims.protocol.rtp.format.video.VideoFormat;
import com.orangelabs.core.ims.protocol.rtp.media.MediaOutput;
import com.orangelabs.core.ims.protocol.rtp.media.MediaSample;
import com.orangelabs.platform.network.DatagramConnection;
import com.orangelabs.platform.network.NetworkFactory;
import com.orangelabs.service.api.client.media.IMediaEventListener;
import com.orangelabs.service.api.client.media.IMediaRenderer;
import com.orangelabs.service.api.client.media.MediaCodec;
import com.orangelabs.service.api.client.media.video.VideoCodec;
import com.orangelabs.service.api.client.media.video.VideoSurfaceView;
import com.orangelabs.utils.logger.Logger;

import de.kp.net.rtsp.RtspConstants;
import de.kp.net.rtsp.client.RtspControl;
import de.kp.net.rtsp.client.SetUpParams;
import de.kp.net.rtsp.client.message.RtspDescriptor;
import de.kp.net.rtsp.client.message.RtspMedia;
import de.kp.rtspcamera.mediaCodec.encode.VideoEncoder;
import de.kp.rtspcamera.poker.utils.Utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Video RTP renderer. Supports only H.263 and H264 QCIF formats.
 *
 * @author jexa7410
 */
public class RtpRenderer {



    /**
     * Local RTP port
     */
    private int localRtpPort;

    /**
     * RTP receiver session
     */
    private MediaRtpReceiver rtpReceiver = null;

    /**
     * RTP media output
     */
    private MediaRtpOutput rtpOutput = null;

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

    /**
     * Temporary connection to reserve the port
     */
    private DatagramConnection temporaryConnection = null;

    /**
     * RTSP Control
     */
    private RtspControl rtspControl;
    
    public static OnConnectListener connectListener = null;
    
//    private CallbackTask callbackTask;
    
    /**
     * Constructor Force a RTSP Server Uri
     * @throws Exception 
     */
    
    public RtpRenderer(String uri, OnConnectListener listener, SetUpParams params) throws Exception {
        
        /*
         * The RtspControl opens a connection to an RtspServer, that
         * is determined by the URI provided.
         */
        rtspControl = new RtspControl(uri,params);    
        
//        callbackTask = new CallbackTask();
        
        connectListener = listener;
        
        /*
         * wait unit the rtspControl has achieved status READY; in this 
         * state, an SDP file is present and is ready to get evaluated
         */
        while (rtspControl.getState() != RtspConstants.READY) {
        	Log.d("RtspViewer", " rtspControl.getState() = "+rtspControl.getState());
        	; // blocking
        }

        /* 
         * Set the local RTP port: this is the (socket)
         * port, the RtspVideoRenderer is listening to
         * (UDP) RTP packets.
         */
        
    	// localRtpPort = NetworkRessourceManager.generateLocalRtpPort();
    	localRtpPort = rtspControl.getClientPort();
        reservePort(localRtpPort);

        /*
         * The media resources associated with the SDP descriptor are
         * evaluated and the respective video encoding determined
         */
        
        RtspDescriptor rtspDescriptor = rtspControl.getDescriptor();
        List<RtspMedia> mediaList = rtspDescriptor.getMediaList();
        
        if (mediaList.size() == 0) throw new Exception("The session description contains no media resource.");
        RtspMedia videoResource = null;
        
        for (RtspMedia mediaItem:mediaList) {
        	
        	if (mediaItem.getMediaType().equals(RtspConstants.SDP_VIDEO_TYPE)) {
        		videoResource = mediaItem;
        		break;
        	}
        	
        }
        
        if (videoResource == null) throw new Exception("The session description contains no video resource.");
        
        String codec = videoResource.getEncoding();
        if (codec == null) throw new Exception("No encoding provided for video resource.");
        Log.d("RtspViewer", " codec =  "+codec.toLowerCase());
           
        listener.onConnect();
        
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
     * Returns the local RTP port
     *
     * @return Port
     */
    public int getLocalRtpPort() {
        return localRtpPort;
    }

    /**
     * Reserve a port.
     *
     * @param port the port to reserve
     */
    private void reservePort(int port) {

    	if (temporaryConnection != null) return;
        try {
            temporaryConnection = NetworkFactory.getFactory().createDatagramConnection();
            temporaryConnection.open(port);

        } catch (IOException e) {
            temporaryConnection = null;
            Log.d("xxx", " reservePort port : "+port +" failed : "+e);
        }

    }

    /**
     * Release the reserved port; this method
     * is invoked while preparing the RTP layer
     */
    private void releasePort() {

    	if (temporaryConnection == null) return;
		try {
            temporaryConnection.close();
    
        } catch (IOException e) {
            temporaryConnection = null;
        }
        
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
     * Open the renderer
     */
    public void open() {
 
    	if (opened) {
            // Already opened
            return;
        }

        try {

        	// initialize RTP layer
            
        	releasePort();

            rtpOutput = new MediaRtpOutput();
            rtpOutput.open();
            
            rtpReceiver = new MediaRtpReceiver(localRtpPort);
            rtpReceiver.prepareSession(rtpOutput, new MediaCodecFormat());

        } catch (Exception e) {
        	Log.d("xxx", " open Exception e = "+e);
            if (logger.isActivated()) {
                logger.debug("Player error: " + e.getMessage());
            }

            return;

        }

        // Player is opened
        opened = true;

    }

    /**
     * Close the renderer
     */
    public void close() {

    	if (opened == false) return;

    	// Send TEARDOWN request to RTSP Server
    	rtspControl.stop();
    	
        // Close the RTP layer
        rtpReceiver.stopSession();
    	rtpOutput.close();
        // Player is closed
        opened = false;
        started = false;
        videoStartTime = 0L;

    }


    /**
     * Start the RTP layer (i.e listen to the reserved local
     * port for RTP packets), and send a PLAY request to the
     * RTSP server 
     */
    public void start() {

    	if ((opened == false) || (started == true)) {
            return;
        }
    	
    	// Start RTP layer
        rtpReceiver.startSession();

        // Send PLAY request to RTSP Server
        rtspControl.play();
        /*
         * wait unit the rtspControl has achieved status PLAYING
         */
        while (rtspControl.getState() != RtspConstants.PLAYING) {
        	; // blocking
        	Log.d("RtspViewer", "start rtspControl.getState() = "+rtspControl.getState());
        }
        
        // Renderer is started
        videoStartTime = SystemClock.uptimeMillis();
        started = true;

    }

    /**
     * Stop the renderer
     */
    public void stop() {

    	if (started == false) return;

    	// Send TEARDOWN request to RTSP Server
    	rtspControl.stop();
 
        // Stop RTP layer
        if (rtpReceiver != null) rtpReceiver.stopSession();

        if (rtpOutput != null) rtpOutput.close();

        // Renderer is stopped
        started = false;
        videoStartTime = 0L;
    
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
     * Media RTP output
     */
    private class MediaRtpOutput implements MediaOutput {
        /**
         * Video frame
         */
      //  private int decodedFrame[];

        /**
         * Constructor
         */
        public MediaRtpOutput() {
            
        	//decodedFrame = new int[selectedVideoCodec.getWidth() * selectedVideoCodec.getHeight()];
        
        }

        /**
         * Open the renderer
         */
        public void open() {
        }

        /**
         * Close the renderer
         */
        public void close() {
        }

        /**
         * Write a media sample
         *
         * @param sample Sample
         */
		public void writeSample(MediaSample sample) {
			
			if (sample.getData().length == 5 && sample.getData()[0] == -2 && sample.getData()[1] == -2
					&& sample.getData()[2] == -2 && sample.getData()[3] == -2 && sample.getData()[4] == -2) {
				if (connectListener != null){ 
					Log.d("xxx", "stop client");
					connectListener.onPlayerStop();
					return;
				}
			}
			
			if (sample.getData().length> 4 && sample.getData()[0] == -1 && sample.getData()[1] == -10&& sample.getData()[2] == -100) {
				Log.d("xxx", "writeSample result --- sample.getData()[3] = " + sample.getData()[3]);
				int intCount = (sample.getData().length / 4) - 1;
				if(intCount>0){
					int[] result = new int[intCount + 1];
					result[0] = sample.getData()[3];
					for (int i = 0; i < intCount; i++){
						byte[] tempByte = new byte[4];
						for (int j = 0; j < 4; j++){
							tempByte[j] = sample.getData()[4*(i+1)+j];
						}
						result[i+1] = Utils.bytesToInt(tempByte);
						Log.d("xxx", "writeSample result --- result["+i+1+"] = " + result[i+1]);
					}
					
					if (connectListener != null){
						connectListener.onReceiveResult(result);
//						Integer[] Ints = new Integer[intCount + 1];
//						for(int i = 0; i< Ints.length; i++){
//							Ints[i] = new Integer(result[i]);
//						}
//						new CallbackTask().execute(Ints);
					}
				}
			} else if (connectListener != null) {
				connectListener.onFrameData(sample);
			}
		}
    }
	
//	private class CallbackTask extends AsyncTask<Integer, Void, Void> {
//
//		@Override
//		protected Void doInBackground(Integer... params) {
//			// TODO Auto-generated method stub
//			int[] ints = new int[params.length];
//			for(int i = 0; i< params.length; i++){
//				ints[i] = params[i].intValue();
//			}
//			connectListener.onReceiveResult(ints);
//			return null;
//		}
//
//		
//	}

}


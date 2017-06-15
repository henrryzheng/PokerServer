package de.kp.rtspcamera.mediaCodec.encode;

import java.nio.ByteBuffer;

import de.kp.net.rtsp.RtspConstants;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.util.Log;

@SuppressLint("NewApi")
public class VideoEncoder {

	private MediaCodec mMediaEncoder;
	
	private byte[] mYuvBuffer = new byte[Integer.valueOf(RtspConstants.WIDTH)*Integer.valueOf(RtspConstants.HEIGHT)*3/2];
	private static VideoEncoder mInstance;
	public static final int FRAME_RATE = 30;
	private byte[] mMediaHead = null;
	private byte[] sps = null;
    private byte[] pps = null;
	private static String TAG  = "VideoEncoder";
	
	private VideoEncoder(String mime, int width, int height) {
		int colorFormat = selectColorFormat(selectCodec(mime), mime);
		Log.d(TAG,"setupEncoder "+mime+" colorFormat:"+colorFormat+" w:"+width+" h:"+height);
		try{
			mMediaEncoder = MediaCodec.createEncoderByType(mime);  
			MediaFormat mediaFormat = MediaFormat.createVideoFormat(mime, width, height);
			
//			mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 125000);  
			mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 500000);  
			mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);  
//			mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar);
			mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
			mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
			mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
			mMediaEncoder.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);  
			mMediaEncoder.start();
		} catch (Exception e) {
			Log.d(TAG, "instance failed : "+e);
		}
	}
	
	public static VideoEncoder getInstance() {
		if (mInstance == null) {
			mInstance = new VideoEncoder("video/avc",Integer.valueOf(RtspConstants.WIDTH),Integer.valueOf(RtspConstants.HEIGHT));
		}
		return mInstance;
	}
	
	/**
     * Returns a color format that is supported by the codec and by this test code.  If no
     * match is found, this throws a test failure -- the set of formats known to the test
     * should be expanded for new platforms.
     */
    @SuppressLint("NewApi")
	private static int selectColorFormat(MediaCodecInfo codecInfo, String mimeType) {
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(mimeType);
        for (int i = 0; i < capabilities.colorFormats.length; i++) {
            int colorFormat = capabilities.colorFormats[i];
            if (isRecognizedFormat(colorFormat)) {
                return colorFormat;
            }
        }
        Log.e(TAG,"couldn't find a good color format for " + codecInfo.getName() + " / " + mimeType);
        return 0;   // not reached
    }

    /**
     * Returns true if this is a color format that this test code understands (i.e. we know how
     * to read and generate frames in this format).
     */
    private static boolean isRecognizedFormat(int colorFormat) {
        switch (colorFormat) {
            // these are the formats we know how to handle for this test
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Returns the first codec capable of encoding the specified MIME type, or null if no
     * match was found.
     */
	@SuppressLint("NewApi")
	private static MediaCodecInfo selectCodec(String mimeType) {
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

            if (!codecInfo.isEncoder()) {
                continue;
            }

            String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (types[j].equalsIgnoreCase(mimeType)) {
                    return codecInfo;
                }
            }
        }
        return null;
    }
	
	public int encodeData(byte[] input,byte[] output) {
	//	swapYV12toI420(input, mYuvBuffer, 640, 480);
//		NV21toI420SemiPlanar(input, mYuvBuffer, 640, 480);
		swapNV21toI420(input, mYuvBuffer, 640, 480);
//		NV21ToNV12(input, mYuvBuffer, 640, 480);
//		changeYUV420SP2P(input, mYuvBuffer, 640, 480);
		System.arraycopy(mYuvBuffer, 0, input,0,input.length);
		return offerEncoder(input, output);
	}
	
	private int offerEncoder(byte[] input,byte[] output) {

		int pos = 0; 
		
	    try {
	        ByteBuffer[] inputBuffers = mMediaEncoder.getInputBuffers();
	        ByteBuffer[] outputBuffers = mMediaEncoder.getOutputBuffers();
	        int inputBufferIndex = mMediaEncoder.dequeueInputBuffer(-1);
	        if (inputBufferIndex >= 0) {
	            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
	            Log.d(TAG,"offerEncoder InputBufSize: " +inputBuffer.capacity()+" inputSize: "+input.length + " bytes");
	            inputBuffer.clear();
	            inputBuffer.put(input);
	            mMediaEncoder.queueInputBuffer(inputBufferIndex, 0, input.length, 0, 0);
	            
	        }
	       
	        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
	        int outputBufferIndex = mMediaEncoder.dequeueOutputBuffer(bufferInfo,0);
	        while (outputBufferIndex >= 0) {
	        	ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
	        	
	        	byte[] data = new byte[bufferInfo.size];
	            outputBuffer.get(data);
	            
	            Log.d(TAG,"offerEncoder InputBufSize:"+outputBuffer.capacity()+" outputSize:"+ data.length + " bytes written");
	            
	            if(mMediaHead != null)  
                {                 
                    System.arraycopy(data, 0,  output, pos, data.length);  
                    pos += data.length;  
				} else // 保存pps sps 只有开始时 第一个帧里有， 保存起来后面用
				{
					Log.d(TAG, "offer Encoder save sps head,len:"+data.length);
					ByteBuffer spsPpsBuffer = ByteBuffer.wrap(data);
					if (spsPpsBuffer.getInt() == 0x00000001) {
						mMediaHead = new byte[data.length];
						System.arraycopy(data, 0, mMediaHead, 0, data.length);
		                findSpsAndPps(mMediaHead);
					} else {
						Log.e(TAG,"not found media head.");
						return -1;
					}
				}

	            mMediaEncoder.releaseOutputBuffer(outputBufferIndex, false);
	            outputBufferIndex = mMediaEncoder.dequeueOutputBuffer(bufferInfo, 0);
	            
	          
	        }
	        
	        if(output[4] == 0x65) //key frame   编码器生成关键帧时只有 00 00 00 01 65 没有pps sps， 要加上  
            {
                System.arraycopy(output, 0,  input, 0, pos);  
                System.arraycopy(mMediaHead, 0,  output, 0, mMediaHead.length);  
                System.arraycopy(input, 0,  output, mMediaHead.length, pos);  
                pos += mMediaHead.length;  
            }  
	        
	    } catch (Throwable t) {
	        t.printStackTrace();
	    }
	    return pos;
	}
	
	public byte[] getSps() {
        return sps;
    }
 
    public byte[] getPps() {
        return pps;
    }
	
	private void findSpsAndPps(byte[] config) {
        Log.e("tag", printBuffer(config,0,config.length-1));
        int spsEnd = 1;
        for(int i = 4; i < config.length-4; i ++){
            if(config[i]==0X00 && config[i+1]==0X00 &&config[i+2]==0X00 &&config[i+3]==0X01){
                spsEnd = i-1;
                break;
            }
        }
        Log.e("tag", spsEnd - 3 + "...............................");
        sps = new byte[spsEnd-3];
        pps = new byte[config.length-spsEnd-5];
        System.arraycopy(config,4,sps,0,spsEnd-3);
        System.arraycopy(config, spsEnd + 5, pps, 0, config.length - spsEnd - 5);
        Log.e("tag", printBuffer(sps,0,sps.length-1));
        Log.e("tag", printBuffer(pps,0,pps.length-1));
        //
    }

    private String printBuffer(byte[] buffer, int start,int end) {
        String str = "";
        for (int i=start;i<end;i++) str+=","+Integer.toHexString(buffer[i]&0xFF);
        return str+"\r\n";
    }
	
	private void NV21ToNV12(byte[] nv21,byte[] nv12,int width,int height){
		if(nv21 == null || nv12 == null)return;
		int framesize = width*height;
		int i = 0,j = 0;
		System.arraycopy(nv21, 0, nv12, 0, framesize);
		for(i = 0; i < framesize; i++){
			nv12[i] = nv21[i];
		}
		for (j = 0; j < framesize/2; j+=2)
		{
			nv12[framesize + j-1] = nv21[j+framesize];
		}
		for (j = 0; j < framesize/2; j+=2)
		{
			nv12[framesize + j] = nv21[j+framesize-1];
		}
	}
	
	private void NV21toI420SemiPlanar(byte[] nv21bytes, byte[] i420bytes,
			int width, int height) {
		System.arraycopy(nv21bytes, 0, i420bytes, 0, width * height);
		for (int i = width * height; i < nv21bytes.length; i += 2) {
			i420bytes[i] = nv21bytes[i + 1];
			i420bytes[i + 1] = nv21bytes[i];
		}
	}
    
    private void swapYV12toI420(byte[] yv12bytes, byte[] i420bytes, int width, int height)   
    {        
        System.arraycopy(yv12bytes, 0, i420bytes, 0,width*height);
        System.arraycopy(yv12bytes, width*height+width*height/4, i420bytes, width*height,width*height/4);  
        System.arraycopy(yv12bytes, width*height, i420bytes, width*height+width*height/4,width*height/4);    
    }
    
    private void swapNV21toI420(byte[] yv12bytes, byte[] i420bytes, int width, int height)   
    {        
        System.arraycopy(yv12bytes, 0, i420bytes, 0,width*height);
        System.arraycopy(yv12bytes, width*height+width*height/4, i420bytes,width*height+width*height/4,width*height/4);  
        System.arraycopy(yv12bytes, width*height, i420bytes,width*height,width*height/4);    
    }
    
    private void changeYUV420SP2P(byte[] nv21bytes, byte[] i420bytes,
			int width, int height){   
    	  
    	System.arraycopy(nv21bytes, 0, i420bytes, 0,width*height);  
    	  
    	  
    	  
    	int strIndex = width*height;  
    	  
    	  
    	  
    	for(int i = width*height+1; i < nv21bytes.length ;i+=2)  
    	  
    	{  
    	  
    		i420bytes[strIndex++] = nv21bytes[i];  
    	  
    	}  
    	  
    	  
    	  
    	  
    	  
    	for(int i = width*height;i<nv21bytes.length;i+=2)  
    	{  
    		i420bytes[strIndex++] = nv21bytes[i];  
    	  
    	}   
    	  
    } 
    
    public void close () {
    	Log.i(TAG, "close()");
		try {
			mMediaEncoder.stop();
			mMediaEncoder.release();
			mMediaEncoder = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	
}

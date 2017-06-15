package com.orangelabs.core.ims.protocol.rtp.format.video;

public class MediaCodecFormat extends VideoFormat {

	/**
	 * Encoding name
	 */
	public static final String ENCODING = "media-codec";
	
	/**
	 * Payload type
	 */
	public static final int PAYLOAD = 98;
	
	/**
	 * Constructor
	 */
	public MediaCodecFormat() {
		super(ENCODING, PAYLOAD);
	}
}
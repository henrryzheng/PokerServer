/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_orangelabs_core_ims_protocol_rtp_codec_video_h263_encoder_NativeH263Encoder */

#ifndef _Included_com_orangelabs_core_ims_protocol_rtp_codec_video_h263_encoder_NativeH263Encoder
#define _Included_com_orangelabs_core_ims_protocol_rtp_codec_video_h263_encoder_NativeH263Encoder
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_orangelabs_core_ims_protocol_rtp_codec_video_h263_encoder_NativeH263Encoder
 * Method:    InitEncoder
 * Signature: (Lcom/orangelabs/core/ims/protocol/rtp/codec/h263/encoder/EncOptions;)I
 */
JNIEXPORT jint JNICALL Java_com_orangelabs_core_ims_protocol_rtp_codec_video_h263_encoder_NativeH263Encoder_InitEncoder
  (JNIEnv *, jclass, jobject);

/*
 * Class:     com_orangelabs_core_ims_protocol_rtp_codec_video_h263_encoder_NativeH263Encoder
 * Method:    EncodeFrame
 * Signature: ([BJ)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_orangelabs_core_ims_protocol_rtp_codec_video_h263_encoder_NativeH263Encoder_EncodeFrame
  (JNIEnv *, jclass, jbyteArray, jlong);

/*
 * Class:     com_orangelabs_core_ims_protocol_rtp_codec_video_h263_encoder_NativeH263Encoder
 * Method:    DeinitEncoder
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_orangelabs_core_ims_protocol_rtp_codec_video_h263_encoder_NativeH263Encoder_DeinitEncoder
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif

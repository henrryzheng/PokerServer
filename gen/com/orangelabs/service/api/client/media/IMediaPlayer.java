/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\workspace_haiqing\\workspace_0214\\PokerServer\\src\\com\\orangelabs\\service\\api\\client\\media\\IMediaPlayer.aidl
 */
package com.orangelabs.service.api.client.media;
/**
 * Media RTP player
 */
public interface IMediaPlayer extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.orangelabs.service.api.client.media.IMediaPlayer
{
private static final java.lang.String DESCRIPTOR = "com.orangelabs.service.api.client.media.IMediaPlayer";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.orangelabs.service.api.client.media.IMediaPlayer interface,
 * generating a proxy if needed.
 */
public static com.orangelabs.service.api.client.media.IMediaPlayer asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.orangelabs.service.api.client.media.IMediaPlayer))) {
return ((com.orangelabs.service.api.client.media.IMediaPlayer)iin);
}
return new com.orangelabs.service.api.client.media.IMediaPlayer.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_open:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
this.open(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_close:
{
data.enforceInterface(DESCRIPTOR);
this.close();
reply.writeNoException();
return true;
}
case TRANSACTION_start:
{
data.enforceInterface(DESCRIPTOR);
this.start();
reply.writeNoException();
return true;
}
case TRANSACTION_stop:
{
data.enforceInterface(DESCRIPTOR);
this.stop();
reply.writeNoException();
return true;
}
case TRANSACTION_getLocalRtpPort:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getLocalRtpPort();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_addListener:
{
data.enforceInterface(DESCRIPTOR);
com.orangelabs.service.api.client.media.IMediaEventListener _arg0;
_arg0 = com.orangelabs.service.api.client.media.IMediaEventListener.Stub.asInterface(data.readStrongBinder());
this.addListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removeAllListeners:
{
data.enforceInterface(DESCRIPTOR);
this.removeAllListeners();
reply.writeNoException();
return true;
}
case TRANSACTION_getSupportedMediaCodecs:
{
data.enforceInterface(DESCRIPTOR);
com.orangelabs.service.api.client.media.MediaCodec[] _result = this.getSupportedMediaCodecs();
reply.writeNoException();
reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
return true;
}
case TRANSACTION_getMediaCodec:
{
data.enforceInterface(DESCRIPTOR);
com.orangelabs.service.api.client.media.MediaCodec _result = this.getMediaCodec();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_setMediaCodec:
{
data.enforceInterface(DESCRIPTOR);
com.orangelabs.service.api.client.media.MediaCodec _arg0;
if ((0!=data.readInt())) {
_arg0 = com.orangelabs.service.api.client.media.MediaCodec.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.setMediaCodec(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.orangelabs.service.api.client.media.IMediaPlayer
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
// Open the player

@Override public void open(java.lang.String remoteHost, int remotePort) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(remoteHost);
_data.writeInt(remotePort);
mRemote.transact(Stub.TRANSACTION_open, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Close the player

@Override public void close() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_close, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Start the player

@Override public void start() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_start, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Stop the player

@Override public void stop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Returns the local RTP port

@Override public int getLocalRtpPort() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getLocalRtpPort, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// Add a media listener

@Override public void addListener(com.orangelabs.service.api.client.media.IMediaEventListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Remove media listeners

@Override public void removeAllListeners() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_removeAllListeners, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Get supported media codecs

@Override public com.orangelabs.service.api.client.media.MediaCodec[] getSupportedMediaCodecs() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.orangelabs.service.api.client.media.MediaCodec[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSupportedMediaCodecs, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArray(com.orangelabs.service.api.client.media.MediaCodec.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// Get media codec

@Override public com.orangelabs.service.api.client.media.MediaCodec getMediaCodec() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.orangelabs.service.api.client.media.MediaCodec _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMediaCodec, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.orangelabs.service.api.client.media.MediaCodec.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// Set media codec

@Override public void setMediaCodec(com.orangelabs.service.api.client.media.MediaCodec mediaCodec) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((mediaCodec!=null)) {
_data.writeInt(1);
mediaCodec.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_setMediaCodec, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_open = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_close = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_start = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_stop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getLocalRtpPort = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_addListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_removeAllListeners = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getSupportedMediaCodecs = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getMediaCodec = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_setMediaCodec = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
}
// Open the player

public void open(java.lang.String remoteHost, int remotePort) throws android.os.RemoteException;
// Close the player

public void close() throws android.os.RemoteException;
// Start the player

public void start() throws android.os.RemoteException;
// Stop the player

public void stop() throws android.os.RemoteException;
// Returns the local RTP port

public int getLocalRtpPort() throws android.os.RemoteException;
// Add a media listener

public void addListener(com.orangelabs.service.api.client.media.IMediaEventListener listener) throws android.os.RemoteException;
// Remove media listeners

public void removeAllListeners() throws android.os.RemoteException;
// Get supported media codecs

public com.orangelabs.service.api.client.media.MediaCodec[] getSupportedMediaCodecs() throws android.os.RemoteException;
// Get media codec

public com.orangelabs.service.api.client.media.MediaCodec getMediaCodec() throws android.os.RemoteException;
// Set media codec

public void setMediaCodec(com.orangelabs.service.api.client.media.MediaCodec mediaCodec) throws android.os.RemoteException;
}

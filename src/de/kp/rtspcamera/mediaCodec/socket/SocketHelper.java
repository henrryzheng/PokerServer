package de.kp.rtspcamera.mediaCodec.socket;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import de.kp.rtspcamera.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * CameraActivity.java
 * <p/>
 * Created by x.q. on 2017/01/01.
 * <p/>
 * Copyright (c) 2016 x.q.
 */

public class SocketHelper {

    private final static String TAG = "SocketHelper";
    private final static int LISTENING_PORT = 9999;
    private final static int SERVER_PORT = 8888;
    private final static String BROADCAST_IP = "192.168.43.255";

    private static SocketHelper mInstance;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private Socket socket;
    ArrayList<String> connectedIP = new ArrayList<String>();
    private BufferedWriter writer;
    private BufferedReader reader;
    private GetTimeListener listener;

    private DataOutputStream dataOutputStream;

    public void setListener(GetTimeListener listener) {
        this.listener = listener;
    }

    private SocketHelper() {
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread("PeerSocketThread");
            mHandlerThread.setPriority(8);
            mHandlerThread.start();
            mHandler = new Handler(mHandlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {

                }
            };
        }
    }

    public static SocketHelper getInstance() {
        synchronized (SocketHelper.class) {
            if (mInstance == null) {
                mInstance = new SocketHelper();
            }
        }
        return mInstance;
    }

    public void sendIpBroadcast() {
        Log.e(TAG, "sendIpBroadcast11");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "sendIpBroadcast22");
                final String message = getIP(MyApplication.getAppContext());
                try {
                    InetAddress adds = InetAddress.getByName(BROADCAST_IP);
                   // InetAddress adds = getBroadcastAddress(MyApplication.getAppContext());
                    DatagramSocket ds = new DatagramSocket();
                    DatagramPacket dp = new DatagramPacket(message.getBytes(),
                            message.length(), adds, LISTENING_PORT);
                    ds.send(dp);
                    Log.e(TAG, "sendIpBroadcast");
                    ds.close();
                    startListening();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "sendIpBroadcast failed : " + e);
                }
            }
        });
    }
//
    private void startListening() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG," startListening");
                    byte[] buf = new byte[1024];
                    DatagramSocket ds = new DatagramSocket(LISTENING_PORT);
                    DatagramPacket dp = new DatagramPacket(buf, buf.length);
                    ds.receive(dp);
                    ds.close();
                    StringBuffer sb = new StringBuffer();
                    int i;
                    for (i = 0; i < 1024; i++) {
                        if (buf[i] == 0) {
                            break;
                        }
                        sb.append((char) buf[i]);
                    }
                    connect(sb.toString());
                    Log.e(TAG, "startListening 2");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "startListening faile : "+e);
                }
            }
        });
    }

    private void connect(final String serverIp) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG," connect begin");
                try {
                    Log.e(TAG, serverIp);
                    socket = new Socket(serverIp, SERVER_PORT);
                    Log.e(TAG, "client connected");
//                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                    writer.write("Hello world");
//                    writer.newLine();
//                    writer.flush();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String content;
                            try {
                                while ((content = reader.readLine()) != null) {
                                    Log.e(TAG, "receive TIME");
                                    if (content.startsWith("time:")) {
                                        long receiveTime = SystemClock.elapsedRealtime();
                                        String[] str = content.split(":");
                                        long serverTime = Long.valueOf(str[1]);
                                        if (listener != null) {
                                            listener.onGetTime(receiveTime, serverTime);
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void postMsg(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
//                    writer.write(msg);
//                    writer.newLine();
//                    writer.flush();

                    dataOutputStream.write(msg.getBytes("UTF-8"));
                    Log.e(TAG, "write done.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void postMsg(final long time, final byte[] data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject root = new JSONObject();
                    root.put("time", time);
                    root.put("data", new String(data));
                    writer.write(root.toString());
                    writer.newLine();
                    writer.flush();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void postMsg(final byte[] data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    dataOutputStream.write(data);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static InetAddress getBroadcastAddress(Context context) throws UnknownHostException {
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if(dhcp==null) {
            return InetAddress.getByName("255.255.255.255");
        }
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++) {
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
            Log.d(TAG,"getBroadcastAddress quads["+k+"]  =  "+ quads[k] );
        }


        return InetAddress.getByAddress(quads);
    }

    private static String getIP(Context application) {
        WifiManager wifiManager = (WifiManager) application.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        } else {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        }
        return null;
    }
//
    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    interface GetTimeListener {
        void onGetTime(long receiveTime, long serverTime);
    }

    public void initClinet () {
        Log.d(TAG,"initClinet ");
//        mHandler.post(getConnectedIPRunable);
        //涓嶅彲鍙栵紝 杩炴帴璁板綍涓嶈兘娓呴櫎
    }

//    private Runnable getConnectedIPRunable = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                BufferedReader br = new BufferedReader(new FileReader(
//                        "/proc/net/arp"));
//                String line;
//                while ((line = br.readLine()) != null) {
//                    String[] splitted = line.split(" +");
//                    if (splitted != null && splitted.length >= 4) {
//                        String ip = splitted[0];
//                        connectedIP.add(ip);
//                    }
//                    Log.d(TAG,"getConnectedIP : line = "+line);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.d(TAG, "getConnectedIPRunable failed:  " + e);
//            }
//
//            if (connectedIP == null || connectedIP.size() <= 0) {
//                Log.d(TAG, "getConnectedIPRunable is null, try again:  ");
//                mHandler.postDelayed(getConnectedIPRunable, 3000);
//            } else {
//
//                initSockect();
//            }
//        }
//    };
//
//
//    private void initSockect () {
//
//        String clientIp = connectedIP.get(0);
//        Log.d(TAG,"initSockect  clientIp = "+clientIp );
//        try {
//            socket=new Socket(clientIp, 32345);//鍙戦�鍒版湰鏈轰笅鏌愪釜Ip鐨勭鍙ｄ笂
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d(TAG,"initSockect failed : " + e );
//        }
//    }
}

package de.kp.rtspcamera.poker.utils;

public class Utils {
	
	public Utils(){
		
	}
	
	/**
	 * ��intת����byte����
	 * 
	 * @param Ҫת����intֵ
	 * @return ���ص�byte����
	 */
	public static byte[] intToBytes(int n) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (n >> (24 - i * 8));
		}
		return b;
	}
	
	 
	/**
	 * ��byte����ת����int����
	 * 
	 * @param Դbyte����
	 * @return ���ص�intֵ
	 */
	public static int bytesToInt(byte[] b) {
		int a = (((int) b[0]) << 24) + (((int) b[1]) << 16) + (((int) b[2]) << 8) + b[3];
		if (a < 0) {
			a = a + 256;
		}
		return a;
	}
	
}

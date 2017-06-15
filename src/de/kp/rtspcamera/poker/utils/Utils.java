package de.kp.rtspcamera.poker.utils;

public class Utils {
	
	public Utils(){
		
	}
	
	/**
	 * 把int转换成byte数组
	 * 
	 * @param 要转换的int值
	 * @return 返回的byte数组
	 */
	public static byte[] intToBytes(int n) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (n >> (24 - i * 8));
		}
		return b;
	}
	
	 
	/**
	 * 把byte数组转换成int类型
	 * 
	 * @param 源byte数组
	 * @return 返回的int值
	 */
	public static int bytesToInt(byte[] b) {
		int a = (((int) b[0]) << 24) + (((int) b[1]) << 16) + (((int) b[2]) << 8) + b[3];
		if (a < 0) {
			a = a + 256;
		}
		return a;
	}
	
}

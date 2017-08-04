package com.example.administrator.cheshilishop.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author txiuqi
 * 
 */
public class NetworkUtil {

	public static boolean isNetworkValidate(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnected()
				&& cm.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) {
			return cm.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	/**
	 * 获取本地ip地址使用GPRS时，获取本机IP地址
	 * 
	 * @return
	 */
	public static String getLocAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				// 得到每一个网络接口绑定的所有ip
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	public static boolean checkIP(String str) {
		Pattern pattern = Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
				+ "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 获取wifi网络下的手机ip地址
	 *
	 * @param context
	 * @return
	 */
	public static final String getIPAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// if (!wifiManager.isWifiEnabled()) {
		// wifiManager.setWifiEnabled(true);
		// }
		if (!wifiManager.isWifiEnabled()) {
			return getLocAddress();
		} else {

			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			String ipString = intToIp(ipAddress);
			if (ipString != null) {
				return ipString;
			}
		}
		return null;
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}
}

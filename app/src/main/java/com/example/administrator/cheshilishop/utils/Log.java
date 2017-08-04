package com.example.administrator.cheshilishop.utils;
/**
 * @描述 日志管理
 * @author txiuqi
 * 
 */
public class Log {

	private static final boolean isDebug = true;
	private static final String TAG = "BrickUser";
	
	public static String tag() {
		StackTraceElement[] stack = new Exception().getStackTrace();
		String name = null;
		
		for (int i = 0; i < stack.length; i++) {
			if (!stack[i].getClassName().equals(Log.class.getName())) {
				String[] names = stack[i].getClassName().split("\\.");
				name = names[names.length - 1] + "_" + stack[i].getMethodName() + "()";
				break;
			}
		}
		name = TAG + "_" + name;
		return name;
	}
	
	/**
	 * 在软件系统的非DEBUG模式，强制打印Log
	 * 使用此方法如果不为基础数据类型和String,char类型,重写Object对象的toString()方法
	 * @param msg
	 */
	public static void forceLogError(String tag, Object msg) {
		android.util.Log.e(tag, msg + "");
	}
	
	/**
	 * 使用此方法如果不为基础数据类型和String,char类型,重写Object对象的toString()方法
	 * @param msg
	 */
	public static void e(Object msg) {
		if (!isDebug) {
			return;
		}
		android.util.Log.e(tag(), msg + "");
	}
	
	/**
	 * 使用此方法如果不为基础数据类型和String,char类型,重写Object对象的toString()方法
	 * @param msg
	 */
	public static void e(String tag, Object msg) {
		if (!isDebug) {
			return;
		}
		android.util.Log.e(tag, msg + "");
	}
	
	/**
	 * 使用此方法如果不为基础数据类型和String,char类型,重写Object对象的toString()方法
	 * @param msg
	 * @param isShow 是否显示,当软件全局设置为DEBUG模式的话,可以在类别中单独设置是否显示此Log
	 */
	public static void e(String tag, Object msg, boolean isShow) {
		if (!isDebug) {
			return;
		}
		if (!isShow) {
			return;
		}
		android.util.Log.e(tag, msg + "");
	}
	
	/**
	 * 使用此方法如果不为基础数据类型和String,char类型,重写Object对象的toString()方法
	 * @param msg
	 */
	public static void v(Object msg) {
		if (!isDebug) {
			return;
		}
		android.util.Log.v(tag(), msg + "");
	}
	/**
	 * 使用此方法如果不为基础数据类型和String,char类型,重写Object对象的toString()方法
	 * @param msg
	 */
	public static void v(String tag, Object msg) {
		if (!isDebug) {
			return;
		}
		android.util.Log.v(tag, msg + "");
	}
	/**
	 * 使用此方法如果不为基础数据类型和String,char类型,重写Object对象的toString()方法
	 * @param msg
	 */
	public static void d(Object msg) {
		if (!isDebug) {
			return;
		}
		android.util.Log.d(tag(), msg + "");
	}
	/**
	 * 使用此方法如果不为基础数据类型和String,char类型,重写Object对象的toString()方法
	 * @param msg
	 */
	public static void d(String tag, Object msg) {
		if (!isDebug) {
			return;
		}
		android.util.Log.d(tag, msg + "");
	}
	/**
	 * 使用此方法如果不为基础数据类型和String,char类型,重写Object对象的toString()方法
	 * @param msg
	 */
	public static void i(Object msg) {
		if (!isDebug) {
			return;
		}
		android.util.Log.i(tag(), msg + "");
	}
	/**
	 * 使用此方法如果不为基础数据类型和String,char类型,重写Object对象的toString()方法
	 * @param msg
	 */
	public static void i(String tag, Object msg) {
		if (!isDebug) {
			return;
		}
		android.util.Log.i(tag, msg + "");
	}
	/**
	 * 使用此方法如果不为基础数据类型和String,char类型,重写Object对象的toString()方法
	 * @param msg
	 */
	public static void w(Object msg) {
		if (!isDebug) {
			return;
		}
		android.util.Log.w(tag(), msg + "");
	}
	/**
	 * 使用此方法如果不为基础数据类型和String,char类型,重写Object对象的toString()方法
	 * @param msg
	 */
	public static void w(String tag, Object msg) {
		if (!isDebug) {
			return;
		}
		android.util.Log.w(tag, msg + "");
	}
}

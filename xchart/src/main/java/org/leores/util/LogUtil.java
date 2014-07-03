package org.leores.util;

public class LogUtil extends Logger {

	public static String _tLog(int logLevel, String str) {
		return getTLogger()._log(logLevel, str);
	}

	public static String tLog(int logLevel, boolean addLineBreak, String str) {
		return getTLogger().log(logLevel, addLineBreak, str);
	}

	public static String tLog(int logLevel, String str) {
		return getTLogger().log(logLevel, str);
	}

	public static String tLog(boolean addLineBreak, String str) {
		return getTLogger().log(addLineBreak, str);
	}

	public static String tLog(String str) {
		return getTLogger().log(str);
	}

	public static String tLog(Exception e) {
		return getTLogger().log(e);
	}

	public static String tLog(Object obj) {
		return getTLogger().log(obj);
	}

	public static String tLog(String str, Object obj) {
		return getTLogger().log(str, obj);
	}

	public static String tLog(Object... objs) {
		return getTLogger().log(objs);
	}

	public static String tLog(String str, Object... objs) {
		return getTLogger().log(str, objs);
	}
}

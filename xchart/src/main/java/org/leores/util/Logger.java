package org.leores.util;

import java.util.List;

import org.leores.util.able.Processable2;

public class Logger {
	protected String log = "";
	protected Integer logRecordLevel = LOG_MAX;
	protected Integer logOutputLevel = LOG_MIN;
	protected Processable2<String, Integer, String> logProcessor = null;
	protected static Logger tLogger = new Logger();

	public final static int LOG_MAX = 100;
	public final static int LOG_CRITICAL = 80;
	public final static int LOG_ERROR = 70;
	public final static int LOG_WARNING = 60;
	public final static int LOG_INFO = 50; //default log level
	public final static int LOG_CASUAL = 40;
	public final static int LOG_TRIVIAL = 30;
	public final static int LOG_MIN = 0;

	public static void setTLogger(Logger logger) {
		tLogger = logger;
	}

	public static Logger getTLogger() {
		return tLogger;
	}

	public String getLog() {
		return log;
	}

	public void setLogProcessor(Processable2<String, Integer, String> logProcessor) {
		this.logProcessor = logProcessor;
	}

	public Integer getLogRecordLevel() {
		return logRecordLevel;
	}

	public void setLogRecordLevel(Integer logRecordLevel) {
		this.logRecordLevel = logRecordLevel;
	}

	public Integer getLogOutputLevel() {
		return logOutputLevel;
	}

	public void setLogOutputLevel(Integer logOutputLevel) {
		this.logOutputLevel = logOutputLevel;
	}

	protected String process(int logLevel, String str) {
		String rtn = str;
		if (logProcessor != null) {
			rtn = logProcessor.process(logLevel, str);
		}
		return rtn;
	}

	protected String _log(int logLevel, String str) {
		if (logLevel >= LOG_ERROR) {
			System.err.print(str);
		} else {
			System.out.print(str);
		}
		return str;
	}

	protected String log(int logLevel, boolean addLineBreak, String str) {
		String rtn = process(logLevel, str);
		if (rtn != null) {
			String sEnd = "";
			if (addLineBreak) {
				sEnd = "\n";
			}
			if (logLevel >= logRecordLevel) {
				log += rtn + sEnd;
			}
			if (logLevel >= logOutputLevel) {
				if (this.equals(getTLogger())) {
					rtn = _log(logLevel, rtn + sEnd);
				} else {
					rtn = getTLogger().log(logLevel, addLineBreak, rtn);
				}

			}
		}
		return rtn;
	}

	protected String log(int logLevel, String str) {
		return log(logLevel, true, str);
	}

	protected String log(boolean addLineBreak, String str) {
		return log(LOG_INFO, addLineBreak, str);
	}

	protected String log(Exception e) {
		e.printStackTrace();
		return log(LOG_ERROR, true, e.toString());
	}

	protected String log(String str) {
		return log(LOG_INFO, true, str);
	}

	protected String log(String str, Object obj) {
		return log(str + U.toStr(obj));
	}

	protected String log(String str, Object... objs) {
		return log(str + U.toStr(objs));
	}

	protected String log(Object obj) {
		return log("", obj);
	}

	protected String log(Object... objs) {
		return log("", objs);
	}

}

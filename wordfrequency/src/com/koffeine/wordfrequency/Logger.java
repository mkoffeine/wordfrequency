package com.koffeine.wordfrequency;

import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 23.03.13
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
public class Logger {
	private String tag;
	private Logger(String tag) {
		this.tag = tag;
	}
	public static Logger getLogger(String tag) {
		return new Logger(tag);
	}
	public void debug(String log) {
		Log.d("123123" + tag, "123123 "+ log );
	}
}

package com.koffeine.wordfrequency2;

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

    public static Logger getLogger() {
        String tag;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length > 2) {
            //[0]-Thread [1]-Logger [2]  Class which we need
            tag = Thread.currentThread().getStackTrace()[2].getClassName();
        } else {
            tag = "Unknown";
        }
        return new Logger(tag);
    }

    private Logger(String tag) {
        this.tag = tag;
    }

    public static Logger getLogger(String tag) {
        return new Logger(tag);
    }

    public void debug(String log) {
        Log.d("123123" + tag, "123123 " + log);
    }

//    static class A {
//        private static Logger logger = Logger.getLogger();
//        static void m() {
//            logger.debug("aaaa");
//        }
//    }
//    public static void main(String[] args) {
//        A.m();
//    }
}

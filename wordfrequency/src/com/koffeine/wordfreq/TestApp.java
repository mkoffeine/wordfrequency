package com.koffeine.wordfreq;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 24.03.13
 * Time: 0:06
 * To change this template use File | Settings | File Templates.
 */
public class TestApp {
	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("qqq");
		list.add("111q");
		list.add("aaaqqqqqq");
		list.add("ssssqqq");
		String[] arr = list.toArray(new String[]{});
		System.err.println(arr.toString());
	}
}

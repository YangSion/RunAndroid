package com.yangsion.runandroid.util;

import java.util.Random;

/**
 * 随机数值 工具
 * @author stabilit.yang
 *
 */
public class RunningNumberUtil {

	public int number() {
		Random ra = new Random();
		int number = 0;
		for (int i = 100; i < 10000000; i++) {
			number = i;
		}
		return ra.nextInt((number));
	}
}

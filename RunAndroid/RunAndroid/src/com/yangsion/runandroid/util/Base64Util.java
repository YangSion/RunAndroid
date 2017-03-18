package com.yangsion.runandroid.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Base64工具
 * @author stabilit.yang
 *
 */
public class Base64Util {

	/**
	 * Base64加密
	 * 
	 * @param data
	 * @return
	 */
	public static String[] setBase64(byte[] data) {
		String photoData = Base64.encodeToString(data, Base64.NO_WRAP);
		String[] strData = { photoData };
		return strData;
	}

	/**
	 * Base64解密
	 * 
	 * @param data
	 * @return
	 */
	public static Bitmap getBase64(JSONArray pickfile) {
		Bitmap bitmap = null;
		try {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < pickfile.length(); i++) {
				list.add(pickfile.getString(i));

			}
			String[] stringArray = list.toArray(new String[list.size()]);
			String pick = null;
			for (String string : stringArray) {
				pick = string;
			}
			byte[] bitmapArray = Base64.decode(pick, Base64.DEFAULT);
			// 将字符串转换成Bitmap类型
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}
}

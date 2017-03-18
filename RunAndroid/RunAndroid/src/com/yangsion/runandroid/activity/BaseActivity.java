package com.yangsion.runandroid.activity;

import com.yangsion.runandroid.MyApplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
/**
 * super activity
 * @author stabilit.yang
 *
 */
public class BaseActivity extends FragmentActivity{
	public static final String TAG = "BaseActivity";
	protected ActivityManager am;
	protected SharedPreferences sp;
	protected TelephonyManager tm;
	public Context context;
	
	/**
	 * 网络状态
	 * @return
	 */
	protected boolean isNetWorkAvailable(){
		return MyApplication.isNetWorkAvailable(context);
	}
	
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		context = this;
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		tm = (TelephonyManager)MyApplication.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	
	protected String getVersionName() throws Exception {  
	        // 获取packagemanager的实例  
	        PackageManager packageManager = getPackageManager();  
	        // getPackageName()是你当前类的包名，0代表是获取版本信息  
	        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);  
	        return packInfo.versionName;  
	}
	
	public void toast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		Log.d(TAG, msg);
	}
	
	Toast mToast;

	public void showToast(String text) {
		if (!TextUtils.isEmpty(text)) {
			if (mToast == null) {
				mToast = Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT);
			} else {
				mToast.setText(text);
			}
			mToast.show();
		}
	}
	
	public void showToast(int resId) {
		if (mToast == null) {
			mToast = Toast.makeText(getApplicationContext(), resId,
					Toast.LENGTH_SHORT);
		} else {
			mToast.setText(resId);
		}
		mToast.show();
	}
	
	public static void showLog(String msg) {
		Log.i("smile", msg);
	}
	
}

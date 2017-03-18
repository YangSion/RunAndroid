package com.yangsion.runandroid;


import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;


/**
 * Application
 * 只值初始化一次，不在此获取变化值
 * @author stabilit.yang
 *
 */
public class MyApplication extends Application {
	public static final String TAG  = "MyApplication";
	public static final String APPID  = "";

	public static Context applicationContext;
	private static MyApplication instance;
	public String mCore = null;


	@Override
	public void onCreate() {
		super.onCreate();
		//初始化R.string.Core
		mCore = (String)getString(R.string.Core);
		applicationContext = this;
		instance = this;
				
		//提供以下两种方式进行初始化操作：
        //第一：默认初始化
        //Bmob.initialize(this, "Your Application ID");

        //第二：设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，自v3.4.7版本开始
        BmobConfig config =new BmobConfig.Builder(this)
        //设置appkey
        .setApplicationId(APPID)
        //请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(30)
        //文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        //文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);
        Bmob.initialize(this, APPID);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}

	public static MyApplication getInstance() {
		return instance;
	}
	
	/**
	 * 手机是否联网 需要android.permission.ACCESS_NETWORK_STATE权限
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		}
		return false;
	}
	
}

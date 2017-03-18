package com.yangsion.runandroid.activity;


import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.yangsion.runandroid.R;
/**
 * 开屏页  The start page
 * @author stabilit.yang
 *
 */
public class StartActivity extends BaseActivity {
	public static final String TAG = "StartActivity";
	
	private static final int STARTTIME = 1000;
	private GifView mGifbg;
	private Timer mTimer;
	private TimerTask mTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				gifBg();
				mTimer = new Timer();
				mTask = new TimerTask() {
					@Override
					public void run() {
						startActivity(new Intent(StartActivity.this, HomeActivity.class));
						StartActivity.this.finish();
					}
				};
				mTimer.schedule(mTask, STARTTIME);
			}
		});
		
	}
	
	
	private void gifBg() {
		// 从xml中得到GifView的句柄
		mGifbg = (GifView) findViewById(R.id.gif_bg);
		// 设置Gif图片源
		mGifbg.setGifImage(R.drawable.start_bg);
		// 设置显示的大小，拉伸或者压缩
		mGifbg.setShowDimension(getWindowManager().getDefaultDisplay().getWidth(),getWindowManager().getDefaultDisplay().getHeight());
		// 添加监听器
		mGifbg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
				mGifbg.setGifImageType(GifImageType.COVER);
			}
		});
	}	
	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		//getMenuInflater().inflate(R.menu.start, menu);
//		//去设置，或者弹出设置选项
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
}

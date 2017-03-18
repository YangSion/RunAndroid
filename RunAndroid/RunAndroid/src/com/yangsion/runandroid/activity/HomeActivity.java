package com.yangsion.runandroid.activity;


import org.json.JSONArray;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.yangsion.runandroid.R;
import com.yangsion.runandroid.domen.FeedBackInfo;
import com.yangsion.runandroid.fragment.QingLiFragment;
import com.yangsion.runandroid.fragment.JianCeFragment;
import com.yangsion.runandroid.fragment.JinChengFragment;
import com.yangsion.runandroid.fragment.RuanJianFragment;
import com.yangsion.runandroid.util.Base64Util;
import com.yangsion.runandroid.widget.CircleImageView;
/**
 * 主页  The main page
 * @author stabilit.yang
 *
 */
public class HomeActivity extends BaseActivity implements OnClickListener {
	public static final String TAG = "HomeActivity";
	
	private Dialog mDialog;
	private CircleImageView mPick;
	private ImageButton mMenu, mHuanfu;
	public TextView[] mTabs;
	private Fragment[] mFragments;
	private int mIndex;
	// 当前fragment的index
	private int mCurrentTabIndex = 0;
	private FrameLayout mFrameLayout;
	private JianCeFragment mJianCeFragment;
	private QingLiFragment mJiaSuFragment;
	private JinChengFragment mJinChengFragment;
	private RuanJianFragment mRuanJianFragment;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mFrameLayout = (FrameLayout) findViewById(R.id.list_frame);
		mPick = (CircleImageView) findViewById(R.id.pick);
		mMenu = (ImageButton) findViewById(R.id.menu);
		mHuanfu = (ImageButton) findViewById(R.id.huanfu);
		mPick.setOnClickListener(this);
		mMenu.setOnClickListener(this);
		mHuanfu.setOnClickListener(this);
		
		mJianCeFragment = new JianCeFragment();
		mJiaSuFragment = new QingLiFragment();
		mJinChengFragment = new JinChengFragment();
		mRuanJianFragment = new RuanJianFragment();
		
		mFragments = new Fragment[]{mJianCeFragment, mJiaSuFragment, mJinChengFragment, mRuanJianFragment};
		
		mTabs = new TextView[4];
		mTabs[0] = (TextView) findViewById(R.id.button1);
		mTabs[1] = (TextView) findViewById(R.id.button2);
		mTabs[2] = (TextView) findViewById(R.id.button3);
		mTabs[3] = (TextView) findViewById(R.id.button4);
		mTabs[0].setOnClickListener(this);
		mTabs[1].setOnClickListener(this);
		mTabs[2].setOnClickListener(this);
		mTabs[3].setOnClickListener(this);
		
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.list_frame, mJianCeFragment)
				.add(R.id.list_frame, mRuanJianFragment)
				.hide(mRuanJianFragment)//程序停止运行会和mJianCeFragment同时显示
				.show(mJianCeFragment)
				.commit();
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);
		  
	}
	

	public ActivityManager getAm() {
		return am;
	}


	public SharedPreferences getSp() {
		return sp;
	}


	/**
	 * 点击事件
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			mIndex = 0;
			break;
		case R.id.button2:
			mIndex = 1;
			break;
		case R.id.button3:
			mIndex = 2;
			break;
		case R.id.button4:
			mIndex = 3;
			break;
		case R.id.pick:
			//（去登陆界面  关联QQ 微信  新浪微博  （facebook twitter 待办）未做）
			//-------------------------------------------------------
			//改为去个人中心  只修改头像
			startActivity(new Intent(this, Login_RegisterActivity.class));
			break;
		case R.id.menu:
			startActivity(new Intent(this, MenuActivity.class));
			break;
		case R.id.huanfu:
			hfAction();
			break;
		}
		
		if (mCurrentTabIndex != mIndex) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(mFragments[mCurrentTabIndex]);
			if (!mFragments[mIndex].isAdded()) {
				trx.add(mFrameLayout.getId(), mFragments[mIndex]);
			}
			trx.show(mFragments[mIndex]).commit();
		}
		mTabs[mCurrentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[mIndex].setSelected(true);
		mCurrentTabIndex = mIndex;
		
	}

	/**
	 * 换肤
	 */
	private void hfAction() {
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.dialog_hf, null);
		// 不重定义dilog类 ，dilog点击不消失（自解决）
		final Dialog dialog = new AlertDialog.Builder(this).create();
		// 点击屏幕dismiss/此方法调用dialog需先create()
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();// 这方法必须在setContentView之前用
		dialog.getWindow().setContentView(layout);
		
		Button button1 = (Button) layout.findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDialog = ProgressDialog.show(HomeActivity.this, getString(R.string.bj_ts), getString(R.string.bj_waiting));
				//提交操作
				FeedBackInfo mInfos = new FeedBackInfo();
				mInfos.setDeviceId(tm.getDeviceId());
				mInfos.setText(getString(R.string.cjfu));
				mInfos.save(HomeActivity.this, new SaveListener() {
					@Override
					public void onSuccess() {
						dialog.dismiss();
						mDialog.dismiss();
						toast(getString(R.string.bj_succes));
					}
					
					@Override
					public void onFailure(int code, final String msg) {
						dialog.dismiss();
						mDialog.dismiss();
						toast(getString(R.string.bj_fialure)+msg);
					}
				});
			}
		});
		Button button2 = (Button) layout.findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	
	/**
	 * activity可见调用
	 */
	@Override
	protected void onStart() {
		super.onStart();
		JSONArray pickfile = (JSONArray) BmobUser.getObjectByKey(this, "pickfile");
		if (pickfile != null) {
			Bitmap bitmap = Base64Util.getBase64(pickfile);
	    	mPick.setImageBitmap(bitmap);
		}
		else {
			mPick.setImageResource(R.drawable.pick);
		}
	}
	
	/**
	 * activity没销毁重新打开调用
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		JSONArray pickfile = (JSONArray) BmobUser.getObjectByKey(this, "pickfile");
		if (pickfile != null) {
			Bitmap bitmap = Base64Util.getBase64(pickfile);
	    	mPick.setImageBitmap(bitmap);
		}
		else {
			mPick.setImageResource(R.drawable.pick);
		}
	}
	
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (event.getAction() == KeyEvent.ACTION_DOWN) {
//			
//			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//				//ExitApp();
//				return true;
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
	
	

//	private long exitTime = 0;
//	private int status = 0;
//	/**
//	 * 退出app提示
//	 */
//	public void ExitApp(){
//         if ((System.currentTimeMillis() - exitTime) > 2000 || status == 0){
//             toast(getString(R.string.Toast_exitapp));
//             status++;
//             exitTime = System.currentTimeMillis();
//         } else if((System.currentTimeMillis() - exitTime) < 2000 && status != 0){
//        	 HomeActivity.this.finish();
//         }
//
//     }


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		//getMenuInflater().inflate(R.menu.start, menu);
//		//去设置，或者弹出设置选项
//		return true;
//	}
//
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

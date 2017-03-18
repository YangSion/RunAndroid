package com.yangsion.runandroid.activity;

import cn.bmob.v3.listener.SaveListener;

import com.yangsion.runandroid.R;
import com.yangsion.runandroid.domen.FeedBackInfo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 菜单选项   Menu options
 * @author stabilit.yang
 *
 */
public class MenuActivity extends BaseActivity implements OnClickListener {
	public static final String TAG = "MenuActivity";
	
	private Dialog mDialog;
	
	private ScrollView mFrame_lx, mFrame_app;
	private RelativeLayout mFrame_yjfk, mFrame_gy;
	private LinearLayout mOptions;
	private Button mSubmit, mApp, mAuthor;
	private EditText mTextEmail, mLianxi_fs, mFankui_nr, mName;
	private TextView mBtn_back, mTitle, mApp_tj, mLx, mYjfk, mSc, mGy, mKfzlm, mTextDetailed, mSdk;
	
	
	private boolean show_frame_lx = false;
	private boolean show_frame_yjfk = false;
	private boolean show_frame_gy = false;
	private boolean show_frame_app = false;
	
	@Override
	protected void onCreate(@Nullable Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_menu);
		
		mBtn_back = (TextView) findViewById(R.id.btn_back);
		mTitle = (TextView) findViewById(R.id.title);
		mApp_tj = (TextView) findViewById(R.id.app_tj);
		mLx = (TextView) findViewById(R.id.lx);
		mYjfk = (TextView) findViewById(R.id.yjfk);
		mSc = (TextView) findViewById(R.id.sc);
		mGy = (TextView) findViewById(R.id.gy);
		mKfzlm = (TextView) findViewById(R.id.kfzlm);
		mTextDetailed = (TextView) findViewById(R.id.textDetailed);
		mSdk = (TextView) findViewById(R.id.sdk);
		mTextEmail = (EditText) findViewById(R.id.textEmail);
		mLianxi_fs = (EditText) findViewById(R.id.lianxi_fs);
		mFankui_nr = (EditText) findViewById(R.id.fankui_nr);
		mName = (EditText) findViewById(R.id.name);
		mFrame_lx = (ScrollView) findViewById(R.id.frame_lx);
		mOptions = (LinearLayout) findViewById(R.id.options);
		mFrame_yjfk = (RelativeLayout) findViewById(R.id.frame_yjfk);
		mFrame_gy = (RelativeLayout) findViewById(R.id.frame_gy);
		mFrame_app = (ScrollView) findViewById(R.id.frame_app);
		mSubmit = (Button) findViewById(R.id.submit);
		mApp = (Button) findViewById(R.id.app);
		mAuthor = (Button) findViewById(R.id.author);
		
		mBtn_back.setOnClickListener(this);
		mApp_tj.setOnClickListener(this);
		mLx.setOnClickListener(this);
		mYjfk.setOnClickListener(this);
		mSc.setOnClickListener(this);
		mGy.setOnClickListener(this);
		mKfzlm.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mApp.setOnClickListener(this);
		mAuthor.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == mBtn_back.getId()) {
			if (show_frame_lx == true) {
				mTitle.setText(R.string.options);
				mOptions.setVisibility(View.VISIBLE);
				mFrame_lx.setVisibility(View.GONE);
				show_frame_lx = false;
			}
			else if (show_frame_yjfk == true){
				mTitle.setText(R.string.options);
				mFrame_yjfk.setVisibility(View.GONE);
				show_frame_yjfk = false;
			}
			else if (show_frame_gy == true && show_frame_app == false){
				mTitle.setText(R.string.options);
				mOptions.setVisibility(View.VISIBLE);
				mFrame_gy.setVisibility(View.GONE);
				show_frame_gy = false;
			}
			else if (show_frame_gy == true && show_frame_app == true){
				mTitle.setText(R.string.About);
				mFrame_gy.setVisibility(View.VISIBLE);
				mFrame_app.setVisibility(View.GONE);
				show_frame_app = false;
			}
			else {
				this.finish();
			}
		}
		else if (v.getId() == mApp_tj.getId()) {//应用推荐
			startActivity(new Intent(this, WebBaseActivity.class).putExtra("where", "app_tj"));
		}
		else if (v.getId() == mLx.getId()) {//联系  frame
			mTextEmail.setText(Html.fromHtml("<u>"+"stability.yang@outlook.com"+"</u>"));
			mTitle.setText(R.string.Contact_me_us);
			show_frame_lx = true;
			mOptions.setVisibility(View.INVISIBLE);
			mFrame_lx.setVisibility(View.VISIBLE);
		}
		else if (v.getId() == mYjfk.getId()) {//意见反馈   frame
			mTitle.setText(R.string.feedback);
			show_frame_yjfk = true;
			//显示frame
			mFrame_yjfk.setVisibility(View.VISIBLE);
		}
		else if (v.getId() == mSubmit.getId()) {//意见反馈(提交) frame
			
			mDialog = ProgressDialog.show(this, getString(R.string.feedback), getString(R.string.Submit_waiting));
			String name = mName.getText().toString().trim();
			String text = mFankui_nr.getText().toString().trim();
			String contact = mLianxi_fs.getText().toString().trim();
			if (TextUtils.isEmpty(text)) {
				mDialog.dismiss();
				toast(getString(R.string.Text_not_isnull));
				return;
			}
			
			//提交操作
			FeedBackInfo mInfos = new FeedBackInfo();
			mInfos.setDeviceId(tm.getDeviceId());
			mInfos.setName(name);
			mInfos.setText(text);
			mInfos.setContact(contact);
			mInfos.save(this, new SaveListener() {
				@Override
				public void onSuccess() {
					mDialog.dismiss();
					toast(getString(R.string.Submit_success));
					show_frame_yjfk = false;
					mTitle.setText(R.string.options);
					//隐藏frame
					mFrame_yjfk.setVisibility(View.GONE);
				}
				
				@Override
				public void onFailure(int code, final String msg) {
					mDialog.dismiss();
					toast(getString(R.string.Submit_failure)+msg);
				}
			});
			
		}
		else if (v.getId() == mSc.getId()) {//商城   暂未开通(商城推荐)
			toast(getString(R.string.Not_open));
			//startActivity(new Intent(this, WebBaseActivity.class).putExtra("where", "mall"));
		}
		else if (v.getId() == mGy.getId()) {//关于  frame 
			mTitle.setText(R.string.About);
			show_frame_gy = true;
			mOptions.setVisibility(View.INVISIBLE);
			//显示frame
			mFrame_gy.setVisibility(View.VISIBLE);
		}
		else if (v.getId() == mApp.getId()) {//关于  frame (关于...应用)
			show_frame_app = true;
			mTitle.setText(R.string.About_app);
			try {
				mSdk.setText(getString(R.string.Sdk)+getVersionName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			mTextDetailed.setText(getString(R.string.Detailed_S)+"https://github.com/CMythYang/RunAndroid");
			mFrame_gy.setVisibility(View.INVISIBLE);
			//显示frame
			mFrame_app.setVisibility(View.VISIBLE);
		}
		else if (v.getId() == mAuthor.getId()) {//关于  frame (关于...作者)
			startActivity(new Intent(this, WebBaseActivity.class).putExtra("where", "author"));
		}
		else if (v.getId() == mKfzlm.getId()) {//开发者联盟
			startActivity(new Intent(this, WebBaseActivity.class).putExtra("where", "developer"));
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (show_frame_lx == true) {
					mTitle.setText(R.string.options);
					mOptions.setVisibility(View.VISIBLE);
					mFrame_lx.setVisibility(View.GONE);
					show_frame_lx = false;
				}
				else if (show_frame_yjfk == true){
					mTitle.setText(R.string.options);
					mFrame_yjfk.setVisibility(View.GONE);
					show_frame_yjfk = false;
				}
				else if (show_frame_gy == true && show_frame_app == false){
					mOptions.setVisibility(View.VISIBLE);
					mFrame_gy.setVisibility(View.GONE);
					show_frame_gy = false;
				}
				else if (show_frame_gy == true && show_frame_app == true){
					mTitle.setText(R.string.About);
					mFrame_gy.setVisibility(View.VISIBLE);
					mFrame_app.setVisibility(View.GONE);
					show_frame_app = false;
				}
				else {
					this.finish();
				}
				return true;
			}
		}
		return false;
	}
	
}

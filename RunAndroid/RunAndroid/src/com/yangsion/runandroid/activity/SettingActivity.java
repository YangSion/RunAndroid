package com.yangsion.runandroid.activity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.yangsion.runandroid.R;
import com.yangsion.runandroid.domen.UserInfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * （个人中心）设置   Personal Setting
 * @author stabilit.yang
 *
 */
public class SettingActivity extends BaseActivity implements OnClickListener{
	public static final String TAG = "SettingActivity";
	
	private Dialog mDialog;
	private TextView mBack, mQuery_user_info;
	private Button mChange_pwd, mQuery_user, mBtn_logout;
	
	@Override
	protected void onCreate(@Nullable Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_setting);
		
		mChange_pwd = (Button) findViewById(R.id.change_pwd);
		mQuery_user = (Button) findViewById(R.id.query_user);
		mBack = (TextView) findViewById(R.id.back);
		mQuery_user_info = (TextView) findViewById(R.id.query_user_info);
		mBtn_logout = (Button) findViewById(R.id.logout);
		
		mBack.setOnClickListener(this);
		mChange_pwd.setOnClickListener(this);
		mQuery_user.setOnClickListener(this);
		mBtn_logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == mBack.getId()) {//返回
			startActivity(new Intent(this, PersonalActivity.class));
			this.finish();
		}
		else if (v.getId() == mBtn_logout.getId()) {//退出登录
			BmobUser.logOut(getBaseContext());
			this.finish();
		}
		else if (v.getId() == mChange_pwd.getId()) {//修改密码
			final EditText editText = new EditText(this);
			final EditText editText2 = new EditText(this);
			editText.setInputType(0x00000081);//textPassword
			editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});//最大字符数
			editText.setPadding(6, 0, 0, 0);
			editText.setHint(getString(R.string.Old_pwd));
			editText.setBackgroundResource(R.drawable.btn_selector5ss);
			editText2.setInputType(0x00000081);//textPassword
			editText2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});//最大字符数
			editText2.setPadding(6, 0, 0, 0);
			editText2.setBackgroundResource(R.drawable.btn_selector5ss);
			editText2.setHint(getString(R.string.New_pwd));
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(1);
			layout.addView(editText);
			layout.addView(editText2);
			mDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.Update_pwd)).setView(layout).setPositiveButton(getString(R.string.Dl_ok), new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					String message = editText.getText().toString().trim();
					String message2 = editText2.getText().toString().trim();
					if (TextUtils.isEmpty(message) || TextUtils.isEmpty(message2)) {
						toast(getString(R.string.Password_no_empty));
						return;
					}
					mDialog = ProgressDialog.show(SettingActivity.this, getString(R.string.Update_pwd), getString(R.string.Dl_waiting));
					BmobUser.updateCurrentUserPassword(SettingActivity.this, message, message2, new UpdateListener() {
						@Override
						public void onSuccess() {
							dialog.dismiss();
							mDialog.dismiss();
							toast(getString(R.string.Pwd_up_succes));
						}
						@Override
						public void onFailure(int code, String msg) {
							dialog.dismiss();
							mDialog.dismiss();
							toast(getString(R.string.Pwd_up_failure)+msg+"("+code+")");
						}
					});
				}
			}).setNegativeButton(getString(R.string.Dl_cancel), null).show();
			
		}
		else if (v.getId() == mQuery_user.getId()) {//查询用户
			final EditText editText = new EditText(this);
			editText.setPadding(6, 0, 0, 0);
			editText.setBackgroundResource(R.drawable.btn_selector5ss);
			editText.setHint(getString(R.string.Please_enter_Run));
			mDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.Query_user)).setView(editText).setPositiveButton(getString(R.string.Query), new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					String username = editText.getText().toString().trim();
					if (TextUtils.isEmpty(username)) {
						toast(getString(R.string.number_no_empty));
						return;
					}
					mDialog = ProgressDialog.show(SettingActivity.this, getString(R.string.Query_user), getString(R.string.Query_waiting));
					BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
					query.addWhereEqualTo("username", username);
					query.findObjects(SettingActivity.this, new FindListener<UserInfo>() {
						@Override
						public void onSuccess(List<UserInfo> user) {
							dialog.dismiss();
							mDialog.dismiss();
							toast(getString(R.string.Query_user_succes));
							String nickname = "";
							String hobbysignature = "";
							for (UserInfo myUser : user) {
								nickname = myUser.getNickname();
								hobbysignature = myUser.getHobbysignature();
							}
							mQuery_user_info.setText(getString(R.string.nick_mh)+nickname+"\n\n"+getString(R.string.Hobbies_signature)+"\t\t"+hobbysignature);
						}
						@Override
						public void onError(int arg0, String msg) {
							dialog.dismiss();
							mDialog.dismiss();
							toast(getString(R.string.Query_user_failure) + msg);
						}
					});
				}
			}).setNegativeButton(getString(R.string.Dl_cancel), null).show();
		}
	}

	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				//if  更多
				startActivity(new Intent(this, PersonalActivity.class));
				this.finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	protected void onPause() {//不可见设置为“”
		super.onPause();
		mQuery_user_info.setText("");
	}
	
}

package com.yangsion.runandroid.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;

import com.yangsion.runandroid.R;
import com.yangsion.runandroid.domen.UserInfo;
import com.yangsion.runandroid.util.RunningNumberUtil;
import com.yangsion.runandroid.util.SharedUtil;
import com.yangsion.runandroid.widget.CircleImageView;

/**
 * 登录和注册  Login_Register 
 * @author stabilit.yang
 *
 */
public class Login_RegisterActivity extends BaseActivity implements OnClickListener {
	public static final String TAG = "Login_RegisterActivity";
	
//	private TextView mQQ, mWeiBo, mWeiXin;
//	private UserInfo mInfo;
//	public static Tencent mTencent;
	private FrameLayout mView_login, mView_register, mView_wjpassword;
	private TextView mForget_password, mR_number;
	private Button mRegister, mLogin, mR_btn_register, mR_btn_back_login, mWj_btn_send_number;
	private EditText mL_number, mL_password, mR_email, mR_password, mWj_email;
	private CircleImageView mPick;
	
	private boolean mFregister = false;
	private boolean mFwj_password = false;

	/**
	 * 保存的账号
	 */
	private CharSequence num;

	
	
	@Override
	protected void onCreate(@Nullable Bundle bundle) {
		super.onCreate(bundle);
		UserInfo myUser = BmobUser.getCurrentUser(this, UserInfo.class);
		if (myUser != null) {//登录过
			startActivity(new Intent(this, PersonalActivity.class));
			finish();
		}
		setContentView(R.layout.activity_login_register);
		
		mView_login = (FrameLayout) findViewById(R.id.view_login);
		mView_register = (FrameLayout) findViewById(R.id.view_register);
		mView_wjpassword = (FrameLayout) findViewById(R.id.view_wjpassword);
		
		mPick = (CircleImageView) findViewById(R.id.l_avatar);
		mR_number = (TextView) findViewById(R.id.r_number);
		mForget_password = (TextView) findViewById(R.id.forget_password);
		
		mL_number = (EditText) findViewById(R.id.l_number);
		mL_password = (EditText) findViewById(R.id.l_password);
		mR_email = (EditText) findViewById(R.id.r_email);
		mR_password = (EditText) findViewById(R.id.r_password);
		mWj_email = (EditText) findViewById(R.id.wj_email);
		
		mRegister = (Button) findViewById(R.id.register);
		mLogin = (Button) findViewById(R.id.login);
		mR_btn_register = (Button) findViewById(R.id.r_btn_register);
		mR_btn_back_login = (Button) findViewById(R.id.r_btn_back_login);
		mWj_btn_send_number = (Button) findViewById(R.id.wj_btn_send_number);
		
		mForget_password.setOnClickListener(this);
		mRegister.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mR_btn_register.setOnClickListener(this);
		mR_btn_back_login.setOnClickListener(this);
		mWj_btn_send_number.setOnClickListener(this);
		
		mR_password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});//最大字符数
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (isNetWorkAvailable() == false) {
			//toast 需要联网
			toast(getString(R.string.On_network));
		}
		//获取头像缓存
		String strPick = (String) SharedUtil.getParam(this, "pick", "");
		if ("" != strPick) {
			byte[] bitmapArray = Base64.decode(strPick, Base64.DEFAULT);
			// 将字符串转换成Bitmap类型
			Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,bitmapArray.length);
			mPick.setImageBitmap(bitmap);
		}
		num = (CharSequence) SharedUtil.getParam(Login_RegisterActivity.this, "getUsername", "");
		if ("" != num) {
			mL_number.setText(num);
		}
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == mForget_password.getId()) {//忘记密码
			mView_login.setVisibility(View.GONE);
			mView_wjpassword.setVisibility(View.VISIBLE);
			mFwj_password = true;
		}
		
		else if (v.getId() == mLogin.getId()) {//登录
			login();
		}
		
		else if (v.getId() == mRegister.getId()) {//注册
			mView_login.setVisibility(View.GONE);
			mView_register.setVisibility(View.VISIBLE);
			mFregister = true;
		}
		
		else if (v.getId() == mWj_btn_send_number.getId()) {//wj 发送验证码
			resetPassword();
		}
		
		else if (v.getId() == mR_btn_register.getId()) {//r 注册
			register();
		}
		
		else if (v.getId() == mR_btn_back_login.getId()) {//r 返回登录
			mR_email.setVisibility(View.VISIBLE);
			mR_password.setVisibility(View.VISIBLE);
			mR_email.setText(null);
			mR_password.setText(null);
			mR_number.setVisibility(View.INVISIBLE);
			mView_login.setVisibility(View.VISIBLE);
			mView_register.setVisibility(View.GONE);
			mR_btn_register.setVisibility(View.VISIBLE);
			mFregister = false;
		}
	}

	
	/**
	 * 注册
	 */
	private void register() {
		
		final ProgressDialog mDialog = ProgressDialog.show(this, getString(R.string.Dialog_title_register), getString(R.string.Dialog_waiting_register));
		
		RunningNumberUtil numberUtil = new RunningNumberUtil();
		String username = String.valueOf(numberUtil.number()+numberUtil.number()+(numberUtil.number()-numberUtil.number()));
		
		final String email = mR_email.getText().toString().trim();//邮箱
		final String password = mR_password.getText().toString().trim();//密码
		
		if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
			mDialog.dismiss();
			toast(getString(R.string.Please_enter_email_pwd));
			return;
		}
		
		final UserInfo myUser = new UserInfo();
		if (username.indexOf("-") != -1) {
			myUser.setUsername(username.replace("-", "").trim());
		} else {
			myUser.setUsername(username);
		}
		myUser.setEmail(email);
		myUser.setPassword(password);
		myUser.setDeviceId(tm.getDeviceId());
		myUser.signUp(Login_RegisterActivity.this, new SaveListener() {

			@Override
			public void onSuccess() {
				mDialog.dismiss();
				mR_email.setVisibility(View.INVISIBLE);
				mR_password.setVisibility(View.INVISIBLE);
				mR_number.setVisibility(View.VISIBLE);
				mR_number.setText(getString(R.string.Run)+myUser.getUsername());
				mL_number.setText(myUser.getUsername());
				SharedUtil.setParam(Login_RegisterActivity.this, "getUsername", myUser.getUsername());
				mR_btn_register.setVisibility(View.INVISIBLE);
				toast(getString(R.string.Register_succes));
			}

			@Override
			public void onFailure(int code, String msg) {
				mDialog.dismiss();
				toast(getString(R.string.Register_failure) + msg);
			}
		});
		
	}
	
	
	
	/**
	 * 重置密码
	 */
	private void resetPassword() {
		final ProgressDialog mDialog = ProgressDialog.show(this, getString(R.string.Dialog_title_reset), getString(R.string.Dialog_waiting_reset));
		final String email = mWj_email.getText().toString().trim(); 
		if (TextUtils.isEmpty(email)) {
			mDialog.dismiss();
			toast(getString(R.string.Please_enter_email));
			return;
		}
		BmobUser.resetPasswordByEmail(this, email, new ResetPasswordByEmailListener() {

			@Override
			public void onSuccess() {
				mDialog.dismiss();
				toast(getString(R.string.Reset_succes_please_login) + email + getString(R.string.Email_runing_verify_reset));
				mWj_email.setText(null);
				if (email.indexOf("qq") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemail_qq"));
				}
				else if (email.indexOf("163") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_163"));
				}
				else if (email.indexOf("126") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_126"));
				}
				else if (email.indexOf("139") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_10086"));
				}
				else if (email.indexOf("hotmail") != -1 || email.indexOf("outlook") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_live"));
				}
				else if (email.indexOf("google") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_google"));
				}
				else if (email.indexOf("foxmail") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_foxmail"));
				}
				else if (email.indexOf("eyou") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_eyou"));
				}
				else if (email.indexOf("yeah") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_yeah"));
				}
				else if (email.indexOf("sina") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_sina"));
				}
				else if (email.indexOf("sohu") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_sohu"));
				}
				else if (email.indexOf("tom") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_tom"));
				}
				else if (email.indexOf("aliyun") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_aliyun"));
				}
				else if (email.indexOf("yahoo") != -1) {
					startActivity(new Intent(Login_RegisterActivity.this, WebBaseActivity.class).putExtra("where", "lemailq_yahoo"));
				} 
				Login_RegisterActivity.this.finish();
			}

			@Override
			public void onFailure(int code, String e) {
				mDialog.dismiss();
				toast(getString(R.string.Reset_pwd_Please_failure) + e);
			}
		});
		
		
	}

	
	/**
	 * 登录
	 */
	private void login() {
		
		final ProgressDialog mDialog = ProgressDialog.show(this, getString(R.string.Dialog_title_login), getString(R.string.Dialog_waiting_login));
		
		String number = mL_number.getText().toString().trim();
		String password = mL_password.getText().toString().trim();
		if (TextUtils.isEmpty(number) || TextUtils.isEmpty(password)) {
			mDialog.dismiss();
			toast(getString(R.string.Please_enter_email_run_pwd));
			return;
		}
		
		if (number.indexOf("@") != -1 || number.indexOf(".com") != -1) {
			BmobUser.loginByAccount(this, number, password, new LogInListener<UserInfo>() {
				
				@Override
				public void done(UserInfo user, BmobException e) {
					if(user != null){
						mDialog.dismiss();
						SharedUtil.setParam(Login_RegisterActivity.this, "getUsername", user.getEmail());
						toast(getString(R.string.Login_succes));
						//到个人中心获取个人信息
						startActivity(new Intent(Login_RegisterActivity.this, PersonalActivity.class));
						Login_RegisterActivity.this.finish();
						return;
					}
					else {
						mDialog.dismiss();
						toast(getString(R.string.Login_failure)+e);
						return;
					}
				}
			});
		}
		else {
			final BmobUser bu2 = new BmobUser();
			bu2.setUsername(number);
			bu2.setPassword(password);
			bu2.login(this, new SaveListener() {
				
				@Override
				public void onSuccess() {
					mDialog.dismiss();
					SharedUtil.setParam(Login_RegisterActivity.this, "getUsername", bu2.getUsername());
					toast(getString(R.string.Login_succes));
					//到个人中心获取个人信息
					startActivity(new Intent(Login_RegisterActivity.this, PersonalActivity.class));
					Login_RegisterActivity.this.finish();
				}
				
				@Override
				public void onFailure(int code, String msg) {
					mDialog.dismiss();
					toast(getString(R.string.Login_failure) + code+","+msg);
				}
			});
		}
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (mFwj_password != false || mFregister != false) {
					mView_login.setVisibility(View.VISIBLE);
					mView_register.setVisibility(View.GONE);
					mView_wjpassword.setVisibility(View.GONE);
					mR_email.setText(null);
					mR_password.setText(null);
					mWj_email.setText(null);
					mFwj_password = false;
					mFregister = false;
					return true;
				}
				finish();
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
//	private Handler mHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			if (msg.what == 0) {
//				JSONObject response = (JSONObject) msg.obj;
//				if (response.has("nickname")) {
//					try {
//						//设置呢称
////						mUserInfo.setVisibility(android.view.View.VISIBLE);
////						mUserInfo.setText(response.getString("nickname"));
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}else if(msg.what == 1){
//				//设置头像
//				Bitmap bitmap = (Bitmap)msg.obj;
////				mUserLogo.setImageBitmap(bitmap);
////				mUserLogo.setVisibility(android.view.View.VISIBLE);
//			}
//		}
//
//	};
//	
//	@Override
//	protected void onCreate(@Nullable Bundle bundle) {
//		super.onCreate(bundle);
//		setContentView(R.layout.activity_login_register);
//		// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
//		// 其中APP_ID是分配给第三方应用的appid，类型为String。
//		mTencent = Tencent.createInstance("appId", this.getApplicationContext());
//		// 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
//		initView();
//	}
//	
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == Constants.REQUEST_LOGIN ||
//		    	requestCode == Constants.REQUEST_APPBAR) {
//		    	Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
//		    }
//
//		    super.onActivityResult(requestCode, resultCode, data);
//	}
//	
//	
//	private void initView() {
//		
//		mQQ = (TextView) findViewById(R.id.qq);
//		mWeiBo = (TextView) findViewById(R.id.weibo);
//		mWeiXin = (TextView) findViewById(R.id.weixin);
//		
//		mQQ.setOnClickListener(this);
//		mWeiBo.setOnClickListener(this);
//		mWeiXin.setOnClickListener(this);
//		
//	}
//
//	/**
//	 * QQ 获取OpenidAndToken
//	 * @param jsonObject
//	 */
//	public static void initOpenidAndToken(JSONObject jsonObject) {
//        try {
//            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
//            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
//            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
//            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
//                    && !TextUtils.isEmpty(openId)) {
//                mTencent.setAccessToken(token, expires);
//                mTencent.setOpenId(openId);
//            }
//        } catch(Exception e) {
//        }
//    }
//	
//	/**
//	 * QQ监听
//	 */
//	private IUiListener loginListener = new BaseUiListener() {
//        @Override
//        protected void doComplete(JSONObject values) {
//            initOpenidAndToken(values);
//            updateUserInfo();
//        }
//    };
//
//    /**
//     * QQ回调接口实现
//     * @author stabilit.yang
//     *
//     */
//	private class BaseUiListener implements IUiListener {
//	
//		@Override
//		public void onError(UiError arg0) {
//			Log.v(TAG, arg0.errorMessage);
//			
//		}
//		
//		@Override
//		public void onComplete(Object arg0) {
//			Log.v(TAG, arg0.toString());
//			doComplete((JSONObject)arg0);
//		}
//		
//		protected void doComplete(JSONObject values) {
//		}
//		
//		@Override
//		public void onCancel() {
//			Log.v(TAG, "onCancel");
//		}
//	}
//
//	/**
//	 * 获取QQ信息  头像，呢称。。。
//	 */
//	private void updateUserInfo() {
//		
//		if (mTencent != null && mTencent.isSessionValid()) {
//			IUiListener listener = new IUiListener() {
//
//				@Override
//				public void onError(UiError e) {
//
//				}
//
//				@Override
//				public void onComplete(final Object response) {
//					Message msg = new Message();
//					msg.obj = response;
//					msg.what = 0;
//					mHandler.sendMessage(msg);
//					new Thread(){
//
//						@Override
//						public void run() {
//							JSONObject json = (JSONObject)response;
//							if(json.has("figureurl")){
//								Bitmap bitmap = null;
//								try {
//									bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
//								} catch (JSONException e) {
//
//								}
//								Message msg = new Message();
//								msg.obj = bitmap;
//								msg.what = 1;
//								mHandler.sendMessage(msg);
//							}
//						}
//
//					}.start();
//				}
//
//				@Override
//				public void onCancel() {
//
//				}
//			};
//			mInfo = new UserInfo(this, mTencent.getQQToken());
//			mInfo.getUserInfo(listener);
//
//		} else {
//			//获取失败  不显示
////			mUserInfo.setText("");
////			mUserInfo.setVisibility(android.view.View.GONE);
////			mUserLogo.setVisibility(android.view.View.GONE);
//		}
//	}
//	
//	@Override
//	public void onClick(View v) {
//		if (v.getId() == mQQ.getId()) {//选择QQ登录
//			if (!mTencent.isSessionValid())
//			{
//				mTencent.login(this, "all", loginListener);
//			}
//			mTencent.logout(this);
//		}
//		
//	}
//	
	
}

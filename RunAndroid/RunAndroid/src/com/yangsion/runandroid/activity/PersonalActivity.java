package com.yangsion.runandroid.activity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

import com.yangsion.runandroid.R;
import com.yangsion.runandroid.domen.UserInfo;
import com.yangsion.runandroid.util.Base64Util;
import com.yangsion.runandroid.util.SharedUtil;
import com.yangsion.runandroid.widget.CircleImageView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 个人中心  Personal center 
 * @author stabilit.yang
 *
 */
public class PersonalActivity extends BaseActivity implements OnClickListener {
	public static final String TAG = "PersonalActivity";
	
	private static final int REQUESTCODE_PICK = 1;
	private static final int REQUESTCODE_CUTTING = 2;
	
	private Dialog mDialog;
	
	private TextView mNick, mBack, mSetting, mPerso, mUsername, mSex, mEmail_status, mTime;
	private CircleImageView mPick;
	private RelativeLayout mBtn_sex, mBtn_email_status, mFrame_showimg;
	private Button mBtn_update_nick, mBtn_update_pick;
	private ImageView mShowimg;
	
	private boolean show_img_frame = false;
	private boolean sex;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_personal);
		
		mBack = (TextView) findViewById(R.id.back);
		mSetting = (TextView) findViewById(R.id.setting);
		mPick = (CircleImageView) findViewById(R.id.pick);
		mNick = (TextView) findViewById(R.id.nick);
		mPerso = (TextView) findViewById(R.id.perso);
		mUsername = (TextView) findViewById(R.id.username);
		mSex = (TextView) findViewById(R.id.sex);
		mEmail_status = (TextView) findViewById(R.id.email_status);
		mTime = (TextView) findViewById(R.id.time);
		mBtn_sex = (RelativeLayout) findViewById(R.id.btn_sex);
		mBtn_email_status = (RelativeLayout) findViewById(R.id.btn_email_status);
		mBtn_update_nick = (Button) findViewById(R.id.update_nick);
		mBtn_update_pick = (Button) findViewById(R.id.update_pick);
		mFrame_showimg = (RelativeLayout) findViewById(R.id.frame_showimg);
		mShowimg = (ImageView) findViewById(R.id.showimg);
		
		mBack.setOnClickListener(this);
		mSetting.setOnClickListener(this);
		mPick.setOnClickListener(this);
		mBtn_sex.setOnClickListener(this);
		mBtn_email_status.setOnClickListener(this);
		mPerso.setOnClickListener(this);
		mBtn_update_nick.setOnClickListener(this);
		mBtn_update_pick.setOnClickListener(this);
		mFrame_showimg.setOnClickListener(this);
		
		getCurrentUser();
	}
	
	
	/**
	 * 获取用户信息
	 * @
	 * MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
	 *		if (myUser != null) {
	 *			Log.i("life","本地用户信息:objectId = " + myUser.getObjectId() + ",name = " + myUser.getUsername()
	 *					+ ",age = "+ myUser.getAge());
	 *		} else {
	 *			toast("本地用户为null,请登录。");
	 *		}
	 */
	private void getCurrentUser() {
		
		//V3.4.5版本新增加getObjectByKey方法获取本地用户对象中某一列的值
		JSONArray pickfile = (JSONArray) BmobUser.getObjectByKey(this, "pickfile");
		String nickname = (String) BmobUser.getObjectByKey(this, "nickname");
		String username = (String) BmobUser.getObjectByKey(this, "username");
		String time = (String) BmobUser.getObjectByKey(this, "time");
		String hobbysignature = (String) BmobUser.getObjectByKey(this, "hobbysignature");
		Boolean sex = (Boolean) BmobUser.getObjectByKey(this, "sex");
		Boolean emailstatus = (Boolean) BmobUser.getObjectByKey(this, "emailVerified");
		Log.i("bmob", "nickname："+nickname+",\nusername："+username+",\nsex："+ sex+",\nemailstatus："+emailstatus+",\nhobbysignature："+hobbysignature+",\npickfile："+pickfile);
		
		//------------------------pickfile---------------------------
		if (pickfile != null) {
			Bitmap bitmap = Base64Util.getBase64(pickfile);
	    	mPick.setImageBitmap(bitmap);
			mShowimg.setImageBitmap(bitmap);
			
			//保存到登录界面显示
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
					SharedUtil.setParam(this, "pick", pick);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else {
			mPick.setImageResource(R.drawable.pick);
			mShowimg.setImageResource(R.drawable.pick);
		}
		//---------------------------nickname------------------------
		if (nickname != null) {
			mNick.setText(nickname);
		}
		else {
			mNick.setText(username);
		}
		//----------------------------username-----------------------
		if (username != null) {
			mUsername.setText(username);
		}
		//------------------------time---------------------------
		if (time != null) {
			mTime.setText(time);
		}
		else {
			mTime.setText("");
		}
		//-------------------------hobbysignature--------------------------
		if (hobbysignature != null) {
			mPerso.setText(hobbysignature);
		}
		//----------------------sex-----------------------------
		if (sex != null) {
			if (sex == true) {
				mSex.setText(getString(R.string.Sex_woman));
			}
			else {
				mSex.setText(getString(R.string.Sex_man));
			}
		}
		//----------------------emailstatus-----------------------------
		if (emailstatus != null) {
			if (emailstatus == true) {
				mEmail_status.setText(getString(R.string.Already_bingding));
				mBtn_email_status.setClickable(false);
			}
			else {
				mEmail_status.setText(getString(R.string.Not_bingding));
			}
		}
		
	}
	
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		if (v.getId() == mBack.getId()) {//返回
			this.finish();
		}
		else if (v.getId() == mSetting.getId()) {//设置
			startActivity(new Intent(this, SettingActivity.class));
			this.finish();
		}
		else if (v.getId() == mPick.getId()) {//（头像）显示原图
			show_img_frame = true;
			mFrame_showimg.setVisibility(View.VISIBLE);
		}
		else if (v.getId() == mFrame_showimg.getId()) {//（头像）frame
			show_img_frame = false;
			mFrame_showimg.setVisibility(View.GONE);
		}
		else if (v.getId() == mBtn_sex.getId()) {//sex 显示 dialog
			sex = false;
			mDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.Sex_selection)).setItems(new String[]{getString(R.string.Sex_man), getString(R.string.Sex_woman)}, new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						mSex.setText(getString(R.string.Sex_man));
						sex = false;
						// 发送上传sex：男 请求 
						updateUserSex(dialog,sex);
						
					} else if (which == 1) {
						mSex.setText(getString(R.string.Sex_woman));
						sex = true;
						// 发送上传sex：女 请求 
						updateUserSex(dialog,sex);
					}
				}
			}).show();
		}
		else if (v.getId() == mBtn_email_status.getId()) {//是否绑定邮箱
			SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.Year_Month_Day));       
			Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
			final String time = formatter.format(curDate);
			
			mDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.Bingding_email)).setPositiveButton(getString(R.string.Dl_ok), new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					// 发送邮箱验证请求
					final UserInfo bmobUser = BmobUser.getCurrentUser(PersonalActivity.this, UserInfo.class);
					if (bmobUser != null) {
						UserInfo newUser = new UserInfo();
						//number类型
						newUser.setEmailVerified(true);
						newUser.setTime(time);
						newUser.update(PersonalActivity.this,bmobUser.getObjectId(),new UpdateListener() {
							
							@Override
							public void onSuccess() {
								dialog.dismiss();
								mTime.setText(time);
								mEmail_status.setText(getString(R.string.Already_bingding));
								toast(getString(R.string.Bingding_email_succes));
								//本地的用户信息均已更新成功，可在此调用getCurrentUser方法来获取最新的用户信息
								getCurrentUser();
							}
							
							@Override
							public void onFailure(int code, String msg) {
								dialog.dismiss();
								toast(getString(R.string.Bingding_email_failure)+msg.toString());
							}
						});
					} else {
						toast(getString(R.string.Not_local_user_please_login));
					}
				}
			}).setNegativeButton(getString(R.string.Dl_cancel), null).show();
		}
		else if (v.getId() == mPerso.getId()) {//爱好，签名
			final EditText editText = new EditText(this);
			editText.setPadding(6, 0, 0, 0);
			editText.setBackgroundResource(R.drawable.btn_selector5ss);
			editText.setHint(getString(R.string.Please_enter));
			mDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.Hobbies_signature)).setView(editText).setPositiveButton(getString(R.string.Save), new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String message = editText.getText().toString().trim();
					if (TextUtils.isEmpty(message)) {
						toast(getString(R.string.Not_enter_text));
						return;
					}
					mPerso.setText(message);
					// 发送邮箱验证请求 dialog.dismiss(); setHobby
					updateHobbiesSignature(dialog, message);
				}
			}).setNegativeButton(getString(R.string.Dl_cancel), null).show();
		}
		else if (v.getId() == mBtn_update_nick.getId()) {//上传呢称 
			
			final EditText editText = new EditText(this);
			editText.setHint(R.string.Hint_nickname);
			editText.setPadding(6, 0, 0, 0);
			editText.setBackgroundResource(R.drawable.btn_selector5ss);
			editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});//最大字符数
			new AlertDialog.Builder(this).setTitle(R.string.Setting_nickname).setView(editText)
					.setPositiveButton(R.string.Dl_ok, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String nickString = editText.getText().toString();
							if (TextUtils.isEmpty(nickString)) {
								toast(getString(R.string.Toast_nick_not_isnull));
								return;
							}
							updateRemoteNick(nickString);
						}
					}).setNegativeButton(R.string.Dl_cancel, null).show();
		}
		else if (v.getId() == mBtn_update_pick.getId()) {//上传头像
			uploadHeadPhoto();
		}
		
		
		
	}
	
	
	/**
	 * 更换呢称
	 * @param nickName
	 */
	private void updateRemoteNick(final String nickName) {
		mDialog = ProgressDialog.show(this, getString(R.string.Dl_update_nick), getString(R.string.Dl_waiting));
		final UserInfo bmobUser = BmobUser.getCurrentUser(this, UserInfo.class);
		if (bmobUser != null) {
			UserInfo newUser = new UserInfo();
			//number类型
			newUser.setNickname(nickName);
			newUser.update(this,bmobUser.getObjectId(),new UpdateListener() {
				
				@Override
				public void onSuccess() {
					mNick.setText(nickName);
					mDialog.dismiss();
					toast(getString(R.string.Toast_update_success));
					//本地的用户信息均已更新成功，可在此调用getCurrentUser方法来获取最新的用户信息
					getCurrentUser();
				}
				
				@Override
				public void onFailure(int code, String msg) {
					mDialog.dismiss();
					toast(getString(R.string.Toast_update_failure)+"\n"+msg.toString());
				}
			});
		} else {
			toast(getString(R.string.Not_local_user_please_login));
		}
	}
	
	
	
	/**
	 * 更换头像
	 */
	private void uploadHeadPhoto() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.Dl_update_pick);
		builder.setItems(
				new String[] { getString(R.string.Dl_msg_local_upload)},
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:
							Intent pickIntent = new Intent(Intent.ACTION_PICK,null);
							pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
							startActivityForResult(pickIntent, REQUESTCODE_PICK);
							break;
						default:
							break;
						}
					}
				});
		builder.create().show();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUESTCODE_PICK:
			if (data == null || data.getData() == null) {
				return;
			}
			startPhotoZoom(data.getData());
			break;
		case REQUESTCODE_CUTTING:
			if (data != null) {
				setPicToView(data);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, REQUESTCODE_CUTTING);
	}

	/**
	 * save the picture data
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			BitmapDrawable drawable = new BitmapDrawable(getResources(), photo);
			/*
			 * headAvatar.setImageBitmap(photo); ByteArrayOutputStream baos =
			 * new ByteArrayOutputStream();
			 * photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); byte[]
			 * byteArray = baos.toByteArray();
			 */
			uploadUserAvatar(Bitmap2Bytes(photo), drawable);
		}

	}
	
	
	
	private void uploadUserAvatar(final byte[] data, final BitmapDrawable drawable) {
		mDialog = ProgressDialog.show(this, getString(R.string.Dl_update_pick), getString(R.string.Dl_waiting));
		final UserInfo bmobUser = BmobUser.getCurrentUser(this, UserInfo.class);
		if (bmobUser != null) {
			UserInfo newUser = new UserInfo();
			//number类型
			newUser.setPickfile(Base64Util.setBase64(data));
			newUser.update(this,bmobUser.getObjectId(),new UpdateListener() {
				
				@Override
				public void onSuccess() {
					mPick.setImageDrawable(drawable);
					mDialog.dismiss();
					toast(getString(R.string.Toast_update_success));
					//本地的用户信息均已更新成功，可在此调用getCurrentUser方法来获取最新的用户信息
					getCurrentUser();
				}
				
				@Override
				public void onFailure(int code, String msg) {
					mDialog.dismiss();
					toast(getString(R.string.Toast_update_failure)+"\n"+msg.toString());
				}
			});
		} else {
			toast(getString(R.string.Not_local_user_please_login));
		}
	}


	public byte[] Bitmap2Bytes(Bitmap bm){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	/**
	 * 更新用户操作并同步更新本地的用户信息(UserSex)
	 */
	private void updateUserSex(final DialogInterface dialog, boolean sex) {
		final UserInfo bmobUser = BmobUser.getCurrentUser(this, UserInfo.class);
		if (bmobUser != null) {
			UserInfo newUser = new UserInfo();
			//number类型
			newUser.setSex(sex);//true为女人，别问我为什么，因为女人说什么都是对的

			newUser.update(this,bmobUser.getObjectId(),new UpdateListener() {

				@Override
				public void onSuccess() {
					dialog.dismiss();
					toast(getString(R.string.Update_sex_succes));
					//本地的用户信息均已更新成功，可在此调用getCurrentUser方法来获取最新的用户信息
					getCurrentUser();
				}

				@Override
				public void onFailure(int code, String msg) {
					dialog.dismiss();
					toast(getString(R.string.Update_sex_failure) + msg);
				}
			});
		} else {
			toast(getString(R.string.Not_local_user_please_login));
		}
	}	
	/**
	 * 更新用户操作并同步更新本地的用户信息(Hobbies_signature)
	 */
	private void updateHobbiesSignature(final DialogInterface dialog, String message) {
		final UserInfo bmobUser = BmobUser.getCurrentUser(this, UserInfo.class);
		if (bmobUser != null) {
			UserInfo newUser = new UserInfo();
			//number类型
			newUser.setHobbysignature(message);
			
			newUser.update(this,bmobUser.getObjectId(),new UpdateListener() {
				
				@Override
				public void onSuccess() {
					dialog.dismiss();
					toast(getString(R.string.Update_Hobbies_signature_succes));
					//本地的用户信息均已更新成功，可在此调用getCurrentUser方法来获取最新的用户信息
					getCurrentUser();
				}
				
				@Override
				public void onFailure(int code, String msg) {
					dialog.dismiss();
					toast(getString(R.string.Update_Hobbies_signature_failure) + msg);
				}
			});
		} else {
			toast(getString(R.string.Not_local_user_please_login));
		}
	}	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				
				if (show_img_frame == true) {
					mFrame_showimg.setVisibility(View.GONE);
					show_img_frame = false;
				} 
				else {
					PersonalActivity.this.finish();
				}
				
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	
}

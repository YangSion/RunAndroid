package com.yangsion.runandroid.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangsion.runandroid.R;
import com.yangsion.runandroid.widget.ProgressWebView;
/**
 * WebBase activity
 * @author stabilit.yang
 *
 */
public class WebBaseActivity extends BaseActivity implements OnClickListener{
	public static final String TAG = "WebBaseActivity";
	
	public static final String EMAIL = "email";
	public static final String LEMAIL = "lemail";
	
	private TextView mTitle, mBack3, mTv_title; 
	private EditText mLink; 
	private Button mOpen; 
	private LinearLayout mLl_open;
	private ProgressWebView mWebView;
	private String where; 
	
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_web_base);
		
		
		mBack3 = (TextView) findViewById(R.id.back3);
		mTv_title = (TextView) findViewById(R.id.tv_title);
		mLl_open = (LinearLayout) findViewById(R.id.ll_open);
		mLink = (EditText) findViewById(R.id.link);
		mOpen = (Button) findViewById(R.id.open);
		mTitle = (TextView) findViewById(R.id.tv_title);
		mWebView = (ProgressWebView) findViewById(R.id.webview);
		
		mBack3.setOnClickListener(this);
		mOpen.setOnClickListener(this);
		where = getIntent().getStringExtra("where");
		
		openEmailUrl();
	}


	/**
	 * 链接
	 */
	private void openEmailUrl() {
		
		if (where.indexOf(EMAIL) != -1) {//是邮箱web
			mTv_title.setText(R.string.Email);
			emailWebStting();
			
			if (where.indexOf("qq") != -1) {
				// 改为嵌入的web页面链接
				mWebView.loadUrl("https://mail.qq.com/");
			}
			else if (where.indexOf("163") != -1) {
				mWebView.loadUrl("http://mail.163.com/");
			}
			else if (where.indexOf("126") != -1) {
				mWebView.loadUrl("http://www.126.com/");
			}
			else if (where.indexOf("139") != -1) {
				mWebView.loadUrl("http://mail.10086.com/");
			}
			else if (where.indexOf("hotmail") != -1 || where.indexOf("outlook") != -1) {
				mWebView.loadUrl("https://login.live.com/");
			}
			else if (where.indexOf("google") != -1) {
				mWebView.loadUrl("http://gmail.google.com/");
			}
			else if (where.indexOf("foxmail") != -1) {
				mWebView.loadUrl("http://www.foxmail.com/");
			}
			else if (where.indexOf("eyou") != -1) {
				mWebView.loadUrl("http://eyou.com/");
			}
			else if (where.indexOf("yeah") != -1) {
				mWebView.loadUrl("http://www.yeah.net/");
			}
			else if (where.indexOf("sina") != -1) {
				mWebView.loadUrl("http://mail.sina.com.cn/");
			}
			else if (where.indexOf("sohu") != -1) {
				mWebView.loadUrl("http://mail.sohu.com/fe/#/login");
			}
			else if (where.indexOf("tom") != -1) {
				mWebView.loadUrl("http://web.mail.tom.com/webmail/login/index.action");
			}
			else if (where.indexOf("aliyun") != -1) {
				mWebView.loadUrl("https://mail.aliyun.com/");
			}
			else if (where.indexOf("yahoo") != -1) {
				mWebView.loadUrl("https://login.yahoo.com/");
			} 
		}
		else if (where.indexOf("app_tj") != -1) {
			mTv_title.setText(R.string.App_recommended);
			mWebView.loadUrl("http://www.anzhi.com/soft_1781867.html");
		}
		else if (where.indexOf("mall") != -1) {//暂未开通(商城推荐)
			//mWebView.loadUrl("");
		}
		else if (where.indexOf("author") != -1) {
			mTv_title.setText(R.string.About_author);
			mWebView.loadUrl("http://weibo.com/CMythYangSion");
		}
		else if (where.indexOf("developer") != -1) {
			mTv_title.setText(R.string.The_developer_alliance);
			mWebView.loadUrl("https://www.oschina.net/project");
		}
		
		   	openAction();
	}



	/**
	 * email页面设置
	 */
	private void emailWebStting() {
		mTitle.setText(R.string.Email);
		mLl_open.setVisibility(View.VISIBLE);
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(R.string.wx_ts);
		dialog.setMessage(R.string.Dialog_message);
		//View.OnClickListener与aDialogInterface.OnClickListenerch冲突,带上全路径名
		dialog.setPositiveButton(R.string.Dl_ok, new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});	
		dialog.show();
	}


	/**
	 * 打开操作
	 */
	private void openAction() {
		// 不使用默认滚动条
		// mWebView.setVerticalScrollBarEnabled(false);
		// mWebView.setHorizontalScrollBarEnabled(false);
		// 支持“viewport”HTML meta标记或应该使用视窗。
		mWebView.getSettings().setUseWideViewPort(true);
		// 加载页概述模式
		mWebView.getSettings().setLoadWithOverviewMode(true);
		// 自定义焦点设置
		mWebView.requestFocus();
		// 设置可以访问文件
		mWebView.getSettings().setAllowFileAccess(true);
		// 执行JS代码
		mWebView.getSettings().setJavaScriptEnabled(true);
		// 支持屏幕缩放控件和手势缩放
		mWebView.getSettings().setSupportZoom(true);
		// 设置支持缩放
		mWebView.getSettings().setBuiltInZoomControls(true);
		// 不使用缓存
		// mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		// // 设置启用数据库存储
		// mWebView.getSettings().setDatabaseEnabled(true);
		// // 设置启用DOM storage
		// mWebView.getSettings().setDomStorageEnabled(true);
		// /*
		// * 设置启用地理定位 为了使地理定位API WebView中可用的页面,必须满足下列条件
		// * 1.权限:
		// * android.Manifest.permission.ACCESS_COARSE_LOCATION,
		// * android.Manifest.permission.ACCESS_FINE_LOCATION;
		// * 2.必须提供WebChromeClient的实现
		// */
		// mWebView.getSettings().setGeolocationEnabled(true);
		// 是下载链接,可以下载
		mWebView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				if (url != null && url.startsWith("http://"))
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			}
		});
		
		
		// 覆盖手机返回键指向web的上一个历史页面
		mWebView.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					
					if ((keyCode == KeyEvent.KEYCODE_BACK)&& mWebView.canGoBack()) {
						//mWebView.goBack();
						if (where.indexOf(LEMAIL) != -1) {
							startActivity(new Intent(WebBaseActivity.this, Login_RegisterActivity.class));
						}
						WebBaseActivity.this.finish();
						return true;
					}
				}
				return false;
			}
		});
		
		
		/*
		 * 调用js方法并传递数据
		 */
		mWebView.addJavascriptInterface(this, "this");
		// mWebView.loadData("", "text/html", null);
		// mWebView.loadUrl("javascript:alert(injectedObject.toString())");

		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
	}
	
	

	@Override
	public void onClick(View v) {
		
		if (v.getId() == mOpen.getId()) {//链接打开
			mWebView.loadUrl(mLink.getText().toString().trim());
			openAction();
		}
		
		else if (v.getId() == mBack3.getId()) {//邮箱网页
			if (where.indexOf(LEMAIL) != -1) {
				startActivity(new Intent(WebBaseActivity.this, Login_RegisterActivity.class));
			}
			WebBaseActivity.this.finish();
		}
		
	}
	
}

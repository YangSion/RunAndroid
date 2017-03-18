package com.yangsion.runandroid.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * @copy 带进度条的WebView
 */
public class ProgressWebView extends WebView {

	 private ProgressBar progressbar;

		@SuppressLint("RtlHardcoded")
		@SuppressWarnings("deprecation")
		public ProgressWebView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
	        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3, 0, 0));
	        ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.GREEN), Gravity.LEFT, ClipDrawable.HORIZONTAL);
	        progressbar.setProgressDrawable(d);
	        addView(progressbar);
	        //setWebViewClient(new WebViewClient(){});
	        setWebChromeClient(new WebChromeClient());
	    }

	    public class WebChromeClient extends android.webkit.WebChromeClient {
	        @Override
	        public void onProgressChanged(WebView view, int newProgress) {
	            if (newProgress == 100) {
	                progressbar.setVisibility(GONE);
	            } else {
	                if (progressbar.getVisibility() == GONE)
	                    progressbar.setVisibility(VISIBLE);
	                progressbar.setProgress(newProgress);
	            }
	            super.onProgressChanged(view, newProgress);
	        }

	    }

	    @SuppressWarnings("deprecation")
		@Override
	    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
	        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
	        lp.x = l;
	        lp.y = t;
	        progressbar.setLayoutParams(lp);
	        super.onScrollChanged(l, t, oldl, oldt);
	    }

}

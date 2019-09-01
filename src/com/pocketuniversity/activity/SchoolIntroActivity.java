package com.pocketuniversity.activity;

import com.example.pocketuniversity.R;
import com.pocketuniversity.bean.Notice;
import com.pocketuniversity.other.activity.NoticeDetailActivity;
import com.pocketuniversity.view.HeaderLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ProgressBar;

public class SchoolIntroActivity extends BaseActivity{
	
	public static String URL = "http://114.215.81.194:8187/XZMCIntroduction.aspx";
	private WebView webView;
	private ProgressBar load_pro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_intro);
		initView();
		setData();
	}
	private void initView(){
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SchoolIntroActivity.this.finish();
			}
		});
		headerLayout.showTitle("徐州医学院简介");
		webView = (WebView) findViewById(R.id.webView);
		setWebSeeting(webView);
		load_pro = (ProgressBar) findViewById(R.id.load_pro);
		WebSettings settings = webView.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setLoadWithOverviewMode(true);
	}
	@SuppressLint("SetJavaScriptEnabled") 
	public void setWebSeeting(final WebView webView) {
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					// 加载完毕，关闭进度条
					load_pro.setVisibility(View.GONE);
				} else {
					load_pro.setVisibility(View.VISIBLE);
					// 网页正在加载，显示进度框
					load_pro.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}
		});
	}

	public void clearWebView(WebView webView) {
		if (webView != null) {
			webView.stopLoading();
			webView.removeAllViews();
			webView.loadUrl("");
			webView.destroy();
		}
	}

	public boolean setData() {
		
		webView.loadUrl(URL);
		
		return true;
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearWebView(webView);
	}


}

package com.pocketuniversity.other.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.base.App;
import com.pocketuniversity.view.HeaderLayout;

public class BanliprocessDetailActivity extends BaseActivity {

	public static String url_address = "http://114.215.81.194:8187/web-handle-visit-clean.aspx?nid=";
	private WebView webView;
	private ProgressBar load_pro;
	private String processId;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_banli_detail);
		initView();
		if (!setData()) {
			Toast.makeText(getApplicationContext(), "无数据", Toast.LENGTH_SHORT)
					.show();
			return;
		}
	}

	private void initView() {
		Intent intent = getIntent();
		processId = intent.getStringExtra("processId");
		title = intent.getStringExtra("processName");
		load_pro = (ProgressBar) findViewById(R.id.load_pro);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		webView = (WebView) findViewById(R.id.webView);
		setWebSeeting(webView);
		headerLayout.showTitle("详情");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BanliprocessDetailActivity.this.finish();
			}
		});
		headerLayout.showRightTextButton("刷新", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				webView.reload();
			}
		});
		WebSettings settings = webView.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setLoadWithOverviewMode(true);
	}

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
		String url = url_address + processId;
		webView.loadUrl(url);
		headerLayout.showTitle(title);
		System.out.println(title+",,," + processId);
		return true;
	}
}

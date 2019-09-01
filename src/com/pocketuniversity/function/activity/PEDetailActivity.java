package com.pocketuniversity.function.activity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.bean.PE;
import com.pocketuniversity.utils.FileUtil;
import com.pocketuniversity.view.HeaderLayout;

@SuppressLint("SetJavaScriptEnabled") public class PEDetailActivity extends BaseActivity implements OnTouchListener{

	private WebView webView;
	private float startX;
	private float startY;
	private ProgressBar load_pro;
	PE pe;
	String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pe_detail);
		Intent intent = getIntent();
		pe = (PE) intent.getSerializableExtra("post");
		initView();
	}
	
	@SuppressLint("SetJavaScriptEnabled") 
	private void initView(){
		load_pro = (ProgressBar) findViewById(R.id.load_pro);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PEDetailActivity.this.finish();
			}
		});
		String title = pe.getTitle();
		headerLayout.showTitle(title);
		url = pe.getUrl();
		System.out.println("url:"+url);
		webView = (WebView)findViewById(R.id.news_details_webview);
		
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		settingWebView(webView);
		webView.loadUrl(url);
		webView.setOnTouchListener(this);
	}
	
	private void settingWebView(final WebView webView){
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

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			startY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			float endX = event.getX();
			float endY = event.getY();
			if(Math.abs(endX-startX)>150&&((endY-startY)==0||Math.abs((endX-startX)/(endY-startY))>2)){
				finish();
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	final class MyWebViewClient extends WebViewClient{ 
		public int count;
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {  
			view.loadUrl(url);  
			return true;  
		} 
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}   
		@Override
		public void onPageFinished(WebView view, String url) {
//			view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
//					"document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			super.onPageFinished(view, url);
		}
	}

	public void clearWebView(WebView webView) {
		if (webView != null) {
			webView.stopLoading();
			webView.removeAllViews();
			webView.loadUrl("");
			webView.destroy();
		}
	}
}

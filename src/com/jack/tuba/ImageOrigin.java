package com.jack.tuba;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
/**
 * 图片来源地址
 * 用webView展示
 * @author Administrator
 *
 */
public class ImageOrigin extends Activity{
	/**
	 * webView进度条
	 */
	private ProgressBar pb;
	
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_origin);
		initWebView();
		initProgressBar();
		initData();
		
		
		
	}

	private void initData() {
		// TODO Auto-generated method stub
		String url=getIntent().getStringExtra("origin_url");
		mWebView.loadUrl(url);
	}

	private void initProgressBar() {
		pb = (ProgressBar) findViewById(R.id.pb);  
	    pb.setMax(100);
	}

	private void initWebView() {
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.setWebChromeClient(new ChromeWebClient());
		mWebView.setWebViewClient(new AndroidWebView());

		
	} 
	
	
	private class AndroidWebView extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			return true;
		}
	}
	
	private class ChromeWebClient extends WebChromeClient {  
	    @Override  
	    public void onProgressChanged(WebView view, int newProgress) { 
	    	
	        pb.setProgress(newProgress);  
	        if(newProgress==100){  
	            pb.setVisibility(View.GONE);  
	        }  
	        super.onProgressChanged(view, newProgress);  
	    }  
	      
	}
	
	

	
}

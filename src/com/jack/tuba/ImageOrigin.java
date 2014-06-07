package com.jack.tuba;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ImageOrigin extends Activity{
	
	private ProgressBar pb;
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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

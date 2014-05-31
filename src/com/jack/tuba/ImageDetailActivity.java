package com.jack.tuba;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageDetailActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagedetail);
		
		PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);
		
		String url=getIntent().getStringExtra("url");
		if (url!=null) {
			ImageLoader.getInstance().displayImage(url, photoView);
		}
		
	}
}

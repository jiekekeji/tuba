package com.jack.tuba;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.jack.tuba.utils.TubaUtils;

public class MySingleDownLoadAtivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mysingledownload);
		
		initImageView();
	}

	private void initImageView() {
		// TODO Auto-generated method stub
		PhotoView photoView=(PhotoView) findViewById(R.id.download_iv_photo);
		String path=getIntent().getStringExtra("path");
		int[] xy=TubaUtils.getScreenXy(this);
		Bitmap bitmap=TubaUtils.getImageThumbnail(path, xy[0], xy[1]);
		photoView.setImageBitmap(bitmap);
	}
}

package com.jack.tuba;

import uk.co.senab.photoview.PhotoView;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageDetailActivity extends Activity{

	private ShareActionProvider mShareActionProvider;
	private DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagedetail);
		initOptions();
		initActionBar();
		initPhotoView();
		
		
		
	}

	@SuppressLint("NewApi")
	private void initActionBar() {
		// TODO Auto-generated method stub
		ActionBar mActionBar=getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	/**
	 * 设置显示参数
	 */
	private void initOptions() {
		// TODO Auto-generated method stub
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.empty_photo)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

	}

	/**
	 * 
	 */
	private void initPhotoView() {
		// TODO Auto-generated method stub
		PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);
		
		String url=getIntent().getStringExtra("url");
		
		if (url!=null) {
			ImageLoader.getInstance().displayImage(url, photoView, options);
//			ImageLoader.getInstance().displayImage(url, photoView);
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();  
	    inflater.inflate(R.menu.image_detail_menu, menu);  
	    MenuItem shareItem = menu.findItem(R.id.action_share);  
	    ShareActionProvider provider = (ShareActionProvider) shareItem.getActionProvider();  
	    provider.setShareIntent(getDefaultIntent()); 
	    
		return super.onCreateOptionsMenu(menu);
	}
	
	/** Defines a default (dummy) share intent to initialize the action provider.
	  * However, as soon as the actual content to be used in the intent
	  * is known or changes, you must update the share intent by again calling
	  * mShareActionProvider.setShareIntent()
	  */
	private Intent getDefaultIntent() {  
	    Intent intent = new Intent(Intent.ACTION_SEND);  
	    intent.setType("image/*");  
	    return intent;  
	}
}

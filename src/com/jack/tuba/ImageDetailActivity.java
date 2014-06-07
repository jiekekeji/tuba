package com.jack.tuba;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.co.senab.photoview.PhotoView;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.jack.tuba.utils.TubaUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageDetailActivity extends Activity {

	private static final String TAG = "ImageDetailActivity";
	private ShareActionProvider mShareActionProvider;
	private DisplayImageOptions options;
	private PhotoView photoView;
	private String url;
	private static final String downLoadFile = "tubaDownLoad";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		url = getIntent().getStringExtra("url");
		setContentView(R.layout.activity_imagedetail);
		initOptions();
		initActionBar();
		initPhotoView();
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(false);
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
		photoView = (PhotoView) findViewById(R.id.iv_photo);
		if (url != null) {
			ImageLoader.getInstance().displayImage(url, photoView, options);
		}

	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.image_detail_menu, menu);
		MenuItem shareItem = menu.findItem(R.id.action_share);
		ShareActionProvider provider = (ShareActionProvider) shareItem
				.getActionProvider();
		provider.setShareIntent(getDefaultIntent());

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Defines a default (dummy) share intent to initialize the action provider.
	 * However, as soon as the actual content to be used in the intent is known
	 * or changes, you must update the share intent by again calling
	 * mShareActionProvider.setShareIntent()
	 */
	private Intent getDefaultIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		return intent;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.set_wallpaper:// 设为壁纸
			setWallPhoto();
			break;
		case R.id.action_download:// 下载图
			new LoadImageTask().execute(url);
			break;

		case android.R.id.home:
			finish();
			break;
		
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 设置壁纸
	 */
	private void setWallPhoto() {
		// TODO Auto-generated method stub
		WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
		photoView.setDrawingCacheEnabled(true);
		try {
			wallpaperManager.setBitmap(photoView.getDrawingCache());
			TubaUtils.MyToast(this, "设置成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "wallpaperManager.setBitmap() error" + e);
			TubaUtils.MyToast(this, "设置失败");
		}
	}
	
	/**
	 * 复制图片的任务
	 * @author Administrator
	 *
	 */
	class LoadImageTask extends AsyncTask<String, integer, Void>{

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			copyImages();
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			TubaUtils.MyToast(ImageDetailActivity.this, "下载完成");
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		
	}

	/**
	 * 复制缓存的图片到下载目录
	 */
	private void copyImages() {
		// TODO Auto-generated method stub

		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			String imageCachePath = TubaUtils.getImageCachePath(url);
			fis = new FileInputStream(imageCachePath);

			String targetPath = TubaUtils.getTubaDownloadPath(this,
					downLoadFile) + TubaUtils.getImageName(url);
			File targetFile = new File(targetPath);
			if (!targetFile.exists()) {
				targetFile.createNewFile();
			}

			fos = new FileOutputStream(targetFile);

			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = fis.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
		} catch (IOException e) {
			Log.i(TAG, "复制文件失败");
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				Log.i(TAG, "读取关闭失败");
			}
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				Log.i(TAG, "写入关闭失败");
			}
		}
	}
	
	
	
}

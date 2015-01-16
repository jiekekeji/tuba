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
import android.view.View;
import android.widget.ShareActionProvider;

import com.jack.tuba.utils.TubaUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
/**
 * 
 * @author Administrator
 *
 */
public class ImageDetailActivity extends Activity {

	private static final String TAG = ImageDetailActivity.class.getName();
	/**
	 * universalimageloader展示的参数
	 */
	private DisplayImageOptions options;
	/**
	 * 用于展示image,可以缩放
	 */
	private PhotoView photoView;
	/**
	 * 图片的地址
	 */
	private String url;
	/**
	 * 图片下载到sd卡的文件名
	 */
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

	/**
	 * 配置actionBar,
	 */
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
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

	}

	/**
	 * 展示图片
	 */
	private void initPhotoView() {
		photoView = (PhotoView) findViewById(R.id.iv_photo);
		if (url != null) {
//			ImageLoader.getInstance().displayImage(url, photoView, options);
			int imgHeight=getIntent().getIntExtra("ivHeight",0);
			int imgWidth=getIntent().getIntExtra("ivWidth", 0);
			
			
			if (imgHeight!=0&&imgWidth!=0) {
				ImageLoader.getInstance().loadImage(url,new ImageSize(imgWidth, 800), new ImgListener());
			}
			Log.i(TAG, "imgHeight="+imgHeight+"imgWidth="+imgWidth);
		}

	}
	
	class ImgListener extends SimpleImageLoadingListener{
		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			photoView.setImageBitmap(loadedImage);
			super.onLoadingComplete(imageUri, view, loadedImage);
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
	class LoadImageTask extends AsyncTask<String, integer, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			return copyImages();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			TubaUtils.MyToast(ImageDetailActivity.this, result);
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		
	}

	/**
	 * 从缓存目录复制到下载目录
	 * @return  String 下载的状态
	 */
	private String copyImages() {
		
		FileOutputStream fos = null;
		FileInputStream fis = null;
		
		try {
			
			File imageCache=getImageCachePath(url);
			if (imageCache==null) {
				return "无法下载，稍后图片打开后再试";
			}
			fis = new FileInputStream(imageCache);

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
			
			return "下载成功";
		} catch (IOException e) {
			
			return "无法下载，稍后图片打开后再试";
			
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
	
	/**
	 * 得到缓存图片的路径
	 * @param url
	 * @return 缓存图片的路径
	 */
	public File  getImageCachePath(String url) {
		
		File f=ImageLoader.getInstance().getDiskCache().get(url);
		if (f!=null&&f.exists()) {
			return f;	
		}else {
			return null;
		}	
	}
}

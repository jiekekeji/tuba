package com.jack.tuba.app;

import java.io.IOException;

import libcore.io.DiskLruCache;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.jack.tuba.utils.TubaUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;


public class TuBaApp extends Application {
	private static final String TAG = "TuBaApp";
	public static DiskLruCache mDiskLruCache;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		try {
			mDiskLruCache = TubaUtils.openDiskLruCache(getApplicationContext());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initImageLoader(getApplicationContext());

		Log.i(TAG, "xxx");
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		ImageLoader.getInstance().destroy();
		super.onTerminate();
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}

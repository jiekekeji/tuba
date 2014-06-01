package com.jack.tuba.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import libcore.io.DiskLruCache;
import libcore.io.DiskLruCache.Snapshot;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.Toast;
public class TubaUtils {
	
	/**
	 * 获取当前应用程序的版本号。
	 */
	public static int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
					0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	/**
	 * 根据传入的uniqueName获取硬盘缓存的路径地址。
	 */
	public static File getDiskDefaultCacheDir(Context context, String uniqueName) {
		
		String cachePath = null;
		File file=null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			cachePath= context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		file=new File(cachePath + File.separator + uniqueName);
		if (!file.exists()) {
			file.mkdirs();
		}		
		return file;
	}
	
	/**
	 * 
	 * @param keyword  关键词
	 * @param size     每页大小
	 * @param start    起始页
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getEncoderUrl(String keyword,int size,int start){
		
		String q=null;
		try {
			q = URLEncoder.encode(keyword, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String rsz=String.valueOf(size);
		String str=String.valueOf(start);
		
		String url="https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+q+"&rsz="+rsz+"&start="+str+"";
		return url;
		
	}
	
	/**
	 * 如果有SD卡 得到SD卡的缓存路径
	 * 否则得到手机的缓存路径
	 * @param context
	 * @return
	 */
	public static String getImageCachePath(Context context) {
		String imageCachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			imageCachePath = Environment.getDataDirectory().getPath();
		} else {
			imageCachePath = context.getCacheDir().getPath();
		}
		return imageCachePath;
	}
	
	/**
	 * 得到sd卡缓存实例
	 * @return
	 * @throws IOException 
	 */
	public static DiskLruCache openDiskLruCache(Context context)
			throws IOException {

		File diskCacheFile = getDiskDefaultCacheDir(context, "tuBa");
		int appVersion = getAppVersion(context);
		int diskCacheMaxSize = 1024 * 10 * 10;

		DiskLruCache mDiskLruCache = null;

		mDiskLruCache = DiskLruCache.open(diskCacheFile, appVersion, 1,
				diskCacheMaxSize);

		return mDiskLruCache;

	}
	/**
	 * 
	 * @param Context context
	 * @param key  MD5后的String
	 * @return
	 * @throws IOException 
	 * @throws StreamCorruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static Object getObjectFromDiskLruCache(DiskLruCache diskLruCache, String key){
		
		Snapshot snapShot = null;
		ObjectInputStream objectInputStream = null;
		FileInputStream fileInputStream = null;
		Object object = null;
		try {
			snapShot = diskLruCache.get(key);
			if (snapShot != null) {
				fileInputStream = (FileInputStream) snapShot.getInputStream(0);
				objectInputStream = new ObjectInputStream(fileInputStream);
				object = objectInputStream.readObject();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return object;
	}
	/**
	 * 
	 * @param diskLruCache
	 * @param key  MD5后的key
	 * @throws IOException 
	 */
	public static void writeObjectTODiskLruCache(DiskLruCache diskLruCache,String key,Object object) {
		OutputStream outputStream=null;
		ObjectOutput objectOutput=null;
		
		DiskLruCache.Editor editor=null;
	
		try {
			editor = diskLruCache.edit(key);
			outputStream=editor.newOutputStream(0);
			objectOutput = new ObjectOutputStream(outputStream);
			objectOutput.writeObject(object);
			editor.commit();
			diskLruCache.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	

	/**
	 * 获取屏幕宽高
	 * @param context
	 * @return  x 宽  y 高
	 */
	public static int[] getScreenXy(Context context){
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height= wm.getDefaultDisplay().getHeight();
		
		int[] xy={width,height};
		return xy;
		
	}
	
	public static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if(networkInfo == null || networkInfo.isConnected() == false)
		{
			return false;
		}
		return true;
	}

	public static boolean isWifi(Context context)
	{
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
	}
	
	/**
	 * 使用MD5算法对传入的key进行加密并返回。
	 */
	public static String keyOfMD5(String key) {
		// TODO Auto-generated method stub
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;

	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToHexString(byte[] bytes) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	
	/**
	 * 将输入流转为字符串
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String Stream2String(InputStream is) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is),
				16 * 1024);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) { // 处理换行符
			sb.append(line + "\n");
		}
		return sb.toString();
	}
	
	/**
	 * Toast
	 * @param context
	 * @param text
	 */
	public static void MyToast(Context context,String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT);
	}
}

package com.jack.tuba.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedList;

import libcore.io.DiskLruCache;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jack.tuba.app.TuBaApp;
import com.jack.tuba.domain.Image;
import com.jack.tuba.domain.Result;

/**
 * 加载网络json
 * 
 * @author jack
 */
public class LoadDataTask extends
		AsyncTask<String, Integer, LinkedList<Result>> {

	
	private static final String TAG = LoadDataTask.class.getName();
	/**
	 * 用来将缓存jsonObject
	 */
	private DiskLruCache mDiskCache;
	
	private Context context;
	/**
	 * 对加载过程进行监听
	 */
	private LoadDataTaskListenner listenner;

	public LoadDataTask(DiskLruCache diskLruCache,Context context) {
		this.context=context;
		mDiskCache = diskLruCache;
	}

	/**
	 * 开启线程获取数据之前
	 */
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if (listenner!=null) {
			listenner.onPreLoadData();
		}
		super.onPreExecute();
	}

	@Override
	protected LinkedList<Result> doInBackground(String... params) {
		// TODO Auto-generated method stub	
		if (this.isCancelled()) {
			return null;
		}
		Log.i(TAG, "doInBackground"+Thread.currentThread().getName());
		String url = params[0];
		LinkedList<Result> results = null;
		results=getFromDiskLruCache(url);
		if (results!=null&&results.size()>0) {
			Log.i(TAG, "getFromDiskLruCache");
			return results;
		}else{
			if (TubaUtils.isNetworkAvailable(context)) {
				results=getFromNet(url);
				Log.i(TAG, "getFromNet");
			}else {
				Log.i(TAG, "nothing");
				return results;
			}
		} 
		return results;
	}

	/**
	 * 从网络获取json
	 * @param url
	 * @return  成功获取返回多个图片信息，否返回null
	 */
	private LinkedList<Result> getFromNet(String url) {
		LinkedList<Result> results = null;
		try {
			Log.i(TAG, "getJson");
			String json = getJsonString(url);
			Log.i(TAG, json);
			if (json != null) {
				results = parseJson2Object(json);
				if (results != null) {
					String key = TubaUtils.keyOfMD5(url);
					TubaUtils.writeObjectTODiskLruCache(mDiskCache, key,
							results);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * 从本地获取json
	 * 
	 * @param url
	 *            请求的url
	 * @return LinkedList 成功获取返回多个图片信息，否返回null
	 */
	private LinkedList<Result> getFromDiskLruCache(String url) {
		LinkedList<Result> results = null;
		String key = TubaUtils.keyOfMD5(url);
		results = (LinkedList<Result>) TubaUtils.getObjectFromDiskLruCache(
				TuBaApp.mDiskLruCache, key);
		return results;
	}

	/**
	 * 发起网络请求，获取json字符串
	 * 
	 * @param url
	 * @return  String
	 */
	private String getJsonString(String url) {
		String tempRul;
		StringBuilder builder;
		try {
			Log.i(TAG, " getJsonString");
			tempRul = URLDecoder.decode(url, "utf-8");
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(tempRul);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000); 
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
		   
			HttpResponse response = client.execute(get);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) { 
				InputStream in = response.getEntity().getContent();
				int length = 0;
				byte[] b = new byte[1024];
				builder = new StringBuilder();

				while ((length = in.read(b)) != -1) {

					builder.append(new String(b, 0, length, "utf-8"));
				}
				return builder.toString();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage().toString());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		
		super.onProgressUpdate(values);
		

	}

	@Override
	protected void onPostExecute(LinkedList<Result> results) {
		// TODO Auto-generated method stu
		if (listenner!=null) {
			listenner.onLoadDataComplete(results);
		}
		Log.i(TAG, "onPostExecute");
		super.onPostExecute(results);
		
	}
	
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		if (listenner!=null) {
			listenner.onCancelLoadData();
		}
		super.onCancelled();
	}

	/**
	 * 解析json字符串
	 * 
	 * @param json
	 *            字符串
	 * 
	 * @return  LinkedList
	 */
	private LinkedList<Result> parseJson2Object(String json) {
		// TODO Auto-generated method stub
		LinkedList<Result> results = null;
		Image image = JSON.parseObject(json, Image.class);
		if (200 == image.getResponseStatus()) {
			if (image.getResponseData().getResults() != null
					&& image.getResponseData().getResults().size() > 0) {

				results = image.getResponseData().getResults();
			}
			return results;
		}
		return null;
	}
	
	/**
	 * 设置数据加载的监听者
	 * @param listenner
	 */
	public void setOnLoadDataTaskListenner(LoadDataTaskListenner listenner){
		this.listenner=listenner;
	}
    /**
     * 数据加载过程的监听者
     * @author jack
     *
     */
	public interface LoadDataTaskListenner{
		/**
		 * 加载数据之前
		 */
		void onPreLoadData();
		
		/**
		 *线程被取消掉后的回调
		 */
		public abstract void onCancelLoadData();
		/**
		 * 加载完成之后
		 * 
		 * @param results
		 */
		public abstract void onLoadDataComplete(LinkedList<Result> results);
	} 

}

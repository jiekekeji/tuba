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

import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jack.tuba.domain.Image;
import com.jack.tuba.domain.Result;

/**
 * 
 * @author Administrator
 * 
 */
public abstract class LoadDataTask extends
		AsyncTask<String, Integer, LinkedList<Result>> {

	private static final String TAG = LoadDataTask.class.getName();
	/**
	 * 用来将缓存jsonObject
	 */
	private DiskLruCache mDiskCache;

	public LoadDataTask(DiskLruCache diskLruCache) {

		mDiskCache = diskLruCache;
	}

	/**
	 * 开启线程获取数据之前
	 */
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		onPreLoadData();

	}

	@Override
	protected LinkedList<Result> doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = params[0];
		LinkedList<Result> results = null;
		try {
			String json = getJsonString(url);
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
	 * 发起网络请求，获取json字符串
	 * 
	 * @param url
	 * @return
	 */
	private String getJsonString(String url) {
		String tempRul;
		StringBuilder builder;
		try {
			tempRul = URLDecoder.decode(url, "utf-8");
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(tempRul);
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
		onDoingLoadData(values[0]);

	}

	@Override
	protected void onPostExecute(LinkedList<Result> results) {
		// TODO Auto-generated method stub
		super.onPostExecute(results);
		onLoadDataComplete(results);
	}

	/**
	 * 解析json字符串
	 * 
	 * @param json 字符串
	 *            
	 * @return
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
	 * 加载数据之前
	 */
	public abstract void onPreLoadData();

	/**
	 * 正在加载数据
	 * 
	 * @param i
	 */
	public abstract void onDoingLoadData(Integer i);;

	/**
	 * 加载完成之后
	 * 
	 * @param results
	 */
	public abstract void onLoadDataComplete(LinkedList<Result> results);

}

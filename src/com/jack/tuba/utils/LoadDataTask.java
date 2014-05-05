package com.jack.tuba.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import libcore.io.DiskLruCache;

import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.jack.tuba.domain.Image;
import com.jack.tuba.domain.Result;

public class LoadDataTask extends
		AsyncTask<String, Integer, LinkedList<Result>> {

	private OnLoadDataListener mOnLoadDataListener;

	private DiskLruCache mDiskLruCache;
	
	public LoadDataTask(DiskLruCache diskLruCache) {
	
		mDiskLruCache=diskLruCache;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (mOnLoadDataListener != null) {
			mOnLoadDataListener.onPreLoadData();
		}

	}

	@Override
	protected LinkedList<Result> doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = params[0];
		LinkedList<Result> results = null;
		InputStream in = null;
		try {
			in = HttpUtils.getInputStream(url);
			if (in != null) {
				String mString = TubaUtils.Stream2String(in);
				if (mString != null) {
					results = parseJsonString2Object(mString);
					if (results != null && results.size() > 0) {
						String key=TubaUtils.keyOfMD5(url);
						TubaUtils.writeObjectTODiskLruCache(mDiskLruCache, key, results);
						return results;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return results;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		if (mOnLoadDataListener != null) {
			mOnLoadDataListener.onDoingLoadData(values[0]);
		}

	}

	@Override
	protected void onPostExecute(LinkedList<Result> results) {
		// TODO Auto-generated method stub
		super.onPostExecute(results);
		if (mOnLoadDataListener != null) {
			mOnLoadDataListener.onLoadDataComplete(results);
		}
	}

	/**
	 * 解析json字符串
	 * 
	 * @param json
	 *            字符串
	 * @return
	 */
	private LinkedList<Result> parseJsonString2Object(String json) {
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

	public interface OnLoadDataListener {

		void onPreLoadData();

		void onDoingLoadData(Integer i);

		void onLoadDataComplete(LinkedList<Result> results);

	}

	public void setOnLoadDataListener(OnLoadDataListener l) {
		mOnLoadDataListener = l;
	}
}

package com.jack.tuba;

import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.jack.tuba.adapter.CommonAdapter;
import com.jack.tuba.app.TuBaApp;
import com.jack.tuba.domain.Result;
import com.jack.tuba.utils.LoadDataTask;
import com.jack.tuba.utils.LoadDataTask.OnLoadDataListener;
import com.jack.tuba.utils.TubaUtils;
import com.jack.tuba.widget.PullAndLoadListView;
import com.jack.tuba.widget.PullAndLoadListView.OnLoadMoreListener;
import com.jack.tuba.widget.PullToRefreshListView.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


public class MainActivity extends Activity implements OnRefreshListener, OnLoadMoreListener, OnLoadDataListener {

	private static final String TAG = "MainActivity";
	private LinkedList<Result> mListItems;
	private DisplayImageOptions options;
	private PullAndLoadListView mListView;
	private boolean pullOrLoad;
    
	private CommonAdapter mCommonAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initOptions();
		initActionBar();
		initView();
	}
	
	@SuppressLint("NewApi")
	private void initActionBar() {
		// TODO Auto-generated method stub
		ActionBar ab=getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		mListItems = new LinkedList<Result>();
		int[] xy=TubaUtils.getScreenXy(this);
		mCommonAdapter = new CommonAdapter(this,mListItems, options,xy[1],xy[0]);
		
		mListView = (PullAndLoadListView) findViewById(R.id.list);
		mListView.setAdapter(mCommonAdapter);
		mListView.setOnRefreshListener(this);
		// set a listener to be invoked when the list reaches the end
		mListView.setOnLoadMoreListener(this);
		refreshData();

	}
	
	/**
	 * 设置显示参数
	 */
	private void initOptions() {
		// TODO Auto-generated method stub
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onRefresh");
		
		refreshData();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onLoadMore");
		refreshData();
	}
	
	public void refreshData(){
		String url="http://jiekekeji.wicp.net/imageba/url.txt";
		String key=TubaUtils.keyOfMD5(url);
		LinkedList<Result> results=(LinkedList<Result>) TubaUtils.getObjectFromDiskLruCache(TuBaApp.mDiskLruCache, key);
		if (results!=null&&results.size()>0) {
			if (mListItems.size()>0) {
				mListItems.clear();
				mListItems.addAll(results);
			}else {
				mListItems.addAll(results);
			}
			mCommonAdapter.notifyDataSetChanged();
			mListView.onLoadMoreComplete();
			mListView.onRefreshComplete();
		}else {
			if (TubaUtils.isNetworkAvailable(this)) {
				LoadDataTask mDataTask=new LoadDataTask(TuBaApp.mDiskLruCache);
				mDataTask.setOnLoadDataListener(this);
				mDataTask.execute(url);
				
			}
		}
	}

	@Override
	public void onPreLoadData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDoingLoadData(Integer i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadDataComplete(LinkedList<Result> results) {
		// TODO Auto-generated method stub
		if (results!=null&&results.size()>0) {
			if (mListItems.size()>0) {
				mListItems.clear();
				mListItems.addAll(results);
			}else {
				mListItems.addAll(results);
			}
		}else {
			Log.i(TAG, "获取数据失败");
		}
		mListView.onLoadMoreComplete();
		mListView.onRefreshComplete();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



}

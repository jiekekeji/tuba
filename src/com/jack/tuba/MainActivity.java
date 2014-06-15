package com.jack.tuba;

import java.util.LinkedList;

import libcore.io.DiskLruCache;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.jack.tuba.adapter.CommonAdapter;
import com.jack.tuba.app.TuBaApp;
import com.jack.tuba.domain.Result;
import com.jack.tuba.utils.Constant;
import com.jack.tuba.utils.LoadDataTask;
import com.jack.tuba.utils.TubaUtils;
import com.jack.tuba.widget.PullUpAndDownListView;
import com.jack.tuba.widget.PullUpAndDownListView.RefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MainActivity extends Activity implements OnItemClickListener {

	private static final String TAG = "MainActivity";
	private LinkedList<Result> mListItems;
	private DisplayImageOptions options;
	private PullUpAndDownListView mListView;

	private CommonAdapter mCommonAdapter;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerArrowDrawable drawerArrow;
	private boolean drawerArrowColor;
	// "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=girl&rsz=8&start=10";
	private String q = "美女";
	private int rsz = 8;
	private int start = 0;
	
	private boolean isOnLoading=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initOptions();
		initActionBar();
		initDrawerMenu();
		initView();
	}

	/**
	 * 侧滑菜单
	 */
	private void initDrawerMenu() {
		// TODO Auto-generated method stub
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.navdrawer);

		drawerArrow = new DrawerArrowDrawable(this) {
			@Override
			public boolean isLayoutRtl() {
				return false;
			}
		};
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				drawerArrow, R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item_drawer_list, R.id.drawerlist_item,
				Constant.menuItem);

		mDrawerList.setAdapter(adapter);
		DrawerOnItemClickListener mListener = new DrawerOnItemClickListener();
		mDrawerList.setOnItemClickListener(mListener);
	}

	/**
	 * mDrawerList监听者
	 * 
	 * @author Administrator
	 * 
	 */
	class DrawerOnItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent;
			switch (position) {
			case 0:// 我的下载
				intent = new Intent(MainActivity.this, MyDownLoadActivity.class);
				startActivity(intent);
				mDrawerLayout.closeDrawer(mDrawerList);
				break;
			case 1:// 应用推荐
				intent = new Intent(MainActivity.this,
						RecommendAppActivity.class);
				startActivity(intent);
				mDrawerLayout.closeDrawer(mDrawerList);
				break;
			case 2:// 关于我们
				intent = new Intent(MainActivity.this, AboutUsActivity.class);
				startActivity(intent);
				mDrawerLayout.closeDrawer(mDrawerList);
				break;
			case 3:// 意见反馈
				intent = new Intent(MainActivity.this, SuggestActivity.class);
				startActivity(intent);
				mDrawerLayout.closeDrawer(mDrawerList);
				break;
			case 4:// 设置中心
				intent = new Intent(MainActivity.this, SettingActivity.class);
				startActivity(intent);
				mDrawerLayout.closeDrawer(mDrawerList);
				break;
			case 5:// 查找更多
				intent = new Intent(MainActivity.this, HelpActivity.class);
				startActivity(intent);
				mDrawerLayout.closeDrawer(mDrawerList);
				break;
			default:
				break;
			}
		}

	}

	@SuppressLint("NewApi")
	private void initActionBar() {
		// TODO Auto-generated method stub
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
	}

	/**
	 * listView 初始化
	 */
	private void initView() {

		mListItems = new LinkedList<Result>();
		int[] xy = TubaUtils.getScreenXy(this);
		mCommonAdapter = new CommonAdapter(this, mListItems, options, xy[1],
				xy[0]);

		mListView = (PullUpAndDownListView) findViewById(R.id.list);
		mListView.setAdapter(mCommonAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setRefreshListener(new MyRefreshListener());

		refreshData(getUrl(q, rsz, start));

	}

	// "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=girl&rsz=8&start=10"
	private String getUrl(String q,int rsz,int start) {
		
		StringBuffer buffer = new StringBuffer(Constant.homeUrl);
		buffer.append("&q=").append(q).append("&rsz=").append(rsz)
				.append("&start=").append(start);
		start = start + rsz;		
		return buffer.toString();

	}

	/**
	 * 上拉和下拉的监听者
	 * 
	 * @author Administrator
	 * 
	 */
	class MyRefreshListener implements RefreshListener {

		@Override
		public void pullUp() {
			loadUpPage();
		}

		@Override
		public void pullDown() {
			// TODO Auto-generated method stub
			loadNexPage();
		}	

	}
	/**
	 * 加载上一页
	 */
	private void loadUpPage() {
		if (isOnLoading) {
			return;
		}
		start=start+rsz;
		String nextUrl=getUrl(q, rsz, start);		
		refreshData(nextUrl);
		Log.i(TAG, nextUrl);
	}
    /**
     * 加载下一页	
     */
	private void loadNexPage() {
		if (isOnLoading) {
			return;
		}
		if (start>10) {
			start=start-rsz;
		}
		String upUrl=getUrl(q, rsz, start);
		refreshData(upUrl);
		Log.i(TAG, upUrl);
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
	 * 发起数据请求
	 */
	public void refreshData(String url) {

		isOnLoading=true;
		String key = TubaUtils.keyOfMD5(url);
		/**
		 * 从sd卡中拿，如果没有则判断网络是否ok,然后从网络拿
		 */
		LinkedList<Result> results = (LinkedList<Result>) TubaUtils
				.getObjectFromDiskLruCache(TuBaApp.mDiskLruCache, key);

		if (results != null && results.size() > 0) {
			if (mListItems.size() > 0) {
				mListItems.clear();
				mListItems.addAll(results);
			} else {
				mListItems.addAll(results);
			}
			mCommonAdapter.notifyDataSetChanged();
			mListView.setSelection(1);
			mListView.setRefreshComplete();
			isOnLoading=false;

		} else {
			if (TubaUtils.isNetworkAvailable(this)) {
				new MyLoadDataTask(TuBaApp.mDiskLruCache).execute(url);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, ImageDetailActivity.class);
		intent.putExtra("url", mListItems.get(position - 1).getUrl());
		intent.putExtra("ivHeight", mListItems.get(position - 1).getHeight());
		intent.putExtra("ivWidth", mListItems.get(position - 1).getWidth());
		startActivity(intent);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	class MyLoadDataTask extends LoadDataTask {

		public MyLoadDataTask(DiskLruCache diskLruCache) {
			super(diskLruCache);
			// TODO Auto-generated constructor stub
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
			if (results != null && results.size() > 0) {
				if (mListItems.size()>0) {
					mListItems.clear();
					mListItems.addAll(results);
				}else {
					mListItems.addAll(results);
				}
				mCommonAdapter.notifyDataSetChanged();
			}else {
				Toast.makeText(MainActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
			}
			mListView.setRefreshComplete();
			mListView.setSelection(1);
			isOnLoading=false;
		}

	}

}

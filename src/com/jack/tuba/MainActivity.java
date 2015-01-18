package com.jack.tuba;

import java.util.LinkedList;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.jack.tuba.adapter.ShowImageAdapter;
import com.jack.tuba.app.TuBaApp;
import com.jack.tuba.domain.Result;
import com.jack.tuba.utils.Constant;
import com.jack.tuba.utils.LoadDataTask;
import com.jack.tuba.utils.LoadDataTask.LoadDataTaskListenner;
import com.jack.tuba.utils.TubaUtils;
import com.jack.tuba.widget.PullAndLoadListView;
import com.jack.tuba.widget.PullAndLoadListView.OnLoadMoreListener;
import com.jack.tuba.widget.PullToRefreshListView.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 入口类
 * 
 * @author jack
 *
 */
@SuppressLint("NewApi")
public class MainActivity extends ListActivity implements OnItemClickListener {

	private static final String TAG = "MainActivity";
	/**
	 * item的信息
	 */
	private LinkedList<Result> mListItems;
	/**
	 * UIL展示参数
	 */
	private DisplayImageOptions options;
	/**
	 * 上拉加载下一页，下拉加载上一页ListView
	 */
	private PullAndLoadListView mListView;
	/**
	 * 展示image的adapter
	 */
	private ShowImageAdapter mImageAdapter;
	/**
	 * 左侧菜单
	 */
	private DrawerLayout mDrawerLayout;
	/**
	 * 左侧菜单列表
	 */
	private ListView mDrawerList;
	/**
	 * 左侧菜单开关
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerArrowDrawable drawerArrow;

	private boolean drawerArrowColor;
	/**
	 * 搜索关键字
	 */
	private String q = "美女";
	/**
	 * 搜索每页的数量
	 */
	private int rsz = 8;
	/**
	 * 搜索的起始位置
	 */
	private int start = 0;
	/**
	 * 等待条
	 */
	private ProgressBar mProgressBar;
	/**
	 * 当listview为空时，显示的view
	 */
	private TextView emptyView;
	/**
	 * 加载数据的任务
	 */
	private LoadDataTask mDataTask;
	/**
	 * 是否第一次加载数据，用于判断是否需要显示进度条
	 */
	private Boolean isFirstLoadData = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		checkKey();
		initOptions();
		initActionBar();
		initDrawerMenu();
		initView();
		
	}
	
	
   /**
    * 设置搜索词
    */
	private void checkKey() {
		// TODO Auto-generated method stub
		String k=getIntent().getStringExtra("key");
		if (k!=null&&!k.equals("")) {
			q=k;
		}
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
			switch (position) {
			case 0:
				openActivity(MyDownLoadActivity.class);
				break;
			case 1:
				openActivity(RecommendAppActivity.class);
				;
				break;
			case 2:
				openActivity(AboutUsActivity.class);
				break;
			case 3:
				openActivity(SuggestActivity.class);
				break;
			case 4:// 设置中心
				openActivity(SettingActivity.class);
				break;
			case 5:// 查找更多
				openActivity(HelpActivity.class);
				break;
			default:
				break;
			}
		}

		private void openActivity(Class clazz) {
			Intent intent = new Intent(MainActivity.this, clazz);
			startActivity(intent);
			mDrawerLayout.closeDrawer(mDrawerList);
		}

	}

	/**
	 * 显示actionbar的返回建
	 */
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

		mListView = (PullAndLoadListView) getListView();
		mListView.setOnItemClickListener(this);

		mListView.setOnLoadMoreListener(new MyOnloadMoreListener());
		mListView.setOnRefreshListener(new MyRefreshDataListener());

		mProgressBar = (ProgressBar) findViewById(R.id.main_pro_bar);

		emptyView = (TextView) findViewById(R.id.empty);
		emptyView.setVisibility(View.GONE);

		getMoreData();

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "stop");
		cancelDataTask();
		super.onStop();
	}

	/**
	 * 取消加载任务
	 */
	private void cancelDataTask() {
		Log.i(TAG, "cancelDataTask");
		if (mDataTask != null
				&& mDataTask.getStatus() == AsyncTask.Status.RUNNING) {
			mDataTask.cancel(true);
			mDataTask = null;
		}
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestory");
		cancelDataTask();
		super.onDestroy();
	}

	/**
	 * 构造请求地址
	 * 
	 * @param q
	 *            关键字
	 * @param rsz
	 *            每页大小
	 * @param start
	 *            起始页码
	 * @return String get请求地址
	 */
	private String getUrl(String q, int rsz, int start) {
		StringBuffer buffer = new StringBuffer(Constant.homeUrl);
		buffer.append("&q=").append(q).append("&rsz=").append(rsz)
				.append("&start=").append(start);
		start = start + rsz;
		
		return buffer.toString();
	}
	
	

	/**
	 * 上拉加载更多的监听者 *
	 * 
	 * @author jack *
	 */
	class MyOnloadMoreListener implements OnLoadMoreListener {
		@Override
		public void onLoadMore() {
			Log.i(TAG, "onLoadMore");
			if (isFirstLoadData) {
				mListView.onLoadMoreComplete();
			}else {
				getMoreData();
			}
			
		}
	}

	/**
	 * 下拉更新数据
	 * 
	 * @author jack
	 */
	class MyRefreshDataListener implements OnRefreshListener {
		@Override
		public void onRefresh() {
			mListView.onRefreshComplete();
			Log.i(TAG, "MyRefreshData");
		}
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
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
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
		if (position == mListItems.size() + 1) {
			return;
		}
		Intent intent = new Intent(this, ImageDetailActivity.class);
		intent.putExtra("url", mListItems.get(position - 1).getUrl());
		intent.putExtra("ivHeight", mListItems.get(position - 1).getHeight());
		intent.putExtra("ivWidth", mListItems.get(position - 1).getWidth());
		overridePendingTransition(R.anim.hold, R.anim.hold);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			openOrCloseDrawerList();
			break;
		case R.id.action_search:
			Intent i = new Intent(MainActivity.this, SearchImageActivity.class);
			startActivityForResult(i, 0);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 左侧菜单开关
	 */
	private void openOrCloseDrawerList() {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			mDrawerLayout.openDrawer(mDrawerList);
		}
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

	/**
	 * 获取更多
	 */
	private void getMoreData() {
		mDataTask = new LoadDataTask(TuBaApp.mDiskLruCache, MainActivity.this);
		mDataTask.setOnLoadDataTaskListenner(new MyoadDataTaskListenner());
		String url = getUrl(q, rsz, start);
		mDataTask.execute(url);
	}

	/**
	 * 加载更多的监听者
	 * 
	 * @author jack
	 *
	 */
	class MyoadDataTaskListenner implements LoadDataTaskListenner {

		@Override
		public void onPreLoadData() {
			// TODO Auto-generated method stub
			if (isFirstLoadData) {
				mProgressBar.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onCancelLoadData() {
			// TODO Auto-generated method stub
			mListView.onLoadMoreComplete();
			Log.i(TAG, "onCancelLoadData");
		}

		@Override
		public void onLoadDataComplete(LinkedList<Result> results) {
			if (results != null) {
				if (mListItems == null) {
					mListItems = results;
					int[] xy = TubaUtils.getScreenXy(MainActivity.this);
					mImageAdapter = new ShowImageAdapter(MainActivity.this,
							mListItems, options, xy[1], xy[0]);
					mListView.setAdapter(mImageAdapter);		
				}else{
					if (isFirstLoadData) {
						mListItems.clear();
						mListItems.addAll(results);
						mImageAdapter.notifyDataSetChanged();
						mListView.setSelection(1);
					}else {
						mListItems.addAll(results);
					}					
				}
				start = start + rsz;
			} else {
				if (mListItems == null) {
					mProgressBar.setVisibility(View.GONE);
					mListView.setEmptyView(emptyView);
				}
			}
			mListView.onLoadMoreComplete();
			Log.i(TAG, "onLoadDataComplete");
			if (isFirstLoadData) {
				mProgressBar.setVisibility(View.GONE);
				isFirstLoadData=false;
			}
		}
	}

	/**
	 * 刷新数据的任务
	 * 
	 * @author jack
	 *
	 */
	class MyRefreshDataTask extends AsyncTask<Void, integer, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mListView.onRefreshComplete();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if (resultCode==RESULT_OK) {
			Log.i(TAG, "onActivityResult"+resultCode+data.getStringExtra("key"));
			isFirstLoadData=true;
			start=0;
			rsz=8;
			q=data.getStringExtra("key");
			getMoreData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}

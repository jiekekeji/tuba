package com.jack.tuba;

import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
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


public class MainActivity extends Activity implements OnRefreshListener, OnLoadMoreListener, OnLoadDataListener, OnItemClickListener {

	private static final String TAG = "MainActivity";
	private LinkedList<Result> mListItems;
	private DisplayImageOptions options;
	private PullAndLoadListView mListView;
	private boolean pullOrLoad;
    
	private CommonAdapter mCommonAdapter;
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initOptions();
		initActionBar();
//		initDrawerMenu();
		initView();
	}
	
//	private void initDrawerMenu() {
//		// TODO Auto-generated method stub
//		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.navdrawer);
//
//
//        drawerArrow = new DrawerArrowDrawable(this) {
//            @Override
//            public boolean isLayoutRtl() {
//                return false;
//            }
//        };
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//            drawerArrow, R.string.drawer_open,
//            R.string.drawer_close) {
//
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//                invalidateOptionsMenu();
//            }
//
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                invalidateOptionsMenu();
//            }
//        };
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        mDrawerToggle.syncState();
//          
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//            android.R.layout.simple_list_item_1, android.R.id.text1, Constant.menuItem);
//        mDrawerList.setAdapter(adapter);
//        mDrawerList.setOnItemClickListener(this);
//	}

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
		mListView.setOnItemClickListener(this);
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
				.showImageOnLoading(R.drawable.empty_photo)
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(this,ImageDetailActivity.class);
		intent.putExtra("url", mListItems.get(position-1).getUrl());
		startActivity(intent);
		
	}


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
//                mDrawerLayout.closeDrawer(mDrawerList);
//            } else {
//                mDrawerLayout.openDrawer(mDrawerList);
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		// TODO Auto-generated method stub
//		 switch (position) {
//         case 0:
//             mDrawerToggle.setAnimateEnabled(false);
//             drawerArrow.setProgress(1f);
//             break;
//         case 1:
//             mDrawerToggle.setAnimateEnabled(false);
//             drawerArrow.setProgress(0f);
//             break;
//         case 2:
//             mDrawerToggle.setAnimateEnabled(true);
//             mDrawerToggle.syncState();
//             break;
//         case 4:
//             Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/IkiMuhendis/LDrawer"));
//             startActivity(browserIntent);
//             break;
//         case 5:
//             Intent share = new Intent(Intent.ACTION_SEND);
//             share.setType("text/plain");
//             share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//             share.putExtra(Intent.EXTRA_SUBJECT,
//                 getString(R.string.app_name));
//             share.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_description) + "\n" +
//                 "GitHub Page :  https://github.com/IkiMuhendis/LDrawer\n" +
//                 "Sample App : https://play.google.com/store/apps/details?id=" +
//                 getPackageName());
//             startActivity(Intent.createChooser(share,
//                 getString(R.string.app_name)));
//             break;
//         case 6:
//             String appUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();
//             Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl));
//             startActivity(rateIntent);
//             break;
//     }
//	}


}

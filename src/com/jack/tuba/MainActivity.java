package com.jack.tuba;

import java.util.LinkedList;

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

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.jack.tuba.adapter.CommonAdapter;
import com.jack.tuba.app.TuBaApp;
import com.jack.tuba.domain.Result;
import com.jack.tuba.utils.Constant;
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
            drawerArrow, R.string.drawer_open,
            R.string.drawer_close) {

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
            R.layout.item_drawer_list, R.id.drawerlist_item, Constant.menuItem);
        
        mDrawerList.setAdapter(adapter);
        DrawerOnItemClickListener mListener=new DrawerOnItemClickListener();
        mDrawerList.setOnItemClickListener(mListener);
	}
	/**
	 * mDrawerList监听者
	 * @author Administrator
	 *
	 */
	class DrawerOnItemClickListener implements AdapterView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent;
			switch (position) {
			case 0://我的下载
				intent=new Intent(MainActivity.this, MyDownLoadActivity.class);
				startActivity(intent);
				mDrawerLayout.closeDrawer(mDrawerList);
				break;
            case 1://应用推荐
            	intent=new Intent(MainActivity.this, RecommendAppActivity.class);
				startActivity(intent);
				mDrawerLayout.closeDrawer(mDrawerList);
				break;
			case 2://关于我们
				intent=new Intent(MainActivity.this, AboutUsActivity.class);
				startActivity(intent);
				mDrawerLayout.closeDrawer(mDrawerList);
				break;
            case 3://意见反馈
            	intent=new Intent(MainActivity.this, SuggestActivity.class);
				startActivity(intent);
				mDrawerLayout.closeDrawer(mDrawerList);
				break;
			case 4://设置中心
				intent=new Intent(MainActivity.this, SettingActivity.class);
				startActivity(intent);
				mDrawerLayout.closeDrawer(mDrawerList);
				break;
            case 5://查找更多
            	intent=new Intent(MainActivity.this, HelpActivity.class);
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
		ActionBar ab=getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
	}

	/**
	 * listView 初始化
	 */
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
		intent.putExtra("ivHeight", mListItems.get(position-1).getHeight());
		intent.putExtra("ivWidth", mListItems.get(position-1).getWidth());
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

}

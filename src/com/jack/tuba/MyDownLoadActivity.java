package com.jack.tuba;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.jack.tuba.adapter.MyDownLoadAdpter;
import com.jack.tuba.utils.TubaUtils;

/***
 * 展示已下载的图
 * 
 * @author Administrator
 *
 */
public class MyDownLoadActivity extends Activity implements
		OnMenuItemClickListener {

	private final String TAG = MyDownLoadActivity.class.getName();
	/**
	 * 下载文件夹
	 */
	private final String downLoadFile = "tubaDownLoad";
	/**
	 * 一个item可以左滑动的listView
	 */
	private SwipeMenuListView mListView;
	/**
	 * adapter
	 */
	private MyDownLoadAdpter mAdapter;
	/**
	 * 图片列表
	 */
	private List<File> imageList;
	/**
	 * 加载等待条
	 */
	private FrameLayout progessLayout;
	/**
	 * 空listVIew显示
	 */
	private TextView emptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydownload);
		initActionBar();
		initView();
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mListView = (SwipeMenuListView) findViewById(R.id.swipemenulistview_);
		progessLayout = (FrameLayout) findViewById(R.id.frame_progress_bar);

		emptyView = (TextView) findViewById(R.id.empty);
		emptyView.setVisibility(View.GONE);
		new LoadImageFileTask().execute();
	}

	/**
	 * 对SwipeMenuListView的初始化
	 */
	private void initSwipeMenuListView() {
		mAdapter = new MyDownLoadAdpter(imageList, this);
		mListView.setAdapter(mAdapter);
		SwipeMenuCreator creator = new MySwipeMenuCreator();
		// set creator
		mListView.setMenuCreator(creator);
		mListView.setOnMenuItemClickListener(this);
	}

	/**
	 * 得到下载目录下的所有文件
	 * 
	 * @return File[]
	 */
	private File[] getImageFiles() {
		// TODO Auto-generated method stub
		String targetPath = TubaUtils.getTubaDownloadPath(this, downLoadFile);
		File targetFile = new File(targetPath);
		if (!targetFile.exists()) {
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.i(TAG, e.toString());
			}
		}
		return targetFile.listFiles();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * SwipeItem 滑动事件的处理
	 * 
	 * @param position
	 *            listView item
	 * @param menu
	 * @param index
	 *            打开还是删除
	 * @return
	 */
	@Override
	public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			openImg(position);
			break;
		case 1:
			deleteImg(position);
			break;
		}
		return false;
	}

	/**
	 * 删除选中的图
	 * 
	 * @param position
	 * @param file
	 */
	private void deleteImg(int position) {
		File file = imageList.get(position);
		imageList.remove(position);
		file.delete();
		mAdapter.notifyDataSetChanged();
		int size=mAdapter.getCount();
		if (size<= 0) {
			mListView.setEmptyView(emptyView);
		}
		getActionBar().setTitle("当前下载数："+size);
		
	}

	/**
	 * 打开图
	 * 
	 * @param file
	 */
	private void openImg(int position) {
		// TODO Auto-generated method stub
//		File file = imageList.get(position);
//		String path = file.getAbsolutePath();
		Intent intent = new Intent(this, DoadImgActivity.class);
		intent.putExtra("position",position);
		intent.putExtra("files", (Serializable)imageList);
		startActivity(intent);
	}

	/**
	 * 加载本地的图
	 * 
	 * @author Administrator
	 *
	 */
	class LoadImageFileTask extends AsyncTask<Void, Void, File[]> {

		@Override
		protected File[] doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return getImags();
		}

		@Override
		protected void onPostExecute(File[] files) {
			// TODO Auto-generated method stub
			if (null == files || files.length == 0) {
				mListView.setEmptyView(emptyView);
				getActionBar().setTitle("当前下载数："+0);
			} else {
				imageList = new ArrayList<>(Arrays.asList(files));
				initSwipeMenuListView();
				getActionBar().setTitle("当前下载数："+files.length);
			}
			Log.i(TAG, "onPostExecute");
			progessLayout.setVisibility(View.GONE);
			super.onPostExecute(files);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progessLayout.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
	}

	/**
	 * 获取一下在的图
	 * 
	 * @return File[] 图文件
	 */
	private File[] getImags() {
		File[] imaFiles = getImageFiles();
		return imaFiles;
	}

	class MySwipeMenuCreator implements SwipeMenuCreator {
		@Override
		public void create(SwipeMenu menu) {
			// create "open" item
			SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
			// set item background
			openItem.setBackground(new ColorDrawable(Color
					.rgb(0xC9, 0xC9, 0xCE)));
			// set item width
			openItem.setWidth(TubaUtils.dp2px(90, MyDownLoadActivity.this));
			// set item title
			openItem.setTitle("Open");
			// set item title fontsize
			openItem.setTitleSize(18);
			// set item title font color
			openItem.setTitleColor(Color.WHITE);
			// add to menu
			menu.addMenuItem(openItem);
			// create "delete" item
			SwipeMenuItem deleteItem = new SwipeMenuItem(
					getApplicationContext());
			// set item background
			deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F,
					0x25)));
			// set item width
			deleteItem.setWidth(TubaUtils.dp2px(90, MyDownLoadActivity.this));
			// set a icon
			deleteItem.setIcon(R.drawable.ic_delete);
			// add to menu
			menu.addMenuItem(deleteItem);
		}
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}

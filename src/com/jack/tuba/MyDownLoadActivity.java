package com.jack.tuba;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.jack.tuba.adapter.MyDownLoadAdpter;
import com.jack.tuba.utils.TubaUtils;
/***
 * 展示已下载的图
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydownload);

		File[] imaFiles = getImageFiles();
		imageList = new ArrayList<>(Arrays.asList(imaFiles));

		if (null == imaFiles || imaFiles.length == 0) {
			TubaUtils.MyToast(this, "没有下载的");
			return;
		} else {
			initSwipeMenuListView();
		}

	}

	/**
	 * 对SwipeMenuListView的初始化
	 */
	private void initSwipeMenuListView() {
		mListView = (SwipeMenuListView) findViewById(R.id.swipemenulistview_);
		mAdapter = new MyDownLoadAdpter(imageList, this);
		mListView.setAdapter(mAdapter);

		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
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
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(TubaUtils
						.dp2px(90, MyDownLoadActivity.this));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);
		mListView.setOnMenuItemClickListener(this);
	}

	/**
	 * 
	 * 得到下载目录下的所有文件
	 * 
	 * @return
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
		File file = imageList.get(position);
		switch (index) {
		case 0:
			// open
			open(file);
			break;
		case 1:
			// delete
			imageList.remove(position);
			file.delete();
			// mAppList.remove(position);
			mAdapter.notifyDataSetChanged();
			break;
		}
		return false;
	}

	/**
	 * 	 打开
	 * @param file
	 */
	private void open(File file) {
		// TODO Auto-generated method stub
		String path=file.getAbsolutePath();
		Intent intent=new Intent(this, MySingleDownLoadAtivity.class);
		intent.putExtra("path",path);
		startActivity(intent);
	}

}

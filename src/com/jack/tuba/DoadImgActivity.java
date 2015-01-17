package com.jack.tuba;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

import com.jack.tuba.adapter.ImgPagerAdapter;
/**
 * 已下载图片的单个
 * @author jack
 *
 */
public class DoadImgActivity extends BaseActivity{
	
	private static final String TAG = DoadImgActivity.class.getName();
	
	private ImgPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_img);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		Intent i=getIntent();
		List<File> imgList = (List<File>) i.getSerializableExtra("files");
		int positon = i.getIntExtra("position",-1);
		Log.i(TAG, "positon"+positon);
		ViewPager mPager=(ViewPager) findViewById(R.id.pager);
		mAdapter = new ImgPagerAdapter(this, imgList);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(positon);
		mPager.setOnPageChangeListener(new MyPagerListener());
		changeTitle(positon+1);
	}
	/**
	 * 更改actionBar的标题
	 * @param selection
	 */
	private void changeTitle(int selection){
		getActionBar().setTitle(new StringBuffer("总也数:")
		.append(mAdapter.getCount())
		.append("当前页：")
		.append(selection));
	}
   /**
    * PageChangeListener
    * @author jack
    *
    */
	class MyPagerListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onPageScrollStateChanged="+arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onPageScrolled=="+arg0);
			Log.i(TAG, "onPageScrolled=="+arg1);
			Log.i(TAG, "onPageScrolled=="+arg2);
		}

		@Override
		public void onPageSelected(int selection) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onPageSelected="+selection);
			changeTitle(selection+1);
		}
		
	}
	

}

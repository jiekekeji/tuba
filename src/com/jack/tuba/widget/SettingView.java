package com.jack.tuba.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.jack.tuba.R;

public class SettingView extends RelativeLayout{

	private LayoutInflater inflater;
	private String nameSpace="http://schemas.android.com/apk/res/com.jack.tuba";
	
	public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		initVIew(context);
	}

	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		String settimg_src=attrs.getAttributeValue(nameSpace, "settimg_src");
		String settimg_text=attrs.getAttributeValue(nameSpace, "settimg_text");
		System.out.println(settimg_src+"xx"+settimg_text);
		initVIew(context);
	}

	public SettingView(Context context) {
		super(context);
		initVIew(context);
	}

	private void initVIew(Context context) {
		// TODO Auto-generated method stub
		inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.item_setting,null);
	}


	

}

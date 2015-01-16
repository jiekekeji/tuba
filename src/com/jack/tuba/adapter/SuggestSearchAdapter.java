package com.jack.tuba.adapter;

import com.jack.tuba.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SuggestSearchAdapter extends BaseAdapter{

	private String[]  suggestSearch;
	private Context context;
	
	public SuggestSearchAdapter(String[] array,Context context) {
		// TODO Auto-generated constructor stub
		suggestSearch=array;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return suggestSearch.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return suggestSearch[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view=View.inflate(context, R.layout.item_search_gridview,null);
		TextView tView=(TextView) view.findViewById(R.id.tv_gridview_item);
		tView.setText(suggestSearch[position]);
		return tView;
	}

}

package com.jack.tuba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jack.tuba.R;

public class DrawerListAdapter extends BaseAdapter{

	private int[] titles={R.string.drawer_titile1,
			R.string.drawer_title2,
			R.string.drawer_title3,
			R.string.drawer_title4,
			R.string.drawer_title5,
			R.string.drawer_title6};
	
//	private int[] icons={android.R.drawable.stat_sys_download,
//			android.R.drawable.stat_sys_download,
//			android.R.drawable.stat_sys_download,
//			android.R.drawable.stat_sys_download,
//			android.R.drawable.stat_sys_download,
//			android.R.drawable.stat_sys_download,		
//	};
	
	private LayoutInflater inflater;
	
	public DrawerListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.item_drawer_list,null);;
			holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.drawer_title);
			holder.image = (ImageView) view.findViewById(R.id.drawer_icon);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.text.setText(titles[position]);
//		holder.image.setImageResource(icons[position]);
		
		return null;
	}
	
	private static class ViewHolder {
		TextView text;
		ImageView image;
	}

}

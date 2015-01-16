package com.jack.tuba.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jack.tuba.R;
import com.jack.tuba.domain.HistorySearch;

public class HistorySearchAdapter extends BaseAdapter {

	private List<HistorySearch> list;

	private LayoutInflater inflater;

	public HistorySearchAdapter(Context context, List<HistorySearch> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
		final ViewHolder holder;
		if (convertView == null) {
			view = inflater
					.inflate(R.layout.item_search_history, null);
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.tv_history_item);
			holder.time=(TextView) view.findViewById(R.id.history_time);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		HistorySearch hs=list.get(position);
		holder.title.setText(hs.getKey());
		holder.time.setText(hs.getTime());

		return view;
	}

	private static class ViewHolder {
		TextView title;
		TextView time;
	}

}

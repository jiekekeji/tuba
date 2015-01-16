package com.jack.tuba.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jack.tuba.R;
import com.jack.tuba.utils.TubaUtils;
/**
 * 下载列表的适配器
 * @author Administrator
 *
 */
public class MyDownLoadAdpter extends BaseAdapter {

	private List<File> files;
	
	private Context context;
	
	public MyDownLoadAdpter(List<File> files,Context context) {
		// TODO Auto-generated constructor stub
		this.files=files;
		this.context=context;
	}
	@Override
	public int getCount() {
		return files.size();
	}

	@Override
	public File getItem(int position) {
		return files.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.item_mydownload, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
		File tempFile=files.get(position);
		String imagePath=tempFile.getAbsolutePath();
		int width=TubaUtils.dp2px(50, context);
		int height=TubaUtils.dp2px(50, context);
		Bitmap bitmap=TubaUtils.getImageThumbnail(imagePath, width, height);
		
		if (bitmap==null) {
			holder.iv_icon.setImageResource(R.id.image_title);
		}else{
			holder.iv_icon.setImageBitmap(bitmap);
		}
		
		holder.tv_name.setText(tempFile.getName());
		holder.tv_length.setText(String.valueOf(tempFile.length()/1024+"k"));
		return convertView;
	}

	class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
        TextView tv_length;
        
		public ViewHolder(View view) {
			iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			tv_name = (TextView) view.findViewById(R.id.tv_icom_name);
			tv_length = (TextView) view.findViewById(R.id.tv_icon_length);
			view.setTag(this);
		}
	}

}

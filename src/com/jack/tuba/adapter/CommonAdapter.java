package com.jack.tuba.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jack.tuba.R;
import com.jack.tuba.domain.Result;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class CommonAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private LinkedList<Result> results;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener;
	private int displayHeight;
	private int displayWidth;
	private String TAG="CommonAdapter";
	/**
	 * 
	 * @param context
	 * @param results
	 * @param options
	 * @param displayHeight
	 * @param displayWidth
	 */
	public CommonAdapter(Context context,
			LinkedList<Result> results,
			DisplayImageOptions options,
			int displayHeight,
			int displayWidth){
		
		this.results=results;
        this.options=options;
        this.displayHeight=displayHeight;
        this.displayWidth=displayWidth;
        animateFirstListener = new AnimateFirstDisplayListener();
        inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return results.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "CommonAdapter=="+results.get(position).getUrl());
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.item_image_listview, parent, false);
			holder = new ViewHolder();
//			holder.text = (TextView) view.findViewById(R.id.text);
			holder.image = (ImageView) view.findViewById(R.id.iv_item_image_list_big);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		int imageHeight=results.get(position).getHeight();
		int imageWidth=results.get(position).getWidth();
		
		if (imageWidth>=displayWidth) {
			float sca=imageWidth/displayWidth;
			
			holder.image.getLayoutParams().width=displayWidth;
			holder.image.getLayoutParams().height=(int) (imageHeight/sca);
		}else {
			float sca=displayHeight/imageHeight;
			holder.image.getLayoutParams().width=displayWidth;
			holder.image.getLayoutParams().height=(int) (imageHeight*sca);
		}
	
		ImageLoader.getInstance().displayImage(results.get(position).getUrl(), 
				holder.image,
				options, 
				animateFirstListener);
		return view;
	}
		
	/**
	 * 在activity退出之前调用
	 */
	public void clearAnimateFirstDisplayListener(){
		AnimateFirstDisplayListener.displayedImages.clear();
	}
	
	private static class ViewHolder {
//		TextView text;
		ImageView image;
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
}

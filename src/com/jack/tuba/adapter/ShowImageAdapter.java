package com.jack.tuba.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jack.tuba.ImageOrigin;
import com.jack.tuba.R;
import com.jack.tuba.domain.Result;
import com.jack.tuba.widget.ListTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
/**
 * 主页展示图片的adapter
 * @author jack
 *
 */
public class ShowImageAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private LinkedList<Result> results;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener;
	private int displayHeight;
	private int displayWidth;
	private static final String TAG=ShowImageAdapter.class.getName();
	private Context context;
	/**
	 * 
	 * @param context
	 * @param results
	 * @param options
	 * @param displayHeight
	 * @param displayWidth
	 */
	public ShowImageAdapter(Context context,
			LinkedList<Result> results,
			DisplayImageOptions options,
			int displayHeight,
			int displayWidth){
		this.context=context;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "CommonAdapter=="+results.get(position).getUrl());
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.item_image_listview, parent, false);
			holder = new ViewHolder();
			holder.title = (ListTextView) view.findViewById(R.id.image_title);
			holder.image = (ImageView) view.findViewById(R.id.iv_item_image_list_big);
			holder.moreUrl=(ListTextView) view.findViewById(R.id.more_image);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "TitleNoFormatting"+results.get(position).getTitleNoFormatting());
			}
		});
		
		holder.moreUrl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,ImageOrigin.class );
				intent.putExtra("origin_url",results.get(position).getOriginalContextUrl());
				context.startActivity(intent);
				Log.i(TAG, "OriginalContextUrl"+results.get(position).getOriginalContextUrl());
			}
		});
				
		int imageHeight=results.get(position).getHeight();
		int imageWidth=results.get(position).getWidth();
		
		Log.i(TAG, "trueheight"+imageHeight);
		Log.i(TAG, "truewidth"+imageWidth);
		
		Log.i(TAG, "displayheight"+displayHeight);
		Log.i(TAG, "tdisplaywidth"+displayWidth);
		
		
		if (imageWidth>=displayWidth) {
			float sca=imageWidth/displayWidth;
			Log.i(TAG, "sca"+sca);
			holder.image.getLayoutParams().width=displayWidth;
			holder.image.getLayoutParams().height=(int) (imageHeight/sca+0.5);
		}else {
			float sca=displayWidth/imageWidth;
			Log.i(TAG, "sca"+sca);
			holder.image.getLayoutParams().width=displayWidth;
			holder.image.getLayoutParams().height=(int) (imageHeight*sca+0.5);
		}
////		holder.title.getLayoutParams().width=LayoutParams.MATCH_PARENT;
////		holder.title.getLayoutParams().height=45;
////		
////		holder.moreUrl.getLayoutParams().width=LayoutParams.MATCH_PARENT;
////		holder.moreUrl.getLayoutParams().height=45;
		Log.i(TAG, "lastheight"+holder.image.getLayoutParams().height);
		Log.i(TAG, "lastwidth"+holder.image.getLayoutParams().width);
		Log.i(TAG, "============================================");
	    holder.title.setText(results.get(position).getTitleNoFormatting());
	    holder.moreUrl.setText("点击查看图片来源");
		
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
		ListTextView title;
		ImageView image;
		ListTextView moreUrl;
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

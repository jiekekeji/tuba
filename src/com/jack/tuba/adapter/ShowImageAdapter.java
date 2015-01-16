package com.jack.tuba.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jack.tuba.ImageOrigin;
import com.jack.tuba.R;
import com.jack.tuba.domain.Result;
import com.jack.tuba.widget.ListTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 主页展示图片的adapter
 * 
 * @author jack
 *
 */
public class ShowImageAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private LinkedList<Result> results;
	private DisplayImageOptions options;
	private ImageLoadingListener loadingListener;
	private int displayHeight;
	private int displayWidth;
	private static final String TAG = ShowImageAdapter.class.getName();
	private Context context;

	/**
	 * 
	 * @param context
	 * @param results
	 * @param options
	 * @param displayHeight
	 * @param displayWidth
	 */
	public ShowImageAdapter(Context context, LinkedList<Result> results,
			DisplayImageOptions options, int displayHeight, int displayWidth) {
		this.context = context;
		this.results = results;
		this.options = options;
		this.displayHeight = displayHeight;
		this.displayWidth = displayWidth;
		loadingListener = new LoadingListener();
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
		Log.i(TAG, "CommonAdapter==" + results.get(position).getUrl());
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = inflater
					.inflate(R.layout.item_image_listview, parent, false);
			holder = new ViewHolder();
			holder.title = (ListTextView) view.findViewById(R.id.image_title);
			holder.image = (ImageView) view
					.findViewById(R.id.iv_item_image_list_big);
			holder.moreUrl = (ListTextView) view.findViewById(R.id.more_image);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "TitleNoFormatting"
						+ results.get(position).getTitleNoFormatting());
			}
		});

		holder.moreUrl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ImageOrigin.class);
				intent.putExtra("origin_url", results.get(position)
						.getOriginalContextUrl());
				context.startActivity(intent);
				Log.i(TAG, "OriginalContextUrl"
						+ results.get(position).getOriginalContextUrl());
			}
		});

		int imageHeight = results.get(position).getHeight();
		int imageWidth = results.get(position).getWidth();

		Log.i(TAG, "trueheight" + imageHeight);
		Log.i(TAG, "truewidth" + imageWidth);

		Log.i(TAG, "displayheight" + displayHeight);
		Log.i(TAG, "tdisplaywidth" + displayWidth);

		double iw = imageWidth;
		double dw = displayWidth;
		if (imageWidth >= displayWidth) {

			double sca = iw / dw;
			Log.i(TAG, "sca" + sca);
			holder.image.getLayoutParams().width = displayWidth;
			holder.image.getLayoutParams().height = (int) (imageHeight / sca + 0.5);
		} else {
			double sca = dw / iw;
			Log.i(TAG, "sca" + sca);
			holder.image.getLayoutParams().width = displayWidth;
			holder.image.getLayoutParams().height = (int) (imageHeight * sca + 0.5);
		}
		// // holder.title.getLayoutParams().width=LayoutParams.MATCH_PARENT;
		// // holder.title.getLayoutParams().height=45;
		// //
		// // holder.moreUrl.getLayoutParams().width=LayoutParams.MATCH_PARENT;
		// // holder.moreUrl.getLayoutParams().height=45;
		Log.i(TAG, "lastheight" + holder.image.getLayoutParams().height);
		Log.i(TAG, "lastwidth" + holder.image.getLayoutParams().width);
		Log.i(TAG, "============================================");
		holder.title.setText(results.get(position).getTitleNoFormatting());
		holder.moreUrl.setText("点击查看图片来源");

		ImageLoader.getInstance().displayImage(results.get(position).getUrl(),
				holder.image, options, loadingListener);
		return view;
	}

	/**
	 * 在activity退出之前调用
	 */
	// public void clearAnimateFirstDisplayListener(){
	// loadingListener.displayedImages.clear();
	// }

	private static class ViewHolder {
		ListTextView title;
		ImageView image;
		ListTextView moreUrl;
	}

	/**
	 * 图片加载监听者
	 * 
	 * @author jack
	 *
	 */
	private class LoadingListener implements ImageLoadingListener {

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLoadingFailed(String imageUri, View view, FailReason arg2) {
			// TODO Auto-generated method stub
			ImageView iv = (ImageView) view;
			iv.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
			iv.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
			iv.setImageResource(R.drawable.ic_fail);

		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// TODO Auto-generated method stub

		}

	}

}

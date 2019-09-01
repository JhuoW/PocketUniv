package com.byl.imageselector.adapter;

import java.util.List;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PubSelectedImgsAdapter extends BaseAdapter {

	Context context;
	List<String> list;
	OnItemClickClass onItemClickClass;
	private DisplayImageOptions displayImageOptions;
	
	public PubSelectedImgsAdapter(Context context,List<String> data,OnItemClickClass onItemClickClass) {
		this.context=context;
		this.list=data;
		this.onItemClickClass=onItemClickClass;
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.image_error)
				.showImageOnFail(R.drawable.image_error).build();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		Holder holder;
		if (view==null) {
			view=LayoutInflater.from(context).inflate(R.layout.pub_selected_imgs_item, null);
			holder=new Holder();
			holder.imageView=(ImageView) view.findViewById(R.id.imageView2);
			holder.delete_img=(ImageView) view.findViewById(R.id.delete_img);
			view.setTag(holder);
		}else {
			holder= (Holder) view.getTag();
		}
		//ImageLoader.getInstance().displayImage("file://"+list.get(position),holder.imageView,R.drawable.default_error);
		ImageLoader.getInstance().displayImage("file://"+list.get(position), holder.imageView, displayImageOptions);
		//ImageLoader.getInstance().displayImage(list.get(position),holder.imageView);
		System.out.println("路径："+list.get(position));
		holder.delete_img.setOnClickListener(new OnPhotoClick(list.get(position)));
		return view;
	}
	
	class Holder{
		ImageView imageView,delete_img;
	}


	public interface OnItemClickClass{
		public void OnItemClick(View v,String filepath);
	}
	
	class OnPhotoClick implements OnClickListener{
		String filepath;
		
		public OnPhotoClick(String  filepath) {
			this.filepath=filepath;
		}
		@Override
		public void onClick(View v) {
			if (list!=null && onItemClickClass!=null ) {
				onItemClickClass.OnItemClick(v, filepath);
			}
		}
	}
	
}

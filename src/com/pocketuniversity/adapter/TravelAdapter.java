package com.pocketuniversity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.aphidmobile.utils.UI;
import com.pocketuniversity.bean.History;
import com.pocketuniversity.function.activity.HistoryActivity;
import com.pocketuniversity.view.HeaderLayout;

public class TravelAdapter extends BaseAdapter {

	List<History> list = new ArrayList<History>();
	private LayoutInflater inflater;
	private int repeatCount = 1;
	private DisplayImageOptions displayImageOptions;
	Context context;

	public TravelAdapter(Context context,List<History> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.ic_default_head)
				.showImageOnFail(R.drawable.ic_default_head).build();
	}

	public int getRepeatCount() {
	    return repeatCount;
	  }

	public void setRepeatCount(int repeatCount) {
	    this.repeatCount = repeatCount;
	  }

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View layout = convertView;
	    if (convertView == null) {
	      layout = inflater.inflate(R.layout.complex1, null);
	    }

	    History history = list.get(position);
	    
	    
	    UI
	        .<TextView>findViewById(layout, R.id.title)
	        .setText(position+1+"."+history.getYear()+"."+history.getMonth()+"."+history.getDay());

	    String imgUrl = history.getImg();
	    
	    if (imgUrl.length() > 0) {
	    	 UI
	 	        .<ImageView>findViewById(layout, R.id.photo).setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(imgUrl,
		    		 UI
		 	        .<ImageView>findViewById(layout, R.id.photo), displayImageOptions);
		} else {
			 UI
	 	        .<ImageView>findViewById(layout, R.id.photo).setVisibility(View.GONE);
		}
	   
	        

	    UI
	        .<TextView>findViewById(layout, R.id.description)
	        .setText(history.getTitle());
	    
	    HeaderLayout headerLayout;
	    headerLayout = UI.<HeaderLayout>findViewById(layout, R.id.headerLayout);
	    headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HistoryActivity)context).finish();
			}
		});
	    headerLayout.showTitle("历史上的今天");
//	    UI
//	        .<Button>findViewById(layout, R.id.wikipedia)
//	        .setOnClickListener(new View.OnClickListener() {
//	          @Override
//	          public void onClick(View v) {
//	            Intent intent = new Intent(
//	                Intent.ACTION_VIEW,
//	                Uri.parse(data.link)
//	            );
//	            inflater.getContext().startActivity(intent);
//	          }
//	        });

	    return layout;
	}
	
	 public void removeData(int index) {
		    if (list.size() > 1) {
		    	list.remove(index);
		    }
		  }
}

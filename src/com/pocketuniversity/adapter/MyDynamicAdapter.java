package com.pocketuniversity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.pocketuniversity.bean.MyDynamic;
import com.pocketuniversity.friendcircle.activity.ImagePagerActivity;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.image.NoScrollGridView;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.utils.StringUtils;
import com.pocketuniversity.view.MyProgressDialog;

public class MyDynamicAdapter  extends BaseAdapter {
	private List<MyDynamic> list;
	private LayoutInflater inflater;
	private DisplayImageOptions displayImageOptions;

	MyProgressDialog dialog;

	Context context;

	public MyDynamicAdapter(Context context, List<MyDynamic> list,
			LayoutInflater inflater) {
		this.context = context;
		this.list = list;
		this.inflater = inflater;
		dialog = new MyProgressDialog(context);
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.ic_default_head)
				.showImageOnFail(R.drawable.ic_default_head).build();
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
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.friend_mydynamic_item, null);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			viewHolder.day = (TextView) convertView.findViewById(R.id.day);
			viewHolder.month = (TextView) convertView.findViewById(R.id.month);
			viewHolder.gridview = (NoScrollGridView) convertView.findViewById(R.id.gridview);
			viewHolder.delete = (TextView) convertView.findViewById(R.id.delete);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final MyDynamic myDynamic = list.get(position);
		viewHolder.content.setText(myDynamic.getContent());
		viewHolder.day.setText(myDynamic.getDay());
		
		String month = myDynamic.getMonth();
		if(month.equals("01")){
			viewHolder.month.setText(R.string.one);
		}else if(month.equals("02")){
			viewHolder.month.setText(R.string.two);
		}else if(month.equals("03")){
			viewHolder.month.setText(R.string.three);
		}else if(month.equals("04")){
			viewHolder.month.setText(R.string.four);
		}else if(month.equals("05")){
			viewHolder.month.setText(R.string.five);
		}else if(month.equals("06")){
			viewHolder.month.setText(R.string.six);
		}else if(month.equals("07")){
			viewHolder.month.setText(R.string.seven);
		}else if(month.equals("08")){
			viewHolder.month.setText(R.string.eight);
		}else if(month.equals("09")){
			viewHolder.month.setText(R.string.night);
		}else if(month.equals("10")){
			viewHolder.month.setText(R.string.ten);
		}else if(month.equals("11")){
			viewHolder.month.setText(R.string.elenve);
		}else if(month.equals("12")){
			viewHolder.month.setText(R.string.twelve);
		}
		
		String imgUrl = myDynamic.getImgUrl();
		String s1 = StringUtils.trimFirstAndLastChar(imgUrl, "[".charAt(0));
		String s2 = StringUtils.trimFirstAndLastChar(s1, "]".charAt(0));
		String s4;
		final ArrayList<String> list = new ArrayList<String>();
		String[] stringArr = s2.split(",");

		if (imgUrl.equals("[\"\"]")) {

		} else {
			for (int i = 0; i < stringArr.length; i++) {
				String s = stringArr[i];
				String s3 = StringUtils.trimFirstAndLastChar(s, "\"".charAt(0));
				s4 = StringUtils.trimFirstAndLastChar(s3, "\"".charAt(0));
				System.out.println("s4:" + s4);
				String s5 = s4.replace("\\", "");
				list.add(s5);
			}
		}

		// for (int i = 0; i < stringArr.length; i++) {
		// list.add(stringArr[i]);
		// }

		if (TextUtils.isEmpty(imgUrl)) {
			viewHolder.gridview.setVisibility(View.GONE);
		} else {
			viewHolder.gridview.setAdapter(new NoScrollGridAdapter(context,
					list));
		}

		// 点击回帖九宫格，查看大图
		viewHolder.gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				imageBrower(position, list);
			}
		});
		
		final String id = myDynamic.getId();
		viewHolder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE).setTitleText("是否确定删除？")
				.showCancelButton(true).setCancelText("取消").setConfirmText("确定").setConfirmClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(final SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						new SimpleNetTask(context) {
							boolean flag;
							@Override
							protected void onSucceed() {
								// TODO Auto-generated method stub
								if(flag){
									//list.remove(position);
									//notifyDataSetChanged();
									update(position);
									Toast.makeText(context, "删除动态成功", Toast.LENGTH_SHORT).show();
									sweetAlertDialog.dismiss();
								}else{
									Toast.makeText(context, "删除动态失败", Toast.LENGTH_SHORT).show();
									sweetAlertDialog.dismiss();
								}
							}
							
							@Override
							protected void doInBack() throws Exception {
								// TODO Auto-generated method stub
								String jsonStr = new APIHelper().deleteDynamic(id);
								flag = GetObjectFromService.getSimplyResult(jsonStr);
							}
						}.execute();
					}
				}).setCancelClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						sweetAlertDialog.dismiss();
					}
				}).show();
			}
		});
		return convertView;
	}
	
	public class ViewHolder {
		private TextView content;
		private NoScrollGridView gridview;
		private TextView day;
		private TextView month;
		private TextView delete;
	}
	
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		context.startActivity(intent);
	}
	
	
	public void update(int position) {
		loading(position);
	}
	
	public void loading(final int position){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				list.remove(position);
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		}).start();
	}
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};
}

package com.pocketuniversity.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.activity.UserDetailActivity;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.Comment;
import com.pocketuniversity.bean.FriendsCircle;
import com.pocketuniversity.bean.InnerComment;
import com.pocketuniversity.friendcircle.activity.FriendsCircleActivity;
import com.pocketuniversity.friendcircle.activity.FriendsCircleDetailActivity;
import com.pocketuniversity.friendcircle.activity.ImagePagerActivity;
import com.pocketuniversity.friendscircle.db.FriendsCircleDBHelper;
import com.pocketuniversity.friendscircle.db.FriendsCommentDB;
import com.pocketuniversity.https.APIHelper;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.image.NoScrollGridView;
import com.pocketuniversity.ui.xlist.XListView;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.utils.StringUtils;
import com.pocketuniversity.view.MyListView;
import com.pocketuniversity.view.RoundImageView;

public class FriendAdapter extends BaseAdapter implements XListView.IXListViewListener,OnItemClickListener {

	private List<FriendsCircle> list;
	private LayoutInflater inflater;
	private DisplayImageOptions displayImageOptions;

	FriendsCircleDBHelper dbHelper;
	Context context;

	public FriendAdapter(Context context, List<FriendsCircle> list,
			LayoutInflater inflater) {
		dbHelper = new FriendsCircleDBHelper(context);
		this.context = context;
		this.list = list;
		this.inflater = inflater;
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
			convertView = inflater.inflate(R.layout.friend_items, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.time = (TextView) convertView.findViewById(R.id.date);
			viewHolder.ll_thumb = (LinearLayout) convertView
					.findViewById(R.id.ll_thumb);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.content_text);
			viewHolder.header = (RoundImageView) convertView
					.findViewById(R.id.photo);
			viewHolder.commentCount = (TextView) convertView
					.findViewById(R.id.tv_comment);
			viewHolder.zancount = (TextView) convertView
					.findViewById(R.id.tv_thumb);
			viewHolder.gridview = (NoScrollGridView) convertView
					.findViewById(R.id.gridview);
			viewHolder.img_thumb = (ImageView) convertView
					.findViewById(R.id.img_thumb);
			viewHolder.commentList = (MyListView) convertView.findViewById(R.id.friend_list);
			viewHolder.replyLayout = (LinearLayout) convertView.findViewById(R.id.reply_content);
			viewHolder.content_layout = (RelativeLayout) convertView.findViewById(R.id.content_layout);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final FriendsCircle friendCircle = list.get(position);
		viewHolder.name.setText(friendCircle.getName());
		viewHolder.time.setText(friendCircle.getTime());
		viewHolder.content.setText(friendCircle.getContent());
		viewHolder.commentCount.setText(friendCircle.getCommentCount());
		viewHolder.zancount.setText(friendCircle.getZanCount());
		
		
		String headerUrl = friendCircle.getHeader();
		System.out.println("头像：" + headerUrl);
		ImageLoader.getInstance().displayImage(headerUrl, viewHolder.header,
				displayImageOptions);
		// 使用ImageLoader加载网络图片
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisc(true).bitmapConfig(Config.RGB_565)// 设置最低配置
				.build();//

		String imgUrl = friendCircle.getImgUrl();
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
//		viewHolder.gridview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
////				imageBrower(position, list);
//				Log.i("FriendAdapter", "点击："+position);
//			}
//		});
		
		viewHolder.gridview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				 //阻止手指离开时onTouch方法的继续执行  
		        if(event.getAction() == MotionEvent.ACTION_UP){  
		        	  System.out.println("ontouch方法被执行");
						Intent intent = new Intent(context,
								FriendsCircleDetailActivity.class);
						intent.putExtra("post", friendCircle);
						intent.putExtra("position", position + "");
						((FriendsCircleActivity)context).startActivityForResult(intent,1000);
						((FriendsCircleActivity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
						return true;
		        }  
				return true;
			}
		});
		
		String isZan = friendCircle.getIsZan();
		
		if (isZan.equals("true")) {
			viewHolder.img_thumb.setImageResource(R.drawable.ic_thumb_checked);
			friendCircle.setIsZan("true");
		} else {
			viewHolder.img_thumb.setImageResource(R.drawable.ic_thumb_uncheck);
			friendCircle.setIsZan("false");
		}

		final String id = friendCircle.getId();
		System.out.println("id:" + id);
		viewHolder.ll_thumb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new SimpleNetTask(context) {
					boolean temp;
					String jsonStr ;
					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						if (temp) {
							if (friendCircle.getIsZan().equals("true")) {
								viewHolder.img_thumb
										.setImageResource(R.drawable.ic_thumb_uncheck);
								int zanCount = Integer
										.parseInt(viewHolder.zancount.getText()
												.toString());
								int newZanCount = zanCount - 1;
								viewHolder.zancount.setText(newZanCount + "");
								friendCircle.setIsZan("false");
							} else {
								viewHolder.img_thumb
										.setImageResource(R.drawable.ic_thumb_checked);
								int zanCount = Integer
										.parseInt(viewHolder.zancount.getText()
												.toString());
								int newZanCount = zanCount + 1;
								viewHolder.zancount.setText(newZanCount + "");
								friendCircle.setIsZan("true");
							}
						}
					}

					@Override
					protected void doInBack() throws Exception {
						// TODO Auto-generated method stub
						if (friendCircle.getIsZan().equals("true")) {
							jsonStr = new APIHelper().CancelZan(id);
							
						} else {
							jsonStr = new APIHelper().AddZan(id);
						}
						temp = GetObjectFromService
								.getSimplyResult(jsonStr);
					}
				}.execute();
			}
		});

		viewHolder.header.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						UserDetailActivity.class);
				intent.putExtra("userId", friendCircle.getUserId());
				context.startActivity(intent);
			}
		});
		
		final InnerComment innerComment = new InnerComment();
		FriendCommentAdapter adapter;
		final List<Comment> commentdata = new ArrayList<Comment>();
		adapter = new FriendCommentAdapter(commentdata, context);
		innerComment.setAdapter(adapter);
		viewHolder.commentList.setAdapter(innerComment.getAdapter());
		final int pageSize = 5;
		final int currentPage = 1;
		
		new SimpleNetTask(context) {
			List<Comment> temp = new ArrayList<Comment>();
			@Override
			protected void onSucceed() {
				temp = dbHelper.queryCommentDynamic(currentPage * pageSize + "");
				commentdata.clear();
				commentdata.addAll(temp);
				//adapter.notifyDataSetChanged();
				FriendCommentAdapter adapter = new FriendCommentAdapter(commentdata, context);
				innerComment.setAdapter(adapter);
				viewHolder.commentList.setAdapter(innerComment.getAdapter());
				System.out.println("长度："+temp.size());
				// 判断是否有回复内容
				if (listIsEmpty(commentdata)) {
					viewHolder.replyLayout.setVisibility(View.GONE);
				} else {
					viewHolder.replyLayout.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", id);
				params.put("currentPage", currentPage+"");
				params.put("currentPageSize", pageSize+"");
				String jsonStr = new WebService(C.GETFRIENDCIRCLECOMMENT, params).getReturnInfo();
				JSONObject json = new JSONObject(jsonStr);
				if (json.get("ret").equals("success")) {
					JSONArray jsonarray = json.getJSONArray("comments");
					dbHelper.deleteAllDataFromTable(FriendsCommentDB.TABLE_NAME);
					dbHelper.insertComment(jsonarray);
				}
			}
		}.execute();
		
		return convertView;
	}

	public class ViewHolder {
		public TextView name;
		public TextView time;
		public TextView content;
		public TextView zancount;
		public TextView commentCount;
		public RoundImageView header;
		public NoScrollGridView gridview;
		public ImageView img_thumb;
		public LinearLayout ll_thumb;
		public MyListView commentList;
		public LinearLayout replyLayout;
		public RelativeLayout content_layout;
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		context.startActivity(intent);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 判断集合是否是需要的格式 （不为null size>0）
	 * 
	 * @param list
	 * @return
	 */
	private boolean listIsEmpty(List list) {
		if (list != null && list.size() > 0)
			return false;
		return true;

	}
	
}

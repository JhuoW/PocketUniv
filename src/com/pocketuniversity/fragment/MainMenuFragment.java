package com.pocketuniversity.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.ab.bitmap.AbImageCache;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskQueue;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.ab.utils.AbDialogUtil;
import com.pocketuniversity.ab.utils.AbFileUtil;
import com.pocketuniversity.activity.LoginActivity;
import com.pocketuniversity.activity.MainActivity;
import com.pocketuniversity.activity.SuggestionActivity;
import com.pocketuniversity.adapter.SlidingMenuAdapter;
import com.pocketuniversity.base.App;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.more.activity.ChangePasswordActivity;
import com.pocketuniversity.more.activity.MyDataActivity;
import com.pocketuniversity.more.activity.MyQRActivity;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.PhotoUtils;

public class MainMenuFragment extends Fragment {
	private Activity mActivtity;
	private List<HashMap<String, Object>> list = null;
	private ListView mAbPullListView = null;
	private int currentPage = 1;
	private AbTaskQueue mAbTaskQueue = null;
	private int total = 50;
	private int pageSize = 5;
	private SimpleAdapter adapter;
	private Button btn_quitLogin;
	private Button btn_clear_save;
	private ImageView img_userphoto ;
	private TextView tv_name ;
	
	private TextView tv_class ;
	
	private TextView tv_authority ;
	
	private PUUser curUser ;
	
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	
	List<String> titleList = new ArrayList<String>();
	List<Integer> imgList = new ArrayList<Integer>(); 
	private SlidingMenuAdapter slidAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mActivtity = this.getActivity();
		View view = inflater.inflate(R.layout.main_menu, null);
		
		curUser = new PreferenceMap(getActivity()).getUser();
		
		mAbTaskQueue = AbTaskQueue.getInstance();
		//获取ListView对象
		mAbPullListView = (ListView) view.findViewById(R.id.menu_list);
		btn_quitLogin = (Button) view.findViewById(R.id.quitLogin);
		btn_clear_save = (Button) view.findViewById(R.id.clear_save);
		
		tv_name = (TextView) view.findViewById(R.id.user_name);
		tv_authority = (TextView) view.findViewById(R.id.user_authority);
		tv_class = (TextView) view.findViewById(R.id.user_class);
		
		tv_authority.setText(curUser.getAuthority());
		
		tv_name.setText(curUser.getName());
		tv_class.setText(curUser.getAddress());
		
		img_userphoto = (ImageView) view.findViewById(R.id.user_photo);
		String url = curUser.getImage();
		
		imageLoader
		.displayImage(
				url,
				img_userphoto,
				PhotoUtils
						.getImageOptions(R.drawable.icon_default_avatar_selector));
		
		img_userphoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), MyDataActivity.class);
				startActivity(intent);
			}
		});
		
		
		if(!TextUtils.isEmpty(EMChatManager.getInstance().getCurrentUser())){
			btn_quitLogin.setText(getString(R.string.button_logout) + "(" + EMChatManager.getInstance().getCurrentUser() + ")");
		}
		
		btn_quitLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
				.showCancelButton(true).setTitleText("是否确定退出登录？").setConfirmText("确定")
				.setCancelText("取消").setConfirmClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(final SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						App.getInstance().logout(new EMCallBack() {
							@Override
							public void onSuccess() {
								
								getActivity().runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										sweetAlertDialog.dismiss();
										PUUser user = new PUUser();
										new PreferenceMap(getActivity()).setUser(user);
										getActivity().finish();
										startActivity(new Intent(getActivity(), LoginActivity.class));

									}
								});
							}

							@Override
							public void onProgress(int arg0, String arg1) {

							}

							@Override
							public void onError(int arg0, String arg1) {

							}
						});

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
		
		btn_clear_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AbDialogUtil.showProgressDialog(getActivity(),0, "正在清空缓存...");
				AbTask task = new AbTask();
				// 定义异步执行的对象
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListener() {

					@Override
					public void update() {
						AbDialogUtil.removeDialog(getActivity());
						Toast.makeText(getActivity(), "缓存已清空完成", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void get() {
						try {
							AbFileUtil.clearDownloadFile();
							AbImageCache.removeAllBitmapFromCache();
						} catch (Exception e) {
							Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

						}
					};
				});
				task.execute(item);
			}
		});
		
//		list = new ArrayList<HashMap<String,Object>>();
//		HashMap<String, Object> map1 = new HashMap<String, Object>();
//		map1.put("key", "用户");
//		HashMap<String, Object> map2 = new HashMap<String, Object>();
//		map2.put("key", "关于我们");
//		HashMap<String, Object> map3 = new HashMap<String, Object>();
//		map3.put("key", "反馈意见");
//		HashMap<String, Object> map5 = new HashMap<String, Object>(); 
//		map5.put("key", "修改密码");
//		HashMap<String, Object> map6 = new HashMap<String, Object>();
//		map6.put("key", "我的二维码");
//		list.add(map1);
//		list.add(map2);
//		list.add(map3);
//		list.add(map5);
//		list.add(map6);
//		//使用自定义Adapter
//		adapter = new SimpleAdapter(getActivity(), list, android.R.layout.simple_list_item_1, new String[]{"key"}, new int[]{android.R.id.text1});
		
		titleList.add("用户");
		titleList.add("关于我们");
		titleList.add("反馈意见");
		titleList.add("修改密码");
		titleList.add("我的二维码");
		
		imgList.add(R.drawable.avatar_grassroot);
		imgList.add(R.drawable.ic_drawer_search_normal);
		imgList.add(R.drawable.ic_drawer_feedback_normal);
		imgList.add(R.drawable.ic_drawer_offline_normal);
		imgList.add(R.drawable.a_5);

		slidAdapter = new SlidingMenuAdapter(titleList, imgList, getActivity());
		
		mAbPullListView.setAdapter(slidAdapter);
		
		//item点击事件
		mAbPullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position == 0){
					startActivity(new Intent(getActivity(), MyDataActivity.class));
				}
				if(position == 1){
					
				}
				if(position == 2){
					startActivity(new Intent(getActivity(), SuggestionActivity.class));
				}
				if(position == 3){
					Intent intent  = new Intent(getActivity(), ChangePasswordActivity.class);
					startActivity(intent);
				}
				if(position == 4){
					startActivity(new Intent(getActivity(), MyQRActivity.class));
				}
			}
		});
		
		return view;
	}
	
	void logout() {
		final ProgressDialog pd = new ProgressDialog(getActivity());
		String st = getResources().getString(R.string.Are_logged_out);
		pd.setMessage(st);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		App.getInstance().logout(new EMCallBack() {
			
			@Override
			public void onSuccess() {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						pd.dismiss();
						// 重新显示登陆页面
						((MainActivity) getActivity()).finish();
						startActivity(new Intent(getActivity(), LoginActivity.class));
						
					}
				});
			}
			
			@Override
			public void onProgress(int progress, String status) {
				
			}
			
			@Override
			public void onError(int code, String message) {
				
			}
		});
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		PUUser curUser = new PreferenceMap(App.ctx).getUser();
		tv_name.setText(curUser.getName());
		System.out.println("curUser--->"+ curUser.getName());
		String url=curUser.getImage();
		imageLoader
		.displayImage(
				url,
				img_userphoto,
				PhotoUtils
						.getImageOptions(R.drawable.icon_default_avatar_selector));
	}
}

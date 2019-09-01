package com.pocketuniversity.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.SearchGroup;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.PhotoUtils;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;
import com.pocketuniversity.view.RoundImageView;

public class GroupSearchResultActivity extends BaseActivity{

	private TextView tv_groupName;
	private TextView tv_servergroupId;
	private RoundImageView img_ownerHeader;
	private TextView tv_ownerNickName;
	private TextView tv_description;
	private TextView tv_curNum;
	private Button btn_addGroup;
	MyProgressDialog dialog;
	SearchGroup searchGroup ;
	public static ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_search_result);
		initView();
		initAction();
	}
	
	private void initView(){
		dialog = new MyProgressDialog(ctx);
		searchGroup = new PreferenceMap(ctx).getSearchGroup();
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("群详情");
		tv_groupName = (TextView) findViewById(R.id.tv_groupName);
		tv_servergroupId = (TextView) findViewById(R.id.tv_servergroupId);
		img_ownerHeader = (RoundImageView) findViewById(R.id.ownerheader);
		tv_ownerNickName = (TextView) findViewById(R.id.ownerNickname);
		tv_description = (TextView) findViewById(R.id.description);
		tv_curNum = (TextView) findViewById(R.id.curNum);
		btn_addGroup = (Button) findViewById(R.id.btn_addgroup);
	}
	
	private void initAction(){
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GroupSearchResultActivity.this.finish();
			}
		});
		tv_groupName.setText(searchGroup.getGroupName());
		tv_servergroupId.setText(searchGroup.getServerGroupId());
		String imgUrl = searchGroup.getOwnerHeader();
		imageLoader.displayImage(imgUrl, img_ownerHeader,PhotoUtils.getImageOptions(R.drawable.default_avatar));
		tv_ownerNickName.setText(searchGroup.getOwnerNickName());
		tv_description.setText(searchGroup.getDescription());
		tv_curNum.setText(searchGroup.getCurNum());
		btn_addGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getJoinGroup();
			}
		});
	}
	
	/**
	 * 加入群
	 */
	public void getJoinGroup(){
		new SweetAlertDialog(ctx, SweetAlertDialog.NORMAL_TYPE)
		.setTitleText("是否确定加入该群？").showCancelButton(true).setConfirmText("确定")
		.setCancelText("取消").setCancelClickListener(new OnSweetClickListener() {
			
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		}).setConfirmClickListener(new OnSweetClickListener() {
			
			@Override
			public void onClick(final SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				dialog.show();
				new SimpleNetTask(ctx,false) {
					JSONObject json;
					@Override
					protected void onSucceed() {
						// TODO Auto-generated method stub
						try {
							if(json.getString("ret").equals("success")){
								sweetAlertDialog.dismiss();
								dialog.dismiss();
								Utils.toast("加入群成功");
								GroupSearchResultActivity.this.finish();
							}else if (json.getString("ret").equals("error") && json.getString("errorMsg").equals("您已经是这个群组的成员，无法再次加入该群！")) {
								sweetAlertDialog.dismiss();
								dialog.dismiss();
								Utils.toast(json.getString("errorMsg"));
							}else if (json.getString("ret").equals("error") && json.getString("errorMsg").equals("当前群组人员已满，无法加入该群！")) {
								sweetAlertDialog.dismiss();
								dialog.dismiss();
								Utils.toast(json.getString("errorMsg"));
							}else {
								sweetAlertDialog.dismiss();
								dialog.dismiss();
								Utils.toast("net error");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					@Override
					protected void doInBack() throws Exception {
						// TODO Auto-generated method stub
						String userId = Utils.getID();
						
						String myId ="{\"chatGroupMumber\":[{\"id\":\"" + userId  + "\"}]}";
						System.out.println("myId:"+myId);
						param.clear();
						param.put("userID", myId);
						param.put("groupId", searchGroup.getGroupId());
						param.put("serverGroupId", searchGroup.getServerGroupId());
						String jsonStr = new WebService(C.JOINGROUP, param).getReturnInfo();
						json = new JSONObject(jsonStr);
					}
				}.execute();
				
			}
		}).show();
	}
}

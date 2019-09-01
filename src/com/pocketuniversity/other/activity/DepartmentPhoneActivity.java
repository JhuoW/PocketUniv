package com.pocketuniversity.other.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.example.pocketuniversity.R;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.DepartmentPhoneAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.DepartmentPhone;
import com.pocketuniversity.bean.SortDepart;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.CharacterParser;
import com.pocketuniversity.utils.PinyinComparator2;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.EnLetterView;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;

public class DepartmentPhoneActivity extends BaseActivity {

	private EnLetterView rightLetter;

	private DepartmentPhoneAdapter adapter ;
	private ListView lv_department;
	private static CharacterParser characterParser;
	private static PinyinComparator2 pinyinComparator;
	private TextView dialog;

	MyProgressDialog mdialog;
	
	public List<DepartmentPhone> dp;
	public List<SortDepart> sortDp = new ArrayList<SortDepart>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_phone);
		mdialog = new MyProgressDialog(ctx);
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator2();
		initView();
		initRightLetterView();
		mdialog.show();
		getData();
	}

	private void getData(){
		new SimpleNetTask(ctx , false) {
			
			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				
				if (mdialog != null && mdialog.isShowing()) {
					mdialog.dismiss();
				}
				List<SortDepart> sortUsers = convertAVUser(dp);
				sortDp = Collections.unmodifiableList(sortUsers);
				adapter = new DepartmentPhoneAdapter(ctx, sortDp);
				lv_department.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				String jsonStr = new WebService(C.GETDEPARTPHONELIST, param).getReturnInfo();
				//String jsonStr = "{ \"ret\" : \"success\",\"ContactInformation\":[{\"departmentname\":\"外联部\",\"departmentphone\":\"123\"},{\"departmentname\":\"宣传部\",\"departmentphone\":\"123\"},{\"departmentname\":\"物联网部\",\"departmentphone\":\"123\"},{\"departmentname\":\"阿\",\"departmentphone\":\"1234\"},{\"departmentname\":\"体育部\",\"departmentphone\":\"1234\"},{\"departmentname\":\"波\",\"departmentphone\":\"1234\"},{\"departmentname\":\"吃\",\"departmentphone\":\"1234\"},{\"departmentname\":\"得\",\"departmentphone\":\"1234\"},{\"departmentname\":\"额\",\"departmentphone\":\"1234\"},{\"departmentname\":\"和\",\"departmentphone\":\"1234\"},{\"departmentname\":\"爱\",\"departmentphone\":\"1234\"},{\"departmentname\":\"机\",\"departmentphone\":\"1234\"},{\"departmentname\":\"课\",\"departmentphone\":\"1234\"}]}";
				dp = GetObjectFromService.getDp(jsonStr);
				System.out.println(dp.get(0).getName());
			}
		}.execute();
		
	}
	
	private void initView(){
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("部门电话");
		headerLayout.showLeftBackButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DepartmentPhoneActivity.this.finish();
			}
		});
		lv_department = (ListView) findViewById(R.id.phone_listview);
		adapter = new DepartmentPhoneAdapter(ctx, sortDp);
		lv_department.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DepartmentPhoneActivity.this, DepartmentPhoneDetailActivity.class);
				intent.putExtra("ID", sortDp.get(position).getSortDp().getId());
				intent.putExtra("Name", sortDp.get(position).getSortDp().getName());
				startActivity(intent);
			}
		});
	}
	
	private void initRightLetterView() {
		rightLetter = (EnLetterView) findViewById(R.id.right_letter);
		dialog = (TextView) findViewById(R.id.dialog);
		rightLetter.setTextView(dialog);
		rightLetter
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
	}

	private class LetterListViewListener implements
			EnLetterView.OnTouchingLetterChangedListener {
		@Override
		public void onTouchingLetterChanged(String s) {
			int position = adapter.getPositionForSection(s.charAt(0));
			if (position != -1) {
				lv_department.setSelection(position);
			}
		}
	}

	private List<SortDepart> convertAVUser(List<DepartmentPhone> datas) {
		List<SortDepart> sortUsers = new ArrayList<SortDepart>();
		int total = datas.size();
		for (int i = 0; i < total; i++) {
			DepartmentPhone avUser = datas.get(i);
			SortDepart sortUser = new SortDepart();
			sortUser.setSortDp(avUser);
			String username = avUser.getName();
			if (username != null) {
				String pinyin = characterParser.getSelling(username);
				String sortString = pinyin.substring(0, 1).toUpperCase();
				if (sortString.matches("[A-Z]")) {
					sortUser.setSortLetters(sortString.toUpperCase());
				} else {
					sortUser.setSortLetters("#");
				}
			} else {
				sortUser.setSortLetters("#");
			}
			sortUsers.add(sortUser);
		}
		Collections.sort(sortUsers, pinyinComparator);
		return sortUsers;
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}

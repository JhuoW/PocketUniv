package com.pocketuniversity.function.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.CourierAdapter;
import com.pocketuniversity.bean.Courier;
import com.pocketuniversity.bean.SortCourier;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.utils.CharacterParser;
import com.pocketuniversity.utils.PinyinComparator3;
import com.pocketuniversity.view.EnLetterView;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;
import com.show.api.ShowApiRequest;

public class CourierActivity extends BaseActivity {

	private EnLetterView rightLetter;
	private CourierAdapter adapter;
	private ListView courierListView;
	private static CharacterParser characterParser;
	private static PinyinComparator3 pinyinComparator;
	private TextView dialog;
	MyProgressDialog mdialog;
	public List<Courier> courierList;
	public List<SortCourier> sortCourier = new ArrayList<SortCourier>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courier);
		mdialog = new MyProgressDialog(ctx);
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator3();
		initView();
		initRightLetterView();
		getData();
	}

	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("选择快递");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CourierActivity.this.finish();
			}
		});
		courierListView = (ListView) findViewById(R.id.phone_listview);
		adapter = new CourierAdapter(ctx, sortCourier);
		
		courierListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CourierActivity.this, CourierDetailActivity.class);
				intent.putExtra("name", sortCourier.get(position).getCourier().getName());
				intent.putExtra("url", sortCourier.get(position).getCourier().getUrl());
				intent.putExtra("imgUrl", sortCourier.get(position).getCourier().getImgUrl());
				intent.putExtra("simpleName", sortCourier.get(position).getCourier().getSimpleName());
				intent.putExtra("phone", sortCourier.get(position).getCourier().getPhone());
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}

	private void getData() {
		mdialog.show();
		new ShowApiRequest("http://route.showapi.com/64-20", "6649", "acaadc0e1924440b82a13a859b4952ef")
				.setResponseHandler(resHandler).post();
	}
	
	 final AsyncHttpResponseHandler resHandler=new AsyncHttpResponseHandler(){
	        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
	            //做一些异常处理
	        	if (mdialog != null && mdialog.isShowing()) {
					mdialog.dismiss();
				}
	        	Utils.showtoast(CourierActivity.this, R.drawable.tips_error, "加载超时，请重试");
	            e.printStackTrace();
	        }
	        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
	             
	        	try {
					String jsonStr = new String(responseBody,"utf-8");
					try {
						JSONObject	json = new JSONObject(jsonStr);
						JSONObject jsonobj = json.getJSONObject("showapi_res_body");
						JSONArray jsonarray = jsonobj.getJSONArray("expressList");
						courierList = GetObjectFromService.getCourierList(jsonarray);
						if (mdialog != null && mdialog.isShowing()) {
							mdialog.dismiss();
						}
						List<SortCourier> sortUsers = convertAVUser(courierList);
						sortCourier = Collections.unmodifiableList(sortUsers);
						adapter = new CourierAdapter(ctx, sortCourier);
						courierListView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }};

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
				courierListView.setSelection(position);
			}
		}
	}

	private List<SortCourier> convertAVUser(List<Courier> datas) {
		List<SortCourier> sortUsers = new ArrayList<SortCourier>();
		int total = datas.size();
		for (int i = 0; i < total; i++) {
			Courier avUser = datas.get(i);
			SortCourier sortUser = new SortCourier();
			sortUser.setCourier(avUser);
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

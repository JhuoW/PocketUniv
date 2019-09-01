package com.pocketuniversity.function.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.pocketuniversity.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.PEAdapter;
import com.pocketuniversity.base.C;
import com.pocketuniversity.bean.PE;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.https.WebService;
import com.pocketuniversity.utils.SimpleNetTask;
import com.pocketuniversity.view.HeaderLayout;
import com.show.api.ShowApiRequest;

public class PEActivity extends BaseActivity implements OnItemClickListener,
		OnScrollListener {
	private ListView listView;
	PEAdapter peAdapter;
	private boolean isLoading = true;
	List<PE> list = new ArrayList<PE>();
	private int page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pe);
		initView();
		getData();
		//getDataFromUserServer();

	}

	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PEActivity.this.finish();
			}
		});
		headerLayout.showTitle("体坛资讯");
		listView = (ListView) findViewById(R.id.fragment_news_listview);
		peAdapter = new PEAdapter(ctx, list, getLayoutInflater());
		listView.addFooterView(View.inflate(ctx, R.layout.foot, null));
		listView.setAdapter(peAdapter);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
	}

	private void getDataFromUserServer() {
		new SimpleNetTask(ctx) {
			String jsonStr;

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				try {
					JSONObject json = new JSONObject(jsonStr);
					JSONObject jsonobj = json.getJSONObject("showapi_res_body");
					for (int i = 0; i < 20; i++) {
						JSONObject pejson = jsonobj.getJSONObject(i + "");
						PE pe = GetObjectFromService.getPEList(pejson);
						list.add(pe);
					}
					if (list.size() > 0) {
						peAdapter.addpe(list);
						peAdapter.notifyDataSetChanged();
						page++;
					}
					isLoading = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("page", page + "");
				param.put("num", "20");
				jsonStr = new WebService(C.GETSPORTSEVENTS, param)
						.getReturnInfo();
			}
		}.execute();
	}

	private void getData() {
		new ShowApiRequest("http://route.showapi.com/196-1", "6649",
				"acaadc0e1924440b82a13a859b4952ef")
				.setResponseHandler(resHandler).addTextPara("num", "20")
				.addTextPara("page", page + "").post();
	}

	final AsyncHttpResponseHandler resHandler = new AsyncHttpResponseHandler() {
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable e) {
			// 做一些异常处理
			e.printStackTrace();
		}

		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				String jsonStr = new String(responseBody, "utf-8");
				JSONObject json = new JSONObject(jsonStr);
				JSONObject jsonobj = json.getJSONObject("showapi_res_body");
				for (int i = 0; i < 19; i++) {    //标记，可能会变
					JSONObject pejson = jsonobj.getJSONObject(i + "");
					PE pe = GetObjectFromService.getPEList(pejson);
					list.add(pe);
				}
				if (list.size() > 0) {
					peAdapter.addpe(list);
					peAdapter.notifyDataSetChanged();
					page++;
				}
				isLoading = false;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (totalItemCount <= firstVisibleItem + visibleItemCount + 1
				&& isLoading == false) {
			isLoading = true;
			getData();
			//getDataFromUserServer();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (view.getId() != R.id.foot_view) {
			Intent intent = new Intent(PEActivity.this, PEDetailActivity.class);
			intent.putExtra("post", list.get(position));
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		}
	}
}

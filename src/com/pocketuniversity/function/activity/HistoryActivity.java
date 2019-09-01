package com.pocketuniversity.function.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.TravelAdapter;
import com.pocketuniversity.aphidmobile.flip.FlipViewController;
import com.pocketuniversity.bean.History;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.view.MyProgressDialog;
import com.show.api.ShowApiRequest;

public class HistoryActivity extends BaseActivity{
	private FlipViewController flipView;
	MyProgressDialog mdialog;
	List<History> list = new ArrayList<History>();
	TravelAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
		adapter = new TravelAdapter(ctx, list);
		flipView.setAdapter(adapter);
		mdialog = new MyProgressDialog(ctx);
		setContentView(flipView);
		getData();
	}
	
	private void getData(){
		mdialog.show();
		 new ShowApiRequest( "http://route.showapi.com/119-42", "6649", "acaadc0e1924440b82a13a859b4952ef")
         .setResponseHandler(resHandler)
         .addTextPara("date", "")
         .post();
	}
	
	final AsyncHttpResponseHandler resHandler=new AsyncHttpResponseHandler(){
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
            //做一些异常处理
        	if (mdialog != null && mdialog.isShowing()) {
				mdialog.dismiss();
			}
        	Utils.showtoast(HistoryActivity.this, R.drawable.tips_error, "加载超时，请重试");
            e.printStackTrace();
        }
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
            	if (mdialog != null && mdialog.isShowing()) {
					mdialog.dismiss();
				}
            	String jsonStr = new String(responseBody,"utf-8");
            	JSONObject	json = new JSONObject(jsonStr);
				JSONObject jsonobj = json.getJSONObject("showapi_res_body");
				JSONArray jsonarray = jsonobj.getJSONArray("list");
				list = GetObjectFromService.getHistoryList(jsonarray);
				adapter = new TravelAdapter(ctx, list);
				flipView.setAdapter(adapter);
				} catch (Exception e) {
                e.printStackTrace();
            }
             
    }};
    
    
    @Override
    protected void onResume() {
      super.onResume();
      flipView.onResume();
    }

    @Override
    protected void onPause() {
      super.onPause();
      flipView.onPause();
    }
}

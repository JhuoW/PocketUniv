package com.pocketuniversity.function.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.easemob.chatuidemo.video.util.Utils;
import com.example.pocketuniversity.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pocketuniversity.activity.BaseActivity;
import com.pocketuniversity.adapter.BaseListAdapter;
import com.pocketuniversity.adapter.ViewHolder;
import com.pocketuniversity.bean.Area;
import com.pocketuniversity.bean.CarRoute;
import com.pocketuniversity.bean.CourierDetail;
import com.pocketuniversity.bean.Province;
import com.pocketuniversity.db.CityDB;
import com.pocketuniversity.https.GetObjectFromService;
import com.pocketuniversity.view.CityPopWindow;
import com.pocketuniversity.view.HeaderLayout;
import com.pocketuniversity.view.MyProgressDialog;
import com.show.api.ShowApiRequest;

public class CarActivity extends BaseActivity {

	private CityPopWindow cityPopWindow;
	private ListView province_list, city_list;
	private RelativeLayout select_city_layout;
	private List<Province> provincedatas = new ArrayList<Province>();
	private List<Area> citydatas = new ArrayList<Area>();
	private ProvinceAdapter provinceAdapter;
	private CityAdapter cityAdapter;
	private CityDB cityDB;
	private TextView tv_city;

	private EditText et_search;
	private ImageView iv_search;
	private TextView carName;
	private TextView carInfo;
	private TextView route;
	private LinearLayout ll_carName;
	private LinearLayout ll_carInfo;
	private LinearLayout ll_route;
	private MyProgressDialog dialog;
	private List<CarRoute> list = new ArrayList<CarRoute>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_activity);
		initView();
		initData();
		initAction();
	}

	private void initView() {
		dialog = new MyProgressDialog(ctx);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("路线查询");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CarActivity.this.finish();
			}
		});
		et_search = (EditText) findViewById(R.id.search_edit);
		iv_search = (ImageView) findViewById(R.id.search_btn);
		carName = (TextView) findViewById(R.id.carName);
		carInfo = (TextView) findViewById(R.id.carInfo);
		route = (TextView) findViewById(R.id.route);
		ll_carInfo = (LinearLayout) findViewById(R.id.ll_carInfo);
		ll_carName = (LinearLayout) findViewById(R.id.ll_carName);
		ll_route = (LinearLayout) findViewById(R.id.ll_route);
 		cityDB = new CityDB(this);
		tv_city = (TextView) findViewById(R.id.tv_city);
		select_city_layout = (RelativeLayout) findViewById(R.id.select_city_layout);
		cityPopWindow = new CityPopWindow(CarActivity.this);
		province_list = (ListView) cityPopWindow.conentView
				.findViewById(R.id.province_list);
		city_list = (ListView) cityPopWindow.conentView
				.findViewById(R.id.city_list);
		provinceAdapter = new ProvinceAdapter(ctx, provincedatas,
				R.layout.item_register);
		cityAdapter = new CityAdapter(ctx, citydatas, R.layout.item_register);
		province_list.setAdapter(provinceAdapter);
		city_list.setAdapter(cityAdapter);

	}

	private void initAction() {
		select_city_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cityPopWindow.showPopupWindow(select_city_layout);
			}
		});

		province_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				List<Area> temp = provincedatas.get(position).getCitys();
				citydatas.clear();
				citydatas.addAll(temp);
				cityAdapter.notifyDataSetChanged();
			}
		});
		city_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (cityPopWindow != null && cityPopWindow.isShowing()) {
					cityPopWindow.dismiss();
					tv_city.setText(citydatas.get(position).getName());
				}
			}
		});

		iv_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.show();
				String city = tv_city.getText().toString();
				String busNo = et_search.getText().toString();
				String cityName = city.substring(0,city.length()-2);
				System.out.println("city:"+cityName);
				System.out.println("busNo:"+busNo);
				new ShowApiRequest("http://route.showapi.com/844-2", "6649",
						"acaadc0e1924440b82a13a859b4952ef")
						.setResponseHandler(resHandler)
						.addTextPara("city", cityName).addTextPara("busNo", busNo)
						.post();
			}
		});

	}

	final AsyncHttpResponseHandler resHandler = new AsyncHttpResponseHandler() {
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable e) {
			// 做一些异常处理
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			Utils.showtoast(ctx, R.drawable.tips_error, "查询失败");
			e.printStackTrace();
		}

		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				String jsonStr = new String(responseBody,"utf-8");
            	JSONObject	json = new JSONObject(jsonStr);
				JSONObject jsonobj = json.getJSONObject("showapi_res_body");
				JSONArray jsonarray = jsonobj.getJSONArray("retList");
				list = GetObjectFromService.getCarRouteList(jsonarray);
				System.out.println("list长度："+list.size());
				System.out.println(jsonarray+"");
				CarRoute cr = list.get(0);
				ll_carInfo.setVisibility(View.VISIBLE);
				ll_carName.setVisibility(View.VISIBLE);
				ll_route.setVisibility(View.VISIBLE);
				carName.setText(cr.getName());
				carInfo.setText(cr.getInfo());
				route.setText(cr.getStats());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	private void initData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<Area> temp = cityDB.getProvince();
				if (temp == null || temp.size() == 0) {
					Utils.toast("获取城市数据失败");
					return;
				}
				provincedatas.clear();
				for (Area province : temp) {
					Province prov = new Province();
					prov.setCode(province.getCode());
					prov.setName(province.getName());
					List<Area> citys = cityDB.getCity(province.getCode());
					prov.setCitys(citys);
					provincedatas.add(prov);
				}
				provinceAdapter.notifyDataSetChanged();
				citydatas.clear();
				citydatas.addAll(provincedatas.get(0).getCitys());
				cityAdapter.notifyDataSetChanged();
			}
		}).start();

	}

	class ProvinceAdapter extends BaseListAdapter<Province> {

		public ProvinceAdapter(Context ctx, List<Province> datas, int layoutId) {
			super(ctx, datas, layoutId);

		}

		@Override
		public void conver(ViewHolder holder, int position, Province t) {
			holder.setText(R.id.tv_content, t.getName());
		}

	}

	class CityAdapter extends BaseListAdapter<Area> {

		public CityAdapter(Context ctx, List<Area> datas, int layoutId) {
			super(ctx, datas, layoutId);
		}

		@Override
		public void conver(ViewHolder holder, int position, Area t) {
			holder.setText(R.id.tv_content, t.getName());
		}

	}

}

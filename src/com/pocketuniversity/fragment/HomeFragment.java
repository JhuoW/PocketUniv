package com.pocketuniversity.fragment;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.pocketuniversity.R;
import com.example.pullrefersh.PullToRefreshView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pocketuniversity.activity.CampaignActivity;
import com.pocketuniversity.activity.InformationActivity;
import com.pocketuniversity.activity.MainActivity;
import com.pocketuniversity.activity.SchoolIntroActivity;
import com.pocketuniversity.friendcircle.activity.FriendsCircleActivity;
import com.pocketuniversity.lost.activity.LostActivity;
import com.pocketuniversity.more.activity.ScheduleActivity;
import com.pocketuniversity.other.activity.BanliprocessActivity;
import com.pocketuniversity.other.activity.DepartmentPhoneActivity;
import com.pocketuniversity.other.activity.NoticeActivity;
import com.pocketuniversity.tiaozao.activity.TiaoZaoActivity;
import com.pocketuniversity.utils.SimpleNetTask;
import com.show.api.ShowApiRequest;

public class HomeFragment extends Fragment {

	ImageView school_intr;
	ImageView news;
	ImageView campaign;
	ImageView tiaozao;
	ImageView lost;
	ImageView banli;
	ImageView notice;
	ImageView phone;
	ImageView nullclass;
	ImageView schedule;
	ImageView weather;
	ImageView friendCircle;
	
	TextView tv_temperature;
	TextView tv_time;
	TextView tv_day;
	private ScrollView sv;

	private PullToRefreshView mPullToRefreshView;


	/**
	 * 徐州代码：101190801
	 */
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view  = inflater.inflate(R.layout.fragment_home, container,false);
		school_intr = (ImageView) view.findViewById(R.id.school_intr);
		tv_temperature = (TextView) view.findViewById(R.id.temperature);
		tv_time = (TextView) view.findViewById(R.id.time);
		tv_day = (TextView) view.findViewById(R.id.day);
		weather = (ImageView) view.findViewById(R.id.img_weather);
		news = (ImageView) view.findViewById(R.id.news);
		campaign = (ImageView) view.findViewById(R.id.campaign);
		tiaozao = (ImageView) view.findViewById(R.id.tiaozao);
		lost = (ImageView) view.findViewById(R.id.lost);
		banli = (ImageView) view.findViewById(R.id.banli);
		notice = (ImageView) view.findViewById(R.id.notice);
		phone = (ImageView) view.findViewById(R.id.phone);
		nullclass = (ImageView) view.findViewById(R.id.nullclass);
		schedule = (ImageView) view.findViewById(R.id.schedule);
		friendCircle = (ImageView) view.findViewById(R.id.firendCircle);
		mPullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.pull_to_refresh);
		sv = (ScrollView) view.findViewById(R.id.scrollView);

		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		initAction();
		getWeather();
	}

	private void initAction() {

		mPullToRefreshView
				.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
					@Override
					public void onRefresh() {
						mPullToRefreshView.setRefreshing(false);
					}
				});
		
		tiaozao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), TiaoZaoActivity.class));
			}
		});
		
		campaign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), CampaignActivity.class));
			}
		});
		
		lost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), LostActivity.class));
			}
		});
		
		banli.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), BanliprocessActivity.class));
			}
		});
		notice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), NoticeActivity.class));
			}
		});
		
		phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), DepartmentPhoneActivity.class));
			}
		});
		news.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), InformationActivity.class));
			}
		});
		school_intr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), SchoolIntroActivity.class));

			}
		});
		
		schedule.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), ScheduleActivity.class));
			}
		});
		
		friendCircle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), FriendsCircleActivity.class));
			}
		});
		
	}
	

	private void getWeather(){
		
		 new ShowApiRequest( "http://route.showapi.com/9-2", "6649", "acaadc0e1924440b82a13a859b4952ef")
		 .setResponseHandler(resHandler)
		  .addTextPara("area", "徐州")
		  .addTextPara("needMoreDay", "0")
		  .addTextPara("needIndex", "0")
		 .post();
	}
		
	private String getTime(){
		SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("MM月dd日");       
		String    date    =    sDateFormat.format(new    java.util.Date());
		return date;
	}
	
	final AsyncHttpResponseHandler resHandler=new AsyncHttpResponseHandler(){
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
			//做一些异常处理
			e.printStackTrace();
		}
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			try {
				//long b=System.currentTimeMillis();
				//long a=(Long) txt.getTag();
				//System.out.println("used time is :"+(b-a));
				//txt.setText(new String(responseBody,"utf-8")+new Date());
				//在此对返回内容做处理
				String jsonStr = new String(responseBody,"utf-8");
				System.out.println("weather--" + jsonStr);

				try {
					JSONObject json = new JSONObject(jsonStr);
					JSONObject jsonobj = json.getJSONObject("showapi_res_body");
					JSONObject jsonobj2 = jsonobj.getJSONObject("f1");
					System.out.println("jsonobj2__"+jsonobj2 );
					
					String dayTem = jsonobj2.getString("day_air_temperature");
					String nightTem = jsonobj2.getString("night_air_temperature");
					String day = jsonobj2.getString("weekday");
					String alldayTem = dayTem + "~" + nightTem;
					tv_temperature.setText(alldayTem);
					tv_time.setText(getTime());
					
					if(day.equals("1")){
						tv_day.setText("星期一");
					}else if (day.equals("2")) {
						tv_day.setText("星期二");
					}else if (day.equals("3")) {
						tv_day.setText("星期三");
					}else if (day.equals("4")) {
						tv_day.setText("星期四");
					}else if (day.equals("5")) {
						tv_day.setText("星期五");
					}else if (day.equals("6")) {
						tv_day.setText("星期六");
					}else {
						tv_day.setText("星期日");
					}
					
					String tweather = jsonobj2.getString("day_weather");
					if(tweather.equals("晴")){
						weather.setImageResource(R.drawable.weather_36);
					}else if (tweather.equals("阴")) {
						weather.setImageResource(R.drawable.weather_30);
					}else if (tweather.equals("多云")) {
						weather.setImageResource(R.drawable.weather_28);
					}else if (tweather.equals("少云")) {
						weather.setImageResource(R.drawable.weather_26);
					}else if (tweather.equals("小雨")) {
						weather.setImageResource(R.drawable.weather_5);
					}else if (tweather.equals("中雨")) {
						weather.setImageResource(R.drawable.weather_11);
					}else if (tweather.equals("大雨")) {
						weather.setImageResource(R.drawable.weather_9);
					}else if (tweather.equals("雷阵雨")) {
						weather.setImageResource(R.drawable.weather_3);
					}else if (tweather.equals("暴雨")) {
						weather.setImageResource(R.drawable.weather_9);
					}else if (tweather.equals("雾")) {
						weather.setImageResource(R.drawable.weather_21);
					}else if (tweather.equals("雨夹雪")) {
						weather.setImageResource(R.drawable.weather_10);
					}else if (tweather.equals("小雪")) {
						weather.setImageResource(R.drawable.weather_13);
					}else if (tweather.equals("中雪")) {
						weather.setImageResource(R.drawable.weather_14);
					}else if (tweather.equals("大雪")) {
						weather.setImageResource(R.drawable.weather_16);
					}else if (tweather.equals("暴雪")) {
						weather.setImageResource(R.drawable.weather_15);
					}else if (tweather.equals("阵雨")) {
						weather.setImageResource(R.drawable.weather_11);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
	}};
	
}

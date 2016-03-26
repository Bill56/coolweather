package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity {

	private LinearLayout llWeatherInfo;

	private TextView txtvCityName;
	private TextView txtvPublishTime;
	private TextView txtvWeatherDesp;
	private TextView txtvTemp1;
	private TextView txtvTemp2;
	private TextView txtvCurrentDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weather);
		// 绑定控件对象
		llWeatherInfo = (LinearLayout) findViewById(R.id.ll_weather_info);
		txtvCityName = (TextView) findViewById(R.id.txtv_city_name);
		txtvPublishTime = (TextView) findViewById(R.id.txtv_publish_time);
		txtvWeatherDesp = (TextView) findViewById(R.id.txtv_weather_desp);
		txtvTemp1 = (TextView) findViewById(R.id.txtv_temp1);
		txtvTemp2 = (TextView) findViewById(R.id.txtv_temp2);
		txtvCurrentDate = (TextView) findViewById(R.id.txtv_current_data);
		String countyCode = getIntent().getStringExtra("country_code");
		if (!TextUtils.isEmpty(countyCode)) {
			// 有县级代号就去查询天气
			txtvPublishTime.setText("同步中...");
			llWeatherInfo.setVisibility(View.INVISIBLE);
			txtvCityName.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else {
			// 没有县级代号就直接显示本地天气
			showWeather();
		}
	}

	/**
	 * 查询县级代号所对应的天气代号
	 */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city"
				+ countyCode + ".xml";
		queryFromServer(address, "countryCode");
	}

	/**
	 * 查询天气代号所对应的天气
	 */
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/"
				+ weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}

	/**
	 * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
	 */
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				if ("countryCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						// 从服务器返回的数据中解析出天气代号
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) {
					// 处理服务器返回的天气信息
					Utility.handleWeatherResponse(WeatherActivity.this,
							response);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showWeather();
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						txtvPublishTime.setText("同步失败");
					}
				});
			}
		});
	}

	/**
	 * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上
	 */
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		txtvCityName.setText(prefs.getString("city_name", ""));
		txtvTemp1.setText(prefs.getString("temp1", ""));
		txtvTemp2.setText(prefs.getString("temp2", ""));
		txtvWeatherDesp.setText(prefs.getString("weather_desp", ""));
		txtvPublishTime.setText("今天" + prefs.getString("publish_time", "")
				+ "发布");
		txtvCurrentDate.setText(prefs.getString("current_date", ""));
		llWeatherInfo.setVisibility(View.VISIBLE);
		txtvCityName.setVisibility(View.VISIBLE);
	}

}

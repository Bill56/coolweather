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
		// �󶨿ؼ�����
		llWeatherInfo = (LinearLayout) findViewById(R.id.ll_weather_info);
		txtvCityName = (TextView) findViewById(R.id.txtv_city_name);
		txtvPublishTime = (TextView) findViewById(R.id.txtv_publish_time);
		txtvWeatherDesp = (TextView) findViewById(R.id.txtv_weather_desp);
		txtvTemp1 = (TextView) findViewById(R.id.txtv_temp1);
		txtvTemp2 = (TextView) findViewById(R.id.txtv_temp2);
		txtvCurrentDate = (TextView) findViewById(R.id.txtv_current_data);
		String countyCode = getIntent().getStringExtra("country_code");
		if (!TextUtils.isEmpty(countyCode)) {
			// ���ؼ����ž�ȥ��ѯ����
			txtvPublishTime.setText("ͬ����...");
			llWeatherInfo.setVisibility(View.INVISIBLE);
			txtvCityName.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else {
			// û���ؼ����ž�ֱ����ʾ��������
			showWeather();
		}
	}

	/**
	 * ��ѯ�ؼ���������Ӧ����������
	 */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city"
				+ countyCode + ".xml";
		queryFromServer(address, "countryCode");
	}

	/**
	 * ��ѯ������������Ӧ������
	 */
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/"
				+ weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}

	/**
	 * ���ݴ���ĵ�ַ������ȥ���������ѯ�������Ż���������Ϣ
	 */
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				if ("countryCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						// �ӷ��������ص������н�������������
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) {
					// ������������ص�������Ϣ
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
						txtvPublishTime.setText("ͬ��ʧ��");
					}
				});
			}
		});
	}

	/**
	 * ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ��������
	 */
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		txtvCityName.setText(prefs.getString("city_name", ""));
		txtvTemp1.setText(prefs.getString("temp1", ""));
		txtvTemp2.setText(prefs.getString("temp2", ""));
		txtvWeatherDesp.setText(prefs.getString("weather_desp", ""));
		txtvPublishTime.setText("����" + prefs.getString("publish_time", "")
				+ "����");
		txtvCurrentDate.setText(prefs.getString("current_date", ""));
		llWeatherInfo.setVisibility(View.VISIBLE);
		txtvCityName.setVisibility(View.VISIBLE);
	}

}

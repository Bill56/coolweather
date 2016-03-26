package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.service.AutoUpdateService;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener {

	private LinearLayout llWeatherInfo;

	private TextView txtvCityName;
	private TextView txtvPublishTime;
	private TextView txtvWeatherDesp;
	private TextView txtvTemp1;
	private TextView txtvTemp2;
	private TextView txtvCurrentDate;

	// �л����а�ť
	private Button btnSwitchCity;
	// ����������ť
	private Button btnRefreshWeather;

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
		btnSwitchCity = (Button) findViewById(R.id.btn_switch_city);
		btnRefreshWeather = (Button) findViewById(R.id.btn_refresh_weather);
		// Ϊ��ťע�����ļ����¼�
		btnSwitchCity.setOnClickListener(this);
		btnRefreshWeather.setOnClickListener(this);
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
		// �����Զ�����
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_switch_city:
			Intent intent = new Intent(this, ChooseAreaActiviity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_refresh_weather:
			txtvPublishTime.setText("ͬ����...");
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
	}

}

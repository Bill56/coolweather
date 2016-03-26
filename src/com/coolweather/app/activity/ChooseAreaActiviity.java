package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import net.youmi.android.AdManager;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 选择城市的活动
 * 
 * @author Bill56
 * 
 */
public class ChooseAreaActiviity extends Activity {

	public static final int LEVEL_PROVINCE = 0;

	public static final int LEVEL_CITY = 1;

	public static final int LEVEL_COUNTRY = 2;

	private ProgressDialog progressDialog;
	private TextView txtvTitle;
	private ListView listvShowArea;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();

	// 省列表
	private List<Province> provinceList;
	// 市列表
	private List<City> cityList;
	// 县列表
	private List<Country> countryList;

	// 选中的省份
	private Province selectedProvince;
	// 选中的城市
	private City selectedCity;
	// 当前选中的级别
	private int currentLevel;

	// 是否从weatherActivity中跳过来
	private boolean isFromWeatherActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AdManager.getInstance(this).init("c7f067818fb1d43f", "674408388b294438", false);
		isFromWeatherActivity = getIntent().getBooleanExtra(
				"from_weather_activity", false);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		// 已经选择了城市并且不是从weatherActivity中调过来的
		if (prefs.getBoolean("city_selected", false) && !isFromWeatherActivity) {
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choose_area);
		// 绑定控件变量
		listvShowArea = (ListView) findViewById(R.id.listv_show_area);
		txtvTitle = (TextView) findViewById(R.id.txtv_title);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listvShowArea.setAdapter(adapter);
		// 创建本地数据库操作对象
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listvShowArea.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(arg2);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(arg2);
					queryCountries();
				} else if (currentLevel == LEVEL_COUNTRY) {
					String countryCode = countryList.get(arg2).getCountryCode();
					Intent intent = new Intent(ChooseAreaActiviity.this,
							WeatherActivity.class);
					intent.putExtra("country_code", countryCode);
					startActivity(intent);
					finish();
				}
			}
		});
		// 一开始加载省级列表
		queryProvinces();
	}

	/**
	 * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces();
		/**
		 * 从数据库中查到
		 */
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listvShowArea.setSelection(0);
			txtvTitle.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			// 没有查到数据
			queryFromServer(null, "province");
		}
	}

	/**
	 * 查询全国所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		if (cityList.size() > 0) {
			// 从数据库中查找
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listvShowArea.setSelection(0);
			txtvTitle.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			// 从服务器上查询
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}

	/**
	 * 查询全国所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryCountries() {
		countryList = coolWeatherDB.loadCountries(selectedCity.getId());
		if (countryList.size() > 0) {
			// 从数据库中查找
			dataList.clear();
			for (Country country : countryList) {
				dataList.add(country.getCountryName());
			}
			adapter.notifyDataSetChanged();
			listvShowArea.setSelection(0);
			txtvTitle.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTRY;
		} else {
			// 从服务器上查询
			queryFromServer(selectedCity.getCityCode(), "country");
		}
	}

	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据
	 * 
	 * @param code
	 *            上级代号
	 * @param type
	 *            查询级别
	 */
	private void queryFromServer(final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				boolean result = false;
				// 根据类型调用不用的方法将读到的数据存储到数据库
				if ("province".equals(type)) {
					result = Utility.handleProvincesResponse(coolWeatherDB,
							response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(coolWeatherDB,
							response, selectedProvince.getId());
				} else if ("country".equals(type)) {
					result = Utility.handleCountriesResponse(coolWeatherDB,
							response, selectedCity.getId());
				}
				if (result) {
					// 通过runOnUiThread方法回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type))
								queryProvinces();
							else if ("city".equals(type))
								queryCities();
							else if ("country".equals(type))
								queryCountries();
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// 通过runOnUiThread方法回到主线程处理逻辑
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActiviity.this, "加载失败",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 捕获back按键，根据当前的级别来判断应该返回市列表、省列表还是直接退出
	 */
	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTRY)
			queryCities();
		else if (currentLevel == LEVEL_CITY)
			queryProvinces();
		else {
			if (isFromWeatherActivity) {
				Intent intent = new Intent(this, WeatherActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}

}

package com.coolweather.app.model;

/**
 * 数据库表City所对应的实体模型类
 * 
 * @author Bill56
 * 
 */
public class City {

	// 城市id
	private int id;
	// 城市名称
	private String cityName;
	// 城市代码
	private String cityCode;
	// 城市所在的省份id
	private int provinceId;
	
	/**
	 * 无参构造方法
	 */
	public City() {

	}

	// 带参数构造方法
	public City(int id, String cityName, String cityCode, int provinceId) {
		this.id = id;
		this.cityName = cityName;
		this.cityCode = cityCode;
		this.provinceId = provinceId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	
}

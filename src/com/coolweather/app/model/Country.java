package com.coolweather.app.model;

/**
 * 数据库表Country所对应的实体模型类
 * 
 * @author Bill56
 * 
 */
public class Country {

	// 乡镇县id
	private int id;
	// 乡镇县名称
	private String countryName;
	// 乡镇县代码
	private String countryCode;
	// 乡镇县所在的城市id
	private int cityId;

	/**
	 * 无参构造方法
	 */
	public Country() {
		// TODO Auto-generated constructor stub
	}
	
	// 带参数的构造方法
	public Country(int id, String countryName, String countryCode, int cityId) {
		this.id = id;
		this.countryName = countryName;
		this.countryCode = countryCode;
		this.cityId = cityId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
}

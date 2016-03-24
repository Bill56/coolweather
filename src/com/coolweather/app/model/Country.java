package com.coolweather.app.model;

/**
 * ���ݿ��Country����Ӧ��ʵ��ģ����
 * 
 * @author Bill56
 * 
 */
public class Country {

	// ������id
	private int id;
	// ����������
	private String countryName;
	// �����ش���
	private String countryCode;
	// ���������ڵĳ���id
	private int cityId;

	/**
	 * �޲ι��췽��
	 */
	public Country() {
		// TODO Auto-generated constructor stub
	}
	
	// �������Ĺ��췽��
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

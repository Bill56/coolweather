package com.coolweather.app.model;

/**
 * ���ݿ��City����Ӧ��ʵ��ģ����
 * 
 * @author Bill56
 * 
 */
public class City {

	// ����id
	private int id;
	// ��������
	private String cityName;
	// ���д���
	private String cityCode;
	// �������ڵ�ʡ��id
	private int provinceId;
	
	/**
	 * �޲ι��췽��
	 */
	public City() {

	}

	// ���������췽��
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

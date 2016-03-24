package com.coolweather.app.model;

/**
 * 与数据库表Province所对应的实体模型类
 * 
 * @author Bill56
 * 
 */
public class Province {

	// 省份id
	private int id;
	// 省份名称
	private String provinceName;
	// 省份代号
	private String provinceCode;
	
	/**
	 * 无参构造方法
	 */
	public Province() {
		// TODO Auto-generated constructor stub
	}

	// 带参数的构造方法
	public Province(int id, String provinceName, String provinceCode) {
		this.id = id;
		this.provinceName = provinceName;
		this.provinceCode = provinceCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	
}

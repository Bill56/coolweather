package com.coolweather.app.model;

/**
 * �����ݿ��Province����Ӧ��ʵ��ģ����
 * 
 * @author Bill56
 * 
 */
public class Province {

	// ʡ��id
	private int id;
	// ʡ������
	private String provinceName;
	// ʡ�ݴ���
	private String provinceCode;
	
	/**
	 * �޲ι��췽��
	 */
	public Province() {
		// TODO Auto-generated constructor stub
	}

	// �������Ĺ��췽��
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

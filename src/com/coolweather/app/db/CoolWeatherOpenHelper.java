package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * �����������ݿ��Helper����
 * 
 * @author Bill56
 * 
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	// ����ʡ�����ݱ��sql���
	private static final String CREATE_PROVINCE = "create table Province ("
			+ "id integer primary key autoincrement," 
			+ "province_name text,"
			+ "province_code text)";
	// �����������ݱ��sql���
	private static final String CREATE_CITY = "create table City ("
			+ "id integer primary key autoincrement," 
			+ "city_name text,"
			+ "city_code text," 
			+ "province_id integer)";
	// �������������ݱ��sql���
		private static final String CREATE_COUNTRY = "create table Country ("
				+ "id integer primary key autoincrement," 
				+ "country_name text,"
				+ "country_code text," 
				+ "city_id integer)";
	
	/**
	 * ���췽��
	 * 
	 * @param context
	 *            �����Ļ���
	 * @param name
	 *            ���ݿ�����
	 * @param factory
	 *            �α깤������
	 * @param version
	 *            �汾��
	 */
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// ִ�д������ݱ�����
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTRY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

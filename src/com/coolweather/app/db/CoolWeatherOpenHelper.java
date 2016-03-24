package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 操作本地数据库的Helper子类
 * 
 * @author Bill56
 * 
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	// 创建省份数据表的sql语句
	private static final String CREATE_PROVINCE = "create table Province ("
			+ "id integer primary key autoincrement," 
			+ "province_name text,"
			+ "province_code text)";
	// 创建城市数据表的sql语句
	private static final String CREATE_CITY = "create table City ("
			+ "id integer primary key autoincrement," 
			+ "city_name text,"
			+ "city_code text," 
			+ "province_id integer)";
	// 创建乡镇县数据表的sql语句
		private static final String CREATE_COUNTRY = "create table Country ("
				+ "id integer primary key autoincrement," 
				+ "country_name text,"
				+ "country_code text," 
				+ "city_id integer)";
	
	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文环境
	 * @param name
	 *            数据库名字
	 * @param factory
	 *            游标工厂对象
	 * @param version
	 *            版本号
	 */
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 执行创建数据表的语句
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTRY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

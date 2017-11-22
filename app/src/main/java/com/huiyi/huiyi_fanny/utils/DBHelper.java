package com.huiyi.huiyi_fanny.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String BASE_NAME = "huiyiny.db";
	private static final int VERSION = 2;//数据库版本
	private static final String TABLE_NAME = "test";
	private static  DBHelper instance = null;
	private static final Object mMutex = new Object();
	/**
	 * 在SQLiteOpenHelper的子类中,必须有的构造函数
	 *  context 上下文
	 * BASE_NAME 数据库名称
	 * 游标工厂
	 * VERSION 当前数据库的版本
	 */
	public DBHelper(Context context){
		super(context,BASE_NAME,null,VERSION);
	}

	public static DBHelper getInstance(Context context){
		if (instance == null){
			synchronized (mMutex){
				if (instance == null){
					instance = new DBHelper(context);
				}
			}
			}
		return instance;
		}

	/**
	 * 删除数据库
	 * @param context
	 * @return
	 */
	public boolean deleteDatabase(Context context) {
		return context.deleteDatabase(BASE_NAME);
	}

	//数据库被创建的时候调用该方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		//设置表
		String t_set = ("create table if not exists t_set(" +
				"ID integer primary key autoincrement," +
				"url varchar(500))");
		db.execSQL(t_set);


		//账号信息表
		String t_manager = ("create table if not exists t_manager(" +
				"m_id integer primary key autoincrement," +
				"m_name varchar(500)," +
				"m_password varchar(500)," +
				"stadus integer)");
		db.execSQL(t_manager);

		//产品信息表
		String t_product = ("create table if not exists t_product(" +
				"p_id integer," +
				"p_number varchar(500)," +
				"p_name varchar(500)," +
				"p_standard varchar(500))");
		db.execSQL(t_product);

		//客户信息表
		String t_customer = ("create table if not exists t_customer(" +
				"c_id integer," +
				"c_number varchar(500)," +
				"c_name varchar(500)," +
				"c_address varchar(500))");
		db.execSQL(t_customer);

		//库别信息表
		String t_kube = ("create table if not exists t_kube(" +
				"kb_id integer," +
				"kb_name varchar(500))");
		db.execSQL(t_kube);

		//销售订单信息表
		String t_xorder = ("create table if not exists t_xorder(" +
				"x_id integer," +
				"x_number varchar(500)," +
				"x_productID integer," +
				"x_customerID integer," +
				"x_xquantity integer," +
				"x_yquantity integer," +
				"x_datetime varchar(500))");
		db.execSQL(t_xorder);

		//出货信息表
		String t_shipment = ("create table if not exists t_shipment(" +
				"sh_id integer primary key autoincrement," +
				"sh_xscode varchar(500)," +
				"sh_dcode varchar(500)," +
				"sh_xorderID integer," +
				"sh_kubeID integer," +
				"sh_datetime varchar(500)," +
				"sh_cquantity integer)");
		db.execSQL(t_shipment);

		//退货信息表
		String t_tuigoods = ("create table if not exists t_tuigoods(" +
				"t_id integer primary key autoincrement," +
				"t_xscode varchar(500)," +
				"t_datetime varchar(500)," +
				"t_remarks varchar(500))");
		db.execSQL(t_tuigoods);

		//关联信息表
		String t_packing = ("create table if not exists t_packing(" +
				"pk_id integer primary key autoincrement," +
				"pk_dcode varchar(500)," +
				"pk_xcode varchar(500)," +
				"pk_time varchar(500)," +
				"pk_shezhiID,integer)");
		db.execSQL(t_packing);

		//扫码
		String t_code = ("create table if not exists t_code(" +
				"id integer primary key autoincrement," +
				"code varchar(500)," +
				"productcode varchar(500)," +
				"chilerenamount integer," +
				"message varchar(500))");
		db.execSQL(t_code);
	}




	//数据库版本号发生变更的时候被调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXIST" + TABLE_NAME;
		db.execSQL(sql);

		this.onCreate(db);
		System.out.println("更新已完成");
	}

}

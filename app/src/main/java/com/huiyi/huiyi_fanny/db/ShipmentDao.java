package com.huiyi.huiyi_fanny.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.huiyi.huiyi_fanny.model.GoodsListEntry;
import com.huiyi.huiyi_fanny.utils.DBHelper;


public class ShipmentDao {

	private DBHelper dbhelper;
	private SQLiteDatabase db;

	//获取数据库对象
	private void init(){
		db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
	}


	public ShipmentDao(Context paramContext) {
		dbhelper = new DBHelper(paramContext);
	}

	public void AddShipment(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) {
		init();
		try {
			db.execSQL(
					"insert into t_shipment(" +
					"sh_xscode," +
					"sh_dcode," +
					"sh_xorderID," +
					"sh_kubeID," +
					"sh_datetime," +
					"sh_cquantity) " +
					"values('" + paramString1 + "','" + paramString2 + "','" + paramString3 + "','" + paramString4 + "','" + paramString5 + "','" + paramString6 + "')");

		} catch (SQLException e ){
			e.printStackTrace();
		}

	}

	public void DeleteShipment() {
		init();
		db.execSQL(" delete from t_shipment ");

	}

	public void DeleteShipment2(String paramString) {
		init();
		db.execSQL("" +
				" delete from t_shipment where sh_xorderID in (" +
				"select x_id from t_xorder where x_number='" + paramString + "'); ");
		db.close();
	}

	public void DeleteShipment3(String paramString) {
		init();
		db.execSQL(" delete from t_shipment where number = '" + paramString + "' ");
		db.close();
	}

	public void DeleteShipment_ma(String paramString) {
		init();
		db.execSQL(" delete from t_shipment where sh_xscode = '" + paramString + "' ");

	}

	public void UpdateShipment(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8) {
		init();
		try {
			db.execSQL(" update t_storage set xorderID='" + paramString2 + "',sorderID='" + paramString3 + "',customerID='" + paramString4 + "',quantity='" + paramString5 + "',datetime='" + paramString6 + "',status='" + paramString7 + "',producerID='" + paramString8 + "' where code='" + paramString1 + "'  ");
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.close();
	}
}

package com.huiyi.huiyi_fanny.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.huiyi.huiyi_fanny.utils.DBHelper;


/**
 * Created by lw on 2017/9/26.
 */

public class TuigoodsDao {

    private DBHelper dbhelper;
    private SQLiteDatabase db;

    //获取数据库对象
    private void init(){
        db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
    }

    public TuigoodsDao(Context paramContext) {
        dbhelper = new DBHelper(paramContext);
    }

    public void AddTuigoods(String paramString1, String paramString2, String paramString3){
        init();
        try {

            db.execSQL("insert into t_tuigoods(t_xscode,t_datetime,t_remarks) values('" + paramString1 + "','" + paramString2 + "','" + paramString3 + "')");

        } catch (SQLException localSQLException){

            localSQLException.printStackTrace();
        }
        db.close();
    }

    public void DeleteShipment3(String paramString){
        init();
        db.execSQL(" delete from t_shipment where number = '" + paramString + "' ");
        db.close();
    }

    public void DeleteTuigoods(){
        init();
        db.execSQL(" delete from t_tuigoods ");
        db.close();
    }

    public void DeleteTuigoods_ma(String paramString){
        init();
        db.execSQL(" delete from t_tuigoods where t_xscode = '" + paramString + "' ");
        db.close();
    }

    public void UpdateShipment(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8) {
        init();
        try{
            db.execSQL(" update t_storage set xorderID='" + paramString2 + "',sorderID='" + paramString3 + "',customerID='" + paramString4 + "',quantity='" + paramString5 + "',datetime='" + paramString6 + "',status='" + paramString7 + "',producerID='" + paramString8 + "' where code='" + paramString1 + "'  ");

        } catch (SQLException localSQLException) {
            localSQLException.printStackTrace();
        }
        db.close();
    }
}


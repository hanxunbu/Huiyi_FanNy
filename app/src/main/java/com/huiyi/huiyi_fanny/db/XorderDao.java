package com.huiyi.huiyi_fanny.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.huiyi.huiyi_fanny.utils.DBHelper;


/**
 * Created by lw on 2017/9/26.
 */

public class XorderDao {

    private DBHelper dbhelper;

    private SQLiteDatabase db;

    //获取数据库对象
    private void init(){
        db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
    }


    public XorderDao(Context context) {
       dbhelper = new DBHelper(context);
    }


    public void DeleteXorder(){
        init();
        db.execSQL(" delete from t_xorder ");
        db.close();
    }
}

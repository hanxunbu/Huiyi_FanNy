package com.huiyi.huiyi_fanny.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.huiyi.huiyi_fanny.utils.DBHelper;


/**
 * Created by lw on 2017/9/26.
 */

public class CodeDao {

    private DBHelper dbhelper;
    private SQLiteDatabase db;

    //获取数据库对象
    private void init(){
        db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
    }


    public CodeDao(Context paramContext) {
        dbhelper = new DBHelper(paramContext);
    }

    public void DeleteProduct() {
        init();
        db.execSQL(" delete from t_code ");
        db.close();
    }
}

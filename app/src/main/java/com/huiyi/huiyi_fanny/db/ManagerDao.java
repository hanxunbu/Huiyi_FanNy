package com.huiyi.huiyi_fanny.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.huiyi.huiyi_fanny.utils.DBHelper;


/**
 * Created by lw on 2017/9/26.
 */

public class ManagerDao {

    private DBHelper dbhelper;
    private SQLiteDatabase db;

    //获取数据库对象
    private void init(){
        db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
    }

    public ManagerDao(Context context){
       dbhelper = new DBHelper(context);
    }

    /**
     * 添加
     * @param
     */
    public void AddManage(String paramString) {
        init();
        db.execSQL(paramString);
        db.close();
    }

    /**
     *删除
     * @param
     */
    public void DeleteProducer() {
        init();
        db.execSQL(" delete from t_manager ");
        db.close();
    }
}

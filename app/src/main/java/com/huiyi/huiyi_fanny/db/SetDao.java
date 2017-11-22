package com.huiyi.huiyi_fanny.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.huiyi.huiyi_fanny.utils.DBHelper;


public class SetDao {
	
	private DBHelper dbhelper;
	public String url;
	private SQLiteDatabase db;

	public SetDao(Context context)
	{
		this.dbhelper = new DBHelper(context);
	}

	//获取数据库对象
	private void init(){
		db = dbhelper.getWritableDatabase();//获取一个可读可写的数据库
	}

	/**
	 * 添加
	 * @param a
	 */
	public void AddSet(String a) {
		//取得数据库操作实例
		init();
    	String sql = "insert into t_set(url) values('"+a+"')";
        db.execSQL(sql);
		db.close();

    }  
	
	/**
	 *修改
	 * @param a
	 */
	public void ModifyAbout(String a) {
		//取得数据库操作实例
		init();
    	String sql = "UPDATE t_set SET url='"+a+"'";
        db.execSQL(sql);
		db.close();
    } 

	/**
	 * 查询
	 * @return
	 */
	public String SetCha() {
		init();
		Cursor cz_About = db.rawQuery("select url from t_set",null);
		if (cz_About != null) {
			while (cz_About.moveToNext()) {
				url = cz_About.getString(cz_About.getColumnIndex("url"));
				}
			}
		cz_About.close();
		db.close();
		return url;
	}

}

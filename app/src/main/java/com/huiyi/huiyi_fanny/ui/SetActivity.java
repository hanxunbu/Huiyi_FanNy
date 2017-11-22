package com.huiyi.huiyi_fanny.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huiyi.huiyi_fanny.R;
import com.huiyi.huiyi_fanny.base.TopBarBaseActivity;
import com.huiyi.huiyi_fanny.db.SetDao;
import com.huiyi.huiyi_fanny.utils.AppManager;
import com.huiyi.huiyi_fanny.utils.DBHelper;


/**
 * Created by LW on 2017/9/25.
 * 关于页面
 */
public class SetActivity extends TopBarBaseActivity implements View.OnClickListener{

	private EditText address_et;//主页
	private Button save_bt;
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	private int record;
	private String url;


	//UI布局
	@Override
	protected int getContentView() {
		AppManager.getAppManager().addActivity(this);
		return R.layout.activity_about;

	}
	//自定义Toolbar
	@Override
	protected void init(Bundle savedInstanceState) {
		initView();
		setTitle("设置");
		setLeftButton(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick() {
				Intent intent = new Intent(SetActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initView() {
		address_et = (EditText) findViewById(R.id.about_address_et);
		save_bt = (Button) findViewById(R.id.about_save_bt);
		save_bt.setOnClickListener(this);

		//获取数据库
		db = DBHelper.getInstance(getBaseContext()).getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) as jilu  from t_set", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {//移动光标
				record = cursor.getInt(cursor.getColumnIndex("jilu"));
			}
		}
		cursor.close();



		if (record > 0) {
			Cursor cursor_data = db.rawQuery("select url from t_set", null);
			if (cursor_data != null) {
				while (cursor_data.moveToNext()) {
					String url = cursor_data.getString(cursor_data.getColumnIndex("url"));
					address_et.setText(url);
				}
			}
			cursor_data.close();


		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.about_save_bt:
				//获取数据库
				db = DBHelper.getInstance(getBaseContext()).getReadableDatabase();
				url = address_et.getText().toString();
				if (address_et.getText().toString().trim().equals("") || address_et.getText().toString().trim() == null) {
					Toast.makeText(this, "参数不能为空！", Toast.LENGTH_SHORT).show();
				}else{
					int aa = record;
					if(aa > 0) {
						SetDao setDao = new SetDao(getBaseContext());
						setDao.ModifyAbout(url);
						Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
					}else {
						SetDao setDao = new SetDao(getBaseContext());
						setDao.AddSet(url);
						Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
					}
				}

				break;
		}
	}

	//返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			Intent myIntent = new Intent(SetActivity.this,LoginActivity.class);
			startActivity(myIntent);
			AppManager.getAppManager().finishActivity();
		}
		return super.onKeyDown(keyCode, event);
	}
}

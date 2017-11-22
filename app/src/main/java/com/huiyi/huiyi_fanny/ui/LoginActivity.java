package com.huiyi.huiyi_fanny.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.huiyi.huiyi_fanny.R;
import com.huiyi.huiyi_fanny.base.TopBarBaseActivity;
import com.huiyi.huiyi_fanny.db.SetDao;
import com.huiyi.huiyi_fanny.utils.AppManager;
import com.huiyi.huiyi_fanny.utils.DBHelper;
import com.huiyi.huiyi_fanny.utils.HttpConnSoap;
import com.huiyi.huiyi_fanny.utils.ToastUtil;
import com.huiyi.huiyi_fanny.utils.UpdateManager;

import java.util.ArrayList;

/**
 * Created by LW on 2017/9/25.
 * 登录界面
 */

public class LoginActivity extends TopBarBaseActivity implements OnClickListener {

	private EditText name_et;//账号
	private EditText password_et;//密码
	private CheckBox remember_cb;//记住密码
	private Button login_bt;//登录
	private TextView about_t;//关于
	private SharedPreferences sp ;//储存
	private DBHelper dbhelper;
	private SQLiteDatabase db;
	private String name;//用户名
	private String pwd;//密码
	private ArrayList<String> arrayList = new ArrayList<String>();
	private ArrayList<String> brrayList = new ArrayList<String>();
	private ArrayList<String> crrayList = new ArrayList<String>();
	private HttpConnSoap Soap = new HttpConnSoap();
	private String ServerUrl;
	private int record;

	public void getLogin() {
		final ProgressDialog progressDialog=new ProgressDialog(this);
		progressDialog.setTitle("登录");
		progressDialog.setMessage("正在登录中...");
		progressDialog.onStart();
		progressDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Login(name,pwd);


				} catch (Exception e) {

					e.printStackTrace();
				}finally{
					progressDialog.dismiss();
				}

			}
		}).start();

	}


	@Override
	protected int getContentView() {
		sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		AppManager.getAppManager().addActivity(this);

		return R.layout.activity_login;
	}

	@Override
	protected void init(Bundle savedInstanceState) {
		initView();
		setTitle("慧翼科技溯源");

	}

	private void initView() {

		name_et = (EditText) findViewById(R.id.login_name_et);
		password_et = (EditText) findViewById(R.id.login_password_et);
		remember_cb = (CheckBox) findViewById(R.id.login_remember_cb);

		login_bt = (Button) findViewById(R.id.login_login_bt);
		about_t = (TextView) findViewById(R.id.login_about_tv);

		about_t.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		about_t.setOnClickListener(this);
		login_bt.setOnClickListener(this);

		dbhelper = new DBHelper(getBaseContext());
		db = dbhelper.getReadableDatabase();

		//默认服务器地址t
		try {

			Cursor cza = db.rawQuery("select count(*) as jilu  from t_set",null);
			if (cza != null) {
				while (cza.moveToNext()) {
					record = cza.getInt(cza.getColumnIndex("jilu"));//是否已有记录
				}
			}
			cza.close();
			if(record > 0){

			}else{
				SetDao setDao = new SetDao(getBaseContext());

				setDao.AddSet("http://192.168.1.146/nongyao/web.asmx");
			}
		}catch (Exception e1) {

			e1.printStackTrace();
		}


		// 获取接口地址
		SetDao setDao = new SetDao(getBaseContext());
		ServerUrl = setDao.SetCha();

//		获取Activity 传过来的参数
//		取值：
		Intent intent=getIntent();
		String zhi=intent.getStringExtra("extra");
		if (zhi != null) {
			int a = Integer.valueOf(zhi).intValue();
			if (a == 1) {

				// 检查软件更新
				UpdateManager manager = new UpdateManager(LoginActivity.this);
				manager.checkUpdate("http://hyny.zhuisuma315.com");
			}
		}

		//记住密码
		if (sp.getBoolean("checkboxBoolean",false)){
			name_et.setText(sp.getString("m_name",null));
			password_et.setText(sp.getString("m_password",null));
			remember_cb.setChecked(true);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.login_about_tv://跳转关于页面
				Intent intent_about = new Intent(this,SetActivity.class);
				startActivity(intent_about);
				AppManager.getAppManager().finishActivity();
				break;
			case R.id.login_login_bt://跳转主界面
				//记住密码
				name = name_et.getText().toString().trim();
				pwd = password_et.getText().toString().trim();
				if(name.trim().equals("")){
					ToastUtil.showToast(this,"请输入账号!");

				}else if (pwd.trim().equals("")){
					ToastUtil.showToast(this,"请输入密码!");

				}else{
						getLogin();
					}

				boolean CheckBoxLogin = remember_cb.isChecked();
				if (CheckBoxLogin){
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("m_name",name);
					editor.putString("m_password",pwd);
					editor.putBoolean("checkboxBoolean",true);
					editor.commit();
				}else {
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("m_name",null);
					editor.putString("m_password",null);
					editor.putBoolean("checkboxBoolean",false);
					editor.commit();
				}
				break;
		}
	}



	public void Login(String name, String password) {
		ArrayList<String> List=new ArrayList<String>();
		arrayList.clear();
		brrayList.clear();

		arrayList.add("m_name");
		arrayList.add("m_password");
		brrayList.add(name);
		brrayList.add(password);

		crrayList = Soap.GetWebServre("xml_manager_login", arrayList,brrayList, ServerUrl);
		String str = String.valueOf(crrayList);
		String sj = (str.substring(str.indexOf("[") + 1, str.indexOf("]"))).trim();
		Message message = handler.obtainMessage();
		message.obj = sj;
		handler.sendMessage(message);
	}


	private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 将WebService得到的结果返回给TextView
            String jsonshuju = msg.obj.toString();

            if (jsonshuju.trim().equals("") || jsonshuju.trim() == null) {
				ToastUtil.showToast(getBaseContext(), "登录失败，请检测网络是否正常！");
            }
                try {
                    String s = new String(jsonshuju);
                    String a[] = s.split(",");
                    String jilu = a[0].toString();
					if (Integer.parseInt(jilu) == 0){
						ToastUtil.showToast(getBaseContext(),"账号不存在！");

					}else if (Integer.parseInt(jilu) == 10){
						ToastUtil.showToast(getBaseContext(),"密码不正确！");

					} else {
						ToastUtil.showToast(getBaseContext(), "登录成功！");
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }


    };
}

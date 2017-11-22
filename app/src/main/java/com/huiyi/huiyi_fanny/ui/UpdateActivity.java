package com.huiyi.huiyi_fanny.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.huiyi.huiyi_fanny.R;
import com.huiyi.huiyi_fanny.base.TopBarBaseActivity;
import com.huiyi.huiyi_fanny.db.KubeDao;
import com.huiyi.huiyi_fanny.db.SetDao;
import com.huiyi.huiyi_fanny.db.ShipmentDao;
import com.huiyi.huiyi_fanny.db.TuigoodsDao;
import com.huiyi.huiyi_fanny.db.XorderDao;
import com.huiyi.huiyi_fanny.utils.AppManager;
import com.huiyi.huiyi_fanny.utils.DBHelper;
import com.huiyi.huiyi_fanny.utils.HttpConnSoap;
import com.huiyi.huiyi_fanny.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LW on 2017/9/25.
 * 更新页面
 */

public class UpdateActivity extends TopBarBaseActivity implements View.OnClickListener{

    private Button update_bt;//更新按钮
    private TextView kube_tv;//库别信息
    private Button delete_bt;//删除按钮
    private HttpConnSoap Soap = new HttpConnSoap();//连接WebService;
    private String ServerUrl;//URL
    private DBHelper dbhelper;
    private SQLiteDatabase db;
    private String data_1,data_2;
    private String number = "";

    //UI布局
    @Override
    protected int getContentView() {
        AppManager.getAppManager().addActivity(this);
        return R.layout.activity_update;
    }

    //自定义Toolbar
    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        setTitle("更新/清空数据");
        setLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }

    //初始化
    private void initView() {
        update_bt = (Button) findViewById(R.id.update_bt);
        kube_tv = (TextView) findViewById(R.id.update_kube_tv);
        delete_bt = (Button) findViewById(R.id.delete_bt);

        delete_bt.setOnClickListener(this);
        update_bt.setOnClickListener(this);

        //获取接口地址
        SetDao setDao = new SetDao(getBaseContext());
        ServerUrl = setDao.SetCha();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_bt :
                try {
                    //删除库别数据表
                    KubeDao kubeDao = new KubeDao(getBaseContext());
                    kubeDao.DeleteKube();

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                getRemoteInfo();
                break;
            case R.id.delete_bt:
                ShipmentDao shipmentDao = new ShipmentDao(getBaseContext());
                shipmentDao.DeleteShipment();

                TuigoodsDao tuigoodsDao = new TuigoodsDao(getBaseContext());
                tuigoodsDao.DeleteTuigoods();

                ToastUtil.showToast(getBaseContext(),"清空数据库成功 !");
                break;
        }
    }

    //获取json数据
    public void getRemoteInfo() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("更新数据");
        progressDialog.setMessage("正在更新中...");
        progressDialog.onStart();
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Kube("json_kubeAll",ServerUrl);//库别信息
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    progressDialog.dismiss();
                }
            }
        }).start();

    }

    //库别信息
    public void Kube(String fmethodName, String fuwuqi_url )
    {
        String str = Soap.Getjson( fmethodName,fuwuqi_url);
        Message message = handler_kube.obtainMessage();
        message.obj = str;
        handler_kube.sendMessage(message);
    }


    private Handler handler_kube = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // 将WebService得到的结果返回给TextView
            String jsonshuju = msg.obj.toString();
            try {
                ArrayList<String> list = new ArrayList();
                JSONArray jsonArray = new JSONObject(jsonshuju).getJSONArray("ds");// 获得json字符串里名字为ds的数组，如果ds不是最外层，请通过ds的父对象来取
                int count = jsonArray.length();
                for (int index = 0; index < count; index++) {
                    String mId = jsonArray.optJSONObject(index).getString("kb_id").toString();
                    String mName = jsonArray.optJSONObject(index).getString("kb_name").toString();
                    String sql = "insert into t_kube(kb_id,kb_name) values(" + mId + ",'" + mName + "')";
                    list.add(sql);
                }
                inertOrUpdateDateBatch(list);
                // 本地记录开始
                //获取数据库
                db = DBHelper.getInstance(getBaseContext()).getReadableDatabase();

                Cursor cz_ru = db.rawQuery(" select count(*) as jilu from t_kube ", null);
                if (cz_ru != null) {
                    while (cz_ru.moveToNext()) {
                        data_1 = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
                    }
                }
                cz_ru.close();
                db.close();
                // 本地记录结束
                kube_tv.setText("1.库别资料更新成功..." + data_1 + "条");
            } catch (JSONException e) {
                kube_tv.setText("1.库别资料更新成功...0条");
                e.printStackTrace();
            }

        }
    };


    //批量添加公用方法
    public void inertOrUpdateDateBatch(List<String> sqls) {
        //获取数据库
        db = DBHelper.getInstance(getBaseContext()).getReadableDatabase();
        db.beginTransaction();
        try {
            for (String sql : sqls) {
                db.execSQL(sql);
            }
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();
        }
    }

}

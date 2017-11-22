package com.huiyi.huiyi_fanny.ui;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.huiyi.huiyi_fanny.R;
import com.huiyi.huiyi_fanny.base.TopBarBaseActivity;
import com.huiyi.huiyi_fanny.db.SetDao;
import com.huiyi.huiyi_fanny.db.ShipmentDao;
import com.huiyi.huiyi_fanny.db.TuigoodsDao;
import com.huiyi.huiyi_fanny.model.Tuigoods;
import com.huiyi.huiyi_fanny.utils.AppManager;
import com.huiyi.huiyi_fanny.utils.DBHelper;
import com.huiyi.huiyi_fanny.utils.HttpConnSoap;
import com.huiyi.huiyi_fanny.utils.ToastUtil;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by LW on 2017/9/23.
 * 退货管理
 */
public class RefundActivity extends TopBarBaseActivity implements View.OnClickListener {

    private EditText specifications;//退货原因
    private EditText return_time;//退货时间
    private RadioGroup radioGroup;
    private RadioButton offline_rb;//离线出货
    private RadioButton deletion_rb;//本地删除
    private EditText bar_code;//条码
    private TextView qr_number;//离线扫描数量
//    private TextView qrnumbertishi_tv;//显示条码
    private Button save_bt;//保存(后续开发)
    private Button uploading_bt;//上传
    private String ServiceUrl;//url
    private DBHelper dbhelper;
    private SQLiteDatabase db;
    private String sc_shuju = "";String jilu = "0";String shuliang = "0";String ma;
    private ArrayList<String> arrayList = new ArrayList();
    private ArrayList<String> brrayList = new ArrayList();
    private ArrayList<String> crrayList = new ArrayList();
    private HttpConnSoap Soap = new HttpConnSoap();
    private MediaPlayer mediaPlayer;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg){
            String str = msg.obj.toString();
            if ((str.trim().equals("")) || (str.trim() == null)){
                ToastUtil.showToast(getBaseContext(),"上传失败，请检测网络是否正常！");
                return;
            }
            qr_number.setText("0");
            ToastUtil.showToast(getBaseContext(),str);
            TuigoodsDao tuigoodsDao = new TuigoodsDao(getBaseContext());
            tuigoodsDao.DeleteTuigoods();

        }
    };

    //UI布局
    @Override
    protected int getContentView() {
        AppManager.getAppManager().addActivity(this);
        return R.layout.activity_refund;
    }

    //自定义Toolbar
    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        setTitle("退货管理");
        setLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(RefundActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //初始化
    private void initView() {
        specifications = (EditText) findViewById(R.id.refund_specifications_et);
        return_time = (EditText) findViewById(R.id.refund_return_time_et);
        radioGroup = (RadioGroup) findViewById(R.id.refund_rg);
        offline_rb = (RadioButton) findViewById(R.id.refund_offline_rb);
        deletion_rb = (RadioButton) findViewById(R.id.refund_deletion_rb);
        bar_code = (EditText) findViewById(R.id.refund_bar_code_et);
        qr_number = (TextView) findViewById(R.id.refund_qr_number_tv);
//        qrnumbertishi_tv = (TextView) findViewById(R.id.refund_qrnumberxianshi_tv);
//        save_bt = (Button) findViewById(R.id.refund_save_bt);
        uploading_bt = (Button) findViewById(R.id.refund_uploading_bt);

//        save_bt.setOnClickListener(this);
        uploading_bt.setOnClickListener(this);


        //获取接口
        SetDao setDao = new SetDao(getBaseContext());
        ServiceUrl = setDao.SetCha();
        //获取数据库
        db = DBHelper.getInstance(getBaseContext()).getReadableDatabase();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return_time.setText(str);

        // 离线入库的记录结束
        qr_number.setText(shuliang);// 本地添加数量
        bar_code.requestFocus();// 获取光标
        // 触发回车键
        bar_code.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 是否是回车键 event.getAction() == KeyEvent.ACTION_DOWN是处理让他只执行一次
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                        //获取数据库
                        db = DBHelper.getInstance(getBaseContext()).getReadableDatabase();

                        final String reason = specifications.getText().toString();// 退货原因

                        // 退货时间
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                        Date xs_shijian1 = addOneSecond(curDate);//当前时间加一秒
                        String str = sdf.format(xs_shijian1);  //日期转字符串
                        final String dateTime = str;
                        return_time.setText(dateTime);

                        // 条码
                        ma = bar_code.getText().toString();

                        if (reason.trim().equals("") || reason.trim() == null) {
                            Toast.makeText(getBaseContext(), "退货原因不能为空！", Toast.LENGTH_SHORT).show();
                            specifications.setText("");

                            //播放错误信息
                            mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                            mediaPlayer.start();

                        } else if (dateTime.trim().equals("") || dateTime.trim() == null) {
                            Toast.makeText(getBaseContext(), "退货日期不能为空！", Toast.LENGTH_SHORT).show();
                            return_time.setText("");

                            //播放错误信息
                            mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                            mediaPlayer.start();
                        } else if (ma.trim().equals("") || ma.trim() == null) {
                            Toast.makeText(getBaseContext(), "条码不能为空！", Toast.LENGTH_SHORT).show();
                            bar_code.setText("");

                            //播放错误信息
                            mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                            mediaPlayer.start();
                        } else {
//                            String canshuzhi_pd = getValueByName(ma, "c");
//                            if (canshuzhi_pd == null || canshuzhi_pd.equals("")) {
//                                Toast.makeText(getBaseContext(), "二维码格式不正确！", Toast.LENGTH_SHORT).show();
//                                bar_code.setText("");
//
//                                //播放错误信息
//                                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
//                                mediaPlayer.start();

                            
                                // 条码是否已存在开始
                                Cursor ru_z = db.rawQuery(" select count(*) as jilu from t_tuigoods where t_xscode='"
                                                + ma + "'   ", null);
                                if (ru_z != null) {
                                    while (ru_z.moveToNext()) {
                                        jilu = ru_z.getString(ru_z.getColumnIndex("jilu"));
                                        String test1 = jilu;
                                    }
                                }
                                ru_z.close();


                                //离线出货
                                if (offline_rb.isChecked()) {
                                    if (Integer.parseInt(jilu) == 0) {
                                        TuigoodsDao tgDao = new TuigoodsDao(getBaseContext());
                                        tgDao.AddTuigoods(ma, dateTime, reason);
                                        ToastUtil.showToast(getBaseContext(), "离线出货成功！");
                                        bar_code.setText("");

//                                        //显示条码截取
//                                        String str1 = ma;
//                                        String result = str1.substring(str1.indexOf("2"));
//                                        qrnumbertishi_tv.setText(result);


                                    } else {
                                        ToastUtil.showToast(getBaseContext(), "条码已存在！");
                                        bar_code.setText("");
                                        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                                        vibrator.vibrate(new long[]{0, 1000}, -1);

                                        //播放错误信息
                                        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                        mediaPlayer.start();

                                    }
                                }
                                //本地删除
                                if (deletion_rb.isChecked()) {
                                    if (Integer.parseInt(jilu) != 0) {
                                        TuigoodsDao tgDao = new TuigoodsDao(getBaseContext());
                                        tgDao.DeleteTuigoods_ma(ma);
                                        ToastUtil.showToast(getBaseContext(), "本地删除成功！");
                                        bar_code.setText("");

//                                        //显示条码截取
//                                        String str1 = ma;
//                                        String result = str1.substring(str1.indexOf("2"));
//                                        qrnumbertishi_tv.setText(result);

                                    } else {
                                        ToastUtil.showToast(getBaseContext(), "条码不存在！");
                                        bar_code.setText("");
                                        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                                        vibrator.vibrate(new long[]{0, 1000}, -1);

                                        //播放错误信息
                                        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                        mediaPlayer.start();

                                    }
                                }
                                // 离线入库的记录开始
                                Cursor cz_ru = db.rawQuery(" select count(*) as jilu  from  t_tuigoods ", null);
                                if (cz_ru != null) {
                                    while (cz_ru.moveToNext()) {
                                        shuliang = cz_ru.getString(cz_ru.getColumnIndex("jilu"));
                                    }
                                }
                                cz_ru.close();

                                // 离线入库的记录结束
                                qr_number.setText(shuliang);// 本地添加数量
                                bar_code.setText("");

                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }

        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.refund_uploading_bt:
                //获取数据库
                db = DBHelper.getInstance(getBaseContext()).getReadableDatabase();

                Cursor cursor_up = db.rawQuery("select count(*) as jilu from t_tuigoods",null);
                if (cursor_up != null){
                    while (cursor_up.moveToNext()){
                        shuliang = cursor_up.getString(cursor_up.getColumnIndex("jilu"));
                    }
                }
                cursor_up.close();

                if (Integer.parseInt(shuliang) == 0){
                    ToastUtil.showToast(getBaseContext(),"无上传数据!");
                }else {
                    getUpLoading();
                }
                break;
            default:
                break;

        }
    }

    /***
     * 获取url 指定name的value;
     *
     * @param url
     *            网址
     * @param name
     *            参数名
     * @return
     */
    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }

        return result;
    }

    // 时间类型加一秒
    public Date addOneSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, 1);
        return calendar.getTime();
    }

    //上传
    public void  getUpLoading(){
        //获取数据库
        db = DBHelper.getInstance(getBaseContext()).getReadableDatabase();

        ArrayList localArrayList = new ArrayList();
        Cursor lc = db.rawQuery("select * from t_tuigoods",null);
        if (lc != null){
            while (lc.moveToNext()){
                String str1 = lc.getString(lc.getColumnIndex("t_xscode"));
                String str2 = lc.getString(lc.getColumnIndex("t_remarks"));
                String str3 = lc.getString(lc.getColumnIndex("t_datetime"));
                localArrayList.add(str1 + "," + str2 + "," + str3);
            }
        }
        lc.close();

        sc_shuju = (StringUtils.join(localArrayList.toArray(), ",") + ",");
        UpLoading();
    }

    public void UpLoading(){
        final ProgressDialog localProgressDialog = new ProgressDialog(this);
        localProgressDialog.setTitle("上传数据");
        localProgressDialog.setMessage("正在上传中...");
        localProgressDialog.onStart();
        localProgressDialog.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    shang(sc_shuju);
                } catch (Exception localException) {
                    localException.printStackTrace();
                } finally {
                    localProgressDialog.dismiss();
                }
            }
        }).start();
    }

    private void shang(String paramString) {
        ArrayList<String> list = new ArrayList<>();
        arrayList.clear();
        brrayList.clear();

        arrayList.add("shuju");
        brrayList.add(paramString);
        crrayList =  Soap.GetWebServre("xml_pi_t_tuigoods",arrayList,brrayList,ServiceUrl);
        String str1 = String.valueOf(crrayList);
        String str2 = str1.substring(1 + str1.indexOf("["),str1.indexOf("]")).trim();

        Message localMessage = handler.obtainMessage();
        localMessage.obj = str2;
        handler.sendMessage(localMessage);
    }
}

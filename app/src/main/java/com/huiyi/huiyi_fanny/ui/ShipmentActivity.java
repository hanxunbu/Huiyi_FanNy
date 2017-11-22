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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.huiyi.huiyi_fanny.R;
import com.huiyi.huiyi_fanny.adapter.GoodNameAdapter;
import com.huiyi.huiyi_fanny.base.TopBarBaseActivity;
import com.huiyi.huiyi_fanny.db.SetDao;
import com.huiyi.huiyi_fanny.db.ShipmentDao;
import com.huiyi.huiyi_fanny.model.Dict_Product;
import com.huiyi.huiyi_fanny.model.GoodsListEntry;
import com.huiyi.huiyi_fanny.utils.AppManager;
import com.huiyi.huiyi_fanny.utils.DBHelper;
import com.huiyi.huiyi_fanny.utils.ExpandAnimation;
import com.huiyi.huiyi_fanny.utils.HttpConnSoap;
import com.huiyi.huiyi_fanny.utils.ToastUtil;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * Created by lw on 2017/9/23.
 */
public class ShipmentActivity extends TopBarBaseActivity implements View.OnClickListener{
    private EditText odd_et;//出货单号
    private Button seek_bt;//搜索
    private EditText product_number_et;//产品编号
    private EditText product_name_et;//产品名称
    private EditText customer_number_et;//客户编号
    private EditText customer_name_et;//客户名称
    private EditText shipping_address_et;//收货地址
    private EditText shipping_number_et;//交货数量
    private Spinner shipment_kuname_sp;//出货库名
    private EditText shipping_time_et;//出货时间
    private RadioButton offline_rb;//离线出货
    private RadioButton deletion_rb;//本地删除
    private EditText shipping_code_et;//扫码
    private TextView yifahuo_number;//已发货数量
    private TextView fahuo_number;//扫描货品数量
    private Button qiehuan_btn;//切换
    private Button uploading_bt;//上传
    private ImageView imgClick;
    private LinearLayout llShow;//隐藏界面元素
    private SQLiteDatabase db;//隐藏界面元素
    private DBHelper dbHelper;
    private ArrayList<String> arrayList = new ArrayList();
    private ArrayList<String> brrayList = new ArrayList();
    private ArrayList<String> crrayList = new ArrayList();
    private HttpConnSoap Soap = new HttpConnSoap();
    private String ServerUrl,sc_shuju = "",jilu = "0",kubeID = "0",datetime,xcode,shuliang,xorderID = "0",x_yquantity,x_xquantity;
    private PopupWindow popupWindow;
    private RecyclerView mendianList;
    private List<GoodsListEntry.GoodEntry> goodlist;
    private MediaPlayer mediaPlayer;
    private GoodNameAdapter goodNameAdapter;
    private String str2,str4,mMessage;
    private int zongshu = 0,mChildrenamount;
    private String Url;
    //显示那个商品
    private int selectGoodIndex = 0;

    //获取码的数量
    private Handler handler_code = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // 将WebService得到的结果返回给TextView
            String jsonshuju = msg.obj.toString();
            try {
                ArrayList<String> list = new ArrayList();
                JSONArray jsonArray = new JSONObject(jsonshuju).getJSONArray("ds");// 获得json字符串里名字为ds的数组，如果ds不是最外层，请通过ds的父对象来取
                int count = jsonArray.length();
                for (int index = 0; index < count; index++) {
                    String mCode = jsonArray.optJSONObject(index).getString("code").toString();
                    String mProductcode = jsonArray.optJSONObject(index).getString("productcode").toString();
                    mChildrenamount = Integer.parseInt(jsonArray.optJSONObject(index).getString("childrenamount").toString());
                    mMessage = jsonArray.optJSONObject(index).getString("message").toString();
                    if (mMessage.equals("")){
                        ToastUtil.showToast(getBaseContext(),"离线添加出货数据成功！");
                        zongshu += mChildrenamount;
                        fahuo_number.setText(zongshu + "");
                    }else {
                        ToastUtil.showToast(getBaseContext(),mMessage);
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            String str = msg.obj.toString();
            if ((str.trim().equals("")) || (str.trim() == null)) {
                ToastUtil.showToast(getBaseContext(),"上传失败，请检测网络是否正常！");

            }else {
                ToastUtil.showToast(getBaseContext(),str);
                ShipmentDao shipmentDao = new ShipmentDao(getBaseContext());
                shipmentDao.DeleteShipment();

                goodlist.remove(selectGoodIndex);
                if(goodlist.size() == 0){
                    finish();
                }else{
                    selectGood();
                }
            }
        }
    };

    //UI布局
    @Override
    protected int getContentView() {
        AppManager.getAppManager().addActivity(this);
        return R.layout.activity_shipment;
    }

    //自定义Toolbar
    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        setTitle("出货管理");
        setLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(ShipmentActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        odd_et = (EditText) findViewById(R.id.shipment_shipment_odd_et);
        seek_bt = (Button) findViewById(R.id.shipment_seek_bt);
        product_number_et = (EditText) findViewById(R.id.shipment_product_number_et);
        product_name_et = (EditText) findViewById(R.id.shipment_product_name_et);
        customer_number_et = (EditText) findViewById(R.id.shipment_customer_number_et);
        customer_name_et = (EditText) findViewById(R.id.shipment_customer_name_et);
        shipping_address_et = (EditText) findViewById(R.id.shipment_shipping_address_et);
        shipping_number_et = (EditText) findViewById(R.id.shipment_shipping_number_et);
        shipment_kuname_sp = (Spinner) findViewById(R.id.shipment_shipment_kuname_sp);
        shipping_time_et = (EditText) findViewById(R.id.shipment_shipping_time_et);
        offline_rb = (RadioButton) findViewById(R.id.shipment_offline_rb);
        deletion_rb = (RadioButton) findViewById(R.id.shipment_deletion_rb);
        shipping_code_et = (EditText) findViewById(R.id.shipment_shipping_code_et);
        uploading_bt = (Button) findViewById(R.id.shipment_uploading_bt);
        qiehuan_btn = (Button) findViewById(R.id.shipment_qiehaun_btn);//切换
        fahuo_number = (TextView) findViewById(R.id.shipment_fahuo_number_tv);//扫描货品
        yifahuo_number = (TextView) findViewById(R.id.shipment_yifahuo_number_tv);//已发货

        imgClick = (ImageView) findViewById(R.id.img_click);
        llShow = (LinearLayout) findViewById(R.id.ll_show);


        imgClick.setOnClickListener(this);
        seek_bt.setOnClickListener(this);
        qiehuan_btn.setOnClickListener(this);//切换
        uploading_bt.setOnClickListener(this);
        shipping_time_et.setOnClickListener(this);//时间文本框
        //获取接口
        SetDao setDao = new SetDao(getBaseContext());
        ServerUrl = setDao.SetCha();
        //切换接口
        String str = ServerUrl;
        str = str.substring(0,str.indexOf("w"));
        Url = str + "CodeWeb.asmx";

        //获取数据库
        db = DBHelper.getInstance(getBaseContext()).getReadableDatabase();

        //库别名称
        Cursor cursor = db.rawQuery("select * from t_kube",null);
        List<Dict_Product> larrayList = new ArrayList<>();
        if (cursor != null){
            while(cursor.moveToNext()){
                int kb_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("kb_id")));
                String kb_name = cursor.getString(cursor.getColumnIndex("kb_name"));
                larrayList.add(new Dict_Product(Integer.valueOf(kb_id),kb_name));
            }
        }
        cursor.close();

        //Adapter适配器
        ArrayAdapter<Dict_Product> arrayAdapter = new ArrayAdapter<Dict_Product>(this,android.R.layout.simple_spinner_item,larrayList);
        //设置样式
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        //加载适配器
        shipment_kuname_sp.setAdapter(arrayAdapter);
        //注册OnItemSelected事件
        shipment_kuname_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shipment_kuname_sp.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ToastUtil.showToast(getBaseContext(),"什么也没有选!");
            }
        });

        //当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        datetime = formatter.format(curDate);
        shipping_time_et.setText(datetime);

    }

    View.OnKeyListener onKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            //是否是回车键  event.getAction() == KeyEvent.ACTION_DOWN是处理让他只执行一次
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm.isActive()){
                    imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    db = DBHelper.getInstance(getBaseContext()).getReadableDatabase();

                    xcode = shipping_code_et.getText().toString();//条码
                    datetime = shipping_time_et.getText().toString();//时间
                    str2 = customer_number_et.getText().toString();//客户编号
                    str4 = odd_et.getText().toString();//出货单号

                    if (str4.trim().equals("") || str4.trim() == null){
                        ToastUtil.showToast(getBaseContext(),"交货单号不能为空!");
                        odd_et.setText("");

                        //播放错误信息
                        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                        mediaPlayer.start();

                    } else if (xcode.trim().equals("") || xcode.trim() == null){
                        ToastUtil.showToast(getBaseContext(),"条码不能为空!");
                        shipping_code_et.setText("");

                        //播放错误信息
                        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                        mediaPlayer.start();

                    }else {
                        //选择框
                        //离线
                        if (offline_rb.isChecked()){
                            Cursor lc1 = db.rawQuery(" select COUNT(*) as jilu from  t_shipment where sh_xscode='" + xcode + "'  ", null);
                            if (lc1 != null){
                                while (lc1.moveToNext()){
                                    jilu = lc1.getString(lc1.getColumnIndex("jilu"));
                                }
                            }
                            lc1.close();
                            String test_jilu = jilu;

                            if (Integer.parseInt(test_jilu) == 0){

                                getRemoteInfo();

                                ShipmentDao shipmentDao = new ShipmentDao(getBaseContext());
                                shipmentDao.AddShipment(xcode,str2, xorderID,kubeID,datetime,"1");

                                shipping_code_et.setText("");

                            }else {
                                ToastUtil.showToast(getBaseContext(),"条码已存在!");
                                shipping_code_et.setText("");
                                Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                                vibrator.vibrate(400);

                                //播放错误信息
                                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                mediaPlayer.start();
                            }
                        }
                        //本地删除
                        if (deletion_rb.isChecked()){
                            Cursor lc2 = db.rawQuery(" select count(*) as jilu  from  t_shipment ", null);
                            if (lc2 != null){
                                while (lc2.moveToNext()){
                                    jilu = lc2.getString(lc2.getColumnIndex("jilu"));
                                }
                            }
                            lc2.close();
                            String test_jilu = jilu;

                            if (Integer.parseInt(test_jilu) == 0){

                                ToastUtil.showToast(getBaseContext(),"条码不存在!");
                                shipping_code_et.setText("");
                                Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                                vibrator.vibrate(400);

                                //播放错误信息
                                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.warring);
                                mediaPlayer.start();

                            }else {
                                ShipmentDao shipmentDao = new ShipmentDao(getBaseContext());
                                shipmentDao.DeleteShipment_ma(xcode);
                                ToastUtil.showToast(getBaseContext(),"本地删除成功!");

                                shipping_code_et.setText("");

                            }
                        }
                    }
                }
                return true;
            }else {
                return false;
            }
        }
    };
    private final int getEntry = 111;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shipment_qiehaun_btn:
                shupPopup(view);
                break;

            case R.id.img_click:
                //第一个view参数是点击后修改的对象控件，第二个view参数是需要隐藏显示的布局控件，第三个参数是动画持续时间，单位毫秒
                ExpandAnimation animation = new ExpandAnimation(imgClick,llShow,500);
                llShow.startAnimation(animation);
                break;

            case R.id.shipment_seek_bt:
                Intent intent_seek = new Intent(ShipmentActivity.this,SeekActivity.class);
                startActivityForResult(intent_seek,getEntry);

                break;

            case R.id.shipment_uploading_bt:
                db = DBHelper.getInstance(getBaseContext()).getReadableDatabase();
                Cursor cursor_up = db.rawQuery("select count(*) as jilu from t_shipment",null);
                if (cursor_up != null){
                    while (cursor_up.moveToNext()){
                        shuliang = cursor_up.getString(cursor_up.getColumnIndex("jilu"));
                    }
                }
                cursor_up.close();

                if (Integer.parseInt(shuliang) == 0){
                    ToastUtil.showToast(getBaseContext(),"无上传数据!");
                    shipping_code_et.setText("");

                }else {
                    getUpLoading();
                }
                break;
            default:
                break;
        }
    }

    public void getUpLoading() {
        dbHelper = new DBHelper(getBaseContext());
        db = dbHelper.getReadableDatabase();

        ArrayList localArrayList = new ArrayList();
        Cursor lc = db.rawQuery("select * from t_shipment ", null);
        if (lc != null) {
            while (lc.moveToNext()) {
                String str1 = lc.getString(lc.getColumnIndex("sh_xscode"));
                String str2 = lc.getString(lc.getColumnIndex("sh_dcode"));
                String str3 = lc.getString(lc.getColumnIndex("sh_xorderID"));
                String str4 = lc.getString(lc.getColumnIndex("sh_kubeID"));
                String str5 = lc.getString(lc.getColumnIndex("sh_datetime"));
                String str6 = lc.getString(lc.getColumnIndex("sh_cquantity"));
                localArrayList.add(str1 + "," + str2 + "," + str3 + "," + str4 + "," + str5);

            }
        }
        lc.close();
        sc_shuju = (StringUtils.join(localArrayList.toArray(), ",") + ",");
        UpLoading();
    }

    public void UpLoading() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("上传数据");
        progressDialog.setMessage("正在上传中...");
        progressDialog.onStart();
        progressDialog.show();
        new Thread(new Runnable() {
            public void run() {
                try{
                    shang(sc_shuju);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    progressDialog.dismiss();
                }
            }
        }).start();
    }

    public void shang(String paramString) {
        ArrayList<String> list = new ArrayList<String>();
        arrayList.clear();
        brrayList.clear();

        arrayList.add("shuju");
        brrayList.add(paramString);

        crrayList = Soap.GetWebServre("xml_pi_t_shipment", arrayList, brrayList, ServerUrl);
        String str1 = String.valueOf(crrayList);
        String str2 = str1.substring(1 + str1.indexOf("["), str1.indexOf("]")).trim();
        Message localMessage = handler.obtainMessage();
        localMessage.obj = str2;

        handler.sendMessage(localMessage);
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
                    Code(xcode,"GetCodeInfo ", Url);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    progressDialog.dismiss();
                }
            }
        }).start();

    }

    //码的数量信息
    public void Code(String code,String fmethodName, String fuwuqi_url )
    {
        String str = Soap.Getjson3(code, fmethodName,fuwuqi_url);
        Message message = handler_code.obtainMessage();
        message.obj = str;
        handler_code.sendMessage(message);

    }

    /**
     * 重写onActivityResult 用于返回Seek传送的值
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK && requestCode == getEntry){
            GoodsListEntry entry = (GoodsListEntry) data.getSerializableExtra("entry");
            if(entry != null){
                String x_number =entry.getX_number();
                odd_et.setText(x_number);
                goodlist = entry.getGoodlist();
                //重新赋值
                selectGoodIndex = 0;
                selectGood();

            }
        }
    }
    //重复使用的元素封装
    private void selectGood() {

        GoodsListEntry.GoodEntry goodEntry = goodlist.get(selectGoodIndex);

        xorderID = goodEntry.getX_id();
        String p_number = goodEntry.getP_number();
        String p_name = goodEntry.getP_name();
        String p_standard = goodEntry.getP_standard();
        String c_number = goodEntry.getC_number();
        String c_name = goodEntry.getC_name();
        String c_address =goodEntry.getC_address();
        x_xquantity = goodEntry.getX_xquantity();
        x_yquantity = goodEntry.getX_yquantity();

        product_name_et.setText(p_name);
        product_number_et.setText(p_number);
        customer_number_et.setText(c_number);
        customer_name_et.setText(c_name);
        shipping_address_et.setText(c_address);
        shipping_number_et.setText(x_xquantity);
        yifahuo_number.setText(x_yquantity);
        shipping_code_et.requestFocus();//获取光标
        // 触发回车键
        shipping_code_et.setOnKeyListener(onKey);

    }

    //这里使用RecyclerView
    private void shupPopup(View v ){
        View contentView = LayoutInflater.from(ShipmentActivity.this).inflate(R.layout.popup_mendian, null);
        // 设置按钮的点击事件
        popupWindow = new PopupWindow(contentView,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mendianList = (RecyclerView) contentView.findViewById(R.id.mendianListview);
        mendianList.setLayoutManager(new GridLayoutManager(ShipmentActivity.this,2));
        if( goodlist == null ||  goodlist.size() == 0 || goodlist.size() == 1){
            ToastUtil.showToast(ShipmentActivity.this, "还没选择货号或只有单件");
            if(popupWindow!= null){
                popupWindow.dismiss();
            }
        }else{
            goodNameAdapter = new GoodNameAdapter(goodlist,this);
            mendianList.setAdapter(goodNameAdapter);
            goodNameAdapter.setOnItemClickListener(new GoodNameAdapter.OnItemClickListener() {

                @Override
                public void onItemClickListener(String id, int position) {
                    selectGoodIndex = position;
                    selectGood();
                    popupWindow.dismiss();
                }
            });
        }

        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.et_btn));

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int popupWidth = v.getMeasuredWidth();    //  获取测量后的宽度
        int popupHeight = v.getMeasuredHeight();
        Log.e("cascascsacsagh", popupHeight + " - " + popupWidth + " - " + popupWindow.getHeight());
        popupWindow.showAsDropDown(v);

    }


}

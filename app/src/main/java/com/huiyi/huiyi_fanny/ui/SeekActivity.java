package com.huiyi.huiyi_fanny.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.huiyi.huiyi_fanny.R;
import com.huiyi.huiyi_fanny.adapter.GoodsListAdapter;
import com.huiyi.huiyi_fanny.db.SetDao;
import com.huiyi.huiyi_fanny.model.GoodsListEntry;
import com.huiyi.huiyi_fanny.utils.AppManager;
import com.huiyi.huiyi_fanny.utils.HttpConnSoap;
import com.huiyi.huiyi_fanny.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lw on 2017/10/9.
 */

public class SeekActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText seek_et;
    private Button seek_bt_1;
    private ListView seek_lv;
    //根据这个集合来进行适配器
    private List<GoodsListEntry> goodsList = new ArrayList<>();
    private GoodsListAdapter goodsListAdapter;
    private String number = "";
    private String ServerUrl;


    //网络请求,数据进行合并
    private Handler handler_xorder = new Handler() {
        public void handleMessage(Message msg) {
            String str1 = msg.obj.toString();
            try {
                JSONArray jsonArray = new JSONObject(str1).getJSONArray("ds");
                int i = jsonArray.length();
                for (int j = 0;j < i; j++) {
                    String x_id = jsonArray.optJSONObject(j).getString("x_id").toString();
                    String x_number = jsonArray.optJSONObject(j).getString("x_number").toString();
                    String p_number = jsonArray.optJSONObject(j).getString("p_number").toString();
                    String p_name = jsonArray.optJSONObject(j).getString("p_name").toString();
                    String p_standard = jsonArray.optJSONObject(j).getString("p_standard").toString();
                    String c_number = jsonArray.optJSONObject(j).getString("c_number").toString();
                    String c_name = jsonArray.optJSONObject(j).getString("c_name").toString();
                    String c_address = jsonArray.optJSONObject(j).getString("c_address").toString();
                    String x_xquantity = jsonArray.optJSONObject(j).getString("x_xquantity").toString();
                    String x_yquantity = jsonArray.optJSONObject(j).getString("x_yquantity").toString();

                    int isCombining = -1;// 自己定义: -1则表示查找不到合拼,查找到就赋值
                    for (int k = 0;k < goodsList.size();k++){
                        GoodsListEntry goodsListEntry = goodsList.get(k);
                        //如果相同就把k赋值 下面进行插入合拼并跳出该循环
                        if (goodsListEntry.getX_number().equals(x_number)){
                            isCombining = k;
                            break;
                        }
                    }
                    //循环外判断是否合拼 -1代表没有相同 就新增新的交货单  否则 在这相同的交货单里面添加除 交货单不同的数据
                    if (isCombining == -1){
                        GoodsListEntry goodsListEntry = new GoodsListEntry();
                        goodsListEntry.setX_number(x_number);//交货单

                        GoodsListEntry.GoodEntry good = new GoodsListEntry.GoodEntry();
                        good.setX_id(x_id);
                        good.setP_number(p_number);
                        good.setP_name(p_name);
                        good.setP_standard(p_standard);
                        good.setC_number(c_number);
                        good.setC_name(c_name);
                        good.setC_address(c_address);
                        good.setX_xquantity(x_xquantity);
                        good.setX_yquantity(x_yquantity);

                        List<GoodsListEntry.GoodEntry> goods = new ArrayList<>();
                        goods.add(good);

                        goodsListEntry.setGoodlist(goods);
                        goodsList.add(goodsListEntry);
                    }else {
                        //这里是因为查找到有相同的 交货单 合拼 isCombining是看在list里面什么位置
                        GoodsListEntry goodsListEntry = goodsList.get(isCombining);
                        List<GoodsListEntry.GoodEntry> goodlist = goodsListEntry.getGoodlist();//添加到这个集合
                        //同单号不同其他的货品
                        GoodsListEntry.GoodEntry good = new GoodsListEntry.GoodEntry();
                        good.setX_id(x_id);
                        good.setP_number(p_number);
                        good.setP_name(p_name);
                        good.setP_standard(p_standard);
                        good.setC_number(c_number);
                        good.setC_name(c_name);
                        good.setC_address(c_address);
                        good.setX_xquantity(x_xquantity);
                        good.setX_yquantity(x_yquantity);
                        goodlist.add(good);
                        goodsListAdapter.notifyDataSetChanged();
                    }
                }
            }catch (Exception e){
                ToastUtil.showToast(getApplicationContext(), "加载过程中发生异常：" + e.getMessage());
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seek);
        AppManager.getAppManager().addActivity(this);
        initView();

        SetDao setDao = new SetDao(getBaseContext());
        ServerUrl = setDao.SetCha();

    }

    private void initView() {
        seek_bt_1 = (Button) findViewById(R.id.seek_bt_1);
        seek_et = (EditText) findViewById(R.id.seek_et);
        seek_lv = (ListView) findViewById(R.id.seek_lv);

        seek_bt_1.setOnClickListener(this);
        GetSimpleAdapter();
        seek_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //传送数据到Shipment
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("entry",goodsList.get(i));
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.seek_bt_1:
                number = seek_et.getText().toString();
                initData(number);
                break;
            default:
                break;
        }
    }

    private void initData(String paramString) {
        goodsList.removeAll(goodsList);
        getRemoteInfo();

    }

    public void xorder(String paramString1, String paramString2){

        String str = new HttpConnSoap().Getjson2(paramString1,"x_number",number,paramString2);
        Message localMessage = handler_xorder.obtainMessage();
        localMessage.obj = str;
        handler_xorder.sendMessage(localMessage);
    }

    public void getRemoteInfo() {
        final ProgressDialog localProgressDialog = new ProgressDialog(this);
        localProgressDialog.setCanceledOnTouchOutside(false);
        localProgressDialog.setTitle("获取服务器数据");
        localProgressDialog.setMessage("正在加载中...");
        localProgressDialog.onStart();
        localProgressDialog.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    xorder("json_xorderAll_canshu", ServerUrl);
                    return;
                } catch (Exception e ) {
                    e.printStackTrace();
                    return;
                } finally {
                    localProgressDialog.dismiss();
                }
            }
        }).start();
    }

    //适配器
    public void GetSimpleAdapter(){
        goodsListAdapter = new GoodsListAdapter(this,goodsList);
        seek_lv.setAdapter(goodsListAdapter);
    }


}
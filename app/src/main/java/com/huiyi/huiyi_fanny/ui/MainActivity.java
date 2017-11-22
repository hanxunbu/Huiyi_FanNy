package com.huiyi.huiyi_fanny.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.huiyi.huiyi_fanny.R;
import com.huiyi.huiyi_fanny.utils.AppManager;


/**
 *主页
 * */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout shipment_ll;//出货
    private LinearLayout refund_ll;//退货
    private LinearLayout update_ll;//更新
    private LinearLayout exit_ll;//退出

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppManager.getAppManager().addActivity(this);
        initView();

    }

    private void initView() {
        shipment_ll = (LinearLayout) findViewById(R.id.main_shipment_ll);
        refund_ll = (LinearLayout) findViewById(R.id.main_refund_ll);
        update_ll = (LinearLayout) findViewById(R.id.main_update_ll);
        exit_ll = (LinearLayout) findViewById(R.id.main_exit_ll);

        shipment_ll.setOnClickListener(this);
        refund_ll.setOnClickListener(this);
        update_ll.setOnClickListener(this);
        exit_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_shipment_ll:
                Intent intent_shipment = new Intent(MainActivity.this,ShipmentActivity.class);//出货
                startActivity(intent_shipment);
//                Intent intent_shipment = new Intent(MainActivity.this,ShipmentActivity.class);//出货
//                startActivity(intent_shipment);
                break;
            case R.id.main_refund_ll:
                Intent intent_refund = new Intent(MainActivity.this,RefundActivity.class);//退货
                startActivity(intent_refund);
                break;
            case R.id.main_update_ll:
                Intent intent_update = new Intent(MainActivity.this,UpdateActivity.class);//更新
                startActivity(intent_update);
                break;
            case R.id.main_exit_ll:
                onKeyDown(KeyEvent.KEYCODE_BACK,null);
                break;
            default:
                break;
        }
    }

    // 监听手机的返回键，按到返回键时执行这个方法，提示是否退出。
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 弹出确定退出对话框
            new AlertDialog.Builder(this)
                    .setTitle("退出")
                    .setMessage("确定退出吗？")
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppManager.getAppManager().finishActivity();
                            AppManager.getAppManager().AppExit(this);
                        }
                    }).show();
            // 这里不需要执行父类的点击事件，所以直接return
            return true;
        }
        // 继续执行父类的其他点击事件
        return super.onKeyDown(keyCode, event);
    }


}

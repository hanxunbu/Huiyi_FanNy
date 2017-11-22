package com.huiyi.huiyi_fanny.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.huiyi.huiyi_fanny.R;
import com.huiyi.huiyi_fanny.base.TopBarBaseActivity;
import com.huiyi.huiyi_fanny.utils.AppManager;

/**
 * Created by LW on 2017/11/20.
 */

public class ChuHuoActivity extends TopBarBaseActivity {

    private Button find_bt;
    private ListView lv;

    @Override
    protected int getContentView() {
        AppManager.getAppManager().addActivity(this);
        return R.layout.activity_chuhuo;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        setTitle("出货管理");
        setLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(ChuHuoActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        find_bt = (Button) findViewById(R.id.chuhuo_find_btn);
        lv = (ListView) findViewById(R.id.chuhuo_lv);
    }
}

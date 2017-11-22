package com.huiyi.huiyi_fanny.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lw on 2017/10/10.
 * Toast封装
 */

public class ToastUtil{

    private static Toast toast;

    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

}

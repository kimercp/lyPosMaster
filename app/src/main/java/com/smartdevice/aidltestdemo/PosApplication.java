package com.smartdevice.aidltestdemo;

import android.app.Application;

import com.smartdevice.aidltestdemo.manager.ActionService;
import com.smartdevice.aidltestdemo.manager.Normal;
import com.smartdevice.aidltestdemo.utils.AppUtils;

/**
 * Created by Administrator on 2018/03/29.
 */

public class PosApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.init(getApplicationContext());
        ActionService.Instance();
        new Normal().init();
    }
}

package com.github.nwhhades.base.application;

import android.app.Application;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.github.nwhhades.base.toaster.MyBlackToastStyle;
import com.hjq.toast.Toaster;

public abstract class BaseApplication extends Application {

    protected abstract boolean enableLog();

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        initLogUtils();
        initToaster();
    }

    protected void initLogUtils() {
        LogUtils.Config config = LogUtils.getConfig();
        config.setLogSwitch(enableLog());
    }

    protected void initToaster() {
        Toaster.init(this, new MyBlackToastStyle());
    }

}

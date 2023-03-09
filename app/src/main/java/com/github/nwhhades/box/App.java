package com.github.nwhhades.box;

import com.github.nwhhades.base.application.BaseApplication;
import com.github.nwhhades.box.ijk.IjkPlayerFactory;
import com.github.nwhhades.player.config.PlayerConfig;

public class App extends BaseApplication {

    @Override
    protected boolean enableLog() {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PlayerConfig.getInstance().put("IJK", IjkPlayerFactory.class.getName());

    }

}

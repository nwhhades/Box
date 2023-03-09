package com.github.nwhhades.player.config;

import android.util.Log;

import com.blankj.utilcode.util.CacheDiskStaticUtils;

import com.github.nwhhades.player.inf.IPlayerFactory;
import com.github.nwhhades.player.simple.AndroidFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerConfig implements Serializable {

    private static PlayerConfig instance = null;

    public static synchronized PlayerConfig getInstance() {
        if (instance == null) {
            instance = (PlayerConfig) CacheDiskStaticUtils.getSerializable(TAG, new PlayerConfig());
        }
        return instance;
    }

    private static final String TAG = "PlayerConfig";
    private static final String player_1 = "安卓原生播放器";

    private final Map<String, String> map;
    private volatile String curPlayerFactory = player_1;

    private PlayerConfig() {
        map = new HashMap<>(20);
        map.put(player_1, AndroidFactory.class.getName());
    }

    public IPlayerFactory<?> getPlayerFactory() {
        Log.d(TAG, "getPlayerFactory: 使用播放器" + curPlayerFactory);
        String className = map.get(curPlayerFactory);
        try {
            if (className != null) {
                Object o = Class.forName(className).newInstance();
                if (o instanceof IPlayerFactory) {
                    return (IPlayerFactory<?>) o;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AndroidFactory();
    }

    public void put(String key, String vla) {
        map.put(key, vla);
    }

    public void save(String key) {
        curPlayerFactory = key;
        CacheDiskStaticUtils.put(TAG, this);
    }

    public List<String> getPlayerList() {
        return new ArrayList<>(map.keySet());
    }

    public int getCurIndex() {
        return getPlayerList().indexOf(curPlayerFactory);
    }

}

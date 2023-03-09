package com.github.nwhhades.player.simple;

import android.content.Context;
import android.util.Log;

import com.github.nwhhades.player.inf.IPlayerFactory;

public class AndroidFactory implements IPlayerFactory<AndroidMediaPlayer> {

    private static final String TAG = "AndroidFactory";

    @Override
    public AndroidMediaPlayer getPlayer(Context context) {
        Log.d(TAG, "getPlayer: " + this);
        return new AndroidMediaPlayer(context);
    }

}

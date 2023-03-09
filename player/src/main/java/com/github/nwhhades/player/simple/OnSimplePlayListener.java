package com.github.nwhhades.player.simple;

import com.github.nwhhades.player.inf.IMediaPlayer;

public abstract class OnSimplePlayListener implements IMediaPlayer.OnPlayListener {

    @Override
    public void onPlayState(IMediaPlayer.PlayState state) {

    }

    @Override
    public void onProgress(int progress, long cur_time, long total_time) {

    }

}

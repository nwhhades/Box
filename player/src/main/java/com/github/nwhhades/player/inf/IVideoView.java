package com.github.nwhhades.player.inf;

import com.github.nwhhades.player.simple.OnSimplePlayListener;

public interface IVideoView {


    void setMediaPlayer(IMediaPlayer mediaPlayer);

    IMediaPlayer getMediaPlayer();

    void setControllerView(IControllerView controllerView);

    IControllerView getControllerView();

    void setOnSimplePlayListener(OnSimplePlayListener onSimplePlayListener);

    void setTitle(String title);

    void play(String url, boolean looping);

    void create();

    void resume();

    void pause();

    void destroy();

}

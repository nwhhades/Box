package com.github.nwhhades.player.inf;

import android.view.View;
import android.view.ViewGroup;

public interface IControllerView {

    interface OnViewActionListener {

        void onViewClick(View view);

        void onUserChangeProgress(int progress);

    }

    void addToVideoView(ViewGroup root);

    void delToVideoView(ViewGroup root);

    void initView();

    void destroyView();

    void setOnViewActionListener(OnViewActionListener onViewActionListener);

    void setTitle(String title);

    void setProgress(long time, long time2);

    void setViewText(int view_id, String text);

    void setViewBackgroundResource(int view_id, int res_id);

    void setPlayingUI();

    void setPauseUI();

    void setErrUI();

    void setCompletedUI();

    void setMuteUI(boolean mute);

    void showBufferTip();

    void hideBufferTip();

    void show();

    void hide();

    boolean isShow();

    void toggleShow();

    void autoHide();

    void cancelAutoHide();

}

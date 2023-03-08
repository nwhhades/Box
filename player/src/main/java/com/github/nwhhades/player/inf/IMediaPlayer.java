package com.github.nwhhades.player.inf;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public interface IMediaPlayer {

    enum PlayState {
        STATE_ERROR,
        STATE_IDLE,
        STATE_PREPARING,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED,
        STATE_PLAYBACK_COMPLETED,
        STATE_BUFFERING,
        STATE_BUFFERED,
        STATE_MUTE,
        STATE_UN_MUTE
    }

    enum ScaleType {
        SCALE_DEFAULT,
        SCALE_16_9,
        SCALE_4_3,
        SCALE_MATCH_PARENT,
        SCALE_ORIGINAL,
        SCALE_CENTER_CROP
    }

    interface OnPlayListener {

        void onPlayState(PlayState state);

        void onProgress(int progress, long cur_time, long total_time);

    }

    View getView();

    /**
     * 创建播放器
     *
     * @param root 父容器
     */
    void create_(@NonNull ViewGroup root);

    /**
     * 直接播放地址
     *
     * @param url 地址
     */
    void play_(String url);

    /**
     * 重新播放
     */
    void replay_();

    /**
     * 预加载
     */
    void prepare_();

    /**
     * 开始播放
     */
    void start_();

    /**
     * 继续播放
     */
    void resume_();

    /**
     * 暂停播放
     */
    void pause_();

    /**
     * 停止播放
     */
    void stop_();

    /**
     * 释放播放器
     */
    void release_();

    /**
     * 销毁播放器
     *
     * @param root 父容器
     */
    void destroy_(@NonNull ViewGroup root);

    /**
     * 移动到某个时间戳
     *
     * @param ms 时间戳
     */
    void seekTo_(long ms);

    /**
     * 设置是否循环播放
     *
     * @param looping 是否循环
     */
    void setLooping_(boolean looping);

    /**
     * 是否在播放
     *
     * @return 播放状态
     */
    boolean isPlaying_();

    /**
     * 是否静音
     *
     * @return 静音状态
     */
    boolean isMute_();

    /**
     * 设置静音
     *
     * @param mute 是否静音
     */
    void setMute_(boolean mute);

    /**
     * 返回当前播放时间戳
     *
     * @return 时间戳
     */
    long getCurrentPosition_();

    /**
     * 返回视频总时长
     *
     * @return 总时长
     */
    long getDuration_();

    /**
     * 返回播放进度
     *
     * @return 播放进度
     */
    int getProgress_();

    /**
     * 设置播放速度
     *
     * @param speed 播放速度
     */
    void setPlaySpeed_(float speed);

    /**
     * 获取播放速度
     *
     * @return 播放速度
     */
    float getPlaySpeed_();

    /**
     * 设置画面比例
     *
     * @param scaleType 画面比例
     */
    void setScaleType_(ScaleType scaleType);

    /**
     * 获取画面比例
     *
     * @return 画面比例
     */
    ScaleType getScaleType_();

    /**
     * 设置播放器状态
     *
     * @param playState 播放状态
     */
    void setPlaySate_(PlayState playState);

    /**
     * 返回播放状态
     *
     * @return 播放状态
     */
    PlayState getPlaySate_();

    /**
     * 设置播放监听器
     *
     * @param onPlayListener 监听器
     */
    void setOnPlayListener_(OnPlayListener onPlayListener);

    /**
     * 获取播放监听器
     */
    OnPlayListener getOnPlayListener_();

}

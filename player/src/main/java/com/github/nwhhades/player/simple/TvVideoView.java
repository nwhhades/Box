package com.github.nwhhades.player.simple;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.github.nwhhades.base.view.TvSpinner;
import com.github.nwhhades.player.R;
import com.github.nwhhades.player.config.PlayerConfig;
import com.github.nwhhades.player.inf.IControllerView;
import com.github.nwhhades.player.inf.IMediaPlayer;
import com.github.nwhhades.player.inf.IVideoView;

public class TvVideoView extends FrameLayout implements LifecycleEventObserver, IVideoView, IMediaPlayer.OnPlayListener, IControllerView.OnViewActionListener {

    protected volatile String mUrl;
    protected IMediaPlayer mMediaPlayer = null;
    protected IControllerView mControllerView = null;
    protected OnSimplePlayListener mOnSimplePlayListener = null;

    public TvVideoView(@NonNull Context context) {
        super(context);
    }

    public TvVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TvVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setMediaPlayer(IMediaPlayer mediaPlayer) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnPlayListener_(null);
            mMediaPlayer.destroy_(this);
        }
        mMediaPlayer = mediaPlayer;
        if (mMediaPlayer != null) {
            mMediaPlayer.create_(this);
            mMediaPlayer.setOnPlayListener_(this);
            //切换内核的时候自动继续播放
            if (mUrl != null) {
                play(mUrl, false);
            }
        }
    }

    @Override
    public IMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    @Override
    public void setControllerView(IControllerView controllerView) {
        if (mControllerView != null) {
            mControllerView.setOnViewActionListener(null);
            mControllerView.delToVideoView(this);
        }
        mControllerView = controllerView;
        if (mControllerView != null) {
            mControllerView.addToVideoView(this);
            mControllerView.setOnViewActionListener(this);
        }
    }

    @Override
    public IControllerView getControllerView() {
        return mControllerView;
    }

    @Override
    public void setOnSimplePlayListener(OnSimplePlayListener onSimplePlayListener) {
        mOnSimplePlayListener = onSimplePlayListener;
    }

    @Override
    public void setTitle(String title) {
        if (mControllerView != null) {
            mControllerView.setTitle(title);
        }
    }

    @Override
    public void play(String url, boolean looping) {
        mUrl = url;
        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping_(looping);
            mMediaPlayer.play_(mUrl);
            mMediaPlayer.start_();
            mMediaPlayer.startF5Progress();
        }
    }

    @Override
    public void create() {
        //这里可以读取配置的播放器
        setMediaPlayer(PlayerConfig.getInstance().getPlayerFactory().getPlayer(getContext()));
        setControllerView(new TvControllerView(getContext()));
    }

    @Override
    public void resume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.resume_();
            mMediaPlayer.startF5Progress();
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause_();
            mMediaPlayer.stopF5Progress();
        }
    }

    @Override
    public void destroy() {
        setMediaPlayer(null);
        setControllerView(null);
    }

    @Override
    public void onPlayErr(Exception e) {
        if (mOnSimplePlayListener != null) {
            mOnSimplePlayListener.onPlayErr(e);
        }
    }

    @Override
    public void onPlayCompleted() {
        if (mOnSimplePlayListener != null) {
            mOnSimplePlayListener.onPlayCompleted();
        }
    }

    @Override
    public void onPlayState(IMediaPlayer.PlayState state) {
        if (mOnSimplePlayListener != null) {
            mOnSimplePlayListener.onPlayState(state);
        }
        //播放状态
        if (mControllerView == null) {
            return;
        }
        switch (state) {
            case STATE_ERROR:
                mControllerView.setErrUI();
                break;
            case STATE_BUFFERING:
                mControllerView.showBufferTip();
                break;
            case STATE_BUFFERED:
                mControllerView.hideBufferTip();
                break;
            case STATE_PLAYING:
                mControllerView.setPlayingUI();
                break;
            case STATE_PAUSED:
                mControllerView.setPauseUI();
                break;
            case STATE_PLAYBACK_COMPLETED:
                mControllerView.setCompletedUI();
                break;
            case STATE_MUTE:
                mControllerView.setMuteUI(true);
                break;
            case STATE_UN_MUTE:
                mControllerView.setMuteUI(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgress(int progress, long cur_time, long total_time) {
        if (mOnSimplePlayListener != null) {
            mOnSimplePlayListener.onProgress(progress, cur_time, total_time);
        }
        if (mControllerView != null) {
            mControllerView.setProgress(cur_time, total_time);
        }
    }

    private TvSpinner configSpinner;

    @Override
    public void onViewClick(View view) {
        if (mMediaPlayer == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.btn_1) {
            if (mMediaPlayer.isPlaying_()) {
                mMediaPlayer.pause_();
            } else {
                mMediaPlayer.resume_();
            }
        } else if (id == R.id.btn_2) {
            mMediaPlayer.replay_();
        } else if (id == R.id.btn_3) {
            mMediaPlayer.setMute_(!mMediaPlayer.isMute_());
        } else if (id == R.id.btn_4) {
            if (configSpinner == null) {
                Context context = getContext();
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    configSpinner = new TvSpinner(activity, "选择播放内核", PlayerConfig.getInstance().getPlayerList(), PlayerConfig.getInstance().getCurIndex());
                    configSpinner.setOnSelectItemListener((data, index) -> {
                        PlayerConfig.getInstance().save(data);
                        setMediaPlayer(PlayerConfig.getInstance().getPlayerFactory().getPlayer(context));
                    });
                } else {
                    return;
                }
            }
            configSpinner.show();
        }
    }

    @Override
    public void onUserChangeProgress(int progress) {
        if (mMediaPlayer != null) {
            long total_time = mMediaPlayer.getDuration_();
            float cur_time = progress / 100f * total_time;
            mMediaPlayer.seekTo_((long) cur_time);
        }
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_RESUME:
                resume();
                break;
            case ON_PAUSE:
                pause();
                break;
            case ON_DESTROY:
                destroy();
                break;
            default:
                break;
        }
    }

}

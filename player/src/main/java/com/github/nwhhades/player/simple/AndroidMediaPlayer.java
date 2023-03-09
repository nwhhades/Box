package com.github.nwhhades.player.simple;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.nwhhades.player.inf.IMediaPlayer;

public class AndroidMediaPlayer extends VideoView implements IMediaPlayer {

    private static final String TAG = "AndroidMediaPlayer";

    protected volatile MediaPlayer mMediaPlayer = null;
    protected volatile boolean isLooping = false;
    protected volatile boolean isMute = false;
    protected volatile float mSpeed = 1.0f;
    protected volatile ScaleType mScaleType = ScaleType.SCALE_DEFAULT;
    protected volatile PlayState mPlayState = PlayState.STATE_IDLE;
    protected OnPlayListener mOnPlayListener;
    protected final Handler handler = new Handler(Looper.myLooper());
    protected final Runnable f5ProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mOnPlayListener != null && isPlaying_()) {
                mOnPlayListener.onProgress(getProgress_(), getCurrentPosition_(), getDuration_());
            }
            handler.postDelayed(f5ProgressRunnable, 1000);
        }
    };

    public AndroidMediaPlayer(@NonNull Context context) {
        super(context);
    }

    public AndroidMediaPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AndroidMediaPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //我们重新计算高度
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(false);
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(false);
    }

    @Override
    public void setFocusableInTouchMode(boolean focusableInTouchMode) {
        super.setFocusableInTouchMode(false);
    }

    @Override
    public View getView_() {
        return this;
    }

    @Override
    public void create_(@NonNull ViewGroup root) {
        //添加到root
        root.addView(getView_(), 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //初始化
        setClickable(false);
        setFocusable(false);
        setFocusableInTouchMode(false);
        //添加监听器
        setOnErrorListener((mp, what, extra) -> {
            setErrState(new Exception("err:" + extra));
            return true;
        });
        setOnCompletionListener(mp -> {
            Log.d(TAG, "create_: 播放结束");
            seekTo(0);
            if (isLooping) {
                mp.start();
            } else {
                setPlaySate_(PlayState.STATE_PLAYBACK_COMPLETED);
            }
        });
        setOnPreparedListener(mp -> {
            Log.d(TAG, "create_: 预加载结束");
            mMediaPlayer = mp;
            setPlaySate_(PlayState.STATE_PREPARED);
        });
        setOnInfoListener((mp, what, extra) -> {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    setPlaySate_(PlayState.STATE_PLAYING);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    setPlaySate_(PlayState.STATE_BUFFERING);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    setPlaySate_(PlayState.STATE_BUFFERED);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    @Override
    public void play_(String url) {
        try {
            setVideoURI(Uri.parse(url));
        } catch (Exception e) {
            setErrState(e);
        }
    }

    @Override
    public void replay_() {
        resume();
        start_();
    }

    @Override
    public void prepare_() {
        setPlaySate_(PlayState.STATE_PREPARING);
    }

    @Override
    public void start_() {
        prepare_();
        start();
        setPlaySate_(PlayState.STATE_PLAYING);
    }

    @Override
    public void resume_() {
        if (!isPlaying()) {
            start();
            setPlaySate_(PlayState.STATE_PLAYING);
        }
    }

    @Override
    public void pause_() {
        if (isPlaying()) {
            pause();
            setPlaySate_(PlayState.STATE_PAUSED);
        }
    }

    @Override
    public void stop_() {
        if (isPlaying()) {
            pause();
            setPlaySate_(PlayState.STATE_PAUSED);
        }
    }

    @Override
    public void release_() {
        setOnErrorListener(null);
        setOnPreparedListener(null);
        setOnCompletionListener(null);
        setOnInfoListener(null);
        suspend();
        mOnPlayListener = null;
    }

    @Override
    public void destroy_(@NonNull ViewGroup root) {
        stopF5Progress();
        //移除监听器
        release_();
        //移除view
        root.removeView(getView_());
    }

    @Override
    public void seekTo_(long ms) {
        if (getDuration() > ms) {
            seekTo((int) ms);
        }
    }

    @Override
    public void setLooping_(boolean looping) {
        isLooping = looping;
    }

    @Override
    public boolean isPlaying_() {
        return isPlaying();
    }

    @Override
    public boolean isMute_() {
        return isMute;
    }

    @Override
    public void setMute_(boolean mute) {
        isMute = mute;
        if (mMediaPlayer != null) {
            if (isMute) {
                mMediaPlayer.setVolume(0f, 0f);
                setPlaySate_(PlayState.STATE_MUTE);
            } else {
                mMediaPlayer.setVolume(1f, 1f);
                setPlaySate_(PlayState.STATE_UN_MUTE);
            }
        }
    }

    @Override
    public long getCurrentPosition_() {
        return getCurrentPosition();
    }

    @Override
    public long getDuration_() {
        return getDuration();
    }

    @Override
    public int getProgress_() {
        if (isPlaying()) {
            float p = getCurrentPosition() * 100f / getDuration();
            if (0 <= p && p <= 100) {
                return (int) p;
            }
            return -1;
        }
        return 0;
    }

    @Override
    public void setPlaySpeed_(float speed) {
        mSpeed = speed;
    }

    @Override
    public float getPlaySpeed_() {
        return mSpeed;
    }

    @Override
    public void setScaleType_(ScaleType scaleType) {
        mScaleType = scaleType;
    }

    @Override
    public ScaleType getScaleType_() {
        return mScaleType;
    }

    @Override
    public void setPlaySate_(PlayState playState) {
        mPlayState = playState;
        if (mOnPlayListener != null) {
            mOnPlayListener.onPlayState(mPlayState);
            if (playState == PlayState.STATE_ERROR) {
                mOnPlayListener.onPlayErr(null);
            } else if (playState == PlayState.STATE_PLAYBACK_COMPLETED) {
                mOnPlayListener.onPlayCompleted();
            }
        }
    }

    @Override
    public PlayState getPlaySate_() {
        return mPlayState;
    }

    @Override
    public void setErrState(Exception e) {
        if (mOnPlayListener != null) {
            mOnPlayListener.onPlayErr(e);
            mOnPlayListener.onPlayState(PlayState.STATE_ERROR);
        }
    }

    @Override
    public void setOnPlayListener_(OnPlayListener onPlayListener) {
        mOnPlayListener = onPlayListener;
    }

    @Override
    public OnPlayListener getOnPlayListener_() {
        return mOnPlayListener;
    }

    @Override
    public void startF5Progress() {
        handler.post(f5ProgressRunnable);
    }

    @Override
    public void stopF5Progress() {
        handler.removeCallbacks(f5ProgressRunnable);
    }

}

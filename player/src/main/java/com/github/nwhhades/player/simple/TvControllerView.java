package com.github.nwhhades.player.simple;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;

import com.github.nwhhades.base.view.TvSeekBar;
import com.github.nwhhades.player.R;
import com.github.nwhhades.player.base.BaseControllerView;
import com.github.nwhhades.player.databinding.LayoutTvVideoControllerBinding;
import com.hjq.toast.Toaster;

public class TvControllerView extends BaseControllerView implements View.OnFocusChangeListener, View.OnClickListener, View.OnKeyListener {

    private static final String TAG = "TvControllerView";

    private View vMainBg;
    private ProgressBar progressBar;
    private LayoutTvVideoControllerBinding binding;

    public TvControllerView(@NonNull Context context) {
        super(context);
    }

    public TvControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TvControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        createMainBg();
        createProgress();
        //初始化控制
        binding = LayoutTvVideoControllerBinding.inflate(LayoutInflater.from(getContext()), this, true);
        binding.tpProgress.setOnTvSeekBarListener(new TvSeekBar.OnTvSeekBarListener() {
            @Override
            public void onProgressChanged(TvSeekBar tvSeekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartPreview(TvSeekBar tvSeekBar, int progress) {
                cancelAutoHide();
            }

            @Override
            public void onStopPreview(TvSeekBar tvSeekBar, int progress) {
                autoHide();
                Log.d(TAG, "onStopPreview: 切换进度到" + progress);
                if (progress == 100) {
                    progress = 99;
                }
                if (mOnViewActionListener != null) {
                    mOnViewActionListener.onUserChangeProgress(progress);
                }
            }

            @Override
            public void onBack() {
                hide();
            }
        });
        binding.btn1.setOnFocusChangeListener(this);
        binding.btn2.setOnFocusChangeListener(this);
        binding.btn3.setOnFocusChangeListener(this);
        binding.btn4.setOnFocusChangeListener(this);
        binding.btn1.setOnClickListener(this);
        binding.btn2.setOnClickListener(this);
        binding.btn3.setOnClickListener(this);
        binding.btn4.setOnClickListener(this);
        binding.btn1.setOnKeyListener(this);
        binding.btn2.setOnKeyListener(this);
        binding.btn3.setOnKeyListener(this);
        binding.btn4.setOnKeyListener(this);
        hide();
    }

    @Override
    public void destroyView() {
        destroyMainBg();
        destroyProgress();
        binding.tpProgress.setOnTvSeekBarListener(null);
        binding.btn1.setOnFocusChangeListener(null);
        binding.btn2.setOnFocusChangeListener(null);
        binding.btn3.setOnFocusChangeListener(null);
        binding.btn4.setOnFocusChangeListener(null);
    }

    @Override
    public void setTitle(String title) {
        binding.tvTitle.setText(title);
    }

    @Override
    public void setProgress(long time, long time2) {
        if (binding != null) {
            binding.tpProgress.setProgress(time, time2);
        }
    }

    @Override
    public void setViewText(int view_id, String text) {

    }

    @Override
    public void setViewBackgroundResource(int view_id, int res_id) {

    }

    @Override
    public void setPlayingUI() {
        binding.btn1.setBackgroundResource(R.drawable.ic_pause);
    }

    @Override
    public void setPauseUI() {
        binding.btn1.setBackgroundResource(R.drawable.ic_play);
    }

    @Override
    public void setErrUI() {
        Toaster.show("播放出错");
    }

    @Override
    public void setCompletedUI() {
        Toaster.show("播放结束");
        binding.btn1.setBackgroundResource(R.drawable.ic_play);
        binding.tpProgress.setProgress(0);
    }

    @Override
    public void setMuteUI(boolean mute) {
        if (mute) {
            binding.btn3.setBackgroundResource(R.drawable.ic_voice);
        } else {
            binding.btn3.setBackgroundResource(R.drawable.ic_mute);
        }
    }

    @Override
    public void showBufferTip() {
        if (progressBar != null) {
            progressBar.setVisibility(VISIBLE);
        }
    }

    @Override
    public void hideBufferTip() {
        if (progressBar != null) {
            progressBar.setVisibility(INVISIBLE);
        }
    }


    @Override
    public void show() {
        super.show();
        binding.getRoot().setVisibility(VISIBLE);
        binding.btn1.requestFocus();
    }

    @Override
    public void hide() {
        super.hide();
        binding.getRoot().setVisibility(INVISIBLE);
        vMainBg.requestFocus();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            autoHide();
        }
    }

    @Override
    public void onClick(View v) {
        autoHide();
        if (mOnViewActionListener != null) {
            mOnViewActionListener.onViewClick(v);
        }
    }

    private void createMainBg() {
        vMainBg = new View(getContext());
        vMainBg.setId(R.id.v_main_bg);
        vMainBg.setFocusable(true);
        vMainBg.setClickable(true);
        vMainBg.setFocusableInTouchMode(true);
        vMainBg.setNextFocusUpId(getNextFocusUpId());
        vMainBg.setNextFocusDownId(getNextFocusDownId());
        vMainBg.setNextFocusLeftId(getNextFocusLeftId());
        vMainBg.setNextFocusRightId(getNextFocusRightId());
        vMainBg.setOnClickListener(v -> toggleShow());
        vMainBg.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                    case KeyEvent.KEYCODE_DPAD_UP:
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        toggleShow();
                        return true;
                    default:
                        return false;
                }
            }
            return false;
        });
        addView(vMainBg, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void destroyMainBg() {
        if (vMainBg != null) {
            vMainBg.setOnClickListener(null);
            removeView(vMainBg);
        }
    }

    private void createProgress() {
        progressBar = new ProgressBar(new ContextThemeWrapper(getContext(), R.style.ProgressBar), null);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        progressBar.setId(R.id.pb_loading);
        progressBar.setVisibility(INVISIBLE);
        addView(progressBar, 0, layoutParams);
    }

    private void destroyProgress() {
        if (progressBar != null) {
            removeView(progressBar);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hide();
            return true;
        }
        return false;
    }

}

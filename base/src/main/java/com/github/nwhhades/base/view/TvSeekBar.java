package com.github.nwhhades.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

public class TvSeekBar extends AppCompatSeekBar implements SeekBar.OnSeekBarChangeListener {

    public interface OnTvSeekBarListener {

        void onProgressChanged(TvSeekBar tvSeekBar, int progress, boolean fromUser);

        void onStartPreview(TvSeekBar tvSeekBar, int progress);

        void onStopPreview(TvSeekBar tvSeekBar, int progress);

    }

    private OnTvSeekBarListener onTvSeekBarListener;

    public TvSeekBar(@NonNull Context context) {
        super(context);
        init();
    }

    public TvSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TvSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        super.setOnSeekBarChangeListener(this);
    }

    public void setOnTvSeekBarListener(OnTvSeekBarListener onTvSeekBarListener) {
        this.onTvSeekBarListener = onTvSeekBarListener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (onTvSeekBarListener != null) {
            onTvSeekBarListener.onProgressChanged(this, progress, fromUser);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (onTvSeekBarListener != null) {
            onTvSeekBarListener.onStartPreview(this, seekBar.getProgress());
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (onTvSeekBarListener != null) {
            onTvSeekBarListener.onStopPreview(this, seekBar.getProgress());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT | keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            onStartTrackingTouch(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT | keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            onStopTrackingTouch(this);
        }
        return super.onKeyUp(keyCode, event);
    }

}

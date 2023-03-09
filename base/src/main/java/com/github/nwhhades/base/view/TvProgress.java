package com.github.nwhhades.base.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.nwhhades.base.R;
import com.github.nwhhades.base.databinding.LayoutTvProgressControllerBinding;

import java.util.Locale;

public class TvProgress extends ConstraintLayout implements TvSeekBar.OnTvSeekBarListener, View.OnFocusChangeListener {

    private volatile long total_time;
    private LayoutTvProgressControllerBinding binding;
    private TvSeekBar.OnTvSeekBarListener onTvSeekBarListener;

    public TvProgress(@NonNull Context context) {
        super(context);
        init();
    }

    public TvProgress(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TvProgress(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        binding = LayoutTvProgressControllerBinding.inflate(LayoutInflater.from(getContext()), this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
        setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.tsbProgress.requestFocus();
            }
        });
        binding.vTopFocus.setOnFocusChangeListener(this);
        binding.vBottomFocus.setOnFocusChangeListener(this);
        binding.vLeftFocus.setOnFocusChangeListener(this);
        binding.vRightFocus.setOnFocusChangeListener(this);
        binding.tsbProgress.setOnTvSeekBarListener(this);
    }

    private boolean isStartPre = false;

    @Override
    public void onProgressChanged(TvSeekBar tvSeekBar, int progress, boolean fromUser) {
        if (total_time != 0 && fromUser) {
            float local_cur_time = progress / 100f * total_time;
            String text = stringForTime((long) local_cur_time) + " / " + stringForTime(total_time);
            binding.tvProgress.setText(text);
        }
        if (onTvSeekBarListener != null) {
            onTvSeekBarListener.onProgressChanged(binding.tsbProgress, progress, fromUser);
        }
    }

    @Override
    public void onStartPreview(TvSeekBar tvSeekBar, int progress) {
        isStartPre = true;
        if (onTvSeekBarListener != null) {
            onTvSeekBarListener.onStartPreview(binding.tsbProgress, progress);
        }
    }

    @Override
    public void onStopPreview(TvSeekBar tvSeekBar, int progress) {
        if (!isStartPre) {
            return;
        }
        isStartPre = false;
        if (onTvSeekBarListener != null) {
            onTvSeekBarListener.onStopPreview(binding.tsbProgress, progress);
        }
    }

    @Override
    public void onBack() {
        if (onTvSeekBarListener != null) {
            onTvSeekBarListener.onBack();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (isStartPre) {
                onStopPreview(binding.tsbProgress, binding.tsbProgress.getProgress());
            }
            actionFocus(v.getId());
        }
    }

    private void actionFocus(int view_id) {
        int next_view_id = 0;
        if (view_id == R.id.v_top_focus) {
            next_view_id = getNextFocusUpId();
        } else if (view_id == R.id.v_bottom_focus) {
            next_view_id = getNextFocusDownId();
        } else if (view_id == R.id.v_left_focus) {
            next_view_id = getNextFocusLeftId();
        } else if (view_id == R.id.v_right_focus) {
            next_view_id = getNextFocusRightId();
        }

        if (getContext() instanceof Activity && next_view_id != 0) {
            Activity activity = (Activity) getContext();
            View view = activity.findViewById(next_view_id);
            if (view != null) {
                view.requestFocus();
            }
        }
    }

    public void setProgress(long time1, long time2) {
        this.total_time = time2;
        if (time1 < 0 || time2 <= 0) {
            return;
        }
        if (!isStartPre) {
            float progress = time1 * 100f / time2;
            binding.tsbProgress.setProgress((int) progress);
            String text = stringForTime(time1) + " / " + stringForTime(time2);
            binding.tvProgress.setText(text);
        }
    }

    public void setProgress(int progress) {
        binding.tsbProgress.setProgress(progress);
    }

    public void setOnTvSeekBarListener(TvSeekBar.OnTvSeekBarListener onTvSeekBarListener) {
        this.onTvSeekBarListener = onTvSeekBarListener;
    }

    public static String stringForTime(long timeMs) {
        long totalSeconds = timeMs / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }

}

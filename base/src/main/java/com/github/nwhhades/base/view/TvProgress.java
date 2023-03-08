package com.github.nwhhades.base.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.nwhhades.base.R;

import java.util.Locale;

public class TvProgress extends ConstraintLayout implements TvSeekBar.OnTvSeekBarListener, View.OnFocusChangeListener {

    private volatile long total_time;
    private TvSeekBar tsb_progress;
    private TextView tv_progress;
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
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
        setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (tsb_progress != null) {
                    tsb_progress.requestFocus();
                }
            }
        });
        View root = LayoutInflater.from(getContext()).inflate(R.layout.layout_tv_progress_controller, this, true);
        if (root != null) {
            View v_top_focus = root.findViewById(R.id.v_top_focus);
            View v_bottom_focus = root.findViewById(R.id.v_bottom_focus);
            View v_left_focus = root.findViewById(R.id.v_left_focus);
            View v_right_focus = root.findViewById(R.id.v_right_focus);
            tsb_progress = root.findViewById(R.id.tsb_progress);
            tv_progress = root.findViewById(R.id.tv_progress);
            //添加监听器
            v_top_focus.setOnFocusChangeListener(this);
            v_bottom_focus.setOnFocusChangeListener(this);
            v_left_focus.setOnFocusChangeListener(this);
            v_right_focus.setOnFocusChangeListener(this);
            tsb_progress.setOnTvSeekBarListener(this);
        }
    }

    private boolean isStartPre = false;

    @Override
    public void onProgressChanged(TvSeekBar tvSeekBar, int progress, boolean fromUser) {
        if (total_time != 0 && tv_progress != null && fromUser) {
            float local_cur_time = progress / 100f * total_time;
            String text = stringForTime((long) local_cur_time) + " / " + stringForTime(total_time);
            tv_progress.setText(text);
        }
        if (onTvSeekBarListener != null) {
            onTvSeekBarListener.onProgressChanged(tsb_progress, progress, fromUser);
        }
    }

    @Override
    public void onStartPreview(TvSeekBar tvSeekBar, int progress) {
        if (isStartPre) {
            return;
        }
        isStartPre = true;
        if (onTvSeekBarListener != null) {
            onTvSeekBarListener.onStartPreview(tsb_progress, progress);
        }
    }

    @Override
    public void onStopPreview(TvSeekBar tvSeekBar, int progress) {
        if (!isStartPre) {
            return;
        }
        isStartPre = false;
        if (onTvSeekBarListener != null) {
            onTvSeekBarListener.onStopPreview(tsb_progress, progress);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (isStartPre) {
                onStopPreview(tsb_progress, tsb_progress.getProgress());
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
        if (tsb_progress != null && tv_progress != null && !isStartPre) {
            float progress = time1 * 100f / time2;
            tsb_progress.setProgress((int) progress);
            String text = stringForTime(time1) + " / " + stringForTime(time2);
            tv_progress.setText(text);
        }
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

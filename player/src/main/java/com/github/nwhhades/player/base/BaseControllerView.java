package com.github.nwhhades.player.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.nwhhades.player.inf.IControllerView;

public abstract class BaseControllerView extends FrameLayout implements IControllerView {

    protected boolean isShow = true;
    protected OnViewActionListener mOnViewActionListener;
    protected final long autoHideTime = 5000;
    protected final Handler handler = new Handler(Looper.myLooper());
    protected final Runnable hideRunnable = this::hide;

    public BaseControllerView(@NonNull Context context) {
        super(context);
    }

    public BaseControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addToVideoView(ViewGroup root) {
        //获取父的焦点转移
        setNextFocusUpId(root.getNextFocusUpId());
        setNextFocusDownId(root.getNextFocusDownId());
        setNextFocusRightId(root.getNextFocusRightId());
        setNextFocusLeftId(root.getNextFocusLeftId());
        root.addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView();
    }

    @Override
    public void delToVideoView(ViewGroup root) {
        destroyView();
        root.removeView(this);
    }

    @Override
    public void setOnViewActionListener(OnViewActionListener onViewActionListener) {
        mOnViewActionListener = onViewActionListener;
    }

    @Override
    public void show() {
        if (isShow) {
            return;
        }
        isShow = true;
        autoHide();
    }

    @Override
    public void hide() {
        if (!isShow) {
            return;
        }
        isShow = false;
        cancelAutoHide();
    }

    @Override
    public boolean isShow() {
        return isShow;
    }

    @Override
    public void toggleShow() {
        if (isShow()) {
            hide();
        } else {
            show();
        }
    }

    @Override
    public void autoHide() {
        cancelAutoHide();
        handler.postDelayed(hideRunnable, autoHideTime);
    }

    @Override
    public void cancelAutoHide() {
        handler.removeCallbacks(hideRunnable);
    }

}

package com.github.nwhhades.player.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.nwhhades.player.inf.IControllerView;

public class BaseControllerView extends FrameLayout implements IControllerView {

    public BaseControllerView(@NonNull Context context) {
        super(context);
    }

    public BaseControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}

package com.github.nwhhades.player.simple;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AndroidMediaPlayer extends VideoView {

    public AndroidMediaPlayer(@NonNull Context context) {
        super(context);
    }

    public AndroidMediaPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AndroidMediaPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}

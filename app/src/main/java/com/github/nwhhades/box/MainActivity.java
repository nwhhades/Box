package com.github.nwhhades.box;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.github.nwhhades.base.activity.BaseActivity;
import com.github.nwhhades.base.view.TvSeekBar;
import com.github.nwhhades.box.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected boolean enableBg() {
        return true;
    }

    @Override
    protected boolean enableLoadingView() {
        return true;
    }

    @Override
    protected void preInit() {
        super.preInit();
        setBg("http://www.bing.com/th?id=OHR.YuanyangChina_ZH-CN7360249295_1920x1080.jpg&rf=LaDigue_1920x1080.jpg");
    }

    private int i;
    private volatile long j = 1;
    private long total = 1000 * 60 * 6;

    @Override
    protected void init() {
        binding.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showLoadingView("显示的内容文本！");
                binding.tv2.requestFocus();
            }
        });

        binding.tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingView(null);
            }
        });

        binding.tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLoadingView();
            }
        });

        binding.tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });


        binding.tpProgress.setOnTvSeekBarListener(new TvSeekBar.OnTvSeekBarListener() {
            @Override
            public void onProgressChanged(TvSeekBar tvSeekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartPreview(TvSeekBar tvSeekBar, int progress) {

            }

            @Override
            public void onStopPreview(TvSeekBar tvSeekBar, int progress) {
                Log.d(TAG, "onStopPreview: 切换进度到" + progress);
                j = (long) ((progress / 100f) * total);
            }
        });

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long ss = j + 1000;
                j = ss;
                binding.tpProgress.setProgress(ss, total);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

    }

}
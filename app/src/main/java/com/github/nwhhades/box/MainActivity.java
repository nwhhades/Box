package com.github.nwhhades.box;

import android.util.Log;

import com.github.nwhhades.base.activity.BaseActivity;
import com.github.nwhhades.box.databinding.ActivityMainBinding;
import com.github.nwhhades.player.inf.IMediaPlayer;
import com.github.nwhhades.player.simple.OnSimplePlayListener;

public class MainActivity extends BaseActivity<ActivityMainBinding> {


    private static final String TAG = "MainActivity";

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

    @Override
    protected void init() {
        initPlayer();
    }

    private void initPlayer() {
        getLifecycle().addObserver(binding.tvv);
        binding.tvv.create();
        binding.tvv.setOnSimplePlayListener(new OnSimplePlayListener() {

            @Override
            public void onPlayState(IMediaPlayer.PlayState state) {
                super.onPlayState(state);
                Log.d(TAG, "onPlayState: " + state);
            }

            @Override
            public void onPlayErr(Exception e) {

            }

            @Override
            public void onPlayCompleted() {

            }
        });

        binding.tvv.setTitle("哈哈哈哈哈哈");
        binding.tvv.play("http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4", false);
        //binding.tvv.play("http://hwrr.jx.chinamobile.com:8080/PLTV/88888888/224/3221225619/index.m3u8", false);
        binding.tvv.requestFocus();
    }

}
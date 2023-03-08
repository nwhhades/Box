package com.github.nwhhades.base.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.github.nwhhades.base.R;
import com.hjq.toast.Toaster;

public abstract class BaseActivity<V extends ViewBinding> extends AppCompatActivity {

    protected static final String TAG = "BaseActivity";

    protected static final String SP_KEY_ACTIVITY_BG = "SP_KEY_ACTIVITY_BG";

    protected V binding;

    protected abstract V getBinding();

    protected abstract boolean enableBg();

    protected void setBg(String bg_url) {
        SPUtils.getInstance(TAG).put(SP_KEY_ACTIVITY_BG, bg_url);
    }

    protected void addBg() {
        String bg_url = SPUtils.getInstance(TAG).getString(SP_KEY_ACTIVITY_BG, "");
        if (StringUtils.isEmpty(bg_url)) {
            LogUtils.d(this + " Activity 背景为空");
            return;
        }
        if (binding.getRoot() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) binding.getRoot();
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //初始化背景
            AppCompatImageView imageView = new AppCompatImageView(this);
            imageView.setId(R.id.iv_activity_bg);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //添加到根视图
            viewGroup.addView(imageView, 0, layoutParams);
            //加载背景
            Glide.with(imageView).load(bg_url).into(imageView);
        }
    }

    protected FrameLayout fl_loading;
    protected View oldFocusView;

    protected abstract boolean enableLoadingView();

    protected void addLoadingView() {
        if (binding.getRoot() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) binding.getRoot();
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //初始化View
            fl_loading = new FrameLayout(this);
            //添加到根视图
            viewGroup.addView(fl_loading, layoutParams);
            fl_loading.setId(R.id.fl_loading);
            fl_loading.setBackgroundResource(R.color.tr_black_50);
            fl_loading.setVisibility(View.INVISIBLE);
            fl_loading.setNextFocusLeftId(R.id.fl_loading);
            fl_loading.setNextFocusRightId(R.id.fl_loading);
            fl_loading.setNextFocusUpId(R.id.fl_loading);
            fl_loading.setNextFocusDownId(R.id.fl_loading);
            fl_loading.setClickable(true);
            fl_loading.setFocusable(true);
            fl_loading.setFocusableInTouchMode(true);
            //添加子View
            ProgressBar child_view = new ProgressBar(new ContextThemeWrapper(this, R.style.LoadingProgressBar), null);
            FrameLayout.LayoutParams child_view_layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            child_view_layoutParams.gravity = Gravity.CENTER;
            child_view.setId(R.id.pb_loading);
            child_view.setPadding(30, 30, 30, 30);
            child_view.setBackgroundResource(R.drawable.bg_gray_card);
            fl_loading.addView(child_view, child_view_layoutParams);
            fl_loading.setOnClickListener(v -> {
                Log.d(TAG, "onClick: 用户点击了LoadingView");
                showClickLoadingViewMsg();
            });
            fl_loading.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "onClick: 用户按键了LoadingView");
                    showClickLoadingViewMsg();
                }
                return true;
            });
        }
    }

    protected void showLoadingView(String msg) {
        oldFocusView = getCurrentFocus();
        if (fl_loading != null) {
            fl_loading.setTag(msg);
            fl_loading.bringToFront();
            fl_loading.setVisibility(View.VISIBLE);
            fl_loading.requestFocus();
        }
    }

    protected void hideLoadingView() {
        if (fl_loading != null) {
            fl_loading.setVisibility(View.INVISIBLE);
        }
        if (oldFocusView != null) {
            oldFocusView.requestFocus();
        }
    }

    protected void showClickLoadingViewMsg() {
        if (fl_loading != null) {
            String msg = getString(R.string.def_loading_view_show_msg);
            Object o = fl_loading.getTag();
            if (o instanceof String) {
                msg = o.toString();
            }
            Toaster.show(msg);
        }
    }

    protected void preInit() {
        Log.d(TAG, "preInit: " + this);
    }

    protected abstract void init();

    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 1920);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        preInit();
        Log.d(TAG, "onCreate: " + this);
        super.onCreate(savedInstanceState);
        binding = getBinding();
        //判断是否添加背景
        if (enableBg()) {
            addBg();
        }
        //判断是否添加LoadingView
        if (enableLoadingView()) {
            addLoadingView();
        }
        setContentView(binding.getRoot());
        init();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: " + this);
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: " + this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: " + this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: " + this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: " + this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: " + this);
        super.onBackPressed();
    }

}

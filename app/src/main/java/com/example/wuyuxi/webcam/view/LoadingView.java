package com.example.wuyuxi.webcam.view;


import android.content.Context;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wuyuxi.webcam.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LoadingView extends RelativeLayout {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_GONE, STATE_LOADING, STATE_FAILED, STATE_EMPTY})
    public @interface State {
    }

    public static final int STATE_GONE = 0;

    public static final int STATE_LOADING = 1;

    public static final int STATE_FAILED = 2;

    public static final int STATE_EMPTY = 3;


    View mHintBlock;
    TextView mHint;

    TextView mRetryButton;

    View mLoadingBlock;
    ImageView mLoadingIcon;

    @State
    private int mState = STATE_GONE;
    private OnRetryListener mOnRetryListener;

    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_loading, this);
        setBackgroundResource(R.color.background_light);
        mHintBlock = findViewById(R.id.hint_block);
        mHint = (TextView) findViewById(R.id.hint);
        mLoadingBlock = findViewById(R.id.loading_block);
        mLoadingIcon = (ImageView) findViewById(R.id.icon_loading);
        mRetryButton = (TextView) findViewById(R.id.retry_button);

        initState();
        mRetryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRetryListener != null) {
                    mOnRetryListener.onRetry();
                }
            }
        });
    }

    private void initState() {
        initState(null);
    }

    private void initState(String msg) {
        switch (mState) {
            case STATE_GONE:
                setVisibility(GONE);
                break;
            case STATE_LOADING:
                mLoadingBlock.setVisibility(VISIBLE);
                mLoadingIcon.setAnimation(AnimationUtils
                        .loadAnimation(getContext(), R.anim.rotate_load));
                mHintBlock.setVisibility(GONE);
                mRetryButton.setVisibility(GONE);
                setVisibility(VISIBLE);
                break;
            case STATE_FAILED:
                mLoadingBlock.setVisibility(GONE);
                mHintBlock.setVisibility(VISIBLE);
                mHint.setText(TextUtils.isEmpty(msg) ? "加载失败，请检查网络设置" : msg);
                mRetryButton.setVisibility(VISIBLE);
                setVisibility(VISIBLE);
                break;
            case STATE_EMPTY:
                mLoadingBlock.setVisibility(GONE);
                mHintBlock.setVisibility(VISIBLE);
                mHint.setText("暂时什么也没有");
                mRetryButton.setVisibility(VISIBLE);
                setVisibility(VISIBLE);
                break;

        }
    }

    public void setState(@State int state) {
        mState = state;
        initState(null);
    }

    public void setFailed(String msg) {
        mState = STATE_FAILED;
        initState(msg);
    }


    public interface OnRetryListener {
        void onRetry();
    }

    public void setOnRetryListener(OnRetryListener listener) {
        mOnRetryListener = listener;
    }


}

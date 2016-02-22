package com.example.wuyuxi.webcam.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wuyuxi.webcam.R;
import com.example.wuyuxi.webcam.core.BaseActivity;
import com.example.wuyuxi.webcam.util.Logger;
import com.example.wuyuxi.webcam.view.LoadingView;

import java.io.IOException;

import io.vov.vitamio.MediaPlayer;

/**
 * @Annotation //直播页面
 */
public class DirectVideoActivity extends BaseActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback {
    private int mVideoWidth;
    private int mVideoHeight;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    MediaPlayer mMediaPlayer;
    private Button back;
    private String url;

    LoadingView mLoading;

    private static final int CLIENT_CLOSE_ERROR = -5;
    private static final int DEVICE_ERROR = -1094995529;

    public static void launch(Activity activity, String url) {
        Intent intent = new Intent(activity, DirectVideoActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.activity_direct_video);
        mLoading = (LoadingView) findViewById(R.id.loading);
        mLoading.setState(LoadingView.STATE_LOADING);


        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        url = getIntent().getStringExtra("url");
        Logger.e(url);
        mSurfaceView = (SurfaceView) findViewById(R.id.video);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setFormat(PixelFormat.RGBA_8888);


    }

    private void playVideo(String url) {

        doCleanUp();
        try {
            mMediaPlayer = new MediaPlayer(this);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setDisplay(mHolder);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (what == 1) {
                        if (extra == CLIENT_CLOSE_ERROR) {
                            Toast.makeText(DirectVideoActivity.this, "请检查客户端是否开启", Toast.LENGTH_LONG).show();
                        } else if (extra == DEVICE_ERROR) {
                            Toast.makeText(DirectVideoActivity.this, "请检查客户端服务器ip或设备号有没有填错", Toast.LENGTH_LONG).show();
                        }
                    }
                    return true;
                }
            });
        } catch (IOException e) {
            Logger.e(e);
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideo() {
        mHolder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
        Logger.e("直播开始");
    }

    private void releasePlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        doCleanUp();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
        doCleanUp();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Logger.e("surfaceDestroyed created");

        playVideo(url);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Logger.e("surfaceChanged called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Logger.e("surfaceDestroyed called");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Logger.e("onBufferingUpdate");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Logger.e("onCompletion");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Logger.e("onPrepared called");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            mLoading.setState(LoadingView.STATE_GONE);
            startVideo();
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Logger.i("onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Logger.e("invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideo();
        }
    }
}

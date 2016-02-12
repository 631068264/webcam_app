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
import com.example.wuyuxi.webcam.util.Logging;

import java.io.IOException;

import io.vov.vitamio.MediaPlayer;

/**
 * @Annotation //直播页面
 */
public class DirectVideoActivity extends Activity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback {
    private int mVideoWidth;
    private int mVideoHeight;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    MediaPlayer mMediaPlayer;
    private Button back;
    private String url;


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


        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        url = getIntent().getStringExtra("url");
        Logging.e(url);
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
                        if (extra == -5) {
                            Toast.makeText(DirectVideoActivity.this, "请检查客户端是否开启", Toast.LENGTH_LONG).show();
                        } else if (extra == -1094995529) {
                            Toast.makeText(DirectVideoActivity.this, "请检查客户端服务器ip或设备号有没有填错", Toast.LENGTH_LONG).show();
                        }
                    }
                    return true;
                }
            });
        } catch (IOException e) {
            Logging.e("error" + e.getMessage());
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideo() {
        Logging.d("startVideoPlayback");
        mHolder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
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
        Logging.d("surfaceDestroyed created");

        playVideo(url);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Logging.d("surfaceChanged called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Logging.d("surfaceChanged called");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Logging.d("onPrepared called");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideo();
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Logging.i("onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Logging.e("invalid video width(" + width + ") or height(" + height + ")");
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

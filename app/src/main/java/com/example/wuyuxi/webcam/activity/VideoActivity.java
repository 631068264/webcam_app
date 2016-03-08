package com.example.wuyuxi.webcam.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

/**
 * @Annotation //视频播放
 */
public class VideoActivity extends BaseActivity {
    private SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    MediaPlayer mMediaPlayer;
    private static final String APP_ROOT = "/webcam";
    LoadingView mLoading;
    Button mBack;
    Button mPlay;
    Button mPause;
    Button mReplay;

    private String url;


    public static void launch(Activity activity, String url) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        url = getIntent().getStringExtra("url").replace(APP_ROOT, "");
        Logger.e(url);
        mLoading = (LoadingView) findViewById(R.id.loading);
        mLoading.setState(LoadingView.STATE_LOADING);

        mSurfaceView = (SurfaceView) findViewById(R.id.video);

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDisplay(mHolder);
                play();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        mHolder.setFixedSize(480, 320);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mPlay = (Button) findViewById(R.id.btn_play);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();
                mPlay.setEnabled(false);
            }
        });

        mPause = (Button) findViewById(R.id.btn_pause);
        mPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });

        mReplay = (Button) findViewById(R.id.btn_replay);
        mReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replay();
            }
        });

        mBack = (Button) findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e("video_close");
                release();
                finish();
            }
        });
    }

    private void replay() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(0);
        } else {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
            }
        }
        mPause.setEnabled(true);
        mPause.setText("停止");
        mPlay.setEnabled(false);
    }

    private void pause() {
        if (mPause.getText().toString().trim().equals("继续")) {
            mPause.setText("停止");
            mMediaPlayer.start();
        } else if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mPause.setText("继续");
            mMediaPlayer.pause();
        } else {
            mPause.setEnabled(false);
            mPlay.setEnabled(true);
        }
    }

//    private Handler handler = new Handler();
//    private Runnable checkPlaying = new Runnable() {
//        @Override
//        public void run() {
//            if (!mMediaPlayer.isPlaying()) {
//                mPause.setEnabled(false);
//                mPlay.setEnabled(true);
//            }
//            handler.postDelayed(this, SYNS_HANDLER);
//        }
//    };


    private void release() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            //handler.removeCallbacks(checkPlaying);
            mMediaPlayer = null;
        }
    }

    private void play() {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
            //视频加载
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                    mPlay.setEnabled(false);
                    mLoading.setState(LoadingView.STATE_GONE);
                    //handler.postDelayed(checkPlaying, SYNS_HANDLER);
                }
            });

            //视频结束
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mPause.setEnabled(false);
                    mPlay.setEnabled(true);
                }
            });
        } catch (IOException e) {
            Toast.makeText(VideoActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
        //handler.removeCallbacks(checkPlaying);
    }
}

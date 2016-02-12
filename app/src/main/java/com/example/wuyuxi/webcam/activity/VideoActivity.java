package com.example.wuyuxi.webcam.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wuyuxi.webcam.R;
import com.example.wuyuxi.webcam.util.Logging;

import java.io.IOException;

/**
 * @Annotation //视频播放
 */
public class VideoActivity extends Activity {
    private SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    MediaPlayer mMediaPlayer;
    private Button back;
    private String url;
    private Button btn_play;
    private Button btn_pause;
    private Button btn_replay;
    public static final int SYNS_HANDLER = 1 * 1000;

    public static void launch(Activity activity, String url) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        url = getIntent().getStringExtra("url");
        Logging.d(url);
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

        btn_play = (Button) findViewById(R.id.btn_play);
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();
                btn_play.setEnabled(false);
            }
        });

        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });

        btn_replay = (Button) findViewById(R.id.btn_replay);
        btn_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replay();
            }
        });

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logging.d("video_close");
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
    }

    private void pause() {
        if (btn_pause.getText().toString().trim().equals("继续")) {
            btn_pause.setText("停止");
            mMediaPlayer.start();
        } else if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            btn_pause.setText("继续");
            mMediaPlayer.pause();
        } else {
            btn_pause.setEnabled(false);
            btn_play.setEnabled(true);
        }
    }

    private Handler handler = new Handler();
    private Runnable checkPlaying = new Runnable() {
        @Override
        public void run() {
            if (!mMediaPlayer.isPlaying()) {
                btn_pause.setEnabled(false);
                btn_play.setEnabled(true);
            }
            handler.postDelayed(this, SYNS_HANDLER);
        }
    };


    private void release() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void play() {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                    btn_play.setEnabled(false);
                    handler.postDelayed(checkPlaying, SYNS_HANDLER);
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
        handler.removeCallbacks(checkPlaying);
    }
}

package com.example.wuyuxi.webcam;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by wuyuxi on 2015/11/27.
 */
public class VideoActivity extends Activity {
    private SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    MediaPlayer mMediaPlayer;
    private Button back;
    private String url;
    //VideoView videoView;
    private Button btn_play;
    private Button btn_pause;
    private Button btn_replay;

    public static void launch(Activity activity, String url) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        url = getIntent().getStringExtra("url");
        Log.d("URL", url);
        mSurfaceView = (SurfaceView) findViewById(R.id.video);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mMediaPlayer = new MediaPlayer();

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

        /**btn_function**/
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_replay = (Button) findViewById(R.id.btn_replay);

        btn_play.setOnClickListener(onClick);
        btn_pause.setOnClickListener(onClick);
        btn_replay.setOnClickListener(onClick);

        /**btn function**/
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Close", "video_close");
                release();
                finish();
            }
        });
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_play:
                    mMediaPlayer.start();
                    btn_play.setEnabled(false);
                    break;
                case R.id.btn_pause:
                    pause();
                    break;
                case R.id.btn_replay:
                    replay();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        release();
        super.onDestroy();
    }

    public void play() {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void replay() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(0);
        } else {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
            }
        }
    }

    public void pause() {
        if (btn_pause.getText().toString().trim().equals("继续")) {
            btn_pause.setText("停止");
            mMediaPlayer.start();
        } else if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            btn_pause.setText("继续");
            mMediaPlayer.pause();
        } else {
            btn_play.setEnabled(true);
            Toast.makeText(VideoActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer = null;
        }
    }


}

package com.example.wuyuxi.webcam;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by wuyuxi on 2015/11/27.
 */
public class VideoActivity extends Activity {
    private SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    private Button back;
    VideoView videoView;

    public static void launch(Activity activity,String url){
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

//        surfaceView = (SurfaceView) findViewById(R.id.video);

        videoView = (VideoView) findViewById(R.id.video);

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Close", "video_close");
                videoView.stopPlayback();
                finish();
            }
        });

        String url = getIntent().getStringExtra("url");
        Log.d("device_url",url);
//        url = "rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp";
        videoView.setVideoURI(Uri.parse(url));
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();

//        surfaceHolder = surfaceView.getHolder();
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                mediaPlayer = new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mediaPlayer.setDisplay(surfaceHolder);
//
//                try {
//                    mediaPlayer.reset();
//                    mediaPlayer.setDataSource(url);
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//
//            }
//        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (this.mediaPlayer != null) {
//            this.mediaPlayer.release();
//            this.mediaPlayer = null;
//        }

    }


}

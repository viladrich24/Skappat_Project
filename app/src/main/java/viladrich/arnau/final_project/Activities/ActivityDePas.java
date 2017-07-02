package viladrich.arnau.final_project.Activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import viladrich.arnau.final_project.R;

public class ActivityDePas extends AppCompatActivity {

    TextView textV;
    VideoView videoV;
    int pos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_de_pas);

        textV = (TextView) findViewById(R.id.text_v);
        videoV = (VideoView) findViewById(R.id.video_v);

        textV.setText(R.string.text_mofat);

        setUpVideoView();

    }

    protected void setUpVideoView() {
        String uriPath = "android.resoure://" + getPackageName() + "/" + R.raw.video_siri;
        MediaController mediaContr = new MediaController(this);
        Uri uri = Uri.parse(uriPath);
        videoV.setMediaController(mediaContr);

        try{

            videoV.setVideoURI(uri);
            videoV.requestFocus();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoV.setOnPreparedListener(videoListener);

    }

    protected MediaPlayer.OnPreparedListener videoListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.setLooping(true);

            if(pos == 0) videoV.start();
            else videoV.pause();
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Pos", videoV.getCurrentPosition());
        videoV.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pos = savedInstanceState.getInt("pos");
        videoV.seekTo(pos);
    }
}
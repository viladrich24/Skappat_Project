package viladrich.arnau.final_project.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.material.joanbarroso.flipper.CoolImageFlipper;

import java.io.File;

import viladrich.arnau.final_project.BaseActivity;
import viladrich.arnau.final_project.R;

import static utils.Constants.PREFS_NAME;

public class ReproductorMusica extends BaseActivity implements View.OnClickListener {

    Button previousSong, nextSong;
    TextView navBarMiniText, navBarUser, titolCanco;
    MediaPlayer mediaPlayer;
    ImageView playPause;
    CoolImageFlipper IF;
    SharedPreferences settings;
    Boolean playing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_musica);
        setTitle("MusicPlayer");

        //titolCanco = (TextView) findViewById(R.id.nomCanco);
        String nomCanco = "detonem l'estona";
        //titolCanco.setText("Està sonant: " + nomCanco);

        File sdCard = Environment.getExternalStorageDirectory();
        File song1 = new File(sdCard.getAbsolutePath()+ "/raw/son1.mp3");
        File song2 = new File(sdCard.getAbsolutePath()+ "/raw/song2.mp3");

        try{
            mediaPlayer.setDataSource(song2.getAbsolutePath());

        }catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.song2);

        nextSong = (Button) findViewById(R.id.nextSong);
        previousSong = (Button) findViewById(R.id.previousSong);
        playPause = (ImageView) findViewById(R.id.pausePlayButton);

        nextSong.setOnClickListener(this);
        previousSong.setOnClickListener(this);
        playPause.setOnClickListener(this);

        IF = new CoolImageFlipper(this);

        View navHeaderView = navigationView.getHeaderView(0);
        navBarMiniText = (TextView) navHeaderView.findViewById(R.id.miniTextNavBar);
        navBarUser = (TextView) navHeaderView.findViewById(R.id.nomUsuariHeaderBar);

        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String username = settings.getString("myActualUser", "usuari");

        navBarUser.setText(" > "+username);

    }

    private void playing() {

        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Log.v("TAG", "hola");
        mediaPlayer.start();
        /*
        playing = settings.getBoolean("myMediaButton", false);

        if(playing) {

            mediaPlayer.stop();
            Drawable playImage = getDrawable(R.drawable.play);

            IF.flipImage(playImage, (ImageView) findViewById(R.id.pausePlayButton));

            settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("myMediaButton", false);
            editor.apply();


        }

        else {

            mediaPlayer.start();
            IF.flipImage(getDrawable(R.drawable.pause), (ImageView) findViewById(R.id.pausePlayButton));

            settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("myMediaButton", true);
            editor.apply();
        }*/
    }

    protected int whatIsMyId() {
        return 0;
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pausePlayButton: playing(); break;

        }
    }

}

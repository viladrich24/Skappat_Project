package viladrich.arnau.final_project.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import viladrich.arnau.final_project.BaseActivity;
import viladrich.arnau.final_project.R;

import static utils.Constants.PREFS_NAME;

public class ReproductorMusica extends BaseActivity implements View.OnClickListener {

    Button previousSong, nextSong, playButton, pauseButton;
    MediaPlayer mediaPlayer = new MediaPlayer();
    TextView navBarMiniText, navBarUser, titolCanco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_musica);
        setTitle("MusicPlayer");

        titolCanco = (TextView) findViewById(R.id.nomCanco);
        File sdCard = Environment.getExternalStorageDirectory();
        String nomCanco = "detonem l'estona";
        File song = new File(sdCard.getAbsolutePath()+"/raw/detonem_estona.mp3");
        titolCanco.setText("Està sonant: " + nomCanco);

        try{
            mediaPlayer.setDataSource(song.getAbsolutePath());

        }catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.detonem_estona);

        nextSong = (Button) findViewById(R.id.nextSong);
        previousSong = (Button) findViewById(R.id.previousSong);
        playButton = (Button) findViewById(R.id.playButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);

        nextSong.setOnClickListener(this);
        previousSong.setOnClickListener(this);
        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);

        View navHeaderView = navigationView.getHeaderView(0);
        navBarMiniText = (TextView) navHeaderView.findViewById(R.id.miniTextNavBar);
        navBarUser = (TextView) navHeaderView.findViewById(R.id.nomUsuariHeaderBar);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String username = settings.getString("myActualUser", "usuari");

        navBarUser.setText(" > "+username);

        //TODO: s'hauria de fer amb serveis
    }


    private void pauseMusic() {
        mediaPlayer.stop();
    }

    private void playMusic() {
        mediaPlayer.start();
    }

    protected int whatIsMyId() {
        return 0;
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pauseButton: playMusic(); break;
            case R.id.playButton: pauseMusic(); break;

        }
    }

}

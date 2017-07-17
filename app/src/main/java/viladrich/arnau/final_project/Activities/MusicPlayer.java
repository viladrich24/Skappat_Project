package viladrich.arnau.final_project.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.material.joanbarroso.flipper.CoolImageFlipper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import viladrich.arnau.final_project.BaseActivity;
import viladrich.arnau.final_project.R;

import static utils.Constants.PREFS_NAME;


public class MusicPlayer extends BaseActivity implements View.OnClickListener {

    int actualSong = 0;
    Button previousSong, nextSong;
    ImageView playPause;
    CoolImageFlipper IF;
    SharedPreferences settings;
    Boolean playing = false;
    TextView navBarMiniText, navBarUser;

    ListView listView;
    List<String> list;
    ListAdapter adapter;
    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        setTitle("MusicPlayer");

        actualSong = 0;
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


        //list

        listView = (ListView) findViewById(R.id.llista);
        list = new ArrayList<>();

        Field[] fields = R.raw.class.getFields();

        for(int i = 0; i < fields.length - 1; i++){
            list.add(fields[i].getName());
        }

        list.remove(0);
        list.remove(0);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                actualSong = i;

                if(mediaPlayer != null){ //pares la canço anterior
                    mediaPlayer.release();
                }

                //giro i poso a true i guardo la canco que sona
                IF.flipImage(getDrawable(R.drawable.pause), (ImageView) findViewById(R.id.pausePlayButton));

                settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("myMediaButton", true);
                editor.putInt("mySong", actualSong);
                editor.apply();

                String num = Integer.toString(actualSong + 1);
                Toast.makeText(getApplicationContext(), "Ara sona la song"+num, Toast.LENGTH_LONG).show();

                int resID = getResources().getIdentifier(list.get(i), "raw", getPackageName());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), resID);
                mediaPlayer.start();

            }
        });
    }

    public void reproduirMusica(){

        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        playing = settings.getBoolean("myMediaButton", false);
        actualSong = settings.getInt("mySong", 0);

        if(playing) {

            mediaPlayer.pause();
            IF.flipImage(getDrawable(R.drawable.play), (ImageView) findViewById(R.id.pausePlayButton));

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

            String num = Integer.toString(actualSong + 1);
            Toast.makeText(getApplicationContext(), "Ara sona la song"+num, Toast.LENGTH_LONG).show();

        }

    }

    public void nextSongs(){

        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        actualSong = settings.getInt("mySong", 0);

        if(actualSong == list.size()-1) actualSong = 0;
        else actualSong = actualSong + 1;

        int resID = getResources().getIdentifier(list.get(actualSong), "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), resID);
        mediaPlayer.start();

        settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("mySong", actualSong);
        editor.apply();

        String num = Integer.toString(actualSong + 1);
        Toast.makeText(getApplicationContext(), "Ara sona la song"+num, Toast.LENGTH_LONG).show();
    }

    public void previousSongs(){

        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        actualSong = settings.getInt("mySong", 0);

        if(actualSong == 0) actualSong = list.size() - 1;
        else actualSong = actualSong - 1;

        int resID = getResources().getIdentifier(list.get(actualSong), "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), resID);
        mediaPlayer.start();

        settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("mySong", actualSong);
        editor.apply();

        String num = Integer.toString(actualSong + 1);
        Toast.makeText(getApplicationContext(), "Ara sona la song"+num, Toast.LENGTH_LONG).show();
    }

    @Override
    protected int whatIsMyId() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pausePlayButton: reproduirMusica(); break;
            case R.id.nextSong: nextSongs(); break;
            case R.id.previousSong: previousSongs(); break;

        }
    }
}

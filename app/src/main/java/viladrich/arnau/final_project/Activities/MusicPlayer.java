package viladrich.arnau.final_project.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import viladrich.arnau.final_project.BaseActivity;
import viladrich.arnau.final_project.R;


public class MusicPlayer extends BaseActivity implements View.OnClickListener {

    ListView listView;
    List<String> list;
    ListAdapter adapter;
    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

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


                /*if(mediaPlayer != null){
                    mediaPlayer.release();
                }*/

                //MediaPlayer mPlayer2;

                /*mPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.song2);
                mPlayer2.start();

                MediaPlayer mPlayer1;

                mPlayer1 = MediaPlayer.create(MusicPlayer.this, R.raw.song1);
                mPlayer1.start();


                String urri = "R.raw."+list.get(i);
                //int resID = getResources().getIdentifier(R.raw.song1);
                Log.v("TAG", urri);*/

                int resID = getResources().getIdentifier(list.get(i), "raw", getPackageName());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.song1);
                mediaPlayer.start();

            }
        });



    }

    @Override
    protected int whatIsMyId() {
        return 0;
    }

    @Override
    public void onClick(View v) {

    }
}

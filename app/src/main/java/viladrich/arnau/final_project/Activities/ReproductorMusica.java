package viladrich.arnau.final_project.Activities;

import android.os.Bundle;

import viladrich.arnau.final_project.BaseActivity;
import viladrich.arnau.final_project.R;

public class ReproductorMusica extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_musica);
        setTitle("MusicPlayer");
        setItemChecked();
    }

    @Override
    protected int whatIsMyId() {
        return R.id.reproductor_musica;
    }
}

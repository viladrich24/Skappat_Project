package viladrich.arnau.final_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import viladrich.arnau.final_project.Activities.Calculadora;
import viladrich.arnau.final_project.Activities.Memory;
import viladrich.arnau.final_project.Activities.MusicPlayer;
import viladrich.arnau.final_project.Activities.Ranking;
import viladrich.arnau.final_project.Activities.ReproductorMusica;
import viladrich.arnau.final_project.Activities.UserProfile;

import static utils.Constants.PREFS_NAME;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    public NavigationView navigationView;
    ArrayMap<Integer, Class> m,n;
    private CharSequence mDrawerTitle, mTitle;

    {
        m = new ArrayMap<>(); //activitats de la llista

        m.put(R.id.perfil, UserProfile.class);
        m.put(R.id.calculadora, Calculadora.class);
        m.put(R.id.reproductor_musica, ReproductorMusica.class);
        m.put(R.id.musicPlayer2, MusicPlayer.class);
        m.put(R.id.memory, Memory.class);
        m.put(R.id.ranking, Ranking.class);
        m.put(R.id.logOut, PagerHolderActivity.class);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        setView();
    }

    protected void setView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitle = mDrawerTitle = getTitle();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawer.setDrawerListener(toggle);

        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    protected void setItemChecked() {
        navigationView.setCheckedItem(whatIsMyId());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        final int id = item.getItemId();
        if(id != whatIsMyId()){
            switch (id){
                case R.id.logOut:
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("myBoolean", false);
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(), m.get(id));
                    finish();
                    startActivity(i);
                    break;

                default: startActivity(new Intent(getApplicationContext(), m.get(id))); break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected abstract int whatIsMyId();

    @Override
    public void setContentView(int layoutResID) {

        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.frame_layout_base);

        getLayoutInflater().inflate(layoutResID, frameLayout, true);

        super.setContentView(fullLayout);
        setView();
    }

    public abstract void onClick(View v);
}

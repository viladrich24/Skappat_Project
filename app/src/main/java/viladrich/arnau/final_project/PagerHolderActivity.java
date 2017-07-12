package viladrich.arnau.final_project;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import viladrich.arnau.final_project.Database.MyDatabaseHelper;
import viladrich.arnau.final_project.Fragments_Entrada.PagerAdapter;

public class PagerHolderActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    ViewPager viewPager;
    MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_holder);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        myDatabaseHelper = MyDatabaseHelper.getInstance(this);

        // ens diu si ens trobem al menu de login o signin

        TabLayout tabLayout = (TabLayout) findViewById(R.id.log_o_sign);
        tabLayout.setTabTextColors(Color.BLACK, Color.BLUE);

        // carrega la vista un cop s'ha fet els canvis


        tabLayout.setupWithViewPager(viewPager);

    }
}

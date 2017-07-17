package viladrich.arnau.final_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import viladrich.arnau.final_project.Activities.UserProfile;

import static utils.Constants.PREFS_NAME;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Boolean loggedIn = settings.getBoolean("myBoolean", false);

        Intent i;

        if(loggedIn) i = new Intent(getApplicationContext(), PagerHolderActivity.class);  //fes el loggin

        else i = new Intent(getApplicationContext(), PagerHolderActivity.class); //no cal que el facis

        startActivity(i);

    }
}

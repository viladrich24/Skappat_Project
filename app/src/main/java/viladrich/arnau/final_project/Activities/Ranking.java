package viladrich.arnau.final_project.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import viladrich.arnau.final_project.Activities.rankingClasses.MyCustomAdapter;
import viladrich.arnau.final_project.BaseActivity;
import viladrich.arnau.final_project.Database.MyDatabaseHelper;
import viladrich.arnau.final_project.R;

import static utils.Constants.PREFS_NAME;

public class Ranking extends BaseActivity implements View.OnClickListener {

    TextView navBarMiniText, navBarUser;
    private RecyclerView mRecyclerView;
    MyDatabaseHelper myDatabaseHelper;
    String username;
    Button resetRanking;
    private LinearLayoutManager mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle("Ranking");

        myDatabaseHelper = MyDatabaseHelper.getInstance(getApplicationContext());

        resetRanking = (Button) findViewById(R.id.resetRanking);
        resetRanking.setOnClickListener(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        username = settings.getString("myActualUser", "usuari");

        View navHeaderView = navigationView.getHeaderView(0);
        navBarMiniText = (TextView) navHeaderView.findViewById(R.id.miniTextNavBar);
        navBarUser = (TextView) navHeaderView.findViewById(R.id.nomUsuariHeaderBar);

        navBarUser.setText(" > "+username);

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);

        mLinearLayout = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearLayout);

        HashMap<String, String> totsUsuarisPhones = myDatabaseHelper.agafarPhone();
        HashMap<String, String> totsUsuarisRecords = myDatabaseHelper.agafarRecord();
        HashMap<String, String> totsUsuarisIcons = myDatabaseHelper.agafarIcon();

        MyCustomAdapter mtAdapt = new MyCustomAdapter(totsUsuarisRecords, totsUsuarisPhones, totsUsuarisIcons);

        mRecyclerView.setAdapter(mtAdapt);

        //TODO: relacionar el numero amb el valor que ocupa a la posicio
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int whatIsMyId() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetRanking:
                myDatabaseHelper.resetRanking();
                Intent i = new Intent(getApplicationContext(),Ranking.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Reset done!", Toast.LENGTH_LONG).show();
                break;
        }
    }
}

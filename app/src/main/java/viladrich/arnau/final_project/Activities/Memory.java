package viladrich.arnau.final_project.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.material.joanbarroso.flipper.CoolImageFlipper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import viladrich.arnau.final_project.BaseActivity;
import viladrich.arnau.final_project.Database.MyDatabaseHelper;
import viladrich.arnau.final_project.R;

import static utils.Constants.PREFS_NAME;

public class Memory extends BaseActivity implements View.OnClickListener {

    ImageView casella0_0, casella0_1, casella0_2, casella0_3, casella1_0, casella1_1, casella1_2, casella1_3, casella2_0, casella2_1, casella2_2, casella2_3, casella3_0, casella3_1, casella3_2, casella3_3;
    CoolImageFlipper IF;
    Button reset;
    TextView moves, time;
    Handler start;
    int mins = 0, secs = 0, steadySecs, steadyMins;
    int id_anterior = 0, moviments = 0, num_match = 0, girades = 0;
    TextView navBarMiniText, navBarUser;

    MyDatabaseHelper myDatabaseHelper;

    Boolean unaGirada = false, noMatch = true, timeOn = false, canviemTemps = false;

    Drawable [] vector_memory = new Drawable[16];
    String [] vector_memory_string = new String[16];

    ArrayList<Drawable> a;
    ArrayList<String> inte;

    int [] ids = {R.id.casella0_0, R.id.casella0_1, R.id.casella0_2, R.id.casella0_3, R.id.casella1_0, R.id.casella1_1, R.id.casella1_2, R.id.casella1_3, R.id.casella2_0, R.id.casella2_1, R.id.casella2_2, R.id.casella2_3, R.id.casella3_0, R.id.casella3_1, R.id.casella3_2, R.id.casella3_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        setTitle("Memory");
        setItemChecked();

        start = new Handler();
        a = new ArrayList<>();
        inte = new ArrayList<>();

        myDatabaseHelper = MyDatabaseHelper.getInstance(this);
        reset = (Button) findViewById(R.id.reset_memory);

        casella0_0 = (ImageView) findViewById(R.id.casella0_0);
        casella0_1 = (ImageView) findViewById(R.id.casella0_1);
        casella0_2 = (ImageView) findViewById(R.id.casella0_2);
        casella0_3 = (ImageView) findViewById(R.id.casella0_3);
        casella1_0 = (ImageView) findViewById(R.id.casella1_0);
        casella1_1 = (ImageView) findViewById(R.id.casella1_1);
        casella1_2 = (ImageView) findViewById(R.id.casella1_2);
        casella1_3 = (ImageView) findViewById(R.id.casella1_3);
        casella2_0 = (ImageView) findViewById(R.id.casella2_0);
        casella2_1 = (ImageView) findViewById(R.id.casella2_1);
        casella2_2 = (ImageView) findViewById(R.id.casella2_2);
        casella2_3 = (ImageView) findViewById(R.id.casella2_3);
        casella3_0 = (ImageView) findViewById(R.id.casella3_0);
        casella3_1 = (ImageView) findViewById(R.id.casella3_1);
        casella3_2 = (ImageView) findViewById(R.id.casella3_2);
        casella3_3 = (ImageView) findViewById(R.id.casella3_3);

        moves = (TextView) findViewById(R.id.num_moves);
        time = (TextView) findViewById(R.id.temps);

        casella0_0.setOnClickListener(this);
        casella0_1.setOnClickListener(this);
        casella0_2.setOnClickListener(this);
        casella0_3.setOnClickListener(this);
        casella1_0.setOnClickListener(this);
        casella1_1.setOnClickListener(this);
        casella1_2.setOnClickListener(this);
        casella1_3.setOnClickListener(this);
        casella2_0.setOnClickListener(this);
        casella2_1.setOnClickListener(this);
        casella2_2.setOnClickListener(this);
        casella2_3.setOnClickListener(this);
        casella3_0.setOnClickListener(this);
        casella3_1.setOnClickListener(this);
        casella3_2.setOnClickListener(this);
        casella3_3.setOnClickListener(this);

        reset.setOnClickListener(this);

        IF = new CoolImageFlipper(this);

        View navHeaderView = navigationView.getHeaderView(0);
        navBarMiniText = (TextView) navHeaderView.findViewById(R.id.miniTextNavBar);
        navBarUser = (TextView) navHeaderView.findViewById(R.id.nomUsuariHeaderBar);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String username = settings.getString("myActualUser", "usuari");

        navBarUser.setText(" > "+username);
        createMatrix();
    }

    public void Rellotge (){

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        secs++;
                        if(secs == 60) {
                            mins++;
                            secs = 0;
                        }
                        if(timeOn) time.setText(String.format("Time: %02d:%02d", mins, secs));
                    }
                });
            }
        }, 0, 1000);
    }

    public void hideAll(){

        for(int i = 0; i <  16; i++){
            hideImage(ids[i]);
            clickable(ids[i]);
        }
    }

    public void createMatrix() {  // VA BÉ

        addDrawables();
        addIntDrawables();
        moviments = 0;
        num_match = 0;
        girades = 0;
        unaGirada = false;
        noMatch = true;
        id_anterior = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {

                num_match = 0;
                int ale = 0;

                for (int i = 0; i < 16; i++) {

                    ale = (int) (Math.random() * a.size() + 0);

                    vector_memory[i] = a.get(ale);
                    vector_memory_string[i] = inte.get(ale);

                    a.remove(ale);
                    inte.remove(ale);
                }

            }}).start();
    }

    public void addDrawables(){

        a.add(getDrawable(R.drawable.a));
        a.add(getDrawable(R.drawable.a));
        a.add(getDrawable(R.drawable.b));
        a.add(getDrawable(R.drawable.b));
        a.add(getDrawable(R.drawable.c));
        a.add(getDrawable(R.drawable.c));
        a.add(getDrawable(R.drawable.d));
        a.add(getDrawable(R.drawable.d));
        a.add(getDrawable(R.drawable.e));
        a.add(getDrawable(R.drawable.e));
        a.add(getDrawable(R.drawable.f));
        a.add(getDrawable(R.drawable.f));
        a.add(getDrawable(R.drawable.g));
        a.add(getDrawable(R.drawable.g));
        a.add(getDrawable(R.drawable.h));
        a.add(getDrawable(R.drawable.h));
    }

    public void addIntDrawables(){

        inte.add("a");
        inte.add("a");
        inte.add("b");
        inte.add("b");
        inte.add("c");
        inte.add("c");
        inte.add("d");
        inte.add("d");
        inte.add("e");
        inte.add("e");
        inte.add("f");
        inte.add("f");
        inte.add("g");
        inte.add("g");
        inte.add("h");
        inte.add("h");

    }

    //ensenyar, amagar, girar, comprovar cartes

    public void hideImage(int id){

        Drawable picas = getDrawable(R.drawable.as_picas);

        switch (id){
            case R.id.casella0_0: IF.flipImage(picas, (ImageView) findViewById(R.id.casella0_0)); break;
            case R.id.casella0_1: IF.flipImage(picas, (ImageView) findViewById(R.id.casella0_1)); break;
            case R.id.casella0_2: IF.flipImage(picas, (ImageView) findViewById(R.id.casella0_2)); break;
            case R.id.casella0_3: IF.flipImage(picas, (ImageView) findViewById(R.id.casella0_3)); break;
            case R.id.casella1_0: IF.flipImage(picas, (ImageView) findViewById(R.id.casella1_0)); break;
            case R.id.casella1_1: IF.flipImage(picas, (ImageView) findViewById(R.id.casella1_1)); break;
            case R.id.casella1_2: IF.flipImage(picas, (ImageView) findViewById(R.id.casella1_2)); break;
            case R.id.casella1_3: IF.flipImage(picas, (ImageView) findViewById(R.id.casella1_3)); break;
            case R.id.casella2_0: IF.flipImage(picas, (ImageView) findViewById(R.id.casella2_0)); break;
            case R.id.casella2_1: IF.flipImage(picas, (ImageView) findViewById(R.id.casella2_1)); break;
            case R.id.casella2_2: IF.flipImage(picas, (ImageView) findViewById(R.id.casella2_2)); break;
            case R.id.casella2_3: IF.flipImage(picas, (ImageView) findViewById(R.id.casella2_3)); break;
            case R.id.casella3_0: IF.flipImage(picas, (ImageView) findViewById(R.id.casella3_0)); break;
            case R.id.casella3_1: IF.flipImage(picas, (ImageView) findViewById(R.id.casella3_1)); break;
            case R.id.casella3_2: IF.flipImage(picas, (ImageView) findViewById(R.id.casella3_2)); break;
            case R.id.casella3_3: IF.flipImage(picas, (ImageView) findViewById(R.id.casella3_3)); break;
        }

    }

    public void showImage(int id){

            girades++;

            switch (id) {

                case R.id.casella0_0: IF.flipImage(vector_memory[0], (ImageView) findViewById(R.id.casella0_0)); break;
                case R.id.casella0_1: IF.flipImage(vector_memory[1], (ImageView) findViewById(R.id.casella0_1)); break;
                case R.id.casella0_2: IF.flipImage(vector_memory[2], (ImageView) findViewById(R.id.casella0_2)); break;
                case R.id.casella0_3: IF.flipImage(vector_memory[3], (ImageView) findViewById(R.id.casella0_3)); break;
                case R.id.casella1_0: IF.flipImage(vector_memory[4], (ImageView) findViewById(R.id.casella1_0)); break;
                case R.id.casella1_1: IF.flipImage(vector_memory[5], (ImageView) findViewById(R.id.casella1_1)); break;
                case R.id.casella1_2: IF.flipImage(vector_memory[6], (ImageView) findViewById(R.id.casella1_2)); break;
                case R.id.casella1_3: IF.flipImage(vector_memory[7], (ImageView) findViewById(R.id.casella1_3)); break;
                case R.id.casella2_0: IF.flipImage(vector_memory[8], (ImageView) findViewById(R.id.casella2_0)); break;
                case R.id.casella2_1: IF.flipImage(vector_memory[9], (ImageView) findViewById(R.id.casella2_1)); break;
                case R.id.casella2_2: IF.flipImage(vector_memory[10], (ImageView) findViewById(R.id.casella2_2)); break;
                case R.id.casella2_3: IF.flipImage(vector_memory[11], (ImageView) findViewById(R.id.casella2_3)); break;
                case R.id.casella3_0: IF.flipImage(vector_memory[12], (ImageView) findViewById(R.id.casella3_0)); break;
                case R.id.casella3_1: IF.flipImage(vector_memory[13], (ImageView) findViewById(R.id.casella3_1)); break;
                case R.id.casella3_2: IF.flipImage(vector_memory[14], (ImageView) findViewById(R.id.casella3_2)); break;
                case R.id.casella3_3: IF.flipImage(vector_memory[15], (ImageView) findViewById(R.id.casella3_3)); break;

        }

    }

    public void changeImage(final int id){ // id de la caseela que es gira

        if(moviments == 0) {
            timeOn = true;
            Rellotge();
        }

        if (girades < 2) {

            showImage(id);

            if (moviments != 0 && girades == 2) {

                comprovarMatch(id, id_anterior);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (noMatch) { //els amago si no son iguals

                            hideImage(id);
                            hideImage(id_anterior);
                        }
                        else {

                            unClickable(id);
                            unClickable(id_anterior);
                        }

                        girades = 0;

                    }}, 2000);

            } else {

                id_anterior = id;
                moviments++;
                moves.setText("moves: " + Integer.toString(moviments));
            }
        }
    }

    public void comprovarMatch(int id, int id_anterior){

        int valor1 = -1, valor2 = -1;

        for(int i = 0; i < 16; i++){
            if(ids[i]== id) valor1 = i;
            if(ids[i]== id_anterior) valor2 = i;
        }

        if(valor1 != -1 && valor2 != -1) {

            if (!vector_memory_string[valor1].equals(vector_memory_string[valor2])) {
                noMatch = true;
            }
            else {
                num_match++;
                if(num_match == 8) memoryFinished();
                noMatch = false;
            }
        }

        else Toast.makeText(getApplicationContext(), "Something wrong, RESET!", Toast.LENGTH_LONG).show();
    }

    //finish, reset

    public void memoryFinished(){
        timeOn = false;
        steadySecs = secs;
        steadyMins = mins;
        String tempsGuardat = String.format("%02d:%02d s", steadyMins, steadySecs);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String username = settings.getString("myActualUser", "user");

        String record = myDatabaseHelper.queryMemory(username);
        String bestLastTime = myDatabaseHelper.queryTime(username);

        int sub1 = (Integer.parseInt(bestLastTime.substring(0, 1)) - Integer.parseInt(tempsGuardat.substring(0, 1)));
        int sub2 = (Integer.parseInt(bestLastTime.substring(1, 2)) - Integer.parseInt(tempsGuardat.substring(1, 2)));
        int sub3 = (Integer.parseInt(bestLastTime.substring(3, 4)) - Integer.parseInt(tempsGuardat.substring(3, 4)));
        int sub4 = (Integer.parseInt(bestLastTime.substring(4, 5)) - Integer.parseInt(tempsGuardat.substring(4, 5)));

        if((sub1 > 0) || ((sub1 == 0)&&(sub2 > 0)) || ((sub1 == 0)&&(sub2 == 0)&&(sub3 > 0)) || ((sub1 == 0)&&(sub2 == 0)&&(sub3 == 0)&&(sub4 > 0))) canviemTemps = true;

        if(canviemTemps) myDatabaseHelper.addNewTime(username, tempsGuardat);
        if(moviments < Integer.parseInt(record)) myDatabaseHelper.addNewRanking(username, Integer.toString(moviments));

        dialegGameFinished();

    }

    public void dialegGameFinished(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String mess = "Completat amb "+ Integer.toString(moviments)+" moviments i "+String.format("%02d:%02d s!", steadyMins, steadySecs);

        builder.setTitle("Has acabat la partida, enhorabona!");
        builder.setMessage(mess);

        builder.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    public void resetGame(){
        createMatrix();
        hideAll();
        canviemTemps = false;
        noMatch = true;
        timeOn = true;
        secs = 0;
        mins = 0;
        moves.setText("Moves: "+Integer.toString(moviments));
    }

    public void unClickable(int id) {

        switch (id) {
            case R.id.casella0_0: casella0_0.setEnabled(false); break;
            case R.id.casella0_1: casella0_1.setEnabled(false); break;
            case R.id.casella0_2: casella0_2.setEnabled(false); break;
            case R.id.casella0_3: casella0_3.setEnabled(false); break;
            case R.id.casella1_0: casella1_0.setEnabled(false); break;
            case R.id.casella1_1: casella1_1.setEnabled(false); break;
            case R.id.casella1_2: casella1_2.setEnabled(false); break;
            case R.id.casella1_3: casella1_3.setEnabled(false); break;
            case R.id.casella2_0: casella2_0.setEnabled(false); break;
            case R.id.casella2_1: casella2_1.setEnabled(false); break;
            case R.id.casella2_2: casella2_2.setEnabled(false); break;
            case R.id.casella2_3: casella2_3.setEnabled(false); break;
            case R.id.casella3_0: casella3_0.setEnabled(false); break;
            case R.id.casella3_1: casella3_1.setEnabled(false); break;
            case R.id.casella3_2: casella3_2.setEnabled(false); break;
            case R.id.casella3_3: casella3_3.setEnabled(false); break;
        }
    }

    public void clickable(int ids) {

        switch (ids) {
            case R.id.casella0_0: casella0_0.setEnabled(true); break;
            case R.id.casella0_1: casella0_1.setEnabled(true); break;
            case R.id.casella0_2: casella0_2.setEnabled(true); break;
            case R.id.casella0_3: casella0_3.setEnabled(true); break;
            case R.id.casella1_0: casella1_0.setEnabled(true); break;
            case R.id.casella1_1: casella1_1.setEnabled(true); break;
            case R.id.casella1_2: casella1_2.setEnabled(true); break;
            case R.id.casella1_3: casella1_3.setEnabled(true); break;
            case R.id.casella2_0: casella2_0.setEnabled(true); break;
            case R.id.casella2_1: casella2_1.setEnabled(true); break;
            case R.id.casella2_2: casella2_2.setEnabled(true); break;
            case R.id.casella2_3: casella2_3.setEnabled(true); break;
            case R.id.casella3_0: casella3_0.setEnabled(true); break;
            case R.id.casella3_1: casella3_1.setEnabled(true); break;
            case R.id.casella3_2: casella3_2.setEnabled(true); break;
            case R.id.casella3_3: casella3_3.setEnabled(true); break;
        }
    }

    @Override
    protected int whatIsMyId() {
        return 0;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.reset_memory: resetGame(); break;
            default: changeImage(v.getId());

        }
    }
}

package viladrich.arnau.final_project.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import viladrich.arnau.final_project.BaseActivity;
import viladrich.arnau.final_project.Database.MyDatabaseHelper;
import viladrich.arnau.final_project.R;

import static utils.Constants.PREFS_NAME;


public class Calculadora extends BaseActivity implements View.OnClickListener {

    Button b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b_del, b_AC, b_suma, b_resta, b_multi, b_divisio, b_coma, b_equal, b_mofa, b_ans;
    Boolean coma_pitjada=false, ep_infinit=false, V_Op=false, already_op=false, no_escrit=true, resultat_actiu=false, anterior_V=false;
    TextView navBarMiniText, navBarUser;
    MyDatabaseHelper myDatabaseHelper;
    SharedPreferences settings;

    TextView pantalla;
    int cont, pos_coma, cont_prioritat=0, cont_operadors, i, cont_sumes, resu_int, numDigitsAns;
    Double temp = 0.0, resultat, res_d_arr;

    String valor, primeraPos, username, primera_dada, conca, trans, conc, res, resultat_enString;
    Boolean toastOn = true;

    Boolean toastActivat = true;
    View layout;

    int iD_item;

    public ArrayList<String> operacions = new ArrayList<>();
    public ArrayList<String> tot_unperun = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        setTitle("Calculadora");
        setItemChecked();

        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        iD_item = settings.getInt("myItem", R.id.settings_toast);
        toastActivat = settings.getBoolean("myToast", true);
        toastOn = toastActivat;

        View navHeaderView = navigationView.getHeaderView(0);
        navBarMiniText = (TextView) navHeaderView.findViewById(R.id.miniTextNavBar);
        navBarUser = (TextView) navHeaderView.findViewById(R.id.nomUsuariHeaderBar);

        username = settings.getString("myActualUser", "usuari");
        navBarUser.setText(" > "+username);


        //asdajsdaj

        /*MenuItem itemToast = (MenuItem) findViewById(R.id.settings_toast);
        MenuItem itemNoToast = (MenuItem) findViewById(R.id.settings_estat);

        if(toastOn) {
            itemToast.setChecked(true);
            itemNoToast.setChecked(false);
        }
        else {
            itemNoToast.setChecked(true);
            itemToast.setChecked(false);
        }*/


        //TODO: posar checked l'item q toca
        myDatabaseHelper = MyDatabaseHelper.getInstance(this);

        resultat = 0.0;
        layout = findViewById(R.id.layout);

        b0 = (Button) findViewById(R.id.num_0);
        b1 = (Button) findViewById(R.id.num_1);
        b2 = (Button) findViewById(R.id.num_2);
        b3 = (Button) findViewById(R.id.num_3);
        b4 = (Button) findViewById(R.id.num_4);
        b5 = (Button) findViewById(R.id.num_5);
        b6 = (Button) findViewById(R.id.num_6);
        b7 = (Button) findViewById(R.id.num_7);
        b8 = (Button) findViewById(R.id.num_8);
        b9 = (Button) findViewById(R.id.num_9);
        b_AC = (Button) findViewById(R.id.num_AC);
        b_ans = (Button) findViewById(R.id.num_ans);
        b_del = (Button) findViewById(R.id.num_Del);
        b_coma = (Button) findViewById(R.id.num_coma);
        b_suma = (Button) findViewById(R.id.num_suma);
        b_multi = (Button) findViewById(R.id.num_mult);
        b_mofa = (Button) findViewById(R.id.num_mofa);
        b_resta = (Button) findViewById(R.id.num_resta);
        b_equal = (Button) findViewById(R.id.num_igual);
        b_divisio = (Button) findViewById(R.id.num_div);
        pantalla = (TextView) findViewById(R.id.textview);

        b0.setOnClickListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        b_AC.setOnClickListener(this);
        b_ans.setOnClickListener(this);
        b_del.setOnClickListener(this);
        b_coma.setOnClickListener(this);
        b_suma.setOnClickListener(this);
        b_mofa.setOnClickListener(this);
        b_resta.setOnClickListener(this);
        b_multi.setOnClickListener(this);
        b_equal.setOnClickListener(this);
        b_divisio.setOnClickListener(this);

        pantalla.setMovementMethod(new ScrollingMovementMethod());


        b_mofa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View.OnClickListener pulsarMofa = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), ActivityDePas.class);
                        startActivity(i);
                    }
                };

                Snackbar.make(layout, R.string.snackbar_text, Snackbar.LENGTH_LONG).setAction(R.string.snackbar_action, pulsarMofa).show();
            }
        });
    }

    @Override
    protected int whatIsMyId() {
        return R.id.calculadora;
    }

    public void escriureValors(){   //VA BÉ

        tot_unperun.add(cont, valor);

        if(cont == 0) pantalla.setText(valor);
        else pantalla.append(valor);

        if(cont_operadors == -1) cont_operadors = 0;

        if(!anterior_V) { //cada cop q l'anterior sigui variable ho guardi a al seguent

            operacions.add(cont_operadors, valor);
        }

        else { //l'anterior és varaible, cal concatenar

            primera_dada = operacions.get(cont_operadors);
            conca = primera_dada + valor;

            operacions.add(cont_operadors, conca);

        }

        cont++;  //cada cop que s'escriu alguna cosa sumes 1
        V_Op = true;
        resultat_actiu = false;
    }

    public void escriureOperands(){   //VA BÉ

        if(anterior_V || resultat_actiu) {                             //a la primera no et deixarà, guai

            if(cont > 0) {

                if (!(tot_unperun.get(cont - 1) == "*" || tot_unperun.get(cont - 1) == "/" || tot_unperun.get(cont - 1) == "." || tot_unperun.get(cont - 1) == "+" || tot_unperun.get(cont - 1) == "-")) {

                    V_Op = false;
                    if (cont != 0) pantalla.append(valor);
                    cont_operadors++;                       //a la seguent posicio
                    resultat_actiu = false;
                    operacions.add(cont_operadors, valor);
                    tot_unperun.add(cont, valor);

                    cont_operadors++;
                    cont++;                                 //cada cop q escrius un simbol

                    //lo seguent que vagi (que ha d ser numero) es guardara a la seguent pos
                }
            }
        }

        else araNoToca();
    }

    public void esValor_esOperacio(){  //VA BÉ

        if(V_Op){ //valor
            already_op = false;
            no_escrit = false;
        }
        else { //operand
            already_op = true;
            no_escrit = true;
            coma_pitjada = false;
        }
    }

    public void pulsemComa(){  //VA BÉ

        V_Op = true;

        if(!coma_pitjada){
            coma_pitjada = true;

            if(cont==0) {

                pantalla.setText("0.");
                cont=2;
                pos_coma = cont;
                operacions.add(0,"0.");

                tot_unperun.add(0,"0"); //primer element array
                tot_unperun.add(1,".");

            }

            else {

                if (!anterior_V) { // l'anterior es una operacio, cont ja incrementat

                    pantalla.append("0.");
                    cont = cont + 2;
                    pos_coma = cont;
                    operacions.add(cont_operadors, "0.");
                    tot_unperun.add(cont-2,"0");
                    tot_unperun.add(cont-1,".");


                }
                else {  //si és un valor , cont no canvia (hi ha una dada)

                    cont++;
                    pos_coma = cont;
                    pantalla.append(".");
                    tot_unperun.add(cont-1,".");

                    trans = operacions.get(cont_operadors);
                    conc = trans + valor;

                    operacions.add(cont_operadors, conc);

                }
            }

        }
    }

    public void pulsemAC() {    //VA BÉ

        cont=0;
        cont_operadors=-1;
        pantalla.setText("0");
        V_Op=false;
        coma_pitjada = false;
        already_op = false;
        resultat_actiu = false;
        operacions.clear();
        tot_unperun.clear();
    }

    public void pulsemAns(){   // VA BÉ

        coma_pitjada = true;

        res = Double.toString(resultat);
        resu_int = (int) Double.parseDouble(res);
        res_d_arr = arrodonir(resultat, 3);

        if((resu_int - res_d_arr) == 0) {
            primeraPos = Integer.toString(resu_int);
        }
        else {
            primeraPos = Double.toString(res_d_arr);
        }

        resultat_enString = primeraPos;
        numDigitsAns = resultat_enString.length();

        if(!anterior_V && cont != 0) {

            pantalla.append(resultat_enString);
            operacions.add(cont_operadors, resultat_enString);    //ja actualitzat per l'operand

            for(i = 0; i < numDigitsAns; i++) tot_unperun.add(cont + i, resultat_enString.substring(i,i+1));

            cont = cont + numDigitsAns;

        }

        else {

            cont_operadors = 0;
            operacions.clear();
            operacions.add(cont_operadors,resultat_enString);
            pantalla.setText(resultat_enString);

            for(i = 0; i < numDigitsAns; i++) tot_unperun.add(i, resultat_enString.substring(i, i + 1));

            cont = numDigitsAns;
        }

        V_Op = true;
        anterior_V = true;
    }

    public void pulsemDel(){   //esborrar tota la cela de l'array o un per un?

        if(cont > 0) {

            pantalla.setText(pantalla.getText().subSequence(0, pantalla.getText().length() - 1) + "");

            //si esborro signe o ultim digit de numero abans d'esborrar signe

            if(tot_unperun.get(cont-1).equals("*") || tot_unperun.get(cont-1).equals("/") || tot_unperun.get(cont-1).equals("+") || tot_unperun.get(cont-1).equals("-") || tot_unperun.get(cont-2).equals("*") || tot_unperun.get(cont-2).equals("/") || tot_unperun.get(cont-2).equals("+") || tot_unperun.get(cont-2).equals("-")) {
                operacions.remove(cont_operadors);
                cont_operadors--;
            }
            else {   //esborrar un digit

                int longitud_operand = operacions.get(cont_operadors).length()-1;
                String laParaulaenConcret = operacions.get(cont_operadors);
                laParaulaenConcret = laParaulaenConcret.substring(0,longitud_operand);
                operacions.add(cont_operadors, laParaulaenConcret);
            }

            tot_unperun.remove(cont-1);
            cont--;
            if (cont <= pos_coma) coma_pitjada = false;

        }

        else pulsemAC();
    }

    public void pulsemIgual(){

        //si pitja i lultim és operacio q surti missatge, facil agafant ultima varaible
        if(anterior_V) {

            V_Op = true;
            coma_pitjada = false;
            already_op = false;
            cont = Double.toString(resultat).length();
            cont_prioritat = 0;
            cont_sumes = 0;


            for (i = 0; i < operacions.size(); i++) {     //contem quantes op de prioritat hi ha

                if (operacions.get(i).equals("*") || operacions.get(i).equals("/")) cont_prioritat++;
                if (operacions.get(i).equals("-") || operacions.get(i).equals("+")) cont_sumes++;

            }

            for (i = 0; i < cont_operadors; i++) {

                if ((cont_prioritat > 0) && (operacions.get(i).equals("*") || operacions.get(i).equals("/"))) {


                    if (operacions.get(i).equals("*")) temp = Double.parseDouble(operacions.get(i - 1)) * Double.parseDouble(operacions.get(i + 1));

                    else {    //estem en divisio

                        if (operacions.get(i + 1).equals("0")) {
                            if (operacions.get(i - 1).equals("0")) ep_infinit = true;
                            else {
                                myDatabaseHelper.addNewNoti(username, "x/0");
                                pantalla.setText("Infinit!!");
                                if(toastOn) Toast.makeText(getApplicationContext(), "El límit ens donaria infinit!", Toast.LENGTH_LONG).show();
                                else notificacio3();
                            }
                        }
                        else temp = Double.parseDouble(operacions.get(i - 1)) / Double.parseDouble(operacions.get(i + 1));
                    }

                    operacions.add(i - 1, Double.toString(temp));
                    cont_prioritat--;
                    cont_operadors = cont_operadors - 2;


                    operacions.remove(i);       //primer operand
                    operacions.remove(i);      //2n operand
                    operacions.remove(i);

                    i = 0;

                }

            }


            if(cont_operadors!=0){

                for (i = 0; i <= cont_operadors; i++) {

                    if ((cont_sumes > 0) && (operacions.get(i).equals("+") || operacions.get(i).equals("-"))) { //sumes o restes

                        if (operacions.get(i).equals("+")) temp = Double.parseDouble(operacions.get(i - 1)) + Double.parseDouble(operacions.get(i + 1));

                        else temp = Double.parseDouble(operacions.get(i - 1)) - Double.parseDouble(operacions.get(i + 1));


                        operacions.add(i - 1, Double.toString(temp));
                        cont_prioritat--;
                        cont_operadors = cont_operadors - 2;


                        operacions.remove(i);       //primer operand
                        operacions.remove(i);      //2n operand
                        operacions.remove(i);

                        i = 0;

                    }
                }
            }

            resultat = Double.parseDouble(operacions.get(0));
            int res_int = (int) Double.parseDouble(operacions.get(0));
            Double res_doub = resultat;
            Double res_double_arrodonit = arrodonir(res_doub, 3);

            esborrarAssignar(res_int, res_double_arrodonit);
        }
    }

    public void esborrarAssignar(int res_int, Double res_doub) {

        V_Op = true;

        if((res_int - res_doub) == 0) { // tindrem un int
            primeraPos = Integer.toString(res_int);
        }
        else { //tindrem un double
            primeraPos = Double.toString(res_doub);
        }

        pulsemAC();
        cont_operadors = 0;
        resultat_actiu = true;

        for (i = 0; i < primeraPos.length(); i++) {

            cont++;
            tot_unperun.add(i, primeraPos.substring(i, i + 1));

        }

        pantalla.setText(primeraPos);
        operacions.add(0,primeraPos);

        if(ep_infinit) notificacioInfinit();

    }

    public double arrodonir(double valorInicial, int num_Decimals){


            Double res_a, partEntera;
            res_a = valorInicial;
            partEntera = Math.floor(res_a);
            res_a = (res_a-partEntera)*Math.pow(10, num_Decimals);
            res_a = Double.valueOf(Math.round(res_a));
            res_a = (res_a/Math.pow(10, num_Decimals))+partEntera;
            return res_a;

    }

    public void notificacioInfinit(){

        pantalla.setText("Però què fas boig!?");
        myDatabaseHelper.addNewNoti(username, "0/0");
        if(toastOn) Toast.makeText(getApplicationContext(), "Què passaria? Pitja el botó per saber-ho!", Toast.LENGTH_LONG).show();
        else notificacio();
        ep_infinit = false;
    }

    public void araNoToca(){
        myDatabaseHelper.addNewNoti(username, "+ / * -");
        if(toastOn) Toast.makeText(getApplicationContext(), "Ja saps que ara no toca ;)", Toast.LENGTH_LONG).show();
        else notificacio2();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //falten els diferents casos

        Intent i;

        settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        switch (item.getItemId()) {
            case R.id.telefon:
                i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:670583366"));
                startActivity(i);
                return true;

            case R.id.navegador:
                Uri uri = Uri.parse("http://www.google.com");
                i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
                return true;

            case R.id.settings_estat:
                toastOn = false;
                editor.putBoolean("myToast", false);
                //editor.putInt("myItem", R.id.settings_estat);
                editor.apply();
                item.setChecked(true);
                return true;

            case R.id.settings_toast:
                toastOn = true;
                editor.putBoolean("myToast", true);
                //editor.putInt("myItem", R.id.settings_toast);
                editor.apply();
                item.setChecked(true);
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        valor = savedInstanceState.getString("valor1");
        cont = savedInstanceState.getInt("conta");
        pos_coma = savedInstanceState.getInt("pos_coma1");
        cont_prioritat = savedInstanceState.getInt("cont_prior1");
        cont_operadors = savedInstanceState.getInt("cont_opera1");
        cont_sumes = savedInstanceState.getInt("cont_sumes1");
        temp = savedInstanceState.getDouble("temp1");
        resultat = savedInstanceState.getDouble("resultat1");
        operacions = savedInstanceState.getStringArrayList("operacions1");
        tot_unperun = savedInstanceState.getStringArrayList("tot_unperun1");

        coma_pitjada = savedInstanceState.getBoolean("coma_p");
        ep_infinit = savedInstanceState.getBoolean("ep_inf");
        V_Op = savedInstanceState.getBoolean("Vo_p");
        already_op = savedInstanceState.getBoolean("alt_op");
        no_escrit = savedInstanceState.getBoolean("no_esc");
        resultat_actiu = savedInstanceState.getBoolean("res_act");
        anterior_V = savedInstanceState.getBoolean("ant_V");
        //res_doub = savedInstanceState.getDouble("res_d");
        //res_double_arrodonit = savedInstanceState.getDouble("res_d_A");
        primeraPos = savedInstanceState.getString("1apos");
        username = savedInstanceState.getString("usern");

        conca = savedInstanceState.getString("conc");
        primera_dada = savedInstanceState.getString("1ad");
        conc = savedInstanceState.getString("cc");
        trans = savedInstanceState.getString("tra");

        res = savedInstanceState.getString("res");
        resultat_enString = savedInstanceState.getString("res_S");
        resu_int = savedInstanceState.getInt("res_int");
        numDigitsAns = savedInstanceState.getInt("nDA");
        //res_ints = savedInstanceState.getInt("res_in");

        //partEntera = savedInstanceState.getDouble("pE");
        //res_a = savedInstanceState.getDouble("resa");

        String resaa = savedInstanceState.getString("pantalla1");
        pantalla.setText(resaa);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("pantalla1", pantalla.getText().toString());
        outState.putString("valor1", valor);
        outState.putInt("conta", cont);
        outState.putInt("pos_coma1", pos_coma);
        outState.putInt("cont_prior1", cont_prioritat);
        outState.putInt("cont_opera1", cont_operadors);
        outState.putInt("cont_sumes1", cont_sumes);
        outState.putDouble("temp1", temp);
        outState.putDouble("resultat1", resultat);
        outState.putStringArrayList("operacions1", operacions);
        outState.putStringArrayList("tot_unperun1", tot_unperun);

        outState.putBoolean("coma_p", coma_pitjada);
        outState.putBoolean("ep_inf", ep_infinit);
        outState.putBoolean("Vo_p", V_Op);
        outState.putBoolean("alt_op", already_op);
        outState.putBoolean("no_esc", no_escrit);
        outState.putBoolean("res_act", resultat_actiu);
        outState.putBoolean("ant_V", anterior_V);
        //outState.putDouble("res_d", res_doub);
        //outState.putDouble("res_d_A", res_double_arrodonit);
        outState.putString("1apos", primeraPos);
        outState.putString("usern", username);

        outState.putString("conc", conca);
        outState.putString("1ad", primera_dada);
        outState.putString("tra", trans);
        outState.putString("cc", conc);

        outState.putString("res", res);
        outState.putString("res_S", resultat_enString);
        outState.putInt("res_int", resu_int);
        outState.putInt("nDA", numDigitsAns);
        //outState.putInt("res_in", res_ints);

        //outState.putDouble("resa", res_a);
        //outState.putDouble("pE", partEntera);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        anterior_V = V_Op;
        if(resultat_actiu) anterior_V = true;

        switch(v.getId()){

            case R.id.num_0: valor="0"; escriureValors(); break;
            case R.id.num_1: valor="1"; escriureValors(); break;
            case R.id.num_2: valor="2"; escriureValors(); break;
            case R.id.num_3: valor="3"; escriureValors(); break;
            case R.id.num_4: valor="4"; escriureValors(); break;
            case R.id.num_5: valor="5"; escriureValors(); break;
            case R.id.num_6: valor="6"; escriureValors(); break;
            case R.id.num_7: valor="7"; escriureValors(); break;
            case R.id.num_8: valor="8"; escriureValors(); break;
            case R.id.num_9: valor="9"; escriureValors(); break;

            case R.id.num_ans: valor ="ans"; pulsemAns(); break;
            case R.id.num_coma: valor = "."; pulsemComa(); break;

            case R.id.num_div: valor ="/"; escriureOperands(); break;
            case R.id.num_suma: valor="+"; escriureOperands(); break;
            case R.id.num_mult: valor ="*"; escriureOperands(); break;
            case R.id.num_resta: valor="-"; escriureOperands(); break;
            case R.id.num_igual: valor="="; pulsemIgual(); break;

            case R.id.num_Del: valor="del"; pulsemDel(); break;
            case R.id.num_AC: valor="AC"; pulsemAC(); break;
        }

        esValor_esOperacio();

    }

    public void notificacio() {

    int mId = 1;

    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.android)
                    .setContentTitle("0/0 ?? ")
                    .setContentText("Si vols saber què passa pitja el botó que tens al costat de AC");


    Intent resultIntent = new Intent(getApplicationContext(), Calculadora.class);

    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

    stackBuilder.addParentStack(Calculadora.class);
    stackBuilder.addNextIntent(resultIntent);

    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

    mBuilder.setContentIntent(resultPendingIntent);
    mNotificationManager.notify(mId, mBuilder.build());

    }

    public void notificacio2() {

        int mId = 1;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.android)
                        .setContentTitle("Error d'escriptura")
                        .setContentText("No pots escriure 2 signes d'operació junts perquè no tindria sentit. " +
                                "Si vols fer una multiplicació amb un nombre negatiu, primer escriu aquest nombre " +
                                "i utilitza l'ANS per multiplicar-lo pel següent.");


        Intent resultIntent = new Intent(getApplicationContext(), Calculadora.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

        stackBuilder.addParentStack(Calculadora.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(mId, mBuilder.build());

    }

    public void notificacio3() {

        int mId = 1;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.android)
                        .setContentTitle("Divisió entre 0")
                        .setContentText("Quan dividim entre zero estem fent el numerador infinitament gran, si en fem el límit podem dir que tenideix a infinit..");


        Intent resultIntent = new Intent(getApplicationContext(), Calculadora.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

        stackBuilder.addParentStack(Calculadora.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(mId, mBuilder.build());

    }

}

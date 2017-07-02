package viladrich.arnau.final_project.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import viladrich.arnau.final_project.BaseActivity;
import viladrich.arnau.final_project.R;


public class Calculadora extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    Button b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b_del, b_AC, b_suma, b_resta, b_multi, b_divisio, b_coma, b_equal, b_mofa, b_ans;
    TextView pantalla;
    int cont, pos_coma, cont_prioritat=0, cont_operadors, i, cont_sumes;
    Double temp=0.0, resultat;
    String valor;
    View layout;

    public ArrayList<String> operacions = new ArrayList<>();
    public ArrayList<String> tot_unperun = new ArrayList<>();

    Boolean coma_pitjada=false, ep_infinit=false, V_Op, already_op=false, no_escrit=true, resultat_actiu=false, anterior_V; //valor true, operacio false


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        setTitle("Calculadora");
        setItemChecked();

        resultat=0.0;
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
        pulsemAC();

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


        V_Op = true;
        resultat_actiu = false;
        tot_unperun.add(cont,valor);


        if(cont==0) pantalla.setText(valor);
        else pantalla.append(valor);

        if(cont_operadors == -1) cont_operadors = 0;

        if(!anterior_V) { //cada cop q l'anterior sigui variable ho guardi a al seguent

            operacions.add(cont_operadors, valor);
        }

        else { //l'anterior és varaible, cal concatenar

            String primera_dada = operacions.get(cont_operadors);
            String conca = primera_dada + valor;

            operacions.add(cont_operadors, conca);

        }

        cont++;  //cada cop que s'escriu alguna cosa sumes 1
    }

    public void escriureOperands(){   //VA BÉ


        Log.v(TAG, Integer.toString(cont));

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

                    String primera_dada = operacions.get(cont_operadors);
                    String conca = primera_dada + valor;

                    operacions.add(cont_operadors, conca);

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
        int numDigitsAns = Double.toString(resultat).length();    //numero digits
        String resultat_enString = Double.toString(resultat);

        if(!anterior_V && cont!=0) {

            pantalla.append(resultat_enString);
            operacions.add(cont_operadors, resultat_enString);    //ja actualitzat per l'operand

            for(i = 0; i < numDigitsAns; i++) tot_unperun.add(cont + i, resultat_enString.substring(i,i+1));

            cont = cont + numDigitsAns;

        }

        else {

            cont_operadors=0;
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

                        if (operacions.get(i - 1).equals("0") && operacions.get(i + 1).equals("0")) ep_infinit = true;

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
            resultat = arreglarResultat();

            esborrarAssignar();
        }
    }

    public Double arreglarResultat(){

        V_Op = true;
        return resultat;
    }


    public void esborrarAssignar() {

        String primeraPos = Double.toString(resultat);
        pulsemAC();
        cont_operadors=0;
        resultat_actiu = true;

        for (i = 0; i < primeraPos.length(); i++) {

            cont++;
            tot_unperun.add(i, primeraPos.substring(i, i + 1));

        }

        pantalla.setText(primeraPos);
        operacions.add(0,primeraPos);

        if(ep_infinit) notificacioInfinit();

    }

    protected static double arrodonir(Double a){

        double gas = Math.round(a);

        if(Double.toString(gas).length()>9) gas = Math.round(a*100)/100;
        return gas;
    }


    public void notificacioInfinit(){

        Log.v(TAG,"not?");
        pantalla.setText("Però què fas boig!?");
        Toast.makeText(getApplicationContext(), "Potser farà un pet!", Toast.LENGTH_LONG).show();
        ep_infinit = false;
        //caldria fer saltar un fil
        //posarhi una notificacio
    }


    public void araNoToca(){
        Toast.makeText(getApplicationContext(), "Ja saps que ara no toca ;)", Toast.LENGTH_LONG).show();
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

}

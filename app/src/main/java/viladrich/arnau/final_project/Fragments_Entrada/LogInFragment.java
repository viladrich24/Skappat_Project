package viladrich.arnau.final_project.Fragments_Entrada;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import viladrich.arnau.final_project.Activities.UserProfile;
import viladrich.arnau.final_project.Database.MyDatabaseHelper;
import viladrich.arnau.final_project.R;

import static utils.Constants.PREFS_NAME;

public class LogInFragment extends Fragment {

    Button logIn_dins, pass_forget;
    EditText et_username_login, et_password_login;
    MyDatabaseHelper myDatabaseHelper;

    public LogInFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_log_in, container, false);
        myDatabaseHelper = MyDatabaseHelper.getInstance(getContext());

        logIn_dins = (Button) rootview.findViewById(R.id.boto_login_dins);
        pass_forget = (Button) rootview.findViewById(R.id.boto_pass_forget);

        et_username_login = (EditText) rootview.findViewById(R.id.username_enter_login);
        et_password_login = (EditText) rootview.findViewById(R.id.password_enter_login);


        pass_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_username_login.equals("") || et_password_login.equals("")) Toast.makeText(getContext(), "Introdueix el teu nom d'usuari i numero de telefon o email!", Toast.LENGTH_LONG).show();

                else{
                    int emailWell = myDatabaseHelper.queryPasswordMail(et_username_login.getText().toString(), et_password_login.getText().toString());
                    int phoneWell = myDatabaseHelper.queryPasswordPhone(et_username_login.getText().toString(), et_password_login.getText().toString());

                    if(emailWell != -1 || phoneWell != -1) {

                        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();

                        editor.putBoolean("myBoolean", true);
                        editor.apply();

                        Intent i = new Intent(getContext(), UserProfile.class);
                        startActivity(i);

                        getActivity().finishActivity(0);
                    }
                    else {
                        Toast.makeText(getContext(), "Aquestes dades no són correctes!", Toast.LENGTH_LONG).show();
                        et_password_login.setText("");
                        et_username_login.setText("");
                    }
                }
            }
        });


        logIn_dins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!et_username_login.equals("") && !et_password_login.equals("")) {

                    int login_exist = myDatabaseHelper.queryRowExist(et_username_login.getText().toString());

                    if (login_exist != -1) { // si existeix, mirem si coincideix el password

                        int login_match = myDatabaseHelper.queryRowMatch(et_username_login.getText().toString(), et_password_login.getText().toString());

                        //exisiteix i ha posat bé la contrasenya
                        if (login_match != -1) {

                            SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();

                            Log.v("TAG", "user login: "+et_username_login.getText().toString());

                            editor.putString("myActualUser", et_username_login.getText().toString());
                            editor.putBoolean("myBoolean", true);
                            editor.apply();

                            Intent i = new Intent(getContext(), UserProfile.class);
                            startActivity(i);

                            getActivity().finishActivity(0);

                        } else{
                            Toast.makeText(getContext(), "Contrasenya errònia!", Toast.LENGTH_LONG).show();
                            et_password_login.setText("");
                        }
                    } else {
                        Intent i = new Intent(getActivity().getApplicationContext(), Expulsat.class);
                        startActivity(i);
                    }
                }else
                    Toast.makeText(getContext(), "Cal introduir totes les dades!", Toast.LENGTH_LONG).show();
            }


        });


        return rootview;
    }

}

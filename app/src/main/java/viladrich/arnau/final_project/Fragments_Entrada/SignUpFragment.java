package viladrich.arnau.final_project.Fragments_Entrada;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import viladrich.arnau.final_project.Database.MyDatabaseHelper;
import viladrich.arnau.final_project.R;


public class SignUpFragment extends Fragment {


    Button signIn_dins, delete_all;
    ImageButton twitter, facebook;
    EditText et_password, et_mail, et_user, et_phone;
    MyDatabaseHelper myDatabaseHelper;


    public SignUpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_sign_in, container, false);

        myDatabaseHelper = MyDatabaseHelper.getInstance(getActivity());

        facebook = (ImageButton) rootview.findViewById(R.id.facebook);
        twitter = (ImageButton) rootview.findViewById(R.id.twitter);

        signIn_dins = (Button) rootview.findViewById(R.id.boto_sign_in_dins);
        delete_all = (Button) rootview.findViewById(R.id.boto_esborrar_tot);

        et_mail = (EditText) rootview.findViewById(R.id.mail_enter);
        et_user = (EditText) rootview.findViewById(R.id.username_enter);
        et_password = (EditText) rootview.findViewById(R.id.password_enter);
        et_phone = (EditText) rootview.findViewById(R.id.phone_number_enter);


        facebook.setOnClickListener(new View.OnClickListener() { //es google
            @Override
            public void onClick(View v) {

            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Not available untill v2.0!", Toast.LENGTH_LONG).show();
            }
        });



        signIn_dins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(et_user.getText().equals("") || et_mail.getText().equals("") || et_password.getText().equals("") || et_phone.getText().equals(""))) {

                    int alreadyExisit = myDatabaseHelper.queryRowExist(et_user.getText().toString());

                    if (alreadyExisit == -1) {  // si no hi ha cap usuari amb aquest nom, el crees amb totes les dades

                        long x = myDatabaseHelper.createRow(et_user.getText().toString(), et_password.getText().toString(), et_mail.getText().toString(), et_phone.getText().toString());
                        Toast.makeText(getContext(), "Enhorabona, t'has registrat amb èxit! \n(user number: " + x + ")", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getContext(), "Aquest usuari ja existeix!", Toast.LENGTH_LONG).show();

                    et_user.setText("");
                    et_mail.setText("");
                    et_phone.setText("");
                    et_password.setText("");
                }
                else Toast.makeText(getContext(), "Cal posar totes les dades!", Toast.LENGTH_LONG).show();
            }

            //esborrar els text edit
        });


        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Potser farà un pet!", Toast.LENGTH_LONG).show();
            }
        });

        return rootview;
    }
}

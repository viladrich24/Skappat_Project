package viladrich.arnau.final_project.Fragments_Entrada;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import viladrich.arnau.final_project.Database.MyDatabaseHelper;
import viladrich.arnau.final_project.R;


public class SignUpFragment extends Fragment {


    Button signIn_dins;
    EditText et_password, et_mail, et_user, et_phone;
    MyDatabaseHelper myDatabaseHelper;

    /*private final static String TAG = "_MAIN_";
    private static final int RC_SIGN_IN = 9001;

    protected FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleApiClient mGoogleApiClient;
    private SignInButton mSignInButton;

    private TextView userInfoDisplay;*/


    public SignUpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_sign_in, container, false);

        myDatabaseHelper = MyDatabaseHelper.getInstance(getActivity());

        signIn_dins = (Button) rootview.findViewById(R.id.boto_sign_in_dins);

        et_mail = (EditText) rootview.findViewById(R.id.mail_enter);
        et_user = (EditText) rootview.findViewById(R.id.username_enter);
        et_password = (EditText) rootview.findViewById(R.id.password_enter);
        et_phone = (EditText) rootview.findViewById(R.id.phone_number_enter);

        /*initFirebaseAuth();

        initUIComponents();

        initGoogleLogin();*/

        signIn_dins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(et_user.getText().equals("") || et_mail.getText().equals("") || et_password.getText().equals("") || et_phone.getText().equals(""))) {

                    int alreadyExisit = myDatabaseHelper.queryRowExist(et_user.getText().toString());

                    if (alreadyExisit == -1) {  // si no hi ha cap usuari amb aquest nom, el crees amb totes les dades

                        long x = myDatabaseHelper.createRow(et_user.getText().toString(), et_password.getText().toString(), et_mail.getText().toString(), et_phone.getText().toString());
                        myDatabaseHelper.addRegistrationNumber(et_user.getText().toString(), Long.toString(x));
                        Toast.makeText(getContext(), "Enhorabona, t'has registrat amb èxit! \nUser number: " + x, Toast.LENGTH_LONG).show();
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

        return rootview;
    }

    /*private void initUIComponents() {

        mSignInButton = (SignInButton) getActivity().findViewById(R.id.sign_in_button);
        userInfoDisplay = (TextView) getActivity().findViewById(R.id.userInfoDisplay);

        mSignInButton.setOnClickListener((View.OnClickListener) this);

    }

    private void initFirebaseAuth() {

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    updateUI();
                }
                else{
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    userInfoDisplay.setText("No user");
                }
            }
        };

    }

   /* private void initGoogleLogin(){

        //https://developers.google.com/identity/sign-in/android/sign-in

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

       /* mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this !!FragmentActivity , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getActivity().getApplicationContext(),"Connection failed",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }*/
/*
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser u = mAuth.getCurrentUser();
        updateUI();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onClick(View v) {

        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        switch (v.getId()){
            case R.id.sign_in_button:
                signInGoogle();
                break;
        }
    }

    private void loginUser(String email, String password){
        //https://firebase.google.com/docs/auth/android/start/
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity().getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void registerUser(String email, String password){
        //https://firebase.google.com/docs/auth/android/start/
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInGoogle(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
            updateUI();
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //https://firebase.google.com/docs/auth/android/google-signin?utm_source=studio
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_item_logout:
                mAuth.signOut();
                Toast.makeText(getApplicationContext(),"Log out",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void updateUI(){
        FirebaseUser u= mAuth.getCurrentUser();
        if (u != null ) userInfoDisplay.setText(u.getEmail());
    }*/

}

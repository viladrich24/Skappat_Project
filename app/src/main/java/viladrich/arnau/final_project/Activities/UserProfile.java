package viladrich.arnau.final_project.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import viladrich.arnau.final_project.Activities.gallery.PermissionUtils;
import viladrich.arnau.final_project.BaseActivity;
import viladrich.arnau.final_project.Database.MyDatabaseHelper;
import viladrich.arnau.final_project.GPS_Files.GPSActivity;
import viladrich.arnau.final_project.R;

import static utils.Constants.PREFS_NAME;

public class UserProfile extends BaseActivity implements View.OnClickListener {

    ImageView fotoPerfil;
    Bitmap originalBitmap;
    Drawable originalDrawable;
    MyDatabaseHelper myDatabaseHelper;
    TextView navBarMiniText, navBarUser;
    RoundedBitmapDrawable roundedDrawable;
    Button localitzacio, galleryFoto, takeFoto, editPhone, editMail;
    TextView tvUser, rankPos, timePos, phoneInfo, mailInfo, lastNoti;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_MANAGE_DOCUMENTS = 2;
    SharedPreferences sp;
    ImageView image;
    String username;
    private boolean canWeRead = false;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle("Profile");
        setItemChecked();

        sp = getSharedPreferences("galleryexample", Context.MODE_PRIVATE);

        ///image = (ImageView) findViewById(R.id.imageView);

        canWeRead = canWeRead();
        if (canWeRead) {
            loadImageFromString(sp.getString("imagePath", null));
        }

        myDatabaseHelper = MyDatabaseHelper.getInstance(getApplicationContext());

        tvUser = (TextView) findViewById(R.id.tv_user);
        rankPos = (TextView) findViewById(R.id.pos_rank);
        timePos = (TextView) findViewById(R.id.pos_time);
        phoneInfo = (TextView) findViewById(R.id.phone_info);
        mailInfo = (TextView) findViewById(R.id.mail_info);
        lastNoti = (TextView) findViewById(R.id.last_notificacio);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        username = settings.getString("myActualUser", "usuari");

        String record = myDatabaseHelper.queryMemory(username);
        String recordTime = myDatabaseHelper.queryTime(username);
        String phoneDB = myDatabaseHelper.queryPhone(username);
        String mailDB = myDatabaseHelper.queryMail(username);
        String notiDB = myDatabaseHelper.queryNoti(username);
        String imageDB = myDatabaseHelper.queryImage(username);

        tvUser.setText(username);
        rankPos.setText(Html.fromHtml("<b>Best score: </b>" + record));
        timePos.setText(Html.fromHtml("<b>Time: </b>" + recordTime));
        phoneInfo.setText(Html.fromHtml("<b>Phone: </b>" + phoneDB));
        mailInfo.setText(Html.fromHtml("<b>E-mail: </b>" + mailDB));
        lastNoti.setText(Html.fromHtml("<b>Last noti: </b>" + notiDB));

        fotoPerfil = (ImageView) findViewById(R.id.imageView_usuari);

        localitzacio = (Button) findViewById(R.id.boto_localitzacio);
        editPhone = (Button) findViewById(R.id.boto_editar_phone);
        editMail = (Button) findViewById(R.id.boto_editar_mail);
        galleryFoto = (Button) findViewById(R.id.boto_galeria);
        takeFoto = (Button) findViewById(R.id.boto_camera);

        localitzacio.setOnClickListener(this);
        editPhone.setOnClickListener(this);
        editMail.setOnClickListener(this);
        takeFoto.setOnClickListener(this);
        galleryFoto.setOnClickListener(this);

        originalDrawable = getResources().getDrawable(R.drawable.linx, null);
        originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
        roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());

        fotoPerfil.setImageDrawable(getDrawable(Integer.parseInt(imageDB)));

        View navHeaderView = navigationView.getHeaderView(0);
        navBarMiniText = (TextView) navHeaderView.findViewById(R.id.miniTextNavBar);
        navBarUser = (TextView) navHeaderView.findViewById(R.id.nomUsuariHeaderBar);

        navBarUser.setText(" > " + username);

    }

    private boolean canWeRead(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED;
    }

    private void loadImageFromString (String imagePath){
        if(imagePath != null){
            Uri imageUri = Uri.parse(imagePath);
            loadImageFromUri(imageUri);
        }
    }

    private void loadImageFromUri(Uri imageUri) {
        try {
            image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Intent getContentIntent() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT <19){
            intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        return intent;
    }

    public void funcioMail(){

        View view = LayoutInflater.from(UserProfile.this).inflate(R.layout.activity_dialog_notification, null);

        final EditText mail = (EditText) view.findViewById(R.id.new_mail);

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);

        builder.setView(view);
        builder.setTitle("Vols modificar la teva adreça?");
        builder.setMessage("\n Escriu el nou correu:");


        builder.setPositiveButton("Done!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ss = mail.getText().toString();
                myDatabaseHelper.addNewMail(username, ss);
                mailInfo.setText(Html.fromHtml("<b>E-mail: </b> " +ss));
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);

        Dialog dialog = builder.create();
        dialog.show();
    }

    public void funcioPhone(){

        View view = LayoutInflater.from(UserProfile.this).inflate(R.layout.activity_dialog_notification, null);

        final EditText mail = (EditText) view.findViewById(R.id.new_mail);

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);

        builder.setView(view);
        builder.setTitle("Vols modificar el teu número de telèfon?");
        builder.setMessage("\n Escriu-lo aquí:");


        builder.setPositiveButton("Done!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ss = mail.getText().toString();
                myDatabaseHelper.addNewPhone(username, ss);
                phoneInfo.setText(Html.fromHtml("<b>Phone: </b> " +ss));
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);

        Dialog dialog = builder.create();
        dialog.show();
    }

    public void funcioLocalitzacio() {
        Intent i = new Intent(getApplicationContext(), GPSActivity.class);
        startActivity(i);
    }

    public void funcioCamera(){

        PermissionUtils.checkReadExternalStoragePermissions(activity,MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        Intent pickAnImage = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickAnImage.setType("image/*");

        startActivityForResult(pickAnImage, 2);

        //TODO: el mateix, s'haura d'afegir al database l'id del drawable perquè pugui sortir al ranking i fer servir el setIcon..
    }

    public void funcioGaleria(){

        Intent getIntent = getContentIntent();
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, 3);

        //TODO: s'haura d'afegir al database l'id del drawable perquè pugui sortir al ranking
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Como en este caso los 3 intents hacen lo mismo, si el estado es correcto recogemos el resultado
        //Aún así comprobamos los request code. Hay que tener total control de lo que hace nuestra app.
        if(resultCode == RESULT_OK){
            if(requestCode >= 1 && requestCode <= 3){
                //Líneas extras debido al usar action get content:
                data.getData();
                Uri selectedImage = data.getData();
                String selectedImagePath = selectedImage.toString();

                if(canWeRead && requestCode == 2){
                    Log.v("PICK","Selected image uri" + selectedImage);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("imagePath",selectedImagePath );
                    editor.apply();
                }
                loadImageFromUri(selectedImage);
            }
        }else{
            Log.v("Result","Something happened");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    canWeRead = true;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    canWeRead = false;
                }
                return;
            }
            case  MY_PERMISSIONS_REQUEST_MANAGE_DOCUMENTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    canWeRead = true;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    canWeRead = false;
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected int whatIsMyId() {
        return R.id.perfil;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boto_galeria: funcioGaleria(); break;
            case R.id.boto_camera: funcioCamera(); break;
            case R.id.boto_editar_mail: funcioMail(); break;
            case R.id.boto_editar_phone: funcioPhone(); break;
            case R.id.boto_localitzacio: funcioLocalitzacio(); break;

        }
    }

}



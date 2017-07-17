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
import android.graphics.BitmapFactory;
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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
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
    TextView tvUser, rankPos, timePos, phoneInfo, mailInfo, lastNoti, infoExtra;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_MANAGE_DOCUMENTS = 2;
    SharedPreferences sp;
    Bitmap bitMapImage;
    String username, imagePathLink;
    private boolean canWeRead = false;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle("Profile");
        setItemChecked();

        sp = getSharedPreferences("galleryexample", Context.MODE_PRIVATE);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        username = settings.getString("myActualUser", "usuari");

        myDatabaseHelper = MyDatabaseHelper.getInstance(getApplicationContext());

        tvUser = (TextView) findViewById(R.id.tv_user);
        rankPos = (TextView) findViewById(R.id.pos_rank);
        timePos = (TextView) findViewById(R.id.pos_time);
        phoneInfo = (TextView) findViewById(R.id.phone_info);
        mailInfo = (TextView) findViewById(R.id.mail_info);
        infoExtra = (TextView) findViewById(R.id.info_extra_user);
        lastNoti = (TextView) findViewById(R.id.last_notificacio);
        fotoPerfil = (ImageView) findViewById(R.id.imageView_usuari);

        String record = myDatabaseHelper.queryMemory(username);
        String recordTime = myDatabaseHelper.queryTime(username);
        String phoneDB = myDatabaseHelper.queryPhone(username);
        String mailDB = myDatabaseHelper.queryMail(username);
        String notiDB = myDatabaseHelper.queryNoti(username);
        String imageDB = myDatabaseHelper.queryImage(username);
        String numDB = myDatabaseHelper.queryRegistrationNumber(username);


        if(!imageDB.equals("null")) {

            Bitmap bm = StringToBitMap(imageDB);
            setImageFromDB(bm);

            //ja està referenciat a fotoPerfil
        }

        else {

            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.user);
            setImageFromDB(icon);
            String aa = BitMapToString(icon);
            myDatabaseHelper.addNewPhoto(username, aa);
        }


        tvUser.setText(username);
        rankPos.setText(Html.fromHtml("<b>Best score: </b>" + record));
        timePos.setText(Html.fromHtml("<b>Time: </b>" + recordTime));
        phoneInfo.setText(Html.fromHtml("<b>Phone: </b>" + phoneDB));
        mailInfo.setText(Html.fromHtml("<b>E-mail: </b>" + mailDB));
        lastNoti.setText(Html.fromHtml("<b>Last noti: </b>" + notiDB));
        infoExtra.setText("Active | User number: " + numDB);

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

        View navHeaderView = navigationView.getHeaderView(0);
        navBarMiniText = (TextView) navHeaderView.findViewById(R.id.miniTextNavBar);
        navBarUser = (TextView) navHeaderView.findViewById(R.id.nomUsuariHeaderBar);

        navBarUser.setText(" > " + username);

        canWeRead = canWeRead();

        if (canWeRead) loadImageFromString(sp.getString("imagePath", null));

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

            bitMapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            fotoPerfil.setImageBitmap(bitMapImage);

            imagePathLink = BitMapToString(bitMapImage);
            myDatabaseHelper.addNewPhoto(username, imagePathLink);
            //guardar el bitMap

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setImageFromDB(Bitmap bm){
        fotoPerfil.setImageBitmap(bm);
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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

    //funcions botons

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

    public void funcioGaleria(){

        Intent getImageAsContent = getContentIntent();
        getImageAsContent.setType("image/*");
        startActivityForResult(getImageAsContent, 1);
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            if(requestCode >= 1 && requestCode <= 3){

                data.getData();
                Uri selectedImage = data.getData();
                String selectedImagePath = selectedImage.toString();

                if(canWeRead && requestCode == 2){

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("imagePath", selectedImagePath);
                    editor.apply();
                }

                loadImageFromUri(selectedImage);
            }

        }else Log.v("TAG","Something happened");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) canWeRead = true;
                else canWeRead = false;

                return;
            }

            case  MY_PERMISSIONS_REQUEST_MANAGE_DOCUMENTS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) canWeRead = true;
                else canWeRead = false;

                return;
            }
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



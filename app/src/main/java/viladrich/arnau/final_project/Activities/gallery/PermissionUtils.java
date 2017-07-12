package viladrich.arnau.final_project.Activities.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {

    private static void checkPermission (Activity activity, String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {}
            else ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        }
    }

    public static void checkReadExternalStoragePermissions(Activity a, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            checkPermission(a, Manifest.permission.READ_EXTERNAL_STORAGE, requestCode);
        }
    }

    public static void checkManageDocumentsPermissions(Activity a, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            checkPermission(a,Manifest.permission.MANAGE_DOCUMENTS, requestCode);
        }
    }
}



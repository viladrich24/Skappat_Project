package viladrich.arnau.final_project.GPS_Files;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import viladrich.arnau.final_project.R;

public class GPSActivity extends AppCompatActivity {

    private static final String TAG = "GPS ACTIVITY";
    private final int MY_PERMISSION_ACCESS_FINE_LOCATION = 0;

    List<Address> addressList;
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;
    TextView latitude, longitude, mostrarCarrer;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void updateLocation(View v) {
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[] {
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }

        if ( Build.VERSION.SDK_INT >= 23
                &&
                (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                        ||
                        ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED))

        {
            return  ;
        }

        if(lastKnownLocation == null){
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(lastKnownLocation != null){
                showNewLocation(lastKnownLocation);
            }
        }

        if(locationManager != null){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        mostrarCarrer = (TextView) findViewById(R.id.mostrarCarrer);
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);

        // Obtenemos una referencia al locationManager del sistema
        locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        // Definimos un listener que repsonderá a las actualizaciones de localización
        locationListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                Log.d(TAG, "onStatusChanged: ");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG, "onProviderEnabled: ");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "onProviderDisabled: ");
            }

            @Override
            public void onLocationChanged(Location location) {
                latitude.setText("Latitude: " + location.getLatitude());
                longitude.setText("Longitude: " + location.getLongitude());
                showNewLocation(location);
            }

        };

        updateLocation(null);
    }

    private void showNewLocation(Location location) {
        Geocoder gc = new Geocoder(getApplicationContext());
        try {
            //5 mxresults
            addressList = gc.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < addressList.size(); ++i) {
            TextView t = (TextView) findViewById(R.id.mostrarCordenades);
            if (i == 0) t.setText("");
            t.setText(t.getText() + "\n" + addressList.get(i).getAddressLine(0));
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}


package com.example.hikerwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView tvLatitude, tvLongitude, tvAltitude, tvAccuracy, tvAdmin, tvPostalCode, tvThoroughFare;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, locationListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        tvAccuracy = (TextView) findViewById(R.id.tvAccuracy);
        tvAltitude = (TextView) findViewById(R.id.tvAltitude);
        tvAdmin = (TextView) findViewById(R.id.tvAdmin);
        tvPostalCode = (TextView) findViewById(R.id.tvPostalCode);
        tvThoroughFare = (TextView) findViewById(R.id.tvThoroughfare);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, locationListener);
        }
    }

    public void updateLocationInfo(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        tvLatitude.setText("Latitude: " + Double.toString(latitude));
        tvLongitude.setText("Longitude: " + Double.toString(longitude));
        tvAccuracy.setText("Accuracy: " + Double.toString(location.getAccuracy()));
        tvAltitude.setText("Altitude: " + Double.toString(location.getAltitude()));
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddress = geocoder.getFromLocation(latitude, longitude, 1);
            if (listAddress!=null && listAddress.size()>0){
                // thoroughfare=16th Street,postalCode=11215,admin=New York
                if (listAddress.get(0).getThoroughfare()!=null)
                    tvThoroughFare.setText(listAddress.get(0).getThoroughfare());
                if (listAddress.get(0).getPostalCode()!=null)
                    tvPostalCode.setText(listAddress.get(0).getPostalCode() + " ");
                if (listAddress.get(0).getAdminArea()!=null)
                    tvAdmin.setText(listAddress.get(0).getAdminArea());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

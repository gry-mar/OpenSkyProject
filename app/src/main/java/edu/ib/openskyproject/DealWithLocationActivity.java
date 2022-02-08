package edu.ib.openskyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import androidx.navigation.Navigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class DealWithLocationActivity extends AppCompatActivity {
    private static final long LOCATION_REFRESH_TIME = 150000;
    private static final float LOCATION_REFRESH_DISTANCE = 500;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected FusedLocationProviderClient fusedLocationProviderClient;
    protected Context context;
    String lat;
    String provider;
    protected double latitude;
    protected double longitude;
    protected boolean gps_enabled, network_enabled;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_with_location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(DealWithLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DealWithLocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(DealWithLocationActivity.this,
                                Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        setLatitude(addresses.get(0).getLatitude());
                        setLongitude(addresses.get(0).getLongitude());
                        System.out.println(addresses.get(0).getLatitude());
                        System.out.println(addresses.get(0).getLongitude());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
//                LOCATION_REFRESH_DISTANCE, mLocationListener);
        Button btnDistance = findViewById(R.id.btnChooseDistance);
        btnDistance.setOnClickListener(this::mapDisplay);
    }

    private double degreeOfLat(double distance) {

        int oneDegreeVal = 111;
        double oneMinLat = 1.85;

        if (distance % oneDegreeVal == 0) {
            return distance / oneDegreeVal;
        } else {
            double r = distance % (double) oneDegreeVal;
            double d = (distance - r) / 111;
            double m = (r / oneMinLat) / 60;
            return d + m;
        }
    }

    public static double degreeOfLon(double distance, double degree) {

        int oneDegreeVal = 111;
        double oneMinLat = 1.85;

        if (distance % oneDegreeVal == 0) {
            return (distance / oneDegreeVal) * Math.cos(Math.toRadians(degree));
        } else {
            double r = distance % (double) oneDegreeVal;
            double d = (distance - r) / 111;
            double m = (r / oneMinLat) / 60;
            return (d + m) * Math.cos(Math.toRadians(degree));
        }
    }

    //private double distanceToLatAndLong(double distance){


//    private final LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(final Location location) {
//            setLatitude(location.getLatitude());
//            setLongitude(location.getLongitude());
//        }
//    };
//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//        setLatitude(location.getLatitude());
//        setLongitude(location.getLongitude());
//        //this.latitude = location.getLatitude();
//        //this.longitude = location.getLongitude();
//    }
//    @Override
//    public void onProviderDisabled(String provider) {
//        Log.d("Latitude","disable");
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//        Log.d("Latitude","enable");
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Log.d("Latitude","status");
//    }

    private void mapDisplay(View view) {
        EditText distanceText = findViewById(R.id.edtDistance);
        double distance = Double.parseDouble(distanceText.getText().toString());
        double decimalDegLat = degreeOfLat(distance);
        double decimalDegLon = degreeOfLon(distance, decimalDegLat);
        String url = "https://opensky-network.org/api/states/all?lamin=" + (getLatitude() - decimalDegLat)
                + "&lomin=" + (getLongitude() - decimalDegLon) + "&lamax=" +
                (getLatitude() + decimalDegLat) + "&lomax=" + (getLongitude() + decimalDegLon);

        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.setArguments(bundle);
        System.out.println(getLongitude());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainerMap, mapsFragment).commit();

//        fragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainerMap, MapsFragment.class, bundle, "tag")
//                .setReorderingAllowed(true)
//                .addToBackStack("name")
//                .commit();
    }

    public void returnMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
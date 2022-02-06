package edu.ib.openskyproject;

import static android.location.LocationManager.*;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import edu.ib.openskyproject.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;

    SupportMapFragment supportMapFragment;

    private FusedLocationProviderClient fusedLocationProviderClient;
    String provider;
    LatLng latLng;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            getCurrentLocation();



    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }else{
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        private LatLng latLng;

                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            this.latLng = new LatLng(location.getLatitude(),location.getLongitude());

                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("You are here");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
                            googleMap.addMarker(options);
                        }
                    });
            }
        }
    });
    }}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==44){
            if(grantResults.length>0 &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

    public void returnMain(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

   public void showFlightsNearby(View view) {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//        }else{
//            Task<Location> task = fusedLocationProviderClient.getLastLocation();
//            task.addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null) {
//                        LatLng latLng;
//                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                        EditText areaText = findViewById(R.id.edtArea);
//                        double areaKm = Double.parseDouble(areaText.toString());
//                        double degrees = distanceToLatAndLong(areaKm);
//                        String url = "https://opensky-network.org/api/states/all?lamin=" + String.valueOf(location.getLatitude() - degrees) +
//                                "&lomin=" + String.valueOf(location.getLongitude() - degrees) + "&lamax=" +
//                                String.valueOf(location.getLatitude() + degrees) + "&lomax=" + String.valueOf(location.getLongitude() + degrees);
//                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                                res -> {
//                                    Results results = gson.fromJson(res.toString(), Results.class);
//
//                                    ArrayList<Flights> flightsArrayList = new ArrayList<>();
//                                    ArrayList<ArrayList> statesList = new ArrayList<>();
//                                    statesList.addAll(results.getStates());
//                                    ArrayList<String> countriesList = new ArrayList<>();
//                                    for (int i = 0; i < statesList.size(); i++) {
//                                        try {
//                                            double latitude = Double.parseDouble(statesList.get(i).get(6).toString());
//                                            double longitude = Double.parseDouble(statesList.get(i).get(5).toString());
//                                            String country = statesList.get(i).get(2).toString();
//                                            flightsArrayList.add(new Flights(longitude, latitude));
//                                            countriesList.add(country);
//                                        } catch (NullPointerException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                    for (int i = 0; i < flightsArrayList.size(); i++) {
//                                        LatLng point = new LatLng(flightsArrayList.get(i).getLatitude(),
//                                                flightsArrayList.get(i).getLongitude());
//                                        MarkerOptions markerOptions = new MarkerOptions().position(point).title("Country: " +
//                                                countriesList.get(i));
//                                        mMap.addMarker(markerOptions);
//
//                                    }
//                                },
//                                error -> {
//                                    System.out.println("Error");
//                                });
//                        queue.add(stringRequest);
//
//
//                    }
//
//                }
//
//                ;
//
//            });
//                        }
                    }


    private double distanceToLatAndLong(double distance){
        int oneDegreeVal = 111;
        int oneMinVal = 2;
        if(distance % oneDegreeVal ==0){
            return distance/oneDegreeVal;
        }else{
            double r = distance % oneDegreeVal;
            double d = (distance - r)/111;
            double m = r/oneMinVal;
            return d+m;
        }
    }

    //    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        String url = "Opensky";
//        LatLng Pwr = new LatLng(51.110650, 17.058135);
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            getCurrentLocation();
//            return;
//        }
//        locationManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);
//
//        mMap.addMarker(new MarkerOptions().position(Pwr).title("You are here"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Pwr));
//
//
//    }
//    private void getCurrentLocation
//
//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//
//    }

}
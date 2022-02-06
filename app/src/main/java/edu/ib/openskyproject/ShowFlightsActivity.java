package edu.ib.openskyproject;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.ib.openskyproject.databinding.ActivityShowFlightsBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.transform.Result;

public class ShowFlightsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityShowFlightsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_flights);
//        binding = ActivityShowFlightsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googlemap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(0, 0);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        RequestQueue queue = Volley.newRequestQueue(this);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        res -> {
                            FullResults fullResults = gson.fromJson(res.toString(), FullResults.class);

                            ArrayList<Flights> flightsArrayList = new ArrayList<>();
                            ArrayList<ArrayList> statesList = new ArrayList<>(fullResults.getStates());
                            ArrayList<String> countriesList = new ArrayList<>();
                            for (int i = 0; i < statesList.size(); i++) {
                                try {
                                    double latitude = Double.parseDouble(statesList.get(i).get(6).toString());
                                    double longitude = Double.parseDouble(statesList.get(i).get(5).toString());
                                    String country = statesList.get(i).get(2).toString();
                                    flightsArrayList.add(new Flights(longitude, latitude));
                                    countriesList.add(country);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                            }
                            for (int i = 0; i < flightsArrayList.size(); i++) {
                                LatLng point = new LatLng(flightsArrayList.get(i).getLatitude(),
                                        flightsArrayList.get(i).getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions().position(point).title("Country: " +
                                        countriesList.get(i));
                                mMap.addMarker(markerOptions);

                            }

                            //System.out.println(result);
                        }, error -> {
                    System.out.println("Error");
                });
                queue.add(stringRequest);

                handler.postDelayed(this::run, 5000);
            }

        };
        handler.postDelayed(runnable, 5000);


    }

    public void showClicked(GoogleMap googleMap){
        mMap = googleMap;
        Intent intent = new Intent(this,)
        EditText edtLatMin = findViewById(R.id.edtLatMin);
        EditText edtLatMax = findViewById(R.id.edtLatMax);
        EditText edtLonMin = findViewById(R.id.edtLongMin);
        EditText edtLonMax = findViewById(R.id.edtLongMax);
        double latMin = Double.parseDouble(edtLatMin.getText().toString());
        double latMax = Double.parseDouble(edtLatMax.getText().toString());
        double lonMin = Double.parseDouble(edtLonMin.getText().toString());
        double lonMax = Double.parseDouble(edtLonMax.getText().toString());

        String url = "https://opensky-network.org/api/states/all?lamin=" + String.valueOf(latMin) + "&lomin" + String.valueOf(lonMin) + "&lamax=" +
                latMax + "&lomax" + lonMax;
        Log.i("LOG PASS", url);

    }
}

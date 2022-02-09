package edu.ib.openskyproject;

import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;

/**
 * Class which enables to find and represent flights on the map in specify boundaries
 */


public class ShowFlightsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_flights);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googlemap);

        mapFragment.getMapAsync(this);
    }

    /**
     * method called when map is ready
     * places appropriate icons on map
     * @param googleMap
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
       String url = (String) getIntent().getStringExtra("passUrl");
            RequestQueue queue = Volley.newRequestQueue(this);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            res -> {
                                Results results = gson.fromJson(res.toString(), Results.class);
                            try {
                                ArrayList<Flights> flightsArrayList = new ArrayList<>();
                                ArrayList<ArrayList> statesList = new ArrayList<>();
                                statesList.addAll(results.getStates());
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
                                    MarkerOptions markerOptions = new MarkerOptions().position(point).title("Origin country: " +
                                            countriesList.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.plane_icon));
                                    mMap.addMarker(markerOptions);
                                }
                            }catch(Exception e){
                                LatLng pointPWR = new LatLng(51.1052862455, 17.055921443);
                                MarkerOptions moPWR = new MarkerOptions().position(pointPWR).title("No flights for that bounds but enjoy PWR :)")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.clown));
                                googleMap.addMarker(moPWR); }
                            }, error -> {
                        System.out.println("Error");
                    });
                    queue.add(stringRequest);
                    handler.postDelayed(this, 5000);
                }
            };
            handler.postDelayed(runnable, 5000);
        }

     /**
     * Return to boundaries view
     * enables to give new boundaries
     * @param view
     */

    public void returnToBounds(View view) {
        Intent intent = new Intent(getApplicationContext(),BoundariesActivity.class);
        startActivity(intent);
    }


}

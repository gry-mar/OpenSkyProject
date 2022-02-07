package edu.ib.openskyproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    SupportMapFragment supportMapFragment;

    private FusedLocationProviderClient fusedLocationProviderClient;
    protected String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
           // String url = getArguments().getString("url");

            System.out.println(getUrl());
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url = getUrl();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            res -> {
                                Results results = gson.fromJson(res.toString(), Results.class);

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
                                    MarkerOptions markerOptions = new MarkerOptions().position(point).title("Country: " +
                                            countriesList.get(i));
                                    googleMap.addMarker(markerOptions);

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
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle b = getArguments();

        View view =  inflater.inflate(R.layout.fragment_maps, container, false);
        if(b!=null){

        setUrl(b.getString("url"));}


return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


}
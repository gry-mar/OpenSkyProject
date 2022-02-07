package edu.ib.openskyproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class ShowPlaneMapFragment extends Fragment {

    protected String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url = getUrl();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            res -> {
                                Result2 results = gson.fromJson(res.toString(), Result2.class);

                                try {
                                    ArrayList<ArrayList> path = results.getPath();
                                    double latitude = Double.parseDouble(String.valueOf(path.get(0).get(1)));
                                    double longtitude = Double.parseDouble(String.valueOf(path.get(0).get(2)));
                                    LatLng point = new LatLng(latitude, longtitude);
                                    MarkerOptions markerOptions = new MarkerOptions().position(point);
                                    googleMap.addMarker(markerOptions);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                            }, error -> {
                        LatLng pointPWR = new LatLng(51.1052862455, 17.055921443);
                        MarkerOptions moPWR = new MarkerOptions().position(pointPWR).title("Aplikacja działa jak PWR, czyli nie działa");
                        googleMap.addMarker(moPWR);
                    });
                    queue.add(stringRequest);
                    handler.postDelayed(this, 5000);
                }
            };
            handler.postDelayed(runnable, 5000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        View view = inflater.inflate(R.layout.fragment_show_plane_map, container, false);
        if (bundle != null) {
            setUrl(bundle.getString("url"));
        }
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
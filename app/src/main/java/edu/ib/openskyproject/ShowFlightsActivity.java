package edu.ib.openskyproject;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
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

import java.io.StringReader;
import java.lang.reflect.Type;
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
    }

    public void showClicked(View view) {
        EditText edtLatMin = findViewById(R.id.edtLatMin);
        EditText edtLatMax = findViewById(R.id.edtLatMax);
        EditText edtLonMin = findViewById(R.id.edtLongMin);
        EditText edtLonMax = findViewById(R.id.edtLongMax);
        double latMin = Double.parseDouble(edtLatMin.getText().toString());
        double latMax = Double.parseDouble(edtLatMax.getText().toString());
        double lonMin = Double.parseDouble(edtLonMin.getText().toString());
        double lonMax = Double.parseDouble(edtLonMax.getText().toString());
        StringBuffer response = new StringBuffer();
        String url = "https://grymar439:Urwis1urwis1@opensky-network.org/api/states/all?lamin="+latMin+"&lomin"+lonMin+"&lamax="+
                latMax+"&lomax"+lonMax;
        RequestQueue queue = Volley.newRequestQueue(this);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                res ->{
            //Flights result = gson.fromJson(res, Flights.class);
                    Map m = gson.fromJson(res.toString(), Map.class);
                    String lat = m.get("states").toString();
                    //double lon =Double.valueOf((Double) m.get("longitude"));
                    System.out.println(lat);
                    for(Object key : m.keySet()){
                        System.out.println("Key"+key);
                    }
                    //System.out.println(result);
        }, error -> {
        });
        queue.add(stringRequest);

        //S/ystem.out.println(m);


    }
    }

package edu.ib.openskyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BoundariesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boundaries);

        Button btnShow = findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ShowFlightsActivity.class);
                EditText edtLatMin = findViewById(R.id.edtLatMin);
                EditText edtLatMax = findViewById(R.id.edtLatMax);
                EditText edtLonMin = findViewById(R.id.edtLongMin);
                EditText edtLonMax = findViewById(R.id.edtLongMax);
                double latMin = Double.parseDouble(edtLatMin.getText().toString());
                double latMax = Double.parseDouble(edtLatMax.getText().toString());
                double lonMin = Double.parseDouble(edtLonMin.getText().toString());
                double lonMax = Double.parseDouble(edtLonMax.getText().toString());
                System.out.println(String.valueOf(latMax));

                String url = "https://opensky-network.org/api/states/all?lamin=" + latMin + "&lomin=" + lonMin + "&lamax=" +
                        latMax + "&lomax=" + lonMax;
                Log.i("LOG PASS", url);
                intent.putExtra("passUrl",url);
                startActivity(intent);

            }
        });
    }

    public void returnToMain(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
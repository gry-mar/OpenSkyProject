package edu.ib.openskyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showFlightsClicked(View view) {
        Intent intent = new Intent(this, BoundariesActivity.class);
        startActivity(intent);


    }


    public void showArea(View view) {
        Intent intent = new Intent(this, DealWithLocationActivity.class);
        startActivity(intent);
    }



    public void trackFlightsClicked(View view) {

    }
}
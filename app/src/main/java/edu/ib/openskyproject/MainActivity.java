package edu.ib.openskyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Map;

/**
 * Main activity of the app
 * enables to move to other activities
 */
public class MainActivity extends AppCompatActivity {

    /**
     * method called when app initializes
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * method that switches window to BoundariesActivity
     * @param view
     */
    public void showFlightsClicked(View view) {
        Intent intent = new Intent(this, BoundariesActivity.class);
        startActivity(intent);


    }

    /**
     * method that switches window to DealWithLocationActivity
     * @param view
     */
    public void showArea(View view) {
        Intent intent = new Intent(this, DealWithLocationActivity.class);
        startActivity(intent);
    }

    /**
     * Method that switches window to ShowPlaneActivity
     * @param view
     */

    public void trackFlightsClicked(View view) {
        Intent intent = new Intent(this, ShowPlaneActivity.class);
        startActivity(intent);

    }
}
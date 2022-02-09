package edu.ib.openskyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity class which uses with specify icao24 number and represent a flight on map
 */

public class ShowPlaneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plane);
    }

    public void showPlayCLicked(View view) {

        EditText icao24 = findViewById(R.id.edtIcao24);
        TextView error = findViewById(R.id.tvPlaneError);
        if(!icao24.getText().toString().equals("")){
            error.setText("");
            String icao = icao24.getText().toString();
        String url = "https://opensky-network.org/api/states/all?icao24=" + icao;
        Bundle bundle = new Bundle();
        bundle.putString("plane_url",url);

        ShowPlaneMapFragment spmf = new ShowPlaneMapFragment();
        spmf.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView,spmf).commit();
        }
        else{
            error.setText("Enter icao24");
        }
    }

    /**
     * Return to main screen button
     * enables to come back directly to main app window
     * @param view
     */

    public void btnReturnToMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
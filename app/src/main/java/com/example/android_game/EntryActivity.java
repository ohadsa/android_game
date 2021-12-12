package com.example.android_game;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

public class EntryActivity extends AppCompatActivity {
    public enum speed {SLOW, MEDIUM, FAST}

    public static final int LOCATION_REQUEST_CODE  = 1001 ;
    private TextInputEditText panel_inputTxt;
    private Button panel_btn_records;
    private Button panel_btn_sensorsGame;
    private Button panel_btn_buttonsGame;
    private RadioGroup panel_radioGroup_speed;
    private ImageView panel_IMG_background;
    private FusedLocationProviderClient fusedLocationClient;
    private int btnOrSens;
    private speed speed_ ;
    private String playerName ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        panel_btn_buttonsGame = findViewById(R.id.panel_btn_buttonsGame);
        panel_btn_records = findViewById(R.id.panel_btn_records);
        panel_btn_sensorsGame = findViewById(R.id.panel_btn_sensorsGame);
        panel_radioGroup_speed = findViewById(R.id.panel_radioGroup_speed);
        panel_IMG_background = findViewById(R.id.panel_IMG_background);
        panel_inputTxt = findViewById(R.id.panel_inputTxt);
        askToEnableGps();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Glide
                .with(this)
                .load(R.drawable.home_background)
                .centerCrop()
                .into(panel_IMG_background);

        Log.d("OHAD", "onCreate: " + R.drawable.home_background );
        panel_btn_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecordActivity();

            }
        });

        panel_btn_buttonsGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameActivity(0, getSpeed(), getPlayerName());
            }
        });
        panel_btn_sensorsGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameActivity(1, getSpeed(), getPlayerName());
            }
        });


    }

    private void askToEnableGps() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE );
            }else{
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE   );
            }
        }
    }

    private String getPlayerName() {
        String str = panel_inputTxt.getText().toString();
        if (str.isEmpty())
            return "player";
        else
            return str;
    }

    private speed getSpeed() {

        String speed =
                ((RadioButton) findViewById(panel_radioGroup_speed.getCheckedRadioButtonId()))
                        .getText().toString();
        if (speed.equals("SLOW"))
            return EntryActivity.speed.SLOW;
        else if (speed.equals("MEDIUM"))
            return EntryActivity.speed.MEDIUM;
        else
            return EntryActivity.speed.FAST;
    }

    private void startGameActivity(int btnOrSens, speed speed, String playerName) {

        this.btnOrSens = btnOrSens;
        this.speed_ = speed;
        this.playerName = playerName;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            startGame(location.getLongitude() , location.getLatitude() );
                        }
                    }

                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        startGame( Fragment_Map.THAILAND_LONDTITUDE ,  Fragment_Map.THAILAND_LANTITUDE );
                    }
                });


    }

    private void startGame(double longitude , double latitude ) {

        Intent myIntent = new Intent(this, GameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(GameActivity.PLAYER_NAME, playerName);
        bundle.putInt(GameActivity.GAME_TYPE, btnOrSens);
        bundle.putInt(GameActivity.SPEED, speed_.ordinal());
        bundle.putDouble(GameActivity.LONGITUDE, longitude);
        bundle.putDouble(GameActivity.LATITUDE, latitude);
        myIntent.putExtra("Bundle", bundle);
        Log.d("ohado", "startGameActivity: before start");
        startActivity(myIntent);
        finish();
    }


    private void startRecordActivity() {

        Intent myIntent = new Intent(this, RecordsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(RecordsActivity.ENTRY_EXIST, true);
        myIntent.putExtra("Bundle", bundle);
        startActivity(myIntent);

    }


}
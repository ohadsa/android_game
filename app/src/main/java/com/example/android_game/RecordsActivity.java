package com.example.android_game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class RecordsActivity extends AppCompatActivity {


    public static String ENTRY_EXIST = "ENTRY_EXIST";
    private Fragment_List fragmentList;
    private Fragment_Map fragmentMap;
    private ImageView panel_IMG_background;
    private Button _btn_back;
    private Boolean isEntryExist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        Bundle extras = getIntent().getBundleExtra("Bundle");
        isEntryExist = extras.getBoolean(ENTRY_EXIST, false);
        findViews();

        Glide
                .with(this)
                .load(R.drawable.records_background)
                .centerCrop()
                .into(panel_IMG_background);

        fragmentList = new Fragment_List();
        fragmentList.setActivity(this);
        fragmentList.setCallBackList(callBackList);
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, fragmentList).commit();

        fragmentMap = new Fragment_Map();
        fragmentMap.setActivity(this);
        fragmentMap.setCallBack_map(callBack_map);
        getSupportFragmentManager().beginTransaction().add(R.id.frame2, fragmentMap).commit();

        _btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEntryActivity();
            }
        });
    }

    private void findViews() {
        panel_IMG_background = findViewById(R.id.panel_IMG_background);
        _btn_back = findViewById(R.id._btn_back);

    }

    private void openEntryActivity() {

        if (isEntryExist)
            finish();
        else {
            Intent myIntent = new Intent(this , EntryActivity.class);
            startActivity(myIntent);
            finish();
        }
    }

    CallBack_Map callBack_map = new CallBack_Map() {
        @Override
        public void mapClicked(double lat, double lon) {

        }
    };


    CallBack_List callBackList = new CallBack_List() {
        @Override
        public void updateTxt(String txt) {
            fragmentList.updateText(txt);
        }


        @Override
        public void placeOnMap(double x, double y, String name) {
            fragmentMap.setText(name);
            fragmentMap.pointOnMap(x,y);

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

}
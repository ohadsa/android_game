package com.example.android_game;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_Map extends Fragment implements OnMapReadyCallback {

    public static final Double THAILAND_LONDTITUDE = 100.5018  ;
    public static final Double THAILAND_LANTITUDE = 13.7563 ;

    private AppCompatActivity activity;
    private CallBack_Map callBack_map;
    private TextView _LBL_details ;
    private SupportMapFragment mapFragment ;
    private FrameLayout map;
    private LatLng point ;
    private GoogleMap mMap;


    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBack_map(CallBack_Map callBack_map) {
        this.callBack_map = callBack_map;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_map, container, false);
        findViews(view);
        initViews();
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map , mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return view;
    }


    private void initViews() {
        Intent intent = new Intent();

    }


    private void findViews(View view) {
        map = view.findViewById(R.id.map);
        _LBL_details = view.findViewById(R.id.data);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
    }

    public void setText(String txt , Boolean GPSok ) {
        if(GPSok)
            _LBL_details.setText(txt + " played here !! ");
        else
            _LBL_details.setText(txt);

    }

    public void pointOnMap(double x, double y) {
        point = new LatLng(x, y);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(point).title(""));
        moveToCurrentLocation(point);
    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);


    }
}
package com.example.android_game;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.appcompat.app.AppCompatActivity;

public class MovementSensors {

    private SensorManager sensorManager;
    private Sensor accSensor ;
    private AppCompatActivity activity ;


    public MovementSensors(){}

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public MovementSensors setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        return this;
    }

    public Sensor getAccSensor() {
        return accSensor;
    }

    public MovementSensors setAccSensor(Sensor accSensor) {
        this.accSensor = accSensor;
        return this;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    public MovementSensors setActivity(AppCompatActivity activity) {
        this.activity = activity;
        return this;
    }

    public void registerLisener(SensorEventListener accSensorEventListener, int sensorDelayNormal) {
        sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}

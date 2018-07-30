package com.example.administrator.googlemaps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements  View.OnClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private MapsUtils mapsUtils;

    /*
    自定义变量
     */
    TextView gerDuration;
    TextView StartTime;
    TextView EndTime;
    TextView getDistanceText;
    TextView getSpeed;
    Button StartRunning;
    Button PauseRunning;
    Button ResumeRunning;
    Button StopRunning;
    Button getDistance;
    Button getDuringTime;
    Button getRunningSpeed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /*
        处理UI逻辑
                */
        initUI();
        initClickListener();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapsUtils = new MapsUtils(getApplicationContext(),mapFragment);

    }


    private void initClickListener() {
        StartRunning.setOnClickListener(this);
        PauseRunning.setOnClickListener(this);
        ResumeRunning.setOnClickListener(this);
        StopRunning.setOnClickListener(this);
//        getDistanceButton.setOnClickListener(this);
//        getDuringTime.setOnClickListener(this);
        getDistance.setOnClickListener(this);
        getRunningSpeed.setOnClickListener(this);
    }

    private void initUI() {
        //UI逻辑
//        gerDuration= (TextView)findViewById(R.id.getDuration);
        StartTime = (TextView)findViewById(R.id.StartTime);
        EndTime = (TextView)findViewById(R.id.EndTime);
        getDistanceText= (TextView)findViewById(R.id.getDistance);
        getSpeed = (TextView)findViewById(R.id.getSpeed);

        StartRunning = (Button) findViewById(R.id.StartButton);
        PauseRunning = (Button) findViewById(R.id.PauseButton);
        ResumeRunning = (Button) findViewById(R.id.ResumeButton);
        StopRunning = (Button)findViewById(R.id.StopButton);
        getDistance = (Button)findViewById(R.id.getDistanceButton);
//        getDuringTime = (Button)findViewById(R.id.getDuringTime);
        getRunningSpeed = (Button)findViewById(R.id.getSpeedButton);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.StartButton:
                    mapsUtils.StartRunning();
                    StartTime.setText("开始时间："+mapsUtils.getmStartTime()+" ");
                    break;
            case R.id.PauseButton:
                    mapsUtils.PauseRunning();
                    break;
            case R.id.ResumeButton:
                    mapsUtils.ResumeRunning();
                    StartTime.setText("开始时间："+mapsUtils.getmStartTime()+" ");
                    break;
            case R.id.StopButton:
                mapsUtils.StopRunning();
                EndTime.setText("结束时间："+mapsUtils.getmEndTime());
//                mapsUtils.StopTime();
                break;
            case R.id.getDistanceButton:
                getDistanceText.setText("累计路程："+mapsUtils.getDistance()+"m");
                break;
//            case R.id.getDuringTime:
//                gerDuration.setText("瞬时间隔："+mapsUtils.getDuration());
//                break;
            case R.id.getSpeedButton:
                getSpeed.setText("瞬时速度："+mapsUtils.getSpeed()+"m/s");
            default:
                break;
        }
    }
}




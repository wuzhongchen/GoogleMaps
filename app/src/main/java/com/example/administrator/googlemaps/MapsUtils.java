package com.example.administrator.googlemaps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsUtils implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location location;
    private List<Location> RecordList=new ArrayList<Location>();

    public long getmStartTime() {
        return mStartTime;
    }

    public long getmEndTime() {
        return mEndTime;
    }

    private float distance;
    private long mStartTime;
    private long mEndTime;
    private SupportMapFragment mSupportMapFragment;
    private LocationManager mLocationManager;
    private String mLocationProvider;
    private Context mContext;
   private long mMinTime = 1000;

    private Marker mMarker;
    private enum State {
        RUNNING,
        PAUSE,
        STOP
    }
    private State mState = State.STOP;


    public MapsUtils(Context context, SupportMapFragment supportMapFragment) {
        this.mContext = context;
        this.mSupportMapFragment = supportMapFragment;
        this.mSupportMapFragment.getMapAsync(this);

        //位置提供器
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
//        List<String> providers = mLocationManager.getProviders(true);
        mLocationProvider = LocationManager.GPS_PROVIDER;
//        if (providers.contains(LocationManager.GPS_PROVIDER)) {
//            mLocationProvider = LocationManager.GPS_PROVIDER;
//            Toast.makeText(mContext, "Current location provider is GPS_PROVIDER", Toast.LENGTH_SHORT).show();
//        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
//            mLocationProvider = LocationManager.NETWORK_PROVIDER;
//            Toast.makeText(mContext, "Current location provider is NETWORK_PROVIDER", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(mContext, "No available location provider", Toast.LENGTH_SHORT).show();
//            return;
//        }

//        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        init();
    }

    private void init() {
        android.location.LocationListener locationListener = new android.location.LocationListener(){
            @Override
            public void onLocationChanged(Location location) {

                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                if (mMap != null) {
                    if (mMarker == null) {
                        mMarker = mMap.addMarker(new MarkerOptions().position(ll)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)));
                    } else {
                        mMarker.setPosition(ll);
                    }
                }

                if (mState == State.RUNNING) {
                    RecordList.add(location);
                    setDistance();
                    for(int k=0; k< RecordList.size()-1 ;k++) {
                        LatLng currentLatlng = new LatLng(RecordList.get(k).getLatitude(), RecordList.get(k).getLongitude());
                        LatLng lastLatlng = new LatLng(RecordList.get(k+1).getLatitude(),RecordList.get(k+1).getLongitude());
                        PolylineOptions rectOptions = new PolylineOptions()
                                .add(lastLatlng,currentLatlng)
                                .color(Color.GRAY)
                                .width(5.5f);
                        mMap.addPolyline(rectOptions);
                    }
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        mLocationManager.requestLocationUpdates(mLocationProvider, mMinTime, 1, locationListener);
    }

    public String getDistance() {
        return String.valueOf(distance);
    }

    public String getDuration() {
        return String.valueOf((mEndTime - mStartTime) / 1000f);
    }

    public String getAverageSpeed() {
        mEndTime=System.currentTimeMillis();
        return String.valueOf(distance / (float) (mEndTime - mStartTime));
    }


    //设置距离
    public void setDistance() {
        this.distance = 0.0f;
        if (RecordList != null && RecordList.size() > 1 ) {
            for (int i = 0; i < RecordList.size() - 1; i++) {
                Location firstPoint = RecordList.get(i);
                Location secondPoint = RecordList.get(i + 1);
                LatLng firstLatLng = new LatLng(firstPoint.getLatitude(),
                        firstPoint.getLongitude());
                LatLng secondLatLng = new LatLng(secondPoint.getLatitude(),
                        secondPoint.getLongitude());
                double betweenDis = calculateLineDistance(firstLatLng,
                        secondLatLng);
                this.distance += (float)(betweenDis);
            }
        }
    }

    public void StartRunning() {
        RecordList.clear();
        mState = State.RUNNING;
        this.mStartTime = System.currentTimeMillis();
    }

    public void PauseRunning() {
        mState = State.PAUSE;
    }

    public void ResumeRunning() {
        mState = State.RUNNING;
    }

    public void StopRunning() {
        RecordList.clear();
        mState = State.STOP;
        this.mEndTime=System.currentTimeMillis();
    }

    //计算经纬度间距离
    private static float calculateLineDistance(LatLng var0, LatLng var1) {
        if (var0 != null && var1 != null) {
            try {
                double var2 = 0.01745329251994329D;
                double var4 = var0.longitude;
                double var6 = var0.latitude;
                double var8 = var1.longitude;
                double var10 = var1.latitude;
                var4 *= 0.01745329251994329D;
                var6 *= 0.01745329251994329D;
                var8 *= 0.01745329251994329D;
                var10 *= 0.01745329251994329D;
                double var12 = Math.sin(var4);
                double var14 = Math.sin(var6);
                double var16 = Math.cos(var4);
                double var18 = Math.cos(var6);
                double var20 = Math.sin(var8);
                double var22 = Math.sin(var10);
                double var24 = Math.cos(var8);
                double var26 = Math.cos(var10);
                double[] var28 = new double[3];
                double[] var29 = new double[3];
                var28[0] = var18 * var16;
                var28[1] = var18 * var12;
                var28[2] = var14;
                var29[0] = var26 * var24;
                var29[1] = var26 * var20;
                var29[2] = var22;
                double var30 = Math.sqrt((var28[0] - var29[0]) * (var28[0] - var29[0]) + (var28[1] - var29[1]) * (var28[1] - var29[1]) + (var28[2] - var29[2]) * (var28[2] - var29[2]));
                return (float)(Math.asin(var30 / 2.0D) * 1.27420015798544E7D);
            } catch (Throwable var32) {
                var32.printStackTrace();
                return 0.0F;
            }
        } else {
            try {
                throw new AMapException("非法坐标值");
            } catch (AMapException var33) {
                var33.printStackTrace();
                return 0.0F;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                Log.e("test", "loaded!!!!1");
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location location = mLocationManager.getLastKnownLocation(mLocationProvider);
                    CameraPosition currentCameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(20.5f)
                            .bearing(300)
                            .tilt(0)
                            .build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentCameraPosition));
                }
            }
        });
    }
}

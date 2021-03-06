package com.example.hoyoung.eyeload;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.view.View.OnTouchListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hoyoung on 2016-11-04.
 */
public class ARActivity extends SensorActivity implements OnTouchListener {
    private static final String TAG = "ARActivity";
    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");
    private static final String END_TEXT = FORMAT.format(ARActivity.MAX_ZOOM)+" km";
    private static final int END_TEXT_COLOR = Color.WHITE;

    private static PowerManager.WakeLock wakeLock=null;
    private static CameraSurface camScreen=null;
    private static TextView endLabel=null;
    private static ARView arView=null;

    private static Bitmap bitmap=null;

    public static final float MAX_ZOOM = 100; //in KM
    public static final float ONE_PERCENT = MAX_ZOOM/100f;
    public static final float TEN_PERCENT = 10f*ONE_PERCENT;
    public static final float TWENTY_PERCENT = 2f*TEN_PERCENT;
    public static final float EIGHTY_PERCENTY = 4f*TWENTY_PERCENT;

    public static boolean useCollisionDetection = true;

    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1);
    private static final ThreadPoolExecutor exeService = new ThreadPoolExecutor(1, 1, 20, TimeUnit.SECONDS, queue);

    @Override
    public void onCreate(Bundle savedInstanceState){
        /*
        if(Build.VERSION.SDK_INT>=23) {  //버전확인
            String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            int i = 0;
            int permissionCode = 0;
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {

                } else {//권한 없음
                    ActivityCompat.requestPermissions(this, permissions, permissionCode);
                }
            }
        }*/

        super.onCreate(savedInstanceState);

        camScreen = new CameraSurface(this);
        setContentView(camScreen);

        arView=new ARView(this);
        arView.setOnTouchListener(this);

        ViewGroup.LayoutParams arLayout=new ViewGroup.LayoutParams(  ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addContentView(arView,arLayout);




        updateDataOnZoom();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "DimScreen");

        bitmap= BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);


    }
    @Override
    public void onStart(){
        super.onStart();
        Location last = ARData.getCurrentLocation();
        updateData(last.getLatitude(),last.getLongitude(),last.getAltitude());
    }
    @Override
    public void onResume() {
        super.onResume();

        wakeLock.acquire();
    }
    @Override
    public void onPause() {
        super.onPause();

        wakeLock.release();
    }

    @Override
    public void onSensorChanged(SensorEvent evt) {
        super.onSensorChanged(evt);

        if (    evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER ||
                evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            arView.postInvalidate();
        }
    }
    private static float calcZoomLevel(){
        int myZoomLevel = 100;
        float out = 0;

        float percent = 0;
        if (myZoomLevel <= 25) {
            percent = myZoomLevel/25f;
            out = ONE_PERCENT*percent;
        } else if (myZoomLevel > 25 && myZoomLevel <= 50) {
            percent = (myZoomLevel-25f)/25f;
            out = ONE_PERCENT+(TEN_PERCENT*percent);
        } else if (myZoomLevel > 50 && myZoomLevel <= 75) {
            percent = (myZoomLevel-50f)/25f;
            out = TEN_PERCENT+(TWENTY_PERCENT*percent);
        } else {
            percent = (myZoomLevel-75f)/25f;
            out = TWENTY_PERCENT+(EIGHTY_PERCENTY*percent);
        }
        return out;

    }
    private void updateDataOnZoom(){
        float zoomLevel=calcZoomLevel();
        ARData.setRadius(zoomLevel);
        ARData.setZoomLevel(FORMAT.format(zoomLevel));
        ARData.setZoomProgress(50);
        Location last = ARData.getCurrentLocation();
        updateData(last.getLatitude(),last.getLongitude(),last.getAltitude());
    }

    @Override
    public boolean onTouch(View v, MotionEvent me) {
        for (Marker marker : ARData.getMarkers()) {
            if (marker.handleClick(me.getX(), me.getY())) {
                if (me.getAction() == MotionEvent.ACTION_UP) markerTouched(marker);
                return true;
            }
        }
        return super.onTouchEvent(me);
    }
    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        updateData(location.getLatitude(),location.getLongitude(),location.getAltitude());
    }

    private void markerTouched(Marker marker){
        //마커 터치 되었을때 동작.
    }

    private void updateData(final double lat, final double lon, final double alt){
        try {
            exeService.execute(
                    new Runnable() {

                        public void run() {
                         //   for (NetworkDataSource source : sources.values())
                                download( lat, lon, alt);
                        }
                    }
            );
        } catch (RejectedExecutionException rej) {
            Log.w(TAG, "Not running new download Runnable, queue is full.");
        } catch (Exception e) {
            Log.e(TAG, "Exception running download Runnable.",e);
        }
    }

    private static boolean download( double lat, double lon, double alt){
        //DB에서 다운하는 부분
        //Bitmap a= BitmapFactory.decodeResource(this.getResources(), );
        List<Marker> markers =new ArrayList<Marker>();
        Marker d=new Marker("Lab",37.5583037 ,126.9984677,90, Color.RED, bitmap );
        markers.add(d);
        Marker c=new Marker("Fuck3",37.4433899,127.1340677,70, Color.YELLOW,bitmap);
        markers.add(c);

        ARData.addMarkers(markers);
        return true;
    }


}

package com.insectiousapp.mtherapyminor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.HashMap;

/**
 * Created by Codev on 12/7/2015.
 */
public class Service_sense_shake extends Service implements SensorEventListener {


    private long lastUpdate = 0;
    private float last_x;
    private float last_y;
    private float last_z;
    private String TAG= "ExerciseSensor";
    private static final double SHAKE_THRESHOLD = 2.5;
    private String exercise_name;
    private TextView tv1;
    ImageView tvplay;
    VideoView vv1;
    private HashMap<String , String> map =  new HashMap<String , String>();
    private HashMap<String, Integer> mapvideo=new HashMap<String, Integer>();
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private int flag =0;
    private int video_decide=0;
    MediaPlayer oursong;
    int counter=0;



    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("reached", "service started");


        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        //   vv1.requestFocus();

       // senSensorManager.unregisterListener(this);


    }

    @Override
    public void onStart(Intent intent, int startId) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    ////////////////////////////////SENSOR PART///////////////////

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];



            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float diff = Math.abs(x - last_x);

                float diffy=Math.abs(y-last_y);
                float diffz=Math.abs(z-last_z);

                if (diffz > 22) {
                    //Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                    Log.d("reachedd", String.valueOf(diffz));


                    senSensorManager.unregisterListener(this);

                    Log.d("reachedd", "sensorma killed");

                    Intent dialogIntent = new Intent(this, StartingActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);


                 //   Toast.makeText(this, "device shaked", Toast.LENGTH_SHORT);


                }
                last_x = x;
                last_y=y;
                last_z=z;
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    /////////////////////////////SENSOR PART END//////////////////


}

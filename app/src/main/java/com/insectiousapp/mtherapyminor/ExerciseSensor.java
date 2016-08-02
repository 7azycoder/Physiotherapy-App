package com.insectiousapp.mtherapyminor;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.HashMap;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

public class ExerciseSensor extends AppCompatActivity implements View.OnClickListener,SensorEventListener {

    private long lastUpdate = 0;
    private float last_x;
    private float last_y;
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
    private SensorManager senSensorManager2;
    private Sensor senAccelerometer2;
    private int flag =0;
    private int video_decide=0;
    MediaPlayer oursong;
    int counter=0;
    int rotationcounter=0;//

    FancyButton btry;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_sensor);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);

                    Log.i("tag", "reached oninit");

                }
            }
        });





        // Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
        // t1.speak("heloow baby", TextToSpeech.QUEUE_FLUSH, null, null);
        Log.i("tag", "reached t1.speak");
        // t1.speak(oncreate, TextToSpeech.QUEUE_FLUSH,)





        tv1=(TextView)findViewById(R.id.tv1_exercise_sensor);
        tvplay=(ImageView)findViewById(R.id.tv_play);
        vv1=(VideoView)findViewById(R.id.vv_exercise_sensor);
        btry=(FancyButton)findViewById(R.id.btry_exercise_sensor);

        oursong=MediaPlayer.create(this, R.raw.alarm2);


        btry.setOnClickListener(this);

        map.put("Hand Curl 1", "1.Hand Curl 1\n" +
                "2. This is first Exercise,  a very basic one for curling of hands. \n" +
                "3. It basically works on forearm and elbow joint");
        map.put("Hand Side Swing 1","1.Hand Side Swing 1\n" +
                "2. This is a exercise for shoulder\n" +
                "3. This exercise mainly makes the shoulder stronger and enhances the movement");
        map.put("Shoulder Front Swing","1.Shoulder Front Swing \n" +
                "2. A exercise to build strong pressure on shoulder.");
        map.put("Bench Press","1.Bench Press\n" +
                "2. A exercise for building massive chest");
        map.put("Crunches","1.Crunches\n" +
                "2. Exercies for building upper 2 pack abs");
        map.put("Chinups","1.Chinups\n" +
                "2. A way to build a cobra back");
        map.put("Dips","1.Dips\n" +
                "2. A super exercise for building shoulder, biceps and triceps");



        mapvideo.put("Hand Curl 1", R.raw.hand_curl1);
        mapvideo.put("Hand Side Swing 1", R.raw.hand_side_swing1);
        mapvideo.put("Shoulder Front Swing", R.raw.shoulder_front_swing);
        mapvideo.put("Bench Press", R.raw.hand_curl1);
        mapvideo.put("Crunches",R.raw.hand_side_swing1);
        mapvideo.put("Chinups",R.raw.shoulder_front_swing);
        mapvideo.put("Dips", R.raw.hand_curl1);

        Intent i=getIntent();
        if(i!=null)
        {

            exercise_name=i.getStringExtra("exercise_name_from_exerciseactivity");
            tv1.setText(map.get(exercise_name));

        }


        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager2 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer2 = senSensorManager2.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        String uriPath = "android.resource://com.insectiousapp.mtherapyminor/" + mapvideo.get(exercise_name);
        Uri uri = Uri.parse(uriPath);
        vv1.setVideoURI(uri);
        //   vv1.requestFocus();



        tv1.setOnClickListener(this);
        vv1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("video", "Video clicked");

                if(!vv1.isPlaying()) {
                    Log.i("video", "Video Started1");
                    vv1.start();
                    Log.i("video", "Video Started");
                    /*Animation zoomout = AnimationUtils.loadAnimation(ExerciseSensor.this, R.anim.zoomout);
                    tvplay.setAnimation(zoomout);*/
                    tvplay.setVisibility(View.GONE);
                }
                else{
                    Log.i("video", "Video Paused1");
                    vv1.pause();
                    tvplay.setVisibility(View.VISIBLE);
                    Log.i("video", "Video Paused");
                }
                return false;
            }
        });
    }
    protected void onResume() {
        super.onResume();
        if(flag==1) {
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
            senSensorManager2.registerListener(this, senAccelerometer2, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        }
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
        senSensorManager2.unregisterListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btry_exercise_sensor:
                if(btry.getText().equals("Try it your self !!"))
                {
                    senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
                    senSensorManager2.registerListener(this, senAccelerometer2, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
                    flag=1;

                    Toast.makeText(ExerciseSensor.this, "Please start performing exercise", Toast.LENGTH_SHORT).show();
                    btry.setText("Stop Performing");
                    if(vv1.isPlaying())
                    {
                        vv1.pause();
                        tvplay.setVisibility(View.VISIBLE);

                    }
                }
                else
                {

                    senSensorManager.unregisterListener(this);
                    senSensorManager2.unregisterListener(this);
                    btry.setText("Try it your self !!");

                }

                break;
        }


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

//        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            float x = sensorEvent.values[0];
//            float y = sensorEvent.values[1];
//            float z = sensorEvent.values[2];
//
//
//
//            long curTime = System.currentTimeMillis();
//
//            if ((curTime - lastUpdate) > 100) {
//                long diffTime = (curTime - lastUpdate);
//                lastUpdate = curTime;
//
//                float diff = Math.abs(x - last_x);
//
//                float diffy=Math.abs(y-last_y);
//
//                if (diffy > SHAKE_THRESHOLD) {
//                    //Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG , String.valueOf(diff));
//
//                    oursong.start();
//
//
//                }
//                last_x = x;
//                last_y=y;
//            }
//        }

        if (mySensor.getType() == Sensor.TYPE_ORIENTATION) {
           // float ox = sensorEvent.values[0];
            float pitch = sensorEvent.values[1];
            float roll = sensorEvent.values[2];
            rotationcounter=0;
            float maxpitch=-5, minpitch=-140;
            float maxroll=72, minroll=-15;

            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 50)
            {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

            Log.i("rotations", " pitch="+String.valueOf(pitch)+" roll="+String.valueOf(roll));


                if((pitch<minpitch)||(pitch>maxpitch)||(roll>maxroll)||(roll<minroll))
                {
                    oursong.start();
                }
                    rotationcounter=1;



            }
           // rotationcounter=0;
           // oursong.start();
        }


    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

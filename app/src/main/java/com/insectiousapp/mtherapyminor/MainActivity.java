package com.insectiousapp.mtherapyminor;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener, TextToSpeech.OnInitListener {

    TextView tvexercise, tvtips;
    TextToSpeech t1;
    TextToSpeech tts;
    ActionBar ab;
    String first="Welcome to the M Therapy, please choose one of the following modes, either, Normal Mode, or, Blind Mode, ";
    String second=", please, Draw, a, 2, for normal mode, and draw, a 3, for blind mode";

    String oncreate=first+second;
    String main=oncreate;
    Thread time;
    String check=null;
    String oncreate2=null;
    int k=0;               //k=0 means that we are inputting from 1-9 and k=1 means we are inputting either forward or backward

    int blind_check=0;

    DataBase_BlindCheck helper;

    ////////////for gesture
    GestureOverlayView gestures;
    private GestureLibrary gLib;
    private static final String TAG = "com.GestureTestTwo";


    ////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        helper=new DataBase_BlindCheck(this);

        tvexercise=(TextView)findViewById(R.id.tvexercise);
        tvtips=(TextView)findViewById(R.id.tvtips);


        tvexercise.setTypeface(EasyFonts.captureIt(this));
        tvtips.setTypeface(EasyFonts.captureIt(this));


        tvexercise.setOnClickListener(this);
        tvtips.setOnClickListener(this);

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
        //t1.speak(oncreate, TextToSpeech.QUEUE_FLUSH, null, null);
        Log.i("tag", "reached t1.speak");
       // t1.speak(oncreate, TextToSpeech.QUEUE_FLUSH,)



       // TextToSpeech tts = new TextToSpeech(this, this);
        //tts.setLanguage(Locale.US);
    //    tts.speak("Text to say aloud", TextToSpeech.QUEUE_ADD, null);


        time=new Thread(){

            @Override
            public void run() {


                try{
                    sleep(250);

                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    {
                        t1.setSpeechRate(1f);
                        t1.speak(main, TextToSpeech.QUEUE_FLUSH, null);
                       // t1.setPitch(0.5f);


                    }
                }

            }
        };


        //////////////gesture///////////////////////



        gLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gLib.load()) {
            Log.w(TAG, "could not load gesture library");
            finish();
        }

         gestures = (GestureOverlayView) findViewById(R.id.gestures);
        gestures.addOnGesturePerformedListener(handleGestureListener);






        /////////////////////////////////////
    }



    //////////////////gesture listener/////////////////

    private GestureOverlayView.OnGesturePerformedListener handleGestureListener = new
            GestureOverlayView.OnGesturePerformedListener() {
                @Override
                public void onGesturePerformed(GestureOverlayView gestureView,
                                               Gesture gesture) {
                    ArrayList predictions = gLib.recognize(gesture);

                    t1.setSpeechRate(1f);

                    if (predictions.size() > 0) {
                        Prediction prediction = (Prediction) predictions.get(0);

                        String pname=prediction.name;
                         check=null;
                        if (k == 1) {//we are checking for backward for forward

                            if (pname.contentEquals("forward") || pname.contentEquals("back")) {


                            if (prediction.score > 2.0) {

                                if(pname.contentEquals("forward"))
                                {
                                    k=0;
                                    t1.speak("accepted", TextToSpeech.QUEUE_FLUSH, null);


                                    time=new Thread(){

                                        @Override
                                        public void run() {


                                            try{
                                                sleep(500);

                                            }catch(InterruptedException e){
                                                e.printStackTrace();
                                            }
                                            finally {
                                                {
                                                    t1.setSpeechRate(1f);
                                                    t1.speak(main, TextToSpeech.QUEUE_FLUSH, null);
                                                     t1.setPitch(0.5f);


                                                }
                                            }

                                        }
                                    };



                                    Intent i=new Intent();
                                    i.setClass(getApplicationContext(), ExerciseActivity.class);
                                    startActivity(i);
                                }
                                else if(pname.contentEquals("back"))
                                {
                                    k=0;
                                    t1.speak("please enter again", TextToSpeech.QUEUE_FLUSH, null);
                                }

                                Toast.makeText(MainActivity.this, prediction.name, Toast.LENGTH_SHORT).show();
                                check = ", you, entered, a, " + prediction.name;
                                Log.i("TAG1", check);
                                // oncreate2
                                k = 0;
                            } else {
                                Toast.makeText(MainActivity.this, "FARZI", Toast.LENGTH_SHORT).show();
                                check = " please, enter a backward or forward gesture again";
                                Log.i("TAG2", check);
                                k = 1;
                            }
                        }
                        }
                        else
                        {
                            //we are checking for 1-7//k=0

                            if (!(pname.contentEquals("forward") || pname.contentEquals("back"))) {


                                if (prediction.score > 2.0) {

                                if(prediction.name.contentEquals("two")||prediction.name.contentEquals("three")) {
                                    Toast.makeText(MainActivity.this, prediction.name, Toast.LENGTH_SHORT).show();
                                    check = ", you entered a, " + prediction.name + " , please enter a forward, or backward, gesture to proceed";
                                    // Log.i("TAG1", check);
                                    // oncreate2
                                    t1.speak(check, TextToSpeech.QUEUE_FLUSH, null);
                                    if(prediction.name.contentEquals("two"))
                                    {
                                        blind_check=0;



                                    }
                                    else
                                    {
                                        blind_check=1;
                                    }
                                    k = 1;

                                    //write the value of blind check in databse


                                    ContentValues cv = new ContentValues();
                                    SQLiteDatabase db=helper.getWritableDatabase();


                                    String[] col=new String[1];
                                    col[0]=DataBase_BlindCheck.BLIND_CHECK;


                                    Cursor c=db.query(DataBase_BlindCheck.ROOM, col, null, null, null, null, null);

                                    if((c!=null)&&c.getCount()>0) {
                                        //  Toast.makeText(this, "Cursor Not Null", Toast.LENGTH_SHORT).show();
                                        //delete the data and replace with the new one


                                        db.delete(DataBase_BlindCheck.ROOM, null, null);

                                    }

                                    cv.put(DataBase_BlindCheck.BLIND_CHECK, blind_check);
                                    db.insert(DataBase_BlindCheck.ROOM, null, cv);

                                    //writing done

                                }
                                else
                                {
                                    check = " wrong choice, please enter again";
                                    t1.speak(check, TextToSpeech.QUEUE_FLUSH, null);
                                }



                                } else {
                                    Toast.makeText(MainActivity.this, "FARZI", Toast.LENGTH_SHORT).show();
                                    check = " please, enter again";
                                    t1.speak(check, TextToSpeech.QUEUE_FLUSH, null);
                                    //  Log.i("TAG2", check);
                                    k = 0;
                                }

                            }
                        }
                    }
                }
            };


    ///////////////////////////////////////////////////


    @Override
    protected void onResume() {

        super.onResume();

        String onresume="Choose one option, Normal Mode or Blind mode";
       // t1.speak(onresume, TextToSpeech.QUEUE_FLUSH, null);

        time.start();

    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.tvexercise:
                //case when exercises are displayed

                Intent i1=new Intent();
                i1.setClass(getApplicationContext(), ExerciseActivity.class);
                t1.speak("hello chetan", TextToSpeech.QUEUE_FLUSH, null);
                startActivity(i1);

                break;
            case R.id.tvtips:
                //case when tips are selected

                Intent i2=new Intent();
           //     i2.setClass(getApplicationContext(), ExerciseActivity.class);
            //    startActivity(i2);

                i2.setClass(getApplicationContext(), MainActivity3.class);
                t1.speak("hello chetan", TextToSpeech.QUEUE_FLUSH, null);
                startActivity(i2);

                break;

        }


    }

    @Override
    public void onInit(int status) {

    }
}

package com.insectiousapp.mtherapyminor;

import android.app.ActionBar;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class ExerciseActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView lv1;
    ArrayList<ExerciseItem> data;
    ExerciseAdapter adapter;
    LayoutInflater l;



    TextToSpeech t1;
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
        setContentView(R.layout.activity_exercise);

        gestures = (GestureOverlayView) findViewById(R.id.gestures_activity_exercise);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);

                    Log.i("tag", "reached oninit");

                }
            }
        });

        helper=new DataBase_BlindCheck(this);

        checkdatabaseforblind();

        data=new ArrayList<ExerciseItem>();
        l=getLayoutInflater();
        adapter=new ExerciseAdapter(this, 0, data, l);

        lv1=(ListView)findViewById(R.id.lv1_exercise_activity);

        lv1.setAdapter(adapter);

        ExerciseItem e1=new ExerciseItem("Hand Curl 1");
        ExerciseItem e2=new ExerciseItem("Hand Side Swing 1");
        ExerciseItem e3=new ExerciseItem("Shoulder Front Swing");
        ExerciseItem e4=new ExerciseItem("Bench Press");
        ExerciseItem e5=new ExerciseItem("Crunches");
        ExerciseItem e6=new ExerciseItem("Chinups");
        ExerciseItem e7=new ExerciseItem("Dips");

        data.add(e1);
        data.add(e2);
        data.add(e3);
        data.add(e4);
        data.add(e5);
        data.add(e6);
        data.add(e7);

        lv1.setOnItemClickListener(this);


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
                      //  t1.speak(main, TextToSpeech.QUEUE_FLUSH, null);
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


        gestures.addOnGesturePerformedListener(handleGestureListener);






        /////////////////////////////////////


    }

    private void checkdatabaseforblind() {


        SQLiteDatabase db=helper.getReadableDatabase();

        String[] col=new String[1];
        col[0]=DataBase_BlindCheck.BLIND_CHECK;

        Cursor c=db.query(DataBase_BlindCheck.ROOM, col, null, null, null, null, null);

        String blind_check=null;

        while(c.moveToNext())
        {

            blind_check = c.getString(c.getColumnIndex(DataBase_BlindCheck.BLIND_CHECK));
            Log.i("TAGG", blind_check);
        }

        Integer checkk=Integer.parseInt(String.valueOf(blind_check));

        if(checkk==0)
        {

            gestures.setVisibility(View.GONE);
            ///////case of normal mode
        }
        else
        {
            //////////case of blind mode


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






        }

    }





    //////////////////gesture listener/////////////////

    private GestureOverlayView.OnGesturePerformedListener handleGestureListener = new
            GestureOverlayView.OnGesturePerformedListener() {
                @Override
                public void onGesturePerformed(GestureOverlayView gestureView,
                                               Gesture gesture) {
                    ArrayList predictions = gLib.recognize(gesture);

                   // t1.setSpeechRate(1f);

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


                                        Intent i=new Intent();
                                        i.setClass(getApplicationContext(), ExerciseSensor_Blind.class);

                                        //ExerciseItem ei1=data.get(position);

                                        String exercise_name="Hand Curl 1";



                                        i.putExtra("exercise_name_from_exerciseactivity", exercise_name);

                                        startActivity(i);


                                    }
                                    else if(pname.contentEquals("back"))
                                    {
                                        k=0;
                                        t1.speak("please enter again", TextToSpeech.QUEUE_FLUSH, null);
                                    }

                                    Toast.makeText(ExerciseActivity.this, prediction.name, Toast.LENGTH_SHORT).show();
                                    check = ", you, entered, a, " + prediction.name;
                                    Log.i("TAG1", check);
                                    // oncreate2
                                    k = 0;
                                } else {
                                    Toast.makeText(ExerciseActivity.this, "FARZI", Toast.LENGTH_SHORT).show();
                                    check = " please, enter a backward or forward gesture again";
                                    Log.i("TAG2", check);
                                    k = 1;
                                }
                            }
                        }
                        else
                        {
                            //we are checking for 1-7//k=0

                            if (!(pname.contentEquals("forward") || pname.contentEquals("back")||pname.contentEquals("line"))) {


                                if (prediction.score > 2.0) {

                                    if(prediction.name.contentEquals("two")||prediction.name.contentEquals("three")) {
                                        Toast.makeText(ExerciseActivity.this, prediction.name, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ExerciseActivity.this, "FARZI", Toast.LENGTH_SHORT).show();
                                    check = " please, enter again";
                                    t1.speak(check, TextToSpeech.QUEUE_FLUSH, null);
                                    //  Log.i("TAG2", check);
                                    k = 0;
                                }

                            }else if(prediction.name.contentEquals("line")&&(prediction.score > 4.0))
                            {

                                String first="Select a Exercise,  ";
                                String second=", please, Draw, a, 2, for normal mode, and draw, a 3, for blind mode";

                                String oncreate=first+second;
                                t1.setSpeechRate(1f);
                                t1.speak(main, TextToSpeech.QUEUE_FLUSH, null);

                        }
                        }
                    }
                }
            };


    ///////////////////////////////////////////////////
















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise, menu);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i=new Intent();
        i.setClass(getApplicationContext(), ExerciseSensor_Blind.class);

        ExerciseItem ei1=data.get(position);

        String exercise_name=ei1.exercise_name;



        i.putExtra("exercise_name_from_exerciseactivity", exercise_name);

        startActivity(i);

    }
}

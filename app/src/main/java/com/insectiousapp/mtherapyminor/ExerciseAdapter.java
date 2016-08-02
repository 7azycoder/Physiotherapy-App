package com.insectiousapp.mtherapyminor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Codev on 10/24/2015.
 */
public class ExerciseAdapter extends ArrayAdapter<ExerciseItem> {

    LayoutInflater l;
    Context context;
    List<ExerciseItem> objects;
    TextView tv_exercise_item;
    ImageView iv_exercise_item;

    int imagelist[]={R.mipmap.ex1, R.mipmap.ex2, R.mipmap.ex3, R.mipmap.ex4, R.mipmap.ex5, R.mipmap.ex6, R.mipmap.ex7};


    public ExerciseAdapter(Context context, int resource, List<ExerciseItem> objects,LayoutInflater l) {
        super(context, resource, objects);

        this.l=l;
        this.context=context;
        this.objects=objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v=convertView;

        if(v==null)
        {
            v=l.inflate(R.layout.exercise_list_item, null);
        }


        tv_exercise_item=(TextView)v.findViewById(R.id.tv_exercise_listitem);
        iv_exercise_item=(ImageView)v.findViewById(R.id.iv_exercise_list_item);
        Log.i("tag", "1");
        ExerciseItem eitem=objects.get(position);
        Log.i("tag", "2");
        tv_exercise_item.setText(eitem.exercise_name);
        Log.i("tag", "3");
       // iv_exercise_item.setBackgroundResource(imagelist[position]);
        iv_exercise_item.setBackgroundResource(R.mipmap.ex1);
        Log.i("tag", "4");

        return v;
    }
}

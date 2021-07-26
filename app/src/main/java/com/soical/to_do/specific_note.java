package com.soical.to_do;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soical.to_do.adapter.todo_adapter;
import com.soical.to_do.databackend.data;
import com.soical.to_do.databackend.dbhelper;


public class specific_note extends AppCompatActivity {
    TextView title ;
    EditText dis ;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_note);
        title = findViewById(R.id.title_notes);
        dis = findViewById(R.id.MultiLine);
        dis.setMovementMethod(LinkMovementMethod.getInstance());

        Bundle extra = getIntent().getExtras();
        int id = extra.getInt("id");
        int pos = extra.getInt("position");
        System.out.println(id +" "+pos);
        title.setText(data.todo_title.get(pos).toString());
        dis.setText(data.todo_dis.get(pos).toString());
//        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.ghha);
//        final Drawable d = new BitmapDrawable(getResources(),b);
    //        d.setBounds(0,0,(int)getResources().getDimension(R.drawable.ghha),(int)getResources().getDimension(R.drawable.ghha));
//        final ImageSpan is = new ImageSpan(d);
//
//        SpannableStringBuilder ss = new SpannableStringBuilder(".\n");
//        ss.setSpan(is,0,(".\n").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new specific_note(),0,(".\n".length()),0);
//        dis.append(ss,0,(".\n").length());
//        dis.setMovementMethod(LinkMovementMethod.getInstance());
         title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new dbhelper(getApplicationContext()).update(title.getText().toString().trim(),dis.getText().toString(),id);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dis.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                System.out.println(keyCode +" its the key code and , its key event "+event);
                return false;
            }
        });

        dis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new dbhelper(getApplicationContext()).update(title.getText().toString().trim(),dis.getText().toString(),id);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @SuppressLint("ResourceType")
    public void add_image(View view){

        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.ghha);
        final Drawable d = new BitmapDrawable(getResources(),b);
        d.setBounds(0,0,d.getMinimumWidth()/5,d.getMinimumHeight()/5);

        final ImageSpan is = new ImageSpan(d);
        Log.i(  "image added"," done ");
        SpannableStringBuilder ss = new SpannableStringBuilder(".\n");
        ss.setSpan(is,0,(".\n").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new specific_note(),0,(".\n".length()),0);
        dis.append(ss,0,(".\n").length());
        dis.setMovementMethod(LinkMovementMethod.getInstance());
        dis.refreshDrawableState();

    }

}
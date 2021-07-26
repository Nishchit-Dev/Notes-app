package com.soical.to_do;

import com.soical.to_do.databackend.data;
import com.soical.to_do.databackend.dbhelper;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String weekDay = null;

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                weekDay = "Sunday" ;
                break;
            case Calendar.MONDAY:
                weekDay = "Monday" ;

                break;
            case Calendar.TUESDAY:
                weekDay = "Tuesday" ;

                break;
            case Calendar.WEDNESDAY:
                weekDay = "Wednesday" ;

                break;
            case Calendar.THURSDAY:
                weekDay = "Thursday" ;

                break;
            case Calendar.FRIDAY:
                weekDay = "Friday" ;

                break;
            case Calendar.SATURDAY:
                weekDay = "Saturday" ;

                break;
        }

        new dbhelper(this).booting_process();
            Intent i = new Intent(this , Notes_screen.class);
            i.putExtra("day",weekDay);
            startActivity(i);

    }

}
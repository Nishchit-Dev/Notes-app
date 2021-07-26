package com.soical.to_do;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soical.to_do.adapter.todo_adapter;
import com.soical.to_do.adapter.todo_adapter.OnLongClickListener;
import com.soical.to_do.databackend.data;
import com.soical.to_do.databackend.dbhelper;

import java.util.Random;
import java.util.SortedSet;
import java.util.zip.Inflater;

public class Notes_screen extends AppCompatActivity  {

    private  RecyclerView rv ;

    public static ImageView img ;
    private static todo_adapter todo ;
    private  StaggeredGridLayoutManager sgl;
    private TextView days ;
    private  Notification.Builder notification;
//    private SearchView searchView ;searc
    private android.widget.SearchView searchView ;


    @Override
    protected void onResume() {
        super.onResume();
        updating_recycler();
    }
    public void imgVisibility(){

        img = findViewById(R.id.emptyState);
        if (new todo_adapter(getApplicationContext()).getItemCount() >= 1   ){
            img.setVisibility(View.INVISIBLE);
        }else{
            img.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_screen);
        imgVisibility();

        // searchview
        searchView  = findViewById(R.id.searchview);
        // day textview
        days = findViewById(R.id.day);

        //intent
        Bundle extra = getIntent().getExtras();
        String day = extra.getString("day");
        days.setText(day);

        // recyclerviews elements
        rv = findViewById(R.id.Rv);
        todo = new todo_adapter(getApplicationContext());
        rv.setAdapter(todo);
        sgl = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(sgl);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("seticonified");
               // searchView.setIconifiedByDefault(true);
                searchView.setIconified(false);

            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ImageView img = findViewById(R.id.insert_notes);
                img.setVisibility(View.VISIBLE);

                new dbhelper(getApplicationContext()).booting_process();
                updating_recycler();
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img = findViewById(R.id.insert_notes);
                img.setVisibility(View.INVISIBLE);
            }
        });
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    System.out.println("has focus on search bar");
                }else{
                    System.out.println("does not have focus ");
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                    return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.toString().isEmpty()){
                    todo_adapter todoobj =new todo_adapter(getApplicationContext());
                    todoobj.getFilter().filter((CharSequence) newText);


                }else{
                    new dbhelper(getApplicationContext()).booting_process();
                    updating_recycler();
                }
                imgVisibility();

                return false;

            }
        });

        todo.setOnClickListener(new todo_adapter.OnClickListener() {
            @Override
            public void onItemClick(int postion) {
                    System.out.println(data.todo_id.get(postion));
                    System.out.println(data.todo_time.get(postion));
                    System.out.println(data.todo_title.get(postion));
                    System.out.println(data.todo_dis.get(postion));
                System.out.println( new todo_adapter(getApplicationContext()).getItemCount());
                    Intent int1 = new Intent(getApplicationContext(), specific_note.class);

                    int o = (int) data.todo_id.get(postion);
                    int1.putExtra("id", o);
                    int o1 = postion;
                    int1.putExtra("position", o1);
                    startActivity(int1);
                    updating_recycler();


                }
        });


        todo.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public void onItemLongClick(int position) {
                try{
                System.out.println("id: " + data.todo_id.get(position));
                System.out.println(data.todo_time.get(position));
                System.out.println("title: " + data.todo_title.get(position));
                System.out.println(data.todo_dis.get(position));
                System.out.println("position in List: " + position);

                // alertdialog box
                AlertDialog.Builder bulider = new AlertDialog.Builder(Notes_screen.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View customView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dailogbox,viewGroup,false);
                bulider.setView(customView);
                AlertDialog dialog = bulider.create() ;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


                TextView caution =customView.findViewById(R.id.Caution);
                Button delete = customView.findViewById(R.id.delete);
                Button cancel = customView.findViewById(R.id.cancel);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            int id = (int) data.todo_id.get(position);
                            System.out.println("id: " + id);
                            new dbhelper(getApplicationContext()).delete_specific(id);
                            updating_recycler();
                            new todo_adapter(getApplicationContext()).update_specific(position);


                        }catch(Exception e ){
                            System.out.println(e.getCause());
                        }
                        finally {
                            dialog.cancel();
                            imgVisibility();

                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        dialog.cancel();
                    }
                });
                }catch(Exception e ){
                    System.out.println(e.getCause());
                }
            }
        });
    }

    public void clearData(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Notes_screen.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View customview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dailogbox,viewGroup,false);

        builder.setView(customview);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView caution = customview.findViewById(R.id.Caution);
        Button delete = customview.findViewById(R.id.delete);
        Button cancel = customview.findViewById(R.id.cancel);

        caution.setText("Do you want to delete all note ?");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new dbhelper(getApplicationContext()).delete();

                    new todo_adapter(getApplicationContext()).dataCleared();
                    updating_recycler();

                }catch(Exception e ){
                    System.out.println(e.getCause());
                }
                finally {
                    alertDialog.cancel();
                    imgVisibility();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void add(View view) {
        String title = "\uD83D\uDCDD New Note" ;
        new dbhelper(this).insertData( "" , "");
        imgVisibility();
        updating_recycler();

    }

    public String random() {
        Random random = new Random();
        String text = "";
        int r = random.nextInt(4);

        for (int i = 0; i < r + 1; i++) {
            text = text + "\n";
        }
        return text;
    }

    public void updating_recycler() {

        todo.notifyDataSetChanged();


    }

}
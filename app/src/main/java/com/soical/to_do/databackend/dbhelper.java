package com.soical.to_do.databackend;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import com.soical.to_do.adapter.todo_adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class dbhelper extends SQLiteOpenHelper {

    // todo_ tables elements
    private static String T_name = "test1" ;
    private static String T_C_dis = "description" ;
    private static String T_C_id = "id" ;
    private static String T_C_Date = "date" ;
    private static String T_C_title ="title";
    private static Context T_context ;

    // notify table elements
    private static String notifyT_name = "notify";
    private static String notifyT_date = "date" ;

    // complete table elements
    private static String T_name_complete = "complete" ;

    // here we are defining the constructor to call the super constructor so that
    // we can give the name of table , version etc
    public dbhelper(Context context){

        super(context,T_name,null,5);
        T_context = context;

    }

    // this function will create the table if the table exist than no need
    @Override
    public void onCreate(SQLiteDatabase db){
       // this is the SQL commands embed in the String
        // todo_table creation


               String create_todo = "CREATE TABLE "+T_name +
                "("
                +T_C_id+" INTEGER PRIMARY KEY ," // 0
                + T_C_dis + " TEXT ,"            // 1
                + T_C_Date + " TEXT ," +// 2
                T_C_title + " TEXT "+ //3
                ")";

               String create_notifi = "CREATE TABLE IF NOT EXISTS " +notifyT_name+
                       "("
                       + T_C_id + " INTEGER NOT NULL ,"
                       + notifyT_name + "DATE NOT NULL,"
                       +"FOREIGN KEY ("+T_C_id+") REFERENCES "+ T_name +"("+T_C_id+ ")"
                       + ")";
        db.execSQL(create_todo);
        db.execSQL(create_notifi);

    }

    // this function update the table if the latest table has arrived
    @Override
    public void onUpgrade(SQLiteDatabase db , int oldver, int newver){
        if(oldver != newver){
            System.out.println("varying version ");
            // deleting the table if exits
            db.execSQL("DROP TABLE IF EXISTS " + T_name);

           // again call the oncreate fun and creating new table
            onCreate(db);
        }
        db.close();
    }


    // inserting into the todo_table
    public void insertData(String dis,String title){

        SQLiteDatabase sq = getWritableDatabase();

        //  here the transaction help to maintain the consistency of the database
        sq.beginTransaction();

        try{
            System.out.println("executed insert data");

            ContentValues values = new ContentValues();

            values.put(T_C_Date , getDate_time());
            values.put(T_C_title,title);
            values.put(T_C_dis , dis);
            // sq.update(T_name,null,values);

            sq.insertOrThrow( T_name , null , values );
            booting_process();
            sq.setTransactionSuccessful();

        }catch(Exception e ){
            System.out.println(e.getCause());
        }
        finally {
            sq.endTransaction();
            sq.close();

            System.out.println("inserted ");
        }

    }

    public String getDate_time(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return sdf.format(date);
    }
    // method to delete the specific row in database
    public void delete(){
        System.out.println("database is clear ");
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();


        db.delete(T_name,null,null);
        booting_process();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    // the method is used to UPDATE the database
    public void update(String title ,String dis ,int id ){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        String where = "id = " + id;

        ContentValues values = new ContentValues();

        values.put(T_C_dis,dis );
        values.put(T_C_title,title);

        db.update(T_name ,values , where,null);


        booting_process();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }
    // this function is used to load the initial data when the app get started
    public void booting_process(){
        SQLiteDatabase sq = getReadableDatabase();

        Cursor cursor_todo  = sq.rawQuery("SELECT * FROM "+T_name, null);
        data_clear();

        while(cursor_todo.moveToNext()){
            data.todo_id.add(cursor_todo.getInt(0));

            data.todo_dis.add(cursor_todo.getString(1));

            data.todo_title.add(cursor_todo.getString(3));

            data.todo_time.add(cursor_todo.getString(2));

        }
    }
    public void delete_specific(int id){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        String where = "id = " + id;
        db.delete(T_name,where,null);
         db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        booting_process();

    }
    public void data_clear(){
        data.todo_title.clear();
        data.todo_dis.clear();
        data.todo_id.clear();
        data.todo_time.clear();
    }

}


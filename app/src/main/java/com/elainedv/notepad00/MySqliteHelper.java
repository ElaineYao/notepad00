package com.elainedv.notepad00;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import utils.Constant;

/**
 * Created by Elaine on 18/1/23.
 */

public class MySqliteHelper extends SQLiteOpenHelper {

    private static final String CREATE_NOTEPAD="create table notepad00 ("
            +"_id integer primary key autoincrement,"
            +"content text not null,"
            +"path text not null,"
            +"video text not null,"
            +"time text not null)";
    public Context mcontext;

    public MySqliteHelper(Context context) {
        super(context, Constant.DBNAME, null, 1);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NOTEPAD);
        Log.i("tag","--------onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

package com.elainedv.notepad00;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import utils.Constant;
import utils.DbManager;

import static android.support.v7.widget.RecyclerView.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button textbt, pngbt, videobt;
    private RecyclerView rv;
    private Intent intent;
    private MySqliteHelper dbHelper;
    private  SQLiteDatabase db;
    private Cursor cursor;
    private static MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textbt = (Button) findViewById(R.id.textbt);
        pngbt = (Button) findViewById(R.id.pngbt);
        videobt = (Button) findViewById(R.id.videobt);
        rv = (RecyclerView) findViewById(R.id.rv);
        dbHelper=DbManager.getInstance(this);
        db=dbHelper.getReadableDatabase();
        cursor=db.query(Constant.DBNAME,null,null,null,null,null,null);
        intent = new Intent(this, addContent.class);
        initview();
    }

    public void initview() {
        textbt.setOnClickListener(this);
        pngbt.setOnClickListener(this);
        videobt.setOnClickListener(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this);
        adapter.setCursor(cursor);
        rv.setAdapter(adapter);
    }


    public static MyAdapter getAdapter(){
        return adapter;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.textbt):
                intent.putExtra("flag", "1");
                startActivity(intent);
                break;
            case (R.id.pngbt):
                intent.putExtra("flag", "2");
                startActivity(intent);
                break;
            case (R.id.videobt):
                intent.putExtra("flag", "3");
                startActivity(intent);
                break;

        }
    }
}


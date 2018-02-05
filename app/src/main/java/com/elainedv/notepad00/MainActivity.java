package com.elainedv.notepad00;

import android.content.ContentValues;
import android.content.Context;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.Constant;
import utils.DbManager;

import static android.support.v7.widget.RecyclerView.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,MyAdapter.OnItemClickListener{

    private Button textbt, pngbt, videobt;
    private RecyclerView rv;
    private Intent intent;
    private MySqliteHelper dbHelper;
    private  SQLiteDatabase db;
    private Cursor cursor;
    private static MyAdapter adapter;
    private List<Pad> padList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textbt = (Button) findViewById(R.id.textbt);
        pngbt = (Button) findViewById(R.id.pngbt);
        videobt = (Button) findViewById(R.id.videobt);
        rv = (RecyclerView) findViewById(R.id.rv);
        padList=new ArrayList<Pad>();
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
        initPadList(cursor,padList);
        adapter = new MyAdapter(this,padList);
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);
    }

    public void initPadList(Cursor mcursor,List<Pad> list) {
        if (mcursor.moveToFirst()) {
           do {
                Pad pad = new Pad();
                pad.setId(mcursor.getInt(mcursor.getColumnIndex(Constant.ID)));
                pad.setContent(mcursor.getString(mcursor.getColumnIndex(Constant.CONTENT)));
                pad.setTime(mcursor.getString(mcursor.getColumnIndex(Constant.TIME)));
                pad.setPath(mcursor.getString(mcursor.getColumnIndex(Constant.PATH)));
                pad.setVideo(mcursor.getString(mcursor.getColumnIndex(Constant.VIDEO)));
                list.add(pad);
            } while (mcursor.moveToNext());
        }mcursor.close();
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

    @Override
    public void onItemClick(View v, int position) {
        Pad pad00=padList.get(position);
        Intent intent =new Intent(MainActivity.this,SelectAct.class);
        intent.putExtra(Constant.ID,pad00.getId());
        intent.putExtra(Constant.CONTENT,pad00.getContent());
        intent.putExtra(Constant.TIME,pad00.getTime());
        intent.putExtra(Constant.PATH,pad00.getPath());
        intent.putExtra(Constant.VIDEO,pad00.getVideo());
        startActivity(intent);
    }
}


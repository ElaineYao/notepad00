package com.elainedv.notepad00;

import android.Manifest;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.Constant;
import utils.DbManager;

public class SelectAct extends AppCompatActivity implements View.OnClickListener {

    private ImageView s_img1;
    private VideoView s_img2;
    private EditText s_et;
    private Button s_bt1, s_bt2;
    private int s_id;
    private String s_content, s_path, s_time, s_video;
    private MySqliteHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ContentValues values;
    private List<Pad> pads;
    private  Uri videoUri;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        s_img1 = (ImageView) findViewById(R.id.s_pngview);
        s_img2 = (VideoView) findViewById(R.id.s_videoview);
        s_et = (EditText) findViewById(R.id.s_edit_text);
        s_bt1 = (Button) findViewById(R.id.s_save);
        s_bt2 = (Button) findViewById(R.id.s_delete);
        dbHelper = DbManager.getInstance(this);
        db = dbHelper.getWritableDatabase();
        cursor=db.query(Constant.DBNAME,null,null,null,null,null,null);
        pads=new ArrayList<Pad>();
        initPadList(cursor,pads);
        values = new ContentValues();
        s_et.setCursorVisible(false);


        s_id = getIntent().getIntExtra(Constant.ID, 0);
        s_content = getIntent().getStringExtra(Constant.CONTENT);
        s_time = getIntent().getStringExtra(Constant.TIME);
        s_path = getIntent().getStringExtra(Constant.PATH);
        s_video = getIntent().getStringExtra(Constant.VIDEO);

        s_bt1.setOnClickListener(this);
        s_bt2.setOnClickListener(this);

        s_et.setText(s_content);
        if (s_path.equals("null")){
            s_img1.setVisibility(View.GONE);
        }else{
            s_img1.setVisibility(View.VISIBLE);
        }
        if (s_video.equals("null")) {
            s_img2.setVisibility(View.GONE);
        } else {
            s_img2.setVisibility(View.VISIBLE);
        }
        Bitmap bitmap0 = BitmapFactory.decodeFile(s_path);
        s_img1.setImageBitmap(bitmap0);
        if(Build.VERSION.SDK_INT>=24){
            videoUri= FileProvider.getUriForFile(this,"com.elainedv.notepad00.fileprovider00",new File(s_video));
        }else{
            videoUri=Uri.fromFile(new File(s_video));
        }
        s_img2.setVideoURI(videoUri);
        s_img2.start();
        /*try {
            mediaPlayer.setDataSource(s_video);
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        s_img2.setOnClickListener(this);
        */
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.s_save:
                pads.clear();
                values.put(Constant.CONTENT, s_et.getText().toString());
                db.update(Constant.DBNAME, values, "_id=?", new String[]{s_id + ""});
                cursor=db.query(Constant.DBNAME,null,null,null,null,null,null);
                initPadList(cursor,pads);
                MainActivity.getAdapter().setMpads(pads);
                MainActivity.getAdapter().notifyDataSetChanged();
                finish();
                break;
            case R.id.s_delete:
                db.delete(Constant.DBNAME, "_id=?", new String[]{s_id + ""});
                pads.remove(s_id);
                MainActivity.getAdapter().setMpads(pads);
                MainActivity.getAdapter().notifyItemRemoved(s_id);
                //MainActivity.getAdapter().notifyItemRangeChanged(s_id-1,pads.size());
                MainActivity.getAdapter().notifyDataSetChanged();
                finish();
                break;
            /*case R.id.s_videoview :
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
                break;
                */
            default:
        }
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
}

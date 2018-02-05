package com.elainedv.notepad00;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.Constant;
import utils.DbManager;

public class addContent extends AppCompatActivity implements View.OnClickListener {

    private String val;
    private ImageView pngView;
    private VideoView videoView;
    private EditText editText;
    private Button bt1, bt2;
    private MySqliteHelper dbHelper;
    private SQLiteDatabase db;
    private Uri imageUri,videoUri;
    private File outputImage,outputVideo;
    private Cursor cursor;
    private List<Pad> pads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        val = getIntent().getStringExtra("flag");
        dbHelper = DbManager.getInstance(this);
        db = dbHelper.getWritableDatabase();
        initview();
    }

    public void initview() {
        pngView = (ImageView) findViewById(R.id.pngview);
        videoView = (VideoView)findViewById(R.id.videoview);
        editText = (EditText) findViewById(R.id.edit_text);
        pads=new ArrayList<Pad>();
        bt1 = (Button) findViewById(R.id.save);
        bt1.setOnClickListener(this);
        bt2 = (Button) findViewById(R.id.notsave);
        bt2.setOnClickListener(this);
        switch (val) {
            case ("1"):
                break;
            case ("2"):
                getPhoto();
                break;
            case ("3"):
                getVideo();
                break;

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                addDb();
                finish();
                break;
            case R.id.notsave:
                finish();
                break;

        }
    }

    public void addDb() {
        ContentValues values = new ContentValues();
        values.put(Constant.CONTENT, editText.getText().toString());
        values.put(Constant.TIME, getTime());
        values.put(Constant.PATH,outputImage+"");
        values.put(Constant.VIDEO,outputVideo+"");
        db.insert(Constant.DBNAME, null, values);
        cursor=db.query(Constant.DBNAME,null,null,null,null,null,null);
        initPadList(cursor,pads);
        MainActivity.getAdapter().setMpads(pads);
        MainActivity.getAdapter().notifyDataSetChanged();
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    public void getPhoto() {
        pngView.setVisibility(View.VISIBLE);
        outputImage = new File(getExternalCacheDir(), "output_image_" + getTime() + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this, "com.elainedv.notepad00.fileprovider00", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, Constant.TAKE_PHOTO);
    }
    public void getVideo(){
        videoView.setVisibility(View.VISIBLE);
        outputVideo = new File(Environment.getExternalStorageDirectory(),"output_video"+getTime()+".mp4");
        Log.d("tag",outputVideo+"");
        try{
            if(outputVideo.exists()){
                outputVideo.delete();
            }
                outputVideo.createNewFile();
            } catch(IOException e){
                e.printStackTrace();
            }
            if(Build.VERSION.SDK_INT>=24){
                videoUri=FileProvider.getUriForFile(this,"com.elainedv.notepad00.fileprovider00",outputVideo);
        } else {
                videoUri=Uri.fromFile(outputVideo);
            }
            Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,videoUri);
            startActivityForResult(intent,Constant.TAKE_VIDEO);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (Constant.TAKE_PHOTO):
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap00 = BitmapFactory.decodeStream(getContentResolver().openInputStream((imageUri)));
                        pngView.setImageBitmap(bitmap00);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case (Constant.TAKE_VIDEO):
                if(resultCode == RESULT_OK){
                        videoView.setVideoURI(videoUri);
                        videoView.start();
                }
                break;


        }
    }
}

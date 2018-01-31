package com.elainedv.notepad00;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import utils.Constant;

/**
 * Created by Elaine on 18/1/28.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    public MyAdapter(Context context) {
       this.context=context;
    }

    public void setCursor(Cursor cursor){
        this.cursor=cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        String content = cursor.getString(cursor.getColumnIndex(Constant.CONTENT));
        String time = cursor.getString(cursor.getColumnIndex(Constant.TIME));
        String path = cursor.getString(cursor.getColumnIndex(Constant.PATH));
        String video00=cursor.getString(cursor.getColumnIndex(Constant.VIDEO));
        holder.content.setText(content);
        holder.time.setText(time);
        holder.png.setImageBitmap(getImageThumbnail(path, 200, 200));
        holder.video.setImageBitmap(getVideoThumbnail(video00,200,200,MediaStore.Images.Thumbnails.MICRO_KIND));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView png, video;
        TextView content, time;


        public ViewHolder(View itemView) {
            super(itemView);
            png = (ImageView) itemView.findViewById(R.id.image00);
            video = (ImageView) itemView.findViewById(R.id.image01);
            content = (TextView) itemView.findViewById(R.id.text00);
            time = (TextView) itemView.findViewById(R.id.text01);
        }
    }

    public Bitmap getImageThumbnail(String uri, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri, options);
        options.inJustDecodeBounds = false;
        int bewidth = options.outWidth / width;
        int beheight = options.outHeight / height;
        int be = 1;
        if (bewidth < beheight) {
            be = bewidth;
        } else {
            be = beheight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    public Bitmap getVideoThumbnail(String uri, int width, int height,int kind) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri, options);
        options.inJustDecodeBounds = false;
        int bewidth=options.outWidth/width;
        int beheight=options.outHeight/height;
        int be = 1;
        if(bewidth<beheight){
            be = bewidth;
        }else{
            be = beheight;
        }
        if(be <=0){
            be=1;
        }
        options.inSampleSize= be;
        bitmap=BitmapFactory.decodeFile(uri,options);
        bitmap=ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return  bitmap;
    }


}

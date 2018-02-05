package com.elainedv.notepad00;

/**
 * Created by Elaine on 18/2/4.
 */

public class Pad {
    private int id;
    private String content,time,path,video;

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getPath() {
        return path;
    }

    public String getTime() {
        return time;
    }

    public String getVideo() {
        return video;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}

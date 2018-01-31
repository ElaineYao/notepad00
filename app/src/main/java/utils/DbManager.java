package utils;

import android.content.Context;

import com.elainedv.notepad00.MySqliteHelper;

/**
 * Created by Elaine on 18/1/23.
 */

public class DbManager {

    private static MySqliteHelper helper;
    public static MySqliteHelper getInstance(Context context){
        if(helper==null){
            return helper=new MySqliteHelper(context);
        }else
            return helper;
    }

}

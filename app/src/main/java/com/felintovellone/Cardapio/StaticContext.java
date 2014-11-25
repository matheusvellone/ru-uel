package com.felintovellone.Cardapio;

import android.app.Application;
import android.content.Context;

/**
 * Created by Matheus on 22/11/2014.
 */
public class StaticContext extends Application{

    private static Context context;

    public void onCreate(){
        super.onCreate();
        StaticContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return StaticContext.context;
    }
}
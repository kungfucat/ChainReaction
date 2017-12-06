package me.kungfucat.chainreaction;

import android.content.Context;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by harsh on 12/6/17.
 */

public class Helper {

    public static GifDrawable red1, red2, red3, red4,
            blue1, blue2, blue3, blue4,
            empty;

    public static void initialise(Context context){
        try {
            red1=new GifDrawable(context.getResources(),R.raw.red1);
            red2 = new GifDrawable(context.getResources(), R.raw.red2);
            red3 = new GifDrawable(context.getResources(), R.raw.red3);
            red4 = new GifDrawable(context.getResources(), R.raw.red4);
            blue1 = new GifDrawable(context.getResources(), R.raw.blue1);
            blue2 = new GifDrawable(context.getResources(), R.raw.blue2);
            blue3 = new GifDrawable(context.getResources(), R.raw.blue3);
            blue4 = new GifDrawable(context.getResources(), R.raw.blue4);
            empty = new GifDrawable(context.getResources(), R.raw.empty);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

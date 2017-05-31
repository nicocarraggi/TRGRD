package com.example.nicolascarraggi.trgrd.logging;

import android.util.Log;

import com.example.nicolascarraggi.trgrd.rulesys.MyTime;

/**
 * Created by nicolascarraggi on 30/05/17.
 */

public class MyLogger {

    public static void debugLog(String tag, String text){
        Log.d(tag,text);
    }

    public static void timeframeLog(String of, MyTime start, MyTime end){
        Log.d("TRGRD","TIME FRAME LOG of "+of+": start = "+start+", end = "+end+", difference = "+start.getDifferenceString(end));
        // TODO log to file?
    }

}

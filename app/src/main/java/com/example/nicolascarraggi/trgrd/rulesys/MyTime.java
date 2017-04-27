package com.example.nicolascarraggi.trgrd.rulesys;

import android.util.Log;

import java.util.Date;

/**
 * Created by nicolascarraggi on 27/04/17.
 */

public class MyTime extends Date {

    public MyTime() {
        super();
    }

    @Override
    public String toString() {

        int hours = this.getHours();
        int minutes = this.getMinutes();
        String h,m;

        if (hours<10){
            h = "0"+hours;
        } else h = ""+hours;

        if (minutes<10){
            m = "0"+minutes;
        } else m = ""+minutes;

        return h+":"+m;
    }

    public boolean equals(MyTime otherTime){
        return (this.getHours() == otherTime.getHours() && this.getMinutes() == otherTime.getMinutes());
    }

    public boolean isBetween(MyTime from, MyTime to){
        // TODO !!
        return false;
    }
}

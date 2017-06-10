package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.Calendar;

/**
 * Created by nicolascarraggi on 27/04/17.
 */

public class MyTime {

    private Calendar calendar;

    public MyTime() {
        this.calendar = Calendar.getInstance();
    }

    public MyTime(int hours, int minutes){
        this.calendar = Calendar.getInstance();
        this.calendar.set(Calendar.HOUR_OF_DAY,hours);
        this.calendar.set(Calendar.MINUTE, minutes);
    }

    public int getHours(){
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public void setHours(int hours){
        calendar.set(Calendar.HOUR_OF_DAY,hours);
    }

    public int getMinutes(){
        return calendar.get(Calendar.MINUTE);
    }

    public void setMinutes(int minutes){
        this.calendar.set(Calendar.MINUTE, minutes);
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

    public boolean equals(MyTime other){
        return (this.getHours() == other.getHours() && this.getMinutes() == other.getMinutes());
    }
    
    public boolean isBefore(MyTime other){
        if(this.getHours() == other.getHours()){
            return (this.getMinutes() <= other.getMinutes());
        } else if(this.getHours() < other.getHours()){
            return true;
        }
        return false;
    }

    public boolean isAfter(MyTime other){
        if(this.getHours() == other.getHours()){
            return (this.getMinutes() >= other.getMinutes());
        } else if(this.getHours() > other.getHours()){
            return true;
        }
        return false;
    }

    public boolean isBetween(MyTime from, MyTime to){
        MyTime dayEnd = new MyTime(23,59);
        MyTime dayStart = new MyTime(0,0);
        //     1) to is later in the day than from!
        // OR  2) to is earlier in the day than from! (goes over midnight!)
        if(to.isAfter(from)){
            return (this.isAfter(from) && this.isBefore(to));
        } else {
            // this is between from and midnight (=23:59) or between midnight (00:00) and to!
            return (this.isAfter(from) && this.isBefore(dayEnd)
                        || this.isAfter(dayStart) && this.isBefore(to));
        }
    }

    public String getDifferenceString(MyTime other) {
        String result = "";
        double difference =  ((double) other.calendar.getTime().getTime() - this.calendar.getTime().getTime())/1000;
        return result+difference+"s";
    }
}

package com.example.nicolascarraggi.trgrd.rulesys.devices;

/**
 * Created by nicolascarraggi on 3/07/17.
 */

public class PebbleScore {

    private int left, right;

    public PebbleScore(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void addLeft(int x){
        this.left += x;
    }

    public void addRight(int x){
        this.right += x;
    }

    public void subtractLeft(int x){
        this.left -= x;
        if(left<0) this.left=0;
    }

    public void subtractRight(int x){
        this.right -= x;
        if(right<0) this.right=0;
    }

    public String toScoreString(){
        String middle = "  -  ";
        if(left>99 || right>99){
            middle = "-";
        }
        return left+middle+right;
    }

}

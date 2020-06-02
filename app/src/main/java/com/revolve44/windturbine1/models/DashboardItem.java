package com.revolve44.windturbine1.models;

public class DashboardItem {
    private int mImageResource;
    private String mText1;
   // private String mText2;
    public DashboardItem(int imageResource, String text1) {
        mImageResource = imageResource;
        mText1 = text1;
       // mText2 = text2;
    }
    public void changeText1(String text) {
        mText1 = text;
    }
    public int getImageResource() {
        return mImageResource;
    }
    public String getText1() {
        return mText1;
    }
//    public String getText2() {
//        return mText2;
//    }
}

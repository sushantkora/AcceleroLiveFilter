package com.sushantkora.cc.accelerolivefilter;

/**
 * Created by cc on 27-07-2017.
 */

public  class AccelerationData{
    float x;
    float y;
    float z;
    double normal,filtered;
    int index;

    public AccelerationData(int index,float x,float y,float z,double normal,double filtered){
        this.index=index;
        this.x=x;
        this.y=y;
        this.z=z;
        this.normal=normal;
        this.filtered=filtered;
    }
    public double NormalData(){
        return Math.sqrt(x*x+y*y+z*z);
    }
}
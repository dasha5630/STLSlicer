package com.example.tdv.slicing;

import java.util.Arrays;

public class Triangle implements Comparable{

    private Point normal;
    private Point first;
    private Point second;
    private Point third;
    private float []Zarray;
    private float Zmin;
    private float Zmax;

    public Triangle(Point first, Point second, Point third) {
        this.normal = null;
        this.first = first;
        this.second = second;
        this.third = third;
        this.Zarray = new float[]{this.first.getZ(), this.second.getZ(), this.third.getZ()};
        //Arrays.sort(Zarray);
        this.Zmax = Zarray[2];
        this.Zmin = Zarray[0];
    }

    public Triangle(){

    }

    public Point getFirst() {
        return first;
    }

    public Point getSecond() {
        return second;
    }

    public Point getThird() {
        return third;
    }

    public void setFirst(Point first) {
        this.first = first;
    }

    public void setSecond(Point second) {
        this.second = second;
    }

    public void setThird(Point third) {
        this.third = third;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Triangle){
            Triangle another = (Triangle)o;
            if(another.Zmin < this.Zmin)      return 1;
            else if(another.Zmin > this.Zmin) return -1;
            else                              return 0;
        } else {
            throw new IllegalArgumentException("type is't suitable to compare");
        }

    }
}

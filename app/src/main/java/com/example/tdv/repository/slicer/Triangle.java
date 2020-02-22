package com.example.tdv.repository.slicer;

import java.util.ArrayList;
import java.util.Collections;

public class Triangle implements Comparable{

    private Point normal;
    private Point first;
    private Point second;
    private Point third;
    private ArrayList<Point> ZToHigh = new ArrayList<Point>();
    private float Zmin;
    private float Z2;
    private float Zmax;

    public Triangle(Point first, Point second, Point third) {
        this.normal = null;
        this.first = first;
        this.second = second;
        this.third = third;
        ZToHigh.add(this.first);
        ZToHigh.add(this.second);
        ZToHigh.add(this.third);
        Collections.sort(ZToHigh);
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

    public Point getNormal() {
        return normal;
    }

    public ArrayList<Point> getZToHigh() {
        return ZToHigh;
    }

    public float getZmin() {
        return Zmin;
    }

    public float getZmax() {
        return Zmax;
    }

    public float getZ2() {
        return Z2;
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

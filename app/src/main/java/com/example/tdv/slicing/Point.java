package com.example.tdv.slicing;

public class Point implements Comparable{

    private float x;
    private float y;
    private float z;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public  Point(){

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Point){
            Point another = (Point) o;
            if(another.getZ() < this.getZ())      return 1;
            else if(another.getZ() > this.getZ()) return -1;
            else                                  return 0;
        } else {
            throw new IllegalArgumentException("type is't suitable to compare");
        }
    }
}

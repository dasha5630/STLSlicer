package com.example.tdv.repository.slicer;

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
        if(o instanceof Point) {
            Point another = (Point) o;
            return Float.compare(this.getZ(), another.getZ());
        } else {
            throw new IllegalArgumentException("type is't suitable to compare");
        }
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Point) {
            Point another = (Point) o;
            return (Float.compare(this.x, another.x) == 0
                    && Float.compare(this.y, another.y) == 0
                    && Float.compare(this.z, another.z) == 0);
        } else {
            throw new IllegalArgumentException("type is't suitable to compare");
        }
    }

    @Override
    public int hashCode(){
        return Float.floatToIntBits(this.x)
                + Float.floatToIntBits(this.y)
                + Float.floatToIntBits(this.z);
    }



    /*
    TODO:
      override equals and hash
     */
}

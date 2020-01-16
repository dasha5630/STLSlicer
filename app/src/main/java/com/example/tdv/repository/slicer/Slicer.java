package com.example.tdv.repository.slicer;

import android.util.Log;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;

public class Slicer {

    private  float numberOfTriangle = 0;
    private  boolean flag = false;
    private  ArrayList<Triangle> list = new ArrayList<Triangle>();
    private  ArrayList<Triangle> activeTriangleList = new ArrayList<>();
    private  ArrayList<Point> points = new ArrayList<>();

    private  Float currentZ = 5f;

    public ArrayList<Point> slicing(InputStream in){
        ParseInputStream(in);
        return slicingAlgorithm();
    }

    private void ParseInputStream(InputStream in) {
        Point point1 = new Point();
        Point point2 = new Point();
        Point point3 = new Point();
        byte[] buff = new byte[4];
        int cntr = 0;
        Float fl = 0f;
        ByteBuffer bf;

        try {
            in.skip(80);
            in.read(buff);
            bf = ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN);
            numberOfTriangle = bf.getInt();
            in.skip(12);
            while ((in.read(buff)) != -1){
                bf = ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN);
                fl = bf.getFloat();
                if(Float.compare(fl, 0.01f) <= 0) fl = 0f;
                cntr++;
                switch (cntr){
                    case 1:
                        point1.setX(fl);
                        break;

                    case 2:
                        point1.setY(fl);
                        break;
                    case 3:
                        point1.setZ(fl);
                        break;

                    case 4:
                        point2.setX(fl);
                        break;

                    case 5:
                        point2.setY(fl);
                        break;
                    case 6:
                        point2.setZ(fl);
                        break;

                    case 7:
                        point3.setX(fl);
                        break;

                    case 8:
                        point3.setY(fl);
                        break;

                    case 9:
                        point3.setZ(fl);
                        list.add(new Triangle(point1, point2, point3));
                        point1 = new Point();
                        point2 = new Point();
                        point3 = new Point();
                        in.skip(14);
                        cntr = 0;
                        break;

                    default:
                        break;
                }
            }

            if(!list.isEmpty()){
                flag = true;
                Collections.sort(list);
            }
        }
        catch (Exception e) {
            Log.e("MainActivity", "ParseInputStream exception: " + e.getMessage());
        }


    }

    private ArrayList<Point> slicingAlgorithm(){
        float x = 0;
        float y = 0;
        float z = 0;

        for(Triangle it:list){
            if(it.getZToHigh().get(0).getZ() <= currentZ) {
                if (it.getZToHigh().get(2).getZ() >= currentZ) {
                    activeTriangleList.add(it);
                } else {
                    if(activeTriangleList.contains(it))
                        activeTriangleList.remove(it);
                }
            }
        }

        if(!activeTriangleList.isEmpty()){
            for (Triangle it:activeTriangleList) {
                float x1 = it.getZToHigh().get(0).getX();
                float y1 = it.getZToHigh().get(0).getY();
                float z1 = it.getZToHigh().get(0).getZ();

                float x2 = it.getZToHigh().get(2).getX();
                float y2 = it.getZToHigh().get(2).getY();
                float z2 = it.getZToHigh().get(2).getZ();



                if(Float.compare(z1,z2) >= 0) continue;

                float t = (currentZ - z1)/(z2 - z1);
                x = x1 + t * (x2-x1);
                y = y1 + t * (y2-y1);
                z = currentZ;
                points.add(new Point(x, y, z));

                if (Float.compare(it.getZToHigh().get(1).getZ(), currentZ) >= 0) {
                    x1 = it.getZToHigh().get(0).getX();
                    y1 = it.getZToHigh().get(0).getY();
                    z1 = it.getZToHigh().get(0).getZ();

                    x2 = it.getZToHigh().get(1).getX();
                    y2 = it.getZToHigh().get(1).getY();
                    z2 = it.getZToHigh().get(1).getZ();

                    t = (currentZ - z1)/(z2 - z1);
                    x = x1 + t * (x2-x1);
                    y = y1 + t * (y2-y1);
                    z = currentZ;
                    points.add(new Point(x, y, z));
                } else {
                    x1 = it.getZToHigh().get(1).getX();
                    y1 = it.getZToHigh().get(1).getY();
                    z1 = it.getZToHigh().get(1).getZ();

                    x2 = it.getZToHigh().get(2).getX();
                    y2 = it.getZToHigh().get(2).getY();
                    z2 = it.getZToHigh().get(2).getZ();

                    if(Float.compare(z1,z2) >= 0) continue;

                    t = (currentZ - z1)/(z2 - z1);
                    x = x1 + t * (x2-x1);
                    y = y1 + t * (y2-y1);
                    z = currentZ;
                    points.add(new Point(x, y, z));
                }
            }
        }

        return points;
    }
}

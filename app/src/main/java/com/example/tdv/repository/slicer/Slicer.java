package com.example.tdv.repository.slicer;

import android.util.Log;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;

/*
 TODO:
  make it class with singleton pattern
 */
public class Slicer {

    private float numberOfTriangle;
    private ArrayList<Triangle> list;
    private ArrayList<Triangle> activeTriangleList;
    private ArrayList<Line> lines;
    private ArrayList<Point> points;
    private ArrayList<ArrayList<Point>> pointsFoPathArray;

    private static  Slicer instance;

    public Float currentZ;
    public Float step;

    private Slicer(){
        numberOfTriangle = 0;
        list = new ArrayList<>();
        activeTriangleList = new ArrayList<>();
        lines = new ArrayList<>();
        points = new ArrayList<>();
        pointsFoPathArray = new ArrayList<>();
        currentZ = 0f;
    }

    public static Slicer getInstance(){
        if(instance == null){
            instance = new Slicer();
        }
        return instance;
    }

    public void readFile(InputStream in){
        ParseInputStream(in);
    }

    public ArrayList<ArrayList<Point>> slice(){
        pointsFoPathArray.clear();
        pointsFoPathArray = linesToPathPoints(slicingAlgorithm());
        return pointsFoPathArray;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    private void ParseInputStream(InputStream in) {
        Point point1 = new Point();
        Point point2 = new Point();
        Point point3 = new Point();
        byte[] buff = new byte[4];
        int cntr = 0;
        float fl = 0f;
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
            in.close();
            if(!list.isEmpty()){
                Collections.sort(list);
            }
        }
        catch (Exception e) {
            Log.e("ShowSliceActivity", "ParseInputStream exception: " + e.getMessage());
        }


    }

    private ArrayList<Line> slicingAlgorithm(){
        float x = 0;
        float y = 0;
        float z = 0;

        //error list
        for(Triangle it:list){
            if(it.getZToHigh().get(0).getZ() <= currentZ) {
                if (it.getZToHigh().get(2).getZ() >= currentZ) {
                    if(!activeTriangleList.contains(it))
                        activeTriangleList.add(it);
                } else {
                    if(activeTriangleList.contains(it)) //??? indexOf -> it.equal()
                        activeTriangleList.remove(it);
                }
            }
        }

        if(!activeTriangleList.isEmpty()){
            for (Triangle it:activeTriangleList) {
                Point p1;
                Point p2;

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

                p1 = new Point(x, y, z);

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

                    p2 = new Point(x, y, z);
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

                    p2 = new Point(x, y, z);
                }
                lines.add(new Line(p1, p2));
            }
        }
        return lines;
    }

    private ArrayList<ArrayList<Point>> linesToPathPoints(ArrayList<Line> lines){
        ArrayList<ArrayList<Point>> pointsFoPathArray = new ArrayList<>();
        while (!lines.isEmpty()){
            ArrayList<Point> points = new ArrayList<>();
            Line line = lines.get(0);
            int size = lines.size();
            lines.remove(line);
            for (int i = 0; i < size; i++){
                if(points.size() > 2
                        && points.get(0) == points.get(points.size() - 1)){
                }
                for(Line l:lines){
                    if(line.getPoint1().equals(l.getPoint1()) || line.getPoint1().equals(l.getPoint2())){
                        if((lines.size() + 1) == size) points.add(line.getPoint2());
                        points.add(line.getPoint1());
                        lines.remove(l);
                        line = l;
                        break;
                    } else if(line.getPoint2().equals(l.getPoint1()) || line.getPoint2().equals(l.getPoint2())){
                        if((lines.size() + 1) == size) points.add(line.getPoint1());
                        points.add(line.getPoint2());
                        lines.remove(l);
                        line = l;
                        break;
                    }
                }
            }
            pointsFoPathArray.add(points);
        }
        return pointsFoPathArray;
    }
}

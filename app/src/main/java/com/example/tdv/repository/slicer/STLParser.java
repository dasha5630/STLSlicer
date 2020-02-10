package com.example.tdv.repository.slicer;

import android.util.Log;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;

public class STLParser {
    public static float numberOfTriangle;
    public static ArrayList<Triangle> list = new ArrayList<>();


    public static void readFile(InputStream in){
        ParseInputStream(in);
    }

    private static void ParseInputStream(InputStream in) {
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
}

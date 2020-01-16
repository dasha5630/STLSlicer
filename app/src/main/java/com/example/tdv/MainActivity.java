package com.example.tdv;

import android.content.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Point;

import com.example.tdv.contract.IPresenter;
import com.example.tdv.contract.IViewSlicerScreen;
//import com.example.tdv.repository.slicer.Point;
import com.example.tdv.repository.slicer.Triangle;

public class MainActivity extends Activity implements IViewSlicerScreen {

    IPresenter presenter;

    String FILENAME = "file";
//
//    float numberOfTriangle = 0;
//
//    boolean flag = false;
//
    View dv;
//    ArrayList<Triangle> list = new ArrayList<Triangle>();
//    ArrayList<Triangle> activeTriangleList = new ArrayList<>();
    ArrayList<Point> points = new ArrayList<>();
//
//    Float currentZ = 5f;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawView(this);
        setContentView(dv);// setContentView(R.layout.activity_main);
        presenter = new Presenter(this);

        Intent intent = getIntent();
        String action = intent.getAction();

        if (action.compareTo(Intent.ACTION_VIEW) == 0) {
            String scheme = intent.getScheme();
            ContentResolver resolver = getContentResolver();
             Uri uri = intent.getData();
                String name = uri.getLastPathSegment();

                Log.v("tag", "File intent detected: " + action + " : " + intent.getDataString() + " : " + intent.getType() + " : " + name);
                InputStream input = null;
                try {
                    input = resolver.openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                FILENAME = name;
                //ParseInputStream(input);
                presenter.fileReceived(input);
        }
    }
//    public void onclick(View v) {
//        switch (v.getId()) {
//            case R.id.btnRead:
//                if(FILENAME != "file")
//                    openText(v);
//                break;
//        }
//    }

 /*   private void ParseInputStream(InputStream in) {
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
                dv.invalidate();
            }
        }
        catch (Exception e) {
            Log.e("MainActivity", "ParseInputStream exception: " + e.getMessage());
        }


    }*/

    public void openText(View view){

        FileInputStream fin = null;
        TextView textView = (TextView) findViewById(R.id.open_text);
        try {
            fin = openFileInput(FILENAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String (bytes);
            textView.setText(text);
        }
        catch(IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{

            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void showSlice(ArrayList<Point> points) {
        this.points.addAll(points);
        //dv.invalidate();
    }

    class DrawView extends View {

        Paint p;
        Rect rect;

        public DrawView(Context context) {
            super(context);
            p = new Paint();
            rect = new Rect();
        }

        @Override
        protected void onDraw(Canvas canvas) {
//
//            float x = 0;
//            float y = 0;
//            float z = 0;
            // заливка канвы цветом
            canvas.drawARGB(80, 102, 204, 255);
            // настройка кисти
            // красный цвет
            p.setColor(Color.RED);
            // толщина линии = 10
            p.setStrokeWidth(10);


 //           if(flag){
/*                for(Triangle it:list){
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
                }*/

                if(!points.isEmpty()) {
                    for (Point it : points) {
                        canvas.drawPoint(it.x * 100, it.y * 100, p);
                    }
                    //flag = false;
                }
    //        }

        }

    }
}

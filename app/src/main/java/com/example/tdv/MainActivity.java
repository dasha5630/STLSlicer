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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tdv.R;
import com.example.tdv.slicing.Point;
import com.example.tdv.slicing.Triangle;

public class MainActivity extends Activity {

    final String LOG_TAG = "myLogs";

    String FILENAME = "file";

    float numberOfTriangle = 0;

    boolean flag = false;

    View dv;
    ArrayList<Triangle> list = new ArrayList<Triangle>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawView(this);
        setContentView(dv);// setContentView(R.layout.activity_main);

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
                FILENAME = (String) name;
                ParseInputStream(input);
        }
    }
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.btnRead:
                if(FILENAME != "file")
                    openText(v);
                break;
        }
    }

    private void ParseInputStream(InputStream in) {
/*        float fl = 0;
        int i = 0;*/

        Point point1 = new Point();
        Point point2 = new Point();
        Point point3 = new Point();

        try {
            byte[] buff = new byte[4];
            byte element = 0;
            int cntr = 0;
            Float fl = 0f;
            ByteBuffer bf;
            //DataInputStream dis = new DataInputStream(in);
            in.skip(80);

            in.read(buff);
            bf = ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN);
            fl = bf.getFloat();
            fl.byteValue();
            numberOfTriangle = fl;


            in.skip(12);
            while ((in.read(buff)) != -1){
                bf = ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN);
                fl = bf.getFloat();
                fl.byteValue();
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
                        flag = true;
                        in.skip(14);
                        cntr = 0;
                        break;

                    default:
                        break;
                }



            }
            if(!list.isEmpty()){
                Collections.sort(list);
            }
/*            in.skip(84);
            int element = 0;
            while ((element = in.read()) != -1){
                fl = element << 8*i;
                i++;
                if(i >= 5){
                    i = 0;
                }

            }*/
/*            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            reader.skip(80);
            in.skip(80);
            Scanner scanner = new Scanner(in);
            Point point = new Point();
            Point point1 = new Point();
            Point point2 = new Point();
            Point point3 = new Point();
            int counter = 0;
            scanner.hasNextFloat();
            numberOfTriangle = scanner.nextByte();
            scanner.hasNextFloat();
            numberOfTriangle = (scanner.nextByte() << 8);
            while (scanner.hasNextFloat()) {
                if(counter == 0)
                    point.setX(scanner.nextFloat());
                if(counter == 1)
                    point.setY(scanner.nextFloat());
                if(counter == 2)
                    point.setZ(scanner.nextFloat());
                if(counter == 3)
                    point1.setX(scanner.nextFloat());
                if(counter == 4)
                    point1.setY(scanner.nextFloat());
                if(counter == 5)
                    point1.setZ(scanner.nextFloat());
                if(counter == 6)
                    point2.setX(scanner.nextFloat());
                if(counter == 7)
                    point2.setY(scanner.nextFloat());
                if(counter == 8)
                    point2.setZ(scanner.nextFloat());
                if(counter == 9)
                    point3.setX(scanner.nextFloat());
                if(counter == 10)
                    point3.setY(scanner.nextFloat());
                if(counter == 11){
                    point3.setZ(scanner.nextFloat());
                    counter = 0;
                    list.add(new Triangle(point, point1, point2, point3));
                }
                counter++;
            }*/
        }
        catch (Exception e) {
            Log.e("MainActivity", "ParseInputStream exception: " + e.getMessage());
        }


    }

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
            // заливка канвы цветом
            canvas.drawARGB(80, 102, 204, 255);
            // настройка кисти
            // красный цвет
            p.setColor(Color.RED);
            // толщина линии = 10
            p.setStrokeWidth(10);

            if(flag){
                for (Triangle it:list){
                    float x1 = it.getFirst().getX();
                    float y1 = it.getFirst().getY();
                    float x2 = it.getSecond().getX();
                    float y2 = it.getSecond().getY();
                   // canvas.drawPoint(it.next().getFirst().getX(), it.next().getFirst().getY(), p);
                   canvas.drawLine(x1*100,y1*100,x2*100,y2*100,p);

                }

                dv.invalidate();
                //flag = false;

            }

        }

    }
}

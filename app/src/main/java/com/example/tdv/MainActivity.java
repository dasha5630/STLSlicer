package com.example.tdv;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.AbstractList;
import java.util.ArrayList;
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

    AbstractList<Triangle> list = new ArrayList<Triangle>();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        try {
            byte[] buff = new byte[4];
            byte element = 0;
            int cntr = -1;
            Float fl = 0f;
            ByteBuffer bf;
            //DataInputStream dis = new DataInputStream(in);
            in.skip(80);
            while ((in.read(buff)) != -1){
                bf = ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN);
                fl = bf.getFloat();
                fl.byteValue();

                if(cntr == -1){
                    numberOfTriangle = fl;
                }
                

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
}

package com.example.tdv;

import com.example.tdv.repository.slicer.STLParser;
import com.example.tdv.repository.slicer.Slicer;

import org.junit.Test;

import java.io.InputStream;



import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void readFileTest() throws NullPointerException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test.STL");
        STLParser.readFile(in);

        assertEquals(4, STLParser.numberOfTriangle, 0.01);
    }

}
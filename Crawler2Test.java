/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler2;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Samra
 */
public class Crawler2Test {
    
    public Crawler2Test() {
    }

    /**
     * Test of createIndex method, of class Crawler2.
     */
    @Test
    public void testCreateIndex() throws Exception {
        
        System.out.println("createIndex");
        File f1 = new File("C:\\Users\\Samra\\Desktop\\Crawler\\");
        Crawler2 instance = new Crawler2("C:\\Users\\Samra\\Desktop\\Crawler\\");
        boolean expResult = true;
        boolean result = instance.createIndex(f1);
        assertEquals(expResult, result);

    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.genesis;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author miles
 */
public class GAParametersTest {
    
    public GAParametersTest() {
    }

    @Test
    public void testSomeMethod() throws IOException {
        new GAParameters.GABuilder().build().saveToJSON(new File("test.json"));
    }
    
}

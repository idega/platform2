/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.experimental;


/**
 * @author palli
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DoubleToFloatTest {

    public static void main(String[] args) {
        double d = 17774635.0d;
        
        System.out.println("d = " + d);
        
        System.out.println("(float)d = " + (float)d);
        
        Double d2 = new Double(d);
        System.out.println("d2.floatValue() = " + d2.floatValue());
        System.out.println("d2.doubleValue() = " + d2.doubleValue());
        
        Float f = new Float(d);
        System.out.println("f.floatValue() = " + f.floatValue());
        System.out.println("f.doubleValue() = " + f.doubleValue());
        
    }
}

/*
 * $Id: TextControl3.java,v 1.1 2001/06/06 11:29:36 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.text.presentation.TextReader;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class TextControl3 extends TextControl {

  public void doMenu(){

    Table T = new Table(3,1);
     T.setWidth("100%");
     T.setBorder(0);
     T.setWidth(1,"50%");
     T.setWidth(3,"50%");
     T.setWidth(2,"20");
     T.setVerticalAlignment(1,1,"top");
     T.setVerticalAlignment(3,1,"top");

    TextReader texti = new TextReader(1);
      texti.setWidth("100%");
    TextReader texti2 = new TextReader(3);
      texti2.setWidth("100%");
    TextReader texti3 = new TextReader(4);
      texti3.setWidth("100%");
    TextReader texti4 = new TextReader(5);
      texti4.setWidth("100%");
    TextReader texti5 = new TextReader(6);
      texti5.setWidth("100%");
    TextReader texti6 = new TextReader(7);
      texti6.setWidth("100%");
    TextReader texti7 = new TextReader(8);
      texti7.setWidth("100%");
    TextReader texti8 = new TextReader(9);
      texti8.setWidth("100%");

    T.add(texti,1,1);
    T.addBreak(1,1);
    T.add(texti2,1,1);
    T.addBreak(1,1);
    T.add(texti3,1,1);

    T.add(texti4,3,1);
    T.addBreak(3,1);
    T.add(texti5,3,1);
    T.addBreak(3,1);
    T.add(texti6,3,1);
    T.addBreak(3,1);
    T.add(texti7,3,1);
    T.addBreak(3,1);
    T.add(texti8,3,1);

    add(T);

  }
}


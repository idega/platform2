/*
 * $Id: TextControl3.java,v 1.3 2001/07/12 21:55:21 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;

import java.sql.SQLException;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.block.text.presentation.TextReader;
import com.idega.block.text.data.TextModule;

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

    try {
      TextReader texti = new TextReader(5);
        if ( texti == null ) {
          TextModule text = new TextModule(5);
            text.setDefaultValues();
            text.insert();
            texti = new TextReader(5);
        }
        texti.setWidth("100%");

      TextReader texti2 = new TextReader(6);
        if ( texti == null ) {
          TextModule text = new TextModule(6);
            text.setDefaultValues();
            text.insert();
            texti = new TextReader(6);
        }
        texti2.setWidth("100%");

      TextReader texti3 = new TextReader(7);
        if ( texti == null ) {
          TextModule text = new TextModule(7);
            text.setDefaultValues();
            text.insert();
            texti = new TextReader(7);
        }
        texti3.setWidth("100%");

      TextReader texti4 = new TextReader(8);
        if ( texti == null ) {
          TextModule text = new TextModule(8);
            text.setDefaultValues();
            text.insert();
            texti = new TextReader(8);
        }
        texti4.setWidth("100%");

      TextReader texti5 = new TextReader(9);
        if ( texti == null ) {
          TextModule text = new TextModule(9);
            text.setDefaultValues();
            text.insert();
            texti = new TextReader(9);
        }
        texti5.setWidth("100%");

      TextReader texti6 = new TextReader(10);
        if ( texti == null ) {
          TextModule text = new TextModule(10);
            text.setDefaultValues();
            text.insert();
            texti = new TextReader(10);
        }
        texti6.setWidth("100%");

      TextReader texti7 = new TextReader(11);
        if ( texti == null ) {
          TextModule text = new TextModule(11);
            text.setDefaultValues();
            text.insert();
            texti = new TextReader(11);
        }
        texti7.setWidth("100%");

      TextReader texti8 = new TextReader(12);
        if ( texti == null ) {
          TextModule text = new TextModule(12);
            text.setDefaultValues();
            text.insert();
            texti = new TextReader(12);
        }
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
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }

    add(T);

  }
}


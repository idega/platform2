/*
 * $Id: TextControl3.java,v 1.5 2001/10/05 08:05:43 tryggvil Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;

import java.sql.SQLException;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
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
      for ( int a = 5; a < 13; a++ ) {
        TextModule text = new TextModule(a);
        if ( text.getTextHeadline() == null || text.getIncludeImage() == null ) {
            text.setDefaultValues();
            text.insert();
        }

        TextReader texti = new TextReader(a);
          texti.setWidth("100%");

        if ( a < 8 ) {
          T.add(texti,1,1);
          T.addBreak(1,1);
        }
        else {
          T.add(texti,3,1);
          T.addBreak(3,1);
        }
      }
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }

    add(T);

  }
}


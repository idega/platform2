/*

 * $Id: TextControl3.java,v 1.3 2004/05/24 14:21:40 palli Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.campus.presentation;







import java.sql.SQLException;

import com.idega.block.text.data.TxText;
import com.idega.block.text.presentation.TextReader;
import com.idega.presentation.Table;



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

        TxText text = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).findByPrimaryKeyLegacy(a);

        if ( text == null  ) {

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




/*
 * $Id: TextControl2.java,v 1.1 2001/06/06 11:29:36 palli Exp $
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
public class TextControl2 extends TextControl {

  public void doMenu(){

    Table T = new Table();
      T.setWidth(400);
      T.setBorder(0);
      //T.setAlignment("CENTER");

    Link L = new Link("Reglur um notkun Garðsbúðar","/regulations.jsp");
    L.addParameter(strAction,ACT1);
    Link L2 = new Link("Úthlutunarreglur","/regulations.jsp");
    L2.addParameter(strAction,ACT2);

    Text texti = new Text("Reglur fyrir Garðana");
      texti.setFontSize(1);


    T.add(L,1,3);
    T.add(L2,1,4);
    T.add(texti,1,1);

    add(T);
  }

  public void doText1(){
      add(new TextReader(26));
  }
  public void doText2(){
       add(new TextReader(27));
  }
}


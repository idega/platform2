package com.idega.projects.campus.service;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.text.presentation.TextReader;
import com.idega.projects.campus.service.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class TextControl2 extends TextControl {

  public void doMenu(){

    Table T = new Table();
      T.setWidth("400 ");
      T.setBorder(0);
          //T.setAlignment("CENTER");


    T.add(new TextReader(27),1,1);

    add(T);
  }



}


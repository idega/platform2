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

public class TextControl3 extends TextControl {

  public void doMenu(){

    Table T = new Table();
     T.setWidth("100%");
            T.setBorder(0);


TextReader texti = new TextReader(1);
TextReader texti2 = new TextReader(3);
TextReader texti3 = new TextReader(4);
TextReader texti4 = new TextReader(5);
TextReader texti5 = new TextReader(6);
TextReader texti6 = new TextReader(7);
TextReader texti7 = new TextReader(8);
TextReader texti8 = new TextReader(9);


  Link L = new AnchorLink(texti.getAnchorName(),texti.getAnchorName());
  Link L2 = new AnchorLink(texti2.getAnchorName(),texti2.getAnchorName());
  Link L3 = new AnchorLink(texti3.getAnchorName(),texti3.getAnchorName());
  Link L4 = new AnchorLink(texti4.getAnchorName(),texti4.getAnchorName());
  Link L5 = new AnchorLink(texti5.getAnchorName(),texti5.getAnchorName());
  Link L6 = new AnchorLink(texti6.getAnchorName(),texti6.getAnchorName());
  Link L7 = new AnchorLink(texti7.getAnchorName(),texti7.getAnchorName());
  Link L8 = new AnchorLink(texti8.getAnchorName(),texti8.getAnchorName());


// Adding Tabels with Ancors
T.add(L,1,1);
    T.add(L2,1,2);
    T.add(L3,1,3);
    T.add(L6,2,1);
    T.add(L5,2,2);
    T.add(L4,2,3);
    T.add(L7,1,4);
    T.add(L8,2,4);
    T.add(" ",1,5);
     add(T);

// Adding Text form TextReader
      add(new TextReader(1));
      add(Text.getBreak());
      add(Text.getBreak());
      add(new TextReader(3));
      add(Text.getBreak());
      add(Text.getBreak());
      add(new TextReader(4));
      add(Text.getBreak());
      add(Text.getBreak());
      add(new TextReader(5));
      add(Text.getBreak());
      add(Text.getBreak());
      add(new TextReader(6));
      add(Text.getBreak());
      add(Text.getBreak());
      add(new TextReader(7));
      add(Text.getBreak());
      add(Text.getBreak());
      add(new TextReader(8));
      add(Text.getBreak());
      add(Text.getBreak());
      add(new TextReader(9));
      add(Text.getBreak());
      add(Text.getBreak());

  }
}


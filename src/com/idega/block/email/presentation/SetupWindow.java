package com.idega.block.email.presentation;



/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega multimedia

 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>

 * @version 1.0

 */



import com.idega.idegaweb.presentation.IWAdminWindow;

import com.idega.presentation.IWContext;

import com.idega.presentation.Table;



public class SetupWindow extends IWAdminWindow{



  public SetupWindow() {

    super();

    setScrollbar(false);

    setWidth(800 );

    setHeight(500 );

    setLocation(true);

    setTitlebar(true);

    //keepFocus();

  }



  public void main(IWContext iwc) throws Exception{

    SetupEditor s = new SetupEditor();

    Table T = new Table(1,1);

    T.setAlignment(1,1,"center");

    T.add(s,1,1);

    add(T);

    setTitle("Setup");

    //addTitle("Login Editor");

  }

}

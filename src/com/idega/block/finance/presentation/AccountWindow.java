package com.idega.block.finance.presentation;



import com.idega.presentation.ui.Window;

import com.idega.presentation.IWContext;

/**

 * Title:   idegaclasses

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author  <a href="mailto:aron@idega.is">aron@idega.is

 * @version 1.0

 */



public class AccountWindow extends Window {



  public AccountWindow() {

    setResizable(true);

    setMenubar(true);

    setHeight(500);

    setWidth(500);

  }



  public void main(IWContext iwc){

    add(new AccountViewer());

  }

}

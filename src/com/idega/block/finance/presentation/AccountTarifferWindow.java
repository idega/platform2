package com.idega.block.finance.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class AccountTarifferWindow extends IWAdminWindow {

  public AccountTarifferWindow() {
    setWidth(500);
    setHeight(500);
    setResizable(true);
  }

  public void main(IWContext iwc) throws Exception{
    AccountTariffer AT = new AccountTariffer();
    add(AT);
    setTitle("Account tariffer");
    addTitle("Account tariffer");
  }
}
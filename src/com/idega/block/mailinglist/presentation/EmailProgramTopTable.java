package com.idega.block.mailinglist.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.Table;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.IWContext;
//import com.idega.jmodule.object.JModuleObject;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class EmailProgramTopTable extends Block {

  public static final String settingsButtonName = "settingsButtonName";
  public static final String newMailinglistButtonName = "newMailinglistButtonName";

  public EmailProgramTopTable() {

  }

  public void main(IWContext modinfo){
    Table topTable = new Table(2,1);
    SubmitButton settingsButton = new SubmitButton(settingsButtonName, "Users Settings");
    SubmitButton newMailinglistButton = new SubmitButton(newMailinglistButtonName, "New Mailinglist");
    topTable.add(settingsButton,1,1);
    topTable.add(newMailinglistButton,2,1);
    add(topTable);
  }
}

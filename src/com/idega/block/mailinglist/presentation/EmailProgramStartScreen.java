package com.idega.block.mailinglist.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class EmailProgramStartScreen extends Block {

  Table startTable = new Table(1,2);

  public EmailProgramStartScreen() {
  }

  public void main(IWContext modinfo){
    startTable.add("Start Program:)",1,1);
    startTable.add("Bite my shiny metal ass",1,2);
    add(startTable);
  }

}
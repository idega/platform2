package com.idega.block.mailinglist.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.block.mailinglist.data.Mailinglist;
import com.idega.block.mailinglist.data.Account;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class MailinglistSettingsTable extends AccountSettingsTable {

  public MailinglistSettingsTable() {
    super();
    this.setHeaderString("Edit Current Mailinglist");
  }

  public void setMailingListSettings(Mailinglist mailinglist){
    super.setAccount((Account) mailinglist);
    nameInput.setValue(mailinglist.getMailinglistName());
  }

  public void main(IWContext iwc){
    add(this.settingsTable);
  }
}

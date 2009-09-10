package com.idega.block.mailinglist.presentation;

import com.idega.block.mailinglist.data.Mailinglist;
import com.idega.presentation.IWContext;


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
    super.setAccount(mailinglist);
    this.nameInput.setValue(mailinglist.getMailinglistName());
  }

  public void main(IWContext iwc){
    add(this.settingsTable);
  }
}

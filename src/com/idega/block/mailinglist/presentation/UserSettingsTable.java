package com.idega.block.mailinglist.presentation;


import com.idega.block.mailinglist.data.MailAccount;
import com.idega.presentation.IWContext;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class UserSettingsTable extends AccountSettingsTable {

  public UserSettingsTable(){
    super();
    this.setHeaderString("Edit Current User Settings");
  }

  public void setUser(IWContext iwc){
    /** @todo IMPLIMENT THIS GET USERS CURRENT SETTINGS THROUG FOR EXAMPLE iwc.getUser
     * MailAccount userAccount
     * setInputs(userAccount);
     */
  }

  public void setInputs(MailAccount mailAccount){
    super.setAccount(mailAccount);
    this.nameInput.setValue(mailAccount.getUserName());
  }

  public void main(IWContext iwc){
    add(this.settingsTable);
  }
}

package com.idega.block.mailinglist.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;

import com.idega.block.mailinglist.data.Account;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public abstract class AccountSettingsTable extends Block {

  public static final String emailInputName = "emailInputName";
  public static final String replyEmailInputName = "replyEmailInputName";
  public static final String nameInputName = "nameInputName";
  public static final String smtpHostInputName = "smtpHostInputName";
  public static final String smtpPortInputName = "smtpPortInputName";
  public static final String smtpLoginInputName = "smtpLoginInputName";
  public static final String smtpPasswordInputName = "smtpPasswordInputName";
  public static final String smtpConfirmPasswordInputName = "smtpConfirmPasswordInputName";
  public static final String pop3HostInputName = "pop3HostInputName";
  public static final String pop3PortInputName = "pop3PortInputName";
  public static final String pop3LoginInputName = "pop3LoginInputName";
  public static final String pop3PasswordInputName = "pop3PasswordInputName";
  public static final String pop3ConfirmPasswordInputName = "pop3ConfirmPasswordInputName";
  public static final String OKButtonName = "OKButtonName";

  private String headerString = "";

  protected TextInput nameInput;
  protected TextInput emailInput;
  protected TextInput replyEmailInput;
  protected TextInput smtpHostInput;
  protected TextInput smtpPortInput;
  protected TextInput smtpLoginInput;
  protected PasswordInput smtpPasswordInput;
  protected PasswordInput smtpConfirmPasswordInput;
  protected TextInput pop3HostInput;
  protected TextInput pop3PortInput;
  protected TextInput pop3LoginInput;
  protected PasswordInput pop3PasswordInput;
  protected PasswordInput pop3ConfirmPasswordInput;

  protected Table settingsTable = new Table(4,10);

  public AccountSettingsTable() {

    settingsTable.mergeCells(1,2,4,2);
    settingsTable.setAlignment(1,1,"center");

    nameInput = new TextInput(nameInputName);
    emailInput = new TextInput(emailInputName);
    replyEmailInput = new TextInput(replyEmailInputName);
    smtpHostInput = new TextInput(smtpHostInputName);
    smtpPortInput = new TextInput(smtpPortInputName);
    smtpLoginInput = new TextInput(smtpLoginInputName);
    smtpPasswordInput = new PasswordInput(smtpPasswordInputName);
    smtpConfirmPasswordInput = new PasswordInput(smtpConfirmPasswordInputName);
    pop3HostInput = new TextInput(pop3HostInputName);
    pop3PortInput = new TextInput(pop3PortInputName);
    pop3LoginInput = new TextInput(pop3LoginInputName);
    pop3PasswordInput = new PasswordInput(pop3PasswordInputName);
    pop3ConfirmPasswordInput = new PasswordInput(pop3ConfirmPasswordInputName);

    settingsTable.mergeCells(2,2,4,2);
    settingsTable.mergeCells(2,3,4,3);
    settingsTable.mergeCells(2,4,4,4);
    settingsTable.mergeCells(2,10,4,10);

    settingsTable.add(nameInput,2,2);
    settingsTable.add(emailInput,2,3);
    settingsTable.add(replyEmailInput,2,4);

    SubmitButton OKButton = new SubmitButton(OKButtonName, "OK");

    settingsTable.add(smtpHostInput,2,5);
    settingsTable.add(smtpPortInput,2,6);
    settingsTable.add(smtpLoginInput,2,7);
    settingsTable.add(smtpPasswordInput,2,8);
    settingsTable.add(smtpConfirmPasswordInput,2,9);
    settingsTable.add(pop3HostInput,4,5);
    settingsTable.add(pop3PortInput,4,6);
    settingsTable.add(pop3LoginInput,4,7);
    settingsTable.add(pop3PasswordInput,4,8);
    settingsTable.add(pop3ConfirmPasswordInput,4,9);
    settingsTable.add(OKButton,2,10);
  }

  protected void setAccount(Account account){
    emailInput.setValue(account.getEmail());
    replyEmailInput.setValue(account.getReplyEmail());
    smtpHostInput.setValue(account.getSMTPHost());
    smtpPortInput.setValue(account.getSMTPPort());
    smtpLoginInput.setValue(account.getSMTPLoginName());
    smtpPasswordInput.setValue(account.getSMTPPassword());
    smtpConfirmPasswordInput.setValue(account.getSMTPPassword());
    pop3HostInput.setValue(account.getPOP3Host());
    pop3PortInput.setValue(account.getPOP3Port());
    pop3LoginInput.setValue(account.getPOP3LoginName());
    pop3PasswordInput.setValue(account.getPOP3Password());
    pop3ConfirmPasswordInput.setValue(account.getPOP3Password());
  }

  public void setHeaderString(String headerString){
    this.headerString = headerString;
    settingsTable.add(headerString,1,1);
  }

  public abstract void main(IWContext iwc);
}
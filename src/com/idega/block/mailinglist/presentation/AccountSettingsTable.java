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

    this.settingsTable.mergeCells(1,2,4,2);
    this.settingsTable.setAlignment(1,1,"center");

    this.nameInput = new TextInput(nameInputName);
    this.emailInput = new TextInput(emailInputName);
    this.replyEmailInput = new TextInput(replyEmailInputName);
    this.smtpHostInput = new TextInput(smtpHostInputName);
    this.smtpPortInput = new TextInput(smtpPortInputName);
    this.smtpLoginInput = new TextInput(smtpLoginInputName);
    this.smtpPasswordInput = new PasswordInput(smtpPasswordInputName);
    this.smtpConfirmPasswordInput = new PasswordInput(smtpConfirmPasswordInputName);
    this.pop3HostInput = new TextInput(pop3HostInputName);
    this.pop3PortInput = new TextInput(pop3PortInputName);
    this.pop3LoginInput = new TextInput(pop3LoginInputName);
    this.pop3PasswordInput = new PasswordInput(pop3PasswordInputName);
    this.pop3ConfirmPasswordInput = new PasswordInput(pop3ConfirmPasswordInputName);

    this.settingsTable.mergeCells(2,2,4,2);
    this.settingsTable.mergeCells(2,3,4,3);
    this.settingsTable.mergeCells(2,4,4,4);
    this.settingsTable.mergeCells(2,10,4,10);

    this.settingsTable.add(this.nameInput,2,2);
    this.settingsTable.add(this.emailInput,2,3);
    this.settingsTable.add(this.replyEmailInput,2,4);

    SubmitButton OKButton = new SubmitButton(OKButtonName, "OK");

    this.settingsTable.add(this.smtpHostInput,2,5);
    this.settingsTable.add(this.smtpPortInput,2,6);
    this.settingsTable.add(this.smtpLoginInput,2,7);
    this.settingsTable.add(this.smtpPasswordInput,2,8);
    this.settingsTable.add(this.smtpConfirmPasswordInput,2,9);
    this.settingsTable.add(this.pop3HostInput,4,5);
    this.settingsTable.add(this.pop3PortInput,4,6);
    this.settingsTable.add(this.pop3LoginInput,4,7);
    this.settingsTable.add(this.pop3PasswordInput,4,8);
    this.settingsTable.add(this.pop3ConfirmPasswordInput,4,9);
    this.settingsTable.add(OKButton,2,10);
  }

  protected void setAccount(Account account){
    this.emailInput.setValue(account.getEmail());
    this.replyEmailInput.setValue(account.getReplyEmail());
    this.smtpHostInput.setValue(account.getSMTPHost());
    this.smtpPortInput.setValue(account.getSMTPPort());
    this.smtpLoginInput.setValue(account.getSMTPLoginName());
    this.smtpPasswordInput.setValue(account.getSMTPPassword());
    this.smtpConfirmPasswordInput.setValue(account.getSMTPPassword());
    this.pop3HostInput.setValue(account.getPOP3Host());
    this.pop3PortInput.setValue(account.getPOP3Port());
    this.pop3LoginInput.setValue(account.getPOP3LoginName());
    this.pop3PasswordInput.setValue(account.getPOP3Password());
    this.pop3ConfirmPasswordInput.setValue(account.getPOP3Password());
  }

  public void setHeaderString(String headerString){
    this.headerString = headerString;
    this.settingsTable.add(headerString,1,1);
  }

  public abstract void main(IWContext iwc);
}

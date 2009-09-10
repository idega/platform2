package com.idega.block.mailinglist.presentation;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

import java.sql.SQLException;

import com.idega.block.mailinglist.business.MailingListBusiness;
import com.idega.block.mailinglist.data.Mailinglist;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

public class MailinglistPresentation extends Block {

    String textInputName = "textInput";
    // String selectionBoxName = "selectionBoxName";
    String submitAndRemoveParameter =  "submitAndRemoveParameter";
    String addParameterValue =  "addParameterValue";
    String removeParameterValue =  "removeParameterValue";
    String insertemail;

  public MailinglistPresentation() {

  }

  public String whatMailinglistsAreChosen(String[] selectionBoxChoices)throws SQLException{
    String stringOfChoices = new String();
    if (selectionBoxChoices != null){
      for (int i = 0; i < selectionBoxChoices.length ; i++) {
        Mailinglist postList = ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).findByPrimaryKeyLegacy(Integer.parseInt(selectionBoxChoices[i]));
        stringOfChoices = stringOfChoices+", "+postList.getStringColumnValue(com.idega.block.mailinglist.data.MailinglistBMPBean.MAILINGLIST_NAME);
      }
    }
    return (stringOfChoices);
  }

  public String chooseReply(int choice){
    String stringReply = new String();

    if ((choice % 2) == 0){
      if ((choice % 5) == 0 ){
        stringReply = "Netfanginu ��nu hefur veri� b�tt vi� p�stlistan(a)";
      }
      else{
        stringReply = "N�tt netfang hefur veri� skr�� og b�tt vi� p�stlistan(a)";
      }
    }
    else if ((choice % 3) == 0){
      if ((choice % 5) == 0){
        stringReply = "Ekki t�kst a� skr� netfangi� � p�stlistann";
      }
      else{
        stringReply = "Netfang skr��, en �a� mist�kst a� skr� �a� � p�stlistan(a)";
      }
    }
    else if ((choice % 11) == 0){
      if ((choice % 17) == 0){
        stringReply = "�� hefur veri� skr��ur �r �llum p�stlistum";
      }
      else{
        stringReply = "�� varst srk��ur �r p�stlistunum";
      }
    }
    else if ((choice % 13) == 0) {
      stringReply = "�� ert ekki skr��ur � neinn af p�stlistunum";
    }
    else{
      stringReply = "Eitthva� er mis h�r";
    }

    return stringReply;
  }


  public void main(IWContext modinfo) throws SQLException{

    String inputEmail;
    String[] selectionBoxChoices;
    int reply=0;
    String stringReply = "Tj�n dau�ans";
    Table table = new Table(2,4);
    Mailinglist mailinglist = ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).createLegacy();

    SubmitButton submitButton = new SubmitButton("Skr� Mig",this.submitAndRemoveParameter,this.addParameterValue);
    SubmitButton removeButton = new SubmitButton("Afskr� Mig",this.submitAndRemoveParameter,this.removeParameterValue);

    Form form = new Form();
    form.maintainAllParameters();
    SelectionBox selctionBox = new SelectionBox(mailinglist.findAllOrdered(com.idega.block.mailinglist.data.MailinglistBMPBean.MAILINGLIST_NAME));
    TextInput textInput = new TextInput(this.textInputName);

    textInput.setSize(10);
    textInput.keepStatusOnAction();

    table.setBorder(1);
    table.setHeight("20%");
    table.setCellpadding(0);
    table.setCellspacing(0);
    table.setColor("#CEDFD0");
    table.setWidth("20%");
    table.mergeCells(1,4,2,4);
    
    Table layoutTable = new Table(1, 1);
    layoutTable.setCellpaddingAndCellspacing(0);
    layoutTable.setWidth(Table.HUNDRED_PERCENT);
    layoutTable.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_CENTER);
    add(form);
    form.add(layoutTable);
    layoutTable.add(table, 1, 1);
    table.add("P�stlistar", 1, 1);
    table.add("Email", 1, 2);
    table.add(selctionBox, 2, 1);
    table.add(textInput, 2, 2);
    table.add(submitButton, 2, 3);
    table.add(removeButton, 1, 3);

    selectionBoxChoices = modinfo.getParameterValues(com.idega.block.mailinglist.data.MailinglistBMPBean.MAILINGLIST_NAME);
    if (selectionBoxChoices != null ) {
      System.out.println("ARRRGG     selectionBoxChoices.length = "+selectionBoxChoices.length);
      boolean hasSubmitted = modinfo.isParameterSet(this.textInputName);
      if(hasSubmitted){
        if (modinfo.isParameterSet(this.submitAndRemoveParameter)){
          String action = modinfo.getParameter(this.submitAndRemoveParameter);
          if(action!=null){
            inputEmail = modinfo.getParameter(this.textInputName);
            if(action.equals(this.addParameterValue)){
            reply = MailingListBusiness.addEmailBusiness(modinfo, selectionBoxChoices, inputEmail);
            }
            if(action.equals(this.removeParameterValue)){

              reply = MailingListBusiness.removeEmailBusiness(modinfo, selectionBoxChoices, inputEmail);
            }
          }
          stringReply = chooseReply(reply)+", "+whatMailinglistsAreChosen(selectionBoxChoices);
        }
      }
    }
    else{
        System.out.println("Ekkert vali� !!!???");
    }
    table.add(stringReply, 1, 4);
   // add(Text.getBreak());
  }
}

package com.idega.block.mailinglist.presentation;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.block.mailinglist.data.*;
import com.idega.block.mailinglist.business.MailingListBusiness;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.Table;
import java.sql.SQLException;
import java.util.*;

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
        Mailinglist postList = new Mailinglist(Integer.parseInt(selectionBoxChoices[i]));
        stringOfChoices = stringOfChoices+", "+postList.getStringColumnValue(Mailinglist.MAILINGLIST_NAME);
      }
    }
    return (stringOfChoices);
  }

  public String chooseReply(int choice){
    String stringReply = new String();

    if ((choice % 2) == 0){
      if ((choice % 5) == 0 ){
        stringReply = "Netfanginu þínu hefur verið bætt við póstlistan(a)";
      }
      else{
        stringReply = "Nýtt netfang hefur verið skráð og bætt við póstlistan(a)";
      }
    }
    else if ((choice % 3) == 0){
      if ((choice % 5) == 0){
        stringReply = "Ekki tókst að skrá netfangið á póstlistann";
      }
      else{
        stringReply = "Netfang skráð, en það mistókst að skrá það á póstlistan(a)";
      }
    }
    else if ((choice % 11) == 0){
      if ((choice % 17) == 0){
        stringReply = "Þú hefur verið skráður úr öllum póstlistum";
      }
      else{
        stringReply = "Þú varst srkáður úr póstlistunum";
      }
    }
    else if ((choice % 13) == 0) {
      stringReply = "Þú ert ekki skráður á neinn af póstlistunum";
    }
    else{
      stringReply = "Eitthvað er mis hér";
    }

    return stringReply;
  }


  public void main(IWContext modinfo) throws SQLException{

    String inputEmail;
    String[] selectionBoxChoices;
    int reply=0;
    String stringReply = "Tjón dauðans";
    Table table = new Table(2,4);
    Mailinglist mailinglist = new Mailinglist();
    MailAccount emaillist = new MailAccount();

    SubmitButton submitButton = new SubmitButton("Skrá Mig",submitAndRemoveParameter,addParameterValue);
    SubmitButton removeButton = new SubmitButton("Afskrá Mig",submitAndRemoveParameter,removeParameterValue);

    Form form = new Form();
    form.maintainAllParameters();
    SelectionBox selctionBox = new SelectionBox(mailinglist.findAllOrdered(Mailinglist.MAILINGLIST_NAME));
    TextInput textInput = new TextInput(textInputName);

    textInput.setSize(10);
    textInput.keepStatusOnAction();

    table.setBorder(1);
    table.setHeight("20%");
    table.setCellpadding(0);
    table.setCellspacing(0);
    table.setColor("#CEDFD0");
    table.setAlignment("center");
    table.setWidth("20%");
    table.mergeCells(1,4,2,4);

    add(form);
    form.add(table);
    table.add("Póstlistar", 1, 1);
    table.add("Email", 1, 2);
    table.add(selctionBox, 2, 1);
    table.add(textInput, 2, 2);
    table.add(submitButton, 2, 3);
    table.add(removeButton, 1, 3);

    selectionBoxChoices = (String[]) modinfo.getParameterValues(Mailinglist.MAILINGLIST_NAME);
    if (selectionBoxChoices != null ) {
      System.out.println("ARRRGG     selectionBoxChoices.length = "+selectionBoxChoices.length);
      boolean hasSubmitted = modinfo.isParameterSet(textInputName);
      if(hasSubmitted){
        if (modinfo.isParameterSet(submitAndRemoveParameter)){
          String action = modinfo.getParameter(submitAndRemoveParameter);
          if(action!=null){
            inputEmail = modinfo.getParameter(textInputName);
            if(action.equals(addParameterValue)){
            reply = MailingListBusiness.addEmailBusiness(modinfo, selectionBoxChoices, inputEmail);
            }
            if(action.equals(removeParameterValue)){

              reply = MailingListBusiness.removeEmailBusiness(modinfo, selectionBoxChoices, inputEmail);
            }
          }
          stringReply = chooseReply(reply)+", "+whatMailinglistsAreChosen(selectionBoxChoices);
        }
      }
    }
    else{
        System.out.println("Ekkert valið !!!???");
    }
    table.add(stringReply, 1, 4);
   // add(Text.getBreak());
  }
}
package com.idega.block.calendar.presentation;

import com.idega.block.calendar.business.CalendarBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ConfirmDeleteWindow extends IWAdminWindow{

  protected final static String PRM_CONFIRM = "iw_confirm";
  public final static String PRM_DELETE = "iw_delete";
  public final static String PRM_DELETE_ID = "iw_del_id";
  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.calendar";


  public ConfirmDeleteWindow() {
    setWidth(250);
    setHeight(135);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
      setTitle("Confirm delete");


      //this.setParentToReload();

      //this.debugParameters(iwc);


      boolean doConfirm = !(iwc.getParameter(PRM_CONFIRM)!=null || iwc.getParameter(PRM_CONFIRM+".x")!=null);
      if(doConfirm){
        add(getConfirmBox(iwc));
      }
      else{
        String _entryID = iwc.getParameter(PRM_DELETE_ID);
        try {
          CalendarBusiness.deleteEntry(Integer.parseInt(_entryID));
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        this.close();
      }
  }

  public Form getConfirmBox(IWContext iwc){
    Table t = new Table(1,2);
    Form f = new Form();

    IWBundle iwb = this.getBundle(iwc);
    IWResourceBundle iwrb = iwb.getResourceBundle(iwc);


    f.maintainParameter(PRM_DELETE);
    f.maintainParameter(PRM_DELETE_ID);

    f.add(t);
    t.setWidth("100%");
    t.setAlignment(1,1,IWConstants.CENTER_ALIGNMENT);
    t.setVerticalAlignment(1,1,IWConstants.MIDDLE_ALIGNMENT);
    //t.setHeight("100");
    t.setHeight(1,"70");
    t.setHeight(2,"20");

    Text confirmText = new Text(iwrb.getLocalizedString("confirm_delete","Are you sure you want to delete this calendar entry?"));
    this.formatText(confirmText,true);
    t.add(confirmText,1,1);

    SubmitButton button = new SubmitButton(iwrb.getLocalizedImageButton("yes", "YES"), PRM_CONFIRM);
    CloseButton closebutton = new CloseButton(iwrb.getLocalizedImageButton("cancel", "CANCEL"));

    Table innerTable = new Table(3,1);
    innerTable.add(button,1,1);
    innerTable.add(closebutton,3,1);
    innerTable.setHeight(20);
    innerTable.setWidth(2,"5");
    innerTable.setCellpadding(0);
    innerTable.setCellspacing(0);
    t.setAlignment(1, 2, IWConstants.CENTER_ALIGNMENT);
    t.add(innerTable,1,2);

//    t.setBorder(1);
//    innerTable.setBorder(1);

    return f;
  }

}

package com.idega.block.cal.presentation;

import com.idega.block.cal.business.CalBusiness;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class ConfirmDeleteWindow extends StyledIWAdminWindow{

  protected final static String PRM_CONFIRM = "iw_confirm";
  public final static String PRM_DELETE = "iw_delete";
  public final static String PRM_DELETE_ID = "iw_del_id";
  private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
  public final static String PRM_ENTRY_OR_LEDGER = "ent_led";
  public final static String PRM_DELETED = "deleted";
  
  private String borderAllWhite = "borderAllWhite";
  
  
  private Form f;
  
  private CalBusiness calBiz;


  public ConfirmDeleteWindow() {
    setWidth(250);
    setHeight(250);
    setScrollbar(false);
  }

  public void main(IWContext iwc){
  	
  	IWResourceBundle iwrb = getResourceBundle(iwc);
    setTitle(iwrb.getLocalizedString("confirmDelete.confirm_delete","Confirm delete"));
    
    String typeOfObject = iwc.getParameter(PRM_ENTRY_OR_LEDGER);
    
    boolean doConfirm = !(iwc.getParameter(PRM_CONFIRM)!=null || iwc.getParameter(PRM_CONFIRM+".x")!=null);
    if(doConfirm){
      add(getConfirmBox(iwc,typeOfObject),iwc);
    }
    else{
    	Link l = new Link();
    	l.addParameter(PRM_DELETE_ID,"");
    	l.addParameter(PRM_DELETED,"yes");
    	
      String id = iwc.getParameter(PRM_DELETE_ID);
      String modifyOneOrMany = iwc.getParameter(CalendarEntryCreator.modifyOneOrManyRadioButtonParameterName);
      try {
      	if(typeOfObject.equals(CalendarEntryCreator.ENTRY)) {
      		if(modifyOneOrMany.equals(CalendarEntryCreator.oneValue)) {
      			getCalendarBusiness(iwc).deleteEntry(Integer.parseInt(id));
      		}
      		else if(modifyOneOrMany.equals(CalendarEntryCreator.manyValue)) {
      			getCalendarBusiness(iwc).deleteEntryGroupByEntryID(Integer.parseInt(id));
      		}
      		setOnUnLoad("window.opener.location.reload()"); 
      		
      	}
      	else if(typeOfObject.equals(LedgerWindow.LEDGER)) {
      		getCalendarBusiness(iwc).deleteLedger(Integer.parseInt(id));
        		l.setWindowToOpen(CalendarWindow.class);
      		setOnLoad("window.opener.close()"); 
        		String script = "window.opener." + l.getWindowToOpenCallingScript(iwc);
        		setOnLoad(script);
      		
      	}
      	else if(typeOfObject.equals(NewMarkWindow.MARK)) {
      		getCalendarBusiness(iwc).deleteMark(Integer.parseInt(id));
      		setOnLoad("window.opener.close()");       		
      	}
                  
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      close();        
    }
  }

  public PresentationObject getConfirmBox(IWContext iwc,String typeOfObject){
    Table t = new Table();
    t.setStyleClass(borderAllWhite);
    f = new Form();

    IWBundle iwb = this.getBundle(iwc);
    IWResourceBundle iwrb = iwb.getResourceBundle(iwc);
    
    f.maintainParameter(PRM_DELETE);
    f.maintainParameter(PRM_DELETE_ID);
    f.maintainParameter(PRM_ENTRY_OR_LEDGER);
    f.maintainParameter(CalendarEntryCreator.modifyOneOrManyRadioButtonParameterName);
    f.maintainParameter(LedgerWindow.LEDGER);
    
    
    f.add(t);
    t.setWidth("100%");
    t.setAlignment(1,1,IWConstants.CENTER_ALIGNMENT);
    t.setVerticalAlignment(1,1,IWConstants.MIDDLE_ALIGNMENT);
    t.setHeight(1,"70");
    t.setHeight(2,"20");
    
    RadioButton modifyOneRadioButton = new RadioButton(CalendarEntryCreator.modifyOneOrManyRadioButtonParameterName,"one");
    modifyOneRadioButton.setSelected();
    RadioButton modifyManyRadioButton = new RadioButton(CalendarEntryCreator.modifyOneOrManyRadioButtonParameterName,"many");
    
    
    Table radioTable = new Table();
    radioTable.add(modifyOneRadioButton,1,1);
    radioTable.add(Text.NON_BREAKING_SPACE,1,1);
    radioTable.add(iwrb.getLocalizedString("confirmDelete.delete_one_entry","Delete one entry"),1,1);
    radioTable.add(Text.BREAK,1,1);
    radioTable.add(modifyManyRadioButton,1,1);
    radioTable.add(Text.NON_BREAKING_SPACE,1,1);
    radioTable.add(iwrb.getLocalizedString("confirmDelete.delete_many_entries","Delete row of entries"),1,1);
    

    Text confirmText = new Text(iwrb.getLocalizedString("confirm_delete","Are you sure you want to delete this calendar entry?"));
    this.formatText(confirmText,true);
    if(typeOfObject.equals(CalendarEntryCreator.ENTRY)) {
    	t.add(radioTable,1,1);
    }
    else {
    	t.add(confirmText,1,1);
    }
    

    SubmitButton button = new SubmitButton(iwrb.getLocalizedImageButton("yes", "YES"), PRM_CONFIRM);
    CloseButton closebutton = new CloseButton(iwrb.getLocalizedImageButton("cancel", "CANCEL"));

    Table innerTable = new Table();
    innerTable.add(button,1,1);
    innerTable.add(closebutton,3,1);
    innerTable.setHeight(20);
    innerTable.setWidth(2,"5");
    innerTable.setCellpadding(0);
    innerTable.setCellspacing(0);
    t.setAlignment(1, 2, IWConstants.CENTER_ALIGNMENT);
    t.add(innerTable,1,2);
    
    

    return f;
  }
  public String getBundleIdentifier() {
  	return IW_BUNDLE_IDENTIFIER;
  }
  
  public CalBusiness getCalendarBusiness(IWApplicationContext iwc) {
  	if (calBiz == null) {
  		try {
  			calBiz = (CalBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CalBusiness.class);
  		}
  		catch (java.rmi.RemoteException rme) {
  			throw new RuntimeException(rme.getMessage());
  		}
  	}
  	return calBiz;
  }

}

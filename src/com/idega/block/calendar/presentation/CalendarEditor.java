package com.idega.block.calendar.presentation;


import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.core.data.ICFile;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.calendar.data.*;
import com.idega.block.calendar.business.*;
import com.idega.block.text.business.TextFinder;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.block.login.business.LoginBusiness;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

public class CalendarEditor extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.calendar";
private boolean _isAdmin = false;
private boolean _superAdmin = false;
private boolean _update = false;
private boolean _save = false;

private int _entryID = -1;
private int _typeID = -1;
private int _userID = -1;
private int _groupID = -1;
private idegaTimestamp _stamp;

private IWBundle _iwb;
private IWResourceBundle _iwrb;

public CalendarEditor(){
  setWidth(440);
  setHeight(420);
  setUnMerged();
  setMethod("get");
}

  public void main(IWContext iwc) throws Exception {
    /**
     * @todo permission
     */
    _isAdmin = true;
    _superAdmin = iwc.hasEditPermission(this);
    _iwb = getBundle(iwc);
    _iwrb = getResourceBundle(iwc);
    addTitle(_iwrb.getLocalizedString("calendar_admin","Calendar Admin"));
    Locale currentLocale = iwc.getCurrentLocale(),chosenLocale;

    try {
      _userID = LoginBusiness.getUser(iwc).getID();
    }
    catch (Exception e) {
      _userID = -1;
    }
    try {
      _groupID = LoginBusiness.getUser(iwc).getPrimaryGroupID();
    }
    catch (Exception e) {
      _groupID = -1;
    }

    String sLocaleId = iwc.getParameter(CalendarBusiness.PARAMETER_LOCALE_DROP);
    int iCategoryId = iwc.isParameterSet(CalendarBusiness.PARAMETER_IC_CAT)?Integer.parseInt(iwc.getParameter(CalendarBusiness.PARAMETER_IC_CAT)):-1;

    int iLocaleId = -1;
    if(sLocaleId!= null){
      iLocaleId = Integer.parseInt(sLocaleId);
      chosenLocale = TextFinder.getLocale(iLocaleId);
    }
    else{
      chosenLocale = currentLocale;
      iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);
    }

    if ( _isAdmin ) {
      processForm(iwc,iLocaleId,iCategoryId);
    }
    else {
      noAccess();
    }
  }

  private void processForm(IWContext iwc, int iLocaleId,int iCategoryId) {
    if ( iwc.getParameter(CalendarBusiness.PARAMETER_ENTRY_ID) != null ) {
      try {
        _entryID = Integer.parseInt(iwc.getParameter(CalendarBusiness.PARAMETER_ENTRY_ID));
      }
      catch (NumberFormatException e) {
        _entryID = -1;
      }
    }

    if ( iwc.getParameter(CalendarBusiness.PARAMETER_TYPE_ID) != null ) {
      try {
        _typeID = Integer.parseInt(iwc.getParameter(CalendarBusiness.PARAMETER_TYPE_ID));
      }
      catch (NumberFormatException e) {
        _typeID = -1;
      }
    }

    if ( iwc.getParameter(CalendarBusiness.PARAMETER_ENTRY_DATE) != null ) {
      try {
        _stamp = new idegaTimestamp(iwc.getParameter(CalendarBusiness.PARAMETER_ENTRY_DATE));
      }
      catch (Exception e) {
        _stamp = new idegaTimestamp();
      }
    }
    else {
      _stamp = new idegaTimestamp();
    }

    if ( iwc.getParameter(CalendarBusiness.PARAMETER_MODE) != null ) {
      if ( iwc.getParameter(CalendarBusiness.PARAMETER_MODE).equalsIgnoreCase(CalendarBusiness.PARAMETER_MODE_CLOSE) ) {
        closeEditor(iwc);
      }
      else if ( iwc.getParameter(CalendarBusiness.PARAMETER_MODE).equalsIgnoreCase(CalendarBusiness.PARAMETER_MODE_SAVE) ) {
        saveEntry(iwc,iLocaleId,iCategoryId);
      }
    }

    if ( _entryID == -1 && iwc.getSessionAttribute(CalendarBusiness.PARAMETER_ENTRY_ID) != null ) {
      try {
        _entryID = Integer.parseInt((String)iwc.getSessionAttribute(CalendarBusiness.PARAMETER_ENTRY_ID));
      }
      catch (NumberFormatException e) {
        _entryID = -1;
      }
      iwc.removeSessionAttribute(CalendarBusiness.PARAMETER_ENTRY_ID);
    }

    if ( _entryID != -1 ) {
      if ( iwc.getParameter(CalendarBusiness.PARAMETER_MODE_DELETE) != null ) {
        deleteEntry(iwc);
      }
      else {
        _update = true;
      }
    }

    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(CalendarBusiness.PARAMETER_LOCALE_DROP);
      localeDrop.setToSubmit();
      localeDrop.setSelectedElement(Integer.toString(iLocaleId));
    addLeft(_iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);
    addHiddenInput(new HiddenInput(CalendarBusiness.PARAMETER_ENTRY_ID,Integer.toString(_entryID)));

    initializeFields(iLocaleId,iCategoryId);
  }

  private void initializeFields(int iLocaleId,int iCategoryId) {
    CalendarEntry entry = null;
    if ( _update )
      entry = CalendarFinder.getEntry(_entryID);

    String[] locTexts = null;
    if ( entry != null )
      locTexts = TextFinder.getLocalizedString(entry,iLocaleId);
    if ( entry != null )
      _stamp = new idegaTimestamp(entry.getDate());

    DropdownMenu entryTypes = CalendarBusiness.getEntryTypes(CalendarBusiness.PARAMETER_TYPE_ID,iLocaleId);
    if ( entry != null )
      entryTypes.setSelectedElement(Integer.toString(entry.getEntryTypeID()));
    addLeft(_iwrb.getLocalizedString("type","Type")+":",entryTypes,true);

    TextInput entryHeadline = new TextInput(CalendarBusiness.PARAMETER_ENTRY_HEADLINE);
      entryHeadline.setLength(24);
      if ( locTexts != null && locTexts[0] != null ) {
        entryHeadline.setContent(locTexts[0]);
      }
    addLeft(_iwrb.getLocalizedString("headline","Headline")+":",entryHeadline,true);

    TextArea entryBody = new TextArea(CalendarBusiness.PARAMETER_ENTRY_BODY,40,6);
      if ( locTexts != null && locTexts[1] != null ) {
        entryBody.setContent(locTexts[1]);
      }
    addLeft(_iwrb.getLocalizedString("body","Body")+":",entryBody,true);

    DateInput entryDate = new DateInput(CalendarBusiness.PARAMETER_ENTRY_DATE);
      entryDate.setYearRange(new idegaTimestamp().getYear()-5,new idegaTimestamp().getYear()+10);
      entryDate.setDay(_stamp.getDay());
      entryDate.setMonth(_stamp.getMonth());
      entryDate.setYear(_stamp.getYear());
      entryDate.setStyleAttribute("style",STYLE);
    addLeft(_iwrb.getLocalizedString("date","Date")+":",entryDate,true);
    addHiddenInput(new HiddenInput(CalendarBusiness.PARAMETER_IC_CAT,String.valueOf(iCategoryId)));
    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("close","CLOSE"),CalendarBusiness.PARAMETER_MODE,CalendarBusiness.PARAMETER_MODE_CLOSE));
    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save","SAVE"),CalendarBusiness.PARAMETER_MODE,CalendarBusiness.PARAMETER_MODE_SAVE));
  }

  private void saveEntry(IWContext iwc, int localeID,int categoryId) {
    String entryHeadline = iwc.getParameter(CalendarBusiness.PARAMETER_ENTRY_HEADLINE);
    String entryBody = iwc.getParameter(CalendarBusiness.PARAMETER_ENTRY_BODY);
    String entryDate = iwc.getParameter(CalendarBusiness.PARAMETER_ENTRY_DATE);
    String entryType = iwc.getParameter(CalendarBusiness.PARAMETER_TYPE_ID);

    int entryID = CalendarBusiness.saveEntry(_entryID,_userID,_groupID,localeID,categoryId,entryHeadline,entryBody,entryDate,entryType);
    iwc.setSessionAttribute(CalendarBusiness.PARAMETER_ENTRY_ID,Integer.toString(entryID));
  }

  private void deleteEntry(IWContext iwc) {
    CalendarBusiness.deleteEntry(_entryID);
    closeEditor(iwc);
  }

  private void closeEditor(IWContext iwc) {
    setParentToReload();
    close();
  }

  private void noAccess() throws IOException,SQLException {
    addLeft(_iwrb.getLocalizedString("no_access","Login first!"));
    addSubmitButton(new CloseButton(_iwrb.getImage("close.gif")));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}

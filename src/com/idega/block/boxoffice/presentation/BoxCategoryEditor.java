package com.idega.block.boxoffice.presentation;


import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICLocale;
import com.idega.block.boxoffice.data.*;
import com.idega.block.boxoffice.business.*;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.block.login.business.LoginBusiness;
import com.idega.block.text.business.TextFinder;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

public class BoxCategoryEditor extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.boxoffice";
private boolean _isAdmin = false;
private boolean _save = false;
private boolean _update = false;
private int _userID = -1;
private int _boxID = -1;
private int _boxCategoryID = -1;

private IWBundle _iwb;
private IWResourceBundle _iwrb;

public BoxCategoryEditor(){
  setWidth(380);
  setHeight(180);
  setUnMerged();
  setMethod("get");
}

  public void main(ModuleInfo modinfo) throws Exception {
    /**
     * @todo permission
     */
    _isAdmin = true; //AccessControl.hasEditPermission(this,modinfo);
    _iwb = getBundle(modinfo);
    _iwrb = getResourceBundle(modinfo);
    addTitle(_iwrb.getLocalizedString("box_category_editor","Category Editor"));
    Locale currentLocale = modinfo.getCurrentLocale(),chosenLocale;

    try {
      _userID = LoginBusiness.getUser(modinfo).getID();
    }
    catch (Exception e) {
      _userID = -1;
    }

    String sLocaleId = modinfo.getParameter(BoxBusiness.PARAMETER_LOCALE_DROP);

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
      processForm(modinfo, iLocaleId, sLocaleId);
    }
    else {
      noAccess();
    }
  }

  private void processForm(ModuleInfo modinfo, int iLocaleId, String sLocaleId) {
    if ( modinfo.getParameter(BoxBusiness.PARAMETER_BOX_ID) != null ) {
      try {
        _boxID = Integer.parseInt(modinfo.getParameter(BoxBusiness.PARAMETER_BOX_ID));
      }
      catch (NumberFormatException e) {
        _boxID = -1;
      }
    }

    if ( modinfo.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID) != null ) {
      try {
        _boxCategoryID = Integer.parseInt(modinfo.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID));
        modinfo.setApplicationAttribute(BoxBusiness.PARAMETER_CATEGORY_ID,Integer.toString(_boxCategoryID));
      }
      catch (NumberFormatException e) {
        _boxCategoryID = -1;
      }
    }

    if ( modinfo.getParameter(BoxBusiness.PARAMETER_MODE) != null ) {
      if ( modinfo.getParameter(BoxBusiness.PARAMETER_MODE).equalsIgnoreCase(BoxBusiness.PARAMETER_CLOSE) ) {
        closePollQuestion(modinfo);
      }
      else if ( modinfo.getParameter(BoxBusiness.PARAMETER_MODE).equalsIgnoreCase(BoxBusiness.PARAMETER_SAVE) ) {
        saveCategory(modinfo,iLocaleId);
        _boxCategoryID = -1;
      }
    }

    if ( _boxCategoryID != -1 ) {
      if ( modinfo.getParameter(BoxBusiness.PARAMETER_MODE) != null ) {
        if ( modinfo.getParameter(BoxBusiness.PARAMETER_MODE).equalsIgnoreCase(BoxBusiness.PARAMETER_DELETE) ) {
          deleteCategory(modinfo);
          _boxCategoryID = -1;
        }
      }
      else {
        _update = true;
      }
    }

    initializeFields(iLocaleId);
  }

  private void initializeFields(int iLocaleID) {
    String categoryName = BoxBusiness.getLocalizedString(BoxFinder.getCategory(_boxCategoryID),iLocaleID);

    Table categoryTable = new Table(3,1);
      categoryTable.setCellpadding(0);
      categoryTable.setCellspacing(0);
      categoryTable.setWidth(2,1,"6");

    DropdownMenu categoryDrop = BoxBusiness.getCategories(BoxBusiness.PARAMETER_CATEGORY_ID,iLocaleID,BoxFinder.getBox(_boxID),_userID);
      categoryDrop.addMenuElementFirst("-1","");
      categoryDrop.setAttribute("style",STYLE);
      categoryDrop.setToSubmit();
      categoryDrop.setSelectedElement(Integer.toString(_boxCategoryID));
    categoryTable.add(categoryDrop,1,1);
    SubmitButton categoryButton = new SubmitButton(_iwrb.getImage("delete.gif"),BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_DELETE);
    if ( _boxCategoryID != -1 )
      categoryTable.add(categoryButton,3,1);

    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(BoxBusiness.PARAMETER_LOCALE_DROP);
      localeDrop.setToSubmit();
      localeDrop.setSelectedElement(Integer.toString(iLocaleID));
    addLeft(_iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);

    TextInput nameInput = new TextInput(BoxBusiness.PARAMETER_CATEGORY_NAME);
      nameInput.setLength(24);
      if ( _update && categoryName != null ) {
        nameInput.setContent(categoryName);
      }

    addLeft(_iwrb.getLocalizedString("category","Category")+":",categoryTable,true,false);
    addLeft(_iwrb.getLocalizedString("category_name","Name")+":",nameInput,true);
    addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_BOX_ID,Integer.toString(_boxID)));
    addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_LOCALE_ID,Integer.toString(iLocaleID)));

    addSubmitButton(new SubmitButton(_iwrb.getImage("close.gif"),BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(_iwrb.getImage("save.gif"),BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_SAVE));
  }

  private void deleteCategory(ModuleInfo modinfo) {
    BoxBusiness.deleteCategory(_boxCategoryID);
  }

  private void saveCategory(ModuleInfo modinfo,int iLocaleID) {
    String categoryName = modinfo.getParameter(BoxBusiness.PARAMETER_CATEGORY_NAME);
    String localeString = modinfo.getParameter(BoxBusiness.PARAMETER_LOCALE_ID);
    int boxCategoryID = -1;

    if ( categoryName == null || categoryName.length() == 0 ) {
      categoryName = _iwrb.getLocalizedString("no_text","No text entered");
    }
    if ( localeString != null ) {
      boxCategoryID = BoxBusiness.saveCategory(_userID,_boxCategoryID,categoryName,Integer.parseInt(localeString));
      modinfo.setApplicationAttribute(BoxBusiness.PARAMETER_CATEGORY_ID,Integer.toString(boxCategoryID));
    }
  }

  private void closePollQuestion(ModuleInfo modinfo) {
    setParentToReload();
    close();
  }

  private void noAccess() throws IOException,SQLException {
    close();
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}
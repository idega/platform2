package com.idega.block.boxoffice.presentation;





import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import com.idega.block.boxoffice.business.BoxBusiness;
import com.idega.block.boxoffice.business.BoxFinder;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;



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



  public void main(IWContext iwc) throws Exception {

    /**

     * @todo permission

     */

    _isAdmin = true; //AccessControl.hasEditPermission(this,iwc);

    _iwb = iwc.getIWMainApplication().getBundle(Builderaware.IW_CORE_BUNDLE_IDENTIFIER);

    _iwrb = getResourceBundle(iwc);

    addTitle(_iwrb.getLocalizedString("box_category_editor","Category Editor"));

    Locale currentLocale = iwc.getCurrentLocale();
    Locale chosenLocale;


    try {

      _userID = LoginBusinessBean.getUser(iwc).getID();

    }

    catch (Exception e) {

      _userID = -1;

    }



    String sLocaleId = iwc.getParameter(BoxBusiness.PARAMETER_LOCALE_DROP);



    int iLocaleId = -1;

    if(sLocaleId!= null){

      iLocaleId = Integer.parseInt(sLocaleId);

      chosenLocale = ICLocaleBusiness.getLocaleReturnIcelandicLocaleIfNotFound(iLocaleId);

    }

    else{

      chosenLocale = currentLocale;

      iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);

    }



    if ( _isAdmin ) {

      processForm(iwc, iLocaleId, sLocaleId);

    }

    else {

      noAccess();

    }

  }



  private void processForm(IWContext iwc, int iLocaleId, String sLocaleId) {

    if ( iwc.getParameter(BoxBusiness.PARAMETER_BOX_ID) != null ) {

      try {

        _boxID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_BOX_ID));

      }

      catch (NumberFormatException e) {

        _boxID = -1;

      }

    }



    if ( iwc.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID) != null ) {

      try {

        _boxCategoryID = Integer.parseInt(iwc.getParameter(BoxBusiness.PARAMETER_CATEGORY_ID));

      }

      catch (NumberFormatException e) {

        _boxCategoryID = -1;

      }

    }



    if ( iwc.getParameter(BoxBusiness.PARAMETER_MODE) != null ) {

      if ( iwc.getParameter(BoxBusiness.PARAMETER_MODE).equalsIgnoreCase(BoxBusiness.PARAMETER_CLOSE) ) {

        closePollQuestion(iwc);

      }

      else if ( iwc.getParameter(BoxBusiness.PARAMETER_MODE).equalsIgnoreCase(BoxBusiness.PARAMETER_SAVE) ) {

        saveCategory(iwc,iLocaleId);

        _boxCategoryID = -1;

      }

    }



    if ( iwc.getSessionAttribute(BoxBusiness.PARAMETER_CATEGORY_ID) != null ) {

      try {

        _boxCategoryID = Integer.parseInt((String)iwc.getSessionAttribute(BoxBusiness.PARAMETER_CATEGORY_ID));

        iwc.removeSessionAttribute(BoxBusiness.PARAMETER_CATEGORY_ID);

      }

      catch (NumberFormatException e) {

        _boxCategoryID = -1;

      }

    }



    if ( _boxCategoryID != -1 ) {

      if ( iwc.getParameter(BoxBusiness.PARAMETER_MODE) != null ) {

        if ( iwc.getParameter(BoxBusiness.PARAMETER_MODE).equalsIgnoreCase(BoxBusiness.PARAMETER_DELETE) ) {

          deleteCategory(iwc);

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

      categoryDrop.setMarkupAttribute("style",STYLE);

      categoryDrop.setToSubmit();

      categoryDrop.setSelectedElement(Integer.toString(_boxCategoryID));

    categoryTable.add(categoryDrop,1,1);

    categoryTable.add(new Link(_iwb.getImage("shared/create.gif")),3,1);

    SubmitButton categoryButton = new SubmitButton(_iwb.getImage("shared/delete.gif"),BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_DELETE);

    if ( _boxCategoryID != -1 )

      categoryTable.add(categoryButton,3,1);



    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(BoxBusiness.PARAMETER_LOCALE_DROP);

      localeDrop.setToSubmit();

      localeDrop.setSelectedElement(Integer.toString(iLocaleID));

    addLeft(_iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);



    TextInput nameInput = new TextInput(BoxBusiness.PARAMETER_CATEGORY_NAME);

      nameInput.setLength(24);

      if ( categoryName != null ) {

        nameInput.setContent(categoryName);

      }



    addLeft(_iwrb.getLocalizedString("category","Category")+":",categoryTable,true,false);

    addLeft(_iwrb.getLocalizedString("category_name","Name")+":",nameInput,true);

    addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_BOX_ID,Integer.toString(_boxID)));

    addHiddenInput(new HiddenInput(BoxBusiness.PARAMETER_LOCALE_ID,Integer.toString(iLocaleID)));



    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("close","CLOSE"),BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_CLOSE));

    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save","SAVE"),BoxBusiness.PARAMETER_MODE,BoxBusiness.PARAMETER_SAVE));

  }



  private void deleteCategory(IWContext iwc) {

    BoxBusiness.deleteCategory(_boxCategoryID);

  }



  private void saveCategory(IWContext iwc,int iLocaleID) {

    String categoryName = iwc.getParameter(BoxBusiness.PARAMETER_CATEGORY_NAME);

    String localeString = iwc.getParameter(BoxBusiness.PARAMETER_LOCALE_ID);

    int boxCategoryID = -1;



    if ( categoryName == null || categoryName.length() == 0 ) {

      categoryName = _iwrb.getLocalizedString("no_text","No text entered");

    }

    if ( localeString != null ) {

      boxCategoryID = BoxBusiness.saveCategory(_userID,_boxCategoryID,categoryName,Integer.parseInt(localeString));

      iwc.setSessionAttribute(BoxBusiness.PARAMETER_CATEGORY_ID,Integer.toString(boxCategoryID));

    }

  }



  private void closePollQuestion(IWContext iwc) {

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

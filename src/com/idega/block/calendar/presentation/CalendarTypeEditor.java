package com.idega.block.calendar.presentation;





import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.block.calendar.business.CalendarBusiness;
import com.idega.block.calendar.business.CalendarFinder;
import com.idega.block.calendar.data.CalendarEntryType;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.text.business.TextFinder;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;



public class CalendarTypeEditor extends IWAdminWindow{



private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.calendar";

private boolean _isAdmin = false;

private boolean _save = false;

private boolean _update = false;

private int _typeID = -1;



private IWBundle _iwb;

private IWResourceBundle _iwrb;



public CalendarTypeEditor(){

  setWidth(380);

  setHeight(250);

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

    addTitle(_iwrb.getLocalizedString("calendar_type_editor","Calendar Type Editor"));

    Locale currentLocale = iwc.getCurrentLocale();
    Locale chosenLocale;


    String sLocaleId = iwc.getParameter(CalendarParameters.PARAMETER_LOCALE_DROP);



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

      processForm(iwc, iLocaleId);

    }

    else {

      noAccess();

    }

  }



  private void processForm(IWContext iwc, int iLocaleId) {

    if ( iwc.getParameter(CalendarParameters.PARAMETER_TYPE_ID) != null ) {

      try {

        _typeID = Integer.parseInt(iwc.getParameter(CalendarParameters.PARAMETER_TYPE_ID));

      }

      catch (NumberFormatException e) {

        _typeID = -1;

      }

    }



    if ( iwc.getParameter(CalendarParameters.PARAMETER_MODE) != null ) {

      if ( iwc.getParameter(CalendarParameters.PARAMETER_MODE).equalsIgnoreCase(CalendarParameters.PARAMETER_MODE_CLOSE) ) {

        closeEditor(iwc);

      }

      else if ( iwc.getParameter(CalendarParameters.PARAMETER_MODE).equalsIgnoreCase(CalendarParameters.PARAMETER_MODE_SAVE) ) {

        saveType(iwc,iLocaleId);

      }

    }



    if ( _typeID == -1 && iwc.getSessionAttribute(CalendarParameters.PARAMETER_TYPE_ID) != null ) {

      try {

        _typeID = Integer.parseInt((String)iwc.getSessionAttribute(CalendarParameters.PARAMETER_TYPE_ID));

      }

      catch (NumberFormatException e) {

        _typeID = -1;

      }

      iwc.removeSessionAttribute(CalendarParameters.PARAMETER_TYPE_ID);

    }



    if ( _typeID != -1 ) {

      if ( iwc.getParameter(CalendarParameters.PARAMETER_MODE_DELETE) != null ) {

        deleteType(iwc);

      }

      else {

        _update = true;

      }

    }



    initializeFields(iLocaleId);

  }



  private void initializeFields(int iLocaleID) {

    CalendarEntryType type = null;

    if ( _update )

      type = CalendarFinder.getInstance().getEntryType(_typeID);



    String[] locStrings = null;

    if ( type != null )

      locStrings = TextFinder.getLocalizedString(type,iLocaleID);



    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(CalendarParameters.PARAMETER_LOCALE_DROP);

      localeDrop.setToSubmit();

      localeDrop.setSelectedElement(Integer.toString(iLocaleID));

    addLeft(_iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);



    Table typesTable = new Table(3,1);

      typesTable.setCellpadding(0);

      typesTable.setCellspacing(0);



    DropdownMenu entryTypes = CalendarBusiness.getEntryTypes(CalendarParameters.PARAMETER_TYPE_ID,iLocaleID);

      entryTypes.addMenuElementFirst("-1","");

      entryTypes.setToSubmit();

      entryTypes.setMarkupAttribute("style",STYLE);

      if ( _typeID != -1 )

        entryTypes.setSelectedElement(Integer.toString(_typeID));

    typesTable.add(entryTypes,1,1);

    typesTable.setWidth(2,1,"5");



    Image newImage = _iwb.getImage("shared/create.gif",_iwrb.getLocalizedString("new_type","New type"));

    Link newLink = new Link(newImage);

    typesTable.add(newLink,3,1);



    Image deleteImage = _iwb.getImage("shared/delete.gif",_iwrb.getLocalizedString("delete_type","Delete type"));

    Link deleteLink = new Link(deleteImage);

      deleteLink.addParameter(CalendarParameters.PARAMETER_MODE_DELETE,CalendarParameters.PARAMETER_TRUE);

      deleteLink.addParameter(CalendarParameters.PARAMETER_TYPE_ID,_typeID);

    typesTable.add(deleteLink,3,1);



    addLeft(_iwrb.getLocalizedString("type","Type")+":",typesTable,true,false);



    TextInput nameInput = new TextInput(CalendarParameters.PARAMETER_ENTRY_HEADLINE);

      nameInput.setLength(24);

      if ( locStrings != null && locStrings[0] != null )

        nameInput.setContent(locStrings[0]);

    addLeft(_iwrb.getLocalizedString("name","Name")+":",nameInput,true);



    ImageInserter image = new ImageInserter(CalendarParameters.PARAMETER_FILE_ID);

      image.setHasUseBox(false);

      if ( type != null && type.getImageID() != -1 )

        image.setImageId(type.getImageID());

    addRight(_iwrb.getLocalizedString("new_image","New image")+":",image,true,false);



    addHiddenInput(new HiddenInput(CalendarParameters.PARAMETER_TYPE_ID,Integer.toString(_typeID)));

    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("close","CLOSE"),CalendarParameters.PARAMETER_MODE,CalendarParameters.PARAMETER_MODE_CLOSE));

    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save","SAVE"),CalendarParameters.PARAMETER_MODE,CalendarParameters.PARAMETER_MODE_SAVE));

  }



  private void deleteType(IWContext iwc) {

    CalendarBusiness.deleteEntryType(_typeID);

    _typeID = -1;

  }



  private void saveType(IWContext iwc,int iLocaleID) {

    String typeHeadline = iwc.getParameter(CalendarParameters.PARAMETER_ENTRY_HEADLINE);

    String fileID = iwc.getParameter(CalendarParameters.PARAMETER_FILE_ID);



    int typeID = CalendarBusiness.saveEntryType(_typeID,iLocaleID,typeHeadline,fileID);

    iwc.setSessionAttribute(CalendarParameters.PARAMETER_TYPE_ID,Integer.toString(typeID));

  }



  private void closeEditor(IWContext iwc) {

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

package com.idega.block.staff.presentation;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.staff.business.StaffBusiness;
import com.idega.block.staff.business.StaffFinder;
import com.idega.block.staff.data.StaffEntity;
import com.idega.block.staff.data.StaffLocalized;
import com.idega.block.staff.data.StaffMeta;
import com.idega.block.text.business.TextFinder;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

public class StaffEditor extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.staff";
private boolean _isAdmin = false;
private boolean _update = true;
private boolean _save = false;

private int _userID = -1;
private IWTimestamp _stamp;

private IWBundle _iwb;
private IWResourceBundle _iwrb;

public StaffEditor(){
  setWidth(590);
  setHeight(590);
  setUnMerged();
}

  public void main(IWContext iwc) throws Exception {
    /**
     * @todo permission
     */
    _isAdmin = true;
    _iwb = getBundle(iwc);
    _iwrb = getResourceBundle(iwc);
    addTitle(_iwrb.getLocalizedString("staff_admin","Staff admin"));
    Locale currentLocale = iwc.getCurrentLocale();
    Locale chosenLocale;

    String sLocaleId = iwc.getParameter(StaffBusiness.PARAMETER_LOCALE_DROP);

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
      processForm(iwc,iLocaleId);
    }
    else {
      noAccess();
    }
  }

  private void processForm(IWContext iwc, int iLocaleId) {
    if ( iwc.getParameter(StaffBusiness.PARAMETER_USER_ID) != null ) {
      try {
        _userID = Integer.parseInt(iwc.getParameter(StaffBusiness.PARAMETER_USER_ID));
      }
      catch (NumberFormatException e) {
        _userID = -1;
      }
    }

    if ( iwc.getParameter(StaffBusiness.PARAMETER_MODE) != null ) {
      if ( iwc.getParameter(StaffBusiness.PARAMETER_MODE).equalsIgnoreCase(StaffBusiness.PARAMETER_CLOSE) ) {
        closeEditor(iwc);
      }
      else if ( iwc.getParameter(StaffBusiness.PARAMETER_MODE).equalsIgnoreCase(StaffBusiness.PARAMETER_SAVE) ) {
        saveEntry(iwc,iLocaleId);
      }
      else if ( iwc.getParameter(StaffBusiness.PARAMETER_DELETE) != null ) {
        deleteEntry(iwc);
      }
    }

    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(StaffBusiness.PARAMETER_LOCALE_DROP);
      localeDrop.setToSubmit();
      localeDrop.setSelectedElement(Integer.toString(iLocaleId));
    addLeft(_iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);
    addHiddenInput(new HiddenInput(StaffBusiness.PARAMETER_USER_ID,Integer.toString(_userID)));

    initializeFields(iLocaleId);
  }

  private void initializeFields(int iLocaleId) {
    StaffEntity entity = StaffFinder.getStaff(_userID);
    StaffMeta[] meta = StaffFinder.getMeta(_userID,iLocaleId);
    if ( entity == null )
      _update = false;

    StaffLocalized locTexts = null;
    if ( _update )
      locTexts = StaffFinder.getLocalizedStaff(entity,iLocaleId);
    if ( _update && entity.getBeganWork() != null )
      _stamp = new IWTimestamp(entity.getBeganWork());

    TextInput title = new TextInput(StaffBusiness.PARAMETER_TITLE);
      title.setLength(24);
      if ( locTexts != null && locTexts.getTitle() != null ) {
        title.setContent(locTexts.getTitle());
      }
    addLeft(_iwrb.getLocalizedString("user_title","Title")+":",title,true);

    TextArea education = new TextArea(StaffBusiness.PARAMETER_EDUCATION,55,3);
      if ( locTexts != null && locTexts.getEducation() != null) {
        education.setContent(locTexts.getEducation());
      }
    addLeft(_iwrb.getLocalizedString("user_education","Education")+":",education,true);

    TextArea area = new TextArea(StaffBusiness.PARAMETER_AREA,55,3);
      if ( locTexts != null && locTexts.getArea() != null ) {
        area.setContent(locTexts.getArea());
      }
    addLeft(_iwrb.getLocalizedString("user_area","Area")+":",area,true);

    DateInput beganWork = new DateInput(StaffBusiness.PARAMETER_BEGAN_WORK);
      beganWork.setYearRange(new IWTimestamp().getYear()-60,new IWTimestamp().getYear());
      if ( _stamp != null ) {
        beganWork.setDate(_stamp.getDate());
      }
      beganWork.setStyleAttribute("style",STYLE);
    addLeft(_iwrb.getLocalizedString("user_began_work","Began work")+":",beganWork,true);

    Table metaTable = new Table(2,6);
      metaTable.setColumnVerticalAlignment(1,"top");
    for ( int a = 0; a < 6; a++ ) {
      TextInput attribute = new TextInput(StaffBusiness.PARAMETER_META_ATTRIBUTE);
        if ( meta != null && meta.length >= a )
          try {
            attribute.setContent(meta[a].getAttribute());
          }
          catch (Exception e) {
            attribute.setContent("");
          }
        attribute.setMarkupAttribute("style",STYLE);
        attribute.setLength(20);
      metaTable.add(attribute,1,a+1);

      TextArea value = new TextArea(StaffBusiness.PARAMETER_META_VALUE,40,2);
        if ( meta != null && meta.length >= a )
          try {
            value.setContent(meta[a].getValue());
          }
          catch (Exception e) {
            value.setContent("");
          }
        value.setMarkupAttribute("style",STYLE);
      metaTable.add(value,2,a+1);
    }
    addLeft(_iwrb.getLocalizedString("extra_info","Extra info")+":",metaTable,true,false);

    ImageInserter image = new ImageInserter(StaffBusiness.PARAMETER_IMAGE_ID);
      image.setWindowClassToOpen(com.idega.block.media.presentation.MediaChooserWindow.class);
      image.setHasUseBox(false);
      if ( entity != null && entity.getImageID() != -1 )
        image.setImageId(entity.getImageID());
    addRight(_iwrb.getLocalizedString("image","Image")+":",image,true,false);

    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("close","CLOSE"),StaffBusiness.PARAMETER_MODE,StaffBusiness.PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(_iwrb.getLocalizedImageButton("save","SAVE"),StaffBusiness.PARAMETER_MODE,StaffBusiness.PARAMETER_SAVE));
  }

  private void saveEntry(IWContext iwc, int localeID) {
    String title = iwc.getParameter(StaffBusiness.PARAMETER_TITLE);
    String education = iwc.getParameter(StaffBusiness.PARAMETER_EDUCATION);
    String area = iwc.getParameter(StaffBusiness.PARAMETER_AREA);
    String beganwork = iwc.getParameter(StaffBusiness.PARAMETER_BEGAN_WORK);
    if ( beganwork != null )
      try {
        _stamp = new IWTimestamp(beganwork);
      }
      catch (Exception e) {
        _stamp = null;
      }
    String imageID = iwc.getParameter(StaffBusiness.PARAMETER_IMAGE_ID);

    String[] values = iwc.getParameterValues(StaffBusiness.PARAMETER_META_VALUE);
    String[] attributes = iwc.getParameterValues(StaffBusiness.PARAMETER_META_ATTRIBUTE);

    StaffBusiness.saveStaff(localeID,_userID,title,education,area,_stamp,imageID);
    StaffBusiness.saveMetaData(localeID,_userID,attributes,values);
  }

  private void deleteEntry(IWContext iwc) {
    StaffBusiness.delete(_userID);
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

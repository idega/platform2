package com.idega.block.text.presentation;


import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.block.text.data.*;
import com.idega.block.text.business.*;
import com.idega.core.user.data.User;
import com.idega.jmodule.image.presentation.ImageInserter;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.util.text.*;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

public class TextEditorWindow extends IWAdminWindow{

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.text";
  private boolean isAdmin = false;
  private boolean update = false;
  private boolean save = false;
  private int iUserId = -1;
  private static String prmAttribute = "txe.attribute";
  private static String prmTextId = "txe.textid";
  private static String prmHeadline = "txep.headline",prmBody = "txep.headline",
    prmImageId = "txep.imageid",prmLocale = "txep.localedrp",
    prmTxTextId = "txep.txtextid",prmLocalizedTextId = "txep.loctextid",prmUseImage = "txep.useimage";
  private static String prmMode = "txep.mode",prmAction = "txep.action";
  private static String actNone = "txea.none",actDelete = "txea.delete",actSave = "txea.save",actUpdate = "txea.update";
  private static String modeNew = "txem.new",modeUpdate ="txem.update",modeSave = "txem.save",modeDelete = "txea.delete";
  private TextHelper textHelper;

  private IWBundle iwb;
  private IWResourceBundle iwrb;

  public TextEditorWindow(){
    setWidth(570);
    setHeight(430);
  }

  public void control(ModuleInfo modinfo)throws Exception{
    Locale currentLocale = modinfo.getCurrentLocale(),chosenLocale;
    java.util.Enumeration e= modinfo.getParameterNames();
    while(e.hasMoreElements())
      System.err.println(e.nextElement());
    String sLocaleId = modinfo.getParameter(prmLocale);
    if(sLocaleId!= null){
      int iLocaleId = Integer.parseInt(sLocaleId);
      chosenLocale = TextFinder.getLocale(iLocaleId);
    }
    else{
      chosenLocale = currentLocale;
    }

    if ( isAdmin ) {
      String sAction;

    String sTextId = null,sAttribute = null;
    String sLocTextId = modinfo.getParameter(prmLocalizedTextId);
    if(modinfo.getParameter(prmTextId) != null){
      sTextId = modinfo.getParameter(prmTextId);
      add(sTextId);
    }
    else if(modinfo.getParameter(prmAttribute)!=null){
      sAttribute = modinfo.getParameter(actSave);
    }

    if(modinfo.getParameter(actSave)!=null || modinfo.getParameter(actSave+".x")!=null ){
      saveText(modinfo,sTextId,sLocTextId);
    }
    else if(modinfo.getParameter( actDelete )!=null || modinfo.getParameter(actDelete+".x")!=null){
      deleteText(modinfo,sTextId,sLocTextId);
    }
    doViewText(sTextId,sAttribute,chosenLocale);
    }
    else {
      noAccess();
    }

  }

  public void doViewText(String sTextId,String sAttribute,Locale locale){
    TextHelper textHelper = null;
    if(sTextId != null){

      int iTextId = Integer.parseInt(sTextId);
      textHelper = TextFinder.getTextHelper(iTextId,locale);
    }
    else if(sAttribute != null){
      textHelper = TextFinder.getTextHelper(sAttribute,locale);
    }
    LocalizedText LocTx = null;
    TxText  TxTx = null;
    if(textHelper != null){
      LocTx = textHelper.getLocalizedText(locale);
      TxTx = textHelper.getTxText();

    }
    else
      System.err.println("texthelper ekki null");
    addLocalizedTextFields(LocTx,TxTx);
  }

  public void addLocalizedTextFields(LocalizedText locText,TxText txText){
    boolean hasTxText = ( txText != null ) ? true: false;
    boolean hasLocalizedText = ( locText != null ) ? true: false;
    if(hasTxText) System.err.println("text ekki null");
    if(hasLocalizedText) System.err.println("loctext ekki null");

    TextInput tiHeadline = new TextInput(prmHeadline);
    tiHeadline.setLength(40);
    tiHeadline.setMaxlength(255);
    List L = TextFinder.listOfLocales();
    DropdownMenu LocaleDrop = new DropdownMenu();
    if(L!= null){
       LocaleDrop = new DropdownMenu(L);
    }

    TextArea taBody = new TextArea(prmBody,65,22);
    if ( hasLocalizedText ) {
      if ( locText.getHeadline() != null ) {
        tiHeadline.setContent(locText.getHeadline());
      }
      if ( locText.getBody() != null ) {
        taBody.setContent(locText.getBody());
      }
      addHiddenInput(new HiddenInput(prmLocalizedTextId,String.valueOf(locText.getID())));
    }

    if( hasLocalizedText )
      addHiddenInput(new HiddenInput(prmTxTextId,Integer.toString(txText.getID())));

    SubmitButton save = new SubmitButton(iwrb.getImage("save.gif"),actSave);

    ImageInserter imageInsert = new ImageInserter();
    imageInsert.setImSessionImageName(prmImageId);
    imageInsert.setSelected(false);
      if ( update ) {
        if ( txText.getIncludeImage()) {
          imageInsert.setImageId(txText.getImageId());
          imageInsert.setSelected(true);
        }
      }

    addLeft(iwrb.getLocalizedString("title","Title"),tiHeadline,true);
    addLeft(iwrb.getLocalizedString("locale","Locale"), LocaleDrop,true);
    addLeft(iwrb.getLocalizedString("body","Text"),taBody,true);
    addRight(iwrb.getLocalizedString("image","Image"),imageInsert,true);
    addSubmitButton(save);
    addHiddenInput(new HiddenInput(prmMode,modeSave));
  }

  public void noAccess() throws IOException,SQLException {
    addLeft(iwrb.getLocalizedString("no_access","Login first!"));
    this.addSubmitButton(new CloseButton(iwrb.getLocalizedString("close","Closee")));
  }

  public void confirmDelete(ModuleInfo modinfo) throws IOException,SQLException {
    TextModule text = TextBusiness.getTextModule(modinfo);
    String textHeadline = text.getTextHeadline();

    if ( textHeadline != null ) {
      addLeft(iwrb.getLocalizedString("text_to_delete","Text to delete")+": "+textHeadline);
      addLeft(iwrb.getLocalizedString("confirm_delete","Are you sure?"));
      addHiddenInput(new HiddenInput("text_id",Integer.toString(text.getID())));
      addHiddenInput(new HiddenInput(prmAction,actDelete));
      addHiddenInput(new HiddenInput(prmMode,modeDelete));
      addSubmitButton(new SubmitButton(iwrb.getImage("delete.gif")));
    }
    else {
      addLeft(iwrb.getLocalizedString("not_exists","Text already deleted or not available."));
      addSubmitButton(new CloseButton(iwrb.getImage("close.gif")));
    }
  }

  public void saveText(ModuleInfo modinfo,String sTxTextId,String sLocalizedTextId){
    String sHeadline = modinfo.getParameter( prmHeadline );
    String sBody = modinfo.getParameter(prmBody );
    String sImageId = modinfo.getParameter(prmImageId);
    String sLocaleId = modinfo.getParameter(prmLocale);
    String sUseImage = modinfo.getParameter(prmUseImage);
    if(sHeadline != null || sBody != null){
      int iTxTextId = sTxTextId!=null?Integer.parseInt(sTxTextId): -1;
      int iLocalizedTextId = sLocalizedTextId != null ? Integer.parseInt(sLocalizedTextId): -1;
      int iLocaleId = sLocaleId != null ? Integer.parseInt(sLocaleId):-1;
      int iImageId = sImageId != null ? Integer.parseInt(sImageId):-1;
      boolean bUseImage = sUseImage!= null?true:false;
      TextBusiness.saveText(iTxTextId,iLocalizedTextId,sHeadline,"",sBody,iImageId,bUseImage,iLocaleId,iUserId);
    }
    TextBusiness.saveText(modinfo,update);
    //setParentToReload();
    //close();
  }

  public void deleteText(ModuleInfo modinfo,String sTxTextId,String sLocalizedTextId ) {
    TextBusiness.deleteText(modinfo);
    setParentToReload();
    close();
  }

  public void main(ModuleInfo modinfo) throws Exception {
    super.main(modinfo);
    isAdmin = AccessControl.hasEditPermission(new TextReader(),modinfo);
    User u= com.idega.block.login.business.LoginBusiness.getUser(modinfo);
    iUserId = u != null?u.getID():-1;
    isAdmin = true;
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);
    addTitle(iwrb.getLocalizedString("text_editor","Text Editor"));
    control(modinfo);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}
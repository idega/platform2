package com.idega.block.text.presentation;


import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.block.text.data.*;
import com.idega.block.text.business.*;
import com.idega.core.user.data.User;
//import com.idega.jmodule.image.presentation.ImageInserter;
import com.idega.block.media.presentation.SimpleChooserWindow;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.util.text.*;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.core.data.ICFile;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class TextEditorWindow extends AbstractChooserWindow{

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.text";
  private boolean isAdmin = false;
  private boolean update = false;
  private boolean save = false;
  private int iUserId = -1;
  private int iObjInsId = -1;
  public final static String prmAttribute = "txe_attribute";
  public final static String prmTextId = "txep_txtextid";
  public final static String prmDelete = "txep_txdeleteid";
  public final static String prmLocale = "txep_localedrp";
  public final static String prmObjInstId = "txep_icobjinstid";
  private final static String prmHeadline = "txep_headline";
  private final static String prmBody = "txep_body";
  public final static String imageAttributeKey = "txre_im_prop";
  public boolean debugParameter = true;

  private String prmUsedTextId = prmTextId;
  //debug
  //private static String prmImageId = "txep.imageid";
  private static String prmImageId = "txep_imageid";
  //private static String prmTextId = "txep_txtextid";
  private static String prmLocalizedTextId = "txep_loctextid";
  private static String prmUseImage = "txep_useimage";
  private static String prmDeleteFile = "txep_deletefile";
  private static String prmSaveFile = "txep_savefile";
  private static String actDelete = "txea_delete";
  private static String actSave = "txea_save";
  private static String actClose = "txea_close";
  private static String actUpdate = "txea_update" ;
  private static String actNew = "txea_new";
  private static String modeNew = "txem_new";
  private static String modeDelete = "txem_delete";

  public static final String ONCLICK_FUNCTION_NAME = "textselect";
  public static final String TEXT_ID_PARAMETER_NAME = "text_id";
  public static final String TEXT_NAME_PARAMETER_NAME = "text_name";
  private boolean parentReload = true;
  private String sTextId = null;

  private TextHelper textHelper;


  private IWBundle iwb,core;
  private IWResourceBundle iwrb;

  public TextEditorWindow(){
    super();
    setWidth(570);
    setHeight(430);
    setResizable(true);
    setUnMerged();
  }

  private void control(IWContext iwc)throws Exception{
    if(debugParameter){
      debugParameters(iwc);
    }

    //Checks if the Window is being usen by the TextChooser
    //if chooserParameterName is null it is not being used by TextChooser
    String chooserParameterName = super.getSelectionParameter(iwc);
    if(chooserParameterName!=null){
      System.out.println("chooserParameterName!=null");
      System.out.println("chooserParameterName="+chooserParameterName);
      System.out.println("iwc.getParameter(chooserParameterName)="+iwc.getParameter(chooserParameterName));
      if(iwc.isParameterSet(chooserParameterName))
      prmUsedTextId=chooserParameterName;
    }
    else{
      System.out.println("chooserParameterName==null");
    }

    boolean doView = true;
    Locale currentLocale = iwc.getCurrentLocale(),chosenLocale;

    String sLocaleId = iwc.getParameter(prmLocale);
    String sAtt = null;

    if(iwc.isParameterSet(actClose)|| iwc.isParameterSet(actClose+".x")){
      if (parentReload) {
        setParentToReload();
      }
      close();
    }
    else{
    // LocaleHandling
    int iLocaleId = -1;
    if(sLocaleId!= null){
      iLocaleId = Integer.parseInt(sLocaleId);
      chosenLocale = TextFinder.getLocale(iLocaleId);
    }
    else{
      chosenLocale = currentLocale;
      iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);
    }

    if ( isAdmin ) {
      String sAction;
    // end of LocaleHandling

    // Text initialization
    String sAttribute = null;
    String sLocTextId = iwc.getParameter(prmLocalizedTextId);
    sAttribute = iwc.getParameter(prmAttribute);
    sTextId = iwc.getParameter(prmUsedTextId);

    // Delete Request :
    if(iwc.getParameter(prmDelete)!=null){
      sTextId = iwc.getParameter(prmDelete);
      //add(""+iObjInsId);
      confirmDelete(sTextId,iObjInsId);
      doView = false;
    }
    // Object Instance Request :
    if(iwc.getParameter(prmObjInstId)!= null){
      iObjInsId = Integer.parseInt(iwc.getParameter(prmObjInstId ) );
    }

    // end of Text initialization

    // Form processing
    processForm(iwc,sTextId,sLocTextId, sAttribute);
      if(doView)
        doViewText(sTextId,sAttribute,chosenLocale,iLocaleId);
      }
      else {
        noAccess();
      }
    }
  }

  // Form Processing :
  private void processForm(IWContext iwc,String sTextId,String sLocTextId,String sAttribute){

    // Save :
    if(iwc.getParameter(actSave)!=null || iwc.getParameter(actSave+".x")!=null ){
      saveText(iwc,sTextId,sLocTextId,sAttribute);
    }
    // Delete :
    else if(iwc.getParameter( actDelete )!=null || iwc.getParameter(actDelete+".x")!=null){
      try {
        if(iwc.getParameter(modeDelete)!=null){
          int I = Integer.parseInt(iwc.getParameter(modeDelete));
          deleteText(I);
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    // New:
     /** @todo make possible */
    else if(iwc.getParameter( actNew ) != null || iwc.getParameter(actNew+".x")!= null){
      sTextId = null;sAttribute = null;
    }
    else if(iwc.getParameter(prmDeleteFile)!=null){
      if(sTextId!=null){
      String sFileId = iwc.getParameter(prmDeleteFile);
      deleteFile(sTextId,sFileId);
      }
    }
    else if(iwc.getParameter(prmSaveFile)!= null || iwc.getParameter(prmSaveFile+".x")!=null){
      if(sTextId!=null){
        String sFileId = iwc.getParameter(prmImageId);
        saveFile(sTextId,sFileId);
      }
    }
    // end of Form Actions
  }

  private void doViewText(String sTextId,String sAttribute,Locale locale,int iLocaleId){
    ContentHelper contentHelper = null;
    TxText eTxText = null;
    int iTextId = -1;
    if(sTextId != null){
      iTextId = Integer.parseInt(sTextId);
      eTxText = TextFinder.getText(iTextId);
      if(iLocaleId > 0)
        contentHelper = TextFinder.getContentHelper(iTextId,iLocaleId);
      else
        contentHelper = TextFinder.getContentHelper(iTextId,locale);
    }
    else if(sAttribute != null){
      contentHelper = TextFinder.getContentHelper(sAttribute,iLocaleId);
    }

    addLocalizedTextFields(eTxText,contentHelper,iLocaleId,sAttribute,iObjInsId);
  }

  private void addLocalizedTextFields(TxText txText,ContentHelper contentHelper, int iLocaleId,String sAttribute,int iObjInsId){
    LocalizedText locText = null;
    boolean hasTxText = ( txText != null ) ? true: false;
    //boolean hasLocalizedText = ( locText != null ) ? true: false;
    boolean hasContent = ( contentHelper != null) ? true:false;
    if(hasContent)
      locText = contentHelper.getLocalizedText(TextFinder.getLocale(iLocaleId));
    boolean hasLocalizedText = ( locText !=null ) ? true:false;

    Link propslink = null;
    TextInput tiHeadline = new TextInput(prmHeadline);
    tiHeadline.setLength(40);
    tiHeadline.setMaxlength(255);
    List L = TextFinder.listOfLocales();
    DropdownMenu LocaleDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(prmLocale);
    LocaleDrop.setToSubmit();
    LocaleDrop.setSelectedElement(Integer.toString(iLocaleId));

    TextArea taBody = new TextArea(prmBody,65,18);
    if ( hasLocalizedText ) {
      if ( locText.getHeadline() != null ) {
        tiHeadline.setContent(locText.getHeadline());
      }
      if ( locText.getBody() != null ) {
        taBody.setContent(locText.getBody());
      }
      addHiddenInput(new HiddenInput(prmLocalizedTextId,String.valueOf(locText.getID())));
    }

    if( hasTxText )
      addHiddenInput(new HiddenInput(prmUsedTextId,Integer.toString(txText.getID())));
    if(sAttribute != null)
      addHiddenInput(new HiddenInput(prmAttribute,sAttribute));
    if(iObjInsId > 0)
      addHiddenInput(new HiddenInput(prmObjInstId,String.valueOf(iObjInsId)));

    SubmitButton addButton = null;
    addButton = new SubmitButton(core.getImage("/shared/create.gif"),prmSaveFile);
    ImageInserter imageInsert = new ImageInserter();
    imageInsert.setImSessionImageName(prmImageId);
    imageInsert.setUseBoxParameterName(prmUseImage);
    imageInsert.setWindowClassToOpen(SimpleChooserWindow.class);
    imageInsert.setMaxImageWidth(130);
    imageInsert.setHasUseBox(false);
    imageInsert.setSelected(false);
    Table imageTable = new Table();
    int row = 1;
    //imageTable.mergeCells(1,row,3,row);
    //imageTable.add(formatText(iwrb.getLocalizedString("image","Chosen image :")),1,row++);
    imageTable.mergeCells(1,row,3,row);
    imageTable.add(imageInsert,1,row++);
    imageTable.mergeCells(1,row,3,row);
    //imageTable.add(leftButton,1,row);
    imageTable.add(addButton,1,row++);

    if ( hasContent ) {
      List files = contentHelper.getFiles();
      if(files != null){
        imageTable.mergeCells(1,row,3,row);
        imageTable.add( formatText(iwrb.getLocalizedString("textimages","Text images :")),1,row++);
        ICFile file1 = (ICFile) files.get(0);
        imageInsert.setImageId(file1.getID());

        Iterator I = files.iterator();
        while(I.hasNext()){
          try {

          ICFile f = (ICFile) I.next();
          Image immi = new Image(f.getID());
          immi.setMaxImageWidth(50);

          imageTable.add(immi,1,row);
          //Link edit = new Link(iwb.getImage("/shared/edit.gif"));
          Link edit = com.idega.block.media.presentation.ImageAttributeSetter.getLink(iwb.getImage("/shared/edit.gif"),file1.getID(),imageAttributeKey);
          Link delete = new Link(core.getImage("/shared/delete.gif"));
          delete.addParameter(prmDeleteFile,f.getID());
          delete.addParameter(prmUsedTextId,txText.getID());
          imageTable.add(edit,2,row);
          imageTable.add(delete,3,row);
          row++;
          }
          catch (Exception ex) {

          }
        }
      }

    }
    addLeft(iwrb.getLocalizedString("title","Title"),tiHeadline,true);
    addLeft(iwrb.getLocalizedString("locale","Locale"), LocaleDrop,true);
    addLeft(iwrb.getLocalizedString("body","Text"),taBody,true);
    addRight(iwrb.getLocalizedString("image","Image"),imageTable,true,false);


    SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),actSave);
    SubmitButton close = new SubmitButton(iwrb.getLocalizedImageButton("close","Close"),actClose);
      getAssociatedScript().addFunction(ONCLICK_FUNCTION_NAME,"function "+ONCLICK_FUNCTION_NAME+"("+TEXT_NAME_PARAMETER_NAME+","+TEXT_ID_PARAMETER_NAME+"){ }");
      getAssociatedScript().addToFunction(ONCLICK_FUNCTION_NAME,AbstractChooserWindow.SELECT_FUNCTION_NAME+"("+TEXT_NAME_PARAMETER_NAME+","+TEXT_ID_PARAMETER_NAME+")");
    if (txText != null) {
      close.setOnClick(ONCLICK_FUNCTION_NAME+"('"+txText.getID()+"','"+txText.getID()+"')");
    }
    addSubmitButton(save);
    addSubmitButton(close);
  }

  private void noAccess() throws IOException,SQLException {
    addLeft(iwrb.getLocalizedString("no_access","Login first!"));
    this.addSubmitButton(new CloseButton(iwrb.getLocalizedString("close","Closee")));
  }

	private void saveFile(String sTextId,String sFileId){
		TxText tx = TextFinder.getText(Integer.parseInt(sTextId));
	  ContentBusiness.addFileToContent(tx.getContentId(),Integer.parseInt(sFileId));
	}

	private void deleteFile(String sTextId,String sFileId){
	  TxText tx = TextFinder.getText(Integer.parseInt(sTextId));
	  ContentBusiness.removeFileFromContent(tx.getContentId(),Integer.parseInt(sFileId));
	}


  private void confirmDelete(String sTextId,int iObjInsId ) throws IOException,SQLException {
    int iTextId = Integer.parseInt(sTextId);
    TxText  txText= TextFinder.getText(iTextId);

    if ( txText != null ) {
      addLeft(iwrb.getLocalizedString("text_to_delete","Text to delete"));
      addLeft(iwrb.getLocalizedString("confirm_delete","Are you sure?"));
      addSubmitButton(new SubmitButton(iwrb.getImage("delete.gif"),actDelete));
      addHiddenInput(new HiddenInput(modeDelete,String.valueOf(txText.getID())));
    }
    else {
      addLeft(iwrb.getLocalizedString("not_exists","Text already deleted or not available."));
      addSubmitButton(new CloseButton(iwrb.getImage("close.gif")));
    }
  }

  private void saveText(IWContext iwc,String sTxTextId,String sLocalizedTextId,String sAttribute){
    String sHeadline = iwc.getParameter( prmHeadline );
    String sBody = iwc.getParameter(prmBody );
    String sImageId = iwc.getParameter(prmImageId);
    String sLocaleId = iwc.getParameter(prmLocale);
    String sUseImage = iwc.getParameter(prmUseImage);
    if(sHeadline != null || sBody != null){
      int iTxTextId = sTxTextId!=null?Integer.parseInt(sTxTextId): -1;
      int iLocalizedTextId = sLocalizedTextId != null ? Integer.parseInt(sLocalizedTextId): -1;
      int iLocaleId = sLocaleId != null ? Integer.parseInt(sLocaleId):-1;
      int iImageId = sImageId != null ? Integer.parseInt(sImageId):-1;
      boolean bUseImage = sUseImage!= null?true:false;
      Vector files = null;

      try {
        ICFile file = new ICFile(iImageId);
        files = new Vector();
        files.add(file);
      }
      catch (SQLException ex) {
      }

      TxText tx = TextBusiness.saveText(iTxTextId,iLocalizedTextId,iLocaleId,iUserId,iObjInsId,null,null,sHeadline,"",sBody,sAttribute,files);
      sTextId = String.valueOf(tx.getID());
    }

  }

  private void deleteText(int iTextId ) {
    /**
     * @todo hondla fyrir TextChooser
     */
    TextBusiness.deleteText(iTextId);
    setParentToReload();
    close();
  }

  public void displaySelection(IWContext iwc) {
//    super.main(iwc);
    isAdmin = iwc.hasEditPermission(new TextReader());
    User u= com.idega.block.login.business.LoginBusiness.getUser(iwc);
    iUserId = u != null?u.getID():-1;
    isAdmin = true;
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    core = iwc.getApplication().getBundle(TextReader.IW_CORE_BUNDLE_IDENTIFIER);
    reloadCheck(iwc);
    addTitle(iwrb.getLocalizedString("text_editor","Text Editor"));
		try{
    control(iwc);
		}
		catch(Exception e){
		  e.printStackTrace();
		}
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void reloadCheck(IWContext iwc) {
    if (iwc.getSessionAttribute(TextChooser.RELOAD_PARENT_PARAMETER) != null) {
      parentReload = false;
    }
  }

  public void setDebugParameters(boolean debug){
    debugParameter = debug;
  }

  public void setParentToReload(boolean reload) {
    this.parentReload = reload;
  }

/*  public void displaySelection(IWContext iwc) {
  }*/
}

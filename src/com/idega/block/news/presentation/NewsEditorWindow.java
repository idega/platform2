package com.idega.block.news.presentation;


import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.block.news.business.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.news.data.*;
import com.idega.block.text.data.*;
import com.idega.block.text.business.*;
import com.idega.core.user.data.User;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.core.localisation.business.ICLocaleBusiness;
//import com.idega.jmodule.image.presentation.ImageInserter;
//import com.idega.jmodule.image.presentation.SimpleChooserWindow;
import com.idega.block.media.presentation.SimpleChooserWindow;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.data.*;
import com.idega.util.text.*;
import com.idega.util.text.TextSoap;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.core.data.ICFile;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class NewsEditorWindow extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.news";
private String Error;
private boolean isAdmin=false;
private int iUserId = -1;
private User eUser = null;
private int iObjInsId = -1;
private int defaultPublishDays = 50;

private static String prmHeadline = "nwep_headline";
private static String prmAuthor = "nwep_author";
private static String prmSource = "nwep_source";
private static String prmDaysshown = "nwep_daysshown";
private static String prmBody = "nwe.body";
public static String prmCategory = "nwep_category";
private static String prmLocale = "nwep_locale";
private static String prmLocalizedTextId = "nwep_loctextid";
public static String prmObjInstId = "nwep_icobjinstid";
public static String prmAttribute = "nwep_attribute";
private static String prmUseImage = "insertImage";//nwep.useimage
public  static String prmDelete = "nwep_txdeleteid";
private static String prmImageId = "nwep_imageid";
public static String prmNwNewsId = "nwep_nwnewsid";
private static String actDelete = "nwea_delete";
private static String actSave = "nwea_save";
private static String modeDelete = "nwem_delete";
private static String prmFormProcess = "nwe_formprocess";
private static String prmNewCategory = "nwep_newcategory";
private static String prmEditCategory = "nwep_editcategory";
private static String prmCatName= "nwep_categoryname";
private static String prmCatDesc = "nwep_categorydesc";
private static String prmPubFrom = "nwep_publishfrom";
private static String prmPubTo = "nwep_publishto";
public static final  String imageAttributeKey = "newsimage";



private String sEditor,sHeadline,sNews,sCategory,sAuthor,sSource,sDaysShown,sImage,sLocale,sPublisFrom,sPublisTo;

private String attributeName = "union_id";
private int attributeId = 3;
private IWBundle iwb;
private IWResourceBundle iwrb;

  public NewsEditorWindow(){
    setWidth(570);
    setHeight(500);
    setResizable(true);
    setUnMerged();
  }

  private void init(){
    sHeadline = iwrb.getLocalizedString("headline","Headline");
    sLocale =  iwrb.getLocalizedString("locale","Locale");
    sNews = iwrb.getLocalizedString("news","News");
    sCategory = iwrb.getLocalizedString("category","Category");
    sAuthor = iwrb.getLocalizedString("author","Author");
    sSource = iwrb.getLocalizedString("source","Source");
    sDaysShown = iwrb.getLocalizedString("visible_days","Number of days visible");
    sImage = iwrb.getLocalizedString("image","Image");
    sEditor = iwrb.getLocalizedString("news_editor","News Editor");
    sPublisFrom = iwrb.getLocalizedString("publish_from","Publish from");
    sPublisTo = iwrb.getLocalizedString("publish_to","Publish to");
    setAllMargins(0);
    setTitle(sEditor);
  }

  private void control(IWContext iwc)throws Exception{
    init();
    boolean doView = true;
    Locale currentLocale = iwc.getCurrentLocale(),chosenLocale;

    String sLocaleId = iwc.getParameter(prmLocale);
    String sCategoryId = iwc.getParameter(prmCategory);
    int iCategoryId = sCategoryId !=null?Integer.parseInt(sCategoryId):-1;
    String sAtt = null;

    // LocaleHandling
    int iLocaleId = -1;
    if(sLocaleId!= null){
      iLocaleId = Integer.parseInt(sLocaleId);
      chosenLocale = NewsFinder.getLocale(iLocaleId);
    }
    else{
      chosenLocale = currentLocale;
      iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);
    }

    if ( isAdmin ) {
      String sAction;
      // end of LocaleHandling

      // Text initialization
      String sNewsId = null,sAttribute = null;
      String sLocTextId = iwc.getParameter(prmLocalizedTextId);
      sAttribute = iwc.getParameter(prmAttribute);

      // News Id Request :
      if(iwc.getParameter(prmNwNewsId) != null){
        sNewsId = iwc.getParameter(prmNwNewsId);
      }
      // Delete Request :
      else if(iwc.getParameter(prmDelete)!=null){
        sNewsId = iwc.getParameter(prmDelete);
        confirmDelete(sNewsId,iObjInsId);
        doView = false;
      }
      // Object Instance Request :
      else if(iwc.getParameter(prmObjInstId)!= null){
        iObjInsId = Integer.parseInt(iwc.getParameter(prmObjInstId ) );
        doView = false;
        if(iObjInsId > 0)
          iCategoryId = NewsFinder.getObjectInstanceCategoryId(iObjInsId );
        addCategoryFields(NewsFinder.getNewsCategory(iCategoryId),iObjInsId  );
      }
      // end of News initialization

      // Form processing
      if(iwc.getParameter(prmFormProcess)!=null){
        if(iwc.getParameter(prmFormProcess).equals("Y"))
          processForm(iwc,sNewsId,sLocTextId, sCategoryId);
        else if(iwc.getParameter(prmFormProcess).equals("C"))
          processCategoryForm(iwc,sCategoryId,iObjInsId);

        doView = false;
      }
      if(doView)
        doViewNews(sNewsId,sAttribute,chosenLocale,iLocaleId,iCategoryId );
    }
    else {
      noAccess();
    }
  }

  // Form Processing :
  private void processForm(IWContext iwc,String sNewsId,String sLocTextId,String sCategory){
    // Save :
    if(iwc.getParameter(actSave)!=null || iwc.getParameter(actSave+".x")!=null ){
      saveNews(iwc,sNewsId,sLocTextId,sCategory);
    }
    // Delete :
    else if(iwc.getParameter( actDelete )!=null || iwc.getParameter(actDelete+".x")!=null){
      try {
        if(iwc.getParameter(modeDelete)!=null){
          int I = Integer.parseInt(iwc.getParameter(modeDelete));
          deleteNews(I);
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    // New:
     /** @todo make possible */
   /*else if(iwc.getParameter( actNew ) != null || iwc.getParameter(actNew+".x")!= null){
      sNewsId = null;
    }
    */
    // end of Form Actions
  }

  private void processCategoryForm(IWContext iwc,String sCategoryId,int iObjInsId){
    String sName = iwc.getParameter(prmCatName);
    String sDesc = iwc.getParameter(prmCatDesc);
    if(sName!=null){
      int iCatId = sCategoryId != null ? Integer.parseInt(sCategoryId):-1;
      NewsBusiness.saveNewsCategory(iCatId,sName,sDesc,iObjInsId);
    }
    setParentToReload();
    close();
  }

  private void doViewNews(String sNewsId,String sAttribute,Locale locale,int iLocaleId,int iCategoryId){
    ContentHelper contentHelper = null;
    NwNews news = null;
    if(sNewsId != null){
      int iNewsId = Integer.parseInt(sNewsId);
      news = NewsFinder.getNews(iNewsId);
      if(news != null && locale != null)
        contentHelper = ContentFinder.getContentHelper(news.getContentId(),locale);
    }

    addNewsFields(news,contentHelper,iLocaleId,iObjInsId,iCategoryId);

  }

  private void saveNews(IWContext iwc,String sNwNewsId,String sLocalizedTextId,String sCategoryId){

    String sHeadline = iwc.getParameter( prmHeadline );
    String sBody = iwc.getParameter(prmBody );
    String sImageId = iwc.getParameter(prmImageId);
    String sLocaleId = iwc.getParameter(prmLocale);
    String sUseImage = iwc.getParameter(prmUseImage);
    String sAuthor = iwc.getParameter(prmAuthor);
    String sSource = iwc.getParameter(prmSource);
    String sPubFrom = iwc.getParameter(prmPubFrom);
    String sPubTo = iwc.getParameter(prmPubTo);
    System.err.println("publish from" + sPubFrom);
    System.err.println("publish to" + sPubTo);
    if(sHeadline != null || sBody != null){
      int iNwNewsId = sNwNewsId!=null?Integer.parseInt(sNwNewsId): -1;
      int iLocalizedTextId = sLocalizedTextId != null ? Integer.parseInt(sLocalizedTextId): -1;
      int iLocaleId = sLocaleId != null ? Integer.parseInt(sLocaleId):-1;
      int iImageId = sImageId != null ? Integer.parseInt(sImageId):-1;
      int iCategoryId = sCategoryId !=null ? Integer.parseInt(sCategoryId):-1;
      boolean bUseImage = sUseImage!= null?true:false;
      idegaTimestamp today = idegaTimestamp.RightNow();
      idegaTimestamp pubFrom = sPubFrom!=null ? new idegaTimestamp(sPubFrom):today;
      today.addDays(defaultPublishDays);
      idegaTimestamp pubTo = sPubTo!=null ?new idegaTimestamp(sPubTo):today;
      Vector V = null;
      ICFile F = null;
      try {
        F = new ICFile(iImageId);
        V = new Vector(1);
        V.add(F);
      }
      catch (SQLException ex) {

      }

      System.err.println(pubFrom.toSQLString());
      System.err.println(pubTo.toString());
      NewsBusiness.saveNews(iNwNewsId,iLocalizedTextId,iCategoryId ,sHeadline,"",sAuthor,sSource,sBody,iLocaleId,iUserId,iObjInsId,pubFrom.getTimestamp(),pubTo.getTimestamp(),V);
    }
    setParentToReload();
    close();
  }

  private void deleteNews(int iNewsId ) {
    NewsBusiness.deleteNews(iNewsId);
    setParentToReload();
    close();
  }

  public String getColumnString(NewsCategoryAttribute[] attribs){
    String values = "";
    for (int i = 0 ; i < attribs.length ; i++) {
      values += NewsCategory.getNameColumnName()+"_id = '"+attribs[i].getNewsCategoryId()+"'" ;
      if( i!= (attribs.length-1) ) values += " OR ";
    }
    return values;
  }

  public Text getHeaderText(String s){
    Text textTemplate = new Text(s);
    textTemplate.setFontSize(Text.FONT_SIZE_7_HTML_1);
    textTemplate.setBold();
    textTemplate.setFontFace(Text.FONT_FACE_VERDANA);
    return textTemplate;
  }

  private void addCategoryFields(NewsCategory newsCategory,int iObjInst){
    boolean hasCategory = newsCategory !=null ? true:false;
    TextInput tiName = new TextInput(prmCatName);
    tiName.setLength(40);
    tiName.setMaxlength(255);

    TextArea taDesc = new TextArea(prmCatDesc,65,10);
    SubmitButton save = new SubmitButton(iwrb.getImage("save.gif"),actSave);
    if(hasCategory){
      if(newsCategory.getName()!=null)
        tiName.setContent(newsCategory.getName());
      if(newsCategory.getDescription()!=null)
        taDesc.setContent(newsCategory.getDescription());
      addHiddenInput(new HiddenInput(prmCategory ,String.valueOf(newsCategory.getID())));
    }

    String sName = iwrb.getLocalizedString("name","Name");
    String sDesc = iwrb.getLocalizedString("description","Description");
    addLeft(sName,tiName,true);
    addLeft(sDesc,taDesc,true);

    addSubmitButton(save);
    addHiddenInput( new HiddenInput (prmObjInstId,String.valueOf(iObjInst)));
    addHiddenInput( new HiddenInput (prmFormProcess,"C"));

  }

  private void addNewsFields(NwNews nwNews ,ContentHelper contentHelper, int iLocaleId,int iObjInsId,int iCategoryId){
    LocalizedText locText = null;
    boolean hasContent = ( contentHelper != null) ? true:false;
    if(hasContent)
      locText = contentHelper.getLocalizedText(TextFinder.getLocale(iLocaleId));
    boolean hasNwNews = ( nwNews != null ) ? true: false;
    boolean hasLocalizedText = ( locText != null ) ? true: false;

    TextInput tiHeadline = new TextInput(prmHeadline);
    tiHeadline.setLength(40);
    tiHeadline.setMaxlength(255);

    idegaTimestamp now = idegaTimestamp.RightNow();
    TimestampInput publishFrom = new TimestampInput(prmPubFrom,true);
      publishFrom.setTimestamp(now.getTimestamp());
    // add default publishing days:
    //now.addDays(defaultPublishDays);

    TimestampInput publishTo = new TimestampInput(prmPubTo,true);
      publishTo.setTimestamp(now.getTimestamp());

    List L = NewsFinder.listOfLocales();
    DropdownMenu LocaleDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(prmLocale);
    LocaleDrop.setToSubmit();
    LocaleDrop.setSelectedElement(Integer.toString(iLocaleId));

    TextArea taBody = new TextArea(prmBody,65,18);

    TextInput tiAuthor = new TextInput(prmAuthor);
    tiAuthor.setLength(22);
    tiAuthor.setMaxlength(255);

    TextInput tiSource = new TextInput(prmSource);
    tiAuthor.setLength(22);
    tiAuthor.setMaxlength(255);

    DropdownMenu drpDaysShown = counterDropdown(prmDaysshown, 1, 30);
    drpDaysShown.addMenuElementFirst("-1", iwrb.getLocalizedString("undetermined","Undetermined") );

    ImageInserter imageInsert = new ImageInserter();
    imageInsert.setImSessionImageName(prmImageId);
    imageInsert.setWindowClassToOpen(SimpleChooserWindow.class);
    Link propslink = null;

    SubmitButton save = new SubmitButton(iwrb.getImage("save.gif"),actSave);

    // Fill or not Fill
    if ( hasLocalizedText ) {
      if ( locText.getHeadline() != null ) {
        tiHeadline.setContent(locText.getHeadline());
      }
      if ( locText.getBody() != null ) {
        taBody.setContent(locText.getBody());
      }
      addHiddenInput(new HiddenInput(prmLocalizedTextId,String.valueOf(locText.getID())));
    }
    if( hasNwNews ){
      if("".equals(nwNews.getAuthor())&& eUser !=null)
        tiAuthor.setContent(eUser.getFirstName());
      else
        tiAuthor.setContent(nwNews.getAuthor());
      tiSource.setContent(nwNews.getSource());
      //drpCategories.setSelectedElement(String.valueOf(nwNews.getNewsCategoryId()));

      if ( hasContent ) {
        List files = contentHelper.getFiles();
        if(files != null){
          ICFile file1 = (ICFile) files.get(0);
          imageInsert.setImageId(file1.getID());
          Text properties = new Text("properties");
          propslink = com.idega.block.media.presentation.ImageAttributeSetter.getLink(properties,file1.getID(),imageAttributeKey);
        }
        Content content = contentHelper.getContent();
        if(content.getPublishFrom()!=null){
          publishFrom.setTimestamp(content.getPublishFrom());
        }
        if(content.getPublishTo()!=null){
          publishTo.setTimestamp(content.getPublishTo());
        }
      }
      addHiddenInput(new HiddenInput(prmNwNewsId,Integer.toString(nwNews.getID())));
      addHiddenInput(new HiddenInput(prmCategory ,String.valueOf(nwNews.getNewsCategoryId())));
    }
    else{
      if( eUser !=null){
        tiAuthor.setContent(eUser.getFirstName());
      }
      idegaTimestamp today = idegaTimestamp.RightNow();
      publishFrom.setTimestamp(today.getTimestamp());
      today.addDays(50);
      publishTo.setTimestamp(today.getTimestamp());
      addHiddenInput(new HiddenInput(prmCategory ,String.valueOf(iCategoryId)));
    }

    addLeft(sHeadline,tiHeadline,true);
    addLeft(sLocale, LocaleDrop,true);
    addLeft(sPublisFrom, publishFrom,true);
    addLeft(sPublisTo,publishTo,true);
    addLeft(sNews,taBody,true);
    addRight(sAuthor,tiAuthor,true);
    addRight(sSource,tiSource,true);
    addRight(sDaysShown,drpDaysShown,true);
    addRight(sImage,imageInsert,true);
    if(propslink != null)
      addRight("props",propslink,true);

    addSubmitButton(save);

    addHiddenInput( new HiddenInput (prmFormProcess,"Y"));
  }

  private void confirmDelete(String sNewsId,int iObjInsId ) throws IOException,SQLException {
    int iNewsId = Integer.parseInt(sNewsId);
    NwNews nwNews = NewsFinder.getNews(iNewsId);

    if ( nwNews != null ) {
      addLeft(iwrb.getLocalizedString("news_to_delete","News to delete"));
      addLeft(iwrb.getLocalizedString("confirm_delete","Are you sure?"));
      addSubmitButton(new SubmitButton(iwrb.getImage("delete.gif"),actDelete));
      addHiddenInput(new HiddenInput(modeDelete,String.valueOf(nwNews.getID())));
      addHiddenInput( new HiddenInput (prmFormProcess,"Y"));
    }
    else {
      addLeft(iwrb.getLocalizedString("not_exists","News already deleted or not available."));
      addSubmitButton(new CloseButton(iwrb.getImage("close.gif")));
    }
  }

  private void noAccess() throws IOException,SQLException {
    addLeft(iwrb.getLocalizedString("no_access","Login first!"));
    this.addSubmitButton(new CloseButton(iwrb.getLocalizedString("close","Closee")));
  }

  public DropdownMenu counterDropdown(String dropdownName, int countFrom, int countTo)
  {
    String from = Integer.toString(countFrom);
    DropdownMenu myDropdown = new DropdownMenu(dropdownName);

    for(; countFrom <= countTo; countFrom++){
      myDropdown.addMenuElement(Integer.toString(countFrom), Integer.toString(countFrom));
    }
    myDropdown.keepStatusOnAction();

    return myDropdown;
  }

  private DropdownMenu drpNewsCategories(String name,String valueIfEmpty,String displayIfEmpty){
    List L = NewsFinder.listOfNewsCategories();
    if(L != null){
      DropdownMenu drp = new DropdownMenu(L,name);
      return drp;
    }
    else{
      DropdownMenu drp = new DropdownMenu(name);
      drp.addDisabledMenuElement("","");
      return drp;
    }
  }

  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    isAdmin = iwc.hasEditPermission(this);
    eUser = com.idega.block.login.business.LoginBusiness.getUser(iwc);
    iUserId = eUser != null?eUser.getID():-1;
    isAdmin = true;
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    addTitle(iwrb.getLocalizedString("news_editor","News Editor"));
    control(iwc);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}

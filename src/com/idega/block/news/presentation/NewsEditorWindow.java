package com.idega.block.news.presentation;


import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.block.news.business.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.block.news.data.*;
import com.idega.block.text.data.*;
import com.idega.core.user.data.User;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.jmodule.image.presentation.ImageInserter;
import com.idega.data.*;
import com.idega.util.text.*;
import com.idega.util.text.TextSoap;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.presentation.IWAdminWindow;


public class NewsEditorWindow extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.news";
private String Error;
private boolean isAdmin=false;
private int iUserId = -1;
private User eUser = null;
private int iObjInsId = -1;

private static String prmHeadline = "nwep.headline";
private static String prmAuthor = "nwep.author";
private static String prmSource = "nwep.source";
private static String prmDaysshown = "nwep.daysshown";
private static String prmBody = "nwe.body";
public static String prmCategory = "nwep.category";
private static String prmLocale = "nwep.locale";
private static String prmLocalizedTextId = "nwep.loctextid";
public static String prmObjInstId = "nwep.icobjinstid";
public static String prmAttribute = "nwep.attribute";
private static String prmUseImage = "insertImage";//nwep.useimage
public  static String prmDelete = "nwep.txdeleteid";
private static String prmImageId = "nwep.imageid";
public static String prmNwNewsId = "nwep.nwnewsid";
private static String actDelete = "nwea.delete";
private static String actSave = "nwea.save";
private static String modeDelete = "nwem.delete";
private static String prmFormProcess = "nwe.formprocess";
private static String prmNewCategory = "nwep.newcategory";
private static String prmEditCategory = "nwep.editcategory";
private static String prmCatName= "nwep.categoryname";
private static String prmCatDesc = "nwep.categorydesc";



private String sEditor,sHeadline,sNews,sCategory,sAuthor,sSource,sDaysShown,sImage,sLocale;

private String attributeName = "union_id";
private int attributeId = 3;
private IWBundle iwb;
private IWResourceBundle iwrb;

  public NewsEditorWindow(){
    setWidth(570);
    setHeight(430);
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
    setAllMargins(0);
    setTitle(sEditor);
  }

  private void control(ModuleInfo modinfo)throws Exception{
    init();
    boolean doView = true;
    Locale currentLocale = modinfo.getCurrentLocale(),chosenLocale;

    String sLocaleId = modinfo.getParameter(prmLocale);
    String sCategoryId = modinfo.getParameter(prmCategory);
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
      String sLocTextId = modinfo.getParameter(prmLocalizedTextId);
      sAttribute = modinfo.getParameter(prmAttribute);

      // News Id Request :
      if(modinfo.getParameter(prmNwNewsId) != null){
        sNewsId = modinfo.getParameter(prmNwNewsId);
      }
      // Delete Request :
      else if(modinfo.getParameter(prmDelete)!=null){
        sNewsId = modinfo.getParameter(prmDelete);
        confirmDelete(sNewsId,iObjInsId);
        doView = false;
      }
      // Object Instance Request :
      else if(modinfo.getParameter(prmObjInstId)!= null){
        iObjInsId = Integer.parseInt(modinfo.getParameter(prmObjInstId ) );
        doView = false;
        if(iObjInsId > 0)
          iCategoryId = NewsFinder.getObjectInstanceCategoryId(iObjInsId );
        addCategoryFields(NewsFinder.getNewsCategory(iCategoryId),iObjInsId  );
      }
      // end of News initialization

      // Form processing
      if(modinfo.getParameter(prmFormProcess)!=null){
        if(modinfo.getParameter(prmFormProcess).equals("Y"))
          processForm(modinfo,sNewsId,sLocTextId, sCategoryId);
        else if(modinfo.getParameter(prmFormProcess).equals("C"))
          processCategoryForm(modinfo,sCategoryId,iObjInsId);

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
  private void processForm(ModuleInfo modinfo,String sNewsId,String sLocTextId,String sCategory){
    // Save :
    if(modinfo.getParameter(actSave)!=null || modinfo.getParameter(actSave+".x")!=null ){
      saveNews(modinfo,sNewsId,sLocTextId,sCategory);
    }
    // Delete :
    else if(modinfo.getParameter( actDelete )!=null || modinfo.getParameter(actDelete+".x")!=null){
      try {
        if(modinfo.getParameter(modeDelete)!=null){
          int I = Integer.parseInt(modinfo.getParameter(modeDelete));
          deleteNews(I);
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    // New:
     /** @todo make possible */
   /*else if(modinfo.getParameter( actNew ) != null || modinfo.getParameter(actNew+".x")!= null){
      sNewsId = null;
    }
    */
    // end of Form Actions
  }

  private void processCategoryForm(ModuleInfo modinfo,String sCategoryId,int iObjInsId){
    String sName = modinfo.getParameter(prmCatName);
    String sDesc = modinfo.getParameter(prmCatDesc);
    if(sName!=null){
      int iCatId = sCategoryId != null ? Integer.parseInt(sCategoryId):-1;
      NewsBusiness.saveNewsCategory(iCatId,sName,sDesc,iObjInsId);
    }
    setParentToReload();
    close();
  }

  private void doViewNews(String sNewsId,String sAttribute,Locale locale,int iLocaleId,int iCategoryId){
    NewsHelper newsHelper = null;
    if(sNewsId != null){
      int iNewsId = Integer.parseInt(sNewsId);
      if(locale != null)
        newsHelper = NewsFinder.getNewsHelper(iNewsId,locale);
    }
    LocalizedText LocTx = null;
    NwNews news = null;
    if(newsHelper != null){
      LocTx = newsHelper.getLocalizedText();
      news = newsHelper.getNwNews();
    }
    addNewsFields(LocTx,news,iLocaleId,iObjInsId,iCategoryId);
  }

  private void saveNews(ModuleInfo modinfo,String sNwNewsId,String sLocalizedTextId,String sCategoryId){

    String sHeadline = modinfo.getParameter( prmHeadline );
    String sBody = modinfo.getParameter(prmBody );
    String sImageId = modinfo.getParameter(prmImageId);
    String sLocaleId = modinfo.getParameter(prmLocale);
    String sUseImage = modinfo.getParameter(prmUseImage);
    String sAuthor = modinfo.getParameter(prmAuthor);
    String sSource = modinfo.getParameter(prmSource);
    if(sHeadline != null || sBody != null){
      int iNwNewsId = sNwNewsId!=null?Integer.parseInt(sNwNewsId): -1;
      int iLocalizedTextId = sLocalizedTextId != null ? Integer.parseInt(sLocalizedTextId): -1;
      int iLocaleId = sLocaleId != null ? Integer.parseInt(sLocaleId):-1;
      int iImageId = sImageId != null ? Integer.parseInt(sImageId):-1;
      int iCategoryId = sCategoryId !=null ? Integer.parseInt(sCategoryId):-1;
      boolean bUseImage = sUseImage!= null?true:false;

      NewsBusiness.saveNews(iNwNewsId,iLocalizedTextId,iCategoryId ,sHeadline,"",sAuthor,sSource,sBody,iImageId,bUseImage,iLocaleId,iUserId,iObjInsId);
    }
    setParentToReload();
    close();
  }

  private void deleteNews(int iNewsId ) {
    NewsBusiness.deleteNews(iNewsId);
    setParentToReload();
    close();
  }

  public void setConnectionAttributes(String attributeName, int attributeId) {
    this.attributeName = attributeName;
    this.attributeId = attributeId;
  }

  public void setConnectionAttributes(String attributeName, String attributeId) {
    this.attributeName = attributeName;
    this.attributeId = Integer.parseInt(attributeId);
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

  private void addNewsFields(LocalizedText locText,NwNews nwNews, int iLocaleId,int iObjInsId,int iCategoryId){
    boolean hasNwNews = ( nwNews != null ) ? true: false;
    boolean hasLocalizedText = ( locText != null ) ? true: false;

    TextInput tiHeadline = new TextInput(prmHeadline);
    tiHeadline.setLength(40);
    tiHeadline.setMaxlength(255);

    List L = NewsFinder.listOfLocales();
    DropdownMenu LocaleDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(prmLocale);
    LocaleDrop.setToSubmit();
    LocaleDrop.setSelectedElement(Integer.toString(iLocaleId));

    TextArea taBody = new TextArea(prmBody,65,18);

    //DropdownMenu drpCategories = drpNewsCategories(prmCategory,"-2",iwrb.getLocalizedString("no_categories","No Categories"));
    //drpCategories.addMenuElementFirst("-1", iwrb.getLocalizedString("choose_category","Choose Category") );

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
    imageInsert.setSelected(nwNews.getIncludeImage());
    imageInsert.setWindowClassToOpen(com.idega.jmodule.image.presentation.SimpleChooserWindow.class);

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
    // old table data
    else if(hasNwNews ){
      if ( nwNews.getHeadline() != null ) {
        tiHeadline.setContent(nwNews.getHeadline());
      }
      if ( nwNews.getText() != null ) {
        taBody.setContent(nwNews.getText());
      }

    }

    if( hasNwNews ){
      if("".equals(nwNews.getAuthor())&& eUser !=null)
        tiAuthor.setContent(eUser.getFirstName());
      else
        tiAuthor.setContent(nwNews.getAuthor());
      tiSource.setContent(nwNews.getSource());
      //drpCategories.setSelectedElement(String.valueOf(nwNews.getNewsCategoryId()));
      drpDaysShown.setSelectedElement(String.valueOf(nwNews.getDaysShown()));
      if(nwNews.getImageId() != -1){
        imageInsert.setImageId(nwNews.getImageId());
      }
      addHiddenInput(new HiddenInput(prmNwNewsId,Integer.toString(nwNews.getID())));
      addHiddenInput(new HiddenInput(prmCategory ,String.valueOf(nwNews.getNewsCategoryId())));
    }
    else if( eUser !=null){
        tiAuthor.setContent(eUser.getFirstName());
        addHiddenInput(new HiddenInput(prmCategory ,String.valueOf(iCategoryId)));
    }

    addLeft(sHeadline,tiHeadline,true);
    addLeft(sLocale, LocaleDrop,true);
    addLeft(sNews,taBody,true);
    //addRight(sCategory,drpCategories,true);
    addRight(sAuthor,tiAuthor,true);
    addRight(sSource,tiSource,true);
    addRight(sDaysShown,drpDaysShown,true);
    addRight(sImage,imageInsert,true);

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

  public void main(ModuleInfo modinfo) throws Exception {
    super.main(modinfo);
    isAdmin = AccessControl.hasEditPermission(this,modinfo);
    eUser = com.idega.block.login.business.LoginBusiness.getUser(modinfo);
    iUserId = eUser != null?eUser.getID():-1;
    isAdmin = true;
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);
    addTitle(iwrb.getLocalizedString("news_editor","News Editor"));
    control(modinfo);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}

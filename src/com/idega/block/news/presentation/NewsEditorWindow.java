package com.idega.block.news.presentation;


import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.news.business.NewsBusiness;
import com.idega.block.news.business.NewsFinder;
import com.idega.block.news.data.NewsCategoryAttribute;
import com.idega.block.news.data.NwNews;
import com.idega.block.text.business.ContentBusiness;
import com.idega.block.text.business.ContentFinder;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.data.Content;
import com.idega.block.text.data.LocalizedText;
import com.idega.core.file.data.ICFile;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.texteditor.TextEditor;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TimestampInput;
import com.idega.util.IWTimestamp;

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
private int SAVECATEGORY = 1,SAVENEWS = 2;

private static String YEARS_AHEAD_PROPERTY = "publish_to_years";

private static String prmHeadline = "nwep_headline";
private static String prmTeaser = "nwep_teaser";
private static String prmAuthor = "nwep_author";
private static String prmSource = "nwep_source";
private static String prmDaysshown = "nwep_daysshown";
private static String prmBody = "nwe_body";
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
private static String actClose = "nwea_close";
private static String modeDelete = "nwem_delete";
private static String prmFormProcess = "nwe_formprocess";
private static String prmNewCategory = "nwep_newcategory";
private static String prmEditCategory = "nwep_editcategory";
private static String prmDeleteFile = "nwep_deletefile";
	private static String prmSaveFile = "nwep_savefile";
private static String prmCatName= "nwep_categoryname";
private static String prmCatDesc = "nwep_categorydesc";
private static String prmPubFrom = "nwep_publishfrom";
private static String prmPubTo = "nwep_publishto";
private static String prmNewsDate = "nwep_newsdate";
private static String prmMoveToCat = "nwep_movtocat";
public static final  String imageAttributeKey = "newsimage";
private String sNewsId = null;
private int iCategoryId = -1;



private String sEditor,sHeadline,sTeaser,sNews,sNewsDate,sCategory,sAuthor,sSource,sDaysShown,sImage,sLocale,sPublisFrom,sPublisTo;

private int attributeId = 3;
private IWBundle iwb,core;
private IWResourceBundle iwrb;

  public NewsEditorWindow(){
    setWidth(570);
    setHeight(620);
    setResizable(true);
    setScrollbar(true);
    setUnMerged();
  }

  private void init(){
    sHeadline = iwrb.getLocalizedString("headline","Headline");
    sLocale =  iwrb.getLocalizedString("locale","Locale");
    sTeaser = iwrb.getLocalizedString("teaser","Teaser");
    sNews = iwrb.getLocalizedString("news","News");
    sCategory = iwrb.getLocalizedString("category","Category");
    sAuthor = iwrb.getLocalizedString("author","Author");
    sSource = iwrb.getLocalizedString("source","Source");
    sDaysShown = iwrb.getLocalizedString("visible_days","Number of days visible");
    sImage = iwrb.getLocalizedString("image","Image");
    sEditor = iwrb.getLocalizedString("news_editor","News Editor");
    sPublisFrom = iwrb.getLocalizedString("publish_from","Publish from");
    sPublisTo = iwrb.getLocalizedString("publish_to","Publish to");
    sNewsDate = iwrb.getLocalizedString("news_date","News date");
    setAllMargins(0);
    setTitle(sEditor);
  }

  private void control(IWContext iwc)throws Exception{
    init();
    //debugParameters(iwc);
    boolean doView = true;
    Locale currentLocale = iwc.getCurrentLocale();
    Locale chosenLocale;
    
    if(iwc.isParameterSet(actClose) || iwc.isParameterSet(actClose+".x")){
      setParentToReload();
      close();
    }
    else{

    String sLocaleId = iwc.getParameter(prmLocale);
    String sCategoryId = iwc.getParameter(prmCategory);
    iCategoryId = sCategoryId !=null?Integer.parseInt(sCategoryId):-1;
    int saveInfo = getSaveInfo(iwc);

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
      // end of LocaleHandling

      // Text initialization
      String sAttribute = null;
      String sLocTextId = iwc.getParameter(prmLocalizedTextId);
      String sObjInstId = iwc.getParameter(prmObjInstId);
      sAttribute = iwc.getParameter(prmAttribute);
      if(sObjInstId!=null)
        iObjInsId = Integer.parseInt(sObjInstId);

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
      else if(sObjInstId!=null){
        //doView = false;
        if(iObjInsId > 0 && saveInfo != SAVECATEGORY)
          iCategoryId = CategoryFinder.getInstance().getObjectInstanceCategoryId(iObjInsId );
      }
      //add("category id "+iCategoryId);
      //add(" instance id "+iObjInsId);
      // end of News initialization

      // Form processing
      if(saveInfo == SAVENEWS)
        processForm(iwc,sNewsId,sLocTextId, sCategoryId);
      else if(saveInfo == SAVECATEGORY)
        processCategoryForm(iwc,sCategoryId,iObjInsId);

      /* old stuff
      if(iwc.isParameterSet(prmObjInstId)){
        addCategoryFields(CategoryFinder.getInstance().getCategory(iCategoryId),iObjInsId  );
      }
      */
      //doView = false;

      if(doView)
        doViewNews(sNewsId,sAttribute,chosenLocale,iLocaleId,iCategoryId );
    }
    else {
      noAccess();
    }
    }
  }

  private int getSaveInfo(IWContext iwc){
    if(iwc.getParameter(prmFormProcess)!=null){
      if(iwc.getParameter(prmFormProcess).equals("Y"))
        return SAVENEWS;
      else if(iwc.getParameter(prmFormProcess).equals("C"))
        return SAVECATEGORY;
  //doView = false;
  }
    return 0;
  }

  private Parameter getParameterSaveNews(){
    return new Parameter(prmFormProcess,"Y");
  }

  private Parameter getParameterSaveCategory(){
    return new Parameter(prmFormProcess,"C");
  }


  // Form Processing :
  private void processForm(IWContext iwc,String sNewsId,String sLocTextId,String sCategory){
    // Save :
    if(iwc.getParameter(actSave)!=null || iwc.getParameter(actSave+".x")!=null ){
      iwc.getIWMainApplication().getIWCacheManager().invalidateCache(NewsReader.CACHE_KEY);
      saveNews(iwc,sNewsId,sLocTextId,sCategory);
    }
    // Delete :
    else if(iwc.getParameter( actDelete )!=null || iwc.getParameter(actDelete+".x")!=null){
      iwc.getIWMainApplication().getIWCacheManager().invalidateCache(NewsReader.CACHE_KEY);
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
    else if(iwc.getParameter(prmDeleteFile)!=null){
      if(sNewsId!=null){
        String sFileId = iwc.getParameter(prmDeleteFile);
        deleteFile(sNewsId,sFileId);
      }
    }
    else if(iwc.getParameter(prmSaveFile)!= null || iwc.getParameter(prmSaveFile+".x")!=null){
      if(sNewsId!=null){
        String sFileId = iwc.getParameter(prmImageId);
        saveFile(sNewsId,sFileId);
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
    String sMoveCat = iwc.getParameter(prmMoveToCat);
    int iMoveCat = sMoveCat !=null ? Integer.parseInt(sMoveCat):-1;
    int iCatId = sCategoryId != null ? Integer.parseInt(sCategoryId):-1;
    if(iwc.getParameter(actSave)!=null || iwc.getParameter(actSave+".x")!=null ){
      if(sName!=null){
        //System.err.println("saving CATId = "+iCatId +" ObjInstId = "+iObjInsId);
        try{
        NewsBusiness.saveNewsCategory(iCatId,sName,sDesc,iObjInsId);
        if(iMoveCat > 0){
          NewsBusiness.moveNewsBetweenCategories(iCatId,iMoveCat);
        }
        }catch(RemoteException ex){ex.printStackTrace();}
      }
    }
    else if(iwc.getParameter(actDelete)!=null || iwc.getParameter(actDelete+".x")!=null ){
      //System.err.println("deleteing CATId = "+iCatId +" ObjInstId = "+iObjInsId);
      NewsBusiness.deleteNewsCategory(iCatId);
    }
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
    String sTeaser = iwc.getParameter( prmTeaser);
    String sBody = iwc.getParameter(prmBody );
    String sImageId = iwc.getParameter(prmImageId);
    String sLocaleId = iwc.getParameter(prmLocale);
    String sAuthor = iwc.getParameter(prmAuthor);
    String sSource = iwc.getParameter(prmSource);
    String sPubFrom = iwc.getParameter(prmPubFrom);
    String sPubTo = iwc.getParameter(prmPubTo);
    String sNewsDate = iwc.getParameter(prmNewsDate);
    //System.err.println("publish from" + sPubFrom);
    //System.err.println("publish to" + sPubTo);
    if(sHeadline != null || sBody != null){
      int iNwNewsId = sNwNewsId!=null?Integer.parseInt(sNwNewsId): -1;
      int iLocalizedTextId = sLocalizedTextId != null ? Integer.parseInt(sLocalizedTextId): -1;
      int iLocaleId = sLocaleId != null ? Integer.parseInt(sLocaleId):-1;
      int iImageId = sImageId != null ? Integer.parseInt(sImageId):-1;
      int iCategoryId = sCategoryId !=null ? Integer.parseInt(sCategoryId):-1;
      IWTimestamp today = IWTimestamp.RightNow();
      IWTimestamp pubFrom = sPubFrom!=null ? new IWTimestamp(sPubFrom):today;
      Timestamp newsDate = sNewsDate != null ? new IWTimestamp(sNewsDate).getTimestamp() : null;
      today.addDays(defaultPublishDays);
      IWTimestamp pubTo = sPubTo!=null ?new IWTimestamp(sPubTo):today;
      Vector V = null;
      ICFile F = null;
      if(iImageId > 0){
        try {
          /** @todo  use finder */
          F = ((com.idega.core.file.data.ICFileHome)com.idega.data.IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(iImageId));
          V = new Vector(1);
          V.add(F);
        }
        catch (Exception ex) {
			ex.printStackTrace();
        }
      }

      //System.err.println(pubFrom.toSQLString());
      //System.err.println(pubTo.toString());
      NwNews news = NewsBusiness.saveNews(iNwNewsId,iLocalizedTextId,iCategoryId ,sHeadline,sTeaser,sAuthor,sSource,sBody,iLocaleId,iUserId,iObjInsId,pubFrom.getTimestamp(),pubTo.getTimestamp(),V, newsDate);
      if(news!=null)
        sNewsId = String.valueOf(news.getID());
    }
  }

	private void saveFile(String sNewsId,String sFileId){
		NwNews nw = NewsFinder.getNews(Integer.parseInt(sNewsId));
	  ContentBusiness.addFileToContent(nw.getContentId(),Integer.parseInt(sFileId));
	}

	private void deleteFile(String sNewsId,String sFileId){
	  NwNews nw = NewsFinder.getNews(Integer.parseInt(sNewsId));
	  ContentBusiness.removeFileFromContent(nw.getContentId(),Integer.parseInt(sFileId));
	}


  private void deleteNews(int iNewsId ) {
    NewsBusiness.deleteNews(iNewsId);
    setParentToReload();
    close();
  }

  public String getColumnString(NewsCategoryAttribute[] attribs){
    String values = "";
    for (int i = 0 ; i < attribs.length ; i++) {
      values += com.idega.block.news.data.NewsCategoryBMPBean.getColumnName()+"_id = '"+attribs[i].getNewsCategoryId()+"'" ;
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

  private void addCategoryFields(ICCategory newsCategory,int iObjInst){

    String sCategory= iwrb.getLocalizedString("category","Category");
    String sName = iwrb.getLocalizedString("name","Name");
    String sDesc = iwrb.getLocalizedString("description","Description");
    String sMoveCat = iwrb.getLocalizedString("movenews","Move news to");

    List L = NewsFinder.listOfValidNewsCategories();
    DropdownMenu catDrop = new DropdownMenu(L,prmCategory);
    catDrop.addMenuElementFirst("-1",sCategory);
    catDrop.setToSubmit();
    DropdownMenu MoveCatDrop = new DropdownMenu(L,prmMoveToCat);
    MoveCatDrop.addMenuElementFirst("-1",sCategory);

    Link newLink = new Link(iwb.getImage("/shared/create.gif"));
    newLink.addParameter(prmCategory,-1);
    newLink.addParameter(prmObjInstId,iObjInst);
    newLink.addParameter(prmFormProcess,"C");

    boolean hasCategory = newsCategory !=null ? true:false;
    TextInput tiName = new TextInput(prmCatName);
    tiName.setLength(40);
    tiName.setMaxlength(255);


    Table catTable = new Table(5,1);
    catTable.setCellpadding(0);
    catTable.setCellspacing(0);
    setStyle(catDrop);
    catTable.add(catDrop,1,1);
    catTable.add(newLink,3,1);
    catTable.setWidth(2,1,"20");
    catTable.setWidth(4,1,"20");

    TextArea taDesc = new TextArea(prmCatDesc,65,5);
    if(hasCategory){
      int id = newsCategory.getID();
      catDrop.setSelectedElement(String.valueOf(newsCategory.getID()));
      if(newsCategory.getName()!=null)
        tiName.setContent(newsCategory.getName());
      if(newsCategory.getDescription()!=null)
        taDesc.setContent(newsCategory.getDescription());
      addHiddenInput(new HiddenInput(prmCategory ,String.valueOf(id)));

      int iNewsCount = NewsFinder.countNewsInCategory(id);
      int iUnPublishedCount = NewsFinder.countNewsInCategory(id,NewsFinder.UNPUBLISHED);
      int iPublishingCount = NewsFinder.countNewsInCategory(id,NewsFinder.PUBLISHISING);
      int iPublishedCount = NewsFinder.countNewsInCategory(id,NewsFinder.PUBLISHED);

      String sNewsCount = iwrb.getLocalizedString("newscount","News count");
      String sUnPublishedCount = iwrb.getLocalizedString("unpublished","Unpublished");
      String sPublishingCount = iwrb.getLocalizedString("publishing","In publish");
      String sPublishedCount = iwrb.getLocalizedString("published","Published");

      Table table = new Table(3,4);
      table.setCellpadding(2);
      table.setCellspacing(1);
      table.setWidth(2,"10");
      String colon = " : ";
      table.add(formatText(sNewsCount+colon),1,1);
      table.add(String.valueOf(iNewsCount),3,1);
      table.add(formatText(sUnPublishedCount+colon),1,2);
      table.add(String.valueOf(iUnPublishedCount),3,2);
      table.add(formatText(sPublishingCount+colon),1,3);
      table.add(String.valueOf(iPublishingCount),3,3);
      table.add(formatText(sPublishedCount+colon),1,4);
      table.add(String.valueOf(iPublishedCount),3,4);

      String sInfo = iwrb.getLocalizedString("info","Info");
      addRight(sInfo,table,false,false);

      if(iNewsCount == 0){
      Link deleteLink = new Link(iwb.getImage("/shared/delete.gif"));
      deleteLink.addParameter(actDelete,"true");
      deleteLink.addParameter(prmCategory,newsCategory.getID());
      deleteLink.addParameter(prmObjInstId,iObjInst);
      deleteLink.addParameter(prmFormProcess,"C");

      catTable.add(deleteLink,5,1);
      }

    }

		addLeft(sCategory,catTable,true,false);
    addLeft(sName,tiName,true);
    addLeft(sDesc,taDesc,true);
		if(hasCategory)
		  addLeft(sMoveCat,MoveCatDrop,true);

		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),actSave);
		SubmitButton close = new SubmitButton(iwrb.getLocalizedImageButton("close","Close"),actClose);
    addSubmitButton(save);
		addSubmitButton(close);
    addHiddenInput( new HiddenInput (prmObjInstId,String.valueOf(iObjInst)));
    addHiddenInput( new HiddenInput (prmFormProcess,"C"));

  }

  private void addNewsFields(NwNews nwNews ,ContentHelper contentHelper, int iLocaleId,int iObjInsId,int iCategoryId){
    LocalizedText locText = null;
    boolean hasContent = ( contentHelper != null) ? true:false;
    if(hasContent)
      locText = contentHelper.getLocalizedText(ICLocaleBusiness.getLocaleReturnIcelandicLocaleIfNotFound(iLocaleId));
    boolean hasNwNews = ( nwNews != null ) ? true: false;
    boolean hasLocalizedText = ( locText != null ) ? true: false;

    TextInput tiHeadline = new TextInput(prmHeadline);
    tiHeadline.setLength(40);
    tiHeadline.setMaxlength(255);

    IWTimestamp now = IWTimestamp.RightNow();
    TimestampInput publishFrom = new TimestampInput(prmPubFrom,true);
      publishFrom.setTimestamp(now.getTimestamp());

		TimestampInput newsDate = new TimestampInput(prmNewsDate,true);
			newsDate.setTimestamp(now.getTimestamp());
			newsDate.setYearRange(now.getYear()-4,now.getYear()+2);

		// add default publishing days:
		int addYears = 0;
		try {
			addYears = Integer.parseInt(iwb.getProperty(YEARS_AHEAD_PROPERTY, "0"));
		}
		catch (NullPointerException ne) {
			addYears = 0;
		}
		catch (NumberFormatException nfe) {
			addYears = 0;
		}
		
		now.addYears(addYears);
    TimestampInput publishTo = new TimestampInput(prmPubTo,true);
      publishTo.setTimestamp(now.getTimestamp());
      
    DropdownMenu LocaleDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(prmLocale);
    LocaleDrop.setToSubmit();
    LocaleDrop.setSelectedElement(Integer.toString(iLocaleId));

    //TextArea taBody = new TextArea(prmBody,65,18);
    TextEditor taBody = new TextEditor();
    taBody.setInputName(prmBody);

    TextArea taTeaser = new TextArea(prmTeaser,65,2);

    List cats = CategoryFinder.getInstance().listOfCategoryForObjectInstanceId(iObjInsId);
    DropdownMenu catDrop = new DropdownMenu(cats,prmCategory);
    //catDrop.addMenuElementFirst("-1",sCategory);


    TextInput tiAuthor = new TextInput(prmAuthor);
    tiAuthor.setLength(22);
    tiAuthor.setMaxlength(255);

    TextInput tiSource = new TextInput(prmSource);
    tiAuthor.setLength(22);
    tiAuthor.setMaxlength(255);

    //DropdownMenu drpDaysShown = counterDropdown(prmDaysshown, 1, 30);
    //drpDaysShown.addMenuElementFirst("-1", iwrb.getLocalizedString("undetermined","Undetermined") );
/*
    ImageInserter imageInsert = new ImageInserter();
    imageInsert.setImSessionImageName(prmImageId);
    Link propslink = null;
*/

    // Fill or not Fill
    if ( hasLocalizedText ) {
      if ( locText.getHeadline() != null ) {
        tiHeadline.setContent(locText.getHeadline());
      }
      if ( locText.getTitle() != null ) {
        taTeaser.setContent(locText.getTitle());
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
				/*
        List files = contentHelper.getFiles();
        if(files != null){
          ICFile file1 = (ICFile) files.get(0);
          imageInsert.setImageId(file1.getID());
          Text properties = new Text("properties");
          propslink = com.idega.block.media.presentation.ImageAttributeSetter.getLink(properties,file1.getID(),imageAttributeKey);
        }
				*/
        Content content = contentHelper.getContent();
        if(content.getPublishFrom()!=null){
          publishFrom.setTimestamp(content.getPublishFrom());
        }
        if(content.getPublishTo()!=null){
          publishTo.setTimestamp(content.getPublishTo());
        }
        if(content.getLastUpdated()!=null){
        	newsDate.setTimestamp(content.getLastUpdated());
        }
      }
      catDrop.setSelectedElement(String.valueOf(nwNews.getNewsCategoryId()));
      addHiddenInput(new HiddenInput(prmNwNewsId,Integer.toString(nwNews.getID())));
      //addHiddenInput(new HiddenInput(prmCategory ,String.valueOf(nwNews.getNewsCategoryId())));
    }
    else{
      if( eUser !=null){
        tiAuthor.setContent(eUser.getFirstName());
      }
      IWTimestamp today = IWTimestamp.RightNow();
      publishFrom.setTimestamp(today.getTimestamp());

			if (addYears > 0)
				today.addYears(addYears);
			else
      	today.addDays(defaultPublishDays);
      publishTo.setTimestamp(today.getTimestamp());
      addHiddenInput(new HiddenInput(prmCategory ,String.valueOf(iCategoryId)));
    }
      addHiddenInput(new HiddenInput(prmObjInstId ,String.valueOf(iObjInsId)));

      SubmitButton addButton = new SubmitButton(core.getImage("/shared/create.gif","Add to news"),prmSaveFile);
      //SubmitButton leftButton = new SubmitButton(core.getImage("/shared/frew.gif","Insert image"),prmSaveFile);
      ImageInserter imageInsert = new ImageInserter();
      imageInsert.setImSessionImageName(prmImageId);
      imageInsert.setUseBoxParameterName(prmUseImage);
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
      if(files != null && !files.isEmpty()){
      imageTable.mergeCells(1,row,3,row);
      imageTable.add( formatText(iwrb.getLocalizedString("newsimages","News images :")),1,row++);
      ICFile file1 = (ICFile) files.get(0);
      imageInsert.setImageId(((Integer)file1.getPrimaryKey()).intValue());

      Iterator I = files.iterator();
      while(I.hasNext()){
        try {

          ICFile f = (ICFile) I.next();
          Image immi = new Image(((Integer)f.getPrimaryKey()).intValue());
          immi.setMaxImageWidth(50);

          imageTable.add(immi,1,row);
          //Link edit = new Link(iwb.getImage("/shared/edit.gif"));
          Link edit = com.idega.block.image.presentation.ImageAttributeSetter.getLink(iwb.getImage("/shared/edit.gif"),((Integer)file1.getPrimaryKey()).intValue(),imageAttributeKey);
          Link delete = new Link(core.getImage("/shared/delete.gif"));
          delete.addParameter(prmDeleteFile,f.getPrimaryKey().toString());
          delete.addParameter(prmNwNewsId,nwNews.getID());
          delete.addParameter(getParameterSaveNews());
          imageTable.add(edit,2,row);
          imageTable.add(delete,3,row);
          row++;
        }
        catch (Exception ex) {

        }
        }
      }
    }

    addLeft(sHeadline,tiHeadline,true);
    addLeft(sLocale, LocaleDrop,true);
    addLeft(sTeaser,taTeaser,true);
    addLeft(sNews,taBody,true);
    addLeft(sNewsDate,newsDate,true);
    addLeft(sPublisFrom, publishFrom,true);
    addLeft(sPublisTo,publishTo,true);

    addRight(sCategory,catDrop,true);
    addRight(sAuthor,tiAuthor,true);
    addRight(sSource,tiSource,true);
    //addRight(iwrb.getLocalizedString("image","Image"),imageInsert,true);
    //if(addButton!=null){
    //addRight("",addButton,true,false);
    //}
    addRight(iwrb.getLocalizedString("images","Images"),imageTable,true,false);

    /*
    addRight(sImage,imageInsert,true);
    if(propslink != null)
    addRight("props",propslink,true);
    */

    SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),actSave);
    SubmitButton close = new SubmitButton(iwrb.getLocalizedImageButton("close","Close"),actClose);
    addSubmitButton(save);
    addSubmitButton(close);

    addHiddenInput( new HiddenInput (prmFormProcess,"Y"));
  }

  private void deleteCat(int iCatId){

  }

  private void confirmDelete(String sNewsId,int iObjInsId ) throws IOException,SQLException {
    int iNewsId = Integer.parseInt(sNewsId);
    NwNews nwNews = NewsFinder.getNews(iNewsId);

    if ( nwNews != null ) {
      addLeft(iwrb.getLocalizedString("news_to_delete","News to delete"));
      addLeft(iwrb.getLocalizedString("confirm_delete","Are you sure?"));

      //addSubmitButton(new SubmitButton(iwrb.getImage("delete.gif"),actDelete));
      addSubmitButton(new SubmitButton(iwrb.getLocalizedImageButton("delete","Delete"),actDelete));

      addHiddenInput(new HiddenInput(modeDelete,String.valueOf(nwNews.getID())));
      addHiddenInput( new HiddenInput (prmFormProcess,"Y"));
    }
    else {
      addLeft(iwrb.getLocalizedString("not_exists","News already deleted or not available."));
      //addSubmitButton(new CloseButton(iwrb.getImage("close.gif")));
      addSubmitButton(new CloseButton());
    }
  }

  private void noAccess() throws IOException,SQLException {
    addLeft(iwrb.getLocalizedString("no_access","Login first!"));
    this.addSubmitButton(new CloseButton());
  }

  public DropdownMenu counterDropdown(String dropdownName, int countFrom, int countTo)
  {
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

    isAdmin = true;
    eUser = com.idega.core.accesscontrol.business.LoginBusinessBean.getUser(iwc);
    iUserId = eUser != null?eUser.getID():-1;
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
		core = iwc.getIWMainApplication().getBundle(NewsReader.IW_CORE_BUNDLE_IDENTIFIER);
    addTitle(iwrb.getLocalizedString("news_editor","News Editor"));
    control(iwc);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}

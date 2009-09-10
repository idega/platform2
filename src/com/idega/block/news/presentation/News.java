package com.idega.block.news.presentation;



import com.idega.idegaweb.block.presentation.Builderaware;





/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved

 * Company:      idega

  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>

 * @version 1.1

 */



public class News extends NewsReader implements Builderaware{

/*

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.news";

  private boolean isAdmin=false;

  private int iCategoryId = -1;

  private String attributeName = null;

  private int attributeId = -1;

  private User eUser = null;



  private boolean showNewsCollectionButton=true;

  private int categoryId = 0;





  private Table outerTable = new Table(1,1);



  private int numberOfLetters = 273;

  private int numberOfDisplayedNews = 5;

  private int numberOfExpandedNews = 3;

  private int iSpaceBetween = 1;

  private boolean backbutton = false;

  private boolean showAll = false;

  private boolean showImages = true;

  private boolean showOnlyDates = false;

  private boolean headlineAsLink = false;

  private boolean showHeadlineImage = false;

  private boolean showMoreButton = false;

  private boolean alignWithHeadline = false;

  private boolean limitNumberOfNews = false;

  private boolean enableDelete=true;

  private String date = null;

  private String newsReaderURL;

  private String newsCollectionURL;

  private String selectFrom = "select nw_news.* from nw_news where ";

  private String orderBy = " order by news_date DESC";

  private String sNewsCategoryId = "nw_news_cat_id ='";

  private String sNewsEditorUrl ="/news/editor.jsp";

  private String headlineImageURL = "/pics/jmodules/news/nanar2.gif";



  private static String prmDelete = "nwr.delete";

  private static String prmEdit = "nwr.edit";

  private static String prmNew = "nwr.new";

  private static String prmMore = "nwr.more";



  public static String prmNewsCategoryId = "nwr.newscategoryid";

  private boolean newobjinst = false;

  private Hashtable objectsBetween = null;

  private String sObjectAlign = "center";



  private Text textProxy = new Text();

  private Text headlineProxy  = new Text();

  private Text informationProxy  = new Text();



  private String outerTableWidth = "100%";



  private int textSize = 2;



  private IWBundle iwb;

  private IWResourceBundle iwrb ;



  public static final int SINGLE_FILE_LAYOUT = 1;

  public static final int NEWS_SITE_LAYOUT = 2;

  public static final int NEWS_PAPER_LAYOUT = 3;

  private int iLayout =1;

*/

  public News(){

    super();/*

    init();

    showAll = true;

    */

  }



  public News(int iCategoryId){

    super(iCategoryId );

    /*

    this();

    this.iCategoryId=iCategoryId;

    this.showAll = false;

    */

  }

/*

  private void init(){

    headlineProxy.setBold();

    informationProxy.setFontColor("#666666");

    textProxy.setFontSize(1);

    informationProxy.setFontSize(1);



  }



  private void checkCategories(){



  }



  private void control(IWContext iwc){

    Locale locale = iwc.getCurrentLocale();

    String sNewsId = iwc.getParameter(prmMore);

    NewsCategory newsCategory = null;



    if(iCategoryId <= 0){

      String sCategoryId = iwc.getParameter(prmNewsCategoryId );

      if(sCategoryId != null)

        iCategoryId = Integer.parseInt(sCategoryId);

      else if(getICObjectInstanceID() > 0){

        iCategoryId = NewsFinder.getObjectInstanceCategoryId(getICObjectInstanceID());

        if(iCategoryId <= 0 ){

          newobjinst = true;

        }

      }

    }

    Table T = new Table(1,1);

    T.setCellpadding(0);

    T.setCellpadding(0);

    T.setWidth( "100%");

    if(isAdmin){

      T.add(getAdminPart(iCategoryId,false,newobjinst),1,1);

    }

    if(iCategoryId >0){

      newsCategory = NewsFinder.getNewsCategory(iCategoryId);

      if(newsCategory != null){

        if(sNewsId != null){

          int id = Integer.parseInt(sNewsId);

          NewsHelper nh = NewsFinder.getNewsHelper(id);

          T.add(getNewsTable(nh,newsCategory,locale,true),1,1);

        }

        else

          T.add(publishNews(iwc,newsCategory,locale),1,1);

      }

    }

    else{

      T.add(new Text(iwrb.getLocalizedString("no_news_category","No news category")));

      showCategoryMaker();

    }

    super.add(T);

  }



  public PresentationObject getAdminPart(int iCategoryId,boolean enableDelete,boolean newObjInst){

    Table T = new Table(3,1);

    T.setCellpadding(2);

    T.setCellspacing(2);

    T.setBorder(0);



    if(iCategoryId > 0){

      Link ne = new Link(iwrb.getImage("newseditor.gif"));

      ne.setWindowToOpen(NewsEditorWindow.class,this.getICObjectInstanceID());

      ne.addParameter(NewsEditorWindow.prmCategory,iCategoryId);

      T.add(ne,1,1);



      Link change = new Link(iwrb.getImage("change.gif"));

      change.setWindowToOpen(NewsEditorWindow.class,this.getICObjectInstanceID());

      change.addParameter(NewsEditorWindow.prmCategory,iCategoryId);

      change.addParameter(NewsEditorWindow.prmObjInstId,getICObjectInstanceID());

      T.add(change,2,1);



      if ( enableDelete ) {

        Link delete = new Link(iwrb.getImage("delete.gif"));

        delete.setWindowToOpen(NewsEditorWindow.class,this.getICObjectInstanceID());

        delete.addParameter(NewsEditorWindow.prmDelete,iCategoryId);

        T.add(delete,3,1);

      }

    }

    if(newObjInst){

      Link newLink = new Link(iwrb.getImage("new.gif"));

      newLink.setWindowToOpen(NewsEditorWindow.class,this.getICObjectInstanceID());

      if(newObjInst)

        newLink.addParameter(NewsEditorWindow.prmObjInstId,getICObjectInstanceID());



      T.add(newLink,2,1);

    }



    T.setWidth("100%");

    return T;



  }



  private PresentationObject publishNews(IWContext iwc ,NewsCategory newsCategory,Locale locale){

    List L = NewsFinder.listOfNewsHelpersInCategory(newsCategory.getID(),numberOfDisplayedNews,locale );

    NewsTable T = new NewsTable(NewsTable.NEWS_SITE_LAYOUT );

    boolean useDividedTable = iLayout == NEWS_SITE_LAYOUT ? true:false;

    if(L!=null){

      int len = L.size();

      Integer I;

      NewsHelper newsHelper;

      for (int i = 0; i < len; i++) {

        newsHelper = (NewsHelper) L.get(i);

        I = new Integer(i);

        if(objectsBetween != null && objectsBetween.containsKey(I)){

          Table t = new Table(1,1);

          t.setCellpadding(4);

          t.add((PresentationObject)objectsBetween.get(I));

          T.add(t,sObjectAlign );

          objectsBetween.remove(I);

        }

        T.add(getNewsTable(newsHelper,newsCategory,locale ,false),useDividedTable,"left");

      }

      if(objectsBetween != null && objectsBetween.size() > 0){

        Vector V = new Vector(objectsBetween.values());

        Collections.reverse(V);

        Iterator iter = V.iterator();

        while(iter.hasNext()){

          T.add((PresentationObject)iter.next(),sObjectAlign );

        }

      }

    }

    else{

      T.add(new Text(iwrb.getLocalizedString("no_news","No News")));

    }

   return(T);

  }



  // Make a table around each news

  private PresentationObject getNewsTable(NewsHelper newsHelper,NewsCategory newsCategory, Locale locale,boolean showAll){

    Table T = new Table(1,4);

    T.setCellpadding(0);

    T.setCellspacing(0);

    T.setBorder(0);

    T.setWidth("100%");



    NwNews news = newsHelper.getNwNews();

    LocalizedText locText = newsHelper.getLocalizedText(locale);

    Text newsInfo = getInfoText(news,newsCategory.getName(),locale,showOnlyDates);



    String sNewsBody = "";

    String sHeadline = "";



    if(locText!=null){

      sHeadline = locText.getHeadline();

      sNewsBody =  locText.getBody();

    }

    else{

      if(news.getHeadline()!=null){

        sHeadline = news.getHeadline();

      }

      if(news.getText()!=null){

        sNewsBody = NewsFormatter.formatNews(news.getText(),"2");

      }

    }



    // shortening newstext

    if(!showAll && sNewsBody.length() >= numberOfLetters){

      sNewsBody=sNewsBody.substring(0,numberOfLetters)+"...";

    }



    Text headLine = new Text(sHeadline);

    Text newsBody = new Text(sNewsBody);



    if( showAll ) {

        T.add(new BackButton(iwrb.getImage("back.gif")), 1, 4);

    }



    newsInfo = setInformationAttributes(newsInfo);

    headLine = setHeadlineAttributes(headLine);

    newsBody = setTextAttributes(newsBody);

    T.add(newsInfo,1,1);

    if (news.getImageId()!= -1 && showImages && news.getIncludeImage()){

      try{

      Table imageTable = new Table(1, 2);

      Image newsImage = new Image(news.getImageId());

      newsImage.setAlignment("right");

      //imageTable.setAlignment("right");

      //imageTable.setVerticalAlignment("top");

      //imageTable.add(newsImage, 1, 1);

      T.add(newsImage,1,3);

      }

      catch(SQLException ex){

        ex.printStackTrace();

      }

    }

    T.add(newsBody,1,3);



    //  add news

     if(!showAll && showMoreButton){

      Link moreLink = new Link(iwrb.getImage("more.gif"));

      moreLink.addParameter(prmMore,news.getID());

      T.add(moreLink, 1, 4);

    }



    if ( alignWithHeadline && headlineImageURL!=null){

      T.add(new Image(headlineImageURL), 1, 2);

    }



    if ( headlineAsLink ) {

      Link headlineLink = new Link(headLine);

      headlineLink.addParameter(prmMore,news.getID());

      T.add(headlineLink, 1, 2);

    }

    else {

      T.add(headLine, 1, 2);

    }







    if(isAdmin){

      T.add(getNewsAdminPart(news),1,4);

    }

    return T;

  }



  private PresentationObject getNewsAdminPart(NwNews news){

    Table links = new Table(2,1);

      Link newsEdit = new Link(iwrb.getImage("change.gif"));

      newsEdit.setWindowToOpen(NewsEditorWindow.class,this.getICObjectInstanceID());

      newsEdit.addParameter(NewsEditorWindow.prmNwNewsId,news.getID());



      Link newsDelete = new Link(iwrb.getImage("delete.gif"));

      newsDelete.setWindowToOpen(NewsEditorWindow.class,this.getICObjectInstanceID());

      newsDelete.addParameter(NewsEditorWindow.prmDelete,news.getID());



      links.setAlignment(1,1,"left");

      links.setAlignment(2,1,"right");

      links.add(newsEdit,2,1);

      links.add(newsDelete,1,1);

    return links;

  }



  private Text getInfoText(NwNews nwNews,String sCategoryName,Locale locale, boolean ifUseOnlyDates){

    return new Text(NewsFormatter.getInfoText(nwNews,sCategoryName,locale,ifUseOnlyDates) );

  }



  private void showCategoryMaker(){



  }



  public void main(IWContext iwc)throws Exception{

    try {

      //isAdmin = AccessControl.isAdmin(iwc);

      /** @todo  *//*

      isAdmin = iwc.getAccessController().hasEditPermission(this,iwc);

    }

    catch (SQLException ex) {

      isAdmin = false;

    }



    iwb = getBundle(iwc);

    iwrb = getResourceBundle(iwc);

    control(iwc);

  }



  public boolean deleteBlock(int instanceid){

    return NewsBusiness.disconnectBlock(instanceid);

  }



  public void setConnectionAttributes(String attributeName, int attributeId) {

    this.attributeName = attributeName;

    this.attributeId = attributeId;

  }



  public void setConnectionAttributes(String attributeName, String attributeId) {

    this.attributeName = attributeName;

    this.attributeId = Integer.parseInt(attributeId);

  }



  public void setNumberOfDays( int daysIn ){

    IWTimestamp stamp= IWTimestamp.RightNow();

    stamp.addDays(-daysIn);//dagar inni

    this.date= stamp.toSQLString();

  }



  /*

  ** This method uses static layouts from this class

  **

  *//*

  public void setLayout(int LAYOUT){

    this.iLayout = LAYOUT;

  }



  /**

  *

  * return a proxy for the main text. Use the standard

  * set methods on this object such as .setFontSize(1) etc.

  * and it will set the property for all texts.

  *//*

  public Text getTextProxy(){

    return textProxy;

  }

  public Text getHeadlineProxy(){

    return headlineProxy;

  }

  public Text getInformationProxy(){

    return informationProxy;

  }

  public void setTextProxy(Text textProxy){

    this.textProxy = textProxy;

  }

  public void setHeadlineProxy(Text headlineProxy){

    this.headlineProxy = headlineProxy;

  }

  public void setInformationProxy(Text informationProxy){

    this.informationProxy = informationProxy;

  }

  public Text setTextAttributes( Text realText ){

    Text tempText = (Text) textProxy.clone();

    tempText.setText( realText.getText() );

  return tempText;

  }

  public Text setHeadlineAttributes( Text realText ){

    Text tempText = (Text) headlineProxy.clone();

    tempText.setText( realText.getText() );

    return tempText;

  }

  public Text setInformationAttributes( Text realText ){

    Text tempText = (Text) informationProxy.clone();

    tempText.setText( realText.getText() );

    return tempText;

  }

  public void setNumberOfLetters(int numberOfLetters){

    this.numberOfLetters = Math.abs(numberOfLetters);

  }

  //debug this changes the number of news displayed..that is the date alone is failing

  public void setNumberOfDisplayedNews(int numberOfDisplayedNews){

    this.limitNumberOfNews = true;

    this.numberOfDisplayedNews = Math.abs(numberOfDisplayedNews);

  }

  public void setAdmin(boolean isAdmin){

    this.isAdmin=isAdmin;

  }

  public void setFromDate(String SQLdate){

    this.date=SQLdate;

  }

  public void setWidth(int width){

    setWidth(Integer.toString(width));

  }

  public void setWidth(String width){

    this.outerTableWidth = width;

  }



  public void setNewsReaderURL(String URL){

    this.newsReaderURL = URL;

  }

  public String getNewsReaderURL(){

    return newsReaderURL;

  }

  public void setNewsCollectionURL(String URL){

    this.newsCollectionURL = URL;

  }

  public String getNewsCollectionURL(){

    return newsCollectionURL;

  }

  public void showNewsCollectionButton(boolean showNewsCollectionButton){

    this.showNewsCollectionButton = showNewsCollectionButton;

  }

  public void setNewsReaderURLAsSamePage(IWContext iwc){

    this.newsReaderURL =  iwc.getRequestURI();

  }

  public void setNewsCollectionURLAsSamePage(IWContext iwc){

    this.newsCollectionURL =  iwc.getRequestURI();

  }

  public void setNumberOfExpandedNews(int numberOfExpandedNews){

    this.numberOfExpandedNews = Math.abs(numberOfExpandedNews);

  }

  public void setShowImages(boolean showImages) {

    this.showImages=showImages;

  }

  public void setShowMoreButton(boolean showMoreButton) {

    this.showMoreButton=showMoreButton;

  }

  public void setShowHeadlineImage(boolean showHeadlineImage) {

    this.showHeadlineImage=showHeadlineImage;

  }

  public void alignImageWithHeadline() {

    this.alignWithHeadline=true;

  }

  public void setHeadlineAsLink(boolean headlineAsLink) {

    this.headlineAsLink=headlineAsLink;

    this.showHeadlineImage=true;

    this.showMoreButton=false;

  }

  public void setHeadlineImageURL(String headlineImageURL) {

    this.headlineImageURL=headlineImageURL;

  }

  public void setShowOnlyDates(boolean showOnlyDates) {

    this.showOnlyDates=showOnlyDates;

  }

  public String getBundleIdentifier(){

    return IW_BUNDLE_IDENTIFIER;

  }

  public String getObjectAlignment(){

    return sObjectAlign ;

  }

  public void setObjectAligment(String sAlign){

    sObjectAlign = sAlign;

  }

  public void addObjectBetween(PresentationObject object,int spaceNumber){

    if(objectsBetween == null)

      objectsBetween = new Hashtable();

    objectsBetween.put(new Integer(spaceNumber),object);

  }

  // overriding super class method

  public void add(PresentationObject MO){

    addObjectBetween(MO,iSpaceBetween);

    if(iLayout == NEWS_SITE_LAYOUT){

      iSpaceBetween+=2;

    }

    else

      iSpaceBetween++;

  }*/

}


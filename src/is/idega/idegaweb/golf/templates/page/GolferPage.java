package is.idega.idegaweb.golf.templates.page;

import is.idega.idegaweb.golf.HandicapOverview;
import com.idega.data.genericentity.Member;
import is.idega.idegaweb.golf.presentation.GolferFriendsSigningSheet;

import java.sql.SQLException;
import java.util.Vector;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.jmodule.login.business.AccessControl;
import com.idega.jmodule.news.presentation.NewsReader;
import com.idega.jmodule.sidemenu.presentation.Sidemenu;
import com.idega.jmodule.text.presentation.TextReader;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;


/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class GolferPage extends Page{

  public final String sTopMenuParameterName = "sTopMenuParameterName";
  public final String sInfoParameterValue = "sInfoParameterValue";
  public final String sRecordParameterValue = "sRecordParameterValue";
  public final String sInterviewsParameterValue = "sInterviewsParameterValue";
  public final String sStatisticsParameterValue = "sStatisticsParameterValue";
  public final String sPicturesParameterValue = "sPicturesParameterValue";
  public final String sHomeParameterValue = "sHomeParameterValue";
  public final String homeResultsParameterValue = "homeResultsParameterValue";
  public final String abroadResultsParameterValue = "abroadResultsParameterValue";
  public final String sSubmitParameterValue = "sSubmitParameterValue";

  private int memberId = 3152;  //Björgvins ID!!!!!!!

  //It would be smart to override this String to create possible diffrent sidemenus for different users.
  public String sideMenuAttributeName = "sideMenuAttributeName";

  private Table Maintable, innerLeftTable, innerMainTable, tempSideMenuTable, topBannerTable;
  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
  protected boolean isAdmin, isTopPictureSet;
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private Sidemenu sidemenu = new Sidemenu();

  //Variables to set!
  public String cornerLogoImageUrlInBundle;
  public int cornerLogoImageWidth, cornerLogoImageHeight;
  private int profileTextReaderId, golfbagTextReaderId, statisticsTextReaderId,
    homeNewsReaderId, supportTextReaderId, supportListTextReaderId, abroadResultsTextReaderId;

  //The text objects in the side menu are cloned from this text.
  Text theText = new Text();
  Vector color = new Vector();
  {
    theText.setFontColor("RED");
    theText.setFontFace(Text.FONT_FACE_VERDANA);
    theText.setFontSize("1");
//    theText.setBold();
    sidemenu.setText(theText);
    color.add("#FFFFFF");
    sidemenu.setColors(color);
    //sidemenu.setAddBullet(true);
  }


  public GolferPage() {
    this("");
  }

  public GolferPage(String title){
    super(title);
    setAllMargins(0);
    this.setBackgroundColor("FFFFFF");
    this.setAlinkColor("FF6000");
    this.setVlinkColor("black");
    this.setLinkColor("FF6000");
    this.setHoverColor("#FF9310");
    this.setTextDecoration("none");
    this.setStyleSheetURL("/style/GolferPageView.css");

    Maintable = new Table(3,5);
    Maintable.mergeCells(3,3,3,4);
    Maintable.mergeCells(2,3,2,4);
    //Maintable.mergeCells(2,1,2,2);
    Maintable.mergeCells(1,2,2,2);
    Maintable.mergeCells(1,5,3,5);
    Maintable.mergeCells(1,3,1,4);


    Maintable.setWidth("100%");
    Maintable.setWidth(1,1,"251");
    Maintable.setWidth(1,2,"251");
    Maintable.setWidth(1,3,"251");
    Maintable.setHeight(1,"74");
//    Maintable.setWidth(1,1,"12");
//    Maintable.setWidth(1,2,"120");
//    Maintable.setWidth(1,3,"120");

    Maintable.setAlignment(1,5,"center");
    Maintable.setAlignment(1,2,"right");
    Maintable.setAlignment(3,3, "left");
    Maintable.setAlignment(2,3,"left");
    Maintable.setAlignment(1,3,"left");
    //Maintable.setAlignment(1,3,"center");
    Maintable.setVerticalAlignment(1,3,"top");
    Maintable.setVerticalAlignment(1,4,"top");
    Maintable.setVerticalAlignment(3,3,"top");
    Maintable.setVerticalAlignment(3,4,"top");

    Maintable.add(Text.emptyString(),2,1);
    Maintable.add(Text.emptyString(),2,3);
    Maintable.setCellpadding(0);
    Maintable.setCellspacing(0);
    Maintable.addBreak(1,5);
    Maintable.addBreak(1,3);
    innerLeftTable = new Table(1,2);
    innerLeftTable.setCellpadding(0);
    innerLeftTable.setWidth(1,"251");
    innerLeftTable.setAlignment(1,2,"center");
    innerLeftTable.setCellspacing(0);
    Maintable.add(innerLeftTable,1,3);
    topBannerTable = new Table(5,1);
    topBannerTable.setWidth(1,"28");
    topBannerTable.setWidth(2,"128");
    topBannerTable.setWidth(4,"131");
    topBannerTable.setWidth(5,"28");
    topBannerTable.setCellpadding(0);
    topBannerTable.setCellspacing(0);
    topBannerTable.setAlignment(2,1,"left");
    topBannerTable.setAlignment(3,1,"center");
    topBannerTable.setAlignment(4,1,"right");
    topBannerTable.setWidth("100%");
    Maintable.add(topBannerTable,3,1);
    super.add(Maintable);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public boolean isAdmin(){
    return isAdmin;
  }

  public void add(PresentationObject objectToAdd){
    Maintable.add(objectToAdd,3,3);
  }

  public void add(String stringToAdd){
    Maintable.add(stringToAdd,3,3);
  }

  public void addFooter(PresentationObject objectToAdd){
    Maintable.add(objectToAdd,1,5);
  }

  public void addLeftTopBanner(PresentationObject objectToAdd){
    topBannerTable.add(objectToAdd,2,1);
  }

  public void addRightTopBanner(PresentationObject objectToAdd){
    topBannerTable.add(objectToAdd,4,1);
  }

  public void addCenterTopBanner(PresentationObject objectToAdd){
    topBannerTable.add(objectToAdd,3,1);
  }

  public void addLeftLogo(PresentationObject objectToAdd){
    Maintable.add(objectToAdd,1,2);
  }

  public void addLeftBanners(PresentationObject objectToAdd){
    innerLeftTable.add(objectToAdd,1,2);
  }

  public void addLeftLink(PresentationObject objectToAdd){
    innerLeftTable.add(objectToAdd,1,1);
  }

  public void addCornerLogo(PresentationObject objectToAdd){
    Maintable.add(objectToAdd,1,1);
  }

  public void addMenuLinks(PresentationObject objectToAdd){
    Maintable.add(objectToAdd,3,2);
  }

  private void addCornerLogoImage(){
    if (this.cornerLogoImageUrlInBundle != null) {
      if (cornerLogoImageWidth != -1) {
        Image imageToAdd = iwrb.getImage(this.cornerLogoImageUrlInBundle, this.cornerLogoImageWidth, this.cornerLogoImageHeight);
        Maintable.add(imageToAdd,1,1);
      }
      else{
        Image imageToAdd = iwrb.getImage(this.cornerLogoImageUrlInBundle);
        Maintable.add(imageToAdd,1,1);
      }
    }
  }

  public void addSideBannerImage(String sideBannerImageIWBundleUrl){
    Image sideBannerImage;
    sideBannerImage = iwb.getImage(sideBannerImageIWBundleUrl);
    addLeftBanners(sideBannerImage);
  }

  public void addRightTopImage(String rightTopImageIWBundleUrl){
    Image rightTopImage;
    rightTopImage = iwb.getImage(rightTopImageIWBundleUrl);
    addRightTopBanner(rightTopImage);
  }

  public void addCenterTopImage(String centerTopImageIWBundleUrl){
    Image centerTopImage;
    centerTopImage = iwb.getImage(centerTopImageIWBundleUrl);
    addCenterTopBanner(centerTopImage);
  }

  public void addLeftTopImage(String leftTopImageIWBundleUrl){
    Image leftTopImage;
    leftTopImage = iwb.getImage(leftTopImageIWBundleUrl);
    addLeftTopBanner(leftTopImage);
  }

  public void setProfileTextReaderId(int profileTextReaderId){
    this.profileTextReaderId = profileTextReaderId;
  }

  public void setGolfbagTextReaderId(int golfbagTextReaderId){
    this.golfbagTextReaderId = golfbagTextReaderId;
  }

  public void setStatisticsTextReaderId(int statisticsTextReaderId){
    this.statisticsTextReaderId = statisticsTextReaderId;
  }

  public void setSupporterTextReaderId(int supportTextReaderId){
    this.supportTextReaderId = supportTextReaderId;
  }

  public void setSupporterListTextReaderId(int supportListTextReaderId){
    this.supportListTextReaderId = supportListTextReaderId;
  }

  public void setHomeNewsReaderId(int homeNewsReaderId){
    this.homeNewsReaderId = homeNewsReaderId;
  }

  public void setAbroadResultsTextReaderId(int abroadResultsTextReaderId){
    this.abroadResultsTextReaderId = abroadResultsTextReaderId;
  }

  public void setCornerLogoImage(String cornerLogoImageUrlInBundle, int cornerLogoImageWidth, int cornerLogoImageHeight){
    this.cornerLogoImageUrlInBundle = cornerLogoImageUrlInBundle;
    this.cornerLogoImageWidth = cornerLogoImageWidth;
    this.cornerLogoImageHeight = cornerLogoImageHeight;
  }

  public void setCornerLogoImage(String cornerLogoImageUrlInBundle){
    this.cornerLogoImageUrlInBundle = cornerLogoImageUrlInBundle;
    cornerLogoImageWidth=-1;
  }

  private void setLinkMenu(){

    Table topTable = new Table(8,1);
    topTable.setHeight("101");

    Image iInfo = iwrb.getImage("/golferpage/navbar_01.gif");
    Image iRecord = iwrb.getImage("/golferpage/navbar_03.gif");
    Image iInterviews = iwrb.getImage("/golferpage/navbar_02.gif");
    Image iStatistics = iwrb.getImage("/golferpage/navbar_04.gif");
    Image iPictures = iwrb.getImage("/golferpage/navbar_05.gif");
    Image iHome = iwrb.getImage("/golferpage/navbar_06.gif");
    Image iMenuBackground = iwb.getImage("/shared/menuBackground.gif");
    Image iGolf = iwrb.getImage("/golferpage/navbar_07.gif");

    Link lInfo = new Link(iInfo);
    Link lRecord = new Link(iRecord);
    Link lInterviews = new Link(iInterviews);
    Link lStatistics = new Link(iStatistics);
    Link lPictures = new Link(iPictures);
    Link lHome = new Link(iHome);
    Link lGolf = new Link(iGolf, "/");

    /*lHome.addParameter("text_id","753");
    lHome.addParameter("module_object","com.idega.jmodule.text.presentation.TextReader");*/
    topTable.setBackgroundImage(iMenuBackground);
    //topTable.setBorder(1);

    topTable.setCellpadding(0);
    topTable.setCellspacing(0);

    topTable.add(lInfo,1,1);
    topTable.add(lRecord,2,1);
    topTable.add(lInterviews,3,1);
    topTable.add(lStatistics,4,1);
    //topTable.add(lPictures,5,1);
    topTable.add(lHome,6,1);
    topTable.add(lGolf,7,1);

    //topTable.add(iRecord,2,1);
    //topTable.add(iInterviews,3,1);
    //topTable.add(iStatistics,4,1);
    topTable.add(iPictures,5,1);

    topTable.setWidth(1,1,"20");
    topTable.setWidth(2,1,"20");
    topTable.setWidth(3,1,"20");
    topTable.setWidth(4,1,"20");
    topTable.setWidth(5,1,"20");
    topTable.setWidth(6,1,"20");
    topTable.setWidth(7,1,"20");

    topTable.setWidth("100%");
    topTable.setAlignment("left");

    topTable.setVerticalAlignment(1,1,"bottom");
    topTable.setVerticalAlignment(2,1,"bottom");
    topTable.setVerticalAlignment(3,1,"bottom");
    topTable.setVerticalAlignment(4,1,"bottom");
    topTable.setVerticalAlignment(5,1,"bottom");
    topTable.setVerticalAlignment(6,1,"bottom");
    topTable.setVerticalAlignment(7,1,"bottom");

    topTable.setCellpadding(0);
    topTable.setCellspacing(0);

    lInfo.addParameter(sTopMenuParameterName, sInfoParameterValue);
    lRecord.addParameter(sTopMenuParameterName, sRecordParameterValue);
    lInterviews.addParameter(sTopMenuParameterName, sInterviewsParameterValue);
    lInterviews.addParameter(sTopMenuParameterName, sSubmitParameterValue);
    lStatistics.addParameter(sTopMenuParameterName, sStatisticsParameterValue);
    lPictures.addParameter(sTopMenuParameterName, sPicturesParameterValue);
    lHome.addParameter(sTopMenuParameterName, sHomeParameterValue);


    addMenuLinks(topTable);

  }

  public void chooseView(IWContext iwc){

    if (iwc.isParameterSet(sTopMenuParameterName)) {

      String[] chosenParameterValue;
       chosenParameterValue = iwc.getParameterValues(sTopMenuParameterName);

      //INFO
      if (chosenParameterValue[0].equals(sInfoParameterValue)) {
        setInfoView(iwc);
      }

      //RESULTS HOME
      else if ((chosenParameterValue[0].equals(sRecordParameterValue)) || (chosenParameterValue[0].equals(homeResultsParameterValue))) {
        setHomeResultsView();
      }

      //RESULTS ABROAD
      else if (chosenParameterValue[0].equals(abroadResultsParameterValue)) {
        setAbroadResultsView(iwc);
      }

      //STATISTICS
      else if (chosenParameterValue[0].equals(sStatisticsParameterValue)) {
        setStatisticsView(iwc);
      }

      //PICTURES
      else if (chosenParameterValue[0].equals(sPicturesParameterValue)) {
        setPictureView();
      }

      //HOME
      else if (chosenParameterValue[0].equals(sHomeParameterValue)) {
        setHomeView(iwc);
      }

      //INTERVIEWS
      else if ((chosenParameterValue[0].equals(sInterviewsParameterValue)) || (chosenParameterValue[0].equals(sSubmitParameterValue))) {
        setInterviewsView(iwc);
      }
      /*else{
        this.setStyleSheetURL("/style/idega.css");
      }*/
    }
    //temporarily!!
    else{
//      this.setStyleSheetURL("/style/StatisticsView.css");
      setHomeView(iwc);
    }
//    getSideMenuViewType(iwc);
  }

  //HOME_VIEW

  private void setHomeView(IWContext iwc){
    this.setStyleSheetURL("/style/GolferPageView.css");
    Image iWelcomeLogo = iwrb.getImage("/golferpage/velkomin.gif");
    this.addLeftLogo(iWelcomeLogo);

    Table homeTable = new Table(3,1);
    homeTable.setCellpadding(0);
    homeTable.setCellspacing(0);
    Image dotLineBackgroundImage;
    dotLineBackgroundImage = iwb.getImage("shared/brotalina.gif");
    homeTable.setBackgroundImage(2,1,dotLineBackgroundImage);
    homeTable.setWidth(1,1,"410");
    homeTable.setWidth(2,1,"1");
    homeTable.add(Text.emptyString(),2,1);

    NewsReader news = new NewsReader();

    //temp
    boolean isBjorgvin = false;
    try {
      isBjorgvin = (getMember(iwc).getID() == this.memberId);
    }
    catch (Exception ex) {
      isBjorgvin = false;
    }

    news.setAdmin(isBjorgvin);
    //temp end
    news.setConnectionAttributes("golfer_page", homeNewsReaderId);
    news.setNewsEditorURL("/golfers/newsIndex.jsp");
//    news.setNumberOfDays(4);
    news.setHeadlineImageURL("/idegaweb/bundles/golf.bundle/resources/shared/bullet.gif");
    news.setWidth("100%");
    news.setNewsReaderURLAsSamePage(iwc);
    news.setNumberOfExpandedNews(8);
    news.setNumberOfDisplayedNews(8);
    news.setNumberOfLetters(220);
    news.getTextProxy().setFontSize(1);
    news.getHeadlineProxy().setFontSize(1);
    news.showNewsCollectionButton(false);
    news.setCollectionImage("collection.gif");

    //only hide images when in category view
    if( iwc.getParameter("news_id") == null ){
      news.setShowImages(false);
    }

    news.setShowOnlyDates(true);
    news.setHeadlineAsLink(true);
    //homeTable.addBreak(1,1);
    Table dummyTable1 = new Table(1,1);
    dummyTable1.setCellpadding(16);
    dummyTable1.setCellspacing(0);
    dummyTable1.add(news,1,1);
    homeTable.add(dummyTable1,1,1);
    homeTable.setVerticalAlignment(1,1,"top");

    Image golferImage = new Image();
    golferImage = iwrb.getImage("golferpage/upplysingar_logo.gif");
    Table pictureTable = new Table(1,2);
    pictureTable.setCellpadding(10);
    pictureTable.setCellspacing(0);
    pictureTable.add(golferImage,1,1);
    TextReader text = new TextReader(profileTextReaderId);
    //Added by Laddi
    text.setAdmin(isBjorgvin);
      text.setEnableDelete(false);
    text.setWidth("100%");
    text.setTableTextSize(1);
    text.setHeadlineSize(1);
    text.setTextSize(1);
    Table dummyTable2 = new Table(2,1);
    dummyTable2.setCellpadding(0);
    dummyTable2.setWidth(1,"9");
    dummyTable2.setCellspacing(0);
    dummyTable2.add(text,2,1);
    pictureTable.add(dummyTable2,1,2);
    homeTable.add(pictureTable,3,1);
    homeTable.setVerticalAlignment(3,1,"top");
    add(homeTable);
  }

  //Golfpokinn
  private void setInfoView(IWContext iwc){
    //Added by Laddi
    boolean isBjorgvin = false;
    try {
      isBjorgvin = (getMember(iwc).getID() == this.memberId);
    }
    catch (Exception ex) {
      isBjorgvin = false;
    }


    Table dummyTable = new Table(2,1);
    dummyTable.setCellpadding(24);
    Image iWelcomeLogo = iwrb.getImage("/golferpage/golfpokinn.gif");
    this.addLeftLogo(iWelcomeLogo);
//    this.addLeftLink("Arrrrrg");
    TextReader golfbagText = new TextReader(golfbagTextReaderId);
    //Added by Laddi
    golfbagText.setAdmin(isBjorgvin);
    golfbagText.setWidth("100%");
    golfbagText.setTableTextSize(1);
    golfbagText.setTextSize(1);
    golfbagText.setTextStyle(Text.FONT_FACE_ARIAL);
    golfbagText.setHeadlineSize(1);
    dummyTable.add(golfbagText,1,1);
    Image sideImage;
    sideImage = iwb.getImage("/shared/pingi3.jpg");
    dummyTable.add(sideImage,2,1);
    dummyTable.setVerticalAlignment(2,1,"top");
    dummyTable.setAlignment(2,1,"right");
    add(dummyTable);
  }

  //Árangur Erlendis
  private void setAbroadResultsView(IWContext iwc){
    //Added by Laddi
    boolean isBjorgvin = false;
    try {
      isBjorgvin = (getMember(iwc).getID() == this.memberId);
    }
    catch (Exception ex) {
      isBjorgvin = false;
    }

    Image iResultsLogo = iwrb.getImage("/golferpage/arangur.gif");
    this.addLeftLogo(iResultsLogo);
    addFakeResultsSidemenu();
    Table dummyTable = new Table(2,1);
    dummyTable.setCellpadding(10);
    dummyTable.setWidth("100%");
    dummyTable.setAlignment(2,1,"center");
    dummyTable.setAlignment(1,1,"center");
    dummyTable.setVerticalAlignment(1,1,"top");
    dummyTable.setVerticalAlignment(2,1,"top");
    TextReader abroadResultsTextReader = new TextReader(abroadResultsTextReaderId);
    //Added by Laddi
    abroadResultsTextReader.setAdmin(isBjorgvin);
    Image resultsImage;
    resultsImage = iwb.getImage("/shared/arangurMyndsmall.jpg");
    dummyTable.add(resultsImage,2,1);
    dummyTable.add(abroadResultsTextReader, 1, 1);
    add(dummyTable);
  }

  //Árangur Heima
  private void setHomeResultsView(){
    Image iResultsLogo = iwrb.getImage("/golferpage/arangur.gif");
    this.addLeftLogo(iResultsLogo);
    addFakeResultsSidemenu();
    Table dummyTable1 = new Table(1,1);
    dummyTable1.setWidth("100%");
    dummyTable1.setCellpadding(12);
    dummyTable1.addBreak(1,1);
  /*  dummyTable.setAlignment(1,1,"center");
    dummyTable.setVerticalAlignment(1,2,"top");*/
/*    Text handicapText = new Text("Forgjafar Yfirlit Björgvins");
    handicapText.setFontSize(3);
    handicapText.setBold();
    dummyTable.add(handicapText);*/

    HandicapOverview hOverview = new HandicapOverview(memberId);
      hOverview.noIcons();
      hOverview.setTilPicture("/golferpage/til.gif");
      hOverview.setFraPicture("/golferpage/fra.gif");
      hOverview.setGetOverviewButton("/golferpage/saekja.gif", sTopMenuParameterName, homeResultsParameterValue);
      hOverview.setViewScoreIconUrlInBundle("/shared/iconSkoda.gif");
      Text headerText = new Text();
      headerText.setFontColor("#FF6000");
      headerText.setFontSize(2);
      headerText.setBold();
      headerText.setFontStyle(Text.FONT_FACE_VERDANA);
      hOverview.setHeaderTextProperties(headerText);
      Text tableText = new Text();
      tableText.setBold();
      tableText.setFontColor("000000");
      tableText.setFontSize(1);
      tableText.setFontStyle(Text.FONT_FACE_VERDANA);
      hOverview.setTableTextProperties(tableText);
      Link textLink = new Link();
      textLink.setBold();
      //textLink.setFontColor("000000");
      textLink.setFontSize(1);
      textLink.setFontStyle(Text.FONT_FACE_VERDANA);
      hOverview.setTextLinkProperties(textLink);
      hOverview.setHeaderColor("#FFFFFF");
      hOverview.setTeeTextColor("#000000");
//    dummyTable.addBreak(1,1);
    dummyTable1.add(hOverview,1,1);
    add(dummyTable1);
    /*TextReader recordText = new TextReader(756);
    add(recordText);*/
  }

  //STUÐNINGSAÐILAR
  private void setInterviewsView(IWContext iwc){
    //Added by Laddi
    boolean isBjorgvin = false;
    try {
      isBjorgvin = (getMember(iwc).getID() == this.memberId);
    }
    catch (Exception ex) {
      isBjorgvin = false;
    }

    Image iInterviewsLogo = iwrb.getImage("/golferpage/studningsadilar.gif");
    this.addLeftLogo(iInterviewsLogo);

    Table interviewsTable = new Table(3,1);
    interviewsTable.setWidth("100%");
    interviewsTable.setCellpadding(0);
    interviewsTable.setCellspacing(0);
    Image dotLineBackgroundImage;
    dotLineBackgroundImage = iwb.getImage("shared/brotalina.gif");
    interviewsTable.setBackgroundImage(2,1,dotLineBackgroundImage);
    interviewsTable.setWidth(1,1,"410");
    interviewsTable.setWidth(2,1,"1");
    interviewsTable.add(Text.emptyString(),2,1);
    interviewsTable.setVerticalAlignment(1,1,"top");

    TextReader supportText = new TextReader(supportListTextReaderId);
    //Added by Laddi
    supportText.setAdmin(isBjorgvin);
    supportText.setTextStyle(Text.FONT_FACE_VERDANA);
    supportText.setAlignment("center");
    supportText.setHeadlineColor("FF6000");
    supportText.setHeadlineSize(2);
    interviewsTable.setVerticalAlignment(3,1,"top");
    interviewsTable.setAlignment( 3, 1, "center");
    Table dummyTable = new Table(1,2);
    dummyTable.setHeight(1,"3");
    dummyTable.setCellpadding(0);
    dummyTable.setCellspacing(0);
    dummyTable.addBreak(1,1);
    dummyTable.add(supportText,1,2);
    interviewsTable.add(dummyTable,3,1);

    GolferFriendsSigningSheet golferFriendsSigningSheet = new GolferFriendsSigningSheet(supportTextReaderId,
      (String) iwc.getSessionAttribute("golferName"), sTopMenuParameterName,sInterviewsParameterValue,
      sSubmitParameterValue, (String) iwc.getSessionAttribute("fullGolferName"));
    /*Table dummyTable = new Table(1,2);
    //dummyTable.setHeight(1,"20");
    dummyTable.setCellpadding(0);
    dummyTable.setCellspacing(0);
    dummyTable.addBreak(1,1);
    dummyTable.add(golferFriendsSigningSheet,1,2);*/
    interviewsTable.add(golferFriendsSigningSheet,1,1);
    add(interviewsTable);
  }

  //STATISTICS_VIEW
  private void setStatisticsView(IWContext iwc){
    //Added by Laddi
    boolean isBjorgvin = false;
    try {
      isBjorgvin = (getMember(iwc).getID() == this.memberId);
    }
    catch (Exception ex) {
      isBjorgvin = false;
    }

    Table dummyTable = new Table(2,1);
    dummyTable.setWidth("100%");
    dummyTable.setCellpadding(24);
    Image iStatisticsLogo = iwrb.getImage("/golferpage/tolfraedi.gif");
    this.addLeftLogo(iStatisticsLogo);
    TextReader statisticText = new TextReader(statisticsTextReaderId);
    //Added by Laddi
    statisticText.setAdmin(isBjorgvin);
    statisticText.setWidth("100%");
    statisticText.setTableTextSize(1);
    statisticText.setTextSize(1);
    statisticText.setTextStyle(Text.FONT_FACE_ARIAL);
    statisticText.setHeadlineSize(1);
    dummyTable.add(statisticText,1,1);
    Image statisticsImage;
    statisticsImage = iwb.getImage("/shared/tolfraediMynd.gif");
    dummyTable.add(statisticsImage,2,1);
    dummyTable.setVerticalAlignment(2,1,"top");
    dummyTable.setAlignment(2,1,"right");
    add(dummyTable);
  }

  //PICTURES_VIEW
  private void setPictureView(){
    Image iWelcomeLogo = iwrb.getImage("/golferpage/velkomin.gif");
    this.addLeftLogo(iWelcomeLogo);
    /*sidemenu.setConnectionAttributes(sideMenuAttributeName,2);
    sidemenu.addParameter(sTopMenuParameterName, sPicturesParameterValue);*/
   // this.addUpperLeftLink(sidemenu);
  }

  private void addFakeResultsSidemenu(){
    String string1 = iwrb.getLocalizedString("golferpage.home_results","Árangur heima");
    String string2 = iwrb.getLocalizedString("golferpage.abroad_results","Árangur erlendis");
    this.setStyleSheetURL("/style/GolferStatisticsView.css");
    Table dummyTable = new Table(1,1);
    dummyTable.setHeight(1,"70");
    dummyTable.setCellspacing(8);
    Table fakeSideMenuHomeTable = new Table(2,1);
    fakeSideMenuHomeTable.setCellpadding(5);
    fakeSideMenuHomeTable.setVerticalAlignment(2,1,"bottom");
    Image bullet = iwb.getImage("shared/arrow_ble.gif");
    fakeSideMenuHomeTable.add(bullet,1,1);
    Link fakeSideMenuLinkHome = new Link("  "+string1);
    fakeSideMenuLinkHome.setCSSClass("style1");
    fakeSideMenuLinkHome.setFontFace(Text.FONT_FACE_VERDANA);
    fakeSideMenuLinkHome.setFontSize(1);
    fakeSideMenuLinkHome.setBold();
    fakeSideMenuLinkHome.setStyle("linkur");
    fakeSideMenuLinkHome.addParameter(sTopMenuParameterName, homeResultsParameterValue);
    fakeSideMenuHomeTable.add(fakeSideMenuLinkHome,2,1);
    dummyTable.add(fakeSideMenuHomeTable,1,1);
    Link fakeSideMenuLinkAbroad = new Link("  "+string2);
    fakeSideMenuLinkAbroad.setCSSClass("style1");
    fakeSideMenuLinkAbroad.setFontFace(Text.FONT_FACE_VERDANA);
    fakeSideMenuLinkAbroad.setStyle("linkur");
    fakeSideMenuLinkAbroad.setFontSize(1);
    fakeSideMenuLinkAbroad.setBold();
    fakeSideMenuLinkAbroad.addParameter(sTopMenuParameterName, abroadResultsParameterValue);
    Table fakeSideMenuLinkAbroadTable = new Table(2,1);
    fakeSideMenuLinkAbroadTable.setCellpadding(5);
    fakeSideMenuLinkAbroadTable.setVerticalAlignment(2,1,"bottom");
    fakeSideMenuLinkAbroadTable.add(bullet,1,1);
    fakeSideMenuLinkAbroadTable.add(fakeSideMenuLinkAbroad,2,1);
//    dummyTable.addBreak(1,1);
    dummyTable.add(fakeSideMenuLinkAbroadTable,1,1);
    this.addLeftLink(dummyTable);
  }

/* private void getSideMenuViewType(IWContext iwc){
  String module_object = iwc.getParameter("module_object");

  if (module_object == null) {
    //add(getNews(iwc),2,2);
  }
  else {
      try {
          if (!module_object.equals("com.idega.jmodule.sidemenu.presentation.Sidemenu")) {
             PresentationObject  jmodule = (PresentationObject) Class.forName(module_object).newInstance();
             add(jmodule);
             configure(module_object,jmodule,iwc);
          }
      }
      catch (Exception e) {
          //add(getNews(iwc));
      }
    }
  }


  private void configure(String module_object, PresentationObject jmodule, IWContext iwc) throws Exception {
    if (module_object.equals("com.idega.jmodule.boxoffice.presentation.BoxReader")) {

      com.idega.jmodule.boxoffice.presentation.BoxReader box = (com.idega.jmodule.boxoffice.presentation.BoxReader) jmodule;
      box.setBoxWidth(230);
      box.setNumberOfDisplayed(6);
      box.setNumberOfColumns(2);
      box.setNumberOfLetters(60);
      box.setNoIcons(true);
      box.setBoxOutline(1);
      box.setOutlineColor("#8ab490");
      box.setInBoxColor("#FFFFFF");
      box.setLeftBoxWidth(0);
      box.setRightBoxWidth(0);
      box.setTableAlignment("center");
      box.setBoxPadding(1);
      box.setShowCategoryHeadline(true);
      box.setIncludeBackgroundImage(false);
      box.setBoxCategoryHeadlineSize(1);
      box.setBoxCategoryHeadlineColor("#FFFFFF");
      box.setTopBoxHeight(20);
      box.setBoxSpacing(10);
      box.setUsePopUp(false);

    }

    else if (module_object.equals("com.idega.jmodule.text.presentation.TextReader")) {

      com.idega.jmodule.text.presentation.TextReader text = (com.idega.jmodule.text.presentation.TextReader) jmodule;
      text.setEnableDelete(false);
      text.setWidth("100%");
      text.setTableTextSize(2);

    }

    else if (module_object.equals("com.idega.jmodule.sidemenu.presentation.Sidemenu")) {

      com.idega.jmodule.sidemenu.presentation.Sidemenu sidemenu = (com.idega.jmodule.sidemenu.presentation.Sidemenu) jmodule;
      Vector colors = new Vector();
      colors.addElement("#FFFFFF");
      sidemenu.setColors(colors);

    }

    else if (module_object.equals("com.idega.jmodule.news.presentation.NewsReader")) {


      System.err.println("ÉG ER H'ER!!!!");
      NewsReader news = (NewsReader) jmodule;
//      news.setEnableDelete(false);
      news.setConnectionAttributes("golfer_page", homeNewsReaderId);
      news.setNewsEditorURL("/golfers/newsIndex.jsp");
      news.setNumberOfDays(4);
      news.setNewsReaderURLAsSamePage(iwc);
      news.setNumberOfExpandedNews(3);
      news.setNumberOfDisplayedNews(3);
      news.setNumberOfLetters(250);
      news.getTextProxy().setFontSize(1);
      news.getHeadlineProxy().setFontSize(1);
      news.setShowImages(false);
      news.setShowOnlyDates(true);
      news.setHeadlineAsLink(true);
      news.setWidth(220);
    }
  }*/

  public void main(IWContext iwc) throws Exception {

    try {
      isAdmin =  com.idega.jmodule.login.business.AccessControl.isAdmin(iwc);
    }
    catch(SQLException E) {    }
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    addCornerLogoImage();

    Image dotLineBackgroundImage;
    dotLineBackgroundImage = iwb.getImage("shared/brotalina.gif");
    Maintable.setBackgroundImage(2,3,dotLineBackgroundImage);
    Maintable.setWidth(2,3,"1");

    addLeftTopImage("shared/ping.gif");
    addCenterTopImage("shared/footjoy.gif");
    addRightTopImage("shared/titleist.gif");

    Image footerImage;
    footerImage = iwrb.getImage("golferpage/index_23.gif");
    this.addFooter(footerImage);

    Image sideBanners;
    sideBanners = iwb.getImage("shared/logo_leftside.gif");
    Table dummytable1 = new Table(1,1);
    dummytable1.setCellpadding(10);
    dummytable1.setWidth("100%");
    dummytable1.setAlignment(1,1,"center");
    dummytable1.setVerticalAlignment(1,1, "top");
    dummytable1.add(sideBanners,1,1);
    this.addLeftBanners(dummytable1);

      //Notice this needs changing!
    /*Image bullet = iwb.getImage("shared/bullet.gif");
    sidemenu.setBulletImage(bullet);*/

    setLinkMenu();
    chooseView(iwc);
  }
  
  protected Member getMember(IWContext iwc){
  	return AccessControl.getMember(iwc);
  	
  }
}

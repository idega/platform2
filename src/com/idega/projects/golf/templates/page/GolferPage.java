package com.idega.projects.golf.templates.page;

import com.idega.jmodule.object.Page;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.banner.presentation.*;
import com.idega.jmodule.sidemenu.presentation.Sidemenu;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.textObject.Link;
import com.idega.jmodule.news.presentation.NewsReader;
import java.util.Vector;
import java.sql.SQLException;
import com.idega.projects.golf.HandicapOverview;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.text.presentation.TextReader;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;


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
  public final String fakeSideMenuParameterName = "fakeSideMenuParameterName";
  public final String fakeSideMenuNewsParameterValue = "fakeSideMenuNewsParameterValue";
  public final String fakeSideMenuProfileParameterValue = "fakeSideMenuProfileParameterValue";

  private int memberId = 3152;

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
  private int profileTextReaderId, golfbagTextReaderId, homeNewsReaderId;

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
//    Maintable.mergeCells(1,5,3,5);
    Maintable.mergeCells(2,3,2,4);
    Maintable.mergeCells(2,1,2,2);
    Maintable.mergeCells(1,5,3,5);
    Maintable.mergeCells(1,3,1,4);
    Maintable.setAlignment(1,5,"center");
    Maintable.setVerticalAlignment(1,3,"top");
    Maintable.setVerticalAlignment(1,4,"top");
    Maintable.setVerticalAlignment(3,3,"top");
    Maintable.setVerticalAlignment(3,4,"top");
    Maintable.setWidth("100%");
    Maintable.setAlignment(1,2,"left");
    Maintable.setWidth(1,2,"120");
    Maintable.add(Text.emptyString(),2,1);
    Maintable.add(Text.emptyString(),2,3);
    Maintable.setVerticalAlignment(1,3,"top");
    Maintable.setCellpadding(0);
    Maintable.setCellspacing(0);
    Maintable.setAlignment(1,3,"center");
    Maintable.addBreak(1,5);
    Maintable.setAlignment(3,3, "left");
   /* innerMainTable = new Table(1,2);
    innerMainTable.setCellpadding(0);
    innerMainTable.setCellspacing(10);*/
    innerLeftTable = new Table(1,2);
    innerLeftTable.setCellpadding(0);
    innerLeftTable.setCellspacing(0);
    Maintable.add(innerLeftTable,1,3);
    //Maintable.add(innerMainTable,3,3);
    Maintable.setVerticalAlignment(3,3,"top");
   /* Table dummyTable = new Table(1,1);
    dummyTable.setCellpadding(10);
    dummyTable.setCellspacing(0);
    tempSideMenuTable = new Table(1,2);
    tempSideMenuTable.setCellpadding(0);
    tempSideMenuTable.setCellspacing(0);
    tempSideMenuTable.setAlignment(1,1,"bottom");
    tempSideMenuTable.setAlignment(1,2,"top");
    dummyTable.add(tempSideMenuTable,1,1);
    innerLeftTable.add(dummyTable,1,1);*/
    topBannerTable = new Table(3,1);
    topBannerTable.setAlignment(1,1,"left");
    topBannerTable.setAlignment(2,1,"center");
    topBannerTable.setAlignment(3,1,"right");
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

  public void add(ModuleObject objectToAdd){
    Maintable.add(objectToAdd,3,3);
  }

  public void add(String stringToAdd){
    Maintable.add(stringToAdd,3,3);
  }

  public void addFooter(ModuleObject objectToAdd){
    Maintable.add(objectToAdd,1,5);
  }

  public void addLeftTopBanner(ModuleObject objectToAdd){
    topBannerTable.add(objectToAdd,1,1);
  }

  public void addRightTopBanner(ModuleObject objectToAdd){
    topBannerTable.add(objectToAdd,3,1);
  }

  public void addCenterTopBanner(ModuleObject objectToAdd){
    topBannerTable.add(objectToAdd,2,1);
  }

  public void addLeftLogo(ModuleObject objectToAdd){
    Maintable.add(objectToAdd,1,2);
  }

  public void addLeftBanners(ModuleObject objectToAdd){
    innerLeftTable.add(objectToAdd,1,2);
  }

 /* public void addUpperLeftLink(ModuleObject objectToAdd){
    tempSideMenuTable.add(objectToAdd,1,1);
  }

  public void addLowerLeftLink(ModuleObject objectToAdd){
    tempSideMenuTable.add(objectToAdd,1,2);
  }

  public void addUpperLeftLink(String stringToAdd){
    tempSideMenuTable.add(stringToAdd,1,1);
  }

  public void addLowerLeftLink(String stringToAdd){
    tempSideMenuTable.add(stringToAdd,1,2);
  }*/

  public void addLeftLink(ModuleObject objectToAdd){
    innerLeftTable.add(objectToAdd,1,1);
  }

  public void addCornerLogo(ModuleObject objectToAdd){
    Maintable.add(objectToAdd,1,1);
  }

  public void addMenuLinks(ModuleObject objectToAdd){
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

  public void setHomeNewsReaderId(int homeNewsReaderId){
    this.homeNewsReaderId = homeNewsReaderId;
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

    Table topTable = new Table(7,1);
    topTable.setHeight("101");

    Image iInfo = iwrb.getImage("/golferpage/navbar_01.gif");
    Image iRecord = iwrb.getImage("/golferpage/navbar_03.gif");
    Image iInterviews = iwrb.getImage("/golferpage/navbar_02.gif");
    Image iStatistics = iwrb.getImage("/golferpage/navbar_04.gif");
    Image iPictures = iwrb.getImage("/golferpage/navbar_05.gif");
    Image iHome = iwrb.getImage("/golferpage/navbar_06.gif");
    Image iMenuBackground = iwb.getImage("/shared/menuBackground.gif");

    Link lInfo = new Link(iInfo);
    Link lRecord = new Link(iRecord);
    Link lInterviews = new Link(iInterviews);
    Link lStatistics = new Link(iStatistics);
    Link lPictures = new Link(iPictures);
    Link lHome = new Link(iHome);

    /*lHome.addParameter("text_id","753");
    lHome.addParameter("module_object","com.idega.jmodule.text.presentation.TextReader");*/
    topTable.setBackgroundImage(iMenuBackground);
    //topTable.setBorder(1);

    topTable.setCellpadding(0);
    topTable.setCellspacing(0);

    topTable.add(lInfo,1,1);
    /*topTable.add(lRecord,2,1);
    topTable.add(lInterviews,3,1);
    topTable.add(lStatistics,4,1);
    topTable.add(lPictures,5,1);*/
    topTable.add(lHome,6,1);

    topTable.add(iRecord,2,1);
    topTable.add(iInterviews,3,1);
    topTable.add(iStatistics,4,1);
    topTable.add(iPictures,5,1);

    topTable.setWidth(1,1,"20");
    topTable.setWidth(2,1,"20");
    topTable.setWidth(3,1,"20");
    topTable.setWidth(4,1,"20");
    topTable.setWidth(5,1,"20");
    topTable.setWidth(6,1,"20");

    topTable.setVerticalAlignment(1,1,"bottom");
    topTable.setVerticalAlignment(2,1,"bottom");
    topTable.setVerticalAlignment(3,1,"bottom");
    topTable.setVerticalAlignment(4,1,"bottom");
    topTable.setVerticalAlignment(5,1,"bottom");
    topTable.setVerticalAlignment(6,1,"bottom");

    topTable.setCellpadding(0);
    topTable.setCellspacing(0);

    lInfo.addParameter(sTopMenuParameterName, sInfoParameterValue);
    //lRecord.addParameter(sTopMenuParameterName, sRecordParameterValue);
    //lInterviews.addParameter(sTopMenuParameterName, sInterviewsParameterValue);
    //lStatistics.addParameter(sTopMenuParameterName, sStatisticsParameterValue);
    //lPictures.addParameter(sTopMenuParameterName, sPicturesParameterValue);
    lHome.addParameter(sTopMenuParameterName, sHomeParameterValue);

    topTable.setWidth("100%");

    addMenuLinks(topTable);

  }

  public void chooseView(ModuleInfo modinfo){

    if (modinfo.isParameterSet(sTopMenuParameterName)) {

      String chosenParameterValue = new String(modinfo.getParameter(sTopMenuParameterName));

      //INFO
      if (chosenParameterValue.equals(sInfoParameterValue)) {
        setInfoView();
      }

      //RECORD
      else if (chosenParameterValue.equals(sRecordParameterValue)) {
        setRecordView();
      }

      //INTERVIEWS
      else if (chosenParameterValue.equals(sInterviewsParameterValue)) {
        setInterviewsView();
      }

      //STATISTICS
      else if (chosenParameterValue.equals(sStatisticsParameterValue)) {
        setStatisticsView();
      }

      //PICTURES
      else if (chosenParameterValue.equals(sPicturesParameterValue)) {
        setPictureView();
      }

      //HOME
      else if (chosenParameterValue.equals(sHomeParameterValue)) {
        setHomeView(modinfo);
      }

      if (chosenParameterValue.equals(sStatisticsParameterValue)) {
        this.setStyleSheetURL("/style/StatisticsView.css");
      }
      /*else{
        this.setStyleSheetURL("/style/idega.css");
      }*/
    }
    //temporarily!!
    else if (modinfo.isParameterSet(fakeSideMenuParameterName)) {
      setHomeView(modinfo);
    }
    else{
//b      this.setStyleSheetURL("/style/StatisticsView.css");
      setHomeView(modinfo);
    }
    getSideMenuViewType(modinfo);
  }

  //HOME_VIEW

  public void setHomeView(ModuleInfo modinfo){
    Image iWelcomeLogo = iwrb.getImage("/golferpage/velkomin.gif");
    this.addLeftLogo(iWelcomeLogo);
   /* sidemenu.setConnectionAttributes(sideMenuAttributeName,1);
    sidemenu.addParameter(sTopMenuParameterName, sHomeParameterValue);
    this.addLowerLeftLink(sidemenu);
    Table fakeSideMenuLinkNewsTable = new Table(2,1);
    Image bullet = iwb.getImage("shared/bullet.gif");
    fakeSideMenuLinkNewsTable.add(bullet,1,1);
    Link fakeSideMenuLinkNews = new Link("FRÉTTIR");
    fakeSideMenuLinkNews.setFontFace(Text.FONT_FACE_VERDANA);
    fakeSideMenuLinkNews.setFontSize(1);
    fakeSideMenuLinkNews.setStyle("linkur");
    fakeSideMenuLinkNews.addParameter(fakeSideMenuParameterName, fakeSideMenuNewsParameterValue);
    fakeSideMenuLinkNewsTable.add(fakeSideMenuLinkNews,2,1);
    this.addUpperLeftLink(fakeSideMenuLinkNewsTable);
    Link fakeSideMenuLinkProfile = new Link("UM BJÖRGVIN");
    fakeSideMenuLinkProfile.setFontFace(Text.FONT_FACE_VERDANA);
    fakeSideMenuLinkProfile.setStyle("linkur");
    fakeSideMenuLinkProfile.setFontSize(1);
    fakeSideMenuLinkProfile.addParameter(fakeSideMenuParameterName, fakeSideMenuProfileParameterValue);
    Table fakeSideMenuLinkProfileTable = new Table();
    fakeSideMenuLinkProfileTable.add(bullet,1,1);
    fakeSideMenuLinkProfileTable.add(fakeSideMenuLinkProfile,2,1);
    this.addLowerLeftLink(fakeSideMenuLinkProfileTable);*/

    Table homeTable = new Table(3,1);
    homeTable.setCellpadding(0);
    homeTable.setCellspacing(0);
    Image dotLineBackgroundImage;
    dotLineBackgroundImage = iwb.getImage("shared/brotalina.gif");
    homeTable.setBackgroundImage(2,1,dotLineBackgroundImage);
    homeTable.setWidth(1,1,"375");
    homeTable.setWidth(2,1,"1");
    homeTable.add(Text.emptyString(),2,1);

    NewsReader news = new NewsReader();
    news.setConnectionAttributes("golfer_page", homeNewsReaderId);
    news.setNewsEditorURL("/golfers/newsIndex.jsp");
//    news.setNumberOfDays(4);
    news.setHeadlineImageURL("/idegaweb/bundles/golf.bundle/resources/shared/bullet.gif");
    news.setWidth("100%");
    news.setNewsReaderURLAsSamePage(modinfo);
    news.setNumberOfExpandedNews(8);
    news.setNumberOfDisplayedNews(8);
    news.setNumberOfLetters(200);
    news.getTextProxy().setFontSize(1);
    news.getHeadlineProxy().setFontSize(1);
    news.showNewsCollectionButton(false);
    news.setShowImages(false);
    news.setShowOnlyDates(true);
    news.setHeadlineAsLink(true);
    homeTable.add(news,3,1);
    homeTable.setVerticalAlignment(3,1,"top");

    Image golferImage = new Image();
    golferImage = iwb.getImage("shared/mynd.jpg");
//    setTopPicture(true);
//    addTopPicture(golferImage);
    Table pictureTable = new Table(1,2);
    pictureTable.setCellpadding(10);
    pictureTable.setCellspacing(0);
    pictureTable.add(golferImage,1,1);
    TextReader text = new TextReader(profileTextReaderId);
      text.setEnableDelete(false);
    text.setWidth("100%");
    text.setTableTextSize(1);
    text.setHeadlineSize(1);
    text.setTextSize(1);
    pictureTable.add(text,1,2);
    homeTable.add(pictureTable,1,1);
    homeTable.setVerticalAlignment(1,1,"top");
    add(homeTable);

    /*if (modinfo.getParameter("module_object") == null && !(modinfo.isParameterSet(fakeSideMenuParameterName))){
      golferImage = new Image();
      golferImage = iwb.getImage("shared/mynd.jpg");
      setTopPicture(true);
      addTopPicture(golferImage);
      TextReader text = new TextReader(753);
      text.setWidth("100%");
      text.setTableTextSize(2);
      add(text);
    }
    else if (modinfo.getParameter("module_object").equalsIgnoreCase("com.idega.jmodule.text.presentation.TextReader")) {
      golferImage = new Image();
      golferImage = iwb.getImage("shared/mynd.jpg");
      setTopPicture(true);
      addTopPicture(golferImage);
    }
    else if (modinfo.isParameterSet(fakeSideMenuParameterName)) {
      if (modinfo.getParameter(fakeSideMenuParameterName).equalsIgnoreCase(fakeSideMenuNewsParameterValue)){
        setTopPicture(false);
        NewsReader news = new NewsReader();
        news.setConnectionAttributes("golfer_page", 227);
        news.setNewsEditorURL("/golfers/newsIndex.jsp");
        news.setNumberOfDays(4);
        news.setWidth("100%");
        news.setNewsReaderURLAsSamePage(modinfo);
        news.setNumberOfExpandedNews(3);
        news.setNumberOfDisplayedNews(3);
        news.setNumberOfLetters(250);
        news.getTextProxy().setFontSize(1);
        news.getHeadlineProxy().setFontSize(1);
        news.setShowImages(false);
        news.setShowOnlyDates(true);
        news.setHeadlineAsLink(true);
        this.add(news);
      }
      else if (modinfo.getParameter(fakeSideMenuParameterName).equalsIgnoreCase(fakeSideMenuProfileParameterValue)) {

      }
      else{
        golferImage = new Image();
        golferImage = iwb.getImage("shared/mynd.jpg");
        setTopPicture(true);
        addTopPicture(golferImage);
        TextReader text = new TextReader(753);
        text.setWidth("100%");
        text.setTableTextSize(2);
        add(text);
      }
    }
    else{
      golferImage = new Image();
      golferImage = iwb.getImage("shared/mynd.jpg");
      setTopPicture(true);
      addTopPicture(golferImage);
      TextReader text = new TextReader(753);
      text.setWidth("100%");
      text.setTableTextSize(2);
      add(text);
    }*/
  }

  //Golfpokinn
  public void setInfoView(){

    Table dummyTable = new Table(2,1);
    dummyTable.setCellpadding(24);
    Image iWelcomeLogo = iwrb.getImage("/golferpage/upplysingar.gif");
    this.addLeftLogo(iWelcomeLogo);
//    this.addLeftLink("Arrrrrg");
    TextReader golfbagText = new TextReader(golfbagTextReaderId);
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

  //Árangur
  public void setRecordView(){
    Image iRecordLogo = iwrb.getImage("/golferpage/ferill.gif");
    this.addLeftLogo(iRecordLogo);
    HandicapOverview hOverview = new HandicapOverview(memberId);
    add(hOverview);
    /*TextReader recordText = new TextReader(756);
    add(recordText);*/
  }

  //Stuðningsaðilar
  public void setInterviewsView(){
    Image iInterviewsLogo = iwrb.getImage("/golferpage/velkomin.gif");
    this.addLeftLogo(iInterviewsLogo);
    /*TextReader interviewText = new TextReader(757);
    add(interviewText);*/
  }

  //STATISTICS_VIEW
  public void setStatisticsView(){
    Image iStatisticsLogo = iwrb.getImage("/golferpage/tolfraedi.gif");
    this.addLeftLogo(iStatisticsLogo);

    /*    TextReader statisticText = new TextReader(759);
    add(statisticText);*/
  }

  //PICTURES_VIEW
  public void setPictureView(){
    Image iWelcomeLogo = iwrb.getImage("/golferpage/velkomin.gif");
    this.addLeftLogo(iWelcomeLogo);
    /*sidemenu.setConnectionAttributes(sideMenuAttributeName,2);
    sidemenu.addParameter(sTopMenuParameterName, sPicturesParameterValue);*/
   // this.addUpperLeftLink(sidemenu);

  }

 private void getSideMenuViewType(ModuleInfo modinfo){
  String module_object = modinfo.getParameter("module_object");

  if (module_object == null) {
    //add(getNews(modinfo),2,2);
  }
  else {
      try {
          if (!module_object.equals("com.idega.jmodule.sidemenu.presentation.Sidemenu")) {
             ModuleObject  jmodule = (ModuleObject) Class.forName(module_object).newInstance();
             add(jmodule);
             configure(module_object,jmodule,modinfo);
          }
      }
      catch (Exception e) {
          //add(getNews(modinfo));
      }
  }
  }


  private void configure(String module_object, ModuleObject jmodule, ModuleInfo modinfo) throws Exception {
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
      news.setNewsReaderURLAsSamePage(modinfo);
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
  }

  public void main(ModuleInfo modinfo) throws Exception {

    try {
      isAdmin =  com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException E) {    }
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    addCornerLogoImage();

    Image dotLineBackgroundImage;
    dotLineBackgroundImage = iwb.getImage("shared/brotalina.gif");
    Maintable.setBackgroundImage(2,3,dotLineBackgroundImage);
    Maintable.setWidth(2,3,"1");

    addLeftTopImage("shared/top1.gif");
    addCenterTopImage("shared/top2.gif");
    addRightTopImage("shared/top3.gif");

    Image footerImage;
    footerImage = iwrb.getImage("golferpage/index_23.gif");
    this.addFooter(footerImage);

    Image sideBanners;
    sideBanners = iwb.getImage("shared/sidebanner.gif");
    this.addLeftBanners(sideBanners);

      //Notice this needs changing!
    Image bullet = iwb.getImage("shared/bullet.gif");
    sidemenu.setBulletImage(bullet);

    setLinkMenu();
    chooseView(modinfo);
  }
}

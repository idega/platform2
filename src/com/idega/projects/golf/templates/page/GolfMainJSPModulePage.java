/*
 * $Id: GolfMainJSPModulePage.java,v 1.21 2001/07/30 10:52:34 tryggvil Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.projects.golf.templates.page;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.golf.*;
import com.idega.projects.golf.presentation.*;
import com.idega.projects.golf.moduleobject.Login;
import com.idega.jmodule.*;
import com.idega.jmodule.banner.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.poll.moduleobject.*;
import com.idega.jmodule.news.data.*;
import com.idega.jmodule.forum.data.*;
import com.idega.jmodule.boxoffice.presentation.*;
import com.idega.util.*;
import java.sql.*;
import com.idega.projects.golf.entity.*;
import java.io.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.core.localisation.business.LocaleSwitcher;


/**
 * @author Gudmundur idega iceland
 */
public class GolfMainJSPModulePage extends MainPage {
  protected Login login;
  protected Table centerTable;
  protected String align;

  protected final int SIDEWIDTH = 720;
  protected final int LEFTWIDTH = 163;
  protected final int RIGHTWIDTH = 148;
  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  protected boolean isAdmin = false;

  public GolfMainJSPModulePage() {
    super();
    initCenter();
  }

  protected void User(ModuleInfo modinfo) throws SQLException, IOException {
    this.setTextDecoration("none");
    setTopMargin(5);
    add("top", golfHeader());
    add("top", Top(modinfo));
    add("bottom", golfFooter());
    add(Left(modinfo), Center(), Right(modinfo));
	  setWidth(1, "" + LEFTWIDTH);
	  setContentWidth( "100%");
	  setWidth(3, "" + RIGHTWIDTH);
  }

  protected Table Top(ModuleInfo modinfo) throws SQLException,IOException{
    Table topTable = new Table(3,1);
    topTable.setCellpadding(0);
    topTable.setCellspacing(0);
    topTable.setHeight("90");
    topTable.add(getLogin(),1,1);
    topTable.add(getHBanner(modinfo),2,1);


    topTable.add(iwrb.getImage("/banners/small_ad.gif"),3,1);

    topTable.setAlignment(2,1,"center");
    topTable.setAlignment(3,1,"center");
    topTable.setVerticalAlignment(1,1, "top");
    topTable.setVerticalAlignment(2,1, "middle");
    topTable.setVerticalAlignment(3,1, "middle");

    topTable.setWidth(1, "" + LEFTWIDTH);
    topTable.setWidth("100%");
    topTable.setWidth(3, "" + RIGHTWIDTH);

    return topTable;
  }


  protected Table Left(ModuleInfo modinfo) throws SQLException, IOException {
    Table leftTable = new Table(1,13);
    //leftTable.setBorder(1);
    leftTable.setVerticalAlignment("top");
    leftTable.setVerticalAlignment(1,1,"top");
    leftTable.setVerticalAlignment(1,2,"top");
    leftTable.setVerticalAlignment(1,3,"top");
    leftTable.setVerticalAlignment(1,4,"top");
    leftTable.setVerticalAlignment(1,5,"top");
    leftTable.setVerticalAlignment(1,6,"top");
    leftTable.setVerticalAlignment(1,7,"top");
    leftTable.setVerticalAlignment(1,8,"top");
    leftTable.setVerticalAlignment(1,9,"top");
    leftTable.setVerticalAlignment(1,10,"top");
    leftTable.setVerticalAlignment(1,11,"top");
    //leftTable.setHeight("100%");
    leftTable.setColumnAlignment(1, "left");
    leftTable.setWidth(LEFTWIDTH);
    leftTable.setCellpadding(0);
    leftTable.setCellspacing(0);

    leftTable.setAlignment(1,1,"center");
    leftTable.add(Canon(),1,1);
    leftTable.addBreak(1,1);
    leftTable.add(Languages(),1,2);
    leftTable.add(Sponsors(), 1,3);
    leftTable.add(clubNews(),1,5);
    leftTable.add(new TournamentBox(),1,7);
    leftTable.add(getChat(),1,9);
    leftTable.add(getLinks(modinfo),1,11);
    leftTable.add(idega(),1,13);

    return leftTable;
  }

  protected Link Canon() {
      Window window = new Window("Hola fyrir holu",796,600,"/tournament/holeview.jsp?&tournamentID=100&tournamentGroupID=3&tournamentRoundID=232");
          window.setMenubar(true);
          window.setResizable(true);

      Link link = new Link(iwb.getImage("shared/canon_holufholu.jpg"),window);
      return link;
  }

  protected Table Languages() throws SQLException {
    Table languages = new Table(4,1);
    languages.setAlignment("left");
    Text IS = new Text(iwrb.getLocalizedString("languages.icelandic","icelandic"));
    Text EN = new Text(iwrb.getLocalizedString("languages.english","english"));
    IS.setFontSize(Text.FONT_SIZE_7_HTML_1);
    EN.setFontSize(Text.FONT_SIZE_7_HTML_1);

    // vantar link á textann og myndirnar
   // IS.setFontColor("#CCCCCC");
   // EN.setFontColor("#ABABAB");

    Link isLink = new Link(iwb.getImage("shared/icelandic.gif"));
    isLink.setEventListener(com.idega.core.localisation.business.LocaleSwitcher.class.getName());
    isLink.addParameter(LocaleSwitcher.languageParameterString,LocaleSwitcher.icelandicParameterString);

    Link enLink = new Link(iwb.getImage("shared/english.gif") );
    enLink.setEventListener(com.idega.core.localisation.business.LocaleSwitcher.class.getName());
    enLink.addParameter(LocaleSwitcher.languageParameterString,LocaleSwitcher.englishParameterString);

    languages.add( isLink , 1,1);
    languages.add( IS , 2,1);
    languages.add( enLink , 3,1);
    languages.add( EN , 4,1);

    return languages;
  }


  protected HeaderTable getChat() throws SQLException {
    HeaderTable table = new HeaderTable();
    table.setBorderColor("#8ab490");
    table.setHeadlineSize(1);
    table.setHeadlineColor("#FFFFFF");
    table.setRightHeader(false);
    table.setHeadlineAlign("left");
    table.setWidth(148);
    table.setHeaderText(iwrb.getLocalizedString("Chat","Chat"));

    ForumThread[] forum = (ForumThread[]) (new ForumThread()).findAllByColumnOrdered("parent_thread_id","-1","thread_date desc");

    int links = 4;
    Table myTable = new Table();
    myTable.setWidth("100%");
    myTable.setCellpadding(2);
    myTable.setCellspacing(2);

    if ( forum.length < links ) {
      links = forum.length;
    }

    for (int a = 0; a < links; a++) {
      idegaTimestamp stampur = new idegaTimestamp(forum[a].getThreadDate());
      String minutes = stampur.getMinute()+"";
      if ( stampur.getMinute() < 10 ) {
        minutes = "0" + stampur.getMinute();
      }

      Text userText = new Text(forum[a].getUserName()+" - ");
      userText.setFontSize(1);
      userText.setFontColor("#666666");

      Text chatDate = new Text(stampur.getDate()+"/"+stampur.getMonth()+"/"+stampur.getYear()+" "+stampur.getHour()+":"+minutes);
      chatDate.setFontSize(1);
      chatDate.setFontColor("#666666");

      Link chatLink = new Link(forum[a].getThreadSubject(),"/forum/index.jsp");
      chatLink.setFontSize(1);
      chatLink.addParameter("forum_thread_id",forum[a].getID()+"");
      chatLink.addParameter("forum_id",forum[a].getForumID()+"");
      chatLink.addParameter("state","3");
      chatLink.addParameter("FTopen",forum[a].getID()+"");

      myTable.add(userText,1,a+1);
      myTable.add(chatDate,1,a+1);
      myTable.addBreak(1,a+1);
      myTable.add(chatLink,1,a+1);
    }
    table.add(myTable);

    return table;
  }

  protected HeaderTable getGolfLinks() {
    HeaderTable table = new HeaderTable();
    table.setBorderColor("#8ab490");
    table.setHeadlineSize(1);
    table.setHeadlineColor("#FFFFFF");
    table.setHeadlineLeft();
    table.setWidth(148);
    table.setHeaderText(iwrb.getLocalizedString("links","Links"));

    Table myTable = new Table(1,2);
    myTable.setAlignment(1,1,"center");
    myTable.setAlignment(1,2,"center");
    myTable.setCellpadding(2);
    myTable.setCellspacing(2);
    myTable.setHeight("95");
    myTable.setWidth("100%");

    Image europeant = iwrb.getImage("banners/europeantour.gif");
    europeant.setWidth(69);
    europeant.setHeight(47);


    Link europeantour = new Link(europeant,"http://www.europeantour.com");
    europeantour.setTarget("_new");

    Image pgaimg = iwrb.getImage("banners/pgatour.gif");
    pgaimg.setWidth(59);
    pgaimg.setHeight(80);

    Link pgatour = new Link(pgaimg,"http://www.pgatour.com");
    pgatour.setTarget("_new");

    myTable.add(europeantour,1,1);
    myTable.add(pgatour,1,2);

    table.add(myTable);
    return table;
  }

  protected HeaderTable clubNews() throws SQLException {
    News[] news = (News[]) (new News()).findAll("select distinct news_category_id from news where news_category_id>3 and news_category_id != 226 order by news_date desc");

    HeaderTable headerTable = new HeaderTable();
    headerTable.setWidth(148);
    headerTable.setBorderColor("#8ab490");
    headerTable.setHeaderText(iwrb.getLocalizedString("clubNews","Club news"));
    headerTable.setHeadlineSize(1);
    headerTable.setRightHeader(false);
    headerTable.setHeadlineAlign("left");

    Table myTable = new Table(1,5);
    myTable.setWidth("100%");
    myTable.setCellpadding(2);
    myTable.setCellspacing(2);
    myTable.setBorder(0);

    for (int a = 0; a < 5; a++) {
      if (news.length > a) {
        News[] clubNews = (News[]) (new News()).findAllByColumnOrdered("news_category_id",""+news[a].getNewsCategoryId(),"news_date desc");
        Text unionText = new Text();
        unionText.setFontSize(1);
        unionText.setFontColor("#666666");

        NewsCategoryAttributes[] newsAttribute = (NewsCategoryAttributes[]) (new NewsCategoryAttributes()).findAllByColumn("news_category_id",clubNews[0].getNewsCategoryId());

        int union_id = 0;

        if (newsAttribute.length > 0) {
          union_id = newsAttribute[0].getAttributeId();
          Union union = new Union(union_id);
          unionText.addToText(union.getAbbrevation()+" - ");
        }

        idegaTimestamp stampur = new idegaTimestamp(clubNews[0].getDate());

        String minutes = stampur.getMinute()+"";
        if (stampur.getMinute() < 10) {
          minutes = "0" + stampur.getMinute();
        }

        Text newsDate = new Text(stampur.getDate()+"/"+stampur.getMonth()+"/"+stampur.getYear()+" "+stampur.getHour()+":"+minutes);
        newsDate.setFontSize(1);
        newsDate.setFontColor("#666666");

            Link newsLink = new Link(clubNews[0].getHeadline(),"/clubs/index2.jsp");
              newsLink.addParameter("union_id",""+union_id);
              newsLink.setFontSize(1);

            myTable.add(unionText,1,a+1);
            myTable.add(newsDate,1,a+1);
            myTable.addBreak(1,a+1);
            myTable.add(newsLink,1,a+1);
          }
        }

        headerTable.add(myTable);

        return headerTable;
      }

      protected Table idega(){
          Table idegaTable = new Table (1,1);
          idegaTable.setCellpadding(0);
          idegaTable.setCellspacing(0);
          idegaTable.setAlignment(1,1, "center");
          idegaTable.setWidth(148);

          Link idegaLink = new Link(iwrb.getImage("banners/idegalogo.gif"), "http://www.idega.is");

          idegaLink.setTarget("_blank");
          idegaTable.add(idegaLink, 1, 1);

          return idegaTable;
      }



		public BoxReader getLinks(ModuleInfo modinfo){

			BoxReader box_office= new BoxReader("1",isAdmin(modinfo),3);
				box_office.setBoxBorder(0);
				box_office.setInnerBoxBorder(0);
				box_office.setBoxWidth(148);
				box_office.setNoIcons(true);
				box_office.setBoxOutline(1);
				box_office.setOutlineColor("8ab490");
				box_office.setInBoxColor("FFFFFF");
				box_office.setNumberOfDisplayed(4);
				box_office.setNumberOfLetters(60);
				box_office.setLeftBoxWidth(0);
				box_office.setRightBoxWidth(0);
				box_office.setBoxTableColor("8ab490");
				box_office.setTableAlignment("left");
				box_office.setBoxPadding(1);
				box_office.setBoxOnly(true);
				box_office.setShowCategoryHeadline(true);
				box_office.setBoxCategoryHeadlineSize(1);
				box_office.setBoxCategoryHeadlineColor("#FFFFFF");
				box_office.setRightHeader(false);
                                box_office.setHeadlineAlign("left");

			return box_office;
		}


        protected Login getLogin(){
          return new Login();
        }

        protected HeaderTable getGSIAssociates() {

           HeaderTable table = new HeaderTable();
            table.setBorderColor("#8ab490");
            table.setHeadlineSize(1);
            table.setHeadlineColor("#FFFFFF");
            table.setHeadlineLeft();
            table.setWidth(148);
            table.setHeaderText(iwrb.getLocalizedString("arePartOf","GSI is part of"));



              com.idega.jmodule.object.Image logo1 = iwrb.getImage("banners/WAGC.gif");
              com.idega.jmodule.object.Image logo2 = iwrb.getImage("banners/EGA.gif");
              com.idega.jmodule.object.Image logo3 = iwrb.getImage("banners/RACrest2.gif");


              Link logo1Link = new Link(logo1,"http://www.wagc.org/");
                        logo1Link.setTarget("_new");
              Link logo2Link = new Link(logo2,"http://www.ega-golf.ch/");
                        logo2Link.setTarget("_new");
              Link logo3Link = new Link(logo3,"http://www.randa.org/");
                        logo3Link.setTarget("_new");


              table.add(logo1Link);
              table.add("<br>");
              table.add(logo2Link);
              table.add("<br>");
              table.add(logo3Link);


            return table;



        }

        protected HeaderTable getPollVoter(){
      BasicPollVoter poll = new BasicPollVoter("/poll/results.jsp",true);
        poll.setConnectionAttributes("union_id",3);
        poll.setHeaderColor("#8ab490");
        poll.setColor1("#FFFFFF");
        poll.setHeadlineColor("#FFFFFF");
	poll.setHeadlineSize(1);
        poll.setNumberOfShownPolls(3);
        poll.setHeadlineLeft();
        poll.setAdminButtonURL("/pollmanager.gif");


        HeaderTable pollTable = new HeaderTable();
            pollTable.setBorderColor("#8ab490");
            pollTable.setHeadlineSize(1);
            pollTable.setHeadlineColor("#FFFFFF");
            pollTable.setHeadlineLeft();
            pollTable.setWidth(148);
            pollTable.setHeaderText(iwrb.getLocalizedString("questionOfTheDay","Question of the day"));

        pollTable.add(poll);

          return pollTable;
       }


        protected Table Center(){
          return centerTable;
        }

        protected void initCenter(){

          centerTable = new Table(1,1);
          centerTable.setWidth("100%");
          centerTable.setHeight("100%");
          centerTable.setCellpadding(0);
          centerTable.setCellspacing(0);
          centerTable.setAlignment(1,1, "center");
          setVerticalAlignment( "top" );
        }


        protected Table Right(ModuleInfo modinfo) throws SQLException,IOException{
          Table rightTable = new Table(1,10);
          rightTable.setWidth("" + RIGHTWIDTH);
          rightTable.setCellpadding(0);
          rightTable.setCellspacing(0);

          rightTable.setVerticalAlignment(1,1,"top");
          rightTable.setVerticalAlignment(1,2,"top");
          rightTable.setVerticalAlignment(1,3,"top");
          rightTable.setVerticalAlignment(1,4,"top");
          rightTable.setVerticalAlignment(1,5,"top");
          rightTable.setVerticalAlignment(1,6,"top");
          rightTable.setVerticalAlignment(1,7,"top");
          rightTable.setVerticalAlignment(1,8,"top");
          rightTable.setVerticalAlignment(1,9,"top");
          rightTable.setVerticalAlignment(1,10,"top");

          rightTable.setColumnAlignment(1, "center");

          rightTable.add(new Flash("http://clarke.idega.is/golfnews.swt?text="+java.net.URLEncoder.encode(iwrb.getLocalizedString("template.international_golf_news","International golf news")),148,288),1,1);
          rightTable.add( getPollVoter() ,1,3);

          rightTable.add( getGSIAssociates(),1,5);
          rightTable.add(getGolfLinks(),1,7);
          rightTable.add(getYellowLine(),1,9);


          return rightTable;
        }


      private Form getYellowLine() {

        Form myForm = new Form("http://www.gulalinan.is/leit.asp","get");
          myForm.setAttribute("target","_blank");
          myForm.setName("Search");

        Table myTable = new Table(1,3);
          myTable.setWidth(120);
          myTable.setHeight(70);
          myTable.setCellpadding(0);
          myTable.setCellspacing(0);

        Image rammiUppi = new Image("http://www.gulalinan.is/gulleit/img/lg120/rammi_uppi120.gif","",120,6);
          myTable.add(rammiUppi,1,1);
        Image rammiNidri = new Image("http://www.gulalinan.is/gulleit/img/lg120/rammi_nidri120.gif","",120,8);
          myTable.add(rammiNidri,1,3);

        Table innerTable = new Table(1,3);
          innerTable.setWidth(120);
          innerTable.setHeight("100%");
          innerTable.setCellpadding(0);
          innerTable.setCellspacing(0);
          innerTable.setAlignment(1,1,"center");
          innerTable.setAlignment(1,2,"center");
          innerTable.setAlignment(1,3,"right");
          innerTable.setBackgroundImage(new Image("http://www.gulalinan.is/gulleit/img/lg120/bakgrunnurx120.gif"));

        Image searchImage = new Image("http://www.gulalinan.is/gulleit/img/lg120/gulalinanlogo.gif","",67,12);
        Link yellowLink = new Link(searchImage,"http://www.gulalinan.is");
          yellowLink.setTarget("_blank");

        TextInput textInput = new TextInput("kwd");
          textInput.setLength(12);

        HiddenInput hidden = new HiddenInput("ac","ks");

        Image submitImage = new Image("http://www.gulalinan.is/gulleit/img/lg120/leita.gif","Leita",39,13);
        SubmitButton submit = new SubmitButton(submitImage,"image1");
          submit.setAttribute("hspace","5");

        innerTable.add(yellowLink,1,1);
        innerTable.add(textInput,1,2);
        innerTable.add(submit,1,3);

        myTable.add(innerTable,1,2);
        myForm.add(myTable);
        myForm.add(hidden);

        return myForm;

      }


      protected Table golfHeader(){
          Table golfHeader = new Table(1,2);

          Text zero = new Text("");
          zero.setFontSize("1");

          golfHeader.add(zero ,1,2);

          Table linkTable = new Table(8,1);


          golfHeader.setHeight( 1, "68");
          golfHeader.setHeight( 2, "16");
          setTopHeight("84");
          golfHeader.setWidth("720");
          golfHeader.setCellpadding(0);
          golfHeader.setCellspacing(0);

        Image banBg = iwrb.getImage("/mainpage/banner.gif");
         banBg.setWidth(720);
         banBg.setHeight(68);

         golfHeader.setBackgroundImage(1, 1, banBg);
          golfHeader.setVerticalAlignment(1,2, "top");

          linkTable.setHeight("14");
          linkTable.setCellpadding(0);
          linkTable.setCellspacing(0);
          linkTable.setWidth("720");
          linkTable.setVerticalAlignment("top");
          linkTable.setRowVerticalAlignment(1,"top");

          //Set inn linka
          Image clubImage = iwrb.getImage("/mainpage/clubs.gif");
         // clubImage.setWidth(101);
          //clubImage.setHeight(15);
         Link club = new Link(clubImage, "/clubs/");
          linkTable.add(club, 1, 1);

         Image startingtimesImage = iwrb.getImage("/mainpage/teetimes.gif");
         // startingtimesImage.setWidth(85);
          //startingtimesImage.setHeight(15);
         Link startingtimes = new Link(startingtimesImage, "/start/search.jsp");
         linkTable.add(startingtimes, 2, 1);

         Image handicapImage = iwrb.getImage("/mainpage/handicap.gif");
          //handicapImage.setWidth(85);
          //handicapImage.setHeight(15);
         Link handicap = new Link(handicapImage, "/handicap/");
           linkTable.add(handicap, 3, 1);

        Image modtaskraImage = iwrb.getImage("/mainpage/tournaments.gif");
          //modtaskraImage.setWidth(85);
          //modtaskraImage.setHeight(15);
        Link motaskra = new Link(modtaskraImage, "/tournament/");
          linkTable.add(motaskra, 4, 1);

          Image umGSIImage = iwrb.getImage("/mainpage/aboutgsi.gif");
          //umGSIImage.setWidth(73);
          //umGSIImage.setHeight(15);
          Link umGSI = new Link(umGSIImage, "/gsi/index.jsp");
          linkTable.add(umGSI, 5, 1);

          Image spjallidImage = iwrb.getImage("/mainpage/forums.gif");
          //spjallidImage.setWidth(85);
          //spjallidImage.setHeight(15);
          Link spjallid = new Link(spjallidImage, "/forum/index.jsp");
          linkTable.add(spjallid, 6, 1);

          Image indexImage = iwrb.getImage("/mainpage/home.gif");
          //indexImage.setWidth(85);
          //indexImage.setHeight(15);
          Link index = new Link(indexImage, "/index.jsp");
          linkTable.add(index, 7, 1);

          Image endImage = iwrb.getImage("/mainpage/linkend.gif");
          //endImage.setWidth(121);
          //endImage.setHeight(15);
          linkTable.add( endImage, 8, 1 );

          golfHeader.setAlignment(1,2, "top");
          golfHeader.add(linkTable, 1, 2 );

          return golfHeader;
      }


      protected Table golfFooter(){
          Table golfFooter = new Table(6,1);
          golfFooter.setHeight("21");
          setBottomHeight( "21");
          golfFooter.setWidth("720");
          golfFooter.setCellpadding(0);
          golfFooter.setCellspacing(0);

          golfFooter.add(iwrb.getImage("/mainpage/bottom1.gif"),1,1);
          golfFooter.add(new Link (iwrb.getImage("/mainpage/bottom2.gif"), "/index.jsp"),2,1);
          golfFooter.add(iwrb.getImage("/mainpage/bottom3.gif"),3,1);
          Image back = iwrb.getImage("/mainpage/bottom4.gif");
          back.setAttribute("OnClick", "history.go(-1)");
          golfFooter.add(back,4,1);
          golfFooter.add(iwrb.getImage("/mainpage/bottom5.gif"),5,1);
          golfFooter.add(new Link (iwrb.getImage("/mainpage/bottom6.gif"), "mailto: golf@idega.is"),6,1);

          return golfFooter;
      }


      protected Table getHBanner(ModuleInfo modinfo) throws SQLException{
          Table bannerTable = new Table(1,1);
          bannerTable.setAlignment("center");
          bannerTable.setAlignment(1,1,"middle");
          bannerTable.setCellpadding(10);
          bannerTable.setCellspacing(0);


 		InsertBanner ib = new InsertBanner(3, isAdmin(modinfo));
                  ib.setAdminButtonURL("/pics/jmodules/banner/bannerstjori.gif");
		bannerTable.add(ib,1,1);
          return bannerTable;
      }

      protected HeaderTable Sponsors() throws IOException{

          HeaderTable table = new HeaderTable();
            table.setBorderColor("#8ab490");
            table.setHeadlineSize(1);
            table.setHeadlineColor("#FFFFFF");
            table.setRightHeader(false);
            table.setHeadlineAlign("left");
            table.setWidth(148);
            table.setHeaderText(iwrb.getLocalizedString("associates","Associates"));

                Table innerTable = new Table(1,9);
                  innerTable.setWidth("100%");
                  innerTable.setColumnAlignment(1,"center");

      Link one = new Link(iwrb.getImage("/banners/sjova.gif"),"http://www.sjova.is");
			Link two = new Link(iwrb.getImage("/banners/isbank.gif"),"http://www.isbank.is");
			Link four = new Link(iwrb.getImage("/banners/toyota.gif"),"http://www.toyota.is");
			Link six = new Link(iwrb.getImage("/banners/samvinn.gif"),"http://www.samvinn.is");
			Link five = new Link(iwrb.getImage("/banners/ecco.gif"),"http://www.ecco.com");
			Link three = new Link(iwrb.getImage("/banners/opinkerfi.gif"),"http://www.ok.is");
			Link seven = new Link(iwrb.getImage("/banners/euro.gif"),"http://www.europay.is");
			Link eight = new Link(iwrb.getImage("/banners/syn.gif"),"http://www.syn.is");
			Link nine = new Link(iwrb.getImage("/banners/golfcard.gif"),"http://www.europay.is/form/kort.htm");

			one.setTarget("_blank");
			two.setTarget("_blank");
			three.setTarget("_blank");
			four.setTarget("_blank");
			five.setTarget("_blank");
			six.setTarget("_blank");
			seven.setTarget("_blank");
			eight.setTarget("_blank");
			nine.setTarget("_blank");

			innerTable.add(one,1,1);
			innerTable.add(two,1,2);
			innerTable.add(three,1,3);
			innerTable.add(four,1,4);
			innerTable.add(five,1,5);
			innerTable.add(six,1,6);
			innerTable.add(seven,1,7);
			innerTable.add(eight,1,8);
			innerTable.add(nine,1,9);

                table.add(innerTable);


         return table;
      }

  // ###########  Public - Föll

  public void setVerticalAlignment(String alignment) {
    centerTable.setVerticalAlignment(alignment);
    centerTable.setVerticalAlignment(1,1,alignment);
  }


	public void add(ModuleObject objectToAdd) {
  	  Center().add(objectToAdd,1,1);
        }




  public void removeUnionIdSessionAttribute(ModuleInfo modinfo){
    modinfo.removeSessionAttribute("golf_union_id");
  }

  public String getUnionID(ModuleInfo modinfo){
    return (String)modinfo.getSessionAttribute("golf_union_id");
  }

  public void setUnionID(ModuleInfo modinfo, String union_id){
    modinfo.setSessionAttribute("golf_union_id", union_id);
  }


  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }



  public Member getMember(ModuleInfo modinfo){
          return (Member)modinfo.getSession().getAttribute("member_login");
  }

  public boolean isAdmin(ModuleInfo modinfo) {
    try {
      return com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException E) {
    }
    catch (Exception E) {
		  E.printStackTrace();
	  }
    finally {
	  }

    return false;
  }

  public boolean isDeveloper(ModuleInfo modinfo) {
    return com.idega.jmodule.login.business.AccessControl.isDeveloper(modinfo);
  }

  public boolean isClubAdmin(ModuleInfo modinfo) {
    return com.idega.jmodule.login.business.AccessControl.isClubAdmin(modinfo);
  }

  public boolean isClubWorker(ModuleInfo modinfo) {
    boolean ret;

    try {
      ret = com.idega.jmodule.login.business.AccessControl.isClubWorker(modinfo);
    }
    catch(java.sql.SQLException e) {
      e.printStackTrace();
      ret = false;
    }

    return(ret);
  }

  public boolean isUser(ModuleInfo modinfo) {
    return com.idega.jmodule.login.business.AccessControl.isUser(modinfo);
  }





  public void main(ModuleInfo modinfo) throws Exception {
    isAdmin = isAdmin(modinfo);

    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);

    setLinkColor(iwb.getProperty("link_color","black"));
    setVlinkColor(iwb.getProperty("vlink_color","black"));
    setHoverColor(iwb.getProperty("hover_link_color","#8ab490"));

    try {
     User(modinfo);
    }
    catch(SQLException E) {
    }
  	catch (IOException E) {
    }
  }


}

/*
 * $Id: GolfMainJSPModule.java,v 1.11 2001/05/22 19:30:13 haffi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Strengur hf.
 * Use is subject to license terms.
 *
 */
package com.idega.projects.golf.templates;

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

/**
 * @author
 */
public class GolfMainJSPModule extends MainSideJSPModule {
  protected Login login;
  protected Table centerTable;
  protected String align;

  protected final int SIDEWIDTH = 720;
  protected final int LEFTWIDTH = 163;
  protected final int RIGHTWIDTH = 148;
  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public void initializePage() {
    super.initializePage();
    ModuleInfo modinfo = getModuleInfo();
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);

    String linkColor = iwb.getProperty("link_color");
    if( linkColor == null ) linkColor = "black";

    String vLinkColor = iwb.getProperty("vlink_color");
    if( vLinkColor == null ) vLinkColor = "black";

    String hoverLinkColor = iwb.getProperty("hover_link_color");
    if( hoverLinkColor == null ) hoverLinkColor = "#8ab490";

    setLinkColor(linkColor);
    setVlinkColor(vLinkColor);
    setHoverColor(hoverLinkColor);

    try {
     User();
    }
    catch(SQLException E) {
    }
  	catch (IOException E) {
    }
  }

  protected void User() throws SQLException, IOException {
    getPage().setTextDecoration("none");
    setTopMargin(5);
    add("top", golfHeader());
    add("top", Top());
    add("bottom", golfFooter());
    add(Left(), Center(), Right());
	  setWidth(1, "" + LEFTWIDTH);
	  setContentWidth( "100%");
	  setWidth(3, "" + RIGHTWIDTH);
  }

  protected Table Top() throws SQLException,IOException{
    Table topTable = new Table(3,1);
    topTable.setCellpadding(0);
    topTable.setCellspacing(0);
    topTable.setHeight("90");
    topTable.add(getLogin(),1,1);
    topTable.add(getHBanner(),2,1);
    topTable.add(new Image("/pics/templates/toyotalogo.gif"),3,1);

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

	protected Table AnnouncementsModule() throws SQLException, IOException {
	  Table ModuleTable = new Table(1,3);
    ModuleTable.setColor(1,2,"#99CC99");
    ModuleTable.setCellpadding(1);
    ModuleTable.setCellspacing(0);
    ModuleTable.setWidth(148);
    ModuleTable.setBackgroundImage(1,1, new Image("/pics/news/tilkynningar_banner.gif"));

		Connection conn = getConnection();
/*	  Announcement announcement = new Announcement(1,conn);
	  announcement.setLinkCellWidth("146");
    announcement.setLinkCellHeight("22");

	  Table announcementTable = announcement.getAnnouncementTable();
	  ModuleTable.add(announcementTable, 1,2);
*/
	  freeConnection(conn);
    return ModuleTable;
	}

  protected Table Left() throws SQLException, IOException {
    Table leftTable = new Table(1,12);
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
    leftTable.setHeight("100%");
    leftTable.setColumnAlignment(1, "left");
    leftTable.setWidth("" + LEFTWIDTH);
    leftTable.setCellpadding(0);
    leftTable.setCellspacing(0);
    leftTable.add( Sponsors(), 1,1);
    leftTable.add(clubNews(),1,3);
    leftTable.add(new TournamentBox(),1,5);
    leftTable.add(getChat(),1,7);
    leftTable.add(getLinks(),1,9);
    leftTable.add(idega(),1,11);
    //leftTable.add( getPollVoter() ,1,6);

    return leftTable;
  }

  protected HeaderTable getChat() throws SQLException {
    HeaderTable table = new HeaderTable();
    table.setBorderColor("#8ab490");
    table.setHeadlineSize(1);
    table.setHeadlineColor("#FFFFFF");
    table.setRightHeader(false);
    table.setHeadlineAlign("left");
    table.setWidth(148);
    table.setHeaderText("Spjallið");

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
    table.setHeaderText("Áhugaverðir tenglar");

    Table myTable = new Table(1,2);
    myTable.setAlignment(1,1,"center");
    myTable.setAlignment(1,2,"center");
    myTable.setCellpadding(2);
    myTable.setCellspacing(2);
    myTable.setHeight("95");
    myTable.setWidth("100%");

    Link europeantour = new Link(new Image("/pics/europeantour.gif","European Tour",69,47),"http://www.europeantour.com");
    europeantour.setTarget("_new");
    Link pgatour = new Link(new Image("/pics/pgatour.gif","PGA Tour",59,80),"http://www.pgatour.com");
    pgatour.setTarget("_new");

    myTable.add(europeantour,1,1);
    myTable.add(pgatour,1,2);

    table.add(myTable);
    return table;
  }

  protected HeaderTable clubNews() throws SQLException {
    News[] news = (News[]) (new News()).findAll("select distinct news_category_id from news where news_category_id>3 order by news_date desc");

    HeaderTable headerTable = new HeaderTable();
    headerTable.setWidth(148);
    headerTable.setBorderColor("#8ab490");
    headerTable.setHeaderText("Klúbbafréttir");
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

          Link idegaLink = new Link(new Image("/pics/templates/idegalogo.gif", "idega"), "http://www.idega.is");
          idegaLink.setTarget("_blank");
          idegaTable.add(idegaLink, 1, 1);

          return idegaTable;
      }



		public BoxReader getLinks(){
			/*LinkGroup group = new LinkGroup("#99CC99",new Image("/pics/templates/tenglar.gif"));
			group.addLink("Samvinnuferða Landsýnarmótið - staða","http://www.oddur.is/toyota/toyota.htm");
						//excel skjöl
			group.addLink("Toyota Mótaröðinni: Karlar (excel)","/files/TOYOTAkarlar.xls");
			group.addLink("Toyota Mótaröðinni: Konur (excel)","/files/TOYOTAkonur.xls");

			group.addLink("Stigamót unglinga 2000: Piltar (excel)","/files/Piltar.xls");
			group.addLink("Stigamót unglinga 2000: Stúlkur (excel)","/files/Stulkur.xls");
			group.addLink("Stigamót unglinga 2000: Drengir (excel)","/files/Drengir.xls");
			return group;*/

			BoxReader box_office= new BoxReader("1",isAdmin(),3);
				box_office.setBoxBorder(0);
				box_office.setInnerBoxBorder(0);
				box_office.setBoxWidth(148);
				box_office.setNoIcons(true);
				//box_office.setHeaderImage(new Image("/pics/uppl.gif"));
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
        /*
          if (getServletContext().getAttribute("login_module") != null)
            return (Login)getServletContext().getAttribute("login_module");
          else{
            Login log = new Login();
            getServletContext().setAttribute("login_module", log);
            return log;
            }
           */
        }

        protected HeaderTable getGSIAssociates() {

           HeaderTable table = new HeaderTable();
            table.setBorderColor("#8ab490");
            table.setHeadlineSize(1);
            table.setHeadlineColor("#FFFFFF");
            table.setHeadlineLeft();
            table.setWidth(148);
            table.setHeaderText("GSÍ er aðili að");



              com.idega.jmodule.object.Image logo1 = new com.idega.jmodule.object.Image("/pics/templates/associates/WAGC.gif");
              com.idega.jmodule.object.Image logo2 = new com.idega.jmodule.object.Image("/pics/templates/associates/EGA.gif");
              com.idega.jmodule.object.Image logo3 = new com.idega.jmodule.object.Image("/pics/templates/associates/RACrest2.gif");


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
//        poll.setColor1("#d6f1d6");
        poll.setColor1("#FFFFFF");
        poll.setHeadlineColor("#FFFFFF");
	poll.setHeadlineSize(1);
        poll.setNumberOfShownPolls(3);
        poll.setHeadlineLeft();
        poll.setAdminButtonURL("/pics/poll/pollstjori.gif");
        poll.setSubmitButtonURL("/pics/formtakks/kjosa.gif");
        poll.setOtherPollsImage(new Image("/pics/formtakks/fyrrikannanir.gif","Fyrri kannanir"));

        HeaderTable pollTable = new HeaderTable();
            pollTable.setBorderColor("#8ab490");
            pollTable.setHeadlineSize(1);
            pollTable.setHeadlineColor("#FFFFFF");
            pollTable.setHeadlineLeft();
            pollTable.setWidth(148);
            pollTable.setHeaderText("Spurning dagsins");

        pollTable.add(poll);

          return pollTable;
        /*
          if (isAdmin()){
            if (getServletContext().getAttribute("admin_pollvoter_module") != null){
              return (PollVoter)getServletContext().getAttribute("admin_pollvoter_module");
            } else{
                PollVoter poll = new PollVoter("/poll/results.jsp",true);
                getServletContext().setAttribute("admin_pollvoter_module", poll);
                return poll;
              }
          }else{
              if (getServletContext().getAttribute("pollvoter_module") != null){
               return (PollVoter)getServletContext().getAttribute("pollvoter_module");
              } else{
                 PollVoter poll2 = new PollVoter("/poll/results.jsp",false);
                 getServletContext().setAttribute("pollvoter_module", poll2);
                 return poll2;
               }
           }
           */
        }




        protected Table Center() throws SQLException,IOException{

			if (centerTable == null){
          centerTable = new Table(1,1);
          //centerTable.setBorder(1);

          centerTable.setWidth("100%");
          centerTable.setHeight("100%");
          centerTable.setCellpadding(0);
          centerTable.setCellspacing(0);
//          centerTable.setVerticalAlignment(1,1,"middle");
          centerTable.setAlignment(1,1, "center");
//          centerTable.setAlignment(1,2, "center");
//          centerTable.add(getHBanner(),1,1);

          setVerticalAlignment( "top" );
          }
          return centerTable;
        }


        protected Table Right() throws SQLException,IOException{
	        //getModuleInfo().getSession().setAttribute("union_id","3");
          Table rightTable = new Table(1,10);
          rightTable.setWidth("" + RIGHTWIDTH);
//          rightTable.setHeight("100%");
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

          //
//         rightTable.add(new Image("/pics/templates/toyotalogo.gif"),1,1);
          //rightTable.add(new Image("/pics/templates/right2.gif","right"), 1,1);
            rightTable.add(new Flash("http://clarke.idega.is/golfnews.swt",148,288),1,1);
          //poll
         // rightTable.add(new Image("/pics/templates/samstarf.gif"),1,5);
//		 rightTable.add(Sponsors(),1,5);

//          rightTable.setHeight(3,"40");
/*			Table pollBackTable = new Table(1,2);
				pollBackTable.setWidth(RIGHTWIDTH);
				pollBackTable.setBorder(0);
				pollBackTable.setCellpadding(0);
				pollBackTable.setCellspacing(0);
				pollBackTable.setHeight(1,"22");
				pollBackTable.setBackgroundImage(1,1,new Image("/pics/templates/pollHaegri.gif"));
				pollBackTable.setColor(1,2,"#99CC99");
				pollBackTable.setAlignment(1,2,"center");
  */       rightTable.add( getPollVoter() ,1,3);
//		  rightTable.add("&nbsp;",1,3);
//          rightTable.add(idega(), 1, 6);

          rightTable.add( getGSIAssociates(),1,5);
          rightTable.add(getGolfLinks(),1,7);
          rightTable.add(getYellowLine(),1,9);
	  //rightTable.add(new PollVoter("/poll/results.jsp"),1,7);

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

//          golfHeader.setBorder(1);
//          linkTable.setBorder(1);

          golfHeader.setHeight( 1, "68");
          golfHeader.setHeight( 2, "16");
          setTopHeight("84");
          golfHeader.setWidth("720");
          golfHeader.setCellpadding(0);
          golfHeader.setCellspacing(0);
          golfHeader.setBackgroundImage(1, 1, new Image("/pics/templates/banner.gif","",720,68));
          golfHeader.setVerticalAlignment(1,2, "top");

          linkTable.setHeight("14");
          linkTable.setCellpadding(0);
          linkTable.setCellspacing(0);
          linkTable.setWidth("720");
          linkTable.setVerticalAlignment("top");
          linkTable.setRowVerticalAlignment(1,"top");

          //Set inn linka
//          Link club = new Link(new Image("/pics/templates/klubbar.gif"), "/clubs");
          Link club = new Link(new Image("/pics/templates/klubbar.gif","Klúbbar",101,15), "/clubs/");
//          Image club = new Image("/pics/templates/klubbar.gif","Klúbbar");
          linkTable.add(club, 1, 1);

         Link startingtimes = new Link(new Image("/pics/templates/rastimar.gif","Rástímar",85,15), "/start/search.jsp");
         // Image startingtimes = new Image("/pics/templates/rastimar.gif","Rástímar");
         linkTable.add(startingtimes, 2, 1);

         Link handicap = new Link(new Image("/pics/templates/forgjof.gif","Forgjöf",85,15), "/handicap/");
     //     Image handicap = new Image("/pics/templates/forgjof.gif","Forgjöf");
          linkTable.add(handicap, 3, 1);

        Link motaskra = new Link(new Image("/pics/templates/motaskra.gif","Mótaskrá",85,15), "/tournament/");
         //Image motaskra = new Image("/pics/templates/motaskra.gif","Mótaskrá");
          linkTable.add(motaskra, 4, 1);

//          Link sidanMin = new Link(new Image("/pics/templates/sidanmin.gif"), "/mypage/");
//          linkTable.add(sidanMin, 5, 1);

          Link umGSI = new Link(new Image("/pics/templates/umgsi.gif","um GSÍ",73,15), "/gsi/index.jsp");
//          umGSI.setTarget("_blank");
          linkTable.add(umGSI, 5, 1);

          Link spjallid = new Link(new Image("/pics/templates/spjallid.gif", "Spjallið",85,15), "/forum/index.jsp");
          linkTable.add(spjallid, 6, 1);

          Link index = new Link(new Image("/pics/templates/forsida.gif","Forsíða",85,15), "/index.jsp");
          linkTable.add(index, 7, 1);

          linkTable.add( new Image("/pics/templates/takkaend2.gif", "",121,15), 8, 1 );

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

          golfFooter.add(new Image("/pics/templates/botn1.gif"),1,1);
          golfFooter.add(new Link (new Image("/pics/templates/botn2.gif", "Heim"), "/index.jsp"),2,1);
          golfFooter.add(new Image("/pics/templates/botn3.gif"),3,1);
          Image back = new Image("/pics/templates/botn4.gif", "til baka");
          back.setAttribute("OnClick", "history.go(-1)");
          golfFooter.add(back,4,1);
          golfFooter.add(new Image("/pics/templates/botn5.gif"),5,1);
          golfFooter.add(new Link (new Image("/pics/templates/botn6.gif", "golf@idega.is"), "mailto: golf@idega.is"),6,1);

          return golfFooter;
      }


      protected Table getHBanner() throws SQLException{
          Table bannerTable = new Table(1,1);
          bannerTable.setAlignment("center");
          bannerTable.setAlignment(1,1,"middle");
          //bannerTable.setHeight(90);
          bannerTable.setCellpadding(10);
          bannerTable.setCellspacing(0);


          //Link Hbanner = new Link(new Image("/pics/templates/auglsjova.gif", "Sjóvá Almennar"), "http://www.sjova.is");
          //Hbanner.setTarget("_blank");
          //bannerTable.add(Hbanner, 1, 1);

/*          Link ISbanner = new Link(new Image("/pics/banners/golfkort.gif", "Íslandsbanki-FBA"), "http://www.isbank.is");
		  ISbanner.setTarget("_blank");
          bannerTable.add(ISbanner, 1, 1);
*/
//	boolean joe = isAdmin();
		InsertBanner ib = new InsertBanner(3, isAdmin());
                  ib.setAdminButtonURL("/pics/jmodules/banner/bannerstjori.gif");
		bannerTable.add(ib,1,1);
/*
			Table linkTable = new Table(3,1);
			linkTable.setHeight("100");
			linkTable.setWidth("100%");
			linkTable.setVerticalAlignment("top");
			.linkTable.setRowAlignment(1,"center");
			linkTable.add(new Link(new Image("/pics/templates/akureyri.gif",""),"http://notendur.centrum.is/~gagolf/landsmot/index.html"),1,1);
			linkTable.add(new Link(new Image("/pics/templates/husavik.gif",""),"http://golfhus.nett.is/landsmot1dagur.htm"),2,1);
			linkTable.add(new Link(new Image("/pics/templates/saudarkroki.gif",""),"http://www.krokur.is/~gss/3flokkur.htm"),3,1);

		  bannerTable.add(linkTable,1,1);
*/
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
            table.setHeaderText("Samstarfsaðilar");

                Table innerTable = new Table(1,7);
                  innerTable.setWidth("100%");
                  innerTable.setColumnAlignment(1,"center");

                        Link one = new Link(new Image("/pics/templates/sjova.gif","Sjóvá Almennar"),"http://www.sjova.is");
			Link two = new Link(new Image("/pics/templates/isbank.gif","Íslandsbanki"),"http://www.isbank.is");
			Link four = new Link(new Image("/pics/templates/toyota.gif","Toyota"),"http://www.toyota.is");
			Link six = new Link(new Image("/pics/templates/samvinn.gif","Samvinnuferðir"),"http://www.samvinn.is");
			Link five = new Link(new Image("/pics/templates/ecco.gif","ecco"),"http://www.ecco.com");
			Link three = new Link(new Image("/pics/templates/opinkerfi.gif","Opin Kerfi"),"http://www.opinkerfi.is");
			Link seven = new Link(new Image("/pics/templates/euro.gif","Eurocard"),"http://www.europay.is");

			one.setTarget("_blank");
			two.setTarget("_blank");
			three.setTarget("_blank");
			four.setTarget("_blank");
			five.setTarget("_blank");
			six.setTarget("_blank");
			seven.setTarget("_blank");


			innerTable.add(one,1,1);
			innerTable.add(two,1,2);
			innerTable.add(three,1,3);
			innerTable.add(four,1,4);
			innerTable.add(five,1,5);
			innerTable.add(six,1,6);
			innerTable.add(seven,1,7);

                table.add(innerTable);


         return table;
      }

  // ###########  Public - Föll

  public void setVerticalAlignment(String alignment) {
    centerTable.setVerticalAlignment(alignment);
    centerTable.setVerticalAlignment(1,1,alignment);
  }


	public void add(ModuleObject objectToAdd) {
		//centerTable.add(objectToAdd,1,2);
		try {
			Center().add(objectToAdd,1,1);
		}
		catch(SQLException ex){
			System.err.println(ex.getMessage());
		}
		catch(IOException ex){
			System.err.println(ex.getMessage());
		}
	}

	public Member getMember(){
		return (Member)getModuleInfo().getSession().getAttribute("member_login");
	}

  public boolean isAdmin() {
    try {
      return com.idega.jmodule.login.business.AccessControl.isAdmin(getModuleInfo());
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

  public boolean isDeveloper() {
    return com.idega.jmodule.login.business.AccessControl.isDeveloper(getModuleInfo());
  }

  public boolean isClubAdmin() {
    return com.idega.jmodule.login.business.AccessControl.isClubAdmin(getModuleInfo());
  }

  public boolean isClubWorker() {
    boolean ret;

    try {
      ret = com.idega.jmodule.login.business.AccessControl.isClubWorker(getModuleInfo());
    }
    catch(java.sql.SQLException e) {
      e.printStackTrace();
      ret = false;
    }

    return(ret);
  }

  public boolean isUser() {
    return com.idega.jmodule.login.business.AccessControl.isUser(getModuleInfo());
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
}

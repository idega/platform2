/*
 * $Id: GolfMainJSPModulePage.java,v 1.5 2004/04/01 15:20:20 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.golf.templates.page;

import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.*;
import is.idega.idegaweb.golf.presentation.*;
import is.idega.idegaweb.golf.tournament.presentation.*;
import is.idega.idegaweb.golf.moduleobject.GolfLogin;
import com.idega.jmodule.*;
import com.idega.jmodule.banner.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.poll.moduleobject.*;
import com.idega.jmodule.boxoffice.presentation.*;
import com.idega.util.*;
import java.sql.*;
import java.io.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.core.localisation.business.LocaleSwitcher;

/**
 * @author Gudmundur idega iceland
 */
public class GolfMainJSPModulePage extends MainPage {
	protected GolfLogin login;
	protected Table centerTable;
	protected String align;

	protected final int SIDEWIDTH = 720;
	protected final int LEFTWIDTH = 163;
	protected final int RIGHTWIDTH = 148;
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.idegaweb.golf";
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;

	protected boolean isAdmin = false;

	//temporary solutions for golfdemo.sidan.is
	protected boolean isDemoSite = false;
	protected final static String GOLF_DEMO_SITE = "GOLF_DEMO_VERSION";

	public GolfMainJSPModulePage() {
		super();
		initCenter();
	}

	protected void User(IWContext modinfo) throws SQLException, IOException {
		this.setTextDecoration("none");
		setTopMargin(5);
		add("top", golfHeader());
		add("top", Top(modinfo));
		add("bottom", golfFooter());

//		add(new Table(), Center(), new Table());

		if (!isDemoSite) {
			add(Left(modinfo), Center(), Right(modinfo));
		}
		else {
			add(LeftDemoSite(modinfo), Center(), RightDemoSite(modinfo));
		}

		setWidth(1, Integer.toString(LEFTWIDTH));
		setContentWidth("100%");
		setWidth(3, Integer.toString(RIGHTWIDTH));
	}

	protected Table Top(IWContext modinfo) throws SQLException, IOException {
		Table topTable = new Table(3, 1);
		topTable.setCellpadding(0);
		topTable.setCellspacing(0);
		topTable.setHeight("90");
		topTable.add(getLogin(), 1, 1);
		topTable.add(getHBanner(modinfo), 2, 1);

		topTable.add(getCardView(), 3, 1); // debug golfkort
		//topTable.add(getHoleView(),3,1);//debug landsmot
		//topTable.add(iwrb.getImage("/banners/small_ad.gif"),3,1);

		topTable.setAlignment(2, 1, "center");
		topTable.setAlignment(3, 1, "center");
		topTable.setVerticalAlignment(1, 1, "top");
		topTable.setVerticalAlignment(2, 1, "middle");
		topTable.setVerticalAlignment(3, 1, "middle");

		topTable.setWidth(1, Integer.toString(LEFTWIDTH));
		topTable.setWidth("100%");
		topTable.setWidth(3, Integer.toString(RIGHTWIDTH));

		return topTable;
	}

	protected Table Left(IWContext modinfo) throws SQLException, IOException {
		Table leftTable = new Table(1, 27);
		//leftTable.setBorder(1);
		leftTable.setVerticalAlignment("top");
		leftTable.setVerticalAlignment(1, 1, "top");
		leftTable.setVerticalAlignment(1, 2, "top");
		leftTable.setVerticalAlignment(1, 3, "top");
		leftTable.setVerticalAlignment(1, 4, "top");
		leftTable.setVerticalAlignment(1, 5, "top");
		leftTable.setVerticalAlignment(1, 6, "top");
		leftTable.setVerticalAlignment(1, 7, "top");
		leftTable.setVerticalAlignment(1, 8, "top");
		leftTable.setVerticalAlignment(1, 9, "top");
		leftTable.setVerticalAlignment(1, 10, "top");
		leftTable.setVerticalAlignment(1, 11, "top");
		leftTable.setVerticalAlignment(1, 13, "top");
		leftTable.setVerticalAlignment(1, 15, "top");
		leftTable.setVerticalAlignment(1, 17, "top");
		leftTable.setVerticalAlignment(1, 19, "top");
		leftTable.setVerticalAlignment(1, 21, "top");
		leftTable.setVerticalAlignment(1, 23, "top");
		leftTable.setVerticalAlignment(1, 25, "top");
		leftTable.setVerticalAlignment(1, 27, "top");
		//leftTable.setHeight("100%");
		leftTable.setColumnAlignment(1, "left");
		leftTable.setWidth(LEFTWIDTH);
		leftTable.setCellpadding(0);
		leftTable.setCellspacing(0);

		leftTable.setAlignment(1, 1, "center");
		leftTable.add(Languages(), 1, 1);

		//    HeaderTable amateur = amateur();

		HeaderTable mainSponsorBox = mainSponsor();
		HeaderTable register = register();
		HeaderTable sponsorBox = sponsors();

		Link mbl = new Link(iwrb.getImage("/banners/mbl_golf.gif"), "http://www.mbl.is/sport/golf/");
		mbl.setTarget("_blank");
		leftTable.add(mbl, 1, 3);

/*		Link kbbanki = new Link(iwrb.getImage("/banners/KB_banki_3.jpg"), "http://www.kbbanki.is");
		kbbanki.setTarget("_blank");
		leftTable.add(kbbanki, 1, 5);
	*/	
//		Link ie = new Link(iwrb.getImage("/banners/brosandi144x144.gif"), "http://www.icelandair.is/main/view.jsp?branch=3308585");
//		ie.setTarget("_blank");
//		leftTable.add(ie, 1, 5);
/*		
		Link mi = new Link(iwrb.getImage("/banners/144x144-fyrirgolf.gif"), "http://www4.mmedia.is/margmidlun/einstaklingar/adsl_afmaelistilbod.asp");
		mi.setTarget("_blank");
		leftTable.add(mi, 1, 5);
*/
//		leftTable.add(JModuleObject.getCacheableObject(mainSponsorBox, "MainSponsorBox", 86400000), 1, 5);
/*
		Link isb = new Link(new Image("/banners/gjaldeyrisGladningur_144x14.gif"),"http://sumarferdir.is/template3.asp?pageid=88");
		isb.setTarget("_blank");
		leftTable.add(isb,1,9);*/

		Link nb = new Link(new Image("/banners/nevada_150_55.gif"), "http://www.nevadabob.is/");
		nb.setTarget("_blank");
		leftTable.add(nb, 1, 5); //1,5
		
//		Link securitas = new Link(new Image("/banners/banner_litill.gif"),"http://www.securitas.is");
//		securitas.setTarget("_blank");
//		leftTable.add(securitas,1,9);
		
		Flash flash = new Flash("/banners/banner-changed.swf");
		flash.setWidth(144);
		flash.setHeight(144);
		Link hBanner = new Link(flash,"http://www.gbferdir.is");
		hBanner.setTarget("_blank");
		leftTable.add(hBanner,1,7);

		//sponsorBox.setCacheable("SponsorBox",86400000);//24 hour
		//    leftTable.add(JModuleObject.getCacheableObject(amateur,"AmateurBox",86400000), 1,3);
		leftTable.add(Block.getCacheableObject(register, "RegisterBox", 86400000), 1, 11);

		/*	Link eyjar = new Link(new Image("/banners/aevint.gif"),"http://bjarnarey.eyjar.is/golf/avintyri/default.htm");
			eyjar.setTarget("_blank");
			leftTable.add(eyjar,1,11);
		*/
		Link titleist = new Link(new Image("/banners/titleist_144x144_ani2.gif"), "http://www.titleist.com");
		titleist.setTarget("_blank");
		leftTable.add(titleist, 1, 13);

		//Landsmót TEMPORARY solution
		IWTimestamp stamp = new IWTimestamp();
		IWTimestamp stampChange = new IWTimestamp(2002, 8, 10, 8, 0, 0);
		IWTimestamp stampEnd = new IWTimestamp(2002, 8, 13, 8, 0, 0);
		IWTimestamp kpmgEnd = new IWTimestamp(2002, 8, 29, 8, 0, 0);

		if (kpmgEnd.isLaterThan(stamp)) {
			Link kpmg = new Link(new Image("/banners/kpmg.gif"), "http://www.kpmg.is");
			leftTable.add(kpmg, 1, 15);
		}

		if (stampChange.isLaterThan(stamp)) {
			Window window = new Window("Íslandsmot", 820, 600, "/tournament/gkview.jsp");
			window.setMenubar(true);
			window.setResizable(true);

			Link link = new Link(iwb.getImage("shared/stada_banner.gif"), window);
			leftTable.addBreak(1, 15);
			leftTable.addBreak(1, 15);
			leftTable.add(link, 1, 15);
		}
		if (stamp.isLaterThan(stampChange) && stampEnd.isLaterThan(stamp)) {
			leftTable.addBreak(1, 15);
			leftTable.addBreak(1, 15);
			leftTable.add(getHoleView(), 1, 15);
		}

		leftTable.add(Block.getCacheableObject(sponsorBox, "SponsorBox", 86400000), 1, 17);

		//HeaderTable newsBox = clubNews();
		//newsBox.setCacheable("NewsBox",3600000);//60*60*1000 1 hour
		//leftTable.add(JModuleObject.getCacheableObject(newsBox,"NewsBox",3600000),1,5);
		leftTable.add(new ClubNewsBox(), 1, 19);

		Link link = new Link(iwrb.getImage("/banners/lengjan.gif"), "http://www.1x2.is");
		link.setTarget("_blank");
		leftTable.add(link, 1, 21);

		TournamentBox tBox = new TournamentBox();
		tBox.setCacheable("TournamentBox", 1800000);
		leftTable.add(tBox, 1, 23);

		///HeaderTable chatBox = getChat();
		//chatBox.setCacheable("ChatBox",3600000);
		//leftTable.add(JModuleObject.getCacheableObject(chatBox,"ChatBox",3600000),1,9);
		ForumBox forums = new ForumBox();
		forums.setLeft(true);

		leftTable.add(forums, 1, 25);

		return leftTable;
	}

	protected Table LeftDemoSite(IWContext modinfo) {
		Table leftTable = new Table(1, 14);
		//leftTable.setBorder(1);
		leftTable.setVerticalAlignment("top");
		leftTable.setVerticalAlignment(1, 1, "top");
		leftTable.setVerticalAlignment(1, 2, "top");
		leftTable.setVerticalAlignment(1, 3, "top");
		leftTable.setVerticalAlignment(1, 4, "top");
		leftTable.setVerticalAlignment(1, 5, "top");
		leftTable.setVerticalAlignment(1, 6, "top");
		leftTable.setVerticalAlignment(1, 7, "top");
		leftTable.setVerticalAlignment(1, 8, "top");
		leftTable.setVerticalAlignment(1, 9, "top");
		leftTable.setVerticalAlignment(1, 10, "top");
		leftTable.setVerticalAlignment(1, 11, "top");
		leftTable.setVerticalAlignment(1, 13, "top");

		leftTable.setColumnAlignment(1, "left");
		leftTable.setWidth(LEFTWIDTH);
		leftTable.setCellpadding(0);
		leftTable.setCellspacing(0);

		leftTable.setAlignment(1, 1, "center");

		leftTable.add(Languages(), 1, 1);

		HeaderTable register = register();
		HeaderTable sponsorBox = sponsorsDemoSite();
		TournamentBox tBox = new TournamentBox();
		Link emLink = new Link(iwb.getImage("shared/eptc_banner.gif"), "http://emboys.golf.is");
		emLink.setTarget("_blank");

		leftTable.add(Block.getCacheableObject(sponsorBox, "SponsorBox", 86400000), 1, 3);
		leftTable.add(Block.getCacheableObject(register, "RegisterBox", 86400000), 1, 5);
		leftTable.add(emLink, 1, 7);
		leftTable.add(new ClubNewsBox(), 1, 9);
		tBox.setCacheable("TournamentBox", 1800000);
		leftTable.add(tBox, 1, 11);

		leftTable.add(idega(), 1, 13);

		return leftTable;
	}

	protected Link getHoleView() {
		Window window = new Window("Hola fyrir holu", 820, 600, "/landsmot.jsp");
		window.setMenubar(true);
		window.setResizable(true);

		Link link = new Link(iwb.getImage("shared/landsmot.gif", 111, 76), window);
		return link;
	}

	protected Link getCardView() {
		/*   Window window = new Window("Golfkort",820,600,"/card");
		    window.setMenubar(true);
		    window.setResizable(true);
		
		    Link link = new Link(iwb.getImage("shared/golfcard.gif"),window);
		    return link;*/
		Link banner = new Link(new Image("/pics/banners/KB_banki_3.jpg",""),"http://www.kbbanki.is/");
//		Link banner = new Link(iwb.getImage("shared/golfcard.gif"), "http://www.bi.is/default.asp?skjal=00,02,06,06&lang=is&menu=00,03,06,06");
		banner.setTarget(Link.TARGET_NEW_WINDOW);
		return banner;
	}

	protected Table Languages() {
		Table languages = new Table(4, 1);
		languages.setAlignment("left");
		Text IS = null;
		Text EN = null;
		Link isLink = null;

		if (isDemoSite) {
			IS = new Text(iwrb.getLocalizedString("languages.swedish", "svenska"));
			isLink = new Link(iwb.getImage("shared/swedish.gif"));
			isLink.addParameter(LocaleSwitcher.languageParameterString, "sv_SE");
		}
		else {
			IS = new Text(iwrb.getLocalizedString("languages.icelandic", "icelandic"));
			isLink = new Link(iwb.getImage("shared/icelandic.gif"));
			isLink.addParameter(LocaleSwitcher.languageParameterString, LocaleSwitcher.icelandicParameterString);
		}

		EN = new Text(iwrb.getLocalizedString("languages.english", "english"));

		IS.setFontSize(Text.FONT_SIZE_7_HTML_1);
		EN.setFontSize(Text.FONT_SIZE_7_HTML_1);

		// vantar link á textann og myndirnar
		// IS.setFontColor("#CCCCCC");
		// EN.setFontColor("#ABABAB");

		isLink.setEventListener(com.idega.core.localisation.business.LocaleSwitcher.class.getName());

		Link enLink = new Link(iwb.getImage("shared/english.gif"));
		enLink.setEventListener(com.idega.core.localisation.business.LocaleSwitcher.class.getName());
		enLink.addParameter(LocaleSwitcher.languageParameterString, LocaleSwitcher.englishParameterString);

		languages.add(isLink, 1, 1);
		languages.add(IS, 2, 1);
		languages.add(enLink, 3, 1);
		languages.add(EN, 4, 1);

		return languages;
	}

	protected HeaderTable getGolfLinks() {
		HeaderTable table = new HeaderTable();
		table.setBorderColor("#8ab490");
		table.setHeadlineSize(1);
		table.setHeadlineColor("#FFFFFF");
		table.setHeadlineLeft();
		table.setWidth(148);
		table.setHeaderText(iwrb.getLocalizedString("links", "Links"));

		Table myTable = new Table(1, 2);
		myTable.setAlignment(1, 1, "center");
		myTable.setAlignment(1, 2, "center");
		myTable.setCellpadding(2);
		myTable.setCellspacing(2);
		myTable.setHeight("95");
		myTable.setWidth("100%");

		Image europeant = iwrb.getImage("banners/europeantour.gif");
		europeant.setWidth(69);
		europeant.setHeight(47);

		Link europeantour = new Link(europeant, "http://www.europeantour.com");
		europeantour.setTarget("_new");

		Image pgaimg = iwrb.getImage("banners/pgatour.gif");
		pgaimg.setWidth(59);
		pgaimg.setHeight(80);

		Link pgatour = new Link(pgaimg, "http://www.pgatour.com");
		pgatour.setTarget("_new");

		myTable.add(europeantour, 1, 1);
		myTable.add(pgatour, 1, 2);

		table.add(myTable);
		return table;
	}

	protected Table idega() {
		Table idegaTable = new Table(1, 1);
		idegaTable.setCellpadding(0);
		idegaTable.setCellspacing(0);
		idegaTable.setAlignment(1, 1, "center");
		idegaTable.setWidth(148);

		Link idegaLink = new Link(iwrb.getImage("banners/idegalogo.gif"), "http://www.idega.is");

		idegaLink.setTarget("_blank");
		idegaTable.add(idegaLink, 1, 1);

		return idegaTable;
	}

	public BoxReader getLinks(IWContext modinfo) {

		BoxReader box_office = new BoxReader("1", isAdmin(modinfo), 3); //bullshit isadmin crap
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
		box_office.setLeftHeader(false);
		box_office.setHeadlineAlign("left");

		return box_office;
	}

	protected GolfLogin getLogin() {
		GolfLogin log = new GolfLogin();
		log.showNewUserImage = false;
		log.setForgotPasswordUrl("/login/loginemailer.jsp");
		return log;
	}

	protected HeaderTable getGSIAssociates() {

		HeaderTable table = new HeaderTable();
		table.setBorderColor("#8ab490");
		table.setHeadlineSize(1);
		table.setHeadlineColor("#FFFFFF");
		table.setHeadlineLeft();
		table.setWidth(148);
		table.setHeaderText(iwrb.getLocalizedString("arePartOf", "GSI is part of"));

		com.idega.presentation.Image logo1 = iwrb.getImage("banners/WAGC.gif");
		com.idega.presentation.Image logo2 = iwrb.getImage("banners/EGA.gif");
		com.idega.presentation.Image logo3 = iwrb.getImage("banners/RACrest2.gif");

		Link logo1Link = new Link(logo1, "http://www.wagc.org/");
		logo1Link.setTarget("_new");
		Link logo2Link = new Link(logo2, "http://www.ega-golf.ch/");
		logo2Link.setTarget("_new");
		Link logo3Link = new Link(logo3, "http://www.randa.org/");
		logo3Link.setTarget("_new");

		table.add(logo1Link);
		table.add(Text.getBreak());
		table.add(logo2Link);
		table.add(Text.getBreak());
		table.add(logo3Link);

		return table;

	}

	protected HeaderTable getPollVoter() {
		BasicPollVoter poll = new BasicPollVoter("/poll/results.jsp", true);
		poll.setConnectionAttributes("union_id", 3);
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
		pollTable.setHeaderText(iwrb.getLocalizedString("questionOfTheDay", "Question of the week"));

		pollTable.add(poll);

		return pollTable;
	}

	protected Table Center() {
		return centerTable;
	}

	protected void initCenter() {

		centerTable = new Table(1, 1);
		centerTable.setWidth("100%");
		centerTable.setHeight("100%");
		centerTable.setCellpadding(0);
		centerTable.setCellspacing(0);
		centerTable.setAlignment(1, 1, "center");
		setVerticalAlignment("top");
	}

	protected HeaderTable getProGolfers() {
		HeaderTable table = new HeaderTable();
		table.setBorderColor("#8ab490");
		table.setHeadlineSize(1);
		table.setHeadlineColor("#FFFFFF");
		table.setHeadlineLeft();
		table.setWidth(148);
		table.setHeaderText(iwrb.getLocalizedString("golferpage.header_table_name", "Pro golfers"));
		//this should be automated
		Table golfers = new Table(1, 2);

		/**@todo If golfersPage should be generalized then new data could be inserted through
		 * the GolfersFriendsDataBusiness class, "into" the GolferPageData class.  That class could
		 * contain possibly only golfers who would have their own pages.  Then here we would use a
		 * findAll-function on that Data class, to extract all the "pro"-golfers and when clicked on
		 * they would submit their member_union_id.  Then the rest should work???  (depends on what
		 * you want to get).  Bjarni.
		 * */

		//      Link golferLink = new Link("Björgvin Sigurbergsson","/golfers/index.jsp");
		//      golferLink.addParameter("member_union_id", "3152");  //Björgvins ID!!!!!!!
		Link golferLink = new Link(iwrb.getLocalizedString("BJOGGI_PRO","Bjorgvin Sigurbergsson"), "http://bjorgvin.sidan.is");
		golferLink.setFontSize(1);

		Link golferLink2 = new Link(iwrb.getLocalizedString("BIGGI_PRO","Birgir Leifur Hafþorsson"), "http://www.mmedia.is/~hob/blh/");
		golferLink2.setFontSize(1);

		//      Link golferLink3 = new Link("Ólafur Már Sigurðsson","http://www.velkomin.is/olimar/");
		//      golferLink3.setFontSize(1);

		golfers.add(golferLink, 1, 1);
		golfers.add(golferLink2, 1, 2);
		//      golfers.add(golferLink3, 1, 3);

		table.add(golfers);

		return table;
	}

	protected Table Right(IWContext modinfo) throws SQLException, IOException {
		Table rightTable = new Table(1, 19); //1,15
		rightTable.setWidth(RIGHTWIDTH);
		rightTable.setCellpadding(0);
		rightTable.setCellspacing(0);

		rightTable.setVerticalAlignment(1, 1, "top");
		rightTable.setVerticalAlignment(1, 2, "top");
		rightTable.setVerticalAlignment(1, 3, "top");
		rightTable.setVerticalAlignment(1, 4, "top");
		rightTable.setVerticalAlignment(1, 5, "top");
		rightTable.setVerticalAlignment(1, 6, "top");
		rightTable.setVerticalAlignment(1, 7, "top");
		rightTable.setVerticalAlignment(1, 8, "top");
		rightTable.setVerticalAlignment(1, 9, "top");
		rightTable.setVerticalAlignment(1, 11, "top");
		rightTable.setVerticalAlignment(1, 12, "top");
		rightTable.setVerticalAlignment(1, 13, "top");
		rightTable.setVerticalAlignment(1, 14, "top");
		rightTable.setVerticalAlignment(1, 15, "top");
		rightTable.setVerticalAlignment(1, 16, "top");
		rightTable.setVerticalAlignment(1, 17, "top");
		rightTable.setVerticalAlignment(1, 18, "top");
		rightTable.setVerticalAlignment(1, 19, "top");
//		rightTable.setVerticalAlignment(1, 21, "top");

		rightTable.setColumnAlignment(1, "center");
		HeaderTable proGolfers = getProGolfers();
		//proGolfers.setCacheable("proGolfers",86400000);//24 hour

		rightTable.add(Block.getCacheableObject(proGolfers, "proGolfers", 86400000), 1, 1);
		//rightTable.add(new Flash("http://clarke.idega.is/golfnews.swt?text="+java.net.URLEncoder.encode(iwrb.getLocalizedString("template.international_golf_news","International golf news")),148,288),1,3);

		//Úrval útsýn
		Link uu = new Link(iwrb.getImage("/banners/golfbanner.gif"), "http://www.uu.is/adrar-ferdir/golf/");
		uu.setTarget("_blank");
		rightTable.add(uu, 1, 3); //1,5

		//Hotel Edda
		Link link = new Link(iwrb.getImage("/banners/Edda_golf.gif"), "http://www.hoteledda.is");
		link.setTarget("_blank");
		rightTable.add(link, 1, 5); //1,5

		//		rightTable.addBreak(1, 4);
//		Link nb = new Link(new Image("/banners/nevada_150_55.gif"), "http://www.nevadabob.is/");
//		nb.setTarget("_blank");
//		rightTable.add(nb, 1, 7); //1,5
		//		rightTable.addBreak(1, 4);
		//		rightTable.addBreak(1, 4);
		//Visa tournament banner TEMOPORARY
		/*	  idegaTimestamp stamp = new idegaTimestamp();
			  if ( stamp.getYear() == 2002 && stamp.getMonth() == 8 && stamp.getDay() <= 15 ) {
			    Flash visabanner = new Flash("/banners/visagolf_135_new.swf","visagolf");
			    visabanner.setWidth(135);
			    visabanner.setHeight(135);
			    rightTable.add(visabanner,1,3);
			    rightTable.setHeight(1,3,"160");
			    rightTable.add(Text.getBreak(),1,3);
			    rightTable.add(Text.getBreak(),1,3);
			  }*/

		Link bjor = new Link(new Image("/banners/Carlsberg-golf-144x144.gif"), "http://www.carlsberg.is/");
		bjor.setTarget("_blank");
		rightTable.add(bjor, 1, 7); //1,5

		HeaderTable teaching = getGolfTeaching();
		rightTable.add(Block.getCacheableObject(teaching, "golfTeaching", 86400000), 1, 9);

		HeaderTable poll = getPollVoter();
		//poll.setCacheable("poll",3600000);//1 hour
		rightTable.add(Block.getCacheableObject(poll, "poll", 3600000), 1, 11); //1,5

		HeaderTable asses = getGSIAssociates();
		//asses.setCacheable("asses",86400000);//24 hour
		rightTable.add(Block.getCacheableObject(asses, "asses", 86400000), 1, 13); //1,7

		HeaderTable gLinks = getGolfLinks();
		//gLinks.setCacheable("gLinks",86400000);//24 hour
		rightTable.add(Block.getCacheableObject(gLinks, "gLinks", 86400000), 1, 15); //1,9

		BoxReader bLinks = getLinks(modinfo); //uses is admin crappers
		bLinks.setCacheable("Miscbox", 86400000); //1000*60*60*24 = 24 hours
		rightTable.add(bLinks, 1, 17); //1,11

		rightTable.add(idega(), 1, 19); //1,13

/*		JModuleObject yellow = new JModuleObject();
		yellow.add(getYellowLine());
		yellow.setCacheable("yellow", 86400000); //24 hour
		rightTable.add(getYellowLine(), 1, 23); //1,11//1,15*/

		return rightTable;
	}

	protected Table RightDemoSite(IWContext modinfo) throws SQLException, IOException {
		Table rightTable = new Table(1, 10);
		rightTable.setWidth(RIGHTWIDTH);
		rightTable.setCellpadding(0);
		rightTable.setCellspacing(0);

		rightTable.setVerticalAlignment(1, 1, "top");
		rightTable.setVerticalAlignment(1, 2, "top");
		rightTable.setVerticalAlignment(1, 3, "top");
		rightTable.setVerticalAlignment(1, 4, "top");
		rightTable.setVerticalAlignment(1, 5, "top");
		rightTable.setVerticalAlignment(1, 6, "top");
		rightTable.setVerticalAlignment(1, 7, "top");
		rightTable.setVerticalAlignment(1, 8, "top");
		rightTable.setVerticalAlignment(1, 9, "top");

		rightTable.setColumnAlignment(1, "center");

		HeaderTable poll = getPollVoter();
		rightTable.add(Block.getCacheableObject(poll, "poll", 3600000), 1, 1); //1,5

		ForumBox forums = new ForumBox();
		forums.setRight(true);

		rightTable.add(forums, 1, 3);

		HeaderTable asses = getGSIAssociates();
		rightTable.add(Block.getCacheableObject(asses, "asses", 86400000), 1, 5); //1,7

		HeaderTable gLinks = getGolfLinks();
		rightTable.add(Block.getCacheableObject(gLinks, "gLinks", 86400000), 1, 7); //1,9

		BoxReader bLinks = getLinks(modinfo); //uses is admin crappers
		bLinks.setCacheable("Miscbox", 86400000); //1000*60*60*24 = 24 hours
		rightTable.add(bLinks, 1, 9);

		return rightTable;
	}

	private Form getYellowLine() {

		Form myForm = new Form("http://www.gulalinan.is/leit.asp", "get");
		myForm.setTarget("_blank");
		myForm.setName("Search");

		Table myTable = new Table(1, 3);
		myTable.setWidth(120);
		myTable.setHeight(70);
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);

		Image rammiUppi = new Image("/pics/gulalinan/rammi_uppi120.gif", "", 120, 6);
		myTable.add(rammiUppi, 1, 1);
		Image rammiNidri = new Image("/pics/gulalinan/rammi_nidri120.gif", "", 120, 8);
		myTable.add(rammiNidri, 1, 3);

		Table innerTable = new Table(1, 3);
		innerTable.setWidth(120);
		innerTable.setHeight("100%");
		innerTable.setCellpadding(0);
		innerTable.setCellspacing(0);
		innerTable.setAlignment(1, 1, "center");
		innerTable.setAlignment(1, 2, "center");
		innerTable.setAlignment(1, 3, "right");
		innerTable.setBackgroundImage(new Image("/pics/gulalinan/bakgrunnurx120.gif"));

		Image searchImage = new Image("/pics/gulalinan/gulalinanlogo.gif", "", 67, 12);
		Link yellowLink = new Link(searchImage, "http://www.gulalinan.is");
		yellowLink.setTarget("_blank");

		TextInput textInput = new TextInput("kwd");
		textInput.setLength(12);

		HiddenInput hidden = new HiddenInput("ac", "ks");

		Image submitImage = new Image("/pics/gulalinan/leita.gif", "Leita", 39, 13);
		SubmitButton submit = new SubmitButton(submitImage, "image1");
		submit.setMarkupAttribute("hspace", "5");

		innerTable.add(yellowLink, 1, 1);
		innerTable.add(textInput, 1, 2);
		innerTable.add(submit, 1, 3);
		
		myTable.add(innerTable, 1, 2);
		myForm.add(myTable);
		myForm.add(hidden);

		return myForm;

	}

	protected Table golfHeader() {
		Table golfHeader = new Table(1, 2);

		Text zero = new Text("");
		zero.setFontSize("1");

		golfHeader.add(zero, 1, 2);

		Table linkTable = new Table(8, 1);

		golfHeader.setHeight(1, "68");
		golfHeader.setHeight(2, "16");
		setTopHeight("84");
		golfHeader.setWidth("720");
		golfHeader.setCellpadding(0);
		golfHeader.setCellspacing(0);

		Image banBg = iwrb.getImage("/mainpage/banner.gif");
		banBg.setWidth(720);
		banBg.setHeight(68);

		golfHeader.setBackgroundImage(1, 1, banBg);
		golfHeader.setVerticalAlignment(1, 2, "top");

		linkTable.setHeight("14");
		linkTable.setCellpadding(0);
		linkTable.setCellspacing(0);
		linkTable.setWidth("720");
		linkTable.setVerticalAlignment("top");
		linkTable.setRowVerticalAlignment(1, "top");

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
		linkTable.add(endImage, 8, 1);

		golfHeader.setAlignment(1, 2, "top");
		golfHeader.add(linkTable, 1, 2);

		return golfHeader;
	}

	protected Table golfFooter() {
		Table golfFooter = new Table(6, 1);
		golfFooter.setHeight("21");
		setBottomHeight("21");
		golfFooter.setWidth("720");
		golfFooter.setCellpadding(0);
		golfFooter.setCellspacing(0);

		golfFooter.add(iwrb.getImage("/mainpage/bottom1.gif"), 1, 1);
		golfFooter.add(new Link(iwrb.getImage("/mainpage/bottom2.gif"), "/index.jsp"), 2, 1);
		golfFooter.add(iwrb.getImage("/mainpage/bottom3.gif"), 3, 1);
		Image back = iwrb.getImage("/mainpage/bottom4.gif");
		back.setOnClick("history.go(-1)");
		golfFooter.add(back, 4, 1);
		golfFooter.add(iwrb.getImage("/mainpage/bottom5.gif"), 5, 1);
		golfFooter.add(new Link(iwrb.getImage("/mainpage/bottom6.gif"), "mailto: golf@idega.is"), 6, 1);

		return golfFooter;
	}

	protected Table getHBanner(IWContext modinfo) throws SQLException {
		Table bannerTable = new Table(1, 1);
		bannerTable.setAlignment("center");
		bannerTable.setAlignment(1, 1, "middle");
		bannerTable.setCellpadding(10);
		bannerTable.setCellspacing(0);

		
/*				InsertBanner ib = new InsertBanner(3, isAdmin(modinfo));
				  ib.setAdminButtonURL("/pics/jmodules/banner/bannerstjori.gif");
				bannerTable.add(ib,1,1);*/
/*		Flash flash = new Flash("/banners/365x55.swf");
		flash.setWidth(365);
		flash.setHeight(55);
		Link hBanner = new Link(flash, "http://www.toyota.is");
		hBanner.setTarget(Link.TARGET_NEW_WINDOW);
*/
		/*		Flash flash = new Flash("/banners/VISAGolf.swf");
				flash.setWidth(365);
				flash.setHeight(55);
				Link hBanner = new Link(flash,"http://www.visa.is");*/

//		Link hBanner = new Link(new Image("/pics/banners/titleist_counterbanner365x5.gif",""),"http://www.titleist.com/_prov1/");
//		hBanner.setTarget(Link.TARGET_NEW_WINDOW);

		Link hBanner = new Link(new Image("/pics/banners/KBbanki_bordi_365x55.gif",""),"http://www.kbbanki.is/");
		hBanner.setTarget(Link.TARGET_NEW_WINDOW);
//		bannerTable.add(hBanner, 1, 1);
		/*
		Link audi = new Link(new Image("/pics/banners/audi_golf_365x55.gif",""), "http://www.hekla.is/audi");
		audi.setTarget(Link.TARGET_NEW_WINDOW);
		bannerTable.add(audi, 1, 1);*/
		
		bannerTable.add(hBanner, 1, 1);
		return bannerTable;
	}

	protected HeaderTable sponsors() {

		HeaderTable table = new HeaderTable();
		table.setBorderColor("#8ab490");
		table.setHeadlineSize(1);
		table.setHeadlineColor("#FFFFFF");
		table.setRightHeader(false);
		table.setHeadlineAlign("left");
		table.setWidth(148);
		table.setHeaderText(iwrb.getLocalizedString("associates", "Associates"));

		int bannerNumber = 6;

		Table innerTable = new Table(1, bannerNumber);
		innerTable.setWidth("100%");
		innerTable.setColumnAlignment(1, "center");

		Link[] banners = new Link[bannerNumber];
		banners[0] = (new Link(iwrb.getImage("/banners/sjova.gif"), "http://www.sjova.is"));
		//      banners[1] = (new Link(iwrb.getImage("/banners/bunbank.gif"),"http://www.bi.is"));
		banners[1] = (new Link(iwrb.getImage("/banners/opinkerfi.gif"), "http://www.ok.is"));
		banners[2] = (new Link(iwrb.getImage("/banners/toyota.gif"), "http://www.toyota.is"));

		banners[3] = (new Link(new Image("/pics/banners/logo_ostur.gif"), "http://www.ostur.is/"));
		//      banners[4] = (new Link(new Image("/pics/banners/golf.is.litill.gif"),"http://www.lotto.is"));
		//banners[4] = (new Link(iwrb.getImage("/banners/ecco.gif"),"http://www.ecco.com"));
		banners[4] = (new Link(iwrb.getImage("/banners/euro.gif"), "http://www.europay.is/golf"));
		banners[5] = (new Link(iwrb.getImage("/banners/syn.gif"), "http://www.syn.is"));

		for (int i = 0; i < banners.length; i++) {
			banners[i].setTarget("_blank");
			innerTable.add(banners[i], 1, i + 1);
		}
		table.add(innerTable);
		return table;
	}

	protected HeaderTable sponsorsDemoSite() {
		HeaderTable table = new HeaderTable();
		table.setBorderColor("#8ab490");
		table.setHeadlineSize(1);
		table.setHeadlineColor("#FFFFFF");
		table.setRightHeader(false);
		table.setHeadlineAlign("left");
		table.setWidth(148);
		table.setHeaderText(iwrb.getLocalizedString("associates", "Associates"));
		Table innerTable = new Table(1, 4);
		innerTable.setWidth("100%");
		innerTable.setColumnAlignment(1, "center");
		Link one = new Link(iwrb.getImage("/banners/wmdata.gif"), "http://www.wmdata.se");
		Link two = new Link(iwrb.getImage("/banners/idega.gif"), "http://www.idega.is");
		Link three = new Link(iwrb.getImage("/banners/compaq.gif"), "http://www.compaq.se");
		Link four = new Link(iwrb.getImage("/banners/telia.gif"), "http://www.telia.se");
		one.setTarget("_blank");
		two.setTarget("_blank");
		three.setTarget("_blank");
		four.setTarget("_blank");
		innerTable.add(one, 1, 1);
		innerTable.add(two, 1, 2);
		innerTable.add(three, 1, 3);
		innerTable.add(four, 1, 4);
		table.add(innerTable);
		return table;
	}

	protected HeaderTable mainSponsor() throws IOException {

		HeaderTable table = new HeaderTable();
		table.setBorderColor("#8ab490");
		table.setHeadlineSize(1);
		table.setHeadlineColor("#FFFFFF");
		table.setRightHeader(false);
		table.setHeadlineAlign("left");
		table.setWidth(148);
		table.setHeaderText(iwrb.getLocalizedString("main_associate", "Main sponsor"));

		int bannerNumber = 1;

		Table innerTable = new Table(1, bannerNumber);
		innerTable.setWidth("100%");
		innerTable.setColumnAlignment(1, "center");

		Link banner = new Link(iwrb.getImage("/banners/bunbank.gif"), "http://www.bi.is");

		innerTable.add(banner, 1, 1);
		table.add(innerTable);

		return table;
	}

	protected HeaderTable amateur() throws IOException {

		HeaderTable table = new HeaderTable();
		table.setBorderColor("#8ab490");
		table.setHeadlineSize(1);
		table.setHeadlineColor("#FFFFFF");
		table.setRightHeader(false);
		table.setHeadlineAlign("left");
		table.setWidth(148);
		table.setHeaderText(iwrb.getLocalizedString("edwin", "New book"));

		int bannerNumber = 1;

		Table innerTable = new Table(1, bannerNumber);
		innerTable.setWidth("100%");
		innerTable.setColumnAlignment(1, "center");

		Link banner = new Link(iwrb.getImage("/banners/edwin.jpg"), "http://www.golfvellir.is");

		innerTable.add(banner, 1, 1);
		table.add(innerTable);

		return table;
	}

	protected HeaderTable getGolfTeaching() throws IOException {

		HeaderTable table = new HeaderTable();
		table.setBorderColor("#8ab490");
		table.setHeadlineSize(1);
		table.setHeadlineColor("#FFFFFF");
		table.setRightHeader(false);
		table.setHeadlineAlign("left");
		table.setWidth(148);
		table.setHeaderText(iwrb.getLocalizedString("teaching", "Golf lessons"));

		int bannerNumber = 2;

		Table innerTable = new Table();
		innerTable.setWidth("100%");
		innerTable.setColumnAlignment(1, "center");

		Link banner = new Link(new Image("/banners/augl_golfkennslasmall.gif"), "http://www.golfkennsla.is");
		innerTable.add(banner, 1, 1);

		Link banner2 = new Link(new Image("/banners/golfthjalfun.png"), "http://www.golfthjalfun.is");
		innerTable.add(banner2, 1, 2);
		
		table.add(innerTable);

		return table;
	}

	protected HeaderTable register() {
		Form form = new Form("/createlogin.jsp", "POST");
		HeaderTable table = new HeaderTable();
		table.setBorderColor("#8ab490");
		table.setHeadlineSize(1);
		table.setHeadlineColor("#FFFFFF");
		table.setRightHeader(false);
		table.setHeadlineAlign("left");
		table.setWidth(148);
		table.setHeaderText(iwrb.getLocalizedString("new.members", "New Members"));

		Table innerTable = new Table(3, 2);
		innerTable.mergeCells(1, 1, 3, 1);
		innerTable.setWidth("100%");
		innerTable.setColumnAlignment(1, "left");

		Text texti = new Text(iwrb.getLocalizedString("new.members.text", "Only golfclub members can register."));
		texti.setFontSize(1);

		Text social = new Text(iwrb.getLocalizedString("new.members.socialsecuritynumber", "ssn :"));
		social.setFontSize(2);
		social.setBold();

		innerTable.add(texti, 1, 1);
		TextInput kt = new TextInput("kt");
		kt.setStyleAttribute("fontsize: 10pt");
		kt.setSize(10);

		innerTable.add(social, 1, 2);
		innerTable.add(kt, 2, 2);
		innerTable.add(new SubmitButton(iwb.getImage("shared/register.gif")), 3, 2);

		form.add(innerTable);
		table.add(form);

		return table;
	}

	// ###########  Public - Föll

	public void setVerticalAlignment(String alignment) {
		centerTable.setVerticalAlignment(alignment);
		centerTable.setVerticalAlignment(1, 1, alignment);
	}

	public void add(PresentationObject objectToAdd) {
		Center().add(objectToAdd, 1, 1);
	}

	public void removeUnionIdSessionAttribute(IWContext modinfo) {
		modinfo.removeSessionAttribute("golf_union_id");
	}

	public String getUnionID(IWContext modinfo) {
		return (String)modinfo.getSessionAttribute("golf_union_id");
	}

	public void setUnionID(IWContext modinfo, String union_id) {
		modinfo.setSessionAttribute("golf_union_id", union_id);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public Member getMember(IWContext modinfo) {
		return (Member)modinfo.getSession().getAttribute("member_login");
	}

	public boolean isAdmin(IWContext modinfo) {
		try {
			return is.idega.idegaweb.golf.block.login.business.AccessControl.isAdmin(modinfo);
		}
		catch (SQLException E) {
			E.printStackTrace(System.err);
		}
		catch (Exception E) {
			E.printStackTrace(System.err);
		}
		finally {
		}

		return false;
	}

	public boolean isDeveloper(IWContext modinfo) {
		return is.idega.idegaweb.golf.block.login.business.AccessControl.isDeveloper(modinfo);
	}

	public boolean isClubAdmin(IWContext modinfo) {
		return is.idega.idegaweb.golf.block.login.business.AccessControl.isClubAdmin(modinfo);
	}

	public boolean isClubWorker(IWContext modinfo) {
		boolean ret;

		try {
			ret = is.idega.idegaweb.golf.block.login.business.AccessControl.isClubWorker(modinfo);
		}
		catch (java.sql.SQLException e) {
			e.printStackTrace(System.err);
			ret = false;
		}

		return (ret);
	}

	public boolean isUser(IWContext modinfo) {
		return is.idega.idegaweb.golf.block.login.business.AccessControl.isUser(modinfo);
	}

	public void main(IWContext modinfo) throws Exception {
		isAdmin = isAdmin(modinfo);

		iwrb = getResourceBundle(modinfo);
		iwb = getBundle(modinfo);

		String isDemo = (String)modinfo.getIWMainApplication().getSettings().getProperty(GOLF_DEMO_SITE);
		//System.out.println("IS DEMO : "+isDemo);
		if (isDemo != null) {
			isDemoSite = true;
		}

		setLinkColor(iwb.getProperty("link_color", "black"));
		setVlinkColor(iwb.getProperty("vlink_color", "black"));
		setHoverColor(iwb.getProperty("hover_link_color", "#8ab490"));
		setHoverDecoration("none");
		setTextDecoration("none");

		try {
			User(modinfo);
		}
		catch (SQLException E) {
			E.printStackTrace(System.err);
		}
		catch (IOException E) {
			E.printStackTrace(System.err);
		}
	}

}

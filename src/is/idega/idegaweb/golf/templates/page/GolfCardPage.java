package is.idega.idegaweb.golf.templates.page;



import javax.servlet.*;

import javax.servlet.http.*;

import javax.servlet.jsp.*;

import is.idega.idegaweb.golf.templates.*;

import com.idega.jmodule.*;

import com.idega.presentation.*;

import com.idega.presentation.text.*;

import com.idega.presentation.ui.*;

import com.idega.jmodule.banner.presentation.BannerContainer;

import java.sql.*;

import java.io.*;



public class GolfCardPage extends GolfMainJSPModulePage{



  protected void User(IWContext iwc)throws SQLException,IOException{

    this.setTextDecoration("none");

    setTopMargin(5);

    add( "top", golfHeader());

    add("bottom", golfFooter());

    add(Left(iwc), Center(), Right(iwc));

    setWidth(1, "" + LEFTWIDTH);

    setContentWidth( "100%");

    setWidth(3, "" + RIGHTWIDTH);

  }



  protected Table Left(IWContext iwc) throws IOException,SQLException {

    Table leftTable = new Table(2,1);

      leftTable.setHeight("100%");

      leftTable.setWidth("" + LEFTWIDTH);

      leftTable.setVerticalAlignment(1,1,"top");

      leftTable.setCellpadding(0);

      leftTable.setCellspacing(0);

      leftTable.setWidth("100%");

      leftTable.setWidth(2,"3");



    Image dot = new Image("/pics/golfcard/punktalina.gif","",3,3);

      leftTable.setBackgroundImage(2,1,dot);



    leftTable.add(getLinks(),1,1);



    return leftTable;

  }



  protected Table Right(IWContext iwc) throws SQLException,IOException{

    Table rightTable = new Table(2,1);

      rightTable.setCellpadding(0);

      rightTable.setCellspacing(0);

      rightTable.setVerticalAlignment(2,1,"top");

      rightTable.setAlignment(2,1,"center");

      rightTable.setHeight("100%");

      rightTable.setWidth("100%");

      rightTable.setWidth(1,"3");



    Image dot = new Image("/pics/golfcard/punktalina.gif","",3,3);

      rightTable.setBackgroundImage(1,1,dot);



    rightTable.add("<br>",2,1);

    rightTable.add(cardSponsors(),2,1);



    return rightTable;

  }



  protected Table golfHeader(){

      setTopHeight("84");



      Image banBg = new Image("/pics/golfcard/banner.gif","",720,68);



      Table golfHeader = new Table(1,2);

        golfHeader.setHeight( 1, "68");

        golfHeader.setHeight( 2, "16");

        golfHeader.setWidth("720");

        golfHeader.setCellpadding(0);

        golfHeader.setCellspacing(0);

        golfHeader.setBackgroundImage(1, 1, banBg);

        golfHeader.setVerticalAlignment(1,2, "top");



      Table linkTable = new Table(8,1);

        linkTable.setHeight("14");

        linkTable.setCellpadding(0);

        linkTable.setCellspacing(0);

        linkTable.setWidth("720");

        linkTable.setVerticalAlignment("top");

        linkTable.setRowVerticalAlignment(1,"top");



      Image clubImage = new Image("/pics/golfcard/clubs.gif","Klúbbar",101,15);

      Link club = new Link(clubImage, "/clubs/");

      linkTable.add(club, 1, 1);



      Image startingtimesImage = new Image("/pics/golfcard/teetimes.gif","Rástímar",85,15);

      Link startingtimes = new Link(startingtimesImage, "/start/search.jsp");

      linkTable.add(startingtimes, 2, 1);



      Image handicapImage = new Image("/pics/golfcard/handicap.gif","Forgjöf",85,15);

      Link handicap = new Link(handicapImage, "/handicap/");

      linkTable.add(handicap, 3, 1);



      Image modtaskraImage = new Image("/pics/golfcard/tournaments.gif","Mótaskrá",85,15);

      Link motaskra = new Link(modtaskraImage, "/tournament/");

      linkTable.add(motaskra, 4, 1);



      Image umGSIImage = new Image("/pics/golfcard/aboutgsi.gif","Um GSÍ",73,15);

      Link umGSI = new Link(umGSIImage, "/gsi/index.jsp");

      linkTable.add(umGSI, 5, 1);



      Image spjallidImage = new Image("/pics/golfcard/forums.gif","Spjallið",85,15);

      Link spjallid = new Link(spjallidImage, "/forum/index.jsp");

      linkTable.add(spjallid, 6, 1);



      Image indexImage = new Image("/pics/golfcard/home.gif","Heim",85,15);

      Link index = new Link(indexImage, "/index.jsp");

      linkTable.add(index, 7, 1);



      Image endImage = new Image("/pics/golfcard/linkend.gif","",121,15);

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



      golfFooter.add(new Image("/pics/golfcard/bottom1.gif"),1,1);

      golfFooter.add(new Link (new Image("/pics/golfcard/bottom2.gif"), "/index.jsp"),2,1);

      golfFooter.add(new Image("/pics/golfcard/bottom3.gif"),3,1);

      Link backLink = new Link(new Image("/pics/golfcard/bottom4.gif"),"#");

        backLink.setAttribute("OnClick", "history.go(-1)");

      golfFooter.add(backLink,4,1);

      golfFooter.add(new Image("/pics/golfcard/bottom5.gif"),5,1);

      Link mailLink = new Link(new Image("/pics/golfcard/bottom6.gif"),"mailto: golf@idega.is");

        mailLink.clearParameters();

      golfFooter.add(mailLink,6,1);



      return golfFooter;

  }



  protected Table getLinks() {

      Table linksTable = new Table(2,6);

        linksTable.setCellpadding(4);

        linksTable.setAlignment("center");



      Text offers = new Text("Tilboð mánaðarins");

        offers.setFontSize(1);

      Text terms = new Text("Viðskiptaskilmálar");

        terms.setFontSize(1);

      Text discounts = new Text("Fastir afslættir");

        discounts.setFontSize(1);

      Text insurance = new Text("Tryggingar");

        insurance.setFontSize(1);

      Text info = new Text("Upplýsingar");

        info.setFontSize(1);

      Text apply = new Text("Umsókn");

        apply.setFontSize(1);



      Link offersLink = new Link(offers,"/card/text.jsp");

        offersLink.addParameter("text_id",727);

      Link termsLink = new Link(terms,"/card/text.jsp");

        termsLink.addParameter("text_id",728);

      Link discountsLink = new Link(discounts,"/card/text.jsp");

        discountsLink.addParameter("text_id",729);

      Link insuranceLink = new Link(insurance,"/card/text.jsp");

        insuranceLink.addParameter("text_id",730);

      Link infoLink = new Link(info,"/card/text.jsp");

        infoLink.addParameter("text_id",731);

      Link applyLink = new Link(apply,"http://www.europay.is/form/kort.htm");

        applyLink.setTarget("_blank");



      Image offersImage = new Image("/pics/golfcard/icons/tilbod_manadarins.gif","",17,18);

      Image termsImage = new Image("/pics/golfcard/icons/vidskiptaskilmalar.gif","",17,18);

      Image discountsImage = new Image("/pics/golfcard/icons/fastir_afslaettir.gif","",17,18);

      Image insuranceImage = new Image("/pics/golfcard/icons/tryggingar.gif","",17,18);

      Image infoImage = new Image("/pics/golfcard/icons/upplysingar.gif","",17,18);

      Image applyImage = new Image("/pics/golfcard/icons/umsokn.gif","",17,18);



      linksTable.add(offersImage,1,1);

      linksTable.add(offersLink,2,1);

      linksTable.add(termsImage,1,2);

      linksTable.add(termsLink,2,2);

      linksTable.add(discountsImage,1,3);

      linksTable.add(discountsLink,2,3);

      linksTable.add(insuranceImage,1,4);

      linksTable.add(insuranceLink,2,4);

      linksTable.add(infoImage,1,5);

      linksTable.add(infoLink,2,5);

      linksTable.add(applyImage,1,6);

      linksTable.add(applyLink,2,6);



      return linksTable;



  }



  protected Form cardSponsors() throws IOException{



    String action = getRequest().getParameter("action");

    String union_id = "2";



    Table adalTable = new Table(1,1);

      adalTable.setCellpadding(0);

      adalTable.setCellspacing(0);

      adalTable.setColor("#FFFFFF");

      adalTable.setWidth("100%");

      adalTable.setAlignment("center");



    Form myForm = new Form();

      myForm.add(adalTable);



    if ( (isAdmin) && (action == null) ) {

        adalTable.add(new SubmitButton(iwrb.getImage("/buttons/associates.gif")),1,1);

        adalTable.add(new HiddenInput("action","admin"),1,1);

        adalTable.add(Text.getBreak());

        adalTable.add(Text.getBreak());

    }



    BannerContainer contain = new BannerContainer();

      contain.setConnectionAttributes("union_id",Integer.parseInt(union_id));



    adalTable.add(contain,1,1);



    return myForm;

  }

}


package com.idega.projects.golf.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.golf.*;
import com.idega.projects.golf.moduleobject.Login;
import com.idega.jmodule.*;
import com.idega.jmodule.banner.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.text.data.*;
import com.idega.jmodule.forum.data.*;
import com.idega.jmodule.poll.moduleobject.*;
import com.idega.jmodule.object.interfaceobject.*;
//import com.idega.jmodule.linkgroup.moduleobject.*;
import com.idega.jmodule.boxoffice.presentation.*;
import com.idega.jmodule.banner.presentation.BannerContainer;

import java.sql.*;
import com.idega.projects.golf.entity.*;
import java.io.*;

public class GolfClubJSPModule extends GolfMainJSPModule{

  protected void User()throws SQLException,IOException{
    getPage().setTextDecoration("none");
    setTopMargin(5);
    add( "top", golfHeader());
    add("top", Top());
    add("bottom", golfFooter());
    add(Left(), Center(), Right());
    setWidth(1, "" + LEFTWIDTH);
    setWidth(2, "398");
    setWidth(3, "" + RIGHTWIDTH);
    setContentWidth( "100%");
  }
  protected Table clubTable() throws SQLException,IOException {
    ModuleInfo modinfo = getModuleInfo();
    String union_id = modinfo.getParameter("union_id");

    if ( union_id == null ) union_id = (String) modinfo.getSession().getAttribute("golf_union_id");
    if ( union_id == null ) union_id = "3";
    Image spacer = new Image("/pics/spacer.gif","",20,1);
    Union union = new Union(Integer.parseInt(union_id));
    String abbreviation = union.getStringColumnValue("abbrevation");

    Table innerTable = new Table();
    innerTable.setWidth("148");
    innerTable.setColor("FFFFFF");
    innerTable.setCellspacing(0);
    innerTable.setCellpadding(2);

    ForumAttributes[] forum = (ForumAttributes[]) (new ForumAttributes()).findAllByColumn("attribute_id",union_id);
    Text clubLink = new Text("Um "+abbreviation);
    clubLink.setFontSize(1);
    Text fieldLink = new Text("Völlur");
    fieldLink.setFontSize(1);
    Link tournLink = new Link("Mótaskrá","/tournament/index.jsp");
    tournLink.setFontSize(1);
    tournLink.addParameter("union_id",union_id);
    Link chatLink = new Link("Spjallið","/clubs/thread.jsp");
    chatLink.setFontSize(1);
    chatLink.addParameter("union_id",union_id);
    if ( forum.length > 0 ) {
      chatLink.addParameter("forum_id",forum[0].getForumID()+"");
      chatLink.addParameter("from","FList");
      chatLink.addParameter("state","2");
    }
    Window applyWindow = new Window("Umsóknir",550,600,"/clubs/application.jsp");
    Window requestWindow = new Window("Umsóknir",900,600,"/clubs/request_member.jsp");
    Text applyText = new Text("Umsókn");
    applyText.setFontSize(1);

    Link applyLink = new Link(applyText,applyWindow);
    if ( isAdmin() ) {
      applyLink = new Link(applyText,requestWindow);
    }
    applyLink.addParameter("union_id",union_id);
    Window bagsWindow = new Window("Pokamerki",550,600,"/clubs/select_bags.jsp");
    Text bagsText = new Text("Pokamerki");
    bagsText.setFontSize(1);
    Link bagsLink = new Link(bagsText,bagsWindow);
    Link mailLink = new Link("Hafðu samband","mailto:"+abbreviation.toLowerCase()+"@golf.is");
    mailLink.setFontSize(1);
    mailLink.addParameter("union_id",union_id);
    Link homeLink = new Link("Aðalsíða "+abbreviation,"/clubs/index2.jsp");
    homeLink.setFontSize(1);
    homeLink.addParameter("union_id",union_id);

    Image memberImage = null;
    Link memberLink = null;

    //if(isAdmin()){
    //debug
      memberImage = new Image("/pics/klubbaicon/member.gif");
      memberImage.setAttribute("hspace","6");
      memberImage.setAttribute("align","absmiddle");

      memberLink = new Link("Félagatal","/clubs/members.jsp");
      memberLink.setFontSize(1);
      memberLink.addParameter("union_id",union_id);
    //}

      Image clubImage = new Image("/pics/klubbaicon/about.gif");
      clubImage.setAttribute("hspace","6");
      clubImage.setAttribute("align","absmiddle");
      Image fieldImage = new Image("/pics/klubbaicon/course.gif");
      fieldImage.setAttribute("hspace","6");
      fieldImage.setAttribute("align","absmiddle");
      Image tournImage = new Image("/pics/klubbaicon/tournamentlist.gif");
      tournImage.setAttribute("hspace","6");
      tournImage.setAttribute("align","absmiddle");
      Image chatImage = new Image("/pics/klubbaicon/forum.gif");
      chatImage.setAttribute("hspace","6");
      chatImage.setAttribute("align","absmiddle");
      Image mailImage = new Image("/pics/klubbaicon/contact.gif");
      mailImage.setAttribute("hspace","6");
      mailImage.setAttribute("align","absmiddle");
      Image englishImage = new Image("/pics/klubbaicon/english.gif");
      englishImage.setAttribute("hspace","6");
      englishImage.setAttribute("vspace","2");
      englishImage.setAttribute("align","absmiddle");
      Image homeImage = new Image("/pics/klubbaicon/mainpage.gif");
      homeImage.setAttribute("hspace","6");
      homeImage.setAttribute("align","absmiddle");


      int a = 1;

      innerTable.add(clubImage,1,a);
      innerTable.add(clubLink,1,a);
      a++;
      innerTable.add(fieldImage,1,a);
      innerTable.add(fieldLink,1,a);
      a++;
      innerTable.add(tournImage,1,a);
      innerTable.add(tournLink,1,a);
      a++;
      if ( isAdmin() ) {
        innerTable.add(memberImage,1,a);
        innerTable.add(memberLink,1,a);
        a++;
      }
      innerTable.add(chatImage,1,a);
      innerTable.add(chatLink,1,a);
      a++;

      // unions that does not want applications ( umsóknir)
      if(union_id.equalsIgnoreCase("100") && !isAdmin()){

      }
      else{
        innerTable.add(englishImage,1,a);
        innerTable.add(applyLink,1,a);
        a++;
      }
      if ( isAdmin() && union_id.equalsIgnoreCase("100") ) {
        innerTable.add(englishImage,1,a);
        innerTable.add(bagsLink,1,a);
        a++;
      }
      innerTable.add(homeImage,1,a);
      innerTable.add(homeLink,1,a);

      for ( int b = 1; b <= innerTable.getRows(); b++ ) {
              innerTable.addBreak(1,b);
              innerTable.setVerticalAlignment(1,b,"middle");
      }
      UnionText[] unionText = (UnionText[]) (new UnionText()).findAllByColumn("union_id",union_id);

      Table myTable = new Table();
      myTable.setWidth("100%");
      myTable.setCellpadding(0);
      myTable.setCellspacing(0);

      Table myTable3 = new Table();
      myTable3.setWidth("100%");
      myTable3.setCellpadding(0);
      myTable3.setCellspacing(0);

      if ( unionText.length > 0) {
        for ( int b = 0; b < unionText.length; b++ ) {
          TextModule text = new TextModule(unionText[b].getTextId());
          Link textHeadline = new Link(text.getTextHeadline(),"/clubs/text.jsp");
          textHeadline.addParameter("text_id",String.valueOf(text.getID()));
          textHeadline.addParameter("clubpage","true");
          textHeadline.addParameter("union_id",union_id);
          textHeadline.setFontSize(1);

          if ( text.getTextHeadline().equalsIgnoreCase("english") || text.getTextHeadline().equalsIgnoreCase("dansk") || text.getTextHeadline().equalsIgnoreCase("deutsch") ) {
            myTable.add(englishImage,1,b+1);
            myTable.add(textHeadline,1,b+1);
            myTable.addBreak(1,b+1);
          }
          else {
            myTable.add(spacer,1,b+1);
            myTable.add(textHeadline,1,b+1);
            myTable.addBreak(1,b+1);
          }
        }
        innerTable.add(myTable,1,1);
      }

      Field[] field = (Field[]) (new Field()).findAllByColumn("union_id",union_id);
      for ( int b = 0; b < field.length; b++ ) {
        Link fieldLinkText = new Link(field[b].getName(),"/clubs/field.jsp");
        fieldLinkText.setFontSize(1);
        fieldLinkText.addParameter("field_id",field[b].getID()+"");
        myTable3.add(spacer,1,b+1);
        myTable3.add(fieldLinkText,1,b+1);
      }

      if ( field.length > 0 ) {
        innerTable.add(myTable3,1,2);
        if ( field.length > 1 ) {
          fieldLink.setText("Vellir");
        }
      }
    return innerTable;
  }
  protected Table Left() throws IOException,SQLException {
    Table leftTable = new Table(1,4);
    //leftTable.setBorder(1);
    leftTable.setVerticalAlignment("top");
    leftTable.setVerticalAlignment(1,1,"top");
    leftTable.setVerticalAlignment(1,3,"top");
    leftTable.setVerticalAlignment(1,4,"top");
    leftTable.setHeight("100%");
    leftTable.setColumnAlignment(1, "left");
    leftTable.setWidth("" + LEFTWIDTH);
    leftTable.setCellpadding(0);
    leftTable.setCellspacing(0);
    leftTable.add(clubTable(), 1,1);
    leftTable.add(getLinks(),1,3);
      return leftTable;
  }
  protected Table Right() throws SQLException,IOException{
    Table rightTable = new Table(1,1);
    rightTable.setWidth("" + RIGHTWIDTH);
    rightTable.setCellpadding(0);
    rightTable.setCellspacing(0);
    rightTable.setVerticalAlignment(1,1,"top");
    rightTable.setColumnAlignment(1, "center");
    //debug eiki 24 april because of database stuff
    /*
    rightTable.add(ClubSponsors(),1,1);
    */
    return rightTable;
  }
  protected Form ClubSponsors() throws IOException{
    String action = getRequest().getParameter("action");
    String union_id = "3";
    String temp_union_id;
    String temp2_union_id;
    temp_union_id = getRequest().getParameter("union_id");

    if (temp_union_id != null) {
      union_id = temp_union_id;
    }
    else {
      temp2_union_id = (String) getRequest().getSession().getAttribute("golf_union_id");
      if (temp2_union_id != null) {
        union_id = temp2_union_id;
      }
    }
    Form myForm = new Form();
    Table adalTable = new Table(1,1);
    myForm.add(adalTable);
    adalTable.setCellpadding(0);
    adalTable.setCellspacing(0);
    adalTable.setColor("#FFFFFF");
    adalTable.setWidth("148");
    if ( (isAdmin()) && (action == null) ) {
        adalTable.add(new SubmitButton(new Image("/pics/samstarfsadilar.gif")),1,1);
        adalTable.add(new HiddenInput("action","admin"),1,1);
        adalTable.addText("<br><br>");
    }
    BannerContainer contain = new BannerContainer();
    contain.setConnectionAttributes("union_id",Integer.parseInt(union_id));
    adalTable.add(contain,1,1);
    return myForm;
  }
  public BoxReader getLinks(){
    ModuleInfo modinfo = getModuleInfo();
    String union_id = modinfo.getParameter("union_id");
    if ( union_id == null ) union_id = (String) modinfo.getSession().getAttribute("golf_union_id");
    if ( union_id == null ) union_id = "3";
    BoxReader box_office= new BoxReader(union_id,isAdmin());
    box_office.setBoxBorder(0);
    box_office.setInnerBoxBorder(0);
    box_office.setBoxWidth(148);
    box_office.setNoIcons(true);
    box_office.setHeaderImage(new Image("/pics/uppl-right.gif"));
    box_office.setBoxOutline(1);
    box_office.setOutlineColor("8ab490");
    box_office.setInBoxColor("FFFFFF");
    box_office.setNumberOfDisplayed(6);
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
    box_office.setHeadlineAlign("left");
    box_office.setRightHeader(false);
    box_office.setTopBoxHeight(21);
    box_office.setBoxURL("/clubs/boxoffice.jsp");
    return box_office;
  }

}

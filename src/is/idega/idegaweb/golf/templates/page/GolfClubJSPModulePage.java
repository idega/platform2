package is.idega.idegaweb.golf.templates.page;

import is.idega.idegaweb.golf.block.text.data.TextModule;
import is.idega.idegaweb.golf.block.text.data.TextModuleHome;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.entity.UnionText;

import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;

public class GolfClubJSPModulePage extends GolfMainJSPModulePage{

  protected void User(IWContext modinfo)throws SQLException,IOException{
    this.setTextDecoration("none");
    setTopMargin(5);
    add( "top", golfHeader());
    add("top", Top(modinfo));
    add("bottom", golfFooter());
    add(Left(modinfo), Center(), Right(modinfo));
    setWidth(1, "" + LEFTWIDTH);
    setWidth(2, "398");
    setWidth(3, "" + RIGHTWIDTH);
    setContentWidth( "100%");
  }
  protected Table clubTable(IWContext modinfo) throws SQLException,IOException {
    String union_id = modinfo.getParameter("union_id");

    if ( union_id == null ) union_id = (String) modinfo.getSession().getAttribute("golf_union_id");
    if ( union_id == null ) union_id = "3";
    Image spacer = new Image("/pics/spacer.gif","",20,1);
    String abbreviation = null;
    try {
	    Union union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(Integer.parseInt(union_id));
	    abbreviation = union.getStringColumnValue("abbrevation");
    }
    catch (FinderException fe) {
    		abbreviation = "";
    }

    Table innerTable = new Table();
    innerTable.setWidth("148");
    innerTable.setColor("FFFFFF");
    innerTable.setCellspacing(0);
    innerTable.setCellpadding(2);

    //ForumAttributes[] forum = (ForumAttributes[]) (new ForumAttributes()).findAllByColumn("attribute_id",union_id);
    Text clubLink = new Text(iwrb.getLocalizedString("clubs.about","About")+" "+abbreviation);
    clubLink.setFontSize(1);
    Text fieldLink = new Text(iwrb.getLocalizedString("clubs.course","Courses"));
    fieldLink.setFontSize(1);
    Link tournLink = new Link(iwrb.getLocalizedString("clubs.tournaments","Tournaments"),"/tournament/index.jsp");
    tournLink.setFontSize(1);
    tournLink.addParameter("union_id",union_id);
    Link chatLink = new Link(iwrb.getLocalizedString("clubs.forums","Forums"),"/clubs/thread.jsp");
    chatLink.setFontSize(1);
    chatLink.addParameter("union_id",union_id);
    /*if ( forum.length > 0 ) {
      chatLink.addParameter("forum_id",forum[0].getForumID()+"");
      chatLink.addParameter("from","FList");
      chatLink.addParameter("state","2");
    }*/
    Window applyWindow = new Window(iwrb.getLocalizedString("clubs.applications","Applications"),550,600,"/clubs/application.jsp");
    Window requestWindow = new Window(iwrb.getLocalizedString("clubs.applications","Applications"),900,600,"/clubs/request_member.jsp");
    applyWindow.setResizable(true);
    requestWindow.setResizable(true);
    Text applyText = new Text(iwrb.getLocalizedString("clubs.applications","Applications"));
    applyText.setFontSize(1);

    Link applyLink = new Link(applyText,applyWindow);
    if ( isAdmin ) {
      applyLink = new Link(applyText,requestWindow);
    }
    applyLink.addParameter("union_id",union_id);
    Window bagsWindow = new Window(iwrb.getLocalizedString("clubs.bag_markings","Bag markings"),550,600,"/clubs/select_bags.jsp");
    Text bagsText = new Text(iwrb.getLocalizedString("clubs.bag_markings","Bag markings"));
    bagsText.setFontSize(1);
    Link bagsLink = new Link(bagsText,bagsWindow);
    Link mailLink = new Link( iwrb.getLocalizedString("clubs.contact_us","Contact us") ,"mailto:"+abbreviation.toLowerCase()+"@golf.is");
    mailLink.setFontSize(1);
    mailLink.addParameter("union_id",union_id);
    Link homeLink = new Link(abbreviation+" "+iwrb.getLocalizedString("clubs.home","home"),"/clubs/index2.jsp");
    homeLink.setFontSize(1);
    homeLink.addParameter("union_id",union_id);

    Image memberImage = null;
    Link memberLink = null;

    //if(isAdmin()){
    //debug
      memberImage = new Image("/pics/klubbaicon/member.gif");
      memberImage.setHorizontalSpacing(6);
      memberImage.setAlignment("absmiddle");

      memberLink = new Link(iwrb.getLocalizedString("clubs.members","Members"),"/clubs/members.jsp");
      memberLink.setFontSize(1);
      memberLink.addParameter("union_id",union_id);
    //}

      Image clubImage = new Image("/pics/klubbaicon/about.gif");
      clubImage.setHorizontalSpacing(6);
      clubImage.setAlignment("absmiddle");
      Image fieldImage = new Image("/pics/klubbaicon/course.gif");
      fieldImage.setHorizontalSpacing(6);
      fieldImage.setAlignment("absmiddle");
      Image tournImage = new Image("/pics/klubbaicon/tournamentlist.gif");
      tournImage.setHorizontalSpacing(6);
      tournImage.setAlignment("absmiddle");
      Image chatImage = new Image("/pics/klubbaicon/forum.gif");
      chatImage.setHorizontalSpacing(6);
      chatImage.setAlignment("absmiddle");
      Image mailImage = new Image("/pics/klubbaicon/contact.gif");
      mailImage.setHorizontalSpacing(6);
      mailImage.setAlignment("absmiddle");
      Image englishImage = new Image("/pics/klubbaicon/english.gif");
      englishImage.setHorizontalSpacing(6);
      englishImage.setVerticalSpacing(2);
      englishImage.setAlignment("absmiddle");
      Image homeImage = new Image("/pics/klubbaicon/mainpage.gif");
      homeImage.setHorizontalSpacing(6);
      homeImage.setAlignment("absmiddle");


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
      if ( isAdmin ) {
	innerTable.add(memberImage,1,a);
	innerTable.add(memberLink,1,a);
	a++;
      }
      innerTable.add(chatImage,1,a);
      innerTable.add(chatLink,1,a);
      a++;

      // unions that does not want applications ( umsóknir)
      if((union_id.equalsIgnoreCase("81") || union_id.equalsIgnoreCase("100") || union_id.equalsIgnoreCase("84")) && !isAdmin){

      }
      else{
	innerTable.add(englishImage,1,a);
	innerTable.add(applyLink,1,a);
	a++;
      }
      if ( isAdmin && union_id.equalsIgnoreCase("100") ) {
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
      UnionText[] unionText = (UnionText[]) ((UnionText) IDOLookup.instanciateEntity(UnionText.class)).findAllByColumn("union_id",union_id);

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
	  TextModule text = ((TextModuleHome) IDOLookup.getHomeLegacy(TextModule.class)).findByPrimaryKeyLegacy(unionText[b].getTextId());
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

      Field[] field = (Field[]) ((Field) IDOLookup.instanciateEntity(Field.class)).findAllByColumn("union_id",union_id);
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
	  fieldLink.setText(iwrb.getLocalizedString("clubs.courses","Courses"));
	}
      }
    return innerTable;
  }
  protected Table Left(IWContext modinfo) throws IOException,SQLException {
    Table leftTable = new Table(1,6);
    //leftTable.setBorder(1);
    leftTable.setVerticalAlignment("top");
    leftTable.setVerticalAlignment(1,1,"top");
    leftTable.setVerticalAlignment(1,2,"top");
    leftTable.setVerticalAlignment(1,3,"top");
    leftTable.setVerticalAlignment(1,4,"top");
    leftTable.setVerticalAlignment(1,5,"top");
    leftTable.setVerticalAlignment(1,6,"top");
    leftTable.setHeight("100%");
    leftTable.setColumnAlignment(1, "left");
    leftTable.setWidth("" + LEFTWIDTH);
    leftTable.setCellpadding(0);
    leftTable.setCellspacing(0);
    leftTable.add(Languages(), 1,1);
    leftTable.add(clubTable(modinfo), 1,2);
    leftTable.add(register(),1,4);
    //leftTable.add(getLinks(modinfo),1,6);
    leftTable.setHeight(6,"100%");
      return leftTable;
  }
  protected Table Right(IWContext modinfo) throws SQLException,IOException{
    Table rightTable = new Table(1,1);
    rightTable.setWidth(Integer.toString(RIGHTWIDTH));
    rightTable.setCellpadding(0);
    rightTable.setCellspacing(0);
    rightTable.setVerticalAlignment(1,1,"top");
    rightTable.setColumnAlignment(1, "center");

    rightTable.add(ClubSponsors(modinfo),1,1);

    String union_id = modinfo.getParameter("union_id");
    if ( union_id == null ) union_id = (String) modinfo.getSession().getAttribute("golf_union_id");
    if("81".equalsIgnoreCase((union_id))){
      rightTable.resize(1,2);
      rightTable.setWidth(1,"100%");
      rightTable.setAlignment(1,2, "center");
      rightTable.setVerticalAlignment(1,2,"top");
      //HeaderTable  dummyTable = this.getProGolfers();
      Image iBjorgvinir;
      iBjorgvinir = iwrb.getImage("/golferpage/bjorgvinir.jpg","Heimasíða Björgvins");
      Link lBjorgvinir = new Link( iBjorgvinir, "/golfers/index.jsp");
      //rightTable.add(dummyTable,1,2);
      rightTable.add(lBjorgvinir,1,2);
    }
    return rightTable;
  }
  protected Form ClubSponsors(IWContext modinfo) throws IOException{
    String action = modinfo.getParameter("action");
    String union_id = "3";
    String temp_union_id;
    String temp2_union_id;
    temp_union_id = modinfo.getParameter("union_id");

    if (temp_union_id != null) {
      union_id = temp_union_id;
    }
    else {
      temp2_union_id = (String) modinfo.getSessionAttribute("golf_union_id");
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
    if ( (isAdmin) && (action == null) ) {
	adalTable.add(new SubmitButton(iwrb.getImage("/buttons/associates.gif")),1,1);
	adalTable.add(new HiddenInput("action","admin"),1,1);
	adalTable.add(Text.getBreak());
	adalTable.add(Text.getBreak());

    }
    /*BannerContainer contain = new BannerContainer();
    contain.setConnectionAttributes("union_id",Integer.parseInt(union_id));
    adalTable.add(contain,1,1);*/
    return myForm;
  }
  /*public BoxReader getLinks(IWContext modinfo){

    String union_id = modinfo.getParameter("union_id");
    if ( union_id == null ) union_id = (String) modinfo.getSession().getAttribute("golf_union_id");
    if ( union_id == null ) union_id = "3";
    BoxReader box_office= new BoxReader(union_id,isAdmin);
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
  }*/
}

package is.idega.experimental.kk;

import com.idega.idegaweb.template.TemplatePage;
import com.idega.presentation.Table;
import com.idega.presentation.Image;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Window;
import com.idega.block.login.presentation.Login;

/**
 * Title:        KK
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class KKMainTemplate extends TemplatePage {

  private Table frameTable;
  private Table mainTable;
  private Table contentTable;
  private Table headerTable;
  private Table topHeader;
  private Table contentFrameTable;
  private boolean first;

  private boolean showHeaderTable;
  private boolean showHeaderTableLastValue;

  public KKMainTemplate() {
    super();
    this.setMarginHeight(0);
    this.setMarginWidth(0);
    this.setTopMargin(0);
    this.setLeftMargin(0);
    this.setBackgroundColor("#000000");

    first = true;
    showHeaderTable = true;
    showHeaderTableLastValue = false;
    initializeFrameTable();
    initializeContentTable();
    initializeHeaderTable();
//    intitializeContentFrameTable();
    initializeMainTable();
    initializeTopHeader();

    this.add(frameTable);

    frameTable.setBorder(1);
    mainTable.setBorder(1);
    contentTable.setBorder(1);
    headerTable.setBorder(1);

  }

  public void initializeFrameTable(){
    frameTable = new Table(3,1);
    frameTable.setCellpadding(0);
    frameTable.setCellspacing(0);
    frameTable.setColor(1,1,"#000000");
    frameTable.setColor(2,1,"#000000");
    frameTable.setColor(3,1,"#828282");

    frameTable.setHeight("100%");
    frameTable.setWidth("100%");

    frameTable.setWidth(1,1,"50%");
    frameTable.setWidth(3,1,"50%");
  }



  public void initializeContentTable(){
    contentTable = new Table(1,1);
    contentTable.setCellpadding(15);
    contentTable.setCellspacing(0);
    contentTable.setHeight("100%");
    contentTable.setWidth("100%");
    contentTable.setVerticalAlignment(1,1,"top");
    contentTable.setAlignment(1,1,"left");
  }

  public void initializeHeaderTable(){
    headerTable = new Table(1,1);
    headerTable.setCellpadding(0);
    headerTable.setCellspacing(0);
    headerTable.add(new Image("/images/template/kktemplate_07.gif",""),1,1);
  }

  public void initializeTopHeader(){
    topHeader = new Table(1,1);
    topHeader.setBorder(1);
    topHeader.setCellpadding(0);
    topHeader.setCellspacing(0);
    topHeader.add(new Image("/images/template/kktemplate_03.gif",""),1,1);
  }

  public void intitializeContentFrameTable(){
    contentFrameTable = new Table(1,2);
    contentFrameTable.setCellpadding(0);
    contentFrameTable.setCellspacing(0);
    contentFrameTable.setBorder(1);
    contentFrameTable.setWidth(1,1,"100%");
    contentFrameTable.setHeight(1,1,"100%");
    contentFrameTable.setHeight(1,2,"12");

    Image backGround = new Image("/images/template/kktemplate_18.gif","");
    backGround.setWidth("100%");
    backGround.setHeight(12);

    contentFrameTable.add(backGround,1,2);
  }



  public void initializeMainTable(){
    mainTable = new Table(4,1);
    mainTable.setCellpadding(0);
    mainTable.setCellspacing(0);
    mainTable.setColor("#000000");
    mainTable.setHeight("100%");
    //mainTable.setWidth("100%");
    mainTable.setAlignment("center");
    mainTable.setWidth(1,1,"10");
    mainTable.setWidth(4,1,"10");

    mainTable.setVerticalAlignment(1,1,"top");
    mainTable.setVerticalAlignment(2,1,"top");
    mainTable.setVerticalAlignment(3,1,"top");
    mainTable.setVerticalAlignment(4,1,"top");


    Table leftStripe = new Table(1,4);
    leftStripe.setCellpadding(0);
    leftStripe.setCellspacing(0);
    leftStripe.setHeight("100%");
    leftStripe.setHeight(1,3,"369");
    leftStripe.setWidth(1,3,"140");
    leftStripe.setHeight(1,4,"100%");

    leftStripe.add(new Image("/images/template/kktemplate_02.gif",""),1,1);
    //leftStripe.add(new Image("/images/template/kktemplate_03.gif",""),3,1);


    Link myLink = new Link(new Image("/images/template/kktemplate_06.gif",""));
    myLink.setWindowToOpen(KKMainTemplate.LoginPage.class);
    leftStripe.add(myLink,1,2);
    //leftStripe.add(new Image("/images/template/kktemplate_06.gif",""),1,2);


    leftStripe.setBackgroundImage(1,3,new Image("/images/template/kktemplate_10.gif",""));

    Image grayStripe = new Image("/images/template/kktemplate_14.gif","");
    grayStripe.setHeight("100%");
    grayStripe.setWidth(140);
    leftStripe.add(grayStripe,1,4);

//    leftStripe.add(new Image("/images/template/kktemplate_16.gif",""),1,5);
    leftStripe.setColor("#828282");


    //Links
    Table linkTable = new Table(1,7);
    linkTable.setCellpadding(0);
    linkTable.setCellspacing(0);
    //linkTable.setWidth(140);
    linkTable.setBorder(0);

    linkTable.setHeight(1,"35");
    linkTable.setHeight(2,"35");
    linkTable.setHeight(3,"35");
    linkTable.setHeight(4,"35");
    linkTable.setHeight(5,"35");
    linkTable.setHeight(6,"35");
    linkTable.setHeight(7,"35");

    Text home = new Text("Forsíða");
    home.setFontColor("#FFFFFF");
    home.setFontSize(3);
    Link index = new Link(home,"/index.jsp");
    index.setObject(new Image("/images/links/kk_links_01.gif"));
    index.setFontColor("#FFFFFF");
    index.setBold();

    linkTable.add(index,1,1);



    Text history = new Text("Sagan");
    history.setFontColor("#FFFFFF");
    history.setFontSize(3);
    Link aHistory = new Link(history,"/kk.jsp");
    aHistory.setObject(new Image("/images/links/kk_links_02.gif"));
    aHistory.setFontColor("#FFFFFF");
    aHistory.setBold();

    linkTable.add(aHistory,1,2);


    Text albums = new Text("Plötur");
    albums.setFontColor("#FFFFFF");
    albums.setFontSize(3);
    Link album = new Link(albums,"/cd.jsp");
    album.setObject(new Image("/images/links/kk_links_03.gif"));
    album.setFontColor("#FFFFFF");
    album.setBold();

    linkTable.add(album,1,3);

    Link album4 = new Link(albums,"/cd.jsp");
    album4.setObject(new Image("/images/links/kk_links_04.gif"));
    album4.setFontColor("#FFFFFF");
    album4.setBold();

    linkTable.add(album4,1,4);


    Link album5 = new Link(albums,"/cd.jsp");
    album5.setObject(new Image("/images/links/kk_links_05.gif"));
    album5.setFontColor("#FFFFFF");
    album5.setBold();

    linkTable.add(album5,1,5);


    Link album6 = new Link(albums,"/cd.jsp");
    album6.setObject(new Image("/images/links/kk_links_06.gif"));
    album6.setFontColor("#FFFFFF");
    album6.setBold();

    linkTable.add(album6,1,6);

    leftStripe.setAlignment(1,3,"center");
    leftStripe.setVerticalAlignment(1,3,"top");
    leftStripe.add(Text.getBreak(),1,3);
    leftStripe.add(linkTable,1,3);


    Image leftTail = new Image("/images/template/kktemplate_09.gif","");
    leftTail.setHeight("100%");
    leftTail.setWidth(10);
    mainTable.add(leftTail,1,1);

    Image rightTail = new Image("/images/template/kktemplate_21.gif","");
    rightTail.setHeight("100%");
    rightTail.setWidth(10);
    mainTable.add(rightTail,4,1);


    mainTable.add(leftStripe,2,1);

    //mainTable.add(contentFrameTable,3,1);

    frameTable.add(mainTable,2,1);
  }

  public void showHeaderTable(boolean value){
    showHeaderTable = value;
  }

  public void add1(PresentationObject obj){
    contentTable.add(obj,1,1);
  }

  public void setAlignment1(String alignment){
    contentTable.setAlignment(1,1,alignment);
  }



  public void main(IWContext iwc) throws Exception {
    if(showHeaderTable != showHeaderTableLastValue || first){
      mainTable.emptyCell(3,1);
      mainTable.add(topHeader,3,1);
      if(showHeaderTable){
        mainTable.add(headerTable,3,1);
      }
      mainTable.add(contentTable,3,1);

      first = false;
    }

    showHeaderTableLastValue = showHeaderTable;
  }



  public static class LoginPage extends Window {


    public LoginPage(){
      super(190,140);
      super.setAllMargins(0);
      super.setBackgroundColor("#18224B");
      super.setResizable(false);
      super.setScrollbar(false);
      super.setTitle("Innskráning");

      Table myTable = new Table(1,2);
      myTable.setHeight("100%");
      myTable.setWidth("100%");
      myTable.setHeight(1,"28");
      myTable.setCellpadding(0);
      myTable.setCellspacing(0);
      myTable.setVerticalAlignment(1,1,"middle");
      myTable.setVerticalAlignment(1,2,"middle");
      myTable.setAlignment(1,2,"center");
      myTable.setColor(1,1,"#9BiC11");

      Login login = new Login();
      //login.setColor("#18224B");
      //login.setPasswordTextColor("#FFFFFF");
      //login.setUserTextColor("#FFFFFF");
      login.setColor("#616161");



      Text header = new Text("&nbsp;&nbsp;Innskraning");
      header.setBold();
      header.setFontColor("#FFFFFF");
      header.setFontSize(3);
      header.setFontStyle(Text.FONT_FACE_ARIAL);

      myTable.add(header,1,1);
      myTable.add(login,1,2);
      this.add(myTable);
    }

    public void main(IWContext iwc) throws Exception {
      this.setParentToReload();
    }


  } // InnerClass



} // Class End
package com.idega.block.news.presentation;


import com.idega.core.accesscontrol.business.AccessControl;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.block.news.data.*;
import com.idega.jmodule.image.presentation.ImageInserter;
import com.idega.data.*;
import com.idega.util.text.*;
import javax.servlet.http.*;
import com.idega.util.text.TextSoap;


public class NewsEditor extends JModuleObject{


private String Error;
private boolean isAdmin=false;



private String language = "IS";
private String[] Lang = {"Fréttastjóri","Fyrirsögn", "Frétt", "Flokkur","Höfundur", "Heimild", "Fjöldi sýnilegra daga", "Mynd"};
private String[] IS = {"Fréttastjóri","Fyrirsögn", "Frétt", "Flokkur","Höfundur", "Heimild", "Fjöldi sýnilegra daga", "Mynd"};
private String[] EN = {"News Editor","Headline", "News", "Category","Author", "Source", "Number of visible days", "Image"};



private Text headlinestring;
private Text newsstring;
private Text categorystring;
private Text authorstring;
private Text sourcestring;
private Text daysshownstring;
private Text imagestring;
private String sEditor,sHeadline,sNews,sCategory,sAuthor,sSource,sDaysShown,sImage;

private String attributeName = "union_id";
//private int attributeId = -1;
private int attributeId = 3;

  public NewsEditor(){

  }
  public NewsEditor(boolean isAdmin){
    this.isAdmin=isAdmin;
  }

  private void setSpokenLanguage(ModuleInfo modinfo){

    String language2 = modinfo.getParameter("language");
    if (language2==null) language2 = ( String ) modinfo.getSession().getAttribute("language");
    if ( language2 != null) language = language2;
    if (language.equalsIgnoreCase("IS")){
      Lang = IS;
    }
    else{
      Lang = EN;
    }

  }

  public void setConnectionAttributes(String attributeName, int attributeId) {
    this.attributeName = attributeName;
    this.attributeId = attributeId;
  }

  public void setConnectionAttributes(String attributeName, String attributeId) {
    this.attributeName = attributeName;
    this.attributeId = Integer.parseInt(attributeId);
  }

  public String getColumnString(NewsCategoryAttribute[] attribs){
  String values = "";

    for (int i = 0 ; i < attribs.length ; i++) {
      values += NewsCategory.getNameColumnName()+"_id = '"+attribs[i].getNewsCategoryId()+"'" ;
      if( i!= (attribs.length-1) ) values += " OR ";
    }
  return values;
  }

  public void main(ModuleInfo modinfo)throws Exception{
    this.isAdmin=AccessControl.hasEditPermission(this,modinfo);
    setSpokenLanguage(modinfo);
    String mode = modinfo.getParameter("mode");
    this.sHeadline = Lang[1];
    this.sNews = Lang[2];
    this.sCategory = Lang[3];
    this.sAuthor = Lang[4];
    this.sSource = Lang[5];
    this.sDaysShown = Lang[6];
    this.sImage = Lang[7];
    this.sEditor = Lang[0];

    headlinestring = new Text("<b>"+Lang[1]+"</b>");
    newsstring = new Text("<b>"+Lang[2]+"</b>");
    categorystring = new Text("<b>"+Lang[3]+"</b>");
    authorstring = new Text("<b>"+Lang[4]+"</b>");
    sourcestring = new Text("<b>"+Lang[5]+"</b>");
    daysshownstring = new Text("<b>"+Lang[6]+"</b>");
    imagestring = new Text("<b>"+Lang[7]+"</b>");

    try{
      //check to see if we are doing anything special
      if( mode != null){
        if( mode.equals("update") ){
          //add(drawBrowseTable());
        }
        else if( mode.equals("save")){
          if( storeNews(modinfo) )
            add(feedBack(true));
          else
            add(feedBack(false));
        }
        else if( mode.equals("delete") ){
          add(deleteNews(modinfo.getParameter("news_id")));
        }
      }

      //else we are either writing something new or we are updating something we have selected
      else{
        if(isAdmin){
        add(editorTable(modinfo));
        }
        else add(new Text("<center><b></b>Login First!</b></center>") );
      }
    }
    catch(Exception e){
      add(new Text("villa : "+e.getMessage()) );
      e.printStackTrace(System.err);//something went wrong
    }
  }

  public Text deleteNews(String news_id) throws SQLException{
  News news = new News(Integer.parseInt(news_id));
  news.delete();

  return new Text("Deleted the news");
  }

  public Text getHeaderText(String s){
    Text T = new Text(s);
    T.setBold();
    return T;
  }


  public Table editorTable(ModuleInfo modinfo)throws SQLException, IOException{

    Table mainTable = new Table(1, 2);
    Table topToolTable = new Table(1,1);
    Text tEditor = new Text(sEditor);
    tEditor.setBold();
    tEditor.setFontColor("#FFFFFF");
    tEditor.setFontSize("3");

    topToolTable.add(tEditor,1,1);
    topToolTable.setColor("4d6476");
    topToolTable.setWidth("100%");

    Table mainPanel = new Table(2,4);
    Table sideToolTable = new Table(1,11);

    Form myForm = new Form();


    boolean update=false;
    myForm.add(mainPanel);
    mainTable.add(topToolTable,1,1);
    mainTable.add(myForm,1,2);

    mainPanel.add(getHeaderText(sHeadline),1,1);
    mainPanel.add(getHeaderText(sNews),1,3);
    mainPanel.add(sideToolTable,2,1);

    mainTable.setColor("#EFEFEF");
    mainPanel.setVerticalAlignment(1,1,"top");
    mainPanel.setVerticalAlignment(1,2,"top");
    mainPanel.setVerticalAlignment(1,3,"top");
    mainPanel.setVerticalAlignment(1,4,"top");
    mainPanel.mergeCells(2,1,2,4);

    String news_id = modinfo.getParameter("news_id");
    String category_id = modinfo.getParameter("category_id");

    News news=null;
    HiddenInput newsHidden;

    if(news_id != null){
      news = new News(Integer.parseInt(news_id));
      update = true;
      newsHidden = new HiddenInput("news_id",news_id);
    }
    else
      newsHidden = new HiddenInput("news_id","-1");

    mainPanel.add(newsHidden,1,4);

    TextArea headlineArea;
    if(update) {
      String theheadline = news.getHeadline();
      if( theheadline!=null )
        headlineArea = insertTextArea("NewsHeader",theheadline, 0, 60);
      else
        headlineArea = insertTextArea("NewsHeader","", 0, 60);
    }
    else
      headlineArea = insertTextArea("NewsHeader","", 0, 60);

    mainPanel.add(headlineArea,1,2);

    TextArea newsArea;
    if(update) {
      String thenewstext=news.getText();
      if ( thenewstext!=null)
        newsArea = insertTextArea("NewsText",thenewstext,15, 60);
      else
        newsArea = insertTextArea("NewsText","",15, 60);
    }
    else
      newsArea = insertTextArea("NewsText","",15, 60);

    mainPanel.add(newsArea,1,4);

    DropdownMenu newsCategoryMenu = new DropdownMenu("category_id");

    String attName = NewsCategoryAttribute.getAttributeNameColumnName();
    String attId = NewsCategoryAttribute.getAttributeIdColumnName();
    NewsCategoryAttribute[] attribs = (NewsCategoryAttribute[]) (new NewsCategoryAttribute()).findAllByColumn(attName,attributeName,attId,String.valueOf(attributeId));

    String categoryString = getColumnString(attribs);

    if( (categoryString!=null) && !(categoryString.equals("")) )
      categoryString += " AND ";

    NewsCategory newscat = new NewsCategory();
    String sql = "select * from "+NewsCategory.getNewsCategoryTableName()+" where "+categoryString+ NewsCategory.getValidColumnName()+" ='Y'";
    NewsCategory[] newscats = (NewsCategory[]) newscat.findAll(sql);

    if( newscats!=null || newscats.length!=0){
      for ( int i=0 ; i < newscats.length; i++){
        newsCategoryMenu.addMenuElement(newscats[i].getID(), newscats[i].getName());
      }
      newsCategoryMenu.addMenuElement("-1", "Veldu flokk" );
    }
    else
      newsCategoryMenu.addMenuElement("Finn engan flokk" );

    if(update)
      newsCategoryMenu.setSelectedElement(""+news.getNewsCategoryId());
    else
      newsCategoryMenu.setSelectedElement("-1" );
    newsCategoryMenu.keepStatusOnAction();
    sideToolTable.add(getHeaderText(sCategory),1,1);
    sideToolTable.add(newsCategoryMenu,1,2);

    TextInput authorBox;

    if(update) {
      String theauthor = news.getAuthor();
      if( theauthor!=null )
        authorBox = insertEditBox("author",theauthor);
      else
        authorBox = insertEditBox("author","");
    }
    else
      authorBox = insertEditBox("author");
    sideToolTable.add(getHeaderText(sAuthor),1,3);
    sideToolTable.add(authorBox,1,4);

    TextInput sourceBox;

    if(update) {
      String thesource = news.getSource();
      if( thesource!=null )
        sourceBox = insertEditBox("source",thesource);
      else
        sourceBox = insertEditBox("source","");
    }
    else
      sourceBox = insertEditBox("source");
    sideToolTable.add(getHeaderText(sSource),1,5);
    sideToolTable.add(sourceBox,1,6);

    DropdownMenu counterMenu = counterDropdown( "daysShown", 1, 30);
    counterMenu.addMenuElement("-1", "óákveðið" );

    if(update)
      counterMenu.setSelectedElement(""+news.getDaysShown());
    else
      counterMenu.setSelectedElement("-1");
    sideToolTable.add(getHeaderText(sDaysShown),1,7);
    sideToolTable.add(counterMenu,1,8);
    sideToolTable.add(getHeaderText(sImage), 1, 9);

    ImageInserter imageInsert = new ImageInserter();
      imageInsert.setSelected(false);
      if ( update ) {
        if ( news.getIncludeImage() != null ) {
          if ( news.getIncludeImage().equalsIgnoreCase("y") ) {
            imageInsert.setImageId(news.getImageId());
            imageInsert.setSelected(true);
          }
        }
      }

    sideToolTable.add(imageInsert, 1, 10);
    SubmitButton mySubmit = new SubmitButton("mode", "save");
    sideToolTable.add(mySubmit, 1,11);
    mainPanel.setVerticalAlignment(2,1,"top");
    return mainTable;
  }

  public boolean storeNews(ModuleInfo modinfo)throws SQLException, IOException{

    boolean update=false;
    String news_id = modinfo.getParameter("news_id");

    if ( (news_id!=null) && !(news_id.equals("-1")))
      update=true;

    String newsHeader = modinfo.getParameter("NewsHeader");
    String newsText = modinfo.getParameter("NewsText");

    newsText = TextSoap.findAndReplace(newsText, "“","\"");
    newsText = TextSoap.findAndReplace(newsText, "'","´");

    String category_id = modinfo.getParameter("category_id");

    if( ((newsHeader==null)||(newsHeader.equalsIgnoreCase("")) ) || ((newsText==null)||(newsText.equalsIgnoreCase(""))) || ((category_id==null)||(category_id.equalsIgnoreCase("-1")) )){
        return false;
    }

    News news = null;
    if (update)
      news = new News(Integer.parseInt(news_id));
    else
      news = new News();

    news.setHeadline(newsHeader);
    news.setText(newsText);
    news.setNewsCategoryId(new Integer(category_id));

    String source = modinfo.getParameter("source");
    if ( source!=null )
      news.setSource(source);

    String author = modinfo.getParameter("author");
    if ( author!=null )
      news.setAuthor(author);

    String daysShown = modinfo.getParameter("daysShown");
    if ( daysShown!=null )
      news.setDaysShown(new Integer(daysShown));

    String includeImage = modinfo.getParameter("insertImage");
    System.out.println("includeImage: "+includeImage);
    if( includeImage!=null )
      news.setIncludeImage(includeImage);
    else
      news.setIncludeImage("N");

    String image_id = modinfo.getParameter("image_id");
    System.out.println("imageID: "+image_id);

    if(image_id == null)
      image_id="-1";

    news.setImageId(new Integer(image_id));

    idegaTimestamp date = new idegaTimestamp();

    if (!update)
      news.setDate( date.getTimestampRightNow());

    try{
      if( update ) {
        news.update();
      }
      else{
        news.insert();
      }
    }
    catch(Exception e){
      e.printStackTrace();
      System.out.println(e.getMessage());
    return false;
    }

    return true;
  }

  public TextArea insertTextArea(String TextAreaName, String text, int Height, int Width){
    TextArea textArea =  new TextArea(TextAreaName);
    textArea.setHeight(Height);
    textArea.setWidth(Width);
    textArea.setContent(text);
    textArea.keepStatusOnAction();
    return textArea;
  }

  public TextArea insertTextArea(String TextAreaName, int Height, int Width){
    TextArea textArea =  new TextArea(TextAreaName);
    textArea.setHeight(Height);
    textArea.setWidth(Width);
    textArea.keepStatusOnAction();
    return textArea;
  }

  public TextInput insertEditBox(String name, String text){
    TextInput myInput = new TextInput(name);
    myInput.setContent(text);
    myInput.keepStatusOnAction();
    return myInput;
  }

  public TextInput insertEditBox(String name){
    TextInput myInput = new TextInput(name);
    myInput.keepStatusOnAction();
    return myInput;
  }

  public Table feedBack(boolean isOk){
    Table myTable = new Table(2, 2);
    myTable.mergeCells(1, 1, 2, 1);
    Form actionForm = new Form();
    Form backForm = new Form();
    backForm.add(new BackButton("Til baka"));
    actionForm.add(new SubmitButton("Skoða fréttir"));
    actionForm.setAction("/index.jsp");
    myTable.setAlignment("center");
    //myTable.setBorder(1);

    if(isOk){
      myTable.addText("Fréttin hefur verið skráð", 1, 1);
      myTable.add(backForm, 1, 2);
      myTable.add(actionForm, 2, 2);
    }
    else {
      myTable.addText("Fylla verður í alla reiti auk þess að velja flokk", 1, 1);
      myTable.add(new BackButton("Til baka"), 1, 2);
    }
    return myTable;
  }

  public FileInput insertFileInput(String FileInputName){
    FileInput myFileInput = new FileInput(FileInputName);
    return myFileInput;
  }

  public DropdownMenu insertDropdown(String dropdownName)throws IOException, SQLException{
    DropdownMenu myDropdown = new DropdownMenu(dropdownName);
    myDropdown.addMenuElement("Y", "Frétt");
    myDropdown.addMenuElement("N", "Tilkynning");
    myDropdown.setSelectedElement("Y");
    myDropdown.setToSubmit();
    myDropdown.keepStatusOnAction();

    return myDropdown;
  }

  public DropdownMenu counterDropdown(String dropdownName, int countFrom, int countTo)
  {
    String from = Integer.toString(countFrom);
    DropdownMenu myDropdown = new DropdownMenu(dropdownName);

    for(; countFrom <= countTo; countFrom++){
      myDropdown.addMenuElement(Integer.toString(countFrom), Integer.toString(countFrom));
    }
    myDropdown.keepStatusOnAction();

    return myDropdown;
  }

  public DropdownMenu newsDropdown(String newsDropdownName, String categoryDropdownName, Connection Conn, ModuleInfo modinfo)throws IOException, SQLException{
    DropdownMenu myDropdown = new DropdownMenu(newsDropdownName);
    String cotgegoryId = modinfo.getParameter(categoryDropdownName);

    Statement Stmt = Conn.createStatement();
    StringBuffer sql = new StringBuffer( "select ");
    sql.append(News.getNewsTableName());
    sql.append("_id,");
    sql.append(News.getHeadLineColumnName());
    sql.append(" from ");
    sql.append(News.getNewsTableName());
    sql.append(" where ");
    sql.append(NewsCategory.getNewsCategoryTableName());
    sql.append("_id = '");
    sql.append(cotgegoryId);
    sql.append("' ORDER BY ");
    sql.append(News.getHeadLineColumnName());
    System.err.println(sql.toString());
    ResultSet RS = Stmt.executeQuery(sql.toString());
    myDropdown.addMenuElement("-1", "Veldu frétt" );
    while(RS.next()){
      myDropdown.addMenuElement(RS.getString(News.getNewsTableName()+"_id"), RS.getString(News.getHeadLineColumnName()));
    }
    myDropdown.keepStatusOnAction();
    myDropdown.setToSubmit();
    RS.close();
    Stmt.close();


    return myDropdown;
  }


  public DropdownMenu categoryDropdown(String categoryDropdownName, Connection Conn)throws IOException, SQLException
  {
    //PrintWriter out = modinfo.getResponse().getWriter();
    DropdownMenu myDropdown = new DropdownMenu(categoryDropdownName);

    try
    {
        Statement Stmt = Conn.createStatement();
        StringBuffer sql = new StringBuffer( "select ");
        sql.append(NewsCategory.getNewsCategoryTableName());
        sql.append("_id, ");
        sql.append(NewsCategory.getNameColumnName());
        sql.append(" from ");
        sql.append(NewsCategory.getNewsCategoryTableName());
        sql.append(" where valid = 'Y' ORDER BY ");
        sql.append(NewsCategory.getNewsCategoryTableName());
        sql.append("_id");
        System.err.println(sql.toString());
        ResultSet RS = Stmt.executeQuery(sql.toString());
        myDropdown.addMenuElement("-1", "Veldu fréttaflokk" );
        while(RS.next()){
          myDropdown.addMenuElement(RS.getString(NewsCategory.getNewsCategoryTableName()+"_id"), RS.getString(NewsCategory.getNameColumnName()) );
        }
        myDropdown.keepStatusOnAction();
        myDropdown.setToSubmit();
        RS.close();
        Stmt.close();
    }
    catch (SQLException E) {
      System.err.print("SQLException: " + E.getMessage());
      System.err.print("SQLState:     " + E.getSQLState());
      System.err.print("VendorError:  " + E.getErrorCode());
    }
    return myDropdown;
  }

}

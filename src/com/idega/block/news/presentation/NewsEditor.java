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
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;


public class NewsEditor extends JModuleObject{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.news";
private String Error;
private boolean isAdmin=false;

private String sEditor,sHeadline,sNews,sCategory,sAuthor,sSource,sDaysShown,sImage;

private String attributeName = "union_id";
private int attributeId = 3;
private IWBundle iwb;
private IWResourceBundle iwrb;

  public NewsEditor(){

  }
  public NewsEditor(boolean isAdmin){
    this.isAdmin=isAdmin;
  }

  public void main(ModuleInfo modinfo)throws Exception{
    /**
     * @todo permission
     */
    this.isAdmin=true;  //AccessControl.hasEditPermission(this,modinfo);
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);

    String mode = modinfo.getParameter("mode");
    this.sHeadline = iwrb.getLocalizedString("headline","Headline");
    this.sNews = iwrb.getLocalizedString("news","News");;
    this.sCategory = iwrb.getLocalizedString("category","Category");;
    this.sAuthor = iwrb.getLocalizedString("author","Author");;
    this.sSource = iwrb.getLocalizedString("source","Source");;
    this.sDaysShown = iwrb.getLocalizedString("visible_days","Number of days visible");;
    this.sImage = iwrb.getLocalizedString("image","Image");;
    this.sEditor = iwrb.getLocalizedString("news_editor","News Editor");;

    this.getParentPage().setAllMargins(0);
    this.getParentPage().setTitle(sEditor);

    Text textTemplate = new Text();
      textTemplate.setFontSize(Text.FONT_SIZE_7_HTML_1);
      textTemplate.setBold();
      textTemplate.setFontFace(Text.FONT_FACE_VERDANA);

    try{
      //check to see if we are doing anything special
      if( mode != null){
        if( mode.equals("save")){
          if( storeNews(modinfo) )
            add(feedBack(true));
          else
            add(feedBack(false));
        }
        else if( mode.equals("delete") ){
          deleteNews(modinfo.getParameter("news_id"));
        }
      }

      //else we are either writing something new or we are updating something we have selected
      else{
        if(isAdmin){
        add(editorTable(modinfo));
        }
        else add(new Text("<center><b>"+iwrb.getLocalizedString("login_first","Login first!")+"</b></center>") );
      }
    }
    catch(Exception e){
      add(new Text("villa : "+e.getMessage()) );
      e.printStackTrace(System.err);//something went wrong
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

  public void deleteNews(String news_id) throws SQLException{
    com.idega.block.news.data.News news = new com.idega.block.news.data.News(Integer.parseInt(news_id));
    news.delete();

    getParentPage().setParentToReload();
    getParentPage().close();
  }

  public Text getHeaderText(String s){
    Text textTemplate = new Text(s);
      textTemplate.setFontSize(Text.FONT_SIZE_7_HTML_1);
      textTemplate.setBold();
      textTemplate.setFontFace(Text.FONT_FACE_VERDANA);
    return textTemplate;
  }


  public Form editorTable(ModuleInfo modinfo)throws SQLException, IOException{

    Table mainTable = new Table(2, 2);
      mainTable.setWidth("100%");
      mainTable.setHeight("100%");
      mainTable.setCellpadding(0);
      mainTable.setCellspacing(0);
      mainTable.mergeCells(1,1,2,1);
      mainTable.setBorder(0);
      mainTable.setRowVerticalAlignment(2,"top");
      mainTable.setWidth(1,2,"392");
      mainTable.setWidth(2,2,"178");

    Table topTable = new Table(2,1);
      topTable.setCellpadding(0);
      topTable.setCellspacing(0);
      topTable.setAlignment(2,1,"right");
      topTable.setWidth("100%");

    Image idegaweb = iwb.getImage("shared/idegaweb.gif","idegaWeb",90,25);
      topTable.add(idegaweb,1,1);
    Text tEditor = new Text(sEditor+"&nbsp;&nbsp;");
      tEditor.setBold();
      tEditor.setFontColor("#FFFFFF");
      tEditor.setFontSize("3");
      topTable.add(tEditor,2,1);

    Table mainPanel = new Table(1,2);
      mainPanel.setAlignment("center");
      mainPanel.setCellpadding(8);
      //mainPanel.setWidth("100%");
    Table sideToolTable = new Table(1,6);
      sideToolTable.setAlignment("center");
      //sideToolTable.setWidth("100%");
      sideToolTable.setCellpadding(8);

    Form myForm = new Form();


    boolean update=false;
    mainTable.add(topTable,1,1);
    mainTable.add(mainPanel,1,2);
    myForm.add(mainTable);

    mainPanel.add(getHeaderText(sHeadline),1,1);
    mainPanel.add(Text.getBreak(),1,1);
    mainPanel.add(getHeaderText(sNews),1,2);
    mainPanel.add(Text.getBreak(),1,2);
    mainTable.add(sideToolTable,2,2);

    mainTable.setColor(1,1,"#0E2456");
    mainTable.setColor(1,2,"#FFFFFF");
    mainTable.setColor(2,2,"#EFEFEF");
    mainPanel.setVerticalAlignment(1,1,"top");
    mainPanel.setVerticalAlignment(1,2,"top");

    String news_id = modinfo.getParameter("news_id");
    String category_id = modinfo.getParameter("category_id");

    com.idega.block.news.data.News news=null;
    HiddenInput newsHidden;

    if(news_id != null){
      news = new com.idega.block.news.data.News(Integer.parseInt(news_id));
      update = true;
      newsHidden = new HiddenInput("news_id",news_id);
    }
    else
      newsHidden = new HiddenInput("news_id","-1");

    mainPanel.add(newsHidden,1,2);

    TextInput headlineArea;
    if(update) {
      String theheadline = news.getHeadline();
      if( theheadline!=null )
        headlineArea = insertEditBox("NewsHeader",theheadline);
      else
        headlineArea = insertEditBox("NewsHeader");
    }
    else
      headlineArea = insertEditBox("NewsHeader");
      headlineArea.setSize(50);
      headlineArea.setMaxlength(255);

    mainPanel.add(headlineArea,1,1);

    TextArea newsArea;
    if(update) {
      String thenewstext=news.getText();
      if ( thenewstext!=null)
        newsArea = insertTextArea("NewsText",thenewstext,22, 65);
      else
        newsArea = insertTextArea("NewsText","",22, 65);
    }
    else
      newsArea = insertTextArea("NewsText","",22, 65);

    mainPanel.add(newsArea,1,2);

    DropdownMenu newsCategoryMenu = new DropdownMenu("category_id");
    newsCategoryMenu.setAttribute("style","font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000");

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
    sideToolTable.add(Text.getBreak(),1,1);
    sideToolTable.add(newsCategoryMenu,1,1);

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
    sideToolTable.add(getHeaderText(sAuthor),1,2);
    sideToolTable.add(Text.getBreak(),1,2);
    sideToolTable.add(authorBox,1,2);

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
    sideToolTable.add(getHeaderText(sSource),1,3);
    sideToolTable.add(Text.getBreak(),1,3);
    sideToolTable.add(sourceBox,1,3);

    DropdownMenu counterMenu = counterDropdown( "daysShown", 1, 30);
    counterMenu.addMenuElement("-1", iwrb.getLocalizedString("undetermined","Undetermined") );
    counterMenu.setAttribute("style","font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000");

    if(update)
      counterMenu.setSelectedElement(""+news.getDaysShown());
    else
      counterMenu.setSelectedElement("-1");
    sideToolTable.add(getHeaderText(sDaysShown),1,4);
    sideToolTable.add(Text.getBreak(),1,4);
    sideToolTable.add(counterMenu,1,4);
    sideToolTable.add(getHeaderText(sImage), 1, 5);
    sideToolTable.add(Text.getBreak(),1,5);

    ImageInserter imageInsert = new ImageInserter();
      imageInsert.setSelected(false);
      if ( update ) {
        if ( news.getIncludeImage() != null ) {
          if ( news.getIncludeImage().equalsIgnoreCase("y") ) {
            imageInsert.setSelected(true);
          }
          if ( news.getImageId() != -1 ) {
            imageInsert.setImageId(news.getImageId());
          }
        }
      }

    sideToolTable.add(imageInsert, 1, 5);
    SubmitButton mySubmit = new SubmitButton(iwrb.getImage("save.gif"));
    HiddenInput submitInput = new HiddenInput("mode","save");
    sideToolTable.setAlignment(1,6,"center");
    sideToolTable.add(submitInput, 1,6);
    sideToolTable.add(mySubmit, 1,6);
    return myForm;
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

    com.idega.block.news.data.News news = null;
    if (update)
      news = new com.idega.block.news.data.News(Integer.parseInt(news_id));
    else
      news = new com.idega.block.news.data.News();

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
    if( includeImage!=null )
      news.setIncludeImage(includeImage);
    else
      news.setIncludeImage("N");

    String image_id = modinfo.getParameter("image_id");
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
    textArea.setAttribute("style","font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000");
    textArea.setHeight(Height);
    textArea.setWidth(Width);
    textArea.setContent(text);
    textArea.keepStatusOnAction();
    return textArea;
  }

  public TextArea insertTextArea(String TextAreaName, int Height, int Width){
    TextArea textArea =  new TextArea(TextAreaName);
    textArea.setAttribute("style","font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000");
    textArea.setHeight(Height);
    textArea.setWidth(Width);
    textArea.keepStatusOnAction();
    return textArea;
  }

  public TextInput insertEditBox(String name, String text){
    TextInput myInput = new TextInput(name);
    myInput.setAttribute("style","font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000");
    myInput.setContent(text);
    myInput.keepStatusOnAction();
    return myInput;
  }

  public TextInput insertEditBox(String name){
    TextInput myInput = new TextInput(name);
    myInput.setAttribute("style","font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000");
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
      getParentPage().setParentToReload();
      getParentPage().close();
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
    sql.append(com.idega.block.news.data.News.getNewsTableName());
    sql.append("_id,");
    sql.append(com.idega.block.news.data.News.getHeadLineColumnName());
    sql.append(" from ");
    sql.append(com.idega.block.news.data.News.getNewsTableName());
    sql.append(" where ");
    sql.append(NewsCategory.getNewsCategoryTableName());
    sql.append("_id = '");
    sql.append(cotgegoryId);
    sql.append("' ORDER BY ");
    sql.append(com.idega.block.news.data.News.getHeadLineColumnName());
    System.err.println(sql.toString());
    ResultSet RS = Stmt.executeQuery(sql.toString());
    myDropdown.addMenuElement("-1", "Veldu frétt" );
    while(RS.next()){
      myDropdown.addMenuElement(RS.getString(com.idega.block.news.data.News.getNewsTableName()+"_id"), RS.getString(com.idega.block.news.data.News.getHeadLineColumnName()));
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

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}

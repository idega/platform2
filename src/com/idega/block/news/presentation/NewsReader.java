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
import com.idega.data.*;
import com.idega.util.text.*;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;


/*
**
** need to look at numberofdisplayed news
**
*/

public class NewsReader extends JModuleObject{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.news";
private boolean isAdmin=false;
private boolean showNewsCollectionButton=true;
private int categoryId = 0;
private String date = null;
private boolean backbutton = false;
private boolean cutNews = true;// or default true?
private boolean showAll = false;
private Table outerTable = new Table(1,1);
private String newsReaderURL;
private String newsCollectionURL;
private boolean showImages = true;
private boolean showOnlyDates = false;
private boolean headlineAsLink = false;
private String selectFrom = "select nw_news.* from nw_news where ";
private String orderBy = " order by news_date DESC";
private String sNewsCategoryId = "nw_news_cat_id ='";
private String sNewsEditorUrl ="/news/editor.jsp";
private String headlineImageURL = "/pics/jmodules/news/nanar2.gif";
private boolean showHeadlineImage = false;
private boolean showMoreButton = false;
private Window adminWindow;

private Text textProxy = new Text();
private Text headlineProxy = new Text();
private Text informationProxy = new Text();

private int numberOfLetters = 273;
private int numberOfDisplayedNews = 3;
private int numberOfExpandedNews = 3;
private int currentColumnPosition = 1;
private int currentRowPosition = 1;
private String outerTableWidth = "100%";

private boolean limitNumberOfNews = false;

private Image change;
private Image delete;
private Image editor;
private Image collection;
private Image more;
private Image back;

private String language = "IS";

private int textSize = 2;

private String attributeName = "union_id";
//private int attributeId = -1;
private int attributeId = 3;

public static final int SINGLE_FILE_LAYOUT = 1;
public static final int NEWS_SITE_LAYOUT = 2;
public static final int NEWS_PAPER_LAYOUT = 3;
private int LAYOUT = 1;



public NewsReader(){
  showAll = true;
}


public NewsReader(String date){
  this.date=date;
  this.showAll=true;
}

public NewsReader(idegaTimestamp timestamp){
  this.showAll=true;
  this.date = timestamp.toSQLString();
}


public NewsReader(int categoryId, String date){
  this.categoryId=categoryId;
  this.date=date;
  this.showAll = false;
}

public NewsReader(int categoryId){
  this.categoryId=categoryId;
  this.showAll = false;
}

public NewsReader(int categoryId, idegaTimestamp timestamp){
  this.showAll=false;
  this.categoryId=categoryId;
  this.date = timestamp.toSQLString();
}

public void setNewsEditorUrl(String url){
  this.sNewsEditorUrl = url;
}

public void main(ModuleInfo modinfo)throws Exception{
  this.isAdmin = AccessControl.hasEditPermission(this,modinfo);
  IWBundle iwb = getBundle(modinfo);

  IWResourceBundle iwrb = getResourceBundle(modinfo);

  if( newsReaderURL == null ){
    newsReaderURL = iwb.getProperty("news_reader_url",modinfo.getRequestURI());//link with "" constructs a link to the calling page
  }
  if( newsCollectionURL == null ){
    newsCollectionURL = iwb.getProperty("news_collection_url",modinfo.getRequestURI());
  }

  back = iwrb.getImage("back.gif");
  more  = iwrb.getImage("more.gif");
  change = iwrb.getImage("change.gif");
  delete = iwrb.getImage("delete.gif");
  editor = iwrb.getImage("newseditor.gif");
  collection = iwrb.getImage("collection.gif");

  adminWindow = new Window("AdminWindow",NewsEditor.class,com.idega.jmodule.object.Page.class);
    adminWindow.setWidth(570);
    adminWindow.setHeight(550);  boolean byDate=false;

  News[] news = new News[1];

  String news_id = modinfo.getRequest().getParameter("news_id");
  String news_category_id = modinfo.getRequest().getParameter("news_category_id");
//added for multiple newsreader support in one page
  boolean showSingleNews = false;


  try{
    //debug this is not done yet!
    if(news_category_id !=null) {
        categoryId = Integer.parseInt(news_category_id);//overrides the preset category
        showSingleNews = false;//nope we are showing a collection!
        showAll = false;
        //System.out.println("inni í category");
    }else if( (news_category_id==null) && (news_id != null) ){ //yup only one to see if this. owns the newscategory!
     // System.out.println("(news_category_id==null) && (news_id =="+news_id);
      /*if( categoryId != 0 ){
         System.out.println("categoryId != 0 ");
        if( news[0].getNewsCategoryId() == categoryId ) {
          System.out.println("My category! ");
          showSingleNews = true;
        }
      }
      else{
       System.out.println("categoryId is 0 ");
       showSingleNews = true;
      }
      */
      showSingleNews = true;
    }

    if(isAdmin) {
      Link newsEditor = new Link(editor,adminWindow);
      add(newsEditor);
    }

    if(showSingleNews){//single news
      news[0] = new News(Integer.parseInt(news_id));
      showAll=false;
      //news[0] = new News(Integer.parseInt(news_id)); did this before
      backbutton=true;
      cutNews = false;
      setNumberOfDisplayedNews(1);
      add(drawNewsTable(news));
    }
    else{//view all or category view

      NewsCategoryAttribute[] attribs = null;
      String categoryString = null;

      if ( categoryId == 0) {//show the whole collection
        setLayout(SINGLE_FILE_LAYOUT);
        showAll = true;
        String attName = NewsCategoryAttribute.getAttributeNameColumnName();
        String attId = NewsCategoryAttribute.getAttributeIdColumnName();
        attribs = (NewsCategoryAttribute[]) (new NewsCategoryAttribute()).findAllByColumn(attName,attributeName,attId,String.valueOf(attributeId));
        categoryString = getColumnString(attribs);

        //gimmi bætti við 14.2.01
        if (attribs.length > 0) {
          this.categoryId = attribs[0].getNewsCategoryId();
        }
      }
      else{
        showAll = false;
        categoryString = NewsCategoryAttribute.getNewsCategoryIdColumnName()+" = '"+categoryId+"' ";

      }

      if( date == null ){//not by Date
        if( showAll ) {
          if( categoryString!=null )
            categoryString = " where " + categoryString;
          else
            categoryString = "";

          news = (News[]) (new News()).findAll("select * from "+News.getNewsTableName()+" "+categoryString+orderBy);
        }
        else news = (News[]) (new News()).findAllByColumn(News.getNewsCategoryIdColumnName(),categoryId);
      }
      else{// by date
      //debug
     // System.err.println(date);
        String DatastoreType = getDatastoreType( new News() );
        byDate=true;
        if( (categoryString!=null) && !(categoryString.equals("")) ) categoryString = " OR " + categoryString;
        if( (showAll) && !(DatastoreType.equals("oracle"))  ){
          String statementstring = selectFrom+News.getNewsDateColumnName()+" >= '"+date+"'  "+categoryString+orderBy;
          //debug eiki
        //  System.err.println(statementstring);
          news = (News[]) (new News()).findAll(statementstring);
         // if (news != null) System.err.println("fann "+ news.length +"fréttir");

        }
        else if( (showAll) && (DatastoreType.equals("oracle"))  )  news = (News[]) (new News()).findAll(selectFrom+News.getNewsDateColumnName()+" >= "+date+" "+categoryString+orderBy);
        else {
          if ( !(DatastoreType.equals("oracle")) ){

          String statementstring = selectFrom+sNewsCategoryId+categoryId+"' and "+News.getNewsDateColumnName()+" >= '"+date+"'"+orderBy;
//System.err.println(statementstring);

           news = (News[]) (new News()).findAll(statementstring);

          }
          else news = (News[]) (new News()).findAll(selectFrom+sNewsCategoryId+categoryId+"' and "+News.getNewsDateColumnName()+" >= "+date+orderBy);
        }
      }

      if(news.length > 0){	add(drawNewsTable(news));}
      else {//if out of date range
        if( byDate ){
          date=null;
          categoryString = TextSoap.findAndCut(categoryString,"OR");
          String statementstring = selectFrom+categoryString+orderBy;
          //debug eiki 24.dec
         // System.err.println("OUT OF RANGE "+statementstring);
          news = (News[]) (new News()).findAll(statementstring);

          if( news.length!=0){
            setNumberOfDisplayedNews(1);
            add(drawNewsTable(news));
          }
          else add(new Text("<b>No news in database</b><br>"));
        }
        else add(new Text("<b>No news in database</b><br>"));

      }
    }

    if ( news_id == null){
        Table newsCollection = new Table(1,1);
        if ( showNewsCollectionButton ) {
        Link collectionLink = new Link( collection, newsCollectionURL);
        collectionLink.addParameter("news_category_id",Integer.toString(categoryId));
        newsCollection.add(collectionLink,1,1);
        newsCollection.setAlignment("right");
        add(newsCollection);
        }
    }
  }
  catch( Exception e ){

    add(new Text(e.getMessage()) );//something went wrong
    e.printStackTrace( System.err);
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

public void setNumberOfDays( int daysIn ){

  idegaTimestamp stamp= idegaTimestamp.RightNow();
  stamp.addDays(-daysIn);//dagar inni
  this.date= stamp.toSQLString();

}

private String getDatastoreType(GenericEntity entity){
  return DatastoreInterface.getDatastoreType(entity.getDatasource());
}



private String getColumnString(NewsCategoryAttribute[] attribs){
  StringBuffer values = new StringBuffer("");
  for (int i = 0 ; i < attribs.length ; i++) {
    values.append(attribs[i].getNewsCategoryIdColumnName()+" = '");
    values.append(attribs[i].getNewsCategoryId());
    values.append("'") ;
    if( i!= (attribs.length)-1 ) values.append(" OR ");
  }
  String returnString = values.toString();
  if (returnString.equalsIgnoreCase("")) return null;
  else return returnString;
}


private Table drawNewsTable(News[] news)throws IOException,SQLException{
  idegaCalendar funcDate = new idegaCalendar();
  int news_category_id;
  NewsCategory newscat;
  String news_category;
  String headline;
  String newstext;
  String includeImage;
  String timestamp;
  String author;
  String source;
  int daysshown;
  Text information;
  int image_id;
  int newsLength = news.length;

//System.out.println("limitNumberOfNews: "+ limitNumberOfNews+" numberOfDisplayedNews: "+numberOfDisplayedNews+" newsLength: "+newsLength);
  if( (!limitNumberOfNews) || (numberOfDisplayedNews>newsLength) ){
     numberOfDisplayedNews = newsLength;
  }

  outerTable = createContainerTable();

  for ( int i=0; i<numberOfDisplayedNews; i++){
    news_category_id = news[i].getNewsCategoryId();
    newscat = new NewsCategory(news_category_id);
    news_category = newscat.getName();

    headline = news[i].getHeadline();
    includeImage = news[i].getIncludeImage();
    timestamp = (news[i].getDate()).toString();
    author = news[i].getAuthor();
    source = news[i].getSource();
    daysshown = news[i].getDaysShown();


    if ( (i-numberOfExpandedNews)>=0 ){
      includeImage = "N";
      newstext = "";
    }
    else {
      newstext = news[i].getText();
      newstext=formatNews(newstext);
    }

    information = getInfoText(author, source, news_category, timestamp);
    if("Y".equals(includeImage)){
      image_id = news[i].getImageId();
      addNext(insertTable(timestamp, headline, newstext,information,news[i].getID(),image_id));
    }
    else{
      image_id=-1;
      addNext(insertTable(timestamp, headline, newstext,information,news[i].getID(),image_id));
    }

  }

  return outerTable;
}



private Table insertTable(String TimeStamp, String Headline, String NewsText, Text information,int newsId,int image_id) throws SQLException
{

  Text headline = new Text(Headline);
  boolean showMore = false;

  //cut of news
  if(cutNews){
    if(NewsText.length() >= numberOfLetters){
            showMore=true;
            NewsText=NewsText.substring(0,numberOfLetters)+"...";
    }
    else if (NewsText.length()<5) {
            showMore=false;
    }
  }


  getHeadlineProxy().setBold();
  headline = setHeadlineAttributes(headline);
  if( headline.getAttribute("size") == null ) headline.setFontSize(2);
  else if( headline.getAttribute("size").equals("") )  headline.setFontSize(2);


  Text newstext = new Text(NewsText);
  newstext = setTextAttributes( newstext );
  if( newstext.getAttribute("size") == null ) newstext.setFontSize(2);
  else if( newstext.getAttribute("size").equals("") )  newstext.setFontSize(2);

  getInformationProxy().setFontColor("#666666");
  information = setInformationAttributes(information);
  if( information.getAttribute("size") == null ) information.setFontSize(1);
  else if( information.getAttribute("size").equals("") )  information.setFontSize(1);

  Table newsTable = new Table();
  newsTable.setWidth("100%");
  newsTable.add(information, 1, 1);

  if ( this.showHeadlineImage ) {
    Image headlineImage = new Image(headlineImageURL,"");
    headlineImage.setAttribute("align","absmiddle");
    newsTable.add(headlineImage, 1, 2);
  }

  if ( headlineAsLink ) {
    Link headlineLink = new Link(headline,getNewsReaderURL());
    headlineLink.addParameter("news_id",newsId);
    newsTable.add(headlineLink, 1, 2);
  }
  else {
    newsTable.add(headline, 1, 2);
  }


  if (image_id!=-1){
      //System.out.println("ImageID != -1");
    //debug
    if ( showImages ) {
      //System.out.println("ImageID != -1 && showImages");
      Table imageTable = new Table(1, 2);
      Image newsImage = new Image(image_id);
      imageTable.setAlignment("right");
      imageTable.setVerticalAlignment("top");
      imageTable.add(newsImage, 1, 1);

      if( (LAYOUT!=NEWS_SITE_LAYOUT) ){
        newsTable.add(imageTable, 1, 3);
      }
      else{
       if(currentRowPosition==1){
        newsTable.add(imageTable, 1, 3);
       }
      }
    }

  }

  newsTable.add(newstext, 1, 3);
  newsTable.setRowVerticalAlignment(3, "Top");

  if( backbutton ) {
         newsTable.add(new BackButton(back), 1, 4);
  }
  else {
    //if(showMore && !headlineAsLink) {//always show the more button
      if(showMoreButton) {
        if ( !NewsText.equals("") ) { newsTable.add(Text.getBreak(),1,3); }

        Link moreLink = new Link(more,newsReaderURL);
        moreLink.addParameter("news_id",newsId);
        newsTable.add(moreLink, 1, 4);
      }
  }


  if(isAdmin) {
    Table links = new Table(2,1);
    Link newsEdit = new Link(change,adminWindow);
    newsEdit.addParameter("news_id",newsId);

    Link newsDelete = new Link(delete,adminWindow);
    newsDelete.addParameter("news_id",newsId);
    newsDelete.addParameter("mode","delete");

    links.setAlignment(1,1,"left");
    links.setAlignment(2,1,"right");
    links.add(newsEdit,2,1);
    links.add(newsDelete,1,1);

    newsTable.add(links,1,3);

  }

  return newsTable;
}


private String formatDateWithTime(String date ,String DatastoreType)
{
  StringBuffer ReturnString = new StringBuffer("");

  //String ReturnString = date.substring(5, 7);

  if ( !language.equalsIgnoreCase("IS") ){
    if ( !(DatastoreType.equals("oracle"))){//month/day
      ReturnString.append(date.substring(8, 10));
      ReturnString.append("/");
      ReturnString.append(date.substring(0, 4));
      ReturnString.append("/");
      ReturnString.append(date.substring(5, 7));
      ReturnString.append(" ");
      ReturnString.append(date.substring(11, 13));
      ReturnString.append(":");
      ReturnString.append(date.substring(14, 16));
    }
    else {
      ReturnString.append(date.substring(0, 4));
      ReturnString.append("/");
      ReturnString.append(date.substring(5, 7));
      ReturnString.append("/");
      ReturnString.append(date.substring(8, 10));
      ReturnString.append(" ");
      ReturnString.append(date.substring(11, 13));
      ReturnString.append(":");
      ReturnString.append(date.substring(14, 16));

   //2000-10-10 00:00:00.0
   //30/10/2000 17:58
    }
  }
  else {
     if ( !(DatastoreType.equals("oracle"))){
      ReturnString.append(date.substring(8, 10));
      ReturnString.append("/");
      ReturnString.append(date.substring(5, 7));
      ReturnString.append("/");
      ReturnString.append(date.substring(0, 4));
      ReturnString.append(" ");
      ReturnString.append(date.substring(11, 13));
      ReturnString.append(":");
      ReturnString.append(date.substring(14, 16));
    }
    else {
      ReturnString.append(date.substring(0, 4));
      ReturnString.append("/");
      ReturnString.append(date.substring(8, 10));
      ReturnString.append("/");
      ReturnString.append(date.substring(5, 7));
      ReturnString.append(" ");
      ReturnString.append(date.substring(11, 13));
      ReturnString.append(":");
      ReturnString.append(date.substring(14, 16));

    }




  }
  return ReturnString.toString();
}

//debug StringBuffer this
private Text getInfoText(String Author, String Source, String Category, String TimeStamp)
{
  Text information = new Text();
  String DatastoreType = getDatastoreType( new News() );
  TimeStamp = formatDateWithTime(TimeStamp,DatastoreType);

  if ( showOnlyDates ) {
    idegaTimestamp timeStamp = new idegaTimestamp(TimeStamp.substring(6,10)+"-"+TimeStamp.substring(3,5)+"-"+TimeStamp.substring(0,2));

    String date = Integer.toString(timeStamp.getDate());
    if ( date.length() == 1 ) date = "0" + date;
    String month = Integer.toString(timeStamp.getMonth());
    if ( month.length() == 1 ) month = "0" + month;
    String year = Integer.toString(timeStamp.getYear());

    information = new Text(date+"."+month+"."+year);
  }

  else {

      if((Author == null||  Author.equals("")) && (Source == null || (Source.equals(""))))
              information = new Text(Category+" | "+TimeStamp);

      else if(Author == null || Author.equals("")){
              if(Source != null || !(Source.equals("")))
                      information = new Text(Category+" | "+Source+" | "+TimeStamp);
      }

      else if(Source == null || Source.equals("")){
              if(Author != null || !(Author.equals("")))
                      information = new Text(Category+" | "+Author+" | "+TimeStamp);
      }

      else
              information = new Text(Category+" | "+Author+" | "+Source+" | "+TimeStamp);

  }

  return information;
}


private void addNext(Table table){
 switch (LAYOUT) {
   case SINGLE_FILE_LAYOUT:
    outerTable.add(table, 1, 1);
    outerTable.addBreak(1,1);
    outerTable.addBreak(1,1);
     break;
   case  NEWS_SITE_LAYOUT:
    //debug NEWS_SITE_LAYOUT shows only one image for now...code in insertTable
    //teljari með staðsetningu
      if( (currentColumnPosition==1) && (currentRowPosition==1) && (numberOfDisplayedNews>0)) {
        outerTable.add(table, 1, 1);
        outerTable.setVerticalAlignment(1, 1,"top");
        currentRowPosition++;
      }
      else{
        if( (currentColumnPosition==1) && (currentRowPosition!=2) ){
           outerTable.add(table, 1, currentRowPosition);
           outerTable.setVerticalAlignment(1,currentRowPosition,"top");
           currentColumnPosition=2;
        }
        else if(currentColumnPosition==2){
          outerTable.add(table, 2, currentRowPosition);
          outerTable.setVerticalAlignment(2, currentRowPosition,"top");
          currentColumnPosition=1;
          currentRowPosition++;
        }
        else {
          outerTable.add(table, 1, currentRowPosition);
          outerTable.setVerticalAlignment(1, currentRowPosition,"top");
          currentColumnPosition=2;
        }
      }
     break;
   case NEWS_PAPER_LAYOUT:
       //flowtable

    break;
 }

}

private Table createContainerTable(){
  Table temp;

   switch (LAYOUT) {
   case SINGLE_FILE_LAYOUT:
    temp = new Table(1,1);
    outerTable.setAlignment("center");
     break;
   case  NEWS_SITE_LAYOUT:
    int rows = (numberOfDisplayedNews/2) ;
    int theRest = (numberOfDisplayedNews%2);
    if( theRest==0) rows++;
    else rows+=theRest;

    temp = new Table(2,rows);
    temp.setCellspacing(6);;
    temp.setWidth(1,"50%");
    temp.setWidth(2,"50%");
    temp.mergeCells(1,1,2,1);
     break;
   case NEWS_PAPER_LAYOUT:
    temp = new Table(3,1);
    break;
   default: temp = new Table(1,1);
  }

  temp.setWidth(outerTableWidth);
  return temp;
}

/*
** This method uses static layouts from this class
**
*/
public void setLayout(int LAYOUT){
  this.LAYOUT = LAYOUT;
}


/**
*
* return a proxy for the main text. Use the standard
* set methods on this object such as .setFontSize(1) etc.
* and it will set the property for all texts.
*/
public Text getTextProxy(){

return textProxy;
}

public Text getHeadlineProxy(){

return headlineProxy;
}

public Text getInformationProxy(){

return informationProxy;
}

public void setTextProxy(Text textProxy){
	this.textProxy = textProxy;
}

public void setHeadlineProxy(Text headlineProxy){
	this.headlineProxy = headlineProxy;
}

public void setInformationProxy(Text informationProxy){
	this.informationProxy = informationProxy;
}


public Text setTextAttributes( Text realText ){

Text tempText = (Text) textProxy.clone();
tempText.setText( realText.getText() );

return tempText;
}

public Text setHeadlineAttributes( Text realText ){

Text tempText = (Text) headlineProxy.clone();
tempText.setText( realText.getText() );

return tempText;
}


public Text setInformationAttributes( Text realText ){

Text tempText = (Text) informationProxy.clone();
tempText.setText( realText.getText() );

return tempText;
}


public void setNumberOfLetters(int numberOfLetters){
  if( numberOfLetters<0 ) numberOfLetters = (-1)*numberOfLetters;
  this.numberOfLetters = numberOfLetters;
}


//debug this changes the number of news displayed..that is the date alone is failing
public void setNumberOfDisplayedNews(int numberOfDisplayedNews){
  this.limitNumberOfNews = true;
  if( numberOfDisplayedNews<0 ) numberOfDisplayedNews = (-1)*numberOfDisplayedNews;
  this.numberOfDisplayedNews = numberOfDisplayedNews;
}


public void setAdmin(boolean isAdmin){
  this.isAdmin=isAdmin;
}

public void setFromDate(String SQLdate){
  this.date=SQLdate;
}

public void setWidth(int width){
  setWidth(Integer.toString(width));
}

public void setWidth(String width){
this.outerTableWidth = width;
}

public void setChangeImage(String image_name){
  change = new Image(image_name);
}

public void setDeleteImage(String image_name){
  delete = new Image(image_name);
}

public void setEditorImage(String image_name){
  editor = new Image(image_name);
}

public void setCollectionImage(String image_name){
  editor = new Image(image_name);
}

public void setNewsReaderURL(String URL){
  this.newsReaderURL = URL;
}

public String getNewsReaderURL(){
  return newsReaderURL;
}

public void setNewsCollectionURL(String URL){
  this.newsCollectionURL = URL;
}

public String getNewsCollectionURL(){
  return newsCollectionURL;
}

public void showNewsCollectionButton(boolean showNewsCollectionButton){
  this.showNewsCollectionButton = showNewsCollectionButton;
}

public void setNewsReaderURLAsSamePage(ModuleInfo modinfo){
  this.newsReaderURL =  modinfo.getRequest().getRequestURI();
}

public void setNewsCollectionURLAsSamePage(ModuleInfo modinfo){
  this.newsCollectionURL =  modinfo.getRequest().getRequestURI();
}


public void setNumberOfExpandedNews(int numberOfExpandedNews){
  if( numberOfExpandedNews<0 ) numberOfExpandedNews = (-1)*numberOfExpandedNews;
  this.numberOfExpandedNews=numberOfExpandedNews;
}

public void setShowImages(boolean showImages) {
  this.showImages=showImages;
}

public void setShowMoreButton(boolean showMoreButton) {
  this.showMoreButton=showMoreButton;
}

public void setShowHeadlineImage(boolean showHeadlineImage) {
  this.showHeadlineImage=showHeadlineImage;
}

public void setHeadlineAsLink(boolean headlineAsLink) {
  this.headlineAsLink=headlineAsLink;
  this.showHeadlineImage=true;
  this.showMoreButton=false;
}

public void setHeadlineImageURL(String headlineImageURL) {
  this.headlineImageURL=headlineImageURL;
}

public void setShowOnlyDates(boolean showOnlyDates) {
  this.showOnlyDates=showOnlyDates;
}

private String formatNews(String newsString)
{

  Vector tableVector = createTextTable(newsString);

          for ( int a = 0; a < tableVector.size(); a++ ) {

                  String tableRow = tableVector.elementAt(a).toString();

                  if ( a == 0 ) {
                          tableRow = TextSoap.findAndReplace(tableRow,"|","</font></th><th><font size=\""+textSize+"\">");
                  }

                  else {
                          tableRow = TextSoap.findAndReplace(tableRow,"|","</font></td><td><font size=\""+textSize+"\">");
                  }

                  if ( a == 0 || a == tableVector.size()-1) {
                          if ( a == 0 ) {
                                  tableRow = "<table bgcolor=\"#FFFFFF\" border=\"0\" cellpadding=\"6\" cellspacing=\"1\"><tr bgcolor=\"#FFFFFF\"><th><font size=\""+textSize+"\">"+tableRow+"</font></th></tr>";
                          }

                          if ( a == tableVector.size()-1 ) {
                          tableRow = "<tr bgcolor=\"#FFFFFF\"><td><font size=\""+textSize+"\">"+tableRow+"</font></td></tr></table>";
                          }
                  }
                  else {
                          tableRow = "<tr bgcolor=\"#FFFFFF\"><td><font size=\""+textSize+"\">"+tableRow+"</font></td></tr>";
                  }

                  newsString = TextSoap.findAndReplace(newsString,tableVector.elementAt(a).toString(),tableRow);
          }

          newsString = TextSoap.findAndReplace(newsString,"|\r\n","");
          newsString = TextSoap.findAndReplace(newsString,"|","");
          //Töflugerð lokið

  //Búa til tengla
          Vector linkVector = createTextLink(newsString);

          for ( int a = 0; a < linkVector.size(); a++ ) {
                  String link = linkVector.elementAt(a).toString();
                          int comma = link.indexOf(",");

                  link = "<a href=\""+link.substring(comma+1,link.length())+"\">"+link.substring(0,comma)+"</a>";

                  newsString = TextSoap.findAndReplace(newsString,"Link("+linkVector.elementAt(a).toString()+")",link);
          }

          //Almenn hreinsun
          newsString = TextSoap.findAndReplace(newsString,"*","<li>");
          newsString = TextSoap.findAndReplace(newsString,"\n","<br>");
  newsString = TextSoap.findAndReplace(newsString,"\t","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

  return newsString;

}

private Vector createTextTable(String newsString) {

  Vector tableVector = TextSoap.FindAllBetween(newsString,"|","|\r\n");

return tableVector;
}

private Vector createTextLink(String newsString) {

  Vector linkVector = TextSoap.FindAllBetween(newsString,"Link(",")");

return linkVector;
}


public String getBundleIdentifier(){
  return IW_BUNDLE_IDENTIFIER;
}


}

package com.idega.block.news.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import	com.idega.jmodule.object.*;
import	com.idega.jmodule.object.interfaceobject.*;
import	com.idega.block.news.data.*;
import	com.idega.data.*;
import com.idega.util.text.*;


public class NewsReader extends JModuleObject{

private boolean isAdmin=false;
private boolean showNewsCollectionButton=true;
private int category_id = 0;
private String date = null;
private int numberOfNews = 3;
private boolean backbutton = false;
private boolean cutNews = true;// or default true?
private boolean showAll = false;
private Table outerTable = new Table(1,1);
private String newsReaderURL = "/news/newsreader.jsp";
private String newsCollectionURL = "/news/newsall.jsp";
private boolean showImages = true;
private boolean showOnlyDates = false;
private boolean headlineAsLink = false;


private Text textProxy = new Text();
private Text headlineProxy = new Text();
private Text informationProxy = new Text();

private int numberOfLetters = 273;
private int numberOfDisplayedNews = 3;
private boolean limitNumberOfNews = false;

private Image change;
private Image delete;
private Image editor;
private Image collection;

private String language = "IS";

private int textSize = 2;

private String attributeName = "union_id";
//private int attributeId = -1;
private int attributeId = 3;

private News myNews = new News();

public NewsReader(){
  showAll = true;
}

/**
 * @deprecated replaced with the default constructor
 */
public NewsReader(boolean isAdmin){
  this.showAll=true;
}

/**
 * @deprecated replaced with the category_id constructor
 */
public NewsReader(boolean isAdmin, int category_id){
  this.category_id=category_id;
  this.showAll = false;
}

/**
 * @deprecated replaced with the date or idegaTimestamp constructor
 */
public NewsReader(boolean isAdmin, String date){
  this.date=date;
  this.showAll=true;
}

public NewsReader(String date){
  this.date=date;
  this.showAll=true;
}

public NewsReader(idegaTimestamp timestamp){
  this.showAll=true;
  this.date = timestamp.toSQLString();
}

/**
 * @deprecated replaced with the category_id,date constructor
 */
public NewsReader(boolean isAdmin, int category_id, String date){
  this.category_id=category_id;
  this.date=date;
  this.showAll = false;
}

public NewsReader(int category_id, String date){
  this.category_id=category_id;
  this.date=date;
  this.showAll = false;
}

public NewsReader(int category_id){
  this.category_id=category_id;
  this.showAll = false;
}

public NewsReader(int category_id, idegaTimestamp timestamp){
  this.showAll=false;
  this.category_id=category_id;
  this.date = timestamp.toSQLString();
}

public NewsReader(boolean isAdmin, int category_id, idegaTimestamp timestamp){
  this.category_id=category_id;
  this.showAll = false;
  this.date = timestamp.toSQLString();

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

public String getDatastoreType(GenericEntity entity){
  return DatastoreInterface.getDatastoreType(entity.getDatasource());
}


private String getColumnString(NewsCategoryAttributes[] attribs){
  StringBuffer values = new StringBuffer("");
  for (int i = 0 ; i < attribs.length ; i++) {
    values.append(" news_category_id = '");
    values.append(attribs[i].getNewsCategoryId());
    values.append("'") ;
    if( i!= (attribs.length)-1 ) values.append(" OR ");
  }
  String returnString = values.toString();
  if (returnString.equalsIgnoreCase("")) return null;
  else return returnString;
}

private void setSpokenLanguage(ModuleInfo modinfo){
 String language2 = modinfo.getRequest().getParameter("language");
    if (language2==null) language2 = ( String ) modinfo.getSession().getAttribute("language");
    if ( language2 != null) language = language2;
}

public void main(ModuleInfo modinfo)throws Exception{

  this.empty();

  this.isAdmin=this.isAdministrator(modinfo);
  setSpokenLanguage(modinfo);


  change = new Image("/pics/jmodules/news/"+language+"/change.gif");
  delete = new Image("/pics/jmodules/news/"+language+"/delete.gif");
  editor = new Image("/pics/jmodules/news/"+language+"/newseditor.gif");
  collection = new Image("/pics/jmodules/news/"+language+"/collection.gif");


  String news_id = modinfo.getRequest().getParameter("news_id");
  String news_category_id = modinfo.getRequest().getParameter("news_category_id");


  if( news_category_id !=null ) {
      category_id = Integer.parseInt(news_category_id);
      showAll = false;
  }


  boolean byDate=false;

  News[] news = new News[1];

  try{
   // PrintWriter out = modinfo.getResponse().getWriter();

    if(isAdmin) {

      Form newsEditor = new Form("/news/editor.jsp");
      newsEditor.add(new SubmitButton(editor));
      add(newsEditor);

    }

    if(news_id != null){//single news
      showAll=false;
      news[0] = new News(Integer.parseInt(news_id));
      backbutton=true;
      cutNews = false;
      setNumberOfDisplayedNews(1);
      add(drawNewsTable(news));
    }
    else{//view all or category view

      NewsCategoryAttributes[] attribs = null;
      String categoryString = null;

      if ( category_id == 0) {//show the whole collection
        showAll = true;
        attribs = (NewsCategoryAttributes[]) (new NewsCategoryAttributes()).findAllByColumn("attribute_name",attributeName,"attribute_id",""+attributeId);
//        System.err.println("categoryString1 = " + categoryString);
        categoryString = getColumnString(attribs);
//        System.err.println("categoryString2 = " + categoryString);
        System.err.println("Inside category_id=0");
      }
      else{ // just this category
        showAll = false;
        categoryString = "news_category_id = '"+category_id+"' ";
        System.err.println("Show all false category_id="+category_id);
      }


      if( date == null ){//not by Date
        if( showAll ) {
         if( categoryString!=null ) categoryString = " where " + categoryString;
         else categoryString = "";
          System.err.println("select * from "+myNews.getEntityName()+categoryString+" order by news_date");
          news = (News[]) (myNews).findAll("select * from "+myNews.getEntityName()+categoryString+" order by news_date");
        }
        else news = (News[]) (myNews).findAllByColumn("news_category_id",category_id);

      }
      else{// by date

      //debug
      System.err.println(date);
        String DatastoreType = getDatastoreType( myNews );
        byDate=true;
        if( (categoryString!=null) && !(categoryString.equals("")) ) categoryString = " OR " + categoryString;
        if( (showAll) && !(DatastoreType.equals("oracle"))  ){
          String statementstring = "select * from "+myNews.getEntityName()+" where news_date >= '"+date+"'  "+categoryString+" order by news_date";
          //debug eiki
          System.err.println(statementstring);
          news = (News[]) (myNews).findAll(statementstring);
          if (news != null) System.err.println("fann "+ news.length +"fréttir");

        }
        else if( (showAll) && (DatastoreType.equals("oracle"))  )  news = (News[]) (myNews).findAll("select * from news where news_date >= "+date+" "+categoryString+" order by news_date");
        else {
          if ( !(DatastoreType.equals("oracle")) ){

          String statementstring = "select * from "+myNews.getEntityName()+" where news_category_id ='"+category_id+"' and news_date >= '"+date+"' order by news_date";
System.err.println(statementstring);

           news = (News[]) (myNews).findAll(statementstring);

          }
          else news = (News[]) (myNews).findAll("select * from "+myNews.getEntityName()+" where news_category_id ='"+category_id+"' and news_date >= "+date+" order by news_date");
        }
      }

      if(news.length > 0){	add(drawNewsTable(news));}
      else {//if out of date range
              if( byDate ){
                      date=null;
                      categoryString = TextSoap.findAndCut(categoryString,"OR");
                      String statementstring = "select * from "+myNews.getEntityName()+" where "+categoryString+" order by news_date";
                      //debug eiki 24.dec
                      System.err.println("OUT OF RANGE"+statementstring);
                      news = (News[]) (myNews).findAll(statementstring);

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
        newsCollection.add(new Link( collection, getNewsCollectionURL() ),1,1);
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


public Table drawNewsTable(News[] news)throws IOException,SQLException
{


	//outerTable.setWidth(400);
	outerTable.setAlignment("center");



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
	int i = news.length-1;


		if( !limitNumberOfNews ) numberOfDisplayedNews = news.length;

		while ( (i >= 0) && ( numberOfDisplayedNews > (news.length-i))){


                  news_category_id = news[i].getNewsCategoryId();
                  newscat = new NewsCategory(news_category_id);
                  news_category = newscat.getName();

                  headline = news[i].getHeadline();
                  includeImage = news[i].getIncludeImage();
                  timestamp = (news[i].getDate()).toString();
                  author = news[i].getAuthor();
                  source = news[i].getSource();
                  daysshown = news[i].getDaysShown();


                  if (i < (news.length-numberOfNews)){
                          includeImage = "N";
                          newstext = "";
                  }
                  else {
                          newstext = news[i].getText();
                          newstext=formatNews(newstext);
                  }

                  //newstext = formatNews(newstext);

                  information = getInfoText(author, source, news_category, timestamp);


                  if(includeImage.equals("Y")){
                          image_id = news[i].getImageId();
                          outerTable.add(insertTable(timestamp, headline, newstext,information,news[i].getID(),image_id), 1, 1);
                  }
                  else{
                          image_id=-1;
                          outerTable.add(insertTable(timestamp, headline, newstext,information,news[i].getID(),image_id), 1, 1);
                  }


                  if(isAdmin) {

                          Form newsEdit = new Form("/news/editor.jsp?news_id="+news[i].getID());
                          newsEdit.add(new SubmitButton(change));
                          Form newsEdit2 = new Form("/news/editor.jsp?news_id="+news[i].getID()+"&mode=delete");
                          newsEdit2.add(new SubmitButton(delete));
                          Table editTable = new Table(2,1);
                          editTable.add(newsEdit,1,1);
                          editTable.add(newsEdit2,2,1);

                          outerTable.add(editTable, 1, 1);

                  }


		 i--;
		}

	return outerTable;
}



public Table insertTable(String TimeStamp, String Headline, String NewsText, Text information,int news_id,int image_id) throws SQLException
{
  String btnBackUrl = "/pics/jmodules/news/"+language+"/back.gif";
  String btnNanarUrl = "/pics/jmodules/news/"+language+"/more.gif";

  Text headline = new Text(Headline);
  boolean more = false;

  //cut of news
  if(cutNews){
    if(NewsText.length() >= numberOfLetters){
            more=true;
            NewsText=NewsText.substring(0,numberOfLetters)+"...";
    }
    else if (NewsText.length()<5) {
            more=true;
    }
  }

  Text newstext = new Text(NewsText);

  headline.setFontFace("helvetica,arial");
  headline.setFontSize(3);
  headline = setHeadlineAttributes(headline);
  headline.setBold();

  Link headlineLink = new Link(headline,getNewsReaderURL());
    headlineLink.addParameter("news_id",news_id);

  Image headlineImage = new Image("pics/jmodules/news/nanar2.gif","");
    headlineImage.setAttribute("align","absmiddle");

  //newstext.setFontFace("helvetica,arial");
  newstext = setTextAttributes( newstext );

  information.setFontColor("#666666");
  information.setFontFace("helvetica,arial");
  information.setFontSize(1);

  Table newsTable = new Table(1, 3);

  newsTable.setWidth("100%");
  newsTable.add(information, 1, 1);

  if ( headlineAsLink ) {
    newsTable.add(headlineImage, 1, 2);
    newsTable.add(headlineLink, 1, 2);
  }

  else {
    newsTable.add(headline, 1, 2);
  }


  if (image_id!=-1){
  Table imageTable = new Table(1, 2);
  Image newsImage = new Image(image_id);
  imageTable.setAlignment("right");
  imageTable.setVerticalAlignment("top");
  imageTable.add(newsImage, 1, 1);

  if ( showImages ) {
    newsTable.add(imageTable, 1, 3);
  }

  }

  newsTable.add(newstext, 1, 3);
  newsTable.setRowVerticalAlignment(3, "Top");

  if( backbutton ) {
          newsTable.addText("<br>",1,3);
          newsTable.add(new BackButton(new Image(btnBackUrl)), 1, 3);
  }
  else {
    if(more && !headlineAsLink) {
     if ( !NewsText.equals("") ) newsTable.addText("<br>",1,3);
        newsTable.add(insertHyperlink(btnNanarUrl, "news_id", ""+news_id, getNewsReaderURL() ), 1, 3);
      }
    }


  return newsTable;
}


public String formatDateWithTime(String date ,String DatastoreType)
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
  String DatastoreType = getDatastoreType(myNews);
  TimeStamp = formatDateWithTime(TimeStamp,DatastoreType);

  if ( showOnlyDates ) {

      idegaTimestamp timeStamp = new idegaTimestamp(TimeStamp.substring(6,10)+"-"+TimeStamp.substring(3,5)+"-"+TimeStamp.substring(0,2));
      String timeStamp2 = timeStamp.getISLDate();

      information = new Text(timeStamp.getDate()+"."+timeStamp2.substring(timeStamp2.indexOf(".")+1,timeStamp2.indexOf(".")+4)+".&nbsp;"+timeStamp.getYear());
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


private Link insertHyperlink(String imageUrl, String name, String value, String action)
{
  Image linkImage = new Image(imageUrl);
  linkImage.setBorder(0);
  Link myLink = new Link(linkImage);
  myLink.setURL(action);
  myLink.addParameter(name, value);
  return myLink;
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
  this.numberOfDisplayedNews = numberOfDisplayedNews+1;
}


public void setAdmin(boolean isAdmin){
  this.isAdmin=isAdmin;
}

public void setFromDate(String SQLdate){
  this.date=SQLdate;
}

public void setWidth(int width){
 this.outerTable.setWidth(width);
}

public void setWidth(String width){
this.outerTable.setWidth(width);
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



/**
* Depricated method use setNumberOfExpandedNews instead.
*/
public void setNumberOfNews(int numberOfNews){
  if( numberOfNews<0 ) numberOfNews = (-1)*numberOfNews;
  this.numberOfNews=numberOfNews;
}

public void setNumberOfExpandedNews(int numberOfNews){
  if( numberOfNews<0 ) numberOfNews = (-1)*numberOfNews;
  this.numberOfNews=numberOfNews;
}

public void setShowImages(boolean showImages) {
  this.showImages=showImages;
}

public void setHeadlineAsLink(boolean headlineAsLink) {
  this.headlineAsLink=headlineAsLink;
}

public void setShowOnlyDates(boolean showOnlyDates) {
  this.showOnlyDates=showOnlyDates;
}

public String formatNews(String newsString)
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

public Vector createTextTable(String newsString) {

  Vector tableVector = TextSoap.FindAllBetween(newsString,"|","|\r\n");

return tableVector;
}

public Vector createTextLink(String newsString) {

  Vector linkVector = TextSoap.FindAllBetween(newsString,"Link(",")");

return linkVector;
}


}

package com.idega.block.news.business;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.idega.block.news.data.NwNews;
import com.idega.block.text.data.Content;
import com.idega.util.text.TextSoap;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class NewsFormatter {

  public NewsFormatter() {
  }

  public static List listOfTextBetweenImages(String newsString){
    Vector V = new Vector();
    String image = "[image]";
    int start = 0,end = 0,idstart = 0,idend= 0;

    while((end = newsString.indexOf(image,start)) != -1){
      idstart = newsString.indexOf("[",end+image.length());
      idend = newsString.indexOf("]",end+image.length());
      V.add(newsString.substring(start,end));
      if( idstart != -1 && idend != -1){
        String id = newsString.substring(idstart+1,idend-1);
        V.add(new Integer(id));
      }
    }
    return V;
  }

  public static String formatNews(String newsString,String textSize){
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

  public static  String getInfoText(NwNews news,Content content,String sCategory, Locale locale, boolean showOnlyDates,boolean showTime,boolean showTimeFirst,boolean showUpdated){
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,locale);
		DateFormat dt = DateFormat.getTimeInstance(DateFormat.SHORT,locale);
	java.util.Date newsDate = showUpdated?content.getLastUpdated():content.getCreated();
    String NewsDate = newsDate!=null?df.format(newsDate):null;
	String NewsTime = newsDate!=null?dt.format(newsDate):null;
    StringBuffer info = new StringBuffer();
    String spacer = " | ";
    if(showOnlyDates && NewsDate != null){
      info.append(NewsDate);
      if(showTime && !"".equals(NewsTime)){
        if(showTimeFirst){
          info.insert(0,NewsTime+" ");
        }
        else
          info.append(" ");
          info.append(NewsTime);
      }
      if(!"".equals(sCategory)){
      	info.append(spacer);
        info.append(sCategory);
      }
    }
    else{
      if(!"".equals(sCategory)){
        info.append(sCategory);
        info.append(spacer);
      }
      if(!"".equals(news.getAuthor())){
        info.append(news.getAuthor());
        info.append(spacer);
      }
      if(!"".equals(news.getSource())){
        info.append(news.getSource());
        info.append(spacer);
      }
			if(showTime && showTimeFirst && !"".equals(NewsTime)){
        info.append(NewsTime);
      }
      if(!"".equals(NewsDate)){
        info.append(NewsDate);
				if(showTime)
        info.append(spacer);
      }
			if(showTime && !showTimeFirst && !"".equals(NewsTime)){
        info.append(NewsTime);
      }
    }

    String inf = TextSoap.findAndReplace(info.toString(), " ","&nbsp;");
    //System.err.println(inf);
    return inf;

  }

  private static Vector createTextTable(String newsString) {
    Vector tableVector = TextSoap.FindAllBetween(newsString,"|","|\r\n");
  return tableVector;
  }

  private static Vector createTextLink(String newsString) {
     Vector linkVector = TextSoap.FindAllBetween(newsString,"Link(",")");
  return linkVector;
}



}

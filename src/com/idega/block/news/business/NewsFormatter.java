package com.idega.block.news.business;

import java.util.Vector;
import java.util.StringTokenizer;
import com.idega.util.text.TextSoap;
import com.idega.block.news.data.NwNews;
import java.text.DateFormat;
import java.util.Locale;
import com.idega.util.idegaTimestamp;
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

  public static  String getInfoText(NwNews news,String sCategory, Locale locale, boolean showOnlyDates){
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,locale);
    String NewsStamp = news.getNewsDate()!=null?df.format((java.util.Date)news.getNewsDate()):null;
    StringBuffer info = new StringBuffer();
    String spacer = " | ";
    if(showOnlyDates && NewsStamp != null){
      info.append(NewsStamp);
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
      if(!"".equals(NewsStamp)){
        info.append(NewsStamp);
        info.append(spacer);
      }
    }
    return info.toString();

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
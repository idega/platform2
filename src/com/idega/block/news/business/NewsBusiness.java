package com.idega.block.news.business;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import java.util.Vector;
import com.idega.util.text.TextSoap;

public class NewsBusiness {

public static String language = "IS";

  public static String formatNews(String newsString, int textSize) {
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

  private static Vector createTextTable(String newsString) {
    Vector tableVector = TextSoap.FindAllBetween(newsString,"|","|\r\n");
    return tableVector;
  }

  private static Vector createTextLink(String newsString) {
    Vector linkVector = TextSoap.FindAllBetween(newsString,"Link(",")");
    return linkVector;
  }

  public static String formatDateWithTime(String date ,String DatastoreType) {
    StringBuffer ReturnString = new StringBuffer("");

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


}
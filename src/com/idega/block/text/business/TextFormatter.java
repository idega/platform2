package com.idega.block.text.business;

import java.util.Vector;
import java.util.StringTokenizer;
import com.idega.util.text.TextSoap;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class TextFormatter {

  public TextFormatter() {
  }

  public static String formatMSWordStringToIdegaWebString(String text){
    String returnString = text;
    //lists
   /**@todo implement when we have regular expressions
    *  returnString = TextSoap.findAndReplace(text,"1.	","*");
    */
    returnString = TextSoap.findAndReplace(text,"-	","*");
    returnString = TextSoap.findAndReplace(text,"·	","*");
    returnString = TextSoap.findAndReplace(text,"	"," ");//space in word tables

    return returnString;
  }

  public static String formatText(String textBody,int tableTextSize,String tableWidth) {
    //Búa til töflu
    if (textBody==null || textBody.equals("")) textBody = "";

    Vector tableVector = createTextTable(textBody);

    for ( int a = 0; a < tableVector.size(); a++ ) {

      String tableRow = tableVector.elementAt(a).toString();

      if ( a == 0 ) {
        tableRow = TextSoap.findAndReplace(tableRow,"|","</font></td><td valign=\"top\"><font size=\""+(tableTextSize)+"\">");
      }
      else {
        tableRow = TextSoap.findAndReplace(tableRow,"|","</font></td><td valign=\"top\"><font size=\""+tableTextSize+"\">");
      }

      if ( a == 0 || a == tableVector.size()-1) {
        if ( a == 0 ) {
          tableRow = "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\""+tableWidth+"\"><tr bgcolor=\"#FFFFFF\"><td valign=\"top\"><font size=\""+(tableTextSize)+"\">"+tableRow+"</font></td></tr>";
        }

        if ( a == tableVector.size()-1 ) {
          tableRow = "<tr bgcolor=\"#FFFFFF\"><td valign=\"top\"><font size=\""+tableTextSize+"\">"+tableRow+"</font></td></tr></table>";
        }
      }
      else {
        tableRow = "<tr bgcolor=\"#FFFFFF\"><td valign=\"top\"><font size=\""+tableTextSize+"\">"+tableRow+"</font></td></tr>";
      }

      textBody = TextSoap.findAndReplace(textBody,tableVector.elementAt(a).toString(),tableRow);
    }

    textBody = TextSoap.findAndReplace(textBody,"|\r\n","");
    textBody = TextSoap.findAndReplace(textBody,"|","");
    //Töflugerð lokið


    //Búa til töflu 2
    if (textBody==null || textBody.equals("")) textBody = "";
    tableVector = createTextTableNoBanner(textBody);

    for ( int a = 0; a < tableVector.size(); a++ ) {

      String tableRow = tableVector.elementAt(a).toString();

      if ( a == 0 ) {
        tableRow = TextSoap.findAndReplace(tableRow,"?","</font></td><td><font size=\""+(tableTextSize+1)+"\">");
      }
      else {
        tableRow = TextSoap.findAndReplace(tableRow,"?","</font></td><td><font size=\""+tableTextSize+"\">");
      }

      if ( a == 0 || a == tableVector.size()-1) {
        if ( a == 0 ) {
          tableRow = "<table border=\"0\" cellpadding=\"3\" cellspacing=\"0\" width=\""+tableWidth+"\"><tr bgcolor=\"#FFFFFF\"><td><font size=\""+(tableTextSize+1)+"\">"+tableRow+"</font></td></tr>";
        }

        if ( a == tableVector.size()-1 ) {
          tableRow = "<tr bgcolor=\"#FFFFFF\"><td><font size=\""+tableTextSize+"\">"+tableRow+"</font></td></tr></table>";
        }
      }
      else {
        tableRow = "<tr bgcolor=\"#FFFFFF\"><td><font size=\""+tableTextSize+"\">"+tableRow+"</font></td></tr>";
      }

      textBody = TextSoap.findAndReplace(textBody,tableVector.elementAt(a).toString(),tableRow);
    }

    textBody = TextSoap.findAndReplace(textBody,"?\r\n","");
    textBody = TextSoap.findAndReplace(textBody,"?","");
    //Töflugerð lokið

    //Búa til tengla
    Vector linkVector = createTextLink(textBody);
    for ( int a = 0; a < linkVector.size(); a++ ) {
      String link = linkVector.elementAt(a).toString();
      int comma = link.indexOf(",");
      link = "<a href=\""+link.substring(comma+1,link.length())+"\" target=\"_blank\">"+link.substring(0,comma)+"</a>";
      textBody = TextSoap.findAndReplace(textBody,"Link("+linkVector.elementAt(a).toString()+")",link);
    }

    //Almenn hreinsun
    //textBody = TextSoap.findAndReplace(textBody,"*","<li>");
    //textBody = TextSoap.findAndReplace(textBody,".\r\n",".<br><br>");
    //textBody = TextSoap.findAndReplace(textBody,"?\r\n","?<br><br>");
    //textBody = TextSoap.findAndReplace(textBody,"!\r\n","!<br><br>");

    textBody = TextSoap.findAndReplace(textBody,"\r\n","<br>");
    textBody = TextSoap.findAndReplace(textBody,"\t","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

    return textBody;
  }

  public static Vector testText(String textBody) {
    Vector testVector = TextSoap.FindAllBetween(textBody,"\r\n\r\n","\r\n");
    return testVector;
  }

  public static Vector createTextTable(String textBody) {
    Vector tableVector = TextSoap.FindAllBetween(textBody,"|","|\r\n");
    return tableVector;
  }

  public static Vector createTextTableNoBanner(String textBody) {
    Vector tableVector = TextSoap.FindAllBetween(textBody,"?","?\r\n");
    return tableVector;
  }

  public static Vector createTextLink(String textBody) {
    Vector linkVector = TextSoap.FindAllBetween(textBody,"Link(",")");
    return linkVector;
  }

  public static String textReverse(String strengur) {
    StringBuffer buffer = new StringBuffer(strengur);
    String reverse = buffer.reverse().toString();
    return reverse;
  }

  public static String textCrazy(String strengur) {

    String crazy = "";

    StringTokenizer token = new StringTokenizer(strengur);

    while ( token.hasMoreTokens() ) {
      StringBuffer buffer = new StringBuffer(token.nextToken());
      crazy += buffer.reverse().toString();
      if ( token.hasMoreTokens() ) {
        crazy += " ";
      }
    }

    return crazy;

  }

}
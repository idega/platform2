package com.idega.block.blog.business;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.idega.block.blog.data.BlogEntity;
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

public class BlogFormatter {

  public BlogFormatter() {
  }

  public static List listOfTextBetweenImages(String blogString){
    Vector V = new Vector();
    String image = "[image]";
    int start = 0,end = 0,idstart = 0,idend= 0;

    while((end = blogString.indexOf(image,start)) != -1){
      idstart = blogString.indexOf("[",end+image.length());
      idend = blogString.indexOf("]",end+image.length());
      V.add(blogString.substring(start,end));
      if( idstart != -1 && idend != -1){
        String id = blogString.substring(idstart+1,idend-1);
        V.add(new Integer(id));
      }
    }
    return V;
  }

  public static String formatBlog(String blogString,String textSize){
    Vector tableVector = createTextTable(blogString);

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
      blogString = TextSoap.findAndReplace(blogString,tableVector.elementAt(a).toString(),tableRow);
    }

    blogString = TextSoap.findAndReplace(blogString,"|\r\n","");
    blogString = TextSoap.findAndReplace(blogString,"|","");
    //T�fluger� loki�

    //B�a til tengla
    Vector linkVector = createTextLink(blogString);

    for ( int a = 0; a < linkVector.size(); a++ ) {
      String link = linkVector.elementAt(a).toString();
      int comma = link.indexOf(",");
      link = "<a href=\""+link.substring(comma+1,link.length())+"\">"+link.substring(0,comma)+"</a>";
      blogString = TextSoap.findAndReplace(blogString,"Link("+linkVector.elementAt(a).toString()+")",link);
    }

    //Almenn hreinsun
    blogString = TextSoap.findAndReplace(blogString,"*","<li>");
    blogString = TextSoap.findAndReplace(blogString,"\n","<br>");
    blogString = TextSoap.findAndReplace(blogString,"\t","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

    return blogString;
  }

  public static  String getInfoText(BlogEntity blog,Content content,String sCategory, Locale locale, boolean showOnlyDates,boolean showTime,boolean showTimeFirst,boolean showUpdated){
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,locale);
		DateFormat dt = DateFormat.getTimeInstance(DateFormat.SHORT,locale);
	java.util.Date blogDate = showUpdated?content.getLastUpdated():content.getCreated();
    String BlogDate = blogDate!=null?df.format(blogDate):null;
	String BlogTime = blogDate!=null?dt.format(blogDate):null;
    StringBuffer info = new StringBuffer();
    String spacer = " | ";
    if(showOnlyDates && BlogDate != null){
      info.append(BlogDate);
      if(showTime && !"".equals(BlogTime)){
        if(showTimeFirst){
          info.insert(0,BlogTime+" ");
        }
		else {
			info.append(" ");
		}
          info.append(BlogTime);
      }
    }
    else{
      if(!"".equals(sCategory)){
        info.append(sCategory);
        info.append(spacer);
      }
      if(!"".equals(blog.getAuthor())){
        info.append(blog.getAuthor());
        info.append(spacer);
      }
      if(!"".equals(blog.getSource())){
        info.append(blog.getSource());
        info.append(spacer);
      }
			if(showTime && showTimeFirst && !"".equals(BlogTime)){
        info.append(BlogTime);
      }
      if(!"".equals(BlogDate)){
        info.append(BlogDate);
				if(showTime) {
					info.append(spacer);
				}
      }
			if(showTime && !showTimeFirst && !"".equals(BlogTime)){
        info.append(BlogTime);
      }
    }

    String inf = TextSoap.findAndReplace(info.toString(), " ","&nbsp;");
    //System.err.println(inf);
    return inf;

  }

  private static Vector createTextTable(String blogString) {
    Vector tableVector = TextSoap.FindAllBetween(blogString,"|","|\r\n");
  return tableVector;
  }

  private static Vector createTextLink(String blogString) {
     Vector linkVector = TextSoap.FindAllBetween(blogString,"Link(",")");
  return linkVector;
}



}

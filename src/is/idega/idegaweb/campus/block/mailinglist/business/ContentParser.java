package is.idega.idegaweb.campus.block.mailinglist.business;

import java.util.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class ContentParser{

  /**
   *  Static method to parse given text with a parsable object
   */
  public String parse(ContentParsable parsableObject, String text){
    StringBuffer finalText = new StringBuffer();
    StringTokenizer st = new StringTokenizer(text,"[]");
    Map M = getMappedParseStrings(parsableObject);
    while(st.hasMoreTokens()){
      String token = st.nextToken();
      if(M.containsKey(token)){
        finalText.append(M.get(token));
      }

      /*
      else{
        finalText.append(token);
      }
      */
    }
    /*
    int from = 0,to = 0;
    while((to = text.indexOf("[",from))!=-1){
      finalText.append(text.substring(from,to));
      from = to+1;
      to = text.indexOf("]",from);
      String s = parsableObject.getParsedString(text.substring(from,to-1));
      System.err.println(s);
      finalText.append(s);
      from = to+1;
    }
    */
    return finalText.toString();
  }

  private Map getMappedParseStrings(ContentParsable parsableObject){
    Hashtable H = new Hashtable();
    String[] tags = parsableObject.getParseTags();
    String value;
    for (int i = 0; i < tags.length; i++) {
      value = parsableObject.getParsedString(tags[i]);
      if(value !=null)
        H.put(tags[i],value);
    }
    return H;
  }

  /**
   *  Static method to provide a formatted tag string
   */
  public static String getFormattedTag( String tag){
    return "["+tag+"]";
  }
}
package com.idega.block.text.business;

import com.idega.presentation.IWContext;
import java.util.Vector;
import java.util.StringTokenizer;
import com.idega.util.text.TextSoap;
/**
 *  Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 *@author Aron unds Eiki
 *@created    12. mars 2002
 *@version    1.0
 */

public class TextFormatter {

  private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.text";


  /**
   *  Constructor for the TextFormatter object
   */
  public TextFormatter() { }


  /**
   *  Gets the dummy "Lorem ipsum..." string from the text bundle
   *
   *@param  iwc  IWContext so we can fetch the text bundle
   *@return      The dummy text
   */
  public static String getLoremIpsumString(IWContext iwc) {
    return iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty("lorem", "Lorem ipsum text is missing it should be defined as a bundle property named lorem in com.idega.block.text");
  }

  /**
   *  Gets the dummy "Lorem ipsum..." string from the text bundle and cuts it to a certain length.
   *
   *@param  iwc  IWContext so we can fetch the text bundle.
   *@param  length  Get the lorem string of this size.
   *@return      The dummy text
   */
  public static String getLoremIpsumString(IWContext iwc, int length) {
    String lorem = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty("lorem", "Lorem ipsum text is missing it should be defined as a bundle property named lorem in com.idega.block.text");
    int loremLength = lorem.length();
    if( length<1 ) length*=-1;

    if(length>loremLength){
      int multiply = length/loremLength;
      int modulus = length % loremLength;
      StringBuffer buf = new StringBuffer();

      for (int i = 0; i < multiply ; i++) {
	buf.append(lorem);
      }
      buf.append(lorem.substring(0,modulus));

      return buf.toString();
    }
    else{
     return lorem.substring(0,length);
    }

  }


  /**
   *  Description of the Method
   *
   *@param  text  Description of the Parameter
   *@return       Description of the Return Value
   */
  public static String formatMSWordStringToIdegaWebString(String text) {
    String returnString = text;
    //lists

    //returnString = TextSoap.findAndReplace(text,"-	","*");
    //returnString = TextSoap.findAndReplace(text,"·	","*");
    returnString = TextSoap.findAndReplace(text, "	", " ");
    //space in word tables
    //returnString = TextSoap.findAndReplace(text,"o	","*");
    //returnString = TextSoap.findAndReplace(text,"•	","*");
    returnString = TextSoap.findAndReplace(text, "&#61553;", "*");
    returnString = TextSoap.findAndReplace(text, "&#61558;", "*");
    returnString = TextSoap.findAndReplace(text, "&#61607;", "*");
    returnString = TextSoap.findAndReplace(text, "&#61656;", "*");
//    returnString = TextSoap.findAndReplace(text,"?\t","*");
//    returnString = TextSoap.findAndReplace(text,"o\t","*");
    return returnString;
  }


	/**
	 *@deprecated Use <code>TextSoap.formatText(String textBody)<code> instead
	 */
 public static String formatText(String textBody) {
 	return TextSoap.formatText(textBody);
 }


  /**
   *  A unit test for JUnit
   *
   *@param  textBody  Description of the Parameter
   *@return           Description of the Return Value
   */
  public static Vector testText(String textBody) {
    Vector testVector = TextSoap.FindAllBetween(textBody, "\r\n\r\n", "\r\n");
    return testVector;
  }


  /**
   *  Description of the Method
   *
   *@param  textBody  Description of the Parameter
   *@return           Description of the Return Value
   */
  public static Vector createTextTable(String textBody) {
    Vector tableVector = TextSoap.FindAllBetween(textBody, "|", "|\r\n");
    return tableVector;
  }


  /**
   *  Description of the Method
   *
   *@param  textBody  Description of the Parameter
   *@return           Description of the Return Value
   */
  public static Vector createTextTableNoBanner(String textBody) {
    Vector tableVector = TextSoap.FindAllBetween(textBody, "?", "?\r\n");
    return tableVector;
  }


  /**
   *  Description of the Method
   *
   *@param  textBody  Description of the Parameter
   *@return           Description of the Return Value
   */
  public static Vector createTextLink(String textBody) {
    Vector linkVector = TextSoap.FindAllBetween(textBody, "Link(", ")");
    return linkVector;
  }


  /**
   *  Description of the Method
   *
   *@param  strengur  Description of the Parameter
   *@return           Description of the Return Value
   */
  public static String textReverse(String strengur) {
    StringBuffer buffer = new StringBuffer(strengur);
    String reverse = buffer.reverse().toString();
    return reverse;
  }


  /**
   *  Description of the Method
   *
   *@param  strengur  Description of the Parameter
   *@return           Description of the Return Value
   */
  public static String textCrazy(String strengur) {
    String crazy = "";
    StringTokenizer token = new StringTokenizer(strengur);

    while (token.hasMoreTokens()) {
      StringBuffer buffer = new StringBuffer(token.nextToken());
      crazy += buffer.reverse().toString();
      if (token.hasMoreTokens()) {
	crazy += " ";
      }
    }

    return crazy;
  }
}
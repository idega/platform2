/*
 * $Id: Edit.java,v 1.2 2002/01/22 14:39:44 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.presentation;

import com.idega.presentation.text.Text;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.InterfaceObjectContainer;

/**
 * @author
 * @version 1.0
 */
public class Edit {
  public static final String colorLight = "#D7DADF";
  public static final String colorMiddle = "#9fA9B3";
  public static final String colorDark  = "#27334B";
  public static final String colorWhite =  "#FFFFFF";
  public static final String colorTextFont = "#000000";
  public static final String colorTitleFont = "#FFFFFF";
  public static final String colorHeaderFont = "#27334B";
  public static final String colorIndexFont = "#000000";
  public static final String colorRed = "#942829";
  public static final String colorBlue = "#27324B";
  public static final String colorLightBlue ="#ECEEF0";

  public static final String bottomBarThickness = "8";
  public static final String styleAttribute = "font-size: 8pt";

  public static final int textFontSize = 1;
  public static final int titleFontSize = 1;
  public static final int headerFontSize = 1;

  /**
   *
   */
  public static Text formatText(String text) {
    return(getText(text,false,colorTextFont,textFontSize));
  }

  /**
   *
   */
  public static Text formatText(String text, int size) {
    return(getText(text,true,colorTextFont,size));
  }

  /**
   *
   */
  public static Text formatText(int i) {
    return(formatText(String.valueOf(i)));
  }

  /**
   *
   */
  public static Text titleText(String text) {
    return(getText(text,true,colorTitleFont,titleFontSize));
  }

  /**
   *
   */
  public static Text titleText(String text, int size) {
    return(getText(text,true,colorTitleFont,size));
  }

  /**
   *
   */
  public static Text titleText(int i) {
    return(titleText(String.valueOf(i)));
  }

  /**
   *
   */
  public static Text headerText(String text) {
    return(getText(text,true,colorHeaderFont,headerFontSize));
  }

  /**
   *
   */
  public static Text headerText(String text, int size) {
    return(getText(text,true,colorHeaderFont,size));
  }

  /**
   *
   */
  public static Text headerText(int i) {
    return(headerText(String.valueOf(i)));
  }

  /**
   *
   */
  private static Text getText(String text, boolean bold, String color, int size) {
    Text T = new Text(text,bold,false,false);
    T.setFontColor(color);
    T.setFontSize(size);
    return(T);
  }

  /**
   *
   */
  public static void setStyle(InterfaceObject O) {
    O.setAttribute("style",styleAttribute);
  }

  /**
   *
   */
  public static void setStyle(InterfaceObjectContainer O) {
    O.setAttribute("style",styleAttribute);
  }
}
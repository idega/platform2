package com.idega.block.text.business;

import java.sql.*;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.text.data.TextModule;
import com.idega.util.idegaTimestamp;

public class TextBusiness{

  public static TextModule getTextModule(ModuleInfo modinfo) {
    int textID = -1;
    String text_id = modinfo.getParameter("text_id");

    if ( text_id != null ) {
      try {
        textID = Integer.parseInt(text_id);
      }
      catch (NumberFormatException e) {
        textID = -1;
      }
    }

    if ( textID != -1 ) {
      try {
        TextModule text = new TextModule(textID);
        return text;
      }
      catch (SQLException e) {
        return new TextModule();
      }
    }
    else {
      return new TextModule();
    }
  }

  public static void deleteText(ModuleInfo modinfo) {
    try {
      TextModule text = getTextModule(modinfo);
      text.delete();
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      System.out.println("Text not deleted");
    }
  }

  public static void saveText(ModuleInfo modinfo,boolean update) {
		modinfo.getSession().removeAttribute("image_id");
    int textID = -1;
    String text_id = modinfo.getParameter("text_id");

    if ( text_id != null ) {
      try {
        textID = Integer.parseInt(text_id);
      }
      catch (NumberFormatException e) {
        textID = -1;
      }
    }

    if ( textID != -1 ) {
      String text_headline = modinfo.getParameter("text_headline");
        if ( text_headline == null ) { text_headline = ""; }

      String text_body = modinfo.getParameter("text_body");
        if ( text_body == null ) { text_body = ""; }

      String include_image = modinfo.getParameter("insertImage");
        if ( include_image == null ) { include_image = "N"; }

      int imageID = -1;
      String image_id = modinfo.getParameter("image_id");
      if ( image_id != null ) {
        try {
          imageID = Integer.parseInt(image_id);
        }
        catch (Exception e) {
          imageID = -1;
        }
      }

      idegaTimestamp date = new idegaTimestamp();

      TextModule text;
        if ( update ) {
          try {
            text = new TextModule(textID);
          }
          catch (SQLException e) {
            text = new TextModule();
            update = false;
          }
        }
        else {
          text = new TextModule();
        }

      text.setTextHeadline( text_headline );
      text.setTextBody( text_body );
      text.setIncludeImage(include_image);
      text.setImageId(imageID);
      text.setTextDate( date.getTimestampRightNow());

      if ( update ) {
        try {
          text.update();
        }
        catch (SQLException e) {
          System.out.println("Text not updated.");
        }
      }
      else {
        try {
          text.insert();
        }
        catch (SQLException e) {
          System.out.println("Text not inserted.");
        }
      }
    }

  }
}


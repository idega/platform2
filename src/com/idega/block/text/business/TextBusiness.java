package com.idega.block.text.business;

import java.sql.*;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.text.data.*;
import com.idega.core.data.ICObjectInstance;
import com.idega.util.idegaTimestamp;
import java.util.List;

public class TextBusiness{

  public static TxText getText(int iTextId){
    TxText TX = new TxText();
    if ( iTextId > 0 ) {
      try {
       TX = new TxText(iTextId);
      }
      catch (SQLException e) {
        e.printStackTrace();
        TX = new TxText();
      }
    }
    else {
      TX =  null;
    }
    return TX;
  }



  public static void deleteText(int iTextId) {
    int iObjectInstanceId = TextFinder.getObjectInstanceIdFromTextId(iTextId);
    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
    try {
      t.begin();
    //  List O = TextFinder.listOfObjectInstanceTexts();
      TxText txText= new TxText(iTextId);
      List L = TextFinder.listOfLocalizedText(txText.getID());
      if(L != null){
        LocalizedText lt;
        for (int i = 0; i < L.size(); i++) {
          lt = (LocalizedText) L.get(i);
          lt.removeFrom(txText);
          lt.delete();
        }
      }

      if(iObjectInstanceId > 0  ){
        ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
        txText.removeFrom(obj);
      }
      txText.delete();
     t.commit();
    }
    catch(Exception e) {
      try {
        t.rollback();
      }
      catch(javax.transaction.SystemException ex) {
        ex.printStackTrace();
      }
      e.printStackTrace();
    }
  }


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
          e.printStackTrace();
        }
      }
      else {
        try {
          text.insert();
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

  }

   public static void saveText(int iTxTextId,int iLocalizedTextId,
            String sHeadline,String sTitle,String sBody,
            int iImageId,boolean useImage,int iLocaleId ,int iUserId){

     saveText( iTxTextId, iLocalizedTextId,
             sHeadline, sTitle,sBody,iImageId, useImage, iLocaleId , iUserId,-1,"");

   }

   public static void saveText(int iTxTextId,int iLocalizedTextId,
            String sHeadline,String sTitle,String sBody,
            int iImageId,boolean useImage,int iLocaleId ,int iUserId,String sAttribute){

     saveText( iTxTextId, iLocalizedTextId,
             sHeadline, sTitle,sBody,iImageId, useImage, iLocaleId , iUserId,-1,sAttribute);

   }

    public static void saveText(int iTxTextId,int iLocalizedTextId,
            String sHeadline,String sTitle,String sBody,
            int iImageId,boolean useImage,int iLocaleId ,int iUserId,int iInstanceId){

     saveText( iTxTextId, iLocalizedTextId,sHeadline, sTitle,sBody,iImageId, useImage, iLocaleId , iUserId,iInstanceId,"");

   }


  public static void saveText(int iTxTextId,int iLocalizedTextId,
            String sHeadline,String sTitle,String sBody,
            int iImageId,boolean useImage,int iLocaleId ,int iUserId,int InstanceId,String sAttribute){

    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
    try {
      t.begin();
      boolean txUpdate = false;
      boolean locUpdate = false;
      TxText txText = null;
      LocalizedText locText = null;
      if(iTxTextId > 0){
        txUpdate = true;
        txText = new TxText(iTxTextId);
        if(iLocalizedTextId > 0){
          locUpdate = true;
          locText = new LocalizedText(iLocalizedTextId);
        }
        else{
          locUpdate = false;
          locText = new LocalizedText();
        }
      }
      else{
        txUpdate = false;
        locUpdate = false;
        txText = new TxText();
        locText = new LocalizedText();
      }

      locText.setHeadline(sHeadline);
      locText.setBody(sBody);
      locText.setLocaleId(iLocaleId);
      locText.setTitle( sTitle);
      locText.setUpdated(idegaTimestamp.getTimestampRightNow());

      txText.setImageId(iImageId);
      txText.setIncludeImage(useImage);
      txText.setUpdated(idegaTimestamp.getTimestampRightNow());

      if(sAttribute != null){
        txText.setAttribute(sAttribute);
      }

      if(txUpdate ){
        txText.update();
        if(locUpdate){
          locText.update();
        }
        else if(!locUpdate){
          locText.setCreated(idegaTimestamp.getTimestampRightNow());
          locText.insert();
          locText.addTo(txText);
        }
      }
      else if(!txUpdate){
        txText.setCreated(idegaTimestamp.getTimestampRightNow());
        txText.setUserId(iUserId);
        txText.insert();
        locText.setCreated(idegaTimestamp.getTimestampRightNow());
        locText.insert();
        locText.addTo(txText);
        if(InstanceId > 0){
          System.err.println("instance er til");
          ICObjectInstance objIns = new ICObjectInstance(InstanceId);
          System.err.println(" object instance "+objIns.getID() + objIns.getName());
          txText.addTo(objIns);
        }
      }
      t.commit();
    }
    catch(Exception e) {
      try {
        t.rollback();
      }
      catch(javax.transaction.SystemException ex) {
        ex.printStackTrace();
      }
      e.printStackTrace();
    }


  }
}


package com.idega.block.text.business;

import java.sql.*;
import com.idega.presentation.IWContext;
import com.idega.block.text.data.*;
import com.idega.core.data.ICObjectInstance;
import com.idega.util.idegaTimestamp;
import java.util.List;
import java.util.Iterator;

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



  public static boolean deleteBlock(int instanceid){
    List L = TextFinder.listOfTextForObjectInstanceId(instanceid);
    if(L!= null){
      Iterator I = L.iterator();
      while(I.hasNext()){
        TxText T = (TxText) I.next();
        deleteText(T.getID(),instanceid );
      }
      return true;
    }
    else
      return false;
  }

  public static void deleteText(int iTextId){
    deleteText(iTextId ,TextFinder.getObjectInstanceIdFromTextId(iTextId));
  }

  public static void deleteText(int iTextId , int instanceid) {
    int iObjectInstanceId = TextFinder.getObjectInstanceIdFromTextId(iTextId);

    try {

      TxText txText= new TxText(iTextId);
      if(iObjectInstanceId > 0  ){
          ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
          txText.removeFrom(obj);
      }
      int contentId = txText.getContentId();
      txText.delete();
      ContentBusiness.deleteContent( contentId) ;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public static TxText saveText(int iTxTextId,
                              int iLocalizedTextId,
                              int iLocaleId ,
                              int iUserId,
                              int InstanceId,
                              Timestamp tsPubFrom,
                              Timestamp tsPubTo,
                              String sHeadline,
                              String sTitle,
                              String sBody,
                              String sAttribute,
                              List listOfFiles){


    try {
      boolean update = false;
      TxText eTxText = new TxText();
      if(iTxTextId > 0){
        eTxText = new TxText(iTxTextId);
        update = true;
      }
      Content eContent = ContentBusiness.saveContent(eTxText.getContentId(),iLocalizedTextId,iLocaleId,iUserId,tsPubFrom,tsPubTo,sHeadline,sBody,sTitle,listOfFiles);
      if(eContent != null){
        if(sAttribute != null){
          eTxText.setAttribute(sAttribute);
        }
        if(eContent.getID() > 0)
          eTxText.setContentId(eContent.getID());
        if(update)
          eTxText.update();
        else
          eTxText.insert();
        if(InstanceId > 0){
          //System.err.println("instance er til");
          ICObjectInstance objIns = new ICObjectInstance(InstanceId);
          //System.err.println(" object instance "+objIns.getID() + objIns.getName());
          eTxText.addTo(objIns);
        }
        return eTxText;
      }
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }
}


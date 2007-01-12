package com.idega.block.text.business;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.text.data.Content;
import com.idega.block.text.data.ContentHome;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.TxText;
import com.idega.core.component.data.ICObjectInstance;

public class TextBusiness{

  public static TxText getText(int iTextId){
    TxText TX = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).createLegacy();
    if ( iTextId > 0 ) {
      try {
       TX = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).findByPrimaryKeyLegacy(iTextId);
      }
      catch (SQLException e) {
        e.printStackTrace();
        TX = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).createLegacy();
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
	else {
		return false;
	}
  }

  public static void deleteText(int iTextId){
    deleteText(iTextId ,TextFinder.getObjectInstanceIdFromTextId(iTextId));
  }

  public static void deleteText(int iTextId , int instanceid) {
    int iObjectInstanceId = TextFinder.getObjectInstanceIdFromTextId(iTextId);

    try {

      TxText txText= ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).findByPrimaryKeyLegacy(iTextId);
      if(iObjectInstanceId > 0  ){
          ICObjectInstance obj = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(iObjectInstanceId);
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
      TxText eTxText = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).createLegacy();
      if(iTxTextId > 0){
        eTxText = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).findByPrimaryKeyLegacy(iTxTextId);
        update = true;
      }
      Content eContent = ContentBusiness.saveContent(eTxText.getContentId(),iLocalizedTextId,iLocaleId,iUserId,tsPubFrom,tsPubTo,sHeadline,sBody,sTitle,listOfFiles);
      if(eContent != null){
        if(sAttribute != null){
          eTxText.setAttribute(sAttribute);
        }
        if(eContent.getID() > 0) {
			eTxText.setContentId(eContent.getID());
		}
        if(update) {
			eTxText.update();
		}
		else {
			eTxText.insert();
		}
        if(InstanceId > 0 && !update){
          //System.err.println("instance er til");
          ICObjectInstance objIns = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(InstanceId);
          //System.err.println(" object instance "+objIns.getID() + objIns.getName());
          //objIns.removeFrom(new ICCategory());
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
  
  public static boolean addLocalizedTextToTxText(LocalizedText lText, TxText text) throws RemoteException {
  	int contentId = text.getContentId();
  	try {
			Content cont = ((ContentHome) com.idega.data.IDOLookup.getHome(Content.class)).findByPrimaryKey(contentId);
			cont.addTo(lText);
			return true;
		} catch (FinderException e) {
			e.printStackTrace(System.err);
			return false;
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			return true;
		}
  	
	}
}


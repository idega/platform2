package com.idega.block.text.business;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.TxText;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.data.EntityFinder;
import com.idega.data.IDOEntity;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDORelationshipException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class TextFinder {

  public TextFinder() {

  }

  public static ContentHelper getContentHelper(int iTxTextId){
    TxText T = getText(iTxTextId);

    if(T!=null){
      return ContentFinder.getContentHelper(T.getContentId());
    }
    else
      return null;
  }

   public static ContentHelper getContentHelper(int iTxTextId,int iLocaleId){
    TxText T = getText(iTxTextId);
    if(T!=null){
      return ContentFinder.getContentHelper(T.getContentId(),iLocaleId );
    }
    return null;
  }

  public static ContentHelper getContentHelper(int iTxTextId,Locale locale){
    TxText T = getText(iTxTextId);
    if(T!=null){
      return ContentFinder.getContentHelper(T.getContentId(),locale  );
    }
    return null;
  }

  public static ContentHelper getContentHelper(String sAttribute,Locale locale){
    TxText T = getText(sAttribute);
    if(T!=null){
       return ContentFinder.getContentHelper(T.getContentId(),locale  );
    }
    else
      return null;
  }

  public static ContentHelper getContentHelper(String sAttribute,int iLocaleId){
    TxText T = getText(sAttribute);
    if(T!=null){
      return ContentFinder.getContentHelper(T.getContentId(),iLocaleId );
    }
    return null;
  }

  public static List listOfLocalizedText(int iTxTextId){
    try {
      TxText tt = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).findByPrimaryKeyLegacy(iTxTextId);
      return ContentFinder.listOfLocalizedText(tt.getContentId());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static List listOfLocalizedText(int iTxTextId,int iLocaleId){
    try {
      TxText T = getText(iTxTextId);
      return ContentFinder.listOfLocalizedText(T.getContentId(),iLocaleId );
    }
    catch (Exception ex) {
      return null;
    }
  }

  public static List listOfLocalizedText(IDOLegacyEntity entity){
    List L = null;
    try {
      LocalizedText lt = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
      L = EntityFinder.findRelated(entity,lt);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      L = null;
    }
    return L;
  }

  public static LocalizedText getLocalizedText(IDOLegacyEntity entity, int iLocaleID){
    return getLocalizedText(entity,entity.getID(),iLocaleID);
  }
  public static LocalizedText getLocalizedText(IDOEntity entity, int iLocaleID) {
  	Integer i = (Integer) entity.getPrimaryKey();
  	return getLocalizedText(entity,i.intValue(),iLocaleID);
  }

  public static LocalizedText getLocalizedText(IDOLegacyEntity entity, int entityID, int iLocaleID){
		LocalizedText localText = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
    try {
      if(entity!=null){
	      List list = EntityFinder.findRelated(entity,localText);
	      if ( list != null ) {
	        Iterator iter = list.iterator();
	        while (iter.hasNext()) {
	          LocalizedText item = (LocalizedText) iter.next();
	          if ( item.getLocaleId() == iLocaleID ) {
	            return item;
	          }
	        }
	      }
      }
      return null;
    }
    catch (SQLException e) {
			e.printStackTrace();
      return null;
    }
  }
  public static LocalizedText getLocalizedText(IDOEntity entity, int entityID, int iLocaleID){
  	LocalizedText localText = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
  	Collection list = null;
  	try {
  		if(entity!=null){
  			try {
  				list = localText.ejbFindRelatedEntities(entity);
  			}
  			catch (IDORelationshipException e) {
  				list = null;
  			}
 // 			List list = EntityFinder.findRelated(entity,localText);
  			if ( list != null ) {
  				Iterator iter = list.iterator();
  				while (iter.hasNext()) {
  					LocalizedText item = (LocalizedText) iter.next();
  					if ( item.getLocaleId() == iLocaleID ) {
  						return item;
  					}
  				}
  			}
  		}
  		return null;
  	}
  	catch (Exception e) {//changed from SQLException
  		e.printStackTrace();
  		return null;
  	}
  }

  public static String[] getLocalizedString(IDOLegacyEntity entity, int iLocaleID) {
    String[] locString = new String[3];

    if ( entity != null ) {
      LocalizedText locText = TextFinder.getLocalizedText(entity,iLocaleID);
      if ( locText != null ) {
        locString[0] = locText.getHeadline();
        locString[1] = locText.getBody();
        locString[2] = locText.getTitle();
      }
    }

    return locString;
  }
  public static String[] getLocalizedString(IDOEntity entity, int iLocaleID) {
  	String[] locString = new String[3];

  	if ( entity != null ) {
  		LocalizedText locText = TextFinder.getLocalizedText(entity,iLocaleID);
  		if ( locText != null ) {
  			locString[0] = locText.getHeadline();
  			locString[1] = locText.getBody();
  			locString[2] = locText.getTitle();
  		}
  	}

  	return locString;
  }

  public static LocalizedText getLocalizedText(int iTxTextId,int iLocaleId){
    LocalizedText LTX = null;
    List L =   listOfLocalizedText(iTxTextId,iLocaleId);
    if(L!= null){
      LTX = (LocalizedText) L.get(0);
    }

    return LTX;
  }

  public static LocalizedText getLocalizedText(int iTxTextId,Locale locale){
    int Lid = getLocaleId(locale);
    return getLocalizedText(iTxTextId,Lid);
  }

  public static int getObjectInstanceTextId(ICObjectInstance eObjectInstance){
    try {
      List L = EntityFinder.findRelated(eObjectInstance ,((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).createLegacy());
      if(L!= null){
        return ((TxText) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -2;

    }
  }

  public static int getObjectInstanceIdFromTextId(int iTextId){
    try {
      TxText tx = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).findByPrimaryKeyLegacy(iTextId);
      List L = EntityFinder.findRelated( tx,((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).createLegacy());
      if(L!= null){
        return ((ICObjectInstance) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -2;

    }
  }

  public static List listOfTextForObjectInstanceId(int instanceid){
    try {
      ICObjectInstance obj = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(instanceid );
      return listOfTextForObjectInstanceId(obj);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfTextForObjectInstanceId( ICObjectInstance obj){
    try {
      List L = EntityFinder.findRelated(obj,((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).createLegacy());
      return L;
    }
    catch (SQLException ex) {
      return null;
    }
  }

/*
   public static LocalizedText listOfLocalizedText(int iTxTextId,int iLocaleId){
    LocalizedText LTX = null;
      List L =  listOfLocalizedText(iTxTextId);

      if(L!=null){
        int len = L.size();
        for (int i = 0; i < len; i++) {
          LocalizedText ltx = (LocalizedText) L.get(i);
          if(ltx.getLocaleId() == iLocaleId){
            LTX = ltx;
            break;
          }
        }
    }
    return LTX;
  }
*/
  public static TxText getText(String sAttribute){
    TxText th = null;
    try {
      List L = EntityFinder.findAllByColumn(((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).createLegacy(),com.idega.block.text.data.TxTextBMPBean.getColumnNameAttribute(),sAttribute);
      if(L!= null)
        th =  (TxText) L.get(0);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      th = null;
    }
    return th;
  }

  public static TxText getText(int iTextId){
    try {
      return ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).findByPrimaryKeyLegacy(iTextId);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }
/*
  public static listOfObjectInstanceTexts(){

  }
*/
  public static List listOfLocales(){
    return ICLocaleBusiness.listLocaleCreateIsEn();
  }

  public static int getLocaleId(Locale locale){
   return ICLocaleBusiness.getLocaleId(locale);
  }

  /**
   * @deprecated Use <code>getLocaleReturnIcelandicLocaleIfnotFound(iLocaleId)</code> instead. 
   * @see com.idega.core.localisation.business.ICLocaleBusiness#getLocaleReturnIcelandicLocaleIfNotFound(int)
   * @param iLocaleId
   * @return 
   */
  public static Locale getLocale(int iLocaleId){
  	return ICLocaleBusiness.getLocaleReturnIcelandicLocaleIfNotFound(iLocaleId);
    }




}

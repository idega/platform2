package com.idega.block.text.business;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.ejb.FinderException;
import com.idega.block.text.data.Content;
import com.idega.block.text.data.ContentHome;
import com.idega.block.text.data.LocalizedText;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.data.ICLocale;
import com.idega.core.localisation.data.ICLocaleHome;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ContentFinder {

  public static ContentHelper getContentHelper(int iContentId){
    ContentHelper CH = new ContentHelper();
    Content content = getContent(iContentId);
    if(content!=null){
      CH.setContent( content);
      CH.setLocalizedText( listOfLocalizedText(iContentId));
      CH.setFiles(listOfContentFiles(iContentId, null));
      return CH;
    }
    else
      return null;
  }

  public static ContentHelper getContentHelper(int iContentId,int iLocaleId){
	  return getContentHelper(iContentId, iLocaleId, null);
  }
   public static ContentHelper getContentHelper(int iContentId,int iLocaleId, String datasource){
    ContentHelper CH = new ContentHelper();
	int newLocaleID = tmpDatasourceHack(iLocaleId, datasource);
    Content content = getContent(iContentId, datasource);
    if(content!=null){
      CH.setContent(content);
      CH.setLocalizedText(getLocalizedText(iContentId,newLocaleID, datasource));
      CH.setFiles(listOfContentFiles(iContentId, datasource));
      return CH;
    }
    else{
      return null;
    }
  }
   
   /**
    * Check for the corresponding locale id on the other datasource
    * @return
    */
   private static int tmpDatasourceHack(int iLocaleId, String datasource) {
	   if (datasource != null) {
		   try {
			ICLocaleHome lHome = (ICLocaleHome) IDOLookup.getHome(ICLocale.class);
			String oldDatasource = lHome.getDatasource();
			if (!oldDatasource.equals(datasource)) {
				ICLocale locale = lHome.findByPrimaryKey(new Integer(iLocaleId));
				String localeName = locale.getLocale();
				
				lHome.setDatasource(datasource, false);
				ICLocale newLocale = lHome.findByLocaleName(localeName);
				int newID = ((Integer)newLocale.getPrimaryKey()).intValue();
				lHome.setDatasource(oldDatasource, false);
				return newID;
			}
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	   }
	   return iLocaleId;
   }

  public static ContentHelper getContentHelper(int iContentId,Locale locale){
    ContentHelper CH = new ContentHelper();
     Content content = getContent(iContentId);

    if(content!=null){
      CH.setContent(content);
      CH.setLocalizedText(getLocalizedText(iContentId,locale, null));
      CH.setFiles(listOfContentFiles(iContentId, null));
      return CH;
    }
    else
      return null;
  }


  public static List listOfLocalizedText(int iContentId){
    List L = null;
    try {
      Content tt = ((com.idega.block.text.data.ContentHome)com.idega.data.IDOLookup.getHomeLegacy(Content.class)).findByPrimaryKeyLegacy(iContentId);
      LocalizedText lt = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
      L = EntityFinder.findRelated(tt,lt);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      L = null;
    }
    return L;
  }

  public static List listOfLocalizedText(Content eContent){
    try {
      return EntityFinder.findRelated(eContent,((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy());
    }
    catch (SQLException ex) {
      ex.printStackTrace();

    }
    return null;
  }

  public static List listOfLocalizedText(int iContentId,int iLocaleId){
	  return listOfLocalizedText(iContentId, iLocaleId, null);
  }
  
  public static List listOfLocalizedText(int iContentId,int iLocaleId, String datasource){
    StringBuffer sql = new StringBuffer("select lt.* from tx_localized_text lt, tx_content t,tx_content_TX_LOCALIZED_TEXT ttl ");
    sql.append(" where ttl.tx_content_id = t.tx_content_id ");
    sql.append(" and ttl.tx_localized_text_id = lt.tx_localized_text_id ");
    sql.append(" and t.tx_content_id = ");
    sql.append(iContentId);
    sql.append(" and lt.ic_locale_id =  ");
    sql.append(iLocaleId);
    try {
		IDOLegacyEntity ent = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
		if (datasource != null) {
			ent.setDatasource(datasource);
		}
      return EntityFinder.findAll(ent,sql.toString());
    }
    catch (SQLException ex) {
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

  public static LocalizedText getLocalizedText(IDOLegacyEntity entity, int entityID, int iLocaleID){
		LocalizedText localText = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
    try {
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
      return null;
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static LocalizedText getLocalizedText(int iContentId,int iLocaleId, String datasource){
    LocalizedText LTX = null;
    List L =   listOfLocalizedText(iContentId,iLocaleId, datasource);
    if(L!= null){
      LTX = (LocalizedText) L.get(0);
    }

    return LTX;
  }

  public static LocalizedText getLocalizedText(int iContentId,Locale locale, String datasource){
    int Lid = getLocaleId(locale);
    return getLocalizedText(iContentId,Lid, datasource);
  }

  public static List listOfContentFiles(int id, String datasource){
    try {
    	ContentHome cHome = null;
    	if (datasource == null) {
    		cHome = (ContentHome) IDOLookup.getHome(Content.class);
    	} else {
    		cHome = (ContentHome) IDOLookup.getHome(Content.class, datasource);
    	}
      return listOfContentFiles(cHome.findByPrimaryKeyLegacy(id));
    }
    catch (SQLException ex) {

    }
	catch (IDOLookupException e) {
		e.printStackTrace();
	}
    return null;
  }

  public static List listOfContentFiles(Content content){
    try {
      return (List)content.getContentFiles();  // EntityFinder.findRelated(content,((com.idega.core.data.ICFileHome)com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).createLegacy());
    }
    catch (IDORelationshipException e) {
		e.printStackTrace();
	}
    return null;
  }
  public static Content getContent(int iContentId){
	  return getContent(iContentId, null);
  }
  
  public static Content getContent(int iContentId, String datasource){
    try {
      if(iContentId > 0) {
		  ContentHome cHome = (ContentHome) IDOLookup.getHome(Content.class);
		  if (datasource != null) {
			  cHome = (ContentHome) IDOLookup.getHome(Content.class, datasource);
		  }
		  return cHome.findByPrimaryKey(iContentId);
//        return ((com.idega.block.text.data.ContentHome)com.idega.data.IDOLookup.getHomeLegacy(Content.class)).findByPrimaryKeyLegacy(iContentId);
      }
    }
//    catch (SQLException ex) {
//      ex.printStackTrace();
//
//    }
	catch (IDOLookupException e) {
		e.printStackTrace();
	}
	catch (FinderException e) {
		e.printStackTrace();
	}
    return null;
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

  public static Locale getLocale(int iLocaleId){
    Locale L = ICLocaleBusiness.getLocale(iLocaleId);
    if(L==null)
      L = new Locale("is","IS");
    return L;
  }




}

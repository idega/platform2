package com.idega.block.text.business;

import com.idega.data.EntityFinder;
import com.idega.block.text.business.ContentHelper;
import com.idega.util.LocaleUtil;
import com.idega.block.text.data.*;
import com.idega.data.GenericEntity;
import com.idega.data.EntityControl;
import java.util.List;
import java.util.Hashtable;
import java.util.Iterator;
import java.sql.SQLException;
import java.util.Locale;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICObjectInstance;
import com.idega.core.data.ICFile;

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
      CH.setFiles(listOfContentFiles(iContentId));
      return CH;
    }
    else
      return null;
  }

   public static ContentHelper getContentHelper(int iContentId,int iLocaleId){
    ContentHelper CH = new ContentHelper();
    Content content = getContent(iContentId);
    if(content!=null){
      CH.setContent(content);
      CH.setLocalizedText(getLocalizedText(iContentId,iLocaleId));
      CH.setFiles(listOfContentFiles(iContentId));
      return CH;
    }
    else{
      return null;
    }
  }

  public static ContentHelper getContentHelper(int iContentId,Locale locale){
    ContentHelper CH = new ContentHelper();
     Content content = getContent(iContentId);

    if(content!=null){
      CH.setContent(content);
      CH.setLocalizedText(getLocalizedText(iContentId,locale));
      CH.setFiles(listOfContentFiles(iContentId));
      return CH;
    }
    else
      return null;
  }


  public static List listOfLocalizedText(int iContentId){
    List L = null;
    try {
      Content tt = new Content(iContentId);
      LocalizedText lt = new LocalizedText();
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
      return EntityFinder.findRelated(eContent,new LocalizedText());
    }
    catch (SQLException ex) {
      ex.printStackTrace();

    }
    return null;
  }

  public static List listOfLocalizedText(int iContentId,int iLocaleId){
    StringBuffer sql = new StringBuffer("select lt.* from tx_localized_text lt, tx_content t,tx_content_TX_LOCALIZED_TEXT ttl ");
    sql.append(" where ttl.tx_content_id = t.tx_content_id ");
    sql.append(" and ttl.tx_localized_text_id = lt.tx_localized_text_id ");
    sql.append(" and t.tx_content_id = ");
    sql.append(iContentId);
    sql.append(" and lt.ic_locale_id =  ");
    sql.append(iLocaleId);
    try {
      return EntityFinder.findAll(new LocalizedText(),sql.toString());
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfLocalizedText(GenericEntity entity){
    List L = null;
    try {
      LocalizedText lt = new LocalizedText();
      L = EntityFinder.findRelated(entity,lt);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      L = null;
    }
    return L;
  }

  public static LocalizedText getLocalizedText(GenericEntity entity, int iLocaleID){
    return getLocalizedText(entity,entity.getID(),iLocaleID);
  }

  public static LocalizedText getLocalizedText(GenericEntity entity, int entityID, int iLocaleID){
		LocalizedText localText = new LocalizedText();
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

  public static LocalizedText getLocalizedText(int iContentId,int iLocaleId){
    LocalizedText LTX = null;
    List L =   listOfLocalizedText(iContentId,iLocaleId);
    if(L!= null){
      LTX = (LocalizedText) L.get(0);
    }

    return LTX;
  }

  public static LocalizedText getLocalizedText(int iContentId,Locale locale){
    int Lid = getLocaleId(locale);
    return getLocalizedText(iContentId,Lid);
  }

  public static List listOfContentFiles(int id){
    try {
      return listOfContentFiles(new Content(id));
    }
    catch (SQLException ex) {

    }
    return null;
  }

  public static List listOfContentFiles(Content content){
    try {
      return EntityFinder.findRelated(content,new ICFile());
    }
    catch (SQLException ex) {

    }
    return null;
  }

  public static Content getContent(int iContentId){
    try {
      if(iContentId > 0)
        return new Content(iContentId);
    }
    catch (SQLException ex) {
      ex.printStackTrace();

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
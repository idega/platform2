package com.idega.block.text.business;

import com.idega.data.EntityFinder;
import com.idega.block.text.business.TextHelper;
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
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
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
      TxText tt = new TxText(iTxTextId);
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
			EntityFinder.debug = true;
      List list = EntityFinder.findRelated(entity,localText);
			EntityFinder.debug = false;
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

  public static String[] getLocalizedString(GenericEntity entity, int iLocaleID) {
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
    TxText T = getText(iTxTextId);
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
      List L = EntityFinder.findRelated(eObjectInstance ,new TxText());
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
      TxText tx = new TxText(iTextId);
      List L = EntityFinder.findRelated( tx,new ICObjectInstance());
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
      ICObjectInstance obj = new ICObjectInstance(instanceid );
      return listOfTextForObjectInstanceId(obj);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfTextForObjectInstanceId( ICObjectInstance obj){
    try {
      List L = EntityFinder.findRelated(obj,new TxText());
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
      List L = EntityFinder.findAllByColumn(new TxText(),TxText.getColumnNameAttribute(),sAttribute);
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
      return new TxText(iTextId);
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

  public static Locale getLocale(int iLocaleId){
    Locale L = ICLocaleBusiness.getLocale(iLocaleId);
    if(L==null)
      L = new Locale("is","IS");
    return L;
  }




}
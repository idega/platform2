package com.idega.block.text.business;

import com.idega.data.EntityFinder;
import com.idega.block.text.business.TextHelper;
import com.idega.util.LocaleUtil;
import com.idega.block.text.data.*;
import java.util.List;
import java.util.Hashtable;
import java.sql.SQLException;
import java.util.Locale;
import com.idega.core.business.ICLocaleBusiness;
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

  public static TextHelper getTextHelper(int iTxTextId){
    TextHelper TH = new TextHelper();
    TxText T = getText(iTxTextId);
    if(T!=null){
      TH.setTxText(T);
      TH.setLocalizedText( listOfLocalizedText(iTxTextId));
      return TH;
    }
    else
      return null;
  }

   public static TextHelper getTextHelper(int iTxTextId,int iLocaleId){
    TextHelper TH = new TextHelper();
    TxText T = getText(iTxTextId);
    if(T!=null){
      TH.setTxText(T);
      TH.setLocalizedText(getLocalizedText(iTxTextId,iLocaleId));
      return TH;
    }
    else{
      System.err.println("no text");
      return null;
    }
  }

  public static TextHelper getTextHelper(int iTxTextId,Locale locale){
    TextHelper TH = new TextHelper();
    TxText T = getText(iTxTextId);

    if(T!=null){
      TH.setTxText(T);
      TH.setLocalizedText(getLocalizedText(iTxTextId,locale));
      return TH;
    }
    else
      return null;
  }

  public static TextHelper getTextHelper(String sAttribute,Locale locale){
    TextHelper TH = new TextHelper();
    TxText T = getText(sAttribute);
    if(T!=null){
      TH.setTxText(T);
      TH.setLocalizedText(getLocalizedText(T.getID(),locale));
      return TH;
    }
    else
      return null;
  }

  public static TextHelper getTextHelper(String sAttribute,int iLocaleId){
    TextHelper TH = new TextHelper();
    TxText T = getText(sAttribute);
    if(T!=null){
      TH.setTxText(T);
      TH.setLocalizedText(getLocalizedText(T.getID(),iLocaleId));
      return TH;
    }
    else
      return null;
  }

  public static List listOfLocalizedText(int iTxTextId){
    List L = null;
    try {
      TxText tt = new TxText(iTxTextId);LocalizedText lt = new LocalizedText();
      L = EntityFinder.findRelated(tt,lt);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      L = null;
    }
    return L;
  }

  public static List listOfLocalizedText(int iTxTextId,int iLocaleId){
    /*
    select lt.* from tx_localized_text lt, tx_text t,tx_text_localized ttl
    where ttl.tx_text_id = t.tx_text_id
    and ttl.tx_localized_text_id = lt.tx_localized_text_id
    and t.tx_text_id = 22
    and lt.ic_locale_id = 2

    */
    StringBuffer sql = new StringBuffer("select lt.* from tx_localized_text lt, tx_text t,TX_TEXT_TX_LOCALIZED_TEXT ttl ");
    sql.append(" where ttl.tx_text_id = t.tx_text_id ");
    sql.append(" and ttl.tx_localized_text_id = lt.tx_localized_text_id ");
    sql.append(" and t.tx_text_id = ");
    sql.append(iTxTextId);
    sql.append(" and lt.ic_locale_id =  ");
    sql.append(iLocaleId);
    //System.err.println(sql.toString());
    try {
      return EntityFinder.findAll(new LocalizedText(),sql.toString());
    }
    catch (SQLException ex) {
      return null;
    }

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

  public static List listOfLocales(){
    return ICLocaleBusiness.listLocaleCreateNew();
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
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

  public static TextHelper getTextHelper(int iTxTextId,Locale locale){
    TextHelper TH = new TextHelper();
    TxText T = getText(iTxTextId);

    if(T!=null){
      TH.setTxText(T);
      TH.setLocalizedText(listOfLocalizedText(iTxTextId,locale));
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
      TH.setLocalizedText(listOfLocalizedText(T.getID(),locale));
      return TH;
    }
    else
      return null;
  }

  public static List listOfLocalizedText(int iTxTextId){
    List L = null;
    try {
      L = EntityFinder.findReverseRelated(new TxText(),new LocalizedText());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      L = null;
    }
    if(L==null) System.err.println("failed");
    return L;
  }

  public static LocalizedText listOfLocalizedText(int iTxTextId,Locale locale){
    LocalizedText LTX = null;
      int Lid = getLocaleId(locale);
      List L =  listOfLocalizedText(iTxTextId);
      if(L!=null){
        for (int i = 0; i < L.size(); i++) {
          LocalizedText ltx = (LocalizedText) L.get(i);
          if(ltx.getID() == Lid){
            LTX = ltx;
            break;
          }
        }
    }
    return LTX;
  }

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
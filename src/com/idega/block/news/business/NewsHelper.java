package com.idega.block.news.business;

import com.idega.block.news.data.NwNews;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.business.TextFinder;
import java.util.Vector;
import java.util.List;
import java.util.Locale;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class NewsHelper{
  private NwNews eNews;
  private List lLocalizedText;

  public NwNews getNwNews(){
    return eNews;
  }

  public LocalizedText getLocalizedText(Locale locale){
    return getLocalizedText(TextFinder.getLocaleId(locale));
  }

  public LocalizedText getLocalizedText(int iLocaleId){
    LocalizedText LT= null,lt = null;
    if(lLocalizedText!=null){
      int len = lLocalizedText.size();
      for (int i = 0; i < len; i++) {
        LT = (LocalizedText) lLocalizedText.get(i);
        if(LT.getLocaleId() == iLocaleId)
          lt = LT;
      }
      return lt;
    }
    else
      return null;
  }

  public LocalizedText getLocalizedText(){
    LocalizedText LT= null,lt = null;
    if(lLocalizedText!=null){
      LT = (LocalizedText) lLocalizedText.get(0);
      return  LT;
    }
    else
      return null;
  }

  public List getLocalizedTexts(){
    return lLocalizedText;
  }
  public void setLocalizedText(LocalizedText text ){
    if(text != null){
      if(lLocalizedText!=null )
        lLocalizedText.add(text);
      else{
        lLocalizedText = new Vector();
        lLocalizedText.add(text);
      }
    }
  }

  public void setLocalizedText(List text ){
    if(text != null){
      lLocalizedText = text;
    }
  }
  public void setNews(NwNews news){
    eNews = news;
  }


}


package com.idega.block.text.business;



import com.idega.block.text.data.*;

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



public class ContentHelper{

  private Content eContent;

  private List lLocalizedText = null;

  private List icFiles = null;



  public Content getContent(){

    return eContent;

  }

  public LocalizedText getLocalizedText(Locale locale){

    LocalizedText LT= null,lt = null;

    if(lLocalizedText!=null){

      int len = lLocalizedText.size();

      for (int i = 0; i < len; i++) {

        LT = (LocalizedText) lLocalizedText.get(i);

        if(LT.getLocaleId() == TextFinder.getLocaleId(locale))

          lt = LT;

      }

      return lt;

    }

    else

      return null;

  }



  public LocalizedText getLocalizedText(){

    LocalizedText LT= null;

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

  public void setContent(Content content){

    eContent = content;

  }

  /**

   * Sets a List of ICFile

   */

  public void setFiles(List listOfFiles){

    icFiles = listOfFiles ;

  }



 /**

 * Returns a List of ICFile

 */

  public List getFiles(){

    return icFiles ;

  }

  public boolean hasFiles() {
	  return icFiles != null && !icFiles.isEmpty();
  }


}




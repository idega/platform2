package com.idega.block.news.business;



import com.idega.block.news.data.NwNews;
import com.idega.block.text.business.ContentHelper;





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

  private ContentHelper cHelper;



  public NwNews getNwNews(){

    return this.eNews;

  }



  public void setNews(NwNews news){

    this.eNews = news;

  }



  public  ContentHelper getContentHelper(){

    return this.cHelper ;

  }



  public void setContentHelper(ContentHelper contentHelper){

    this.cHelper = contentHelper;

  }





}




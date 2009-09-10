package com.idega.block.text.business;



import com.idega.block.text.data.TxText;





/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved

 * Company:      idega

  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>

 * @version 1.1

 */



public class TextHelper{

  private TxText eText;

  private ContentHelper eContentHelper;



  public TxText getTxText(){

    return this.eText;

  }

  public void setTxText(TxText text){

    this.eText = text;

  }

  public void setContentHelper(ContentHelper eContentHelper){

    this.eContentHelper = eContentHelper;

  }



  public ContentHelper getContentHelper(){

    return this.eContentHelper ;

  }



}




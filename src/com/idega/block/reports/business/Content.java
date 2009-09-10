package com.idega.block.reports.business;



 /**

  * Title:

  * Description:

  * Copyright:    Copyright (c) 2001

  * Company:      idega multimedia

  * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>

  * @version 1.0

  */



 public class Content{



  private Object[] Content;

  private int index = 0;

  private int size = 0;



  public Content(Object[] content) {

    this.size = content.length;

    this.Content = content;

  }

  public Object getContent(int index) {

    return this.Content[index];

  }

  public Object[] getWholeContent() {

    return this.Content;

  }

  public void setContent(int index,Object content) {

    this.Content[index] = content;

  }

  public void setContent( Object[] content){

    this.Content = content;

    this.size = content.length;

  }

  public int size(){

    return this.size;

  }

}






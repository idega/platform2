package com.idega.block.reports.business;



 /**

  * Title:

  * Description:

  * Copyright:    Copyright (c) 2001

  * Company:      idega multimedia

  * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>

  * @version 1.0

  */



 public class ReportContent{



  private String[] Content;

  private int index = 0;

  private int size = 0;



  public ReportContent(String[] content) {

    this.size = content.length;

    this.Content = content;

  }

  public String getContent(int index) {

    return this.Content[index];

  }

  public String[] getWholeContent() {

    return this.Content;

  }

  public void setContent(int index,String content) {

    this.Content[index] = content;

  }

  public void setContent( String[] content){

    this.Content = content;

    this.size = content.length;

  }

  public int size(){

    return this.size;

  }

}






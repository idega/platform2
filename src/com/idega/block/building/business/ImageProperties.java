 package com.idega.block.building.business;





 /**

  * Title:

  * Description:

  * Copyright:    Copyright (c) 2001

  * Company:      idega multimedia

  * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>

  * @version 1.0

  */



 public class ImageProperties{

    private String sName,sContentType,sPath,sWebPath;



    public ImageProperties(String name,String contenttype,String path,String webpath){

      this.sContentType = contenttype;

      this.sName = name;

      this.sPath = path;

      this.sWebPath = webpath;

    }

    public String getName(){

      return sName;

    }

    public void setName(String name){

      this.sName = name;

    }

    public String getContentType(){

      return sContentType;

    }

    public void setContentType(String cont){

       this.sContentType = cont;

    }

    public String getPath(){

      return sPath;

    }

    public void getPath(String path){

      this.sPath = path;

    }

     public String getWebPath(){

      return sWebPath;

    }

    public void getWebPath(String path){

      this.sWebPath = path;

    }



  }

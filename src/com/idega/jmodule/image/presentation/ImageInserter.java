package com.idega.jmodule.image.presentation;

/**
 * Title: ImageInserter
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Eirikur Hrafnsson, eiki@idega.is
 * @version 1.0
 *
 */


import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.image.data.*;
import com.idega.jmodule.image.business.*;

public class ImageInserter extends JModuleObject{

private int imageId = -1;
private String imSessionImageName = "image_id";
private String defaultImageURL = "/pics/news/x.gif";
private String adminURL = "/image/insertimage.jsp";
private String nameOfWindow = "Ný Mynd";
private String sUseBoxString = "Nota mynd: ";
private int maxImageWidth = 140;
private boolean hasUseBox = true;

public ImageInserter(){
}

public ImageInserter(int imageId) {
  this.imageId=imageId;
}

public ImageInserter(String imSessionImageName) {
  this.imSessionImageName=imSessionImageName;
}

public ImageInserter(int imageId, String imSessionImageName) {
  this.imageId=imageId;
  this.imSessionImageName=imSessionImageName;
}

  public void main(ModuleInfo modinfo)throws Exception{

      String imageSessionId = (String) modinfo.getSession().getAttribute(imSessionImageName);

      if ( imageSessionId != null ) {
        imageId = Integer.parseInt(imageSessionId);
        modinfo.removeSessionAttribute(imSessionImageName);
      }

      Image image;
        if ( imageId == -1 ) {
          image = new Image(defaultImageURL);
        }
        else {
          image = new Image(imageId);
        }
        image.setMaxImageWidth(this.maxImageWidth);
        image.setNoImageLink();

      Window insertNewsImageWindow = new Window(nameOfWindow,ImageBusiness.IM_BROWSER_WIDTH,ImageBusiness.IM_BROWSER_HEIGHT,adminURL);
      Link imageAdmin = new Link(image,insertNewsImageWindow);
        imageAdmin.addParameter("submit","new");
        imageAdmin.addParameter("im_image_session_name",imSessionImageName);
        if ( imageId != -1 )  imageAdmin.addParameter("image_id",imageId);

      HiddenInput hidden = new HiddenInput(imSessionImageName,imageId+"");
      CheckBox insertImage = new CheckBox("insertImage","Y");
        insertImage.setChecked(true);

      Text imageText = new Text(sUseBoxString);
        imageText.setFontSize(1);

      Table imageTable = new Table(1,2);
        imageTable.setAlignment(1,2,"center");
        imageTable.add(imageAdmin,1,1);
        if(hasUseBox){
          imageTable.add(imageText,1,2);
          imageTable.add(insertImage,1,2);
        }
        imageTable.add(hidden,1,2);

      add(imageTable);
  }

  public void setHasUseBox(boolean useBox){
    this.hasUseBox = useBox;
  }

  public void setUseBoxString(String sUseBoxString){
    this.sUseBoxString = sUseBoxString;
  }

  public void setDefaultImageURL(String defaultImageURL){
    this.defaultImageURL = defaultImageURL;
  }

  public void setMaxImageWidth(int maxWidth){
    this.maxImageWidth = maxWidth;
  }

  public void setAdminURL(String adminURL) {
    this.adminURL=adminURL;
  }

  public void setImageId(int imageId) {
    this.imageId=imageId;
  }

  public void setImSessionImageName(String imSessionImageName) {
    this.imSessionImageName=imSessionImageName;
  }
}

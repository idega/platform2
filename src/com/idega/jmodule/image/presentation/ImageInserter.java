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
private String adminURL = "/image/insertimage.jsp";

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
      }

      Image image;
        if ( imageId == -1 ) {
          image = new Image("/pics/news/x.gif");
        }
        else {
          image = new Image(imageId);
        }

      Window insertNewsImageWindow = new Window("Ný mynd",adminURL);
      Link imageAdmin = new Link(image,insertNewsImageWindow);
        imageAdmin.addParameter("submit","new");

      HiddenInput hidden = new HiddenInput(imSessionImageName,imageId+"");
      CheckBox insertImage = new CheckBox("insertImage","Y");
        insertImage.setChecked(true);

      Text imageText = new Text("Nota mynd: ");
        imageText.setFontSize(1);

      Table imageTable = new Table(1,2);
        imageTable.setAlignment(1,2,"center");
        imageTable.add(imageAdmin,1,1);
        imageTable.add(imageText,1,2);
        imageTable.add(insertImage,1,2);
        imageTable.add(hidden,1,2);

      add(imageTable);
  }

  public void setAdminURL(String adminURL) {
    this.adminURL=adminURL;
  }

  public void setImSessionImageName(String imSessionImageName) {
    this.imSessionImageName=imSessionImageName;
  }
}

package com.idega.jmodule.image.business;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.HttpServlet;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.servlet.*;
import com.idega.data.EntityFinder;
//import com.idega.idegaweb.*;
import com.idega.jmodule.image.data.*;

/**
 * Title: ImageBusiness
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Eirikur Hrafnsson
 * @version 1.0
 *
 */

//public class ImageBusiness extends JModule  {
public class ImageBusiness  {

public static Properties getBundleProperties(ModuleInfo modinfo) throws FileNotFoundException,IOException{
  //IWMainApplication application = getApplication();
  String fileSeperator = System.getProperty("file.separator");
  //FileInputStream fin = new FileInputStream(new File( application.getRealPath("/")+fileSeperator+"image"+fileSeperator+"properties"+fileSeperator+"bundle.properties" ));
  FileInputStream fin = new FileInputStream(new File( modinfo.getServletContext().getRealPath("/")+fileSeperator+"image"+fileSeperator+"properties"+fileSeperator+"bundle.properties" ));
  Properties prop = new Properties();
  prop.load(fin);
  fin.close();
  return prop;
}

private static Properties getBundleProperties( HttpServlet servlet ) throws FileNotFoundException,IOException{
  String fileSeperator = System.getProperty("file.separator");
  FileInputStream fin = new FileInputStream(new File( servlet.getServletContext().getRealPath("/")+fileSeperator+"image"+fileSeperator+"properties"+fileSeperator+"bundle.properties" ));
  Properties prop = new Properties();
  prop.load(fin);
  fin.close();
  return prop;
}

public static void saveImageToCatagories(int imageId, String[] categoryId)throws SQLException {
//debug eiki parent id?? fix this
  ImageEntity image = new ImageEntity(imageId);
  image.setParentId(-1);
  image.update();

  for (int i = 0; i < categoryId.length; i++) {
    try{
      int category = Integer.parseInt(categoryId[i]);
      ImageCatagory cat = new ImageCatagory(category);
      cat.addTo(image);
    }
    catch(NumberFormatException e){
      System.err.println("ImageBusiness: categoryId is not a number");
    }
  }
}






/**
 * unimplemented
 */
public void makeDefaultSizes(){




//  try{
//   Properties prop = getBundleProperties(getModuleInfo());
//
//  add(prop.getProperty("image1.width"));
//  }
//  catch(Exception ex){}

}



/*
        IWMainApplicationSettings list = getApplication().getSettings();
	//com.idega.idegaweb.IWPropertyList list = new com.idega.idegaweb.IWPropertyList("test.xml");
	list.setProperty("my","test");
	list.store();


*/


    public static List getImageCatagories(int parentID){
      try {
        if (parentID > 0){
          return EntityFinder.findAll(ImageCatagory.getStaticImageCatagoryInstance(),"Select * from " + ImageCatagory.getStaticImageCatagoryInstance().getEntityName() + " Where " + ImageCatagory.getStaticImageCatagoryInstance().getParentIdColumnName() + " = '" + parentID + "'");
        }else{
          return EntityFinder.findAll(ImageCatagory.getStaticImageCatagoryInstance());
        }
      }
      catch (Exception ex) {
        return null;
      }

    }

    public static List getAllImageCatagories(){
      return ImageBusiness.getImageCatagories(-1);
    }


}//end of class


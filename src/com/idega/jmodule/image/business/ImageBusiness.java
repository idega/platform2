package com.idega.jmodule.image.business;

import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServlet;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.servlet.*;
//import com.idega.idegaweb.*;
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
public class ImageBusiness extends IWCoreServlet  {

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
}//end of class


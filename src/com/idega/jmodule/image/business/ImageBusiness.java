package com.idega.jmodule.image.business;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.HttpServlet;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.servlet.*;
import com.idega.data.GenericEntity;
import com.idega.data.DatastoreInterface;
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
 * unfinished change to entities
 */
public static String getImageID(Connection Conn)throws SQLException{

  String dataBaseType = com.idega.data.DatastoreInterface.getDataStoreType(Conn);

  Statement Stmt = Conn.createStatement();
  ResultSet RS;
  String ImageId;

  if( !(dataBaseType.equals("oracle")) ) {
	RS = Stmt.executeQuery("select gen_id(image_gen,1) from rdb$database");
  }
  else{		//oracle
	RS = Stmt.executeQuery("select image_seq.nextval from dual");
  }

  RS.next();
  ImageId = RS.getString(1);

  Stmt.close();
  RS.close();
  //debug getSession().setAttribute("image_id",ImageId);

return ImageId;
}


public static void handleEvent(ModuleInfo modinfo,ImageHandler handler) throws Throwable{

  String action = modinfo.getRequest().getParameter("action");
  String scaling = modinfo.getRequest().getParameter("scale.x");
  int ImageId = handler.getOriginalImageId();

  if ( action != null){
        if ( action.equalsIgnoreCase("Grayscale") ) handler.convertModifiedImageToGrayscale();
        else if ( action.equalsIgnoreCase("Emboss") ) handler.embossModifiedImage();
        else if ( action.equalsIgnoreCase("Invert") ) handler.invertModifiedImage();
        else if ( action.equalsIgnoreCase("Sharpen") ) handler.sharpenModifiedImage();
        else if( action.equalsIgnoreCase("Save") ){
          handler.writeModifiedImageToDatabase();
         // eyða úr sessions og applications browser.refresh();
        }
        else if( action.equalsIgnoreCase("Undo") ){
          handler.setModifiedImageAsOriginal();
        }
        else if( action.equalsIgnoreCase("delete") ){

          ImageEntity image = new ImageEntity( handler.getImageId() );
          image.delete();
          modinfo.getSession().removeAttribute("image_in_session");
          modinfo.getSession().removeAttribute("handler");
          handler=null;

        }
  }

  if( scaling!=null ){
    if(!scaling.equalsIgnoreCase("0")){//didn't push the button

      String height = modinfo.getRequest().getParameter("height");
      String width = modinfo.getRequest().getParameter("width");
      String constraint = modinfo.getRequest().getParameter("constraint");

      if( constraint!=null ) {

          handler.keepProportions(true);

          if( (height!=null) &&(height!="") && !(height.equalsIgnoreCase("")) ) {
                  if ( Integer.parseInt(height) != handler.getModifiedHeight() ){
                          handler.setModifiedHeight(Integer.parseInt(height));
                  }
                  else handler.setModifiedHeight(-1);
          }

          if( (width!=null) &&(width!="") && !(width.equalsIgnoreCase("")) ) {
                  if ( Integer.parseInt(width) != handler.getModifiedWidth() ){
                          handler.setModifiedWidth(Integer.parseInt(width));
                  }
                  else handler.setModifiedWidth(-1);
          }

       }
       else{

        if( (height!=null) &&(height!="") && !(height.equalsIgnoreCase("")) ) { handler.setModifiedHeight(Integer.parseInt(height)); }
        if( (width!=null) &&(width!="") && !(width.equalsIgnoreCase("")) ) { handler.setModifiedWidth(Integer.parseInt(width)); }

       }

        handler.resizeImage();
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
        //temp ImageCatagory.getStaticImageCatagoryInstance()
          ImageCatagory cat = new ImageCatagory();
          return EntityFinder.findAll(new ImageCatagory(),"Select * from " + cat.getEntityName() + " Where " + cat.getParentIdColumnName() + " = '" + parentID + "'");
        }else{
          return EntityFinder.findAll(new ImageCatagory());
        }
      }
      catch (Exception ex) {
        return null;
      }

    }

    public static List getAllImageCatagories(){
      return ImageBusiness.getImageCatagories(-1);
    }


    public static String getDatastoreType(GenericEntity entity){
      return DatastoreInterface.getDatastoreType(entity.getDatasource());
    }



    public static void storeEditForm(ModuleInfo modinfo){
      String catagoriTextInputName = "catagory";  // same as in ImageViewer getEditForm
      String deleteTextInputName = "delete";      // same as in ImageViewer getEditForm

      String[] catagoryName = modinfo.getParameterValues(catagoriTextInputName);
      String[] deleteValue = modinfo.getParameterValues(deleteTextInputName);
      ImageCatagory catagory = new ImageCatagory();

      //change
//      if(catagoryName != null && catagoryName.length > 0){
//        for (int i = 0; i < catagoryName.length; i++) {
//          String tempName = catagoryName[i];
//          catagory = new ImageCatagory(deleteValue[i]);
//        }
//
//      }

      //delete
      try {
        if(deleteValue != null){
          for(int i = 0; i < deleteValue.length; i++){
            catagory.deleteMultiple(catagory.getIDColumnName(), deleteValue[i]);
          }
        }
      }
      catch (Exception ex) {

      }

      try {
        for (int i = deleteValue.length; i < catagoryName.length; i++) {
          if (catagoryName[i] != null && !"".equals(catagoryName[i]) ) {
            ImageCatagory temp = new ImageCatagory();
            temp.setParentId(-1);
            temp.setImageCatagoryName(catagoryName[i]);
            temp.insert();
          }
        }
      }
      catch (Exception ex) {

      }





    }


}//end of class


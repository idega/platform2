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
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.oreilly.servlet.multipart.*;

/**
 * Title: ImageBusiness
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Eirikur Hrafnsson
 * @version 1.0
 *
 */


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


public static void handleEvent(ModuleInfo modinfo,ImageHandler handler) throws Exception{

  String action = modinfo.getParameter("action");
  String scaling = modinfo.getParameter("scale.x");
  String imageId2 = modinfo.getParameter("image_id");

  int imageId = (handler!=null)? handler.getOriginalImageId() : Integer.parseInt(imageId2);

  if ( action != null){
        if ( action.equalsIgnoreCase("Grayscale") ) handler.convertModifiedImageToGrayscale();
        else if ( action.equalsIgnoreCase("Emboss") ) handler.embossModifiedImage();
        else if ( action.equalsIgnoreCase("Invert") ) handler.invertModifiedImage();
        else if ( action.equalsIgnoreCase("Sharpen") ) handler.sharpenModifiedImage();
        else if( action.equalsIgnoreCase("Save") ){
          System.out.println("ImageBusiness: Saving");
          handler.writeModifiedImageToDatabase(true);
        }
        else if( action.equalsIgnoreCase("Savenew") ){
          System.out.println("ImageBusiness: Saving new image");
          handler.writeModifiedImageToDatabase(false);
        }
        else if( action.equalsIgnoreCase("Undo") ){
          handler.setModifiedImageAsOriginal();
        }
        else if( action.equalsIgnoreCase("delete") ){
          try{
            ImageEntity image = new ImageEntity( imageId );
            ImageEntity[] childs = (ImageEntity[]) image.findAllByColumn("parent_id",imageId);
            //brake childs from parent
            if( (childs!=null) && (childs.length>0)) {
              for (int i = 0; i < childs.length; i++) {
                childs[i].setParentId(image.getParentId());
                childs[i].update();
              }
            }

            image.removeFrom(GenericEntity.getStaticInstance("com.idega.jmodule.image.data.ImageCatagory"));

            image.delete();

            modinfo.removeSessionAttribute("image_in_session");
            modinfo.removeSessionAttribute("handler");
          }
          catch(Exception e){
            e.printStackTrace(System.err);
            System.out.println(e.getMessage());
          }
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
/*      String submButtonValue = modinfo.getParameter("catagory_edit_form");
      if(submButtonValue != null && "save".equalsIgnoreCase(submButtonValue)){
*/        String catagoriTextInputName = "catagory";  // same as in ImageViewer getEditForm
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

//}
    }


  public Form Upload(Connection Conn, ModuleInfo modinfo)throws IOException,SQLException{
    Form newImageForm = new Form();
   // MultipartRequest multi=null;

    try {

     // multi = new MultipartRequest(modinfo.getRequest(),Conn,".", 5 * 1024 * 1024);

      ImageCatagory[] imgCat = (ImageCatagory[]) (new ImageCatagory()).findAll();
      DropdownMenu category = new DropdownMenu("category");
      for (int i = 0 ; i < imgCat.length ; i++ ) {
        category.addMenuElement(imgCat[i].getID(),imgCat[i].getImageCatagoryName());
      }


      Table UploadDoneTable = new Table(2,3);
      UploadDoneTable.mergeCells(1,1,2,1);
      UploadDoneTable.mergeCells(1,2,2,2);
      UploadDoneTable.setBorder(0);
      newImageForm.add(UploadDoneTable);

      UploadDoneTable.add(category,2,3);

      UploadDoneTable.add(new Text("Hér er myndin eins og hún kemur út á vefnum. Veldu aftur ef eitthvað fór úrskeiðis"),1,1);
      UploadDoneTable.add(new Image(Integer.parseInt((String)modinfo.getSessionAttribute("image_id")) ),1,2);

      UploadDoneTable.add(new SubmitButton("submit","Ný mynd"),1,3);
      UploadDoneTable.add(new SubmitButton("submit","Vista"),1,3);
      /*	newImageForm.add(new SubmitButton("submit","Ný mynd"));
      newImageForm.add(new SubmitButton("submit","Vista"));
      newImageForm.setMethod("GET");
      *///	UploadDoneTable.add(newImageForm,1,3);
      //	add(UploadDoneTable);

  }
  catch (Exception e) {
    e.printStackTrace();
  }
  return newImageForm;
}

  private int SaveImage(ImageProperties ip){
    try{
      java.sql.Connection conn = com.idega.util.database.ConnectionBroker.getConnection();
      FileInputStream fin = new FileInputStream(ip.getRealPath() );
      int id = com.idega.io.ImageSave.saveImageToDB(conn,-1,fin,ip.getContenType(),ip.getName(),true);
      ip.setId(id);
      fin.close();
      com.idega.util.database.ConnectionBroker.freeConnection(conn);
      return id;
    }
    catch(Exception e){ip.setId(-1); return -1;}
  }



  private void doUpload(ModuleInfo modinfo){
    String sep = System.getProperty("file.separator");
    String realPath = modinfo.getServletContext().getRealPath(sep);
    String webPath = sep+"pics"+sep;
    String realFile = "";
    ImageProperties  ip = null;

    try {
    MultipartParser mp = new MultipartParser(modinfo.getRequest(), 10*1024*1024); // 10MB
    Part part;
    File dir = null;
    String value = null;
    while ((part = mp.readNextPart()) != null) {
      String name = part.getName();
      if(part.isParam()){
        ParamPart paramPart = (ParamPart) part;
        value = paramPart.getStringValue();
        //debug
        System.out.println(name+" : "+value+Text.getBreak());

      }
      else if (part.isFile()) {
        // it's a file part
        FilePart filePart = (FilePart) part;
        String fileName = filePart.getFileName();
        if (fileName != null) {
          webPath += fileName;
          realFile =  realPath+webPath;
          File file = new File(realFile);
          long size = filePart.writeTo(file);
          ip = new ImageProperties(fileName,filePart.getContentType(),realFile,webPath,size);
          modinfo.setSessionAttribute("im_ip",ip);
          //modinfo.getSession().removeAttribute("bm_ip");
        }
      }
    }
    boolean multi = true;
    modinfo.getSession().setAttribute("isMultiPart",new Boolean(multi));
  }
   catch (Exception s) {
    s.printStackTrace();
  }
/*
      int iImageId = -1;
      int id = -1;

      iImageId = SaveImage(ip);

      if(imageid != -1){
        this.addImage(new Image(imageid));
        try {
          ImageEntity im = new ImageEntity(imageid);
          ImageProperties ip = new ImageProperties(im.getImageName(),im.getContentType(),"","",0);
          ip.setId(imageid);
          modinfo.getSession().setAttribute("im_ip",ip);
        }
        catch (SQLException ex) {     }
      }
      else
      this.addSave(new SubmitButton("submit","Save"));
    }
    else{
      if(ip !=null){
        if(ip.getId() != -1)
          this.addImage(new Image(ip.getId()));
        else
         this.addImage(new Image(ip.getWebPath()));
        }
    }

  }
  catch (Exception s) {
    s.printStackTrace();
  }
  */
}

}//end of class


package is.idega.idegaweb.golf.block.image.business;

import is.idega.idegaweb.golf.block.image.data.ImageCatagory;
import is.idega.idegaweb.golf.block.image.data.ImageCatagoryBMPBean;
import is.idega.idegaweb.golf.block.image.data.ImageCatagoryHome;
import is.idega.idegaweb.golf.block.image.data.ImageEntity;
import is.idega.idegaweb.golf.block.image.data.ImageEntityHome;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.idega.data.DatastoreInterface;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

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

  public static int IM_BROWSER_WIDTH = 800;
  public static int IM_BROWSER_HEIGHT = 600;
  public static int IM_MAX_WIDTH = 140;

public static Properties getBundleProperties(IWContext modinfo) throws FileNotFoundException,IOException{
  //IWMainApplication application = getApplication();
  String fileSeperator = System.getProperty("file.separator");
  //FileInputStream fin = new FileInputStream(new File( application.getRealPath("/")+fileSeperator+"image"+fileSeperator+"properties"+fileSeperator+"bundle.properties" ));
  FileInputStream fin = new FileInputStream(new File( modinfo.getServletContext().getRealPath("/")+fileSeperator+"image"+fileSeperator+"properties"+fileSeperator+"image.properties" ));
  Properties prop = new Properties();
  prop.load(fin);
  fin.close();
  return prop;
}


public static void saveImageToCatagories(int imageId, String[] categoryId)throws SQLException {
  ImageEntity image = (ImageEntity)((ImageEntityHome)IDOLookup.getHomeLegacy(ImageEntity.class)).findByPrimaryKeyLegacy(imageId);
  image.setParentId(-1);//only top level images saved to categories
  image.update();

  for (int i = 0; i < categoryId.length; i++) {
    try{
      int category = Integer.parseInt(categoryId[i]);
      ImageCatagory cat = (ImageCatagory)((ImageCatagoryHome)IDOLookup.getHomeLegacy(ImageCatagory.class)).findByPrimaryKeyLegacy(category);
      cat.addTo(image);
    }
    catch(NumberFormatException e){
      System.err.println("ImageBusiness : categoryId is not a number");
    }
  }
}


public static void handleEvent(IWContext modinfo,ImageHandler handler) throws Exception{

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
          //System.out.println("ImageBusiness: Saving");
          handler.writeModifiedImageToDatabase(true);
        }
        else if( action.equalsIgnoreCase("Savenew") ){
          //System.out.println("ImageBusiness: Saving new image");
          handler.writeModifiedImageToDatabase(false);
        }
        else if( action.equalsIgnoreCase("Undo") || action.equalsIgnoreCase("Revert") ){
          handler.setModifiedImageAsOriginal();
        }
        else if( action.equalsIgnoreCase("delete") ){
          try{
            ImageEntity image = (ImageEntity)((ImageEntityHome)IDOLookup.getHomeLegacy(ImageEntity.class)).findByPrimaryKeyLegacy( imageId );
            ImageEntity[] childs = (ImageEntity[]) image.findAllByColumn("parent_id",imageId);
            ImageCatagory[] catagories = (ImageCatagory[]) image.findReverseRelated((IDOLegacyEntity)IDOLookup.instanciateEntity(ImageCatagory.class));

            //brake childs from parent
            if( (childs!=null) && (childs.length>0)) {
              for (int i = 0; i < childs.length; i++) {
                int parent = image.getParentId();
                childs[i].setParentId(parent);
                childs[i].update();
                if( (catagories!=null) && (catagories.length>0) ){
                  for (int k = 0; k < catagories.length; k++) {
                    catagories[k].addTo(childs[i]);
                  }
                }
              }
            }

            image.removeFrom((IDOLegacyEntity)IDOLookup.instanciateEntity(ImageCatagory.class));

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


public static void makeDefaultSizes(IWContext modinfo){
  try{
    Properties prop = getBundleProperties(modinfo);
    System.out.println(prop.getProperty("image1.width"));
    System.out.println(prop.getProperty("image2.width"));
    System.out.println(prop.getProperty("image3.width"));
  }
  catch(Exception ex){}
}



/*
        IWMainApplicationSettings list = getApplication().getSettings();
	//com.idega.idegaweb.IWPropertyList list = new com.idega.idegaweb.IWPropertyList("test.xml");
	list.setProperty("my","test");
	list.store();


*/


    public static List getImageCatagories(int parentID){
      try {
      	IDOLegacyEntity cat = (IDOLegacyEntity) IDOLookup.instanciateEntity(ImageCatagory.class);
        if (parentID > 0){
          return EntityFinder.findAll(cat,"Select * from " + IDOLookup.getEntityDefinitionForClass(ImageCatagory.class).getSQLTableName() + " Where " + ImageCatagoryBMPBean.getParentIdColumnName() + " = '" + parentID + "'");
        }else{
          return EntityFinder.findAll(cat);
        }
      }
      catch (Exception ex) {
        return null;
      }

    }

    public static List getAllImageCatagories(){
      return getImageCatagories(-1);
    }


    public static String getDatastoreType(IDOLegacyEntity entity){
      return DatastoreInterface.getDatastoreType(entity.getDatasource());
    }



    public static void storeEditForm(IWContext modinfo){
        String catagoriTextInputName = "catagory";  // same as in ImageViewer getEditForm
        String deleteTextInputName = "delete";      // same as in ImageViewer getEditForm
        String idees = "ids";      // same as in ImageViewer getEditForm

        String[] catagoryName = modinfo.getParameterValues(catagoriTextInputName);
        String[] deleteValue = modinfo.getParameterValues(deleteTextInputName);
        String[] ids = modinfo.getParameterValues(idees);

        ImageCatagory catagory = (ImageCatagory)((ImageCatagoryHome)IDOLookup.getHomeLegacy(ImageCatagory.class)).createLegacy();

        //change
  //      if(catagoryName != null && catagoryName.length > 0){
  //        for (int i = 0; i < catagoryName.length; i++) {
  //          String tempName = catagoryName[i];
  //          catagory = new ImageCatagory(deleteValue[i]);
  //        }
  //
  //      }

         //debug this is experimental code NOT failsafe!
        try {
          int k = ids.length;
          ImageCatagory temp;
          for (int i = 0; i < catagoryName.length; i++) {
            if (catagoryName[i] != null && !"".equals(catagoryName[i]) ) {
              String tempName = catagoryName[i];

              if( i >= k ){//insert
                temp = (ImageCatagory)((ImageCatagoryHome)IDOLookup.getHomeLegacy(ImageCatagory.class)).createLegacy();
                temp.setParentId(-1);
                temp.setImageCatagoryName(tempName);
                temp.insert();
              }
              else{//updates
                temp = (ImageCatagory)((ImageCatagoryHome)IDOLookup.getHomeLegacy(ImageCatagory.class)).findByPrimaryKeyLegacy(Integer.parseInt(ids[i]));
                if( !temp.getName().equalsIgnoreCase(tempName) ){
                   temp.setImageCatagoryName(tempName);
                   temp.update();
                }
              }

            }
          }
        }
        catch (Exception ex) {
          ex.printStackTrace(System.err);
          System.err.println("ImageBusiness : error in storeEditForm");
        }


        //delete
        try {
          if(deleteValue != null){
            for(int i = 0; i < deleteValue.length; i++){
              ImageCatagory cat = (ImageCatagory)((ImageCatagoryHome)IDOLookup.getHomeLegacy(ImageCatagory.class)).findByPrimaryKeyLegacy( Integer.parseInt(deleteValue[i]) );
              cat.removeFrom((IDOLegacyEntity)IDOLookup.instanciateEntity(ImageEntity.class));
              cat.delete();
            }
          }
        }
        catch (Exception ex) {
          ex.printStackTrace(System.err);
          System.err.println("ImageBusiness : error in storeEditForm");
        }



//}
    }

  public static int SaveImage(ImageProperties ip){
    int id = -1;
    //Connection Conn = null;

    try{
      FileInputStream input = new FileInputStream(ip.getRealPath());
			ImageEntity image = (ImageEntity)((ImageEntityHome)IDOLookup.getHomeLegacy(ImageEntity.class)).createLegacy();
			image.setImageValue(input);
			image.setName(ip.getName());
			image.setDate(IWTimestamp.getTimestampRightNow());
			image.setFromFile(false);
			image.setContentType(ip.getContentType());
			
			image.insert();  
			
			id = image.getID();
			
    }
		catch(Exception e){
			e.printStackTrace(System.err);
		}
		
		ip.setId(id);
		return id;
					 
  }

  public static ImageProperties doUpload(IWContext modinfo) throws Exception{
    String IW_BUNDLE_IDENTIFIER="com.idega.block.image";
    IWBundle iwb = IWBundle.getBundle(IW_BUNDLE_IDENTIFIER,modinfo.getIWMainApplication());
    String sep = System.getProperty("file.separator");
    String path = sep+"temp"+sep;
    String realPath = iwb.getResourcesRealPath()+path;
    String webPath = iwb.getResourcesVirtualPath()+path;
    //String realPath = modinfo.getServletContext().getRealPath(sep);
    //String webPath = sep+"pics"+sep;
    String realFile = "";
    ImageProperties  ip = null;

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
        //System.out.println(name+" : "+value+Text.getBreak());

      }
      else if (part.isFile()) {
        // it's a file part
        FilePart filePart = (FilePart) part;
        String fileName = filePart.getFileName();
        if (fileName != null) {
          webPath += fileName;
          webPath = webPath.replace('\\','/');
          realFile =  realPath+fileName;
          //File file = new File(realFile);
          System.err.println(realFile);
          File file =  com.idega.util.FileUtil.getFileAndCreateIfNotExists(realFile);

          long size = filePart.writeTo(file);
          ip = new ImageProperties(fileName,filePart.getContentType(),realFile,webPath,size);
        }
      }
    }

    return ip;
}

public static boolean deleteImageFile(String pathToImage){
    File file = new File(pathToImage);
    return file.delete();
}

public static void setImageDimensions(ImageProperties ip) {
  try{
    ImageHandler handler =  new ImageHandler(ip.getId());
    handler.updateOriginalInfo();
  }
  catch(Exception e){
   e.printStackTrace(System.err);
   System.err.println("ImageBusiness : setImageDimensions failed!");
  }

}

  public static void handleSaveImage(IWContext modinfo){
    ImageProperties ip = (ImageProperties) modinfo.getSessionAttribute("im_ip");
    String submit = modinfo.getParameter("submit");
    String categoryId = modinfo.getParameter("category_id");

    if( (ip!=null) && !("cancel".equalsIgnoreCase(submit)) ){
      int imageId = SaveImage(ip);
      ip.setId(imageId);
      setImageDimensions(ip);//adds width height and size in bytes to database

      makeDefaultSizes(modinfo);

      try{
        ImageEntity image = (ImageEntity)((ImageEntityHome)IDOLookup.getHomeLegacy(ImageEntity.class)).findByPrimaryKeyLegacy(imageId);
        ImageCatagory cat = (ImageCatagory)((ImageCatagoryHome)IDOLookup.getHomeLegacy(ImageCatagory.class)).findByPrimaryKeyLegacy(Integer.parseInt(categoryId));
        cat.addTo(image);
      }
      catch(SQLException e){
        e.printStackTrace(System.err);
        System.err.println("ImageBusiness : failed to add to image_image_catagory");
      }

      modinfo.setSessionAttribute("im_image_id",Integer.toString(imageId));
      deleteImageFile(ip.getRealPath());
      modinfo.removeSessionAttribute("im_ip");
      modinfo.setSessionAttribute("refresh",new String("true"));

    }
    else {
      System.err.println("Image save failed or was cancelled!");
    }
  }

  public static void handleTextSave(IWContext modinfo) throws Exception{
    String submit = modinfo.getParameter("submit");
    if( !"cancel".equalsIgnoreCase(submit) ){
      boolean update = true;
      String imageId = modinfo.getParameter("image_id");
      String imageText = modinfo.getParameter("image_text");
      String imageLink = modinfo.getParameter("image_link");
      ImageEntity image = (ImageEntity)((ImageEntityHome)IDOLookup.getHomeLegacy(ImageEntity.class)).findByPrimaryKeyLegacy(Integer.parseInt(imageId));

      if( imageText!=null ) image.setImageText(imageText);
      else update = false;

      if( imageLink!=null ){
        image.setImageLink(imageLink);
        image.setImageLinkOwner("both");
      }
      else update = false;

      if(update){
        image.update();
        modinfo.setSessionAttribute("im_refresh",new String("true"));
      }
    }
  }
}//end of class


package com.idega.jmodule.image.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import	com.idega.jmodule.object.*;
import	com.idega.jmodule.object.interfaceobject.*;
import	com.idega.jmodule.image.data.*;
import	com.idega.data.*;
import com.idega.util.text.*;


public class ImageViewer extends JModuleObject{

private int categoryId = -1;
private int numberOfImages=3;
private int numberOfDisplayedImages=1;
private int iNumberInRow = 3; //iXXXX for int
private int ifirst = 0;

private boolean backbutton = false;
private boolean limitNumberOfImage=false;
private Table outerTable = new Table(1,1);
private boolean isAdmin = false;


private Text textProxy = new Text();

private Image view;
private Image delete;
private Image editor;

private String language = "IS";

private int textSize = 1;

private String attributeName = "union_id";
//private int attributeId = -1;
private int attributeId = 3;


public ImageViewer(){
}

public ImageViewer(int categoryId){
  this.categoryId=categoryId;
}


public void setConnectionAttributes(String attributeName, int attributeId) {
  this.attributeName = attributeName;
  this.attributeId = attributeId;
}

public void setConnectionAttributes(String attributeName, String attributeId) {
  this.attributeName = attributeName;
  this.attributeId = Integer.parseInt(attributeId);
}


/*
**
** not needed for single user mode
**
*/
public String getColumnString(ImageCatagoryAttributes[] attribs){
  String values = "";
  for (int i = 0 ; i < attribs.length ; i++) {
    values += " image_catagory_id = '"+attribs[i].getImageCatagoryId()+"'" ;
    if( i!= (attribs.length-1) ) values += " OR ";
  }
return values;
}

/*
**
** old stuff..replace
**
*/
private void setSpokenLanguage(ModuleInfo modinfo){
 String language2 = modinfo.getRequest().getParameter("language");
    if (language2==null) language2 = ( String ) modinfo.getSession().getAttribute("language");
    if ( language2 != null) language = language2;
}

public void main(ModuleInfo modinfo)throws Exception{
  isAdmin= isAdministrator(modinfo);
  setSpokenLanguage(modinfo);
  ImageEntity[] image =  new ImageEntity[1];

  view = new Image("/pics/jmodules/image/"+language+"/view.gif");
  delete = new Image("/pics/jmodules/image/"+language+"/delete.gif");
  editor = new Image("/pics/jmodules/image/"+language+"/imageeditor.gif");

  String imageId = modinfo.getRequest().getParameter("image_id");
  String imageCategoryId = modinfo.getRequest().getParameter("image_catagory_id");

  if(isAdmin) {
    Form imageEditor = new Form("/image/imageadmin.jsp");
    imageEditor.add(new SubmitButton(editor));
    add(imageEditor);
  }

  if(imageId != null){
    try{
      image[0] = new ImageEntity(Integer.parseInt(imageId));
      add(displayImage(image[0]));
     }
    catch(NumberFormatException e) {
      add(new Text("ImageId must be a number"));
      System.err.println("ImageViewer: ImageId must be a number");
    }
  }
  else{

    try{
      if ( imageCategoryId != null )
        categoryId = Integer.parseInt(imageCategoryId);
      if( categoryId != -1 )
        add(displayCatagory(categoryId, modinfo));
    }
    catch(NumberFormatException e) {
      add(new Text("CategoryId must be a number"));
      System.err.println("ImageViewer: CategoryId must be a number");
    }
  }

}


public Table getImageTable(ImageEntity[] image)throws IOException,SQLException
{

	int imageId;

        int k = 0;
	if( !limitNumberOfImage ) k = image.length;
        else k = numberOfDisplayedImages;

	for ( int i = 0; i<k ; i++){
          outerTable.add(displayImage(image[i]), 1, 1);


	}


return outerTable;
}


public static Table displayImage(int imageId) throws SQLException {
    Table table = new Table();
    com.idega.jmodule.object.Image image = new com.idega.jmodule.object.Image(imageId);
    table.add(image);
return table;
}

private Table displayImage( ImageEntity image ) throws SQLException
{
  String text = image.getText();
  int imageId = image.getID();
  Table imageTable = new Table(1, 2);

  imageTable.setCellpadding(0);
  imageTable.add(new Image(imageId), 1, 1);

  if ( text!=null){
    Text imageText = new Text(text);
    getTextProxy().setFontSize(1);
   // getTextProxy().setFontColor("#FFFFFF");
    imageText = setTextAttributes( imageText );
    imageTable.add(imageText, 1, 2);
    imageTable.setColor(1,2,"#CCCCCC");
  }

  imageTable.setAlignment("center");
  imageTable.setAlignment(1,1,"center");
  imageTable.setAlignment(1,2,"center");

  imageTable.setVerticalAlignment("top");

  if(isAdmin) {
    Table editTable = new Table(5,1);
    Form imageEdit = new Form("/image/imageadmin.jsp?image_id="+imageId);
    imageEdit.add(new SubmitButton(view));

    Form imageEdit2 = new Form("/image/imageadmin.jsp?image_id="+imageId+"&action=delete");
    imageEdit2.add(new SubmitButton(delete));

    Form imageEdit3 = new Form("/image/imageadmin.jsp?image_id="+imageId+"&action=delete");
  //  imageEdit3.add(new SubmitButton(use));

    Form imageEdit4 = new Form("/image/imageadmin.jsp?image_id="+imageId+"&action=delete");
  //  imageEdit4.add(new SubmitButton(copy));

    Form imageEdit5 = new Form("/image/imageadmin.jsp?image_id="+imageId+"&action=delete");
  //  imageEdit5.add(new SubmitButton(cut));


    editTable.add(imageEdit,1,1);
    editTable.add(imageEdit2,2,1);
    editTable.add(imageEdit3,1,1);
    editTable.add(imageEdit4,2,1);
    editTable.add(imageEdit5,1,1);

    imageTable.add(editTable, 1, 2);
  }

return imageTable;
}

private Table displayCatagory(int categoryId, ModuleInfo modinfo)  throws SQLException {
  int k = 0;
  String sFirst = modinfo.getParameter("iv_first");//browsing from this image
  Table table = new Table();

  ImageCatagory category = new ImageCatagory(categoryId);
  ImageEntity[] imageEntity = (ImageEntity[]) category.findRelated(new ImageEntity());
  com.idega.jmodule.object.Image image;

  if( limitNumberOfImage ) k = numberOfDisplayedImages;
  else k = imageEntity.length;

  try {
    if (sFirst!=null) ifirst = Integer.parseInt(sFirst);
    if (ifirst < 0 ) {
      ifirst = (-1)*ifirst;
    }
    else if (ifirst > (imageEntity.length -1)) {
        ifirst = (imageEntity.length -1);
    }
  }
  catch (NumberFormatException n) {
    add(new Text("ImageViewer: sFirst must be a number"));
    System.err.println("ImageViewer: sFirst must be a number");
  }

  int x=0;
  for (int i = ifirst ; (x<k) && ( i < imageEntity.length ) ; i++ ) {
    table.setVerticalAlignment((x%iNumberInRow)+1,(x/iNumberInRow)+1,"top");
    table.setAlignment((x%iNumberInRow)+1,(x/iNumberInRow)+1,"center");
    table.add( displayImage(imageEntity[i]) ,(x%iNumberInRow)+1,(x/iNumberInRow)+1);
    x++;
  }


return table;
}

/**
** return a proxy for the main text. Use the standard
** set methods on this object such as .setFontSize(1) etc.
** and it will set the property for all texts.
*/
public Text getTextProxy(){

return textProxy;
}

public void setTextProxy(Text textProxy){
	this.textProxy = textProxy;
}

public Text setTextAttributes( Text realText ){
  Text tempText = (Text) textProxy.clone();
  tempText.setText( realText.getText() );
return tempText;
}


public void setNumberOfDisplayedImages(int numberOfDisplayedImages){
  this.limitNumberOfImage = true;
  if( numberOfDisplayedImages<0 ) numberOfDisplayedImages = (-1)*numberOfDisplayedImages;
  this.numberOfDisplayedImages = numberOfDisplayedImages;
}

public void setNumberInRow(int NumberOfImagesInOneRow){
  this.iNumberInRow = iNumberInRow;
}


public void setTableWidth(int width){
  setTableWidth(Integer.toString(width));
}

public void setTableWidth(String width){
  this.outerTable.setWidth(width);
}

public void setViewImage(String image_name){
  view = new Image(image_name);
}

public void setDeleteImage(String image_name){
  delete = new Image(image_name);
}

public void setEditorImage(String image_name){
  editor = new Image(image_name);
}

}

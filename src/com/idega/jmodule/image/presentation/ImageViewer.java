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
private boolean limitNumberOfImages=true;
private int numberOfDisplayedImages=9;
private int iNumberInRow = 3; //iXXXX for int
private int ifirst = 0;
private int maxImageWidth =100;
private boolean limitImageWidth=true;

private boolean backbutton = false;
private Table outerTable = new Table(1,3);
private boolean isAdmin = false;
private String outerTableWidth = "100%";
private String outerTableHeight = "100%";
private String headerFooterColor = "#8AA7D6";
private String textColor = "#FFFFFF";
private String headerText = "";
private Image footerBackgroundImage;
private Image headerBackgroundImage;
private ImageEntity[] entities;


private Text textProxy = new Text();

private Image view;
private Image delete;
private Image editor;
private Image use;
private Image copy;
private Image cut;

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

public ImageViewer(ImageEntity[] entities){
  this.entities=entities;
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


  view = new Image("/pics/jmodules/image/"+language+"/view.gif","View all sizes");
  delete = new Image("/pics/jmodules/image/"+language+"/delete.gif","Delete this image");
  editor = new Image("/pics/jmodules/image/"+language+"/imageeditor.gif");
  use = new Image("/pics/jmodules/image/"+language+"/use.gif","Use this image");
  copy = new Image("/pics/jmodules/image/"+language+"/copy.gif","Copy this image");
  cut = new Image("/pics/jmodules/image/"+language+"/cut.gif","Cut this image");

  String imageId = modinfo.getRequest().getParameter("image_id");
  String imageCategoryId = modinfo.getRequest().getParameter("image_catagory_id");

  outerTable.setColor(1,1,headerFooterColor);
  outerTable.setColor(1,3,headerFooterColor);
  outerTable.setAlignment(1,1,"left");
  outerTable.setAlignment(1,2,"center");
  outerTable.setAlignment(1,3,"center");
  outerTable.setWidth(outerTableWidth);
  outerTable.setHeight(outerTableHeight);
  outerTable.setHeight(1,1,"18");
  outerTable.setHeight(1,3,"18");
  outerTable.setCellpadding(2);
  outerTable.setCellspacing(0);
  outerTable.setBorder(1);
  if ( headerBackgroundImage != null ) outerTable.setBackgroundImage(1,1,headerBackgroundImage);
  if ( footerBackgroundImage != null ) outerTable.setBackgroundImage(1,3,footerBackgroundImage);

  if(isAdmin) {
    Link imageEditor = new Link(editor,"/image/imageadmin.jsp");
    outerTable.add(imageEditor,1,1);
  }

  if(imageId != null){
    try{
      limitImageWidth = false;
      image[0] = new ImageEntity(Integer.parseInt(imageId));
      Text imageName = new Text(image[0].getName());
      imageName.setBold();
      imageName.setFontColor(textColor);
      imageName.setFontSize(3);
      outerTable.add(imageName,1,1);

      outerTable.add(displayImage(image[0]),1,2);
      add(outerTable);
     }
    catch(NumberFormatException e) {
      add(new Text("ImageId must be a number"));
      System.err.println("ImageViewer: ImageId must be a number");
    }
  }
  else{

    try{
      if ( (imageCategoryId != null) || (entities!=null) ){
        ImageEntity[] imageEntity;
        String sFirst = modinfo.getParameter("iv_first");//browsing from this image
        if (sFirst!=null) ifirst = Integer.parseInt(sFirst);

        if ( imageCategoryId != null){

          categoryId = Integer.parseInt(imageCategoryId);
          ImageCatagory category = new ImageCatagory(categoryId);
          imageEntity = (ImageEntity[]) category.findRelated(new ImageEntity());
          Text categoryName = new Text(category.getName());
          categoryName.setBold();
          categoryName.setFontColor(textColor);
          categoryName.setFontSize(3);
          outerTable.add(categoryName,1,1);

          if( limitNumberOfImages ) {
            String middle = (ifirst+1)+" til "+(ifirst+numberOfDisplayedImages)+" af "+(imageEntity.length-1);
            Text middleText = new Text(middle);
            middleText.setBold();
            middleText.setFontColor(textColor);

            Text leftText = new Text("Fyrri myndir <<");
            leftText.setBold();

            Link back = new Link(leftText);
            back.setFontColor(textColor);
            ifirst = ifirst-numberOfDisplayedImages;
            if( ifirst<0 ) ifirst = 0;
            back.addParameter("iv_first",ifirst);
            back.addParameter("image_catagory_id",category.getID());

            Text rightText = new Text(">> Næstu myndir");
            rightText.setBold();

            Link forward = new Link(rightText);
            forward.setFontColor(textColor);

            int inext = ifirst+numberOfDisplayedImages;
            if( inext > imageEntity.length) inext = imageEntity.length-numberOfDisplayedImages;
            forward.addParameter("iv_first",inext);
            forward.addParameter("image_catagory_id",category.getID());

            Table links = new Table(3,1);

            links.setWidth("100%");
            links.setCellpadding(0);
            links.setCellspacing(0);
            links.setAlignment(1,1,"left");
            links.setAlignment(2,1,"center");
            links.setAlignment(3,1,"right");

            links.add(back,1,1);
            links.add(middleText,2,1);
            links.add(forward,3,1);
            outerTable.add(links,1,3);

          }
        }
        else{
        //for searches

          Text header = new Text(headerText);
          header.setBold();
          header.setFontColor(textColor);
          header.setFontSize(3);
          outerTable.add(header,1,1);
          imageEntity=entities;
        }

        outerTable.add(displayCatagory(imageEntity),1,2);
        add(outerTable);

      }
    }
    catch(NumberFormatException e) {
      add(new Text("CategoryId must be a number"));
      System.err.println("ImageViewer: CategoryId must be a number");
    }
  }

}

public static Table getImageInTable(int imageId) throws SQLException {
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

  imageTable.setAlignment("center");
  imageTable.setAlignment(1,1,"center");
  imageTable.setAlignment(1,2,"center");
  imageTable.setVerticalAlignment("top");
  imageTable.setCellpadding(0);

  Image theImage = new Image(imageId);
  if( limitImageWidth ) theImage.setWidth(maxImageWidth);
  Link bigger = new Link(theImage);
  bigger.addParameter("image_id",imageId);

  imageTable.add(bigger, 1, 1);

  if ( text!=null){
    Text imageText = new Text(text);
    getTextProxy().setFontSize(1);
   // getTextProxy().setFontColor("#FFFFFF");
    imageText = setTextAttributes( imageText );
    imageTable.add(imageText, 1, 2);
    imageTable.setColor(1,2,"#CCCCCC");
  }

  if(isAdmin) {
    String adminPage="/image/imageadmin.jsp";//change to same page and object
    Table editTable = new Table(5,1);
    Link imageEdit = new Link(delete,adminPage);
    imageEdit.addParameter("image_id",imageId);
    imageEdit.addParameter("action","delete");
    Link imageEdit2 = new Link(cut,adminPage);
    imageEdit2.addParameter("image_id",imageId);
    imageEdit2.addParameter("action","cut");
    Link imageEdit3 = new Link(copy,adminPage);
    imageEdit3.addParameter("image_id",imageId);
    imageEdit3.addParameter("action","copy");
    Link imageEdit4 = new Link(view,adminPage);
    imageEdit4.addParameter("image_id",imageId);
    Link imageEdit5 = new Link(use,adminPage);
    imageEdit5.addParameter("image_id",imageId);
    imageEdit5.addParameter("action","use");


    editTable.add(imageEdit,1,1);
    editTable.add(imageEdit2,2,1);
    editTable.add(imageEdit3,3,1);
    editTable.add(imageEdit4,4,1);
    editTable.add(imageEdit5,5,1);

    imageTable.add(editTable, 1, 2);
  }

return imageTable;
}

private Table displayCatagory( ImageEntity[] imageEntity )  throws SQLException {
  int k = 0;
  Image image;

  if( limitNumberOfImages ) k = numberOfDisplayedImages;
  else k = imageEntity.length;

  int heigth = k/iNumberInRow;
  if( k%iNumberInRow!=0 ) heigth++;
  Table table = new Table(iNumberInRow,heigth);
  table.setWidth("100%");

  try {
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
    table.setWidth((x%iNumberInRow)+1,Integer.toString((int)(100/iNumberInRow))+"%");
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
  this.limitNumberOfImages = true;
  if( numberOfDisplayedImages<0 ) numberOfDisplayedImages = (-1)*numberOfDisplayedImages;
  this.numberOfDisplayedImages = numberOfDisplayedImages;
}

public void setNumberInRow(int NumberOfImagesInOneRow){
  this.iNumberInRow = NumberOfImagesInOneRow;
}

public void setHeaderText(String headerText){
  this.headerText = headerText;
}

public void setHeaderFooterColor(String headerFooterColor){
  this.headerFooterColor=headerFooterColor;
}

public void setHeaderBackgroundImage(Image headerBackgroundImage){
  this.headerBackgroundImage=headerBackgroundImage;
}

public void setHeaderBackgroundImage(String headerBackgroundImageURL){
  setHeaderBackgroundImage(new Image(headerBackgroundImageURL));
}

public void setFooterBackgroundImage(Image footerBackgroundImage){
  this.footerBackgroundImage=footerBackgroundImage;
}

public void setFooterBackgroundImage(String footerBackgroundImageURL){
  setFooterBackgroundImage(new Image(footerBackgroundImageURL));
}

public void setTextColor(String textColor){
  this.textColor=textColor;
}

public void setMaxImageWidth(int maxImageWidth){
  this.limitImageWidth=true;
  this.maxImageWidth = maxImageWidth;
}

public void limitImageWidth( boolean limitImageWidth ){
  this.limitImageWidth=true;
}

public void setTableWidth(int width){
  setTableWidth(Integer.toString(width));
}

public void setTableWidth(String width){
  this.outerTableWidth=width;
}

public void setTableHeight(int height){
  setTableHeight(Integer.toString(height));
}

public void setTableHeight(String height){
  this.outerTableHeight=height;
}
public void setViewImage(String imageName){
  view = new Image(imageName);
}

public void setDeleteImage(String imageName){
  delete = new Image(imageName);
}

public void setEditorImage(String imageName){
  editor = new Image(imageName);
}

public void setUseImage(String imageName){
  use = new Image(imageName);
}

public void setCopyImage(String imageName){
  copy = new Image(imageName);
}

public void setCutImage(String imageName){
  cut = new Image(imageName);
}

}

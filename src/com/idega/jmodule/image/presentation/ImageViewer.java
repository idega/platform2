package com.idega.jmodule.image.presentation;

/**
 * Title: ImageViewer
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
import	com.idega.jmodule.object.*;
import	com.idega.jmodule.object.interfaceobject.*;
import	com.idega.jmodule.image.data.*;
import	com.idega.jmodule.image.business.*;
import	com.idega.data.*;
import com.idega.util.text.*;
import com.idega.jmodule.image.business.*;
import com.oreilly.servlet.MultipartRequest;


public class ImageViewer extends JModuleObject{

private int categoryId = -1;
private boolean limitNumberOfImages=true;
private int numberOfDisplayedImages=9;
private int iNumberInRow = 3; //iXXXX for int
private int ifirst = 0;
private int maxImageWidth =100;
private boolean limitImageWidth=true;
private String callingModule = "";

private boolean backbutton = false;
private boolean refresh = false;
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
private Image use;
private Image copy;
private Image cut;
private Image edit;
private Image save;
private Image cancel;
private Image newImage;
private Image newCategory;

private String language = "IS";

private int textSize = 1;

private String attributeName = "union_id";
//private int attributeId = -1;
private int attributeId = 3;

//private ImageBusiness business;


public ImageViewer(){
//  business = new ImageBusiness();
}

public ImageViewer(int categoryId){
  this();
  this.categoryId=categoryId;
}

public ImageViewer(ImageEntity[] entities){
  this();
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

  /*  ImageBusiness.storeEditForm(modinfo);

  outerTable.add(getEditForm(),1,2);

*/
  if( refresh ){
    modinfo.getSession().removeAttribute("image_previous_catagory_id");
    modinfo.getSession().removeAttribute("image_entities");
  }

  view = new Image("/pics/jmodules/image/"+language+"/view.gif","View all sizes");
  delete = new Image("/pics/jmodules/image/"+language+"/delete.gif","Delete this image");
  use = new Image("/pics/jmodules/image/"+language+"/use.gif","Use this image");
  copy = new Image("/pics/jmodules/image/"+language+"/copy.gif","Copy this image");
  cut = new Image("/pics/jmodules/image/"+language+"/cut.gif","Cut this image");
  edit = new Image("/pics/jmodules/image/"+language+"/edit.gif","Edit this image");

  save = new Image("/pics/jmodules/image/"+language+"/save.gif","Edit this image");
  cancel = new Image("/pics/jmodules/image/"+language+"/cancel.gif","Edit this image");
  newImage = new Image("/pics/jmodules/image/"+language+"/newimage.gif","Edit this image");
  newCategory = new Image("/pics/jmodules/image/"+language+"/newcategory.gif","Edit this image");

  String imageId = modinfo.getRequest().getParameter("image_id");
  String imageCategoryId = modinfo.getRequest().getParameter("image_catagory_id");

  outerTable.setColor(1,1,headerFooterColor);
  outerTable.setColor(1,3,headerFooterColor);
  outerTable.setAlignment(1,1,"left");
  outerTable.setAlignment(1,2,"center");
  outerTable.setAlignment(1,3,"center");
  outerTable.setWidth(outerTableWidth);
  outerTable.setHeight(outerTableHeight);
  outerTable.setHeight(1,1,"23");
  outerTable.setHeight(1,3,"23");
  outerTable.setCellpadding(2);
  outerTable.setCellspacing(0);

  Table links = new Table(3,1);
  links.setWidth("100%");
  links.setCellpadding(0);
  links.setCellspacing(0);
  links.setAlignment(1,1,"left");
  links.setAlignment(2,1,"center");
  links.setAlignment(3,1,"right");

  if ( headerBackgroundImage != null ) outerTable.setBackgroundImage(1,1,headerBackgroundImage);
  if ( footerBackgroundImage != null ) outerTable.setBackgroundImage(1,3,footerBackgroundImage);



  String edit = modinfo.getRequest().getParameter("edit");

  if(edit!=null){
    try{
      getEditor(modinfo);
    }
    catch(Throwable e){
    e.printStackTrace(System.err);


    }
  }
  else{
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
        Text backtext = new Text("Bakka <<");
        backtext.setBold();
        Link backLink = new Link(backtext);
        backLink.setFontColor(textColor);
        backLink.setAsBackLink();
        links.add(backLink,1,1);
        outerTable.add(links,1,3);
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

          String previousCatagory =  (String)modinfo.getSession().getAttribute("image_previous_catagory_id");

          if ( imageCategoryId != null){

            if( (previousCatagory!=null) && (!previousCatagory.equalsIgnoreCase(imageCategoryId)) ){
              modinfo.getSession().removeAttribute("image_previous_catagory_id");
            }

          ImageEntity[] inApplication = (ImageEntity[]) modinfo.getServletContext().getAttribute("image_entities_"+imageCategoryId);
          modinfo.getSession().setAttribute("image_previous_catagory_id",imageCategoryId);

            categoryId = Integer.parseInt(imageCategoryId);
            ImageCatagory category = new ImageCatagory(categoryId);
            if ( inApplication == null ){
              imageEntity = (ImageEntity[]) category.findRelated(new ImageEntity());
            }
            else imageEntity = inApplication;

            if( imageEntity!=null ){
              modinfo.getServletContext().removeAttribute("image_entities_"+imageCategoryId);
              modinfo.getServletContext().setAttribute("image_entities_"+imageCategoryId,imageEntity);
            }

            Text categoryName = new Text(category.getName());
            categoryName.setBold();
            categoryName.setFontColor(textColor);
            categoryName.setFontSize(3);
            outerTable.add(categoryName,1,1);

            if( limitNumberOfImages ) {


              Text leftText = new Text("Fyrri myndir <<");
              leftText.setBold();

              Link back = new Link(leftText);
              back.setFontColor(textColor);
              int iback = ifirst-numberOfDisplayedImages;
              if( iback<0 ) ifirst = 0;
              back.addParameter("iv_first",iback);
              back.addParameter("image_catagory_id",category.getID());

              String middle = (ifirst+1)+" til "+(ifirst+numberOfDisplayedImages)+" af "+(imageEntity.length-1);
              Text middleText = new Text(middle);
              middleText.setBold();
              middleText.setFontColor(textColor);


              Text rightText = new Text(">> Næstu myndir");
              rightText.setBold();

              Link forward = new Link(rightText);
              forward.setFontColor(textColor);

              int inext = ifirst+numberOfDisplayedImages;
              if( inext > (imageEntity.length-1)) inext = (imageEntity.length-1)-numberOfDisplayedImages;

              forward.addParameter("iv_first",inext);
              forward.addParameter("image_catagory_id",category.getID());

              links.add(back,1,1);
              links.add(middleText,2,1);
              links.add(forward,3,1);


            }
          }
          else{
          //for searches

            Text header = new Text(headerText);
            header.setBold();
            header.setFontColor(textColor);
            header.setFontSize(3);
            outerTable.add(header,1,1);
            limitNumberOfImages=false;
            imageEntity=entities;


          }
          outerTable.add(links,1,3);
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
    //String adminPage="/image/imageadmin.jsp";//change to same page and object
    Table editTable = new Table(5,1);
    Link imageEdit = new Link(delete);
    imageEdit.addParameter("image_id",imageId);
    imageEdit.addParameter("action","delete");
    Link imageEdit2 = new Link(cut);
    imageEdit2.addParameter("image_id",imageId);
    imageEdit2.addParameter("action","cut");
    Link imageEdit3 = new Link(copy);
    imageEdit3.addParameter("image_id",imageId);
    imageEdit3.addParameter("action","copy");
    Link imageEdit4 = new Link(view);
    imageEdit4.addParameter("image_id",imageId);
    Link imageEdit5 = new Link(use);
    imageEdit5.addParameter("image_id",imageId);
    imageEdit5.addParameter("action","use");
    Link imageEdit6 = new Link(edit);
    imageEdit6.addParameter("image_id",imageId);
    imageEdit6.addParameter("edit","true");




    editTable.add(imageEdit,1,1);
   // debug eiki add later
   //editTable.add(imageEdit2,2,1);
   // editTable.add(imageEdit3,3,1);
    editTable.add(imageEdit4,4,1);
    editTable.add(imageEdit5,5,1);
    editTable.add(imageEdit6,2,1);

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



public void setUseImage(String imageName){
  use = new Image(imageName);
}

public void setCopyImage(String imageName){
  copy = new Image(imageName);
}

public void setCutImage(String imageName){
  cut = new Image(imageName);
}

public void refresh(boolean refresh){
  this.refresh = refresh;
}


public void setCallingModule(String callingModule){
  this.callingModule = callingModule;
}


public Form getEditorForm(ImageHandler handler, String ImageId, ModuleInfo modinfo) throws Throwable{

  Table toolbarBelow = new Table(4,2);
  Table toolbarRight = new Table(2,5);
  Table imageTable = new Table(2,2);
  Form form = new Form();
  form.setMethod("GET");

  Link delete = new Link(new Image("/pics/jmodules/image/buttons/delete.gif","Delete the image"));
  delete.addParameter("action","delete");
  toolbarBelow.add(delete,1,1);

  Link gray = new Link(new Image("/pics/jmodules/image/buttons/grayscale.gif","Convert the image to grayscale"));
  gray.addParameter("action","Grayscale");
  toolbarBelow.add(gray,1,1);
  Link emboss = new Link(new Image("/pics/jmodules/image/buttons/emboss.gif","Emboss the image"));
  emboss.addParameter("action","emboss");
  toolbarBelow.add(emboss,1,1);
  Link sharpen = new Link(new Image("/pics/jmodules/image/buttons/sharpen.gif","Sharpen the image"));
  sharpen.addParameter("action","sharpen");
  toolbarBelow.add(sharpen,1,1);
  Link invert = new Link(new Image("/pics/jmodules/image/buttons/invert.gif","Invert the image"));
  invert.addParameter("action","Invert");
  toolbarBelow.add(invert,1,1);

  Text widthtext = new Text("Width:<br>");
  widthtext.setFontSize(1);
  toolbarBelow.add(widthtext,1,2);
  TextInput widthInput = new TextInput("width",""+handler.getModifiedWidth());
  widthInput.setSize(5);
  toolbarBelow.add(widthInput,1,2);
  Text heighttext = new Text("Height:<br>");
  heighttext.setFontSize(1);
  toolbarBelow.add(heighttext,2,2);
  TextInput heightInput = new TextInput("height",""+handler.getModifiedHeight());
  heightInput.setSize(5);
  toolbarBelow.add(heightInput,2,2);
  Text con = new Text("<br>Constrain?");
  con.setFontSize(1);
  toolbarBelow.add(con,3,2);
  CheckBox constrained = new CheckBox("constraint","true");
  constrained.setChecked(true);
  toolbarBelow.add(constrained,3,2);
  toolbarBelow.add(new SubmitButton(new Image("/pics/jmodules/image/buttons/scale.gif"),"scale","true"),4,2);

  Link undo = new Link(new Image("/pics/jmodules/image/buttons/undo.gif","Undo the last changes"));
  undo.addParameter("action","undo");
  toolbarBelow.add(undo,3,1);
  Link save = new Link(new Image("/pics/jmodules/image/buttons/save.gif","Save the Image"));
  save.addParameter("action","save");
  toolbarBelow.add(save,4,1);

/*
  Link brightness = new Link(new Image("/pics/jmodules/image/buttons/brightness.gif","Adjust the brightness of the image"));
  brightness.addParameter("action","brightness");
  toolbarRight.add(brightness,1,1);

  Link contrast = new Link(new Image("/pics/jmodules/image/buttons/contrast.gif","Adjust the contrast of the image"));
  contrast.addParameter("action","contrast");
  toolbarRight.add(contrast,1,2);

  Link color = new Link(new Image("/pics/jmodules/image/buttons/color.gif","Adjust the color of the image"));
  color.addParameter("action","color");
  toolbarRight.add(color,1,3);

  Link quality = new Link(new Image("/pics/jmodules/image/buttons/quality.gif","Adjust the quality of the image"));
  quality.addParameter("action","quality");//quality( + _low/_med/_high/_max)
  toolbarRight.add(quality,1,4);

  Link revert = new Link(new Image("/pics/jmodules/image/buttons/revert.gif","Revert to the last saved version of the image"));
  revert.addParameter("action","revert");
  toolbarRight.add(revert,2,1);

  Link rotate = new Link(new Image("/pics/jmodules/image/buttons/rotate.gif","Rotate the image CCW/CW"));
  rotate.addParameter("action","rotate");
  toolbarRight.add(rotate,2,2);

  Link horizontal = new Link(new Image("/pics/jmodules/image/buttons/horizontal.gif","Flip the image horizontaly"));
  horizontal.addParameter("action","horizontal");
  toolbarRight.add(horizontal,2,3);

  Link vertical = new Link(new Image("/pics/jmodules/image/buttons/vertical.gif","Flip the image verticaly"));
  vertical.addParameter("action","vertical");
  toolbarRight.add(vertical,2,4);


  imageTable.add(toolbarRight,2,1);*/
  //ShadowBox below = new ShadowBox();
 // below.add(toolbarBelow);
  imageTable.add(toolbarBelow,1,2);
  imageTable.mergeCells(1,2,2,2);

  //ShadowBox box = new ShadowBox();
  //box.add(new Text("<nobr>"));
  if( handler != null) {

    Image myndin = handler.getModifiedImageAsImageObject(modinfo);
    int percent = 100;
    String percent2  = modinfo.getParameter("percent");
    if (percent2!=null) percent = Integer.parseInt(percent2);

    myndin.setWidth( (myndin.getWidth()* percent)/100  );
    myndin.setHeight( (myndin.getHeight()* percent)/100 );

    imageTable.add( myndin ,1,1);

    Text percentText = new Text("<br>Percent:<br>");
    percentText.setFontSize(1);
    imageTable.add(percentText,1,1);
    TextInput percentInput = new TextInput("percent",""+percent);
    percentInput.setSize(4);
    imageTable.add(percentInput,1,1);



  }

  toolbarBelow.mergeCells(1,1,2,1);

  toolbarBelow.setAlignment(1,1,"center");
  toolbarBelow.setAlignment(1,2,"left");
  toolbarBelow.setAlignment(2,2,"left");
  toolbarBelow.setAlignment(3,2,"left");
  toolbarBelow.setAlignment(4,2,"left");
  toolbarBelow.setAlignment(3,1,"right");
  toolbarBelow.setAlignment(4,1,"right");
  imageTable.setVerticalAlignment(2,1,"bottom");

  ShadowBox box2 = new ShadowBox();
  box2.add(imageTable);
  form.add(box2);

  return form;
  }

private Form getEditForm(){
  Form frameForm = new Form();
  Table frameTable = new Table(1,2);
  frameTable.setCellpadding(0);
  frameTable.setCellspacing(0);
  frameForm.add(frameTable);


  List catagories = ImageBusiness.getAllImageCatagories();
  int catagorieslength = (catagories != null) ? catagories.size() : 0;

  Table contentTable = new Table(3,catagorieslength+2);
  contentTable.setCellpadding(0);
  contentTable.setCellspacing(0);

  int textInputLenth = 20;
  String catagoriTextInputName = "catagory";
  String deleteTextInputName = "delete";

  for (int i = 0; i < catagorieslength; i++) {
    TextInput catagoryInput = new TextInput(catagoriTextInputName,((ImageCatagory)catagories.get(i)).getImageCatagoryName());
    catagoryInput.setLength(textInputLenth);
    contentTable.add(catagoryInput,1,i+2);
    contentTable.setHeight(i+1,"30");
    contentTable.add(new CheckBox(deleteTextInputName, Integer.toString(((ImageCatagory)catagories.get(i)).getID())),3,i+2);
  }

  Text catagoryText = new Text("Flokkur");
  catagoryText.setBold();
  catagoryText.setFontColor("#FFFFFF");

  Text deleteText = new Text("Eyða");
  deleteText.setBold();
  deleteText.setFontColor("#FFFFFF");

  contentTable.add(catagoryText,1,1);
  contentTable.add(deleteText,3,1);

  TextInput catagoryInput = new TextInput(catagoriTextInputName);
  catagoryInput.setLength(textInputLenth);
  contentTable.add(catagoryInput,1,catagorieslength+2);

  frameTable.add(contentTable,1,1);

  //Buttons
  Table buttonTable = new Table(3,1);
  buttonTable.setHeight(40);
  buttonTable.setCellpadding(0);
  buttonTable.setCellspacing(0);
  SubmitButton save = new SubmitButton("Vista","catagory_edit_form","save");
  buttonTable.add(save,3,1);
  buttonTable.setWidth(3,1,"60");
  SubmitButton cancel = new SubmitButton("Hætta við","catagory_edit_form", "cancel");
  buttonTable.add(cancel,2,1);
  buttonTable.setWidth(2,1,"60");
  frameTable.add(buttonTable,1,2);
  frameTable.setAlignment(1,2,"right");
  //Buttons ends

  return frameForm;

}


public void Upload(Connection Conn, ModuleInfo modinfo)throws IOException,SQLException{

  Form newImageForm = new Form();
  newImageForm.setMethod("GET");

  MultipartRequest multi=null;

  try {

    multi = new MultipartRequest(modinfo.getRequest(),Conn,".", 5 * 1024 * 1024);

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
  UploadDoneTable.add(new Image(Integer.parseInt((String)modinfo.getSession().getAttribute("image_id")) ),1,2);

  UploadDoneTable.add(new SubmitButton("submit","Ný mynd"),1,3);
  UploadDoneTable.add(new SubmitButton("submit","Vista"),1,3);
  /*	newImageForm.add(new SubmitButton("submit","Ný mynd"));
  newImageForm.add(new SubmitButton("submit","Vista"));
  newImageForm.setMethod("GET");
  *///	UploadDoneTable.add(newImageForm,1,3);
  //	add(UploadDoneTable);
  add(newImageForm);


  }
  catch (Exception e) {
    e.printStackTrace();
  }

}

public void drawUploadTable(String image_id,boolean replace){
	Form MultipartForm = new Form();
	MultipartForm.setMultiPart();
	//MultipartForm.add(new HiddenInput("statement","insert into images (image_name,image_value,content_type,from_file,image_category_id) values('gunnar', ? ,?,'N',4)"));
	if (replace) {
		MultipartForm.add(new HiddenInput("statement","update image set image_value=?,content_type=?,image_name=? where image_id="+image_id+""));
	}
	else {

		idegaTimestamp dags = new idegaTimestamp();

	   //  MultipartForm.add(new HiddenInput("statement","insert into image (image_id,image_value,content_type,image_name,from_file) values("+image_id+",?,?,?,'N')"));
            if( !ImageBusiness.getDatastoreType(new ImageEntity()).equals("oracle") )
                MultipartForm.add(new HiddenInput("statement","insert into image (image_id,image_value,content_type,image_name,date_added,from_file) values("+image_id+",?,?,?,'"+dags.getTimestampRightNow().toString()+"','N')"));
	        else  MultipartForm.add(new HiddenInput("statement","insert into image (image_id,image_value,content_type,image_name,date_added,from_file) values("+image_id+",?,?,?,"+dags.RightNow().toOracleString()+",'N')"));

 }

	MultipartForm.add(new HiddenInput("toDatabase","true"));

	MultipartForm.add(new FileInput());
	MultipartForm.add(new SubmitButton());


	Table UploadTable = new Table(1,3);
	UploadTable.add(new Text("Veldu mynd af harðadisknum þínum með \"Browse\" hnappnum"),1,1);
	UploadTable.add(new Text("og smelltu svo á \"Submit\". ATH ef myndin er stór getur þetta tekið lengri tíma"),1,2);
	UploadTable.add(MultipartForm,1,3);

	add(UploadTable);
}


public void getEditor(ModuleInfo modinfo) throws Throwable{

  Connection Conn = null;


  String whichButton = getRequest().getParameter("submit");

////


  String ImageId = modinfo.getRequest().getParameter("image_id");
  String image_in_session = (String) modinfo.getSession().getAttribute("image_in_session");
  ImageHandler handler = (ImageHandler) modinfo.getSession().getAttribute("handler");


  if (image_in_session!=null){
    if (ImageId != null) {
      if( !ImageId.equalsIgnoreCase(image_in_session) ){
        modinfo.getSession().setAttribute("image_in_session",ImageId);
        handler = new ImageHandler(Integer.parseInt(ImageId));
        modinfo.getSession().setAttribute("handler",handler);

      }
      ImageBusiness.handleEvent(modinfo,handler);
      outerTable.add(getEditorForm(handler,ImageId,modinfo),1,2);
    }
    else {
      ImageId = image_in_session;
      ImageBusiness.handleEvent(modinfo,handler);
      outerTable.add(getEditorForm(handler,ImageId,modinfo),1,2);
    }
  }
   else{
    if( ImageId!=null ) {
      modinfo.getSession().setAttribute("image_in_session",ImageId);
      handler = new ImageHandler(Integer.parseInt(ImageId));
      modinfo.getSession().setAttribute("handler",handler);
      ImageBusiness.handleEvent(modinfo,handler);
      outerTable.add(getEditorForm(handler,ImageId,modinfo),1,2);
    }
   }



////

 try {

  Conn = (new ImageEntity()).getConnection();

    if (Conn != null) {
        if (whichButton!=null && !(whichButton.equals(""))){
          // opening a new upload window
          if( whichButton.equals("new")){
          //getSession().removeAttribute("ImageId");


            //debug eiki was in main(....
            if( ImageId!=null){//var til fyrir
              drawUploadTable(ImageId,true);
            }
            else{
              drawUploadTable( ImageBusiness.getImageID(Conn) ,false);
            }

            //


            Upload(Conn,modinfo);
          }
          // done uploading
          else if (whichButton.equals("Vista")){

          //debug eiki make this work!
          //  myWindow.setParentToReload();

            //debug eiki added
            int imageId = Integer.parseInt((String)modinfo.getSession().getAttribute("image_id"));
            String[] categories = modinfo.getRequest().getParameterValues("category");
            ImageBusiness.saveImageToCatagories(imageId,categories);

            //debug eiki make this work!

            //myWindow.close();

          }
          // updating
          else{
            ImageId = (String)modinfo.getSession().getAttribute("image_id");

            //debug eiki
            if( ImageId!=null){//var til fyrir
              drawUploadTable(ImageId,true);
            }
            else{
              drawUploadTable( ImageBusiness.getImageID(Conn) ,false);
            }

          }
          }
          else {
                  //Upload(Conn);//no if here because of the multipart-request
          }
    }

  }
  catch (Throwable E){
          E.printStackTrace(System.err);
          add(E.getMessage());
  }
  finally{
    if( Conn!=null) freeConnection(Conn);
  }





}


}

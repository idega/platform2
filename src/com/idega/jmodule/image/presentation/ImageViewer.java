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

private boolean isAdmin=false;
private int catagory_id;
private String date=null;
private int numberOfImage=3;
public int numberOfDisplayedImage=1;
private boolean backbutton = false;
private boolean showAll = false;
private boolean limitNumberOfImage=false;
private Table outerTable = new Table(1,1);


private Text textProxy = new Text();

private Image change;
private Image delete;
private Image editor;

private String language = "IS";

private int textSize = 1;

private String attributeName = "union_id";
//private int attributeId = -1;
private int attributeId = 3;

public ImageViewer(){
	showAll = true;
}

/**
 * @deprecated replaced with the catagory_id constructor
 */
public ImageViewer(boolean isAdmin, int catagory_id){

	//this.isAdmin=isAdmin;
	this.catagory_id=catagory_id;

}


public ImageViewer(boolean isAdmin, String date){

	//this.isAdmin=isAdmin;
	this.date=date;
	this.showAll=true;

}


public ImageViewer(boolean isAdmin, int catagory_id, String date){

	//this.isAdmin=isAdmin;
	this.catagory_id=catagory_id;
	this.date=date;

}

public ImageViewer(boolean isAdmin, idegaTimestamp timestamp){

	//this.isAdmin=isAdmin;
	this.showAll=true;

 /*       idegaTimestamp stamp= idegaTimestamp.RightNow();
        int daysIn =10;
stamp.addDays(-daysIn);//dagar inni
String timestamp = stamp.toOracleString();*/

        this.date=date;


}


public void setConnectionAttributes(String attributeName, int attributeId) {
            this.attributeName = attributeName;
            this.attributeId = attributeId;
}

public void setConnectionAttributes(String attributeName, String attributeId) {
            this.attributeName = attributeName;
            this.attributeId = Integer.parseInt(attributeId);
}


public ImageViewer(boolean isAdmin, int catagory_id, idegaTimestamp timestamp){

	this.isAdmin=isAdmin;
	this.catagory_id=catagory_id;

	this.date=date;

}



public void setNumberOfDays( int daysIn ){

  idegaTimestamp timestamp;

}

public String getDatastoreType(GenericEntity entity){
  return DatastoreInterface.getDatastoreType(entity.getDatasource());
}


public String getColumnString(ImageCatagoryAttributes[] attribs){

String values = "";

	for (int i = 0 ; i < attribs.length ; i++) {

		values += " image_catagory_id = '"+attribs[i].getImageCatagoryId()+"'" ;
                if( i!= (attribs.length-1) ) values += " OR ";

	}

return values;

}


private void setSpokenLanguage(ModuleInfo modinfo){
 String language2 = modinfo.getRequest().getParameter("language");
    if (language2==null) language2 = ( String ) modinfo.getSession().getAttribute("language");
    if ( language2 != null) language = language2;
}

public void main(ModuleInfo modinfo)throws Exception{

      this.isAdmin=this.isAdministrator(modinfo);

      setSpokenLanguage(modinfo);


      change = new Image("/pics/jmodules/image/"+language+"/change.gif");
      delete = new Image("/pics/jmodules/image/"+language+"/delete.gif");
      editor = new Image("/pics/jmodules/image/"+language+"/imageeditor.gif");



	String image_id = modinfo.getRequest().getParameter("image_id");
	String image_catagory_id = modinfo.getRequest().getParameter("image_catagory_id");


      // String union_id = modinfo.getRequest().getParameter("union_id");


	//PrintWriter out=null;

	if( image_catagory_id !=null ) {

		try{
			catagory_id = Integer.parseInt(image_catagory_id);
			showAll = false;
		} catch(NumberFormatException e) {
			add(new Text("Catagory_id must be a number"));
		}

	}


	boolean byDate=false;

	ImageEntity[] image = new ImageEntity[1];

    try{

      /*  Table mainTable = new Table(2,1);
        mainTable.setBorder(0);
        mainTable.setWidth("100%");
        mainTable.setWidth(1,"1");
        mainTable.setVerticalAlignment(1,1,"top");
        mainTable.setVerticalAlignment(2,1,"top");

        if (image_id != null) {
         mainTable.add( displayImage(Integer.parseInt(image_id)),2,1);
        }
        if (catagory_id != null) {
          mainTable.add( displayCatagory(Integer.parseInt(catagory_id)),2,1);
        }

      add(mainTable);*/


             //   ImageCatagoryAttributes[] attribs = (ImageCatagoryAttributes[]) (new ImageCatagoryAttributes()).findAllByColumn("attribute_name",attributeName,"attribute_id",""+attributeId);
             //   String catagoryString = getColumnString(attribs);

               // PrintWriter out = modinfo.getResponse().getWriter();

		if(isAdmin) {

                  Form imageEditor = new Form("/image/imageadmin.jsp");
                  imageEditor.add(new SubmitButton(editor));
                  add(imageEditor);

		}

		if(image_id != null){
			image[0] = new ImageEntity(Integer.parseInt(image_id));
			backbutton=true;
			setNumberOfDisplayedImage(1);
			add(drawImageTable(image));
		}/*
		else{//view all

			if( date == null ){
				if( showAll ) {
                                 if( (catagoryString!=null) && !(catagoryString.equals("")) ) catagoryString = " where " + catagoryString;
                                  image = (ImageEntity[]) (new ImageEntity()).findAll("select * from image "+catagoryString+" order by date_added");
				}
                                else image = (ImageEntity[]) (new ImageEntity()).findAllByColumn("image_catagory_id",catagory_id);

			}
			else{

                            String DatastoreType = getDatastoreType( new ImageEntity() );
				byDate=true;
                                  if( (catagoryString!=null) && !(catagoryString.equals("")) ) catagoryString = " OR " + catagoryString;

				if( (showAll) && !(DatastoreType.equals("oracle"))  )  image = (ImageEntity[]) (new ImageEntity()).findAll("select * from image where date_added >= '"+date+"'  "+catagoryString+" order by date_added");
				else if( (showAll) && (DatastoreType.equals("oracle"))  )  image = (ImageEntity[]) (new ImageEntity()).findAll("select * from image where date_added >= "+date+" "+catagoryString+" order by date_added");
                                else {
                                  if ( !(DatastoreType.equals("oracle")) ) image = (ImageEntity[]) (new ImageEntity()).findAll("select * from image where image_catagory_id ='"+catagory_id+"' and date_added >= '"+date+"' order by date_added");
			          else image = (ImageEntity[]) (new ImageEntity()).findAll("select * from image where image_catagory_id ='"+catagory_id+"' and date_added >= "+date+" order by date_added");
                                }
                        }

			if(image.length != 0)	add(drawImageTable(image));
			else {


				if( byDate ){
					date=null;
					image = (ImageEntity[]) (new ImageEntity()).findAll("select * from image where "+catagoryString+"order by date_added");

					if( image.length!=0){
						setNumberOfDisplayedImage(1);
						add(drawImageTable(image));
					}
					else add(new Text("<b>No image in database</b><br>"));
				}
				else add(new Text("<b>No image in database</b><br>"));

			}
		}*/
	}
	catch( Exception e ){

			add(new Text(e.getMessage()) );//something went wrong
			e.printStackTrace( System.err);


	}
}


public Table drawImageTable(ImageEntity[] image)throws IOException,SQLException
{
//outerTable.setWidth(400);
	//outerTable.setAlignment("center");

	idegaCalendar funcDate = new idegaCalendar();
	int image_catagory_id;
	ImageCatagory imagecat;
	String image_catagory;
	String imagetext;
	String timestamp;
	int image_id;
	int i = image.length-1;


	if( !limitNumberOfImage ) numberOfDisplayedImage = image.length;
	while ( (i >= 0) && ( numberOfDisplayedImage > (image.length-i))){

               	//image_catagory_id = image[i].getImageCatagoryId();
                     //	imagecat = new ImageCatagory(image_catagory_id);
                      //	image_catagory = imagecat.getName();
                     	//timestamp = (image[i].getDate()).toString();
                        timestamp="";
		//information = getInfoText(author, source, image_catagory, timestamp);
                       imagetext = image[i].getText();
                        image_id = image[i].getID();
                       if(image_id!=-1) outerTable.add(displayImage(imagetext,image[i].getID()), 1, 1);
                        if(isAdmin) {
			Form imageEdit = new Form("/image/imageadmin.jsp?image_id="+image[i].getID());
			imageEdit.add(new SubmitButton(change));
			Form imageEdit2 = new Form("/image/imageadmin.jsp?image_id="+image[i].getID()+"&mode=delete");
			imageEdit2.add(new SubmitButton(delete));
			Table editTable = new Table(2,1);
			editTable.add(imageEdit,1,1);
			editTable.add(imageEdit2,2,1);
			outerTable.add(editTable, 1, 1);
			}
	 i--;
	}
return outerTable;
}

//gimmi
public Table displayImage(int image_id) throws SQLException {
    Table table = new Table();
    com.idega.jmodule.object.Image image = new com.idega.jmodule.object.Image(image_id);
    table.add(image);
return table;
}

public Table displayCatagory(int catagory_id)  throws SQLException {
    Table table = new Table();
      table.setBorder(0);

    ImageCatagory catagory = new ImageCatagory(catagory_id);
    ImageEntity[] image_ent = (ImageEntity[]) catagory.findRelated(new ImageEntity());
    com.idega.jmodule.object.Image image;
    int fjoldi_i_linu = 2;

    if (image_ent != null) {
      if (image_ent.length > 0 ) {
        for (int i = 0 ; i < image_ent.length ; i++ ) {
            table.setVerticalAlignment((i%fjoldi_i_linu)+1,(i/fjoldi_i_linu)+1,"top");
            table.add( displayImage("",image_ent[i].getID()) ,(i%fjoldi_i_linu)+1,(i/fjoldi_i_linu)+1);
        }
      }
    }


return table;
}

public Table displayImage(String ImageText,int image_id) throws SQLException
{

	Table imageTable = new Table(1, 2);
        imageTable.setCellpadding(0);
       // imageTable.setCellpadding(0);
	//imageTable.setWidth("100%");

	imageTable.add(new Image(image_id), 1, 1);

        Text imageText = new Text(ImageText);
        getTextProxy().setFontSize(1);
       // getTextProxy().setFontColor("#FFFFFF");

        imageText = setTextAttributes( imageText );

	if ( ImageText!=null){
          imageTable.add(imageText, 1, 2);
          imageTable.setColor(1,2,"#CCCCCC");
        }

	imageTable.setAlignment("center");
        imageTable.setAlignment(1,1,"center");
        imageTable.setAlignment(1,2,"center");

	imageTable.setVerticalAlignment("top");
return imageTable;
}



public String formatDateWithTime(String date ,String DatastoreType)
{
  String ReturnString = "";

	//String ReturnString = date.substring(5, 7);

        if ( !language.equalsIgnoreCase("IS") ){
          if ( !(DatastoreType.equals("oracle"))){//month/day
            ReturnString = date.substring(8, 10);
            ReturnString = ReturnString+"/"+date.substring(0, 4);
            ReturnString = ReturnString+"/"+date.substring(5, 7);
            ReturnString = ReturnString+" "+date.substring(11, 13);
            ReturnString = ReturnString+":"+date.substring(14, 16);
          }
          else {
            ReturnString = date.substring(0, 4);
            ReturnString = ReturnString+"/"+date.substring(5, 7);
            ReturnString = ReturnString+"/"+date.substring(8, 10);
            ReturnString = ReturnString+" "+date.substring(11, 13);
            ReturnString = ReturnString+":"+date.substring(14, 16);

         //2000-10-10 00:00:00.0
         //30/10/2000 17:58
          }
        }
        else {
           if ( !(DatastoreType.equals("oracle"))){
            ReturnString = date.substring(8, 10);
            ReturnString = ReturnString+"/"+date.substring(5, 7);
            ReturnString = ReturnString+"/"+date.substring(0, 4);
            ReturnString = ReturnString+" "+date.substring(11, 13);
            ReturnString = ReturnString+":"+date.substring(14, 16);
          }
          else {
            ReturnString = date.substring(0, 4);
            ReturnString = ReturnString+"/"+date.substring(8, 10);
            ReturnString = ReturnString+"/"+date.substring(5, 7);
            ReturnString = ReturnString+" "+date.substring(11, 13);
            ReturnString = ReturnString+":"+date.substring(14, 16);

          }




        }
	return ReturnString;
}

private Link insertHyperlink(String imageUrl, String name, String value, String action)
{
	Image linkImage = new Image(imageUrl);
	linkImage.setBorder(0);
	Link myLink = new Link(linkImage);
	myLink.setURL(action);
	myLink.addParameter(name, value);
	return myLink;
}

/**
*
* return a proxy for the main text. Use the standard
* set methods on this object such as .setFontSize(1) etc.
* and it will set the property for all texts.
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


public void setNumberOfDisplayedImage(int numberOfDisplayedImage){
	this.limitNumberOfImage = true;
	if( numberOfDisplayedImage<0 ) numberOfDisplayedImage = (-1)*numberOfDisplayedImage;
	this.numberOfDisplayedImage = numberOfDisplayedImage+1;
}

public void setAdmin(boolean isAdmin){
 	this.isAdmin=isAdmin;
}

public void setFromDate(String SQLdate){
 	this.date=SQLdate;
}

public void setWidth(int width){
 	this.outerTable.setWidth(width);
}

public void setWidth(String width){
 	this.outerTable.setWidth(width);
}

public void setChangeImage(String image_name){
 	change = new Image(image_name);
}

public void setDeleteImage(String image_name){
 	delete = new Image(image_name);
}

public void setEditorImage(String image_name){
 	editor = new Image(image_name);
}



/**
* @deprecated method use setNumberOfExpandedImage instead.
*/
public void setNumberOfImage(int numberOfImage){
 	if( numberOfImage<0 ) numberOfImage = (-1)*numberOfImage;
	this.numberOfImage=numberOfImage;
}

}

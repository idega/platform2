/*
*Copyright 2001 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.idega.idegaweb.IWMainApplication;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.image.data.ImageEntity;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.5
*@modified <a href="mailto:eiki@idega.is">Eirikur Hrafnson</a>
*/
public class Image extends ModuleObject{

private Script theAssociatedScript;
private String overImageUrl;
private String textBgColor = "#CCCCCC";
private boolean limitImageWidth = false;
private boolean zoomView = false;
private boolean linkOnImage = true;

private int imageId = -1;
private int maxImageWidth = 140;


public Image(){
	this("");
	setBorder(0);
}

public Image(String url){
	this(url,"");
	setBorder(0);
}

public Image(String url,String name){
	super();
	setName(name);
	setURL(url);
	setBorder(0);
}

public Image(String name,String url, String overImageUrl){
	super();
	setName(name);
	setURL(url);
	setBorder(0);

	setOverImageURL(overImageUrl);
	Script rollOverScript = new Script();
	rollOverScript.addFunction("swapImgRestore()","function swapImgRestore() {var i,x,a=document.sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;}");
	rollOverScript.addFunction("preloadImages()","function preloadImages(){var d=document; if(d.images){ if(!d.p) d.p=new Array(); var i,j=d.p.length,a=preloadImages.arguments; for(i=0; i<a.length; i++)  if (a[i].indexOf(\"#\")!=0){ d.p[j]=new Image; d.p[j++].src=a[i];}}}");
	rollOverScript.addFunction("findObj(n, d)","function findObj(n, d){var p,i,x;  if(!d) d=document; if((p=n.indexOf(\"?\"))>0&&parent.frames.length) {  d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document); return x;}}");
	rollOverScript.addFunction("swapImage()","function swapImage(){ var i,j=0,x,a=swapImage.arguments; document.sr=new Array; for(i=0;i<(a.length-2);i+=3) if ((x=findObj(a[i]))!=null){document.sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}}");
	setAssociatedScript(rollOverScript);


}


private String getImageURL(ImageEntity image, ModuleInfo modinfo){
  String URIString = com.idega.util.caching.BlobCacher.getCachedUrl(image, modinfo ,"image_value");
  if( URIString == null ){
    URIString = IWMainApplication.IMAGE_SERVLET_URL;
    URIString += "?image_id="+image.getID();
  }
  return URIString;
}

/*
public Image(String name,int image_id, int over_image_id){
	super();

	//String URIString = "/servlet/imageModule";
	String URIString = getImageURL(new ImageEntity(image_id));
            //URIString += "?image"+image_id;
            //URIString += "&image_id="+image_id;

	setName(name);
	setURL(URIString);
	setBorder(0);


	//setOverImageURL(""+overImageUrl);


	String URIString2 = getImageURL(over_image_id);
	//URIString = URIString+"?image_id="+image_id;


	setOverImageURL(URIString2);

	setBorder(0);

	Script rollOverScript = new Script();
	rollOverScript.addFunction("swapImgRestore()","function swapImgRestore() {var i,x,a=document.sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;");
	rollOverScript.addFunction("preloadImages()","function preloadImages(){var d=document; if(d.images){ if(!d.p) d.p=new Array(); var i,j=d.p.length,a=preloadImages.arguments; for(i=0; i<a.length; i++)  if (a[i].indexOf(\"#\")!=0){ d.p[j]=new Image; d.p[j++].src=a[i];}}}");
	rollOverScript.addFunction("findObj(n, d)","function findObj(n, d){var p,i,x;  if(!d) d=document; if((p=n.indexOf(\"?\"))>0&&parent.frames.length) {  d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document); return x;}}");
	rollOverScript.addFunction("swapImage()","function swapImage(){ var i,j=0,x,a=swapImage.arguments; document.sr=new Array; for(i=0;i<(a.length-2);i+=3) if ((x=findObj(a[i]))!=null){document.sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}}");

	setAssociatedScript(rollOverScript);

	}
*/
public Image(String url,String name,int width,int height){
	super();
	setName(name);
	setURL(url);
	setWidth(width);
	setHeight(height);
	setBorder(0);
}
/**
*Fetches an image from the database through the imageservlet
*/

public Image(int image_id) throws SQLException{
  super();
  this.imageId = image_id;
  setBorder(0);
/*
public Image(int imageId) throws SQLException{
  super();
  this.imageId = imageId;
  StringBuffer URIString = new StringBuffer("");
  URIString.append("/servlet/imageModule?image_id=");
  URIString.append(imageId);
  setURL(URIString.toString());
  setBorder(0);
>>>>>>> 1.6
*/
}

public void setBorder(String s){
	setAttribute("border",s);
}

public void setBorder(int i){
	setBorder(Integer.toString(i));
}


public void setURL(String url){
	setSrc(url);
}

public void setSrc(String src){
	setAttribute("src",src);
}

public void setWidth(int width){
	setWidth(Integer.toString(width));
}

public void setWidth(String width){
  setAttribute("width",width);
}

public void setHeight(int height){
  setHeight(Integer.toString(height));

}

public void setHeight(String height){
  setAttribute("height",height);

}

public void setVerticalSpacing(int spacing){
  setAttribute("vspace",Integer.toString(spacing));
}

public void setHorizontalSpacing(int spacing){
  setAttribute("hspace",Integer.toString(spacing));
}

public void setTextBackgroundColor(String color){
  this.textBgColor = color;
}

public String getHeight(){
 return getAttribute("height");
}

public String getWidth(){
  return getAttribute("width");
}

public String getURL(){
	return this.getAttribute("src");
}



public void setOverImageURL(String overImageURL){
	this.overImageUrl = overImageUrl;

}

public String getOverImageURL(){
	return this.overImageUrl;

}

public void setAssociatedScript(Script myScript){
	theAssociatedScript = myScript;
}

public Script getAssociatedScript(){
	return theAssociatedScript;
}

public void setImageLinkZoomView(){
  this.zoomView = true;
}

public void setNoImageLink(){
  this.linkOnImage = false;
}

private String getPrintStringWithName(){
  StringBuffer sPrint = new StringBuffer();
  sPrint.append("<img alt=\"");
  sPrint.append(getName());
  sPrint.append("\"");
  sPrint.append(getAttributeString());
  sPrint.append(" >");
  return sPrint.toString();
}

private String getPrintString(){
  StringBuffer sPrint = new StringBuffer();
  sPrint.append("<img ");
  sPrint.append(getAttributeString());
  sPrint.append(" >");
  return sPrint.toString();
}

private String getHTMLString(){
  if (getName() != null){
    return getPrintStringWithName();
  }
  else{
    return getPrintString();
  }
}



private void getHTMLImage(ModuleInfo modinfo){//optimize by writing in pure html
  try{
    ImageEntity image = new ImageEntity(imageId);
    String URIString = getImageURL(image,modinfo);
    setURL(URIString);

    if( (image!=null) && (image.getID()!=-1) ){//begin debug
      String texti = image.getText();
      String link = image.getLink();
      String name = image.getName();
      if( name != null ) setName(name);


      String width = image.getWidth();
      String height = image.getHeight();

      if(!limitImageWidth){
        if( (width!=null) && (!width.equalsIgnoreCase("")) && (!width.equalsIgnoreCase("-1")) ) {
          setWidth(width);
        }
        if( (height!=null) && (!height.equalsIgnoreCase("")) && (!height.equalsIgnoreCase("-1")) ) {
          setHeight(height);
        }
      }
      else{
        setWidth(maxImageWidth);
      }

      if ( (texti!=null) && (!"".equalsIgnoreCase(texti)) ){
        Table imageTable = new Table(1, 2);
        imageTable.setAlignment("center");
        imageTable.setAlignment(1,1,"center");
        imageTable.setAlignment(1,2,"left");
        imageTable.setVerticalAlignment("top");
        //imageTable.setCellpadding(0);
        //imageTable.setCellspacing(0);
        imageTable.setColor(1,2,textBgColor);
        String sWidth = getWidth();

        if( (sWidth!=null) && (!sWidth.equalsIgnoreCase(""))  && (!limitImageWidth) ){
          imageTable.setWidth(sWidth);
        }
        else if( limitImageWidth ){
          imageTable.setWidth(maxImageWidth);
        }

        Text imageText = new Text(texti);
        imageText.setFontSize(1);

        if ( (link!=null) && (!"".equalsIgnoreCase(link)) ){//has a link
          Link textLink = new Link(imageText,link);
          textLink.setTarget("_new");
          textLink.setFontSize(1);
          imageTable.add(textLink, 1, 2);//add the text with the link on it

          //should we add the image with a link? or just the image
          if( zoomView ){
            Link imageLink = new Link(getHTMLString());
            imageLink.addParameter("image_id",imageId);
            imageTable.add(imageLink, 1, 1);
          }
          else if( (!zoomView) && (linkOnImage) ) {
            Link imageLink = new Link(getHTMLString(), link);
            imageLink.setTarget("_new");
            imageTable.add(imageLink, 1, 1);
          }
          else imageTable.add(getHTMLString(),1,1);

        }
        else{//or no link

          if( zoomView ){
            Link imageLink = new Link(getHTMLString());
            imageLink.addParameter("image_id",imageId);
            imageTable.add(imageLink, 1, 1);
          }
          else imageTable.add(getHTMLString(),1,1);

          imageTable.add(imageText, 1, 2);
        }

        imageTable.print(modinfo);
      }
      else  {
        if(zoomView){
          Link imageLink = new Link(getHTMLString());
          imageLink.addParameter("image_id",imageId);
          imageLink.print(modinfo);
        }
        else print(getHTMLString());
      }
    }//end debug
  }
  catch(Exception e){
    e.printStackTrace(System.err);
    System.out.println(e.getMessage());
  }

}

public void setMaxImageWidth(int maxImageWidth){
  this.limitImageWidth = true;
  this.maxImageWidth = maxImageWidth;
}

public void limitImageWidth( boolean limitImageWidth ){
  this.limitImageWidth=limitImageWidth;
}

  public synchronized Object clone() {
    Image obj = null;
    try {
      obj = (Image)super.clone();
      if(theAssociatedScript != null){
        obj.theAssociatedScript = (Script)this.theAssociatedScript.clone();
      }

      obj.overImageUrl = this.overImageUrl;
      obj.textBgColor = this.textBgColor;
      obj.limitImageWidth = this.limitImageWidth;
      obj.zoomView = this.zoomView;
      obj.linkOnImage = this.linkOnImage;
      obj.imageId = this.imageId;
      obj.maxImageWidth = this.maxImageWidth;

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }

    return obj;
  }

public void print(ModuleInfo modinfo)throws IOException{
	initVariables(modinfo);
	//if( doPrint(modinfo) ){
		if (getLanguage().equals("HTML")){

			//if (getInterfaceStyle().equals("something")){
			//}
			//else{
		/*	if(getParentObject()!=null){
				getParentObject().setAttribute("onMouseOut","swapImgRestore()");
				getParentObject().setAttribute("onMouseOver","swapImage('"+getName()+"','','"+getOverImageURL()+"',1)");
			}
			else print("parent = null");

			if ( getAssociatedScript() != null){
					getAssociatedScript().print(modinfo);
			}*/

                        //added by eiki
                        if( imageId ==-1 ){//from an url

                          if (getName() != null){
                            print(getPrintStringWithName());
                          }
                          else{
                            print(getPrintString());
                          }

                        }
                        else{//from the database
                          getHTMLImage(modinfo);
                        }
				//println("</img>");
			// }
		}
	//}
}



}


//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object;

import java.io.*;
import java.util.*;
import java.sql.*;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class Image extends ModuleObject{

private Script theAssociatedScript;
private String overImageUrl;

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

public Image(String name,int image_id, int overImageUrl){
	super();
	String URIString = "/servlet/imageModule";
	URIString = URIString+"?image_id="+image_id;
	setName(name);
	setURL(URIString);
	setBorder(0);

	setOverImageURL(""+overImageUrl);

	String URIString2 = "/servlet/imageModule?image_id="+overImageUrl;

	URIString = URIString+"?image_id="+image_id;
	setURL(URIString);
	setBorder(0);

	Script rollOverScript = new Script();
	rollOverScript.addFunction("swapImgRestore()","function swapImgRestore() {var i,x,a=document.sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;");
	rollOverScript.addFunction("preloadImages()","function preloadImages(){var d=document; if(d.images){ if(!d.p) d.p=new Array(); var i,j=d.p.length,a=preloadImages.arguments; for(i=0; i<a.length; i++)  if (a[i].indexOf(\"#\")!=0){ d.p[j]=new Image; d.p[j++].src=a[i];}}}");
	rollOverScript.addFunction("findObj(n, d)","function findObj(n, d){var p,i,x;  if(!d) d=document; if((p=n.indexOf(\"?\"))>0&&parent.frames.length) {  d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document); return x;}}");
	rollOverScript.addFunction("swapImage()","function swapImage(){ var i,j=0,x,a=swapImage.arguments; document.sr=new Array; for(i=0;i<(a.length-2);i+=3) if ((x=findObj(a[i]))!=null){document.sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}}");

	setAssociatedScript(rollOverScript);

	}

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
	String URIString = "/servlet/imageModule";
	URIString = URIString+"?image_id="+image_id;
	setURL(URIString);
	setBorder(0);

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

public int getHeight(){
 return Integer.parseInt(getAttribute("height"));
}

public int getWidth(){
  return Integer.parseInt(getAttribute("width"));
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



				if (getName() != null){
				print("<img alt=\""+getName()+"\""+getAttributeString()+" >");
				}
				else{
					print("<img "+getAttributeString()+" >");
				}
				//println("</img>");
			// }
		}
	//}
}


}


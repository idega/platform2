package com.idega.projects.golf.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.golf.*;
import com.idega.jmodule.*;
import com.idega.presentation.*;
import com.idega.projects.golf.templates.page.*;


public class MainSideJSPModule extends PageJSPModule{

  public void initializePage(){
    setPage(new MainPage());
  }


///#########   Föll sem erfast fra Page   #########

  //setBackgroundColor
  public void setBackgroundColor(String color){
    ((MainPage)getPage()).setBackgroundColor(color);
  }


  //setTextColor
  public void setTextColor(String color){
    ((MainPage)getPage()).setTextColor(color);
  }

  //setAlinkColor
  public void setAlinkColor(String color){
    ((MainPage)getPage()).setAlinkColor(color);
  }


  //setVlinkColor
  public void setVlinkColor(String color){
    ((MainPage)getPage()).setVlinkColor(color);
  }


  //setLinkColor
  public void setLinkColor(String color){
    ((MainPage)getPage()).setLinkColor(color);
  }

  public void setHoverColor(String color){
    ((MainPage)getPage()).setHoverColor(color);
  }


  //setName
  public void setName(String name){
    ((MainPage)getPage()).setName(name);
  }


  //setTitle
  public void setTitle(String title){
    ((MainPage)getPage()).setTitle( title);
  }


  //setMarginWidth
  public void setMarginWidth(int width){
    ((MainPage)getPage()).setMarginWidth(width);
  }


  //setMarginHeight
  public void setMarginHeight(int height){
    ((MainPage)getPage()).setMarginHeight(height);
  }


  //setLeftMargin
  public void setLeftMargin(int leftmargin){
    ((MainPage)getPage()).setLeftMargin(leftmargin);
  }


  //setTopMargin
  public void setTopMargin(int topmargin){
    ((MainPage)getPage()).setTopMargin(topmargin);
  }


  //getTitle
  public String getTitle(){
    return((MainPage)getPage()).getTitle();
  }


  //getName
  public String getName(){
    return((MainPage)getPage()).getName();
  }


  //setAssociatedScript
  public void setAssociatedScript(Script myScript){
    ((MainPage)getPage()).setAssociatedScript(myScript);
  }


  //getAssociatedScript
  public Script getAssociatedScript(){
    return((MainPage)getPage()).getAssociatedScript();
  }


  //setBackgroundImage
  public void setBackgroundImage(String imageURL){
    ((MainPage)getPage()).setBackgroundImage(imageURL);
  }


  //setBackgroundImage
  public void setBackgroundImage(Image backgroundImage){
    ((MainPage)getPage()).setBackgroundImage(backgroundImage);
  }


//////////////////////#####





  public void setWidth( String width ){
    ((MainPage)getPage()).setWidth( width );
  }

  public void setContentWidth( String width ){
    ((MainPage)getPage()).setContentWidth( width );
  }

  public void setWidth( int xpos, String Width ){
    ((MainPage)getPage()).setWidth( xpos, Width );
  }


  public void setHeight( String height ){
    ((MainPage)getPage()).setHeight( height );
  }

  public void setTopHeight( String height ){
    ((MainPage)getPage()).setTopHeight( height );
  }

  public void setMiddleHeight( String height){
    ((MainPage)getPage()).setMiddleHeight( height);
  }

  public void setBottomHeight( String height){
    ((MainPage)getPage()).setBottomHeight(height);
  }



  public void add(PresentationObject objectToAdd){
    ((MainPage)getPage()).add(objectToAdd);
  }

  public void add(String position, PresentationObject objectToAdd){
    ((MainPage)getPage()).add( position, objectToAdd);
  }



  public void add(PresentationObject Left, PresentationObject Center, PresentationObject Right){
    ((MainPage)getPage()).add( Left, Center, Right);
  }



  public void add(PresentationObject Left,  PresentationObject Right ){
    ((MainPage)getPage()).add( Left,  Right );
  }




 } // class MainSideJSPModule

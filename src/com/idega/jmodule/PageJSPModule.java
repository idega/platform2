package com.idega.jmodule;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.golf.*;
import com.idega.jmodule.*;
import com.idega.presentation.*;


public class PageJSPModule extends JSPModule{

  public void initializePage(){
    setPage(new Page());
  }



  //setBackgroundColor
  public void setBackgroundColor(String color){
    ((Page)getPage()).setBackgroundColor(color);
  }


  //setTextColor
  public void setTextColor(String color){
    ((Page)getPage()).setTextColor(color);
  }

  //setAlinkColor
  public void setAlinkColor(String color){
    ((Page)getPage()).setAlinkColor(color);
  }


  //setVlinkColor
  public void setVlinkColor(String color){
    ((Page)getPage()).setVlinkColor(color);
  }


  //setLinkColor
  public void setLinkColor(String color){
    ((Page)getPage()).setLinkColor(color);
  }


  //setName
  public void setName(String name){
    ((Page)getPage()).setName(name);
  }


  //setTitle
  public void setTitle(String title){
    ((Page)getPage()).setTitle( title);
  }


  //setMarginWidth
  public void setMarginWidth(int width){
    ((Page)getPage()).setMarginWidth(width);
  }


  //setMarginHeight
  public void setMarginHeight(int height){
    ((Page)getPage()).setMarginHeight(height);
  }


  //setLeftMargin
  public void setLeftMargin(int leftmargin){
    ((Page)getPage()).setLeftMargin(leftmargin);
  }


  //setTopMargin
  public void setTopMargin(int topmargin){
    ((Page)getPage()).setTopMargin(topmargin);
  }


  //getTitle
  public String getTitle(){
    return((Page)getPage()).getTitle();
  }


  //getName
  public String getName(){
    return((Page)getPage()).getName();
  }


  //setAssociatedScript
  public void setAssociatedScript(Script myScript){
    ((Page)getPage()).setAssociatedScript(myScript);
  }


  //getAssociatedScript
  public Script getAssociatedScript(){
    return((Page)getPage()).getAssociatedScript();
  }


  //setBackgroundImage
  public void setBackgroundImage(String imageURL){
    ((Page)getPage()).setBackgroundImage(imageURL);
  }


  //setBackgroundImage
  public void setBackgroundImage(Image backgroundImage){
    ((Page)getPage()).setBackgroundImage(backgroundImage);
  }


 } // class PageJSPModule

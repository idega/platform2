package com.idega.jmodule.template;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.golf.*;
import com.idega.projects.golf.templates.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.projects.golf.templates.page.*;


public class MainPageJSPModule extends PageJSPModule{

  public void initializePage(){
    setPage(new MainPage());
  }

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

  public void add(ModuleObject objectToAdd){
    ((MainPage)getPage()).add(objectToAdd);
  }

  public void add(String position, ModuleObject objectToAdd){
    ((MainPage)getPage()).add( position, objectToAdd);
  }

  public void add(ModuleObject Left, ModuleObject Center, ModuleObject Right){
    ((MainPage)getPage()).add( Left, Center, Right);
  }

  public void add(ModuleObject Left,  ModuleObject Right ){
    ((MainPage)getPage()).add( Left,  Right );
  }


 } // class MainPageJSPModule

package com.idega.jmodule.object.plaf.basic;

import com.idega.util.IWColor;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.plaf.GenericTabbedPaneUI;
import com.idega.jmodule.object.plaf.IWTabbedPaneUI;
import com.idega.jmodule.object.plaf.TabbedPaneFrame;
import com.idega.jmodule.object.plaf.TabPagePresentation;
import com.idega.jmodule.object.plaf.TabPresentation;

import java.util.Vector;
import java.util.EventListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;


/**
 * Title:        IW
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author
 * @version 1.0
 */

public class BasicTabbedPaneUI extends GenericTabbedPaneUI{


  public BasicTabbedPaneUI(){
    super();
  }

  public void initFrame(){
    setFrame(new BasicTabbedPaneFrame(getMainColor()));
  }

  public void initTab(){
    setTab(new BasicTabPresentation(getMainColor()));
  }

  public void initTabPage(){
    setTabPage(new BasicTabPagePresentation(getMainColor()));
  }

  public void setMainColor(IWColor color){
    super.setMainColor(color);
    getFrame().setColor(color);
    getTabPresentation().setColor(color);
    getTabPagePresentation().setColor(color);
  }

  public class BasicTabbedPaneFrame extends GenericTabbedPaneFrame implements TabbedPaneFrame {

    public SubmitButton OK;
    public SubmitButton Cancel;
    public SubmitButton Apply;

    public BasicTabbedPaneFrame(){
      super();
    }

    public BasicTabbedPaneFrame( IWColor color ){
      this();
      this.setColor(color);
    }

    public void initTabbedPaneFrame(){
      this.setRows(3);
      this.setColumns(3);
      this.setColor(new IWColor(212,208,200));
      this.setAlignment(3,3,"right");
    }

    public void initOKButton(){
      OK = new SubmitButton("OK");
    }

    public void initCancelButton(){
      Cancel = new SubmitButton("Cancel");
    }

    public void initApplyButton(){
      Apply = new SubmitButton("Apply");
    }

    public void addOKButton(){
      super.addOKButton();
    }

    public void addCancelButton(){
      super.addCancelButton();
    }

    public void addApplyButton(){
      super.addApplyButton();
    }

    public void fireStateChange(){

    }

  } // InnerClass GenericTabbedPaneFrame

  public class BasicTabPresentation extends GenericTabPresentation {

    public BasicTabPresentation(){
      super();
    }

    public BasicTabPresentation( IWColor color ){
      this();
      this.setColor(color);
    }

//  public void empty(ModuleObject obj){}

    /**
     * unimplemented
     */
    public void setWidth(String width){

    }
    /**
     * unimplemented
     */
    public void SetHeight(String height){

    }

  } // InnerClass BasicTabPresentation

  public class BasicTabPagePresentation extends GenericTabPagePresentation {

    public BasicTabPagePresentation(){
      super();
    }

    public BasicTabPagePresentation( IWColor color ){
      this();
      this.setColor(color);
    }



//  public void add(ModuleObject obj){}
//  public void empty(){}

    public void setWidth(String width){
      this.setWidth(width);
    }

    public void setHeight(String height){
      this.setHeight(height);
    }

    public void fireContentChange(){}

  } // InnerClass GenericTabPagePresentation






}


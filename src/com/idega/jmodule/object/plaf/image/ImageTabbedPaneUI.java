package com.idega.jmodule.object.plaf.image;

import com.idega.util.IWColor;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.plaf.GenericTabbedPaneUI;
import com.idega.jmodule.object.plaf.IWTabbedPaneUI;
import com.idega.jmodule.object.plaf.TabbedPaneFrame;
import com.idega.jmodule.object.plaf.TabPagePresentation;
import com.idega.jmodule.object.plaf.TabPresentation;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.textObject.Link;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.Table;

import java.util.Vector;
import java.util.EventListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;



/**
 * Title:        IW Objects
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ImageTabbedPaneUI extends GenericTabbedPaneUI {

  public ImageTabbedPaneUI() {
  }
  public void initTab(){
    setTab(new ImageTabPresentation());
  }

  public void initTabPage(){
    setTabPage(new ImageTabPagePresentation(getMainColor()));
  }


  public class ImageTabPresentation extends GenericTabPresentation {

    public ImageTabPresentation(){
      super();
//      this.setBorder(1);
    }

    public ImageTabPresentation( IWColor color ){
      this();
      this.setCellpadding(0);
      this.setCellspacing(0);
      this.setColor(color);
    }


    public Link getTabLink(ModuleObject obj){
      Link tempLink = null;

      if(obj instanceof ImageTab){
        tempLink = new Link(((ImageTab)obj).getTabNotSelected());
      }else{
        tempLink = new Link(obj.getName());
      }

      if(getForm() != null){
        tempLink.setToFormSubmit(getForm(),true);
      }

      return tempLink;
    }


    public ModuleObject getTab(int index,boolean selected){
      Link tempObj = (Link)this.getAddedTabs().elementAt(index);

      ModuleObject obj = tempObj.getObject();

      if(obj instanceof ImageTab){
        tempObj.setObject( selected ? ((ImageTab)obj).getTabSelected() : ((ImageTab)obj).getTabNotSelected());
      }else{
        tempObj.setText(obj.getName());
      }


      return tempObj;
    }

    public void setSelectedIndex(int index){
      super.setSelectedIndex(index);
      lineUpTabs();
    }

    public void lineUpTabs(){
      this.resize(this.getAddedTabs().size()+1, 1);
      this.empty();

      if(this.getSelectedIndex() == -1 && this.getAddedTabs().size() != 0){
        this.setSelectedIndex(0);
      }

      for (int i = 0; i < this.getAddedTabs().size(); i++) {
        ModuleObject tempObj = this.getTab(i,(this.getSelectedIndex()==i));
        this.add(tempObj,i+1,1);
      }

      this.setWidth("100%");

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


    public void main(ModuleInfo modinfo) throws Exception {
      this.lineUpTabs();
    }


  } // InnerClass BasicTabPresentation



  public class ImageTabPagePresentation extends GenericTabPagePresentation {

    public ImageTabPagePresentation(){
      super();
    }

    public ImageTabPagePresentation( IWColor color ){
      this();
      this.setColor(color);
      this.setCellpadding(0);
      this.setCellspacing(0);
      this.setWidth("100%");
      initilizePage();
    }


    public void initilizePage(){
      this.resize(1,1);
      this.setColor(getColor().getHexColorString());
      this.setAlignment(1,1,"center");
    }

    public void add(ModuleObject obj){
      this.add(Text.getBreak(),1,1);
      this.add(obj,1,1);
      this.setVerticalAlignment(1,1,"top");
    }
//  public void empty(){}

    public void setWidth(String width){
      super.setWidth(width);
    }

    public void setHeight(String height){
      super.setHeight(height);
    }

    public void empty(){
      super.empty();
    }

    public void fireContentChange(){}

  } // InnerClass GenericTabPagePresentation



} // Class ImageTabbedPaneUI
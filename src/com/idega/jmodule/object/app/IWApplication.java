//idega 2001 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.app;

import com.idega.jmodule.object.FrameSet;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.textObject.Link;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.Window;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
import com.idega.core.data.ICObject;

import java.util.List;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class IWApplication extends FrameSet{

  private final static String IW_BUNDLE_IDENTIFIER = "com.idega.core";

  private String applicationName;

  public IWApplication(){
      this("idegaWeb Application");
  }

  public IWApplication(String applicationName){
    this(applicationName,600,400);
  }

  public IWApplication(String applicationName,int initialWidth,int initialHeight){
    this.setApplicationName(applicationName);
    this.setWidth(initialWidth);
    this.setHeight(initialHeight);
  }

  public void _main(ModuleInfo modinfo)throws Exception{
    if(isChildOfOtherPage()){

    }
    else{
      super._main(modinfo);
    }
  }

  public void setApplicationName(String applicationName){
    this.applicationName=applicationName;
    setTitle(applicationName);
  }

  public String getApplicationName(){
    return applicationName;
  }

  public Image getIcon(){
    return new Image("");
  }


  public static ModuleObject getIWApplicationIcon(Class iwApplicationClass,ModuleInfo modinfo){
    ICObject obj = ICObject.getICObject(iwApplicationClass.getName());
    return getIWApplicationIcon(obj,iwApplicationClass,modinfo);
  }

  public static ModuleObject getIWApplicationIcon(ICObject obj,ModuleInfo modinfo){
      Class iwApplicationClass=null;
      try{
        iwApplicationClass = obj.getObjectClass();
      }
      catch(ClassNotFoundException e){
      }
      return getIWApplicationIcon(obj,iwApplicationClass,modinfo);
  }

  private static ModuleObject getIWApplicationIcon(ICObject obj,Class iwApplicationClass,ModuleInfo modinfo){
      Table icon = new Table(1,2);
      String name = null;
      if(obj!=null){
        name = obj.getName();
      }
      else{
          Window instance = Window.getStaticInstance(iwApplicationClass);
          name = instance.getName();
      }
      /**
       * @todo get the right Image
       */
      Image iconImage = null;
      IWBundle bundle = null;
      if(obj==null){
        bundle = modinfo.getApplication().getBundle(IW_BUNDLE_IDENTIFIER);
        iconImage = bundle.getImage("IWApplicationIcon.gif");
      }
      else{
        bundle = obj.getBundle(modinfo.getApplication());
        if(bundle==null){
          bundle = modinfo.getApplication().getBundle(IW_BUNDLE_IDENTIFIER);
          iconImage = bundle.getImage("IWApplicationIcon.gif");
        }
        else{
          iconImage = bundle.getImage("IWApplicationIcon.gif");
        }
      }

      Link icon_image = new Link(iconImage);
      icon_image.setWindowToOpen(iwApplicationClass);
      Text icon_text = new Text(name);
      icon_text.setFontSize(1);
      icon_text.setFontColor("black");
      Link icon_link = new Link(name);
      icon_link.setWindowToOpen(iwApplicationClass);
      icon.setAlignment(1,1,"center");
      icon.add(icon_image,1,1);
      icon.setAlignment(1,2,"center");
      icon.add(icon_link,1,2);
      return icon;
  }

  public static List getApplictionICObjects(){
    try{
      return EntityFinder.findAllByColumn(GenericEntity.getStaticInstance(ICObject.class),ICObject.getObjectTypeColumnName(),ICObject.COMPONENT_TYPE_APPLICATION);
    }
    catch(Exception e){
      return null;
    }
  }

  public void print(ModuleInfo modinfo)throws Exception{

    if(isChildOfOtherPage()){
      getIWApplicationIcon(this.getClass(),modinfo).print(modinfo);
    }
    else{
      super.print(modinfo);
    }

  }

}//End class

/*
 * $Id: ModuleObjectContainer.java,v 1.8 2001/07/09 16:18:28 tryggvil Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.jmodule.object;

import com.idega.jmodule.object.textObject.*;
import java.util.*;
import java.io.*;

/**
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.3
 */
public class ModuleObjectContainer extends ModuleObject {
  protected Vector theObjects;
  private boolean goneThroughMain = false;

  public ModuleObjectContainer() {
  }

  /**
   * Add an object inside this container
   */
  protected void add(int index,ModuleObject modObject) {
    try {
      if (theObjects == null) {
        this.theObjects = new Vector();
      }
      if (modObject != null) {
        modObject.setParentObject(this);
        this.theObjects.add(index,modObject);
      }
    }
    catch(Exception ex) {
      ExceptionWrapper exep = new ExceptionWrapper(ex,this);
    }
  }

  /**
   * Add an object inside this container
   */
  public void add(ModuleObject modObject) {
    try {
      if (theObjects == null) {
        this.theObjects = new Vector();
      }
      if (modObject != null) {
        modObject.setParentObject(this);
        this.theObjects.addElement(modObject);
      }
    }
    catch(Exception ex) {
      ExceptionWrapper exep = new ExceptionWrapper(ex,this);
    }
  }

  public void add(Object moduleObject) {
    if (moduleObject instanceof ModuleObject) {
      add((ModuleObject)moduleObject);
    }
    else {
      System.err.println("Not instance of ModuleObject and therefore cannot be added to ModuleObjectContainer: " + moduleObject);
    }
  }

  public void addAtBeginning(ModuleObject modObject) {
    if (theObjects == null) {
      theObjects = new Vector();
    }
    modObject.setParentObject(this);
    theObjects.insertElementAt(modObject,0);
  }

  /**
   * Add an object inside this container - same as the add() function
   * @deprecated replaced by the add function
   */
  public void addObject(ModuleObject modObject) {
    add(modObject);
  }

  /**
   * Adds an simple string (Creates a Text object around it)
   */
  public void add(String theText) {
    add(new Text(theText));
  }

  /**
   * Adds an array of strings and creates an end of line character after each element
   */
  public void add(String[] theTextArray) {
    for (int i = 0; i < theTextArray.length; i++) {
      add(theTextArray[i]);
      addBreak();
    }
  }

  public void addBreak() {
    Text text = Text.getBreak();
    add(text);
  }

  public void addText(String theText) {
    add(new Text(theText));
  }

  public void addText(String theText, String format) {
    Text text = new Text();
    if (format != null) {
      if (format.equals("bold")) {
        text.setBold();
      }
      else if (format.equals("italic")) {
        text.setItalic();
      }
      else if (format.equals("underline")) {
        text.setUnderline();
      }
    }
    add(text);
  }

  public void addText(int integerToInsert) {
    addText(Integer.toString(integerToInsert));
  }

  public List getAllContainingObjects() {
    return theObjects;
  }

  public boolean isEmpty() {
    if (theObjects != null) {
      return theObjects.isEmpty();
    }
    else {
      return true;
    }
  }

  public void main(ModuleInfo modinfo) throws Exception {
  }

  public void _main(ModuleInfo modinfo) throws Exception {
    if (!goneThroughMain) {
      initVariables(modinfo);
      try {
        main(modinfo);
      }
      catch(Exception ex) {
        add(new ExceptionWrapper(ex,this));
      }
      if (!isEmpty()) {
        for (int index = 0; index < numberOfObjects(); index++) {
          ModuleObject tempobj = objectAt(index);

          try {
            if (tempobj != null) {
              if (tempobj != this) {
                tempobj._main(modinfo);
              }
            }
          }
          catch(Exception ex) {
            add(new ExceptionWrapper(ex,this));
          }
        }
      }
    }
    goneThroughMain = true;
  }

  /**
  * Empties the container of all ModuleObjects stored inside
  */
  public void empty() {
    if (theObjects != null) {
      theObjects.removeAllElements();
    }
  }

  protected void setObjects(Vector objects) {
    this.theObjects = objects;
  }



  /*protected void prepareClone(ModuleObject newObjToCreate){
      int number = numberOfObjects();
      for (int i = 0; i < number; i++) {
        ModuleObject tempObj = this.objectAt(i);
        ((ModuleObjectContainer)newObjToCreate).add((ModuleObject)tempObj.clone());
      }

     // if (this.theObjects!=null){
    //((ModuleObjectContainer)newObjToCreate).setObjects((Vector)this.theObjects.clone());
     // }
  }*/


  public void print(ModuleInfo modinfo) throws Exception {
    goneThroughMain = false;
    initVariables(modinfo);
    //Workaround for JRun - JRun has hardcoded content type text/html in JSP pages
    //if(this.doPrint(modinfo)){
    if (modinfo.getLanguage().equals("WML")) {
      modinfo.setContentType("text/vnd.wap.wml");
    }
    if (!isEmpty()) {
      int numberofObjects = numberOfObjects();
      for (int index = 0; index < numberofObjects; index++) {
        ModuleObject tempobj = objectAt(index);
        try {
          if (tempobj != null) {
            tempobj.print(modinfo);
            flush();
          }
        }
        catch(Exception ex) {
          ExceptionWrapper exep = new ExceptionWrapper(ex,this);
          exep.print(modinfo);
        }
      }
    }
  }


  public ModuleObject getContainedObject(int objectInstanceID) {
    List list = this.getAllContainingObjects();
    Iterator iter = list.iterator();
    while (iter.hasNext()) {
      ModuleObject item = (ModuleObject)iter.next();
      if(item.getICObjectInstanceID()==objectInstanceID){
        return item;
      }
      else if(item instanceof ModuleObjectContainer){
        ModuleObject theReturn = ((ModuleObjectContainer)item).getContainedObject(objectInstanceID);
        if(theReturn != null){
          return theReturn;
        }
      }
    }
    return null;
  }

  public ModuleObject getContainedObject(String objectInstanceID) {

    try{
      return getContainedObject(Integer.parseInt(objectInstanceID));
    }
    catch(NumberFormatException e){
      int objectInstanceIDInt = Integer.parseInt(objectInstanceID.substring(0,objectInstanceID.indexOf(".") + 1));

      String index = objectInstanceID.substring(objectInstanceID.indexOf(".") + 1,objectInstanceID.length());
      if(index.indexOf(".") == -1){
        return ((ModuleObjectContainer)getContainedObject(objectInstanceIDInt)).objectAt(Integer.parseInt(index));
      }
      else{
        int xindex = Integer.parseInt(index.substring(0,index.indexOf(".") + 1));
        int yindex = Integer.parseInt(index.substring(index.indexOf(".") + 1,index.length()));
        return ((Table)getContainedObject(objectInstanceIDInt)).containerAt(xindex,yindex);
      }
    }

  }

  /*public ModuleObject getContainedObject(String objectTreeID) {
    if (objectTreeID.indexOf(".") == -1) {
      return objectAt(Integer.parseInt(objectTreeID));
    }
    else {
      String newString = objectTreeID.substring(objectTreeID.indexOf(".") + 1,objectTreeID.length());
      String index = objectTreeID.substring(0,objectTreeID.indexOf("."));

      ModuleObject obj = objectAt(Integer.parseInt(index));
      if (obj instanceof ModuleObjectContainer){
        return ((ModuleObjectContainer)obj).getContainedObject(newString);
      }
      else {
        return obj;
      }
    }
  }*/

  /*public void updateTreeIDs() {
    if (!isEmpty()) {
      String thisTreeID = this.getTreeID();
      int numberOfObjects = numberOfObjects();
      for(int index = 0; index < numberOfObjects; index++) {
        ModuleObject tempobj = objectAt(index);
        if (tempobj != null) {
          if (tempobj != this) {
            try {
              if (thisTreeID == null) {
                String treeID = Integer.toString(index);
                tempobj.setTreeID(treeID);
              }
              else {
                String treeID = thisTreeID + "." + index;
                tempobj.setTreeID(treeID);
              }
            }
            catch(Exception ex) {
              ExceptionWrapper exep = new ExceptionWrapper(ex,this);
              add(exep);
            }
          }
        }
      }
    }
  }*/

  /*public void setTreeID(String ID) {
    super.setTreeID(ID);
    updateTreeIDs();
  }*/

  public int numberOfObjects() {
    if (theObjects != null) {
      return theObjects.size();
    }
    else {
      return 0;
    }
  }

  public ModuleObject objectAt(int index) {
    if (theObjects != null) {
      return (ModuleObject)theObjects.elementAt(index);
    }
    else {
      return null;
    }
  }

  public int getIndex(ModuleObject ob) {
    if (theObjects == null)
      return(-1);
    else
      return(theObjects.indexOf(ob));
  }

  /**
   * Insert element at specified index
   */
  public void insertAt(ModuleObject modObject, int index) {
    try {
      if (theObjects == null) {
        this.theObjects = new Vector();
      }
      if (modObject != null) {
        modObject.setParentObject(this);
        theObjects.insertElementAt(modObject,index);
      }
    }
    catch(Exception ex) {
      ExceptionWrapper exep = new ExceptionWrapper(ex,this);
    }
  }

  /**
   * Replace element at specified index
   */
/*  public void setAt(ModuleObject modObject, int index) {
    try {
      if (theObjects == null) {
        this.theObjects = new Vector();
      }
      if (modObject != null) {
        modObject.setParentObject(this);
        theObjects.setElementAt(modObject,index);
      }
    }
    catch(Exception ex) {
      ExceptionWrapper exep = new ExceptionWrapper(ex,this);
    }
  }*/

  public void removeAll(java.util.Collection c) {
    if (theObjects != null)
      theObjects.removeAll(c);
  }

  public void _setModuleInfo(ModuleInfo modinfo){
    setModuleInfo(modinfo);
    if ( ! isEmpty() ){
      for(int index=0;index<numberOfObjects();index++){
        ModuleObject tempobj = objectAt(index);
        if(tempobj!=null){
          if(tempobj!=this){
            tempobj._setModuleInfo(modinfo);
          }
        }
      }
    }
  }

  public synchronized Object clone() {
    ModuleObjectContainer obj = null;
    try {
      obj = (ModuleObjectContainer)super.clone();
      //if(!(this instanceof Table)){
        if (this.theObjects != null) {
            //obj.setObjects((Vector)this.theObjects.clone());
            obj.theObjects=(Vector)this.theObjects.clone();
            ListIterator iter = obj.theObjects.listIterator();
            while (iter.hasNext()) {
              int index = iter.nextIndex();
              Object item = iter.next();
              //Object item = obj.theObjects.elementAt(index);
              if(item instanceof ModuleObjectContainer){
                obj.theObjects.set(index,((ModuleObjectContainer)item).clone());
              }
            }
        //}
      }
    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }

  public boolean remove(ModuleObject obj){
    if(theObjects!=null){
      if(theObjects.remove(obj)){
        return true;
      }
    }
    return false;
  }

  /**
   * index lies from 0,length-1
   */
  public Object set(int index,ModuleObject o){
    if(theObjects==null){
     theObjects = new Vector();
    }
    return theObjects.set(index,o);
  }


}

/*
 * $Id: ModuleObjectContainer.java,v 1.4 2001/05/10 10:45:28 palli Exp $
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

  public void add(Object moduleObject){
    if(moduleObject instanceof ModuleObject){
      add((ModuleObject)moduleObject);
    }else{
      System.err.println("Not instance of ModuleObject and therefore cannot be added to ModuleObjectContainer: " + moduleObject);
    }
  }

  public void addAtBeginning(ModuleObject modObject){
      if (theObjects == null){
      this.theObjects = new Vector();
    }
    modObject.setParentObject(this);
    this.theObjects.insertElementAt(modObject,0);
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

  public void addText(String theText,String format){
    Text text = new Text();
    if (format != null){
      if (format.equals("bold")){
        text.setBold();
      }
      else if (format.equals("italic")){
        text.setItalic();
      }
      else if (format.equals("underline")){
        text.setUnderline();
      }
    }
    add(text);
  }

  public void addText(int integerToInsert){
    addText(Integer.toString(integerToInsert));
  }


  public Vector getAllContainingObjects(){
    //return (ModuleObject[]) theObjects.toArray(ModuleObject[]);
    return theObjects;
  }

  public boolean isEmpty(){
    if (theObjects != null){
      return theObjects.isEmpty();
    }
    else{
      return true;
    }
  }


  public void main(ModuleInfo modinfo)throws Exception{
  }

  public void _main(ModuleInfo modinfo)throws Exception{
          if(!goneThroughMain){
            initVariables(modinfo);
            try{
              main(modinfo);
            }
            catch(Exception ex){
              //setErrorMessage("Exception: in "+this.getClass().getName()+" "+ex.getClass().getName()+" - "+ex.getMessage());
              //add("Exception: in "+this.getClass().getName()+" "+ex.getClass().getName()+" - "+ex.getMessage());
              //System.err.println(ex.getMessage());
              //ex.printStackTrace(System.err);
              add(new ExceptionWrapper(ex,this));
            }
            if ( ! isEmpty() ){
                    //for (Enumeration e = theObjects.elements(); e.hasMoreElements(); ){
                    //	ModuleObject tempobj = (ModuleObject)  e.nextElement();
                    for(int index=0;index<numberOfObjects();index++){
                        ModuleObject tempobj = objectAt(index);

                        try{
                            if(tempobj!=null){
                              if(tempobj!=this){
                                tempobj._main(modinfo);
                              }
                            }
                        }
                        catch(Exception ex){
                            //add("Exception: in "+this.getClass().getName()+" "+ex.getClass().getName()+" - "+ex.getMessage());
                            //setErrorMessage("Exception: in "+this.getClass().getName()+" "+ex.getClass().getName()+" - "+ex.getMessage());
                            //System.err.println("Exception: in "+this.getClass().getName()+" "+ex.getClass().getName()+" - "+ex.getMessage());
                            //ex.printStackTrace(System.err);
                            add(new ExceptionWrapper(ex,this));
                        }
                    }
            }
          }
          goneThroughMain=true;
          //System.out.println("Inside _main() for: "+this.getClass().getName()+" - Tread: "+Thread.currentThread().toString());


  }


  /**
  *Empties the container of all ModuleObjects stored inside
  */
  public void empty(){
    if(theObjects != null){
      theObjects.removeAllElements();
    }
  }


  protected void setObjects(Vector objects){
    this.theObjects = objects;
  }


  protected void prepareClone(ModuleObject newObjToCreate){
      int number = numberOfObjects();
      for (int i = 0; i < number; i++) {
        ModuleObject tempObj = this.objectAt(i);
        ((ModuleObjectContainer)newObjToCreate).add((ModuleObject)tempObj.clone());
      }

      /*if (this.theObjects!=null){
    ((ModuleObjectContainer)newObjToCreate).setObjects((Vector)this.theObjects.clone());
      }*/
  }



  public void print(ModuleInfo modinfo)throws IOException{
          goneThroughMain=false;
    initVariables(modinfo);
    //Workaround for JRun - JRun has hardcoded content type text/html in JSP pages
    //if(this.doPrint(modinfo)){
            if (modinfo.getLanguage().equals("WML")){
              modinfo.getResponse().setContentType("text/vnd.wap.wml");
            }
            if ( ! isEmpty() ){
                //String theErrorMessage = getErrorMessage();
                    //if (theErrorMessage == null){
                    //for (Enumeration e = theObjects.elements(); e.hasMoreElements(); ){
                    //	ModuleObject tempobj = (ModuleObject)  e.nextElement();
                    for(int index=0;index<numberOfObjects();index++){
                        ModuleObject tempobj = objectAt(index);

                        try{
                            if(tempobj!=null){
                              tempobj.print(modinfo);
                              flush();
                            }
                        }
                        catch(Exception ex){

                            ExceptionWrapper exep = new ExceptionWrapper(ex,this);
                            exep.print(modinfo);

                            //println("<pre>");
                            //println("Exception:");
                            //println(ex.getClass().getName()+" - ");
                            //println(ex.getMessage());
                            //ex.printStackTrace(getPrintWriter());
                            //println("</pre>");
                            /*
                            tempobj.setErrorMessage(ex.getMessage());
                            System.err.println(ex.getMessage());
                            ex.printStackTrace(System.err);
                            */
                        }

                    }
                  //}
                  //else{
                    //println("<pre>");
                    //println("Exception:");
                    //println(theErrorMessage);
                    //println("</pre>");




                    /*
                    Table table = new Table(1,2);
                    table.setBorder(2);
                    table.add("Exception:",1,1);
                    table.add(errorMessage,1,2);
                    table.print(modinfo);*/

                  }
            //}
          //}
          //System.out.println("Inside print() for: "+this.getClass().getName()+" - Tread: "+Thread.currentThread().toString());
  }



  /*
  protected void registerObject(ModuleObject objectToAdd){
    objectToAdd.setAsAdded();
    if (this.hasBeenAdded()){
      ((ModuleObjectContainer)getParentObject()).registerObject(objectToAdd);
    }
    else{
      if (objectToAdd instanceof ModuleObjectContainer){
        //copy registered objects inside objectToAdd to this
         // register objectToAdd to this

      }
      else{

         //  register objectToAdd to this

      }

    }
  }
  */

  public ModuleObject getContainedObject(String objectTreeID){
    /*String thisTreeID=this.getTreeID();
    if(thisTreeID == null){
      if(objectTreeID.indexOf(".") == -1){
        return objectAt(Integer.parseInt(objectTreeID));
      }
      else{
        String newString=objectTreeID.substring(objectTreeID.indexOf(".")+1,objectTreeID.length());
        //String newString2=newString1.substring(newString1.indexOf(".")+1,newString1.length());
        String index = objectTreeID.substring(0,objectTreeID.indexOf("."));
        return ((ModuleObjectContainer)objectAt(Integer.parseInt(index))).getContainedObject(newString);
      }
    }
    //else if(objectTreeID.equals(thisTreeID)){
    //  return this;
    //}
    else{*/
      if(objectTreeID.indexOf(".") == -1){
        //String newString=objectTreeID.substring()
        return objectAt(Integer.parseInt(objectTreeID));
      }
      else{
        String newString=objectTreeID.substring(objectTreeID.indexOf(".")+1,objectTreeID.length());
        //String newString2=newString.substring(newString.indexOf(".")+1,newString.length());

        //String index = newString.substring(0,newString.indexOf("."));
        String index = objectTreeID.substring(0,objectTreeID.indexOf("."));

        ModuleObject obj = objectAt(Integer.parseInt(index));
        if (obj instanceof ModuleObjectContainer){
          return ((ModuleObjectContainer)obj).getContainedObject(newString);
        }
        else{
          return obj;
        }


      }
    //}
    /*if (this.hasBeenAdded()){
      return ((ModuleObjectContainer)getParentObject()).getContainedObject(objectID);
    }
    else{
      return null;
    }*/
  }

  public void updateTreeIDs(){
         if ( ! isEmpty() ){
                  String thisTreeID=this.getTreeID();
                  int numberOfObjects=numberOfObjects();
                  for(int index=0;index<numberOfObjects;index++){
                      ModuleObject tempobj = objectAt(index);
                      if (tempobj != null){
                        if(tempobj!=this){
                          try{
                              if(thisTreeID==null){
                                String treeID=Integer.toString(index);
                                //System.out.println(treeID);
                                tempobj.setTreeID(treeID);
                              }
                              else{
                                String treeID=thisTreeID+"."+index;
                                //System.out.println(treeID);
                                tempobj.setTreeID(treeID);

                              }
                          }
                          catch(Exception ex){
                              //add("Exception: in "+this.getClass().getName()+" "+ex.getClass().getName()+" - "+ex.getMessage());
                              //setErrorMessage("Exception: in "+this.getClass().getName()+" "+ex.getClass().getName()+" - "+ex.getMessage());
                              //System.err.println("Exception: in "+this.getClass().getName()+" "+ex.getClass().getName()+" - "+ex.getMessage());
                              //ex.printStackTrace(System.err);
                              ExceptionWrapper exep = new ExceptionWrapper(ex,this);
                              add(exep);
                          }
                          /*catch(Throwable ex){
                            add("Exception: in "+this.getClass().getName()+" "+ex.getClass().getName()+" - "+ex.getMessage());
                            //setErrorMessage(ex.getClass().getName()+" - "+ex.getMessage());
                            System.err.println("Exception: in "+this.getClass().getName()+" "+ex.getClass().getName()+" - "+ex.getMessage());
                            ex.printStackTrace(System.err);
                          }*/
                        }
                      }
          //id++;
                  }

    }

  }

  public void setTreeID(String ID){
    super.setTreeID(ID);
    updateTreeIDs();
  }

  public int numberOfObjects(){
    if(theObjects!=null){
      return theObjects.size();
    }
    else{
      return 0;
    }
  }

  public ModuleObject objectAt(int index){
    if(theObjects!=null){
      return (ModuleObject)theObjects.elementAt(index);
    }
    else{
      return null;
    }
  }

  //temp
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
  // temp ends
}

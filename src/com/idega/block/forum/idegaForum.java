package com.idega.block.forum;

import com.idega.block.forum.presentation.*;
import com.idega.jmodule.object.*;
import com.idega.core.accesscontrol.business.AccessControl;

/**
 * Title:        JForums<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega margmiðlun hf.<p>
 * Company:      idega margmiðlun hf.<p>
 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>
 * @version 1.0
 */

public class idegaForum extends JModuleObject{
  private String MenuColor = "#4D6476";
  private String ItemColor= "#C5C5C5";
  private String MenuFontColor= "#FFFFFF";
  private String ItemFontColor= "#000000";
  private int fontSize = 2;
  private int headerFontSize = 2;

//  protected ModuleObject SomeThreads;
//  protected Variables myVariables;
//  protected Forum myForum;
//  protected String UserPresentation;
//  protected String DefaultPresentation;

//  public idegaForum( boolean isAdmin, String UserName, int UserID, String SomeThreadsPresentation ) {
    // setja í Application
//    SomeThreads = ((ModuleObject)Class.forName(SomeThreadsPresentation)).getSomeThreadsModule();

//  }
/*

  public ModuleObject getSomeThreads(){
    return SomeThreads;
  }



  public void main( ModuleInfo modinfo ){

    myVariables = (Variables)modinfo.getRequest().getSession().getAttribute("ForumVariables");
    if (myVariables == null){
      myVariables =  new myVariables();
      modinfo.getRequest().getSession().setAttribute("ForumVariables", myVariables );
    }

    myVariables.update(modinfo);

    myForum = new Forum(modinfo);

    this.add(myForum);
  }



  protected class Forum extends ModuleObjectContainer{

    public Table FrameTable;
    public FourmPresentation myPresentation;

    public Forum( ModuleInfo modinfo, String presentation ){
      FrameTable = new Table(1,1);

      myPresentation = (ForumPresentation)Class.forName(presentation);

      this.add(FrameTable);
    }




    public void doForums(){
      FrameTable.empty();
      FrameTable.add( getSideStatus() );
    }

    public void main(){
      doForums();
    }

  }  // Class Forum


  protected class Variables{

    //public Type ......;

    public Variables(){
      initialiceVariables();
    }

    public void initialiceVariables(){

    }

  }  // Class Variables
*/


  public void main(ModuleInfo modinfo) throws Exception {
    this.empty();

    Forums myForums = (Forums)modinfo.getSessionAttribute("idegaForums");

    if (myForums == null){
      myForums = new Forums();
      myForums.setUseForums(true);
      myForums.setUseUserRegistration(false);
      myForums.setUseLogin(false);
      myForums.setUseNameField(true);

      myForums.setMenuColor(MenuColor);
      myForums.setItemColor(ItemColor);
      myForums.setItemMenuColor(MenuFontColor);
      myForums.setItemFontColor(ItemFontColor);
      myForums.setMainFontSize(fontSize);
      myForums.setHeaderFontSize(headerFontSize);

      modinfo.setSessionAttribute("idegaForums" , myForums );
    }
    //myForums.setAllowedToDeleteThread(this.hasPermission(AccessControl.getDeletePermissionString(),this,modinfo));
    //myForums.setConnectionAttributes("union_id", 1);
    add(myForums);

  }



  public void setMenuColor(String MenuColor){
    this.MenuColor = MenuColor;
  }

  public void setItemColor(String ItemColor){
    this.ItemColor = ItemColor;
  }
  public void setItemMenuColor(String MenuFontColor){
    this.MenuFontColor = MenuFontColor;
  }

  public void setItemFontColor (String ItemFontColor){
    this.ItemFontColor = ItemFontColor;
  }

  public void setMainFontSize(int i){
    this.fontSize = i;
  }

  public void setHeaderFontSize(int i){
    this.headerFontSize = i;
  }




} // Class idegaForum





//    info.getResponse().sendRedirect(targetSide);


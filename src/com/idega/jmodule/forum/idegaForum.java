package com.idega.jmodule.forum;

import com.idega.jmodule.forum.presentation.*;
import com.idega.presentation.*;

/**
 * Title:        JForums<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega margmiðlun hf.<p>
 * Company:      idega margmiðlun hf.<p>
 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>
 * @version 1.0
 */

public class idegaForum extends Block{

//  protected PresentationObject SomeThreads;
//  protected Variables myVariables;
//  protected Forum myForum;
//  protected String UserPresentation;
//  protected String DefaultPresentation;

  public idegaForum( boolean isAdmin, String UserName, int UserID, String SomeThreadsPresentation ) {
    // setja í Application
//    SomeThreads = ((PresentationObject)Class.forName(SomeThreadsPresentation)).getSomeThreadsModule();

  }
/*

  public PresentationObject getSomeThreads(){
    return SomeThreads;
  }



  public void main( IWContext iwc ){

    myVariables = (Variables)iwc.getRequest().getSession().getAttribute("ForumVariables");
    if (myVariables == null){
      myVariables =  new myVariables();
      iwc.getRequest().getSession().setAttribute("ForumVariables", myVariables );
    }

    myVariables.update(iwc);

    myForum = new Forum(iwc);

    this.add(myForum);
  }



  protected class Forum extends PresentationObjectContainer{

    public Table FrameTable;
    public FourmPresentation myPresentation;

    public Forum( IWContext iwc, String presentation ){
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
} // Class idegaForum





//    info.getResponse().sendRedirect(targetSide);


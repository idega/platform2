package com.idega.block.forum.business;

import com.idega.presentation.*;
import com.idega.presentation.text.*;
import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * Title:        JForums<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega margmiðlun hf.<p>
 * Company:      idega margmiðlun hf.<p>
 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>
 * @version 1.0
 */

public class SomeThreads extends PresentationObjectContainer {

  private Table myTable;
  private ForumService service;
  private Link moreLink;
  private boolean more;
  private int numOfThreads;
  private String target;


  // ### Smiðir  ###

  public SomeThreads( String targetSide, int num_Of_Threads, boolean moreLink)throws SQLException {
    super();
    service = new ForumService();
    more = moreLink;
    numOfThreads = (num_Of_Threads <= service.numofThreads() ) ? num_Of_Threads : service.numofThreads();
    target = targetSide;

    if (more)
      myTable = new Table(1,numOfThreads + 1);
    else
      myTable = new Table(1,numOfThreads);

    myTable.setCellspacing(0);
    myTable.setCellpadding(6);
    myTable.setBorder(0);


    initSomeThreads();
    this.add(myTable);
  }




/*
  public SomeThreads( String targetSide, int num_Of_Threads, boolean moreLink)throws SQLException {
    super();
    service = new ForumService();

    int numOfThreads = (num_Of_Threads <= service.numofThreads() ) ? num_Of_Threads : service.numofThreads();

    if (more)
      myTable = new Table(1,numOfThreads + 1);
    else
      myTable = new Table(1,numOfThreads);

    myTable.setCellspacing(0);
    myTable.setCellpadding(6);
    myTable.setBorder(0);

    int i = 1;
    for(i = 1; i <= numOfThreads; i++)
      myTable.add(getThread(targetSide, i), 1, i);

    if (more)
        moreLink = new Link("Meira...",targetSide);
        myTable.add( moreLink , 1, i);

    this.add(myTable);
  }

*/


  // ### Private _ Föll  ###

  private Link getThread(String targetSide, int X_mostNew )throws SQLException{
    String mySubject = "";
    Vector myVector = service.getNewThread( X_mostNew );

    if (myVector != null){
      mySubject = (String)myVector.elementAt(1);

    Link myLink = new Link( mySubject, targetSide);
    myLink.addParameter("forum_thread_id", myVector.elementAt(0).toString());
    myLink.addParameter("state", "3");
    return myLink;
    } else{
    return new Link("", targetSide);
    }
  }

/*
  private Link getThread(String targetSide, int X_mostNew , int forum )throws SQLException{
    String mySubject = "";

    mySubject = service.getNewThread( X_mostNew, forum );

    Link myLink = new Link( mySubject, targetSide);
    return myLink;
  }
*/

  // ### Public - Föll ###


  public void initSomeThreads()throws SQLException {

    myTable.empty();

    int i = 1;
    for(i = 1; i <= numOfThreads; i++)
      myTable.add(getThread(target, i), 1, i);

    if (more)
       moreLink = new Link("Meira...",target);
       moreLink.addParameter("state", "2");
       myTable.add( moreLink , 1, i);
  }



  public void changeMoreLink(String text){
    moreLink = new Link(text);
    moreLink.addParameter("state", "2");
  }

  public void addMoreLinkParameter( String parameter_name, String parameter_value){
    moreLink.addParameter( parameter_name, parameter_value);

  }

  public void setCellpadding(int p){
    myTable.setCellpadding(p);
  }

  public void setBorder(int b){
    myTable.setBorder(b);
  }

  public void setLinkAlignment(String alignment ){
    for (int i = 1; i <= myTable.getColumns(); i++){
      for (int j = 1; j <= myTable.getRows(); j++){
        myTable.setAlignment(i,j,alignment);
      }
    }
  }

  public void setVerticalLinkAlignment(String alignment ){
    for (int i = 1; i <= myTable.getColumns(); i++){
      for (int j = 1; j <= myTable.getRows(); j++){
        myTable.setVerticalAlignment(i,j,alignment);
      }
    }
  }


  public void setLinkCellHeight(String height ){
      for (int j = 1; j <= myTable.getRows(); j++){
        myTable.setHeight(j,height);
      }
  }


  public void setLinkCellWidth(String width ){
      for (int i = 1; i <= myTable.getColumns(); i++){
        myTable.setWidth(i,width);
      }
  }


  public void setBackgroundColor(String color){
    myTable.setColor(color);
  }

}  // class SomeThreads

package com.idega.block.forum.business;



import com.idega.presentation.*;

import com.idega.block.forum.data.*;

import com.idega.presentation.text.*;

import com.idega.util.*;

import java.util.*;

import java.sql.*;



/**

 * Title:        JForums<p>

 * Description:  <p>

 * Copyright:    Copyright (c) idega margmiðlun hf.<p>

 * Company:      idega margmiðlun hf.<p>

 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>

 * @version 2.0

 */



public class ThreadTree  extends PresentationObjectContainer {



  protected Vector OpenThreads;

  protected Table ThreadTable;

  protected ForumService service;

  protected Table FrameTable;

  protected int ForumID;

  protected int level;            //notað til að geta stillt af töflu við mismunandi dypt i trenu



  protected final Boolean isTrue = new Boolean(true);

  protected final Boolean isFalse = new Boolean(false);



  protected String theThreadID;





  // ## config-variables

  protected int ThreadsOnPage;

  protected int TreeNum;

  protected int ImageWidht;

  protected int ImageHeight;

  protected boolean useForums;

  protected IWContext iwc;

  protected boolean TreeOpen;

  protected boolean mainOnes;



  protected Link OpenCloseAll;

  protected PresentationObject CloseAll;

  protected PresentationObject OpenAll;

  protected Link openTree;

  protected Link closeTree;

  protected Link NewThreadLink;

  protected Link nextpage;

  protected Link lastpage;



  protected int IconWidth;

  protected int TableWidth;

  protected int SubjectWitht;

  protected int dateWitdh;

  protected int SenderNameWidth;

  protected int NumOfRespWidth;



  protected Image LCornerOpen;

  protected Image TeOpen;

  protected Image FolderIconOpen;

  protected Image LCornerClosed;

  protected Image TeClosed;

  protected Image FolderIconClosed;

  protected Image Blank;

  protected Image VertLine;

  protected Image FileIconEnd;

  protected Image LCornerEnd;

  protected Image node;



  public Link toForums;



  private Image tempIcon;



  protected Link LOpen;

  protected Link LClosed;

  protected Link IconOpen;

  protected Link IconClosed;

  protected Link TClosed;

  protected Link TOpen;



  protected PresentationObjectContainer container;



  private boolean first;



  private int tempint;





  //## Smiðir ##//



  public ThreadTree()throws SQLException{

    service = new ForumService();

    OpenThreads = new Vector();

    this.ThreadsOnPage = 10;

    TreeNum = 1;

    level = 1;

    TreeOpen = false;

    OpenCloseAll = new Link();

    NewThreadLink = new Link();

    nextpage = new Link("<small>Næsta Síða");

    lastpage = new Link("<small>Fyrri Síða");

    NewThreadLink.setText("<small>Nýtt innlegg");

//    InitTreePageLinks();



//   sækja useFourmus í grunn



//    if (!useForums)

    ForumID = 1;

/*	else

		ForumID = ID úr session;*/





    toForums = null;

    tempIcon = new Image("/pics/forum/bladra.gif");





    FrameTable = new Table(1,1);

    FrameTable.setWidth("100%");

    this.add(FrameTable);

    tempint = 1;

  }



  public ThreadTree(IWContext iwc)throws Exception{

    this();

    if (iwc.getParameter("forum_id") != null){

       ForumID = Integer.parseInt(iwc.getParameter("forum_id"));

    }

    this.iwc = iwc;

    //this.main(iwc);

    first = true;

  }



  public ThreadTree(int forum_id)throws Exception{

    this();

    ForumID = forum_id;

  }





  //## Föll ##//



  protected Link closeLinks( PresentationObject LinkImage, int thread_id ){

    Link closeLink = new Link(LinkImage);

    closeLink.addParameter("FTclose", "" + thread_id);

    closeLink.addParameter( "state", iwc.getParameter("state"));

    return closeLink;

  }





  protected Link openLinks( PresentationObject LinkImage, int thread_id ){

    Link openLink = new Link(LinkImage);

    openLink.addParameter("FTopen", "" + thread_id);

    openLink.addParameter( "state", iwc.getParameter("state"));

    return openLink;

  }



  protected PresentationObject subjectLink(String subject, int thread_id, int forum_id, boolean isOpen ){

      if (theThreadID != null && theThreadID.equals("" + thread_id)){

        Text myText = new Text("<small>" + subject);

        myText.setBold();

//        myText.setFontSize(2);

        return myText;

      }else{

        Link myLink = new Link("<small>" + subject);

        myLink.addParameter( "forum_thread_id", "" + thread_id);

        myLink.addParameter( "forum_id", "" + forum_id );

        myLink.addParameter( "state", "3");



        if (!isOpen)

          myLink.addParameter("FTopen", "" + thread_id);



        return myLink ;

      }

  }



/*

  protected Vector getTreePageLinks( Vector LinkFace, PresentationObject NextIfMore ){

  }

  */



  protected void updateOpenThreads(){

    String text = iwc.getParameter("FTopen");

    if ( text != null &  !OpenThreads.contains(text))

     OpenThreads.add(text);



    if ( iwc.getParameter("FTclose") != null ){

      OpenThreads.remove(iwc.getParameter("FTclose"));

    }

  }



  protected Table ThreadLink(ForumThread theThread, Image Front, PresentationObject Icon, boolean Open){    //, boolean New){

      Table linkTable;

      linkTable = new Table(9,1);



      if (Open)

        linkTable.add( closeLinks(Icon, theThread.getID()), 1, 1);

      else

        linkTable.add( openLinks(Icon, theThread.getID()), 1, 1);



      linkTable.add(Text.emptyString(), 2, 1);

      linkTable.add( subjectLink( theThread.getThreadSubject(), theThread.getID(), ForumID, Open ), 3, 1);

      linkTable.add(Text.emptyString(), 4, 1);



      Text userName = new Text("<small>" + theThread.getUserName());

      userName.setFontFace("Arial");



      linkTable.add( userName, 5, 1);

      linkTable.add(Text.emptyString(), 6, 1);



      Text response = new Text("<small>" + theThread.getNumberOfResponses());

      response.setFontFace("Arial");



      linkTable.add(response, 7, 1);

      linkTable.add(Text.emptyString(), 8, 1);



      Text Thdate = new Text("<small>" + new IWTimestamp(theThread.getThreadDate()).toString(true, false, false));

      Thdate.setFontFace("Arial");



      linkTable.add( Thdate, 9, 1);



      linkTable.setAlignment(1,1, "center");

      linkTable.setAlignment(5,1, "left");

      linkTable.setAlignment(7,1, "center");

      linkTable.setAlignment(9,1, "right");

      linkTable.setWidth("100%");

      linkTable.setWidth(1,"18");

      linkTable.setWidth(2,"6");

      linkTable.setHeight(18);



      linkTable.setWidth(4,"6");

      linkTable.setWidth(5,"53");

      linkTable.setWidth(6,"6");

      linkTable.setWidth(7,"12");

      linkTable.setWidth(8,"6");

      linkTable.setWidth(9,"80");

      linkTable.setCellspacing(0);

      linkTable.setCellpadding(0);

      //linkTable.setBorder(1);



    return linkTable;

  }





  protected Table drawTables(ForumThread[] Threads, boolean topLevel)throws SQLException{

    int Rows = 1;

    Vector openRows = new Vector();



    if (topLevel)

      level = 1;

    else

      level++;



   for (int k = 0; k <= Threads.length; k++){

	openRows.add( k, isFalse);

   }



  if ( !TreeOpen ){

    for(int i = 0; i < Threads.length; i++){

      if ( 0 < Threads[i].getNumberOfResponses()){

        for (int j = 0; j < OpenThreads.size(); j++ ){

         if (((String)OpenThreads.elementAt(j)).equals( "" + Threads[i].getID())){

           Rows++;

           openRows.set(i,isTrue);

           //continue;

         }

        }

      }

      Rows++;

    }

  } else {

      for(int i = 0; i < Threads.length; i++){

        if ( 0 < Threads[i].getNumberOfResponses()){

            Rows++;

            openRows.set(i,isTrue);

        }

        Rows++;

      }

   }





    Table myTable;

    if (topLevel){

      myTable = new Table(1,Rows-1);

      int m = 0;

       for (int k = 1; k < Rows; k++){

         myTable.add(ThreadLink(Threads[m], new Image("images/endicon.gif"), tempIcon, ((Boolean)openRows.elementAt(m)).booleanValue() ), 1, k);

         if (((Boolean)openRows.elementAt(m)).equals(isTrue) ){

            k++;

            level++;

            myTable.add(drawTables(service.getThreadChildrens( ForumID, ((ForumThread)Threads[m]).getID()), false), 1, k);

         }

         m++;

      }



    }else{

      myTable = new Table(2,Rows-1);

      myTable.setWidth(1,"20");

      int m = 0;

      for (int k = 1; k < Rows; k++){

       myTable.add(ThreadLink(Threads[m], new Image("images/endicon.gif"), tempIcon, ((Boolean)openRows.elementAt(m)).booleanValue() ), 2, k);

       if (((Boolean)openRows.elementAt(m)).equals(isTrue) ){

         k++;

         level++;

         myTable.add(drawTables(service.getThreadChildrens( ForumID, ((ForumThread)Threads[m]).getID()), false), 2, k);

       }

       m++;

     }

    }



//    myTable.setBorder(1);

    myTable.setCellspacing(0);

    myTable.setCellpadding(0);

    myTable.setWidth("100%");

    level--;



    return myTable;

  }





  public void doTable()throws SQLException{

  	FrameTable.empty();

        level = 1;

  	FrameTable.add(drawTables(service.getTopLevelThreads(ForumID, ThreadsOnPage*(TreeNum-1), ThreadsOnPage*TreeNum), true));

  }





  public void setNewThreadLink(PresentationObject onLink){

    NewThreadLink.setObject(onLink);

  }



  public Link getNewThreadLink(){

     return NewThreadLink;

  }



  private void updateNewThreadLink(){

    NewThreadLink.clearParameters();

/*    if (this.iwc!= null){

      if (this.iwc.getParameter("forum_id") != null)

         NewThreadLink.addParameter("forum_id", this.iwc.getParameter("forum_id"));

    }

*/

    NewThreadLink.addParameter("forum_id", ForumID);

    NewThreadLink.addParameter("from","NTL");

    NewThreadLink.addParameter( "state", "5");



  }



  public void setToForumListLink(PresentationObject obj){

    if (toForums == null){

       toForums = new Link("<small>Yfirlit Spjallþráða");

       toForums.addParameter("state", "1");

    }

    toForums.setObject(obj);

  }







  public Link getToForumListLink(){

    if (toForums == null){

       toForums = new Link("<small>Yfirlit Spjallþráða");

       toForums.addParameter("from", "TFLink");

       toForums.addParameter("state", "1");

    }



    return toForums;

  }





  public void setLCornerOpen( Image ImageName ){

    LCornerOpen = ImageName;

  }



  public void setTeOpen( Image ImageName ){

    TeOpen = ImageName;

  }



  public void setFolderIconOpen( Image ImageName ){

    FolderIconOpen = ImageName;

  }



  public void setLCornerClosed( Image ImageName ){

    LCornerClosed = ImageName;

  }



  public void setTeClosed( Image ImageName ){

    TeClosed = ImageName;

  }



  public void setFolderIconClosed( Image ImageName ){

    FolderIconClosed = ImageName;

  }



  public void setBlank( Image ImageName ){

    Blank = ImageName;

  }



  public void setVertLine( Image ImageName ){

    VertLine = ImageName;

  }



  public void setFileIconEnd( Image ImageName ){

    FileIconEnd = ImageName;

  }



  public void setLCornerEnd( Image ImageName ){

    LCornerEnd = ImageName;

  }



  public void setNode( Image ImageName ){

    node = ImageName;

  }



  public void setTempIcon( Image ImageName ){

    tempIcon =  ImageName;

  }



  public void setOpenCloseLink(PresentationObject openTree, PresentationObject closeTree){

      OpenAll = openTree;

      CloseAll = closeTree;

  }



  /*

  public void InitTreePageLinks() throws SQLException {

    int length = service.getForumThread( ForumID, -1, "thread_date").length;   // temp

    int links = length/ThreadsOnPage;

    if (length%ThreadsOnPage > 0)

       links++;



    container = new PresentationObjectContainer();



    Link myLink;

    for (int i = 1; i <= links; i++){

        myLink = new Link("<small>" + i);

        myLink.addParameter("FTreePage", "" + i);

        myLink.addParameter( "state", "same");

        container.add(myLink);

        if ( i != links)

           container.add(new Text(" | "));

    }





  }



  public PresentationObjectContainer getTreePageLinks(){

    return container;

  }

*/



  public void updateNextPageLink(){

    nextpage.clearParameters();

    nextpage.addParameter("FTreePage", TreeNum+1);

    nextpage.addParameter("state", "2");

  }





  public PresentationObject getNextPageLink(){

    return nextpage;

  }





  public void updateLastPageLink(){

    if (TreeNum <= 1){

      lastpage.clearParameters();

      lastpage.addParameter("state", iwc.getParameter("state"));

    }else{

      lastpage.clearParameters();

      lastpage.addParameter("FTreePage", TreeNum-1);

      lastpage.addParameter("state", "2");

    }

  }



  public PresentationObject getLastPageLink(){

    return lastpage;

  }





  public Link getOpenCloseLink(){

    return OpenCloseAll;

  }



  public void updateOpenCloseLink(){

    if (!TreeOpen){

      if (OpenAll == null)

        OpenCloseAll.setText("<small>Opna Þræði");

      else

        OpenCloseAll.setObject(OpenAll);



      OpenCloseAll.clearParameters();

      OpenCloseAll.addParameter("FTOpenAll", "true");

      OpenCloseAll.addParameter( "state", "same");



    }else{

      if (CloseAll == null)

        OpenCloseAll.setText("<small>Loka Þraðum");

      else

        OpenCloseAll.setObject(CloseAll);



      OpenCloseAll.clearParameters();

      OpenCloseAll.addParameter("FTCloseAll", "true");

      OpenCloseAll.addParameter( "state", "same");

    }

  }





  protected void checkSettings(){



    if(iwc.getParameter("FTOpenAll") != null)

      TreeOpen = true;



    if(iwc.getParameter("FTCloseAll") != null){

      TreeOpen = false;

      OpenThreads = new Vector();

    }

    if (iwc.getParameter("FTreePage") != null){

       TreeNum = Integer.parseInt(iwc.getParameter("FTreePage"));

       OpenThreads = new Vector();

    }



    if (iwc.getParameter("forum_id") != null){

       ForumID = Integer.parseInt(iwc.getParameter("forum_id"));

    }



    if (iwc.getParameter("from") != null){

      if (iwc.getParameter("from").equals("TFLink"))

       OpenThreads = new Vector();

    }





    updateLastPageLink();

    updateNextPageLink();

    updateNewThreadLink();

    updateOpenCloseLink();

    updateOpenThreads();

  }









  public void main(IWContext iwc) throws SQLException, Exception{

    this.iwc = iwc;

    theThreadID = iwc.getParameter("forum_thread_id");

    checkSettings();

    doTable();

    first = false;

  }















}   // class ThreadTree3


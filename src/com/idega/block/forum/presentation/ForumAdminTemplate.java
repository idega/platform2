package com.idega.block.forum.presentation;



import com.idega.block.forum.business.*;

import com.idega.presentation.*;

import com.idega.presentation.ui.*;

import java.sql.*;





/**

 * Title:        idegaForms

 * Description:

 * Copyright:    Copyright (c) 2000 idega margmiðlun hf.

 * Company:      idega margmiðlun hf.

 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>

 * @version 1.0

 */



public class ForumAdminTemplate extends ForumAdminPresentation {





  public ForumAdminTemplate() throws Exception{

    super();

  }





  public PresentationObject ForumEdit_Presentation() throws SQLException{

    ForumList FList = getForumList();



    Table Frame = new Table(1,7);

      Frame.add(FList.getForumNameInput(),1,3);

      Frame.add(FList.getForumDescriptionArea(),1,5);

      Frame.add(FList.getForumSubmitButton(),1,7);



    return Frame;

  }





  public PresentationObject ForumEmail_Presentation() throws SQLException{

    ForumList FList = getForumList();



    Table Frame = new Table(1,5);

      Frame.add(FList.getEmailInput(),1,3);

      Frame.add(FList.getForumSubmitButton(),1,5);



    return Frame;

  }







} // Class ForumAdminTemplate


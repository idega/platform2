package com.idega.block.forum.business;



import com.idega.presentation.*;

import com.idega.presentation.text.*;

import com.idega.presentation.ui.*;

import java.io.*;



/**

 * Title:        JForums<p>

 * Description:  <p>

 * Copyright:    Copyright (c) idega margmiðlun hf.<p>

 * Company:      idega margmiðlun hf.<p>

 * @author idega 2000 - idega team - <a href="mailto:gummi@idega.is">gummi@idega.is</a>

 * @version 1.0

 */



public class ThreadEntry {





  protected IWContext iwc;

  protected TEVariables myVar;



  //######  Smiðir #######



  public ThreadEntry(IWContext iwc){

     this.iwc = iwc;



    if (this.iwc.getSessionAttribute( "ThreadEntryVar") == null){

      myVar = new TEVariables();

      this.iwc.setSessionAttribute( "ThreadEntryVar", myVar);

    }else{

      myVar = (TEVariables)this.iwc.getSessionAttribute( "ThreadEntryVar");

    }

    checkSettings();

  }





  public void checkSettings(){





  }







  // ### Public-Föll ###





  public void setSubmitButton(String onSubmitButton){

    myVar.Submit = new SubmitButton(onSubmitButton);

    // setja var í session ef þarf

  }



   public void setSubmitButton(Image onSubmitButton){

    myVar.Submit = new SubmitButton(onSubmitButton);

    // setja var í session ef þarf

  }



   public SubmitButton getSubmitButton(){

    if (myVar.Submit == null)

      setSubmitButton("Senda");

    return myVar.Submit;

  }





  public void setBodyTextArea(){

    myVar.bodyTextArea = new TextArea("thread_body");

    myVar.bodyTextArea.keepStatusOnAction();

    myVar.bodyTextArea.setWrap(true);

    // setja var í session ef þarf

  }







  public TextArea getBodyTextArea(){

    if (myVar.bodyTextArea == null){

      setBodyTextArea();

    }

    return myVar.bodyTextArea;

  }



  public void setNameTextInput(){

    myVar.NameTextInput = new TextInput("thread_author");

    myVar.NameTextInput.keepStatusOnAction();

    // setja var í session ef þarf

  }



  public TextInput getNameTextInput(){

    if (myVar.NameTextInput == null){

      setNameTextInput();

    }

    return myVar.NameTextInput;

  }





  public void setEmailInput(){

    myVar.myEmailInput = new TextInput("thread_email");

    myVar.myEmailInput.keepStatusOnAction();

    // setja var í session ef þarf

  }



  public TextInput getEmailInput(){

    if (myVar.myEmailInput == null){

      setEmailInput();

    }

    return myVar.myEmailInput;

  }





  public void setSubjectTextInput( boolean Re ){

/*    if (info.getRequest().getParameter("thread_subj") != null){

      String subj = "Re: " + info.getRequest().getParameter("thread_subj");

      SubjectTextInput = new TextInput("thread_subject", subj);}

    else*/

    myVar.SubjectTextInput = new TextInput("thread_subject");

    myVar.SubjectTextInput.keepStatusOnAction();



    // setja var í session ef þarf

  }



    public TextInput getSubjectTextInput(){

      if (myVar.SubjectTextInput == null){

        myVar.SubjectTextInput = new TextInput("thread_subject");

        myVar.SubjectTextInput.keepStatusOnAction();

      }

    return myVar.SubjectTextInput;

  }







  protected class TEVariables{



    public TextArea bodyTextArea;

    public TextInput SubjectTextInput;

    public TextInput NameTextInput;

    public TextInput myEmailInput;



    public SubmitButton Submit;



    public TEVariables(){



    }



  } // class TEVariables





}   //class ThreadEntry


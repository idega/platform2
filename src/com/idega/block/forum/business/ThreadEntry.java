package com.idega.block.forum.business;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
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


  protected ModuleInfo modinfo;
  protected TEVariables myVar;

  //######  Smiðir #######

  public ThreadEntry(ModuleInfo modinfo){
     this.modinfo = modinfo;

    if (this.modinfo.getRequest().getSession().getAttribute( "ThreadEntryVar") == null){
      myVar = new TEVariables();
      this.modinfo.getRequest().getSession().setAttribute( "ThreadEntryVar", myVar);
    }else{
      myVar = (TEVariables)this.modinfo.getRequest().getSession().getAttribute( "ThreadEntryVar");
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
    //public TextInput EmailTextInpub;

    public SubmitButton Submit;

    public TEVariables(){

    }

  } // class TEVariables


}   //class ThreadEntry

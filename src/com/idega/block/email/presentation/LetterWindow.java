package com.idega.block.email.presentation;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

import com.idega.block.email.business.*;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.text.TextFormat;

import java.rmi.RemoteException;
import java.util.Collection;

public class LetterWindow extends IWAdminWindow{


  private IWBundle iwb;
  private IWResourceBundle iwrb;
  public final static String prmInstanceId = "eml_inst";
  private Collection topics;
  private int instance = -1;
	private TextFormat tf ;

  public LetterWindow() {
    super();
    setScrollbar(false);
    setWidth(800 );
    setHeight(500 );
    setLocation(true);
    setResizable(true);
    setTitlebar(true);
  }


  public String getBundleIdentifier(){
    return "com.idega.block.email";
  }

  public void main(IWContext iwc)throws RemoteException{
    //debugParameters(iwc);

    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
		tf = TextFormat.getInstance();

    setTitle("Letter");
    addTitle("Letter Editor");

    if(iwc.isParameterSet(prmInstanceId))
      instance = Integer.parseInt(iwc.getParameter(prmInstanceId));

    if(instance > 0)
      topics = MailFinder.getInstance().getInstanceTopics(instance);

    processForm(iwc);

    Form F = new Form();
    F.addParameter(prmInstanceId,instance);
    F.add(getLetterForm(iwc));
    add(F);
  }

	public PresentationObject getLetters(IWContext iwc){
	  Table T = new Table();


		return T;
	}

	public PresentationObject getLetterLinks(IWContext iwc){
	  Table T = new Table();

		return T;
	}

  public PresentationObject getLetterForm(IWContext iwc){
    Table T = new Table();
    TextInput fromAddress = new TextInput("from_address");
    fromAddress.setLength(80);
    TextInput fromName = new TextInput("from_name");
    fromName.setLength(80);
    TextInput subject = new TextInput("subject");
    subject.setLength(80);
    TextArea body = new TextArea("body",70,20);
    int row = 1;

    if(topics !=null && topics.size() > 0){
      T.add(tf.format(iwrb.getLocalizedString("list.topic","Topic"),tf.HEADER),1,row);
      DropdownMenu drp = new DropdownMenu("topic_id");
      java.util.Iterator iter = topics.iterator();
      if(topics.size() > 0){
        while (iter.hasNext()) {
          EmailTopic tpc = (EmailTopic) iter.next();
          drp.addMenuElement(tpc.toString(), tpc.getName());
        }
        T.add(drp ,2,row++);
      }
      else if(iter.hasNext()){
        EmailTopic tpc = (EmailTopic) iter.next();
        T.add(new HiddenInput("topic_id",tpc.toString()),2,row++);
      }
    }

    T.add(tf.format(iwrb.getLocalizedString("letter.from_name","Sender name"),tf.HEADER),1,row);
    T.add(fromName,2,row++);
    T.add(tf.format(iwrb.getLocalizedString("letter.from_address","Sender address"),tf.HEADER),1,row);
    T.add(fromAddress,2,row++);
    T.add(tf.format(iwrb.getLocalizedString("letter.subject","Subject"),tf.HEADER),1,row);
    T.add(subject,2,row++);
    T.add(tf.format(iwrb.getLocalizedString("letter.body","Body"),tf.HEADER),1,row);
    T.add(body,2,row++);

    SubmitButton send = new SubmitButton(iwrb.getLocalizedImageButton("send","Send"),"send");
    //SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"save");

		CheckBox save = new CheckBox("save","true");
    Table submitTable = new Table(5,1);
		submitTable.add(tf.format(iwrb.getLocalizedString("save_to_archive","Save to archive")),3,1);
    submitTable.add(save,3,1);
    submitTable.add(send,5,1);
    T.add(submitTable,2,row);
    T.setAlignment(2,row,"right");

    return T;
  }

  public void processForm(IWContext iwc)throws RemoteException{
    String fromname = iwc.getParameter("from_name");
    String fromaddress = iwc.getParameter("from_address");
    String subject = iwc.getParameter("subject");
    String body = iwc.getParameter("body");

    if(iwc.isParameterSet("send.x")){
      int topic = Integer.parseInt(iwc.getParameter("topic_id"));
			boolean save = iwc.isParameterSet("save") && "true".equals(iwc.getParameter("save"));
      EmailLetter letter = MailBusiness.getInstance().saveTopicLetter(-1,fromname,fromaddress,subject,body,EmailLetter.TYPE_SENT,topic);
      if(topic >0){
        MailBusiness.getInstance().sendLetter(letter,MailFinder.getInstance().getTopic(topic));
      }
    }
    else if(iwc.isParameterSet("save.x")){

    }
  }
  
  
}

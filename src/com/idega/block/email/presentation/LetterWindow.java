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
import com.idega.presentation.util.TextFormat;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

import java.rmi.RemoteException;
import java.util.Collection;

public class LetterWindow extends IWAdminWindow {

	private IWBundle iwb;
	private IWResourceBundle iwrb;
	public final static String prmInstanceId = "eml_inst";
	private Collection topics;
	private int instance = -1;
	private TextFormat tf;
	String inpFromname, inpFromaddress,inpSubject , inpBody; 
	EmailTopic defaultTopic = null;
	int topic=-1;

	public LetterWindow() {
		super();
		setScrollbar(false);
		setWidth(800);
		setHeight(600);
		setLocation(true);
		setResizable(true);
		setTitlebar(true);
	}

	public String getBundleIdentifier() {
		return "com.idega.block.email";
	}

	public void main(IWContext iwc) throws RemoteException {
		//debugParameters(iwc);

		this.iwb = getBundle(iwc);
		this.iwrb = getResourceBundle(iwc);
		this.tf = TextFormat.getInstance();

		setTitle("Letter");
		addTitle("Letter Editor");

		if (iwc.isParameterSet(prmInstanceId)) {
			this.instance = Integer.parseInt(iwc.getParameter(prmInstanceId));
		}

		if (this.instance > 0) {
			this.topics = MailFinder.getInstance().getInstanceTopics(this.instance);
		}

		boolean sent = processForm(iwc);

		Form F = new Form();
		F.addParameter(prmInstanceId, this.instance);
		if (sent) {
			F.add(getSentInfo(iwc));
		}
		else {
			F.add(getLetterForm(iwc));
		}
		add(F);
	}

	public PresentationObject getLetters(IWContext iwc) {
		Table T = new Table();

		return T;
	}

	public PresentationObject getLetterLinks(IWContext iwc) {
		Table T = new Table();

		return T;
	}

	public PresentationObject getSentInfo(IWContext iwc) {
		Table T = new Table();
		T.add(
			this.tf.format(
				this.iwrb.getLocalizedString("list.sent_info", "Letter has been sent to all recipients"),
				TextFormat.HEADER));
		return T;
	}

	public PresentationObject getLetterForm(IWContext iwc) {
		Table T = new Table();
		TextInput fromAddress = new TextInput("from_address");
		fromAddress.setLength(80);
		TextInput fromName = new TextInput("from_name");
		fromName.setLength(80);
		TextInput subject = new TextInput("subject");
		subject.setLength(80);
		TextArea body = new TextArea("body", 70, 20);
		int row = 1;

		if (this.topics != null && this.topics.size() > 0) {
			T.add(this.tf.format(this.iwrb.getLocalizedString("list.topic", "Topic"), TextFormat.HEADER), 1, row);
			DropdownMenu drp = new DropdownMenu("topic_id");
			drp.setToSubmit();
			java.util.Iterator iter = this.topics.iterator();
			EmailTopic tpc;
			if (this.topics.size() > 1) {
				while (iter.hasNext()) {
					tpc = (EmailTopic) iter.next();
					if(this.defaultTopic == null) {
						this.defaultTopic = tpc;
					}
					drp.addMenuElement(tpc.getIdentifier().toString(), tpc.getName());
				}
				if(this.topic>0) {
					drp.setSelectedElement(this.topic);
				}
				T.add(drp, 2, row++);
			} 
			else if (iter.hasNext()) {
				 tpc = (EmailTopic) iter.next();
				this.defaultTopic = tpc;
				T.add(new HiddenInput("topic_id",tpc.getIdentifier().toString()), 2, row++);
			}
		}
		
		if(this.defaultTopic!=null){
			fromName.setContent(this.defaultTopic.getSenderName());
			fromAddress.setContent(this.defaultTopic.getSenderEmail());
			if(this.inpSubject!=null) {
				subject.setContent(this.inpSubject);
			}
				body.setContent(this.inpBody);
			
				
		}

		T.add(
			this.tf.format(this.iwrb.getLocalizedString("letter.from_name", "Sender name"), TextFormat.HEADER),	1,	row);
		T.add(fromName, 2, row++);
		T.add(this.tf.format(this.iwrb.getLocalizedString("letter.from_address", "Sender address"), TextFormat.HEADER),1,row);
		T.add(fromAddress, 2, row++);
		T.add(this.tf.format(this.iwrb.getLocalizedString("letter.subject", "Subject"), TextFormat.HEADER), 1, row);
		T.add(subject, 2, row++);
		T.add(this.tf.format(this.iwrb.getLocalizedString("letter.body", "Body"), TextFormat.HEADER), 1, row);
		T.add(body, 2, row++);

		SubmitButton send = new SubmitButton(this.iwrb.getLocalizedImageButton("send", "Send"), "send");
		//SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"save");

		//CheckBox save = new CheckBox("save", "true");
		Table submitTable = new Table(5, 1);
		//submitTable.add(tf.format(iwrb.getLocalizedString("save_to_archive","Save to archive")),3,1);
		//submitTable.add(save,3,1);
		submitTable.add(send, 5, 1);
		T.add(submitTable, 2, row);
		T.setAlignment(2, row, "right");

		return T;
	}

	public boolean processForm(IWContext iwc) throws RemoteException {
		this.inpFromname = iwc.getParameter("from_name");
		this.inpFromaddress = iwc.getParameter("from_address");
		this.inpSubject = iwc.getParameter("subject");
		if(this.inpSubject == null) {
			this.inpSubject = "";
		}
		this.inpBody = iwc.getParameter("body");
		if(this.inpBody == null) {
			this.inpBody = "";
		}
		this.topic = iwc.isParameterSet("topic_id")? Integer.parseInt(iwc.getParameter("topic_id")):-1;
		
		if (iwc.isParameterSet("send.x")) {
			EmailLetter letter =	MailBusiness.getInstance().saveTopicLetter(-1,this.inpFromname,	this.inpFromaddress,	this.inpSubject,this.inpBody,EmailLetter.TYPE_SENT,this.topic);			
			if (this.topic > 0) {
				MailBusiness.getInstance().sendLetter(letter,MailFinder.getInstance().getTopic(this.topic));
				return true;
			}
		} else if (iwc.isParameterSet("save.x")) {

		}
		else{
			if(this.topic >0) {
				this.defaultTopic = MailFinder.getInstance().getTopic(this.topic);
			}
		}
		return false;
	}

}

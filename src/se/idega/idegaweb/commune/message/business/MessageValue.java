/*
 * $Id: MessageValue.java,v 1.2.2.1 2006/02/12 21:57:22 palli Exp $
 * Created on 7.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.business;

import java.io.File;


/**
 * 
 *  Last modified: $Date: 2006/02/12 21:57:22 $ by $Author: palli $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2.2.1 $
 */
public class MessageValue extends com.idega.block.process.message.business.MessageValue {
	
   private String letterBody;
   private Boolean sendLetterIfNoEmail;
   private String contentCode;
   private Boolean alwaysSendLetter; 
   private Boolean sendMail;
   private String printedLetterType;
   private File attachment = null;
	
	public Boolean getAlwaysSendLetter() {
		return alwaysSendLetter;
	}
	
	public void setAlwaysSendLetter(Boolean alwaysSendLetter) {
		this.alwaysSendLetter = alwaysSendLetter;
	}
	
	public String getContentCode() {
		return contentCode;
	}
	
	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}
	
	public String getLetterBody() {
		return letterBody;
	}
	
	public void setLetterBody(String letterBody) {
		this.letterBody = letterBody;
	}
	
	public String getPrintedLetterType() {
		return printedLetterType;
	}
	
	public void setPrintedLetterType(String printedLetterType) {
		this.printedLetterType = printedLetterType;
	}
	
	public Boolean getSendLetterIfNoEmail() {
		return sendLetterIfNoEmail;
	}
	
	public void setSendLetterIfNoEmail(Boolean sendLetterIfNoEmail) {
		this.sendLetterIfNoEmail = sendLetterIfNoEmail;
	}
	
	public Boolean getSendMail() {
		return sendMail;
	}
	
	public void setSendMail(Boolean sendMail) {
		this.sendMail = sendMail;
	}

	
	public File getAttachment() {
		return attachment;
	}

	
	public void setAttachment(File attachment) {
		this.attachment = attachment;
	}

}
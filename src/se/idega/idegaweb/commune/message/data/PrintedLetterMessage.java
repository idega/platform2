/*
 * $Id: PrintedLetterMessage.java 1.1 7.10.2004 aron Exp $
 * Created on 7.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.data;



import com.idega.block.process.data.Case;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 7.10.2004 10:56:40 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface PrintedLetterMessage extends IDOEntity, PrintMessage, Case {
    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getCaseCodeKey
     */
    public String getCaseCodeKey();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getCaseCodeDescription
     */
    public String getCaseCodeDescription();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getSender
     */
    public User getSender();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#setSender
     */
    public void setSender(User sender);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getSenderID
     */
    public int getSenderID();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#setSenderID
     */
    public void setSenderID(int senderID);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#setSubject
     */
    public void setSubject(String subject);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getSubject
     */
    public String getSubject();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#setBody
     */
    public void setBody(String body);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getBody
     */
    public String getBody();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getMessageType
     */
    public String getMessageType();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#setMessageType
     */
    public void setMessageType(String type);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getMessageData
     */
    public ICFile getMessageData();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getMessageDataFileID
     */
    public int getMessageDataFileID();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#setMessageData
     */
    public void setMessageData(ICFile file);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#setMessageData
     */
    public void setMessageData(int fileID);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getContentCode
     */
    public String getContentCode();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#setContentCode
     */
    public void setContentCode(String contentCode);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getMessageBulkData
     */
    public ICFile getMessageBulkData();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getMessageBulkDataFileID
     */
    public int getMessageBulkDataFileID();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#setMessageBulkData
     */
    public void setMessageBulkData(ICFile file);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#setMessageBulkData
     */
    public void setMessageBulkData(int fileID);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getSenderName
     */
    public String getSenderName();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getDateString
     */
    public String getDateString();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#setLetterType
     */
    public void setLetterType(String letterType);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getLetterType
     */
    public String getLetterType();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#setAsPasswordLetter
     */
    public void setAsPasswordLetter();

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getUnPrintedCaseStatusForType
     */
    public String getUnPrintedCaseStatusForType(String type);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getPrintedCaseStatusForType
     */
    public String getPrintedCaseStatusForType(String type);

    /**
     * @see se.idega.idegaweb.commune.message.data.PrintedLetterMessageBMPBean#getPrintType
     */
    public String getPrintType();

}

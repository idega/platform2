/*
 * $Id: SystemArchivationMessage.java 1.1 7.10.2004 aron Exp $
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
 *  Last modified: $Date: 7.10.2004 11:26:08 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface SystemArchivationMessage extends IDOEntity, PrintMessage, Case {
    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getCaseCodeKey
     */
    public String getCaseCodeKey();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getCaseCodeDescription
     */
    public String getCaseCodeDescription();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#setSubject
     */
    public void setSubject(String subject);

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getSubject
     */
    public String getSubject();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#setBody
     */
    public void setBody(String body);

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getBody
     */
    public String getBody();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getMessageType
     */
    public String getMessageType();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#setMessageType
     */
    public void setMessageType(String type);

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getContentCode
     */
    public String getContentCode();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#setContentCode
     */
    public void setContentCode(String contentCode);

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getMessageData
     */
    public ICFile getMessageData();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getMessageDataFileID
     */
    public int getMessageDataFileID();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#setMessageData
     */
    public void setMessageData(ICFile file);

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#setMessageData
     */
    public void setMessageData(int fileID);

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#setAttachedFile
     */
    public void setAttachedFile(ICFile file);

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#setAttachedFile
     */
    public void setAttachedFile(int fileID);

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getAttachedFile
     */
    public ICFile getAttachedFile();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getAttachedFileID
     */
    public int getAttachedFileID();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getMessageBulkData
     */
    public ICFile getMessageBulkData();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getMessageBulkDataFileID
     */
    public int getMessageBulkDataFileID();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#setMessageBulkData
     */
    public void setMessageBulkData(ICFile file);

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#setMessageBulkData
     */
    public void setMessageBulkData(int fileID);

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getSender
     */
    public User getSender();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#setSender
     */
    public void setSender(User sender);

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getSenderID
     */
    public int getSenderID();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#setSenderID
     */
    public void setSenderID(int senderID);

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getSenderName
     */
    public String getSenderName();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getDateString
     */
    public String getDateString();

    /**
     * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#getPrintType
     */
    public String getPrintType();

}

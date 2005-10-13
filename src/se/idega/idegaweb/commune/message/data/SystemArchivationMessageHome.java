/*
 * $Id: SystemArchivationMessageHome.java 1.1 Oct 12, 2005 laddi Exp $
 * Created on Oct 12, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.process.message.data.MessageHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface SystemArchivationMessageHome extends MessageHome {

	/**
	 * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbFindMessages
	 */
	public Collection findMessages(User user) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbFindMessagesByStatus
	 */
	public Collection findMessagesByStatus(User user, String[] status) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbFindPrintedMessages
	 */
	public Collection findPrintedMessages() throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbFindUnPrintedMessages
	 */
	public Collection findUnPrintedMessages() throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbFindPrintedMessages
	 */
	public Collection findPrintedMessages(IWTimestamp from, IWTimestamp to) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbFindUnPrintedMessages
	 */
	public Collection findUnPrintedMessages(IWTimestamp from, IWTimestamp to) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbHomeGetNumberOfUnPrintedMessages
	 */
	public int getNumberOfUnPrintedMessages();

	/**
	 * @see se.idega.idegaweb.commune.message.data.SystemArchivationMessageBMPBean#ejbHomeGetPrintMessageTypes
	 */
	public String[] getPrintMessageTypes();
}
/*
 * $Id: IWBundleStarter.java,v 1.1 2005/10/13 18:36:11 laddi Exp $
 * Created on Oct 12, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune;

import se.idega.idegaweb.commune.message.business.MessageConstants;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.SystemArchivationMessage;
import se.idega.idegaweb.commune.message.data.UserMessage;
import com.idega.block.process.message.business.MessageTypeManager;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;


/**
 * Last modified: $Date: 2005/10/13 18:36:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class IWBundleStarter implements IWBundleStartable {

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#start(com.idega.idegaweb.IWBundle)
	 */
	public void start(IWBundle starterBundle) {
		MessageTypeManager manager = MessageTypeManager.getInstance();
		manager.addDataClassForType(MessageConstants.TYPE_USER_MESSAGE, UserMessage.class);
		manager.addDataClassForType(MessageConstants.TYPE_SYSTEM_PRINT_MAIL_MESSAGE, PrintedLetterMessage.class);
		manager.addDataClassForType(MessageConstants.TYPE_SYSTEM_PRINT_ARCHIVATION_MESSAGE, SystemArchivationMessage.class);
	}

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#stop(com.idega.idegaweb.IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
	}
}

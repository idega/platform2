/*
 * $Id: MessageValue.java,v 1.1 2004/10/12 08:32:54 aron Exp $
 * Created on 7.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.business;

import com.idega.block.process.data.Case;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/10/12 08:32:54 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class MessageValue {
    Case parentCase; 
    User receiver; 
    User sender; 
    Group handler;
    String subject; 
    String body;
    String letterBody;
    Boolean sendLetterIfNoEmail;
    String contentCode;
    Boolean alwaysSendLetter; 
    Boolean sendMail;
    protected String messageType;
    protected String printedLetterType;

}

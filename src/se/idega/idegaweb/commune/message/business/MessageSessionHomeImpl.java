/*
 * $Id: MessageSessionHomeImpl.java 1.1 2.11.2004 aron Exp $
 * Created on 2.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.business;


import com.idega.business.IBOHomeImpl;

/**
 * 
 *  Last modified: $Date: 2.11.2004 21:01:51 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class MessageSessionHomeImpl extends IBOHomeImpl implements
        MessageSessionHome {
    protected Class getBeanInterfaceClass() {
        return MessageSession.class;
    }

    public MessageSession create() throws javax.ejb.CreateException {
        return (MessageSession) super.createIBO();
    }

}

/*
 * $Id: MessageBusinessHome.java,v 1.4 2004/11/02 21:22:41 aron Exp $
 * Created on 2.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.business;




import com.idega.business.IBOHome;

/**
 * 
 *  Last modified: $Date: 2004/11/02 21:22:41 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.4 $
 */
public interface MessageBusinessHome extends IBOHome {
    public MessageBusiness create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}

/*
 * $Id: MessageContentServiceHome.java,v 1.1 2004/10/11 13:35:42 aron Exp $
 * Created on 8.10.2004
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
 *  Last modified: $Date: 2004/10/11 13:35:42 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface MessageContentServiceHome extends IBOHome {
    public MessageContentService create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}

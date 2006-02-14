/*
 * $Id: CommuneMessageBusinessHome.java,v 1.1.2.1 2006/02/14 17:50:54 palli Exp $
 * Created on Feb 13, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.message.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO laddi Describe Type CommuneMessageBusinessHome
 * </p>
 *  Last modified: $Date: 2006/02/14 17:50:54 $ by $Author: palli $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1.2.1 $
 */
public interface CommuneMessageBusinessHome extends IBOHome {

	public CommuneMessageBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

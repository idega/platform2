/*
 * $Id: CommuneUserBusinessHome.java,v 1.3 2005/04/06 09:28:16 laddi Exp $
 * Created on 6.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO laddi Describe Type CommuneUserBusinessHome
 * </p>
 *  Last modified: $Date: 2005/04/06 09:28:16 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface CommuneUserBusinessHome extends IBOHome {

	public CommuneUserBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

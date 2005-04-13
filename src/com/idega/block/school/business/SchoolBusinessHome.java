/*
 * $Id: SchoolBusinessHome.java,v 1.6 2005/04/13 09:53:44 laddi Exp $
 * Created on 13.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO laddi Describe Type SchoolBusinessHome
 * </p>
 *  Last modified: $Date: 2005/04/13 09:53:44 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.6 $
 */
public interface SchoolBusinessHome extends IBOHome {

	public SchoolBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

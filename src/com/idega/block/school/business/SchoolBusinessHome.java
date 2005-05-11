/*
 * $Id: SchoolBusinessHome.java,v 1.9 2005/05/11 07:14:19 laddi Exp $
 * Created on May 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/05/11 07:14:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.9 $
 */
public interface SchoolBusinessHome extends IBOHome {

	public SchoolBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

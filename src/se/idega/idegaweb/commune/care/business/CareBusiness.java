/*
 * $Id: CareBusiness.java,v 1.2 2004/10/14 13:42:44 thomas Exp $
 * Created on Oct 13, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import javax.ejb.FinderException;
import com.idega.block.school.data.School;
import com.idega.business.IBOService;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/10/14 13:42:44 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public interface CareBusiness extends IBOService {

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getProviderForUser
	 */
	public School getProviderForUser(User user) throws FinderException, java.rmi.RemoteException;

}

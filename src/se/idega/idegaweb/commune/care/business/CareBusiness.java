/*
 * $Id: CareBusiness.java,v 1.3 2004/10/14 13:56:32 thomas Exp $
 * Created on Oct 14, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.idega.block.school.data.School;
import com.idega.business.IBOService;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/10/14 13:56:32 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.3 $
 */
public interface CareBusiness extends IBOService {

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getProviderForUser
	 */
	public School getProviderForUser(User user) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#hasGrantedCheck
	 */
	public boolean hasGrantedCheck(User child) throws RemoteException;
}

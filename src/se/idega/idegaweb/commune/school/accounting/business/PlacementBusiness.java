/*
 * $Id: PlacementBusiness.java,v 1.1 2004/10/15 14:45:13 thomas Exp $
 * Created on Oct 15, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.accounting.business;

import java.rmi.RemoteException;
import se.idega.idegaweb.commune.school.business.CentralPlacementException;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOService;
import com.idega.presentation.IWContext;


/**
 * 
 *  Last modified: $Date: 2004/10/15 14:45:13 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface PlacementBusiness extends IBOService {

	/**
	 * @see se.idega.idegaweb.commune.school.accounting.business.PlacementBusinessBean#storeSchoolClassMember
	 */
	public SchoolClassMember storeSchoolClassMember(IWContext iwc, int childID) throws RemoteException,
			CentralPlacementException;
}

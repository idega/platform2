/*
 * $Id: CareInvoiceBusiness.java,v 1.1 2004/10/20 17:05:09 thomas Exp $
 * Created on Oct 20, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import java.rmi.RemoteException;
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import com.idega.business.IBOService;


/**
 * 
 *  Last modified: $Date: 2004/10/20 17:05:09 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface CareInvoiceBusiness extends IBOService {
	
	void removeInvoiceRecords(ChildCareContract contract) throws RemoveException, RemoteException;
}

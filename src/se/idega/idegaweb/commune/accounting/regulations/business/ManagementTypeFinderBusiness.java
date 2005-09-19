/*
 * $Id: ManagementTypeFinderBusiness.java,v 1.1 2005/09/19 22:03:10 palli Exp $
 * Created on Sep 19, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.regulations.business;

import java.util.Collection;
import com.idega.business.IBOService;


/**
 * <p>
 * TODO thomas Describe Type EmploymentTypeFinderBusiness
 * </p>
 *  Last modified: $Date: 2005/09/19 22:03:10 $ by $Author: palli $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface ManagementTypeFinderBusiness extends IBOService {

	public Collection findAllManagementTypes() throws java.rmi.RemoteException;

}

/*
 * $Id: VacationBusinessHome.java,v 1.1 2004/11/25 14:22:35 anna Exp $
 * Created on 24.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.business;

import com.idega.business.IBOHome;


/**
 * Last modified: 24.11.2004 09:27:10 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public interface VacationBusinessHome extends IBOHome {

	public VacationBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

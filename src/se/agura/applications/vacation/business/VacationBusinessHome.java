/*
 * $Id: VacationBusinessHome.java,v 1.4 2005/02/14 14:54:53 laddi Exp $
 * Created on 14.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.business;




import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/02/14 14:54:53 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public interface VacationBusinessHome extends IBOHome {

	public VacationBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}

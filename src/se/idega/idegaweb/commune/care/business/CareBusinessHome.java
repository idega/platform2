/*
 * $Id: CareBusinessHome.java,v 1.6 2005/10/13 19:13:13 laddi Exp $
 * Created on Oct 13, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/10/13 19:13:13 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.6 $
 */
public interface CareBusinessHome extends IBOHome {

	public CareBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

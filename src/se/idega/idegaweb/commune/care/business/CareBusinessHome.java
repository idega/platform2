/*
 * $Id: CareBusinessHome.java,v 1.3 2004/10/19 10:33:42 thomas Exp $
 * Created on Oct 18, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2004/10/19 10:33:42 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.3 $
 */
public interface CareBusinessHome extends IBOHome {

	public CareBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

/*
 * $Id: PlacementBusinessHome.java,v 1.1 2004/10/15 14:45:13 thomas Exp $
 * Created on Oct 15, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.accounting.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2004/10/15 14:45:13 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface PlacementBusinessHome extends IBOHome {

	public PlacementBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

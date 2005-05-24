/*
 * $Id: RunBusinessHome.java,v 1.3 2005/05/24 12:06:30 laddi Exp $
 * Created on May 24, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/05/24 12:06:30 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface RunBusinessHome extends IBOHome {

	public RunBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

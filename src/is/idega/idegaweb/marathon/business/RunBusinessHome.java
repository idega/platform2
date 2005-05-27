/*
 * $Id: RunBusinessHome.java,v 1.5 2005/05/27 09:06:49 laddi Exp $
 * Created on May 27, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/05/27 09:06:49 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
 */
public interface RunBusinessHome extends IBOHome {

	public RunBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

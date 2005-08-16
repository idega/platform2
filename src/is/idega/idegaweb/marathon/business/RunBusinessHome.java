/*
 * $Id: RunBusinessHome.java,v 1.11 2005/08/16 14:09:36 laddi Exp $
 * Created on Aug 16, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/08/16 14:09:36 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.11 $
 */
public interface RunBusinessHome extends IBOHome {

	public RunBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

/*
 * $Id: FamilyLogicHome.java,v 1.4 2005/05/22 16:30:52 laddi Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/05/22 16:30:52 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public interface FamilyLogicHome extends IBOHome {

	public FamilyLogic create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

/*
 * $Id: FamilyLogicHome.java,v 1.2 2004/09/01 11:14:49 joakim Exp $
 * Created on 31.8.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2004/09/01 11:14:49 $ by $Author: joakim $
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision: 1.2 $
 */
public interface FamilyLogicHome extends IBOHome {

	public FamilyLogic create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

/*
 * $Id$
 * Created on 14.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.nationalregister.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision$
 */
public interface NationalRegisterBusinessHome extends IBOHome {

	public NationalRegisterBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}

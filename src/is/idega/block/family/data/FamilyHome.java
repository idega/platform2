/*
 * $Id: FamilyHome.java,v 1.1 2004/08/27 16:15:24 joakim Exp $
 * Created on 27.8.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * 
 *  Last modified: $Date: 2004/08/27 16:15:24 $ by $Author: joakim $
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision: 1.1 $
 */
public interface FamilyHome extends IDOHome {

	public Family create() throws javax.ejb.CreateException, java.rmi.RemoteException;

	public Family findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.data.FamilyBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;
}

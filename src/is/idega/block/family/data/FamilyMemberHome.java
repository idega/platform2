/*
 * $Id: FamilyMemberHome.java,v 1.1 2004/09/01 11:14:49 joakim Exp $
 * Created on 31.8.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.data;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/09/01 11:14:49 $ by $Author: joakim $
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision: 1.1 $
 */
public interface FamilyMemberHome extends IDOHome {

	public FamilyMember create() throws javax.ejb.CreateException, java.rmi.RemoteException;

	public FamilyMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#ejbFindAllByFamilyNR
	 */
	public Collection findAllByFamilyNR(String familyNr) throws FinderException, RemoteException;

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#ejbFindForUser
	 */
	public FamilyMember findForUser(User user) throws FinderException;
}

/*
 * $Id: FamilyMemberHomeImpl.java,v 1.1 2004/09/01 11:14:49 joakim Exp $
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
import com.idega.data.IDOFactory;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/09/01 11:14:49 $ by $Author: joakim $
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision: 1.1 $
 */
public class FamilyMemberHomeImpl extends IDOFactory implements FamilyMemberHome {

	protected Class getEntityInterfaceClass() {
		return FamilyMember.class;
	}

	public FamilyMember create() throws javax.ejb.CreateException {
		return (FamilyMember) super.createIDO();
	}

	public FamilyMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (FamilyMember) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((FamilyMemberBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByFamilyNR(String familyNr) throws FinderException, RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((FamilyMemberBMPBean) entity).ejbFindAllByFamilyNR(familyNr);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public FamilyMember findForUser(User user) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((FamilyMemberBMPBean) entity).ejbFindForUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}

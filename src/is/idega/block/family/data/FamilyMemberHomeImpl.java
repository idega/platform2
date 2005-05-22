/*
 * $Id: FamilyMemberHomeImpl.java,v 1.3 2005/05/22 16:30:52 laddi Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/05/22 16:30:52 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
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

	public Collection findAllByFamilyNR(String familyNr) throws FinderException {
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

	public FamilyMember findBySSN(String ssn) throws IDORelationshipException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((FamilyMemberBMPBean) entity).ejbFindBySSN(ssn);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}

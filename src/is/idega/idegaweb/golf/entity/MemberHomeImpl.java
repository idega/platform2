/*
 * $Id: MemberHomeImpl.java,v 1.8 2005/02/08 10:10:38 laddi Exp $
 * Created on 8.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.entity;


import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/02/08 10:10:38 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.8 $
 */
public class MemberHomeImpl extends IDOFactory implements MemberHome {

	protected Class getEntityInterfaceClass() {
		return Member.class;
	}

	public Member create() throws javax.ejb.CreateException {
		return (Member) super.createIDO();
	}

	public Member findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Member) super.findByPrimaryKeyIDO(pk);
	}

	public Member createLegacy() {
		try {
			return create();
		}
		catch (javax.ejb.CreateException ce) {
			throw new RuntimeException("CreateException:" + ce.getMessage());
		}
	}

	public Member findByPrimaryKey(int id) throws javax.ejb.FinderException {
		return (Member) super.findByPrimaryKeyIDO(id);
	}

	public Member findByPrimaryKeyLegacy(int id) throws java.sql.SQLException {
		try {
			return findByPrimaryKey(id);
		}
		catch (javax.ejb.FinderException fe) {
			throw new java.sql.SQLException("FinderException:" + fe.getMessage());
		}
	}

	public Member findMemberByIWMemberSystemUser(User user) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((MemberBMPBean) entity).ejbFindMemberByIWMemberSystemUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Member findBySSN(String ssn) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((MemberBMPBean) entity).ejbFindBySSN(ssn);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Member findByUniqueID(String uniqueID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((MemberBMPBean) entity).ejbFindByUniqueID(uniqueID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MemberBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByUnion(Union union, String gender) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((MemberBMPBean) entity).ejbFindAllByUnion(union, gender);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}

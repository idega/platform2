/*
 * $Id: MemberHome.java,v 1.7 2005/02/07 11:20:28 laddi Exp $
 * Created on 7.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.entity;


import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/02/07 11:20:28 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
 */
public interface MemberHome extends IDOHome {

	public Member create() throws javax.ejb.CreateException;

	public Member findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	public Member findByPrimaryKey(int id) throws javax.ejb.FinderException;

	public Member findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#ejbFindMemberByIWMemberSystemUser
	 */
	public Member findMemberByIWMemberSystemUser(User user) throws FinderException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#ejbFindBySSN
	 */
	public Member findBySSN(String ssn) throws FinderException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.golf.entity.MemberBMPBean#ejbFindAllByUnion
	 */
	public Collection findAllByUnion(Union union, String gender) throws FinderException;

}

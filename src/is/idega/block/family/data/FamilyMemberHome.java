/*
 * $Id: FamilyMemberHome.java,v 1.3 2005/05/22 16:30:52 laddi Exp $
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
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/05/22 16:30:52 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface FamilyMemberHome extends IDOHome {

	public FamilyMember create() throws javax.ejb.CreateException;

	public FamilyMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#ejbFindAllByFamilyNR
	 */
	public Collection findAllByFamilyNR(String familyNr) throws FinderException;

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#ejbFindForUser
	 */
	public FamilyMember findForUser(User user) throws FinderException;

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#ejbFindBySSN
	 */
	public FamilyMember findBySSN(String ssn) throws IDORelationshipException, FinderException;
}

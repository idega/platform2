/*
 * $Id: FamilyMember.java,v 1.1 2004/09/01 11:14:49 joakim Exp $
 * Created on 31.8.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.data;

import com.idega.data.IDOEntity;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/09/01 11:14:49 $ by $Author: joakim $
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision: 1.1 $
 */
public interface FamilyMember extends IDOEntity {

	public static final int MOTHER = 1;
	public static final int FATHER = 2;
	public static final int CHILD = 3;
	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#setFamilyNr
	 */
	public void setFamilyNr(String familyNr);

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#setRole
	 */
	public void setRole(int role);

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#getFamilyNr
	 */
	public String getFamilyNr();

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.block.family.data.FamilyMemberBMPBean#getRole
	 */
	public int getRole();
}

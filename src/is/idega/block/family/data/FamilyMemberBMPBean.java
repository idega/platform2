/*
 * $Id: FamilyMemberBMPBean.java,v 1.1 2004/09/01 11:14:49 joakim Exp $ Created on 27.8.2004
 * 
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package is.idega.block.family.data;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

/**
 * 
 * Last modified: $Date: 2004/09/01 11:14:49 $ by $Author: joakim $
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim </a>
 * @version $Revision: 1.1 $
 */
public class FamilyMemberBMPBean extends GenericEntity implements FamilyMember{

	private static final String ENTITY_NAME = "fam_family_member";

	private static final String COLUMN_FAMILY_NR = "family_nr";
	private final static String COLUMN_USER = "ic_user";
	private final static String COLUMN_ROLE = "role";
	public static final int MOTHER = 1;
	public static final int FATHER = 2;
	public static final int CHILD = 3;

	/*
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/*
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COLUMN_FAMILY_NR, "Family Number", true, true, java.lang.String.class);
		addManyToOneRelationship(COLUMN_USER, User.class);
		addAttribute(COLUMN_ROLE,"role",true,true,java.lang.Integer.class);
	}
	
	public void setFamilyNr(String familyNr) {
		setColumn(COLUMN_FAMILY_NR, familyNr);
	}

	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}

	public void setRole(int role ) {
		setColumn(COLUMN_ROLE, role);
	}

	public String getFamilyNr() {
		return getStringColumnValue(COLUMN_FAMILY_NR);	
	}
	
	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);	
	}

	public int getRole() {
		return  getIntColumnValue(COLUMN_ROLE);	
	}

	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindAllByFamilyNR(String familyNr) throws FinderException, RemoteException {
		IDOQuery sql = idoQueryGetSelect();
		sql.appendWhereEqualsQuoted(COLUMN_FAMILY_NR, familyNr);
		return this.idoFindPKsBySQL(sql.toString());
	}

	public Integer ejbFindForUser(User user) throws FinderException {
		String userPK = user.getPrimaryKey().toString();
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEqualsQuoted("ic_user_id", userPK);
		return (Integer) idoFindOnePKByQuery(query);
	}
}
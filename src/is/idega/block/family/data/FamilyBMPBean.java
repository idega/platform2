/*
 * $Id: FamilyBMPBean.java,v 1.1 2004/08/27 16:15:24 joakim Exp $ Created on 27.8.2004
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
 * Last modified: $Date: 2004/08/27 16:15:24 $ by $Author: joakim $
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim </a>
 * @version $Revision: 1.1 $
 */
public class FamilyBMPBean extends GenericEntity implements Family {

	private static final String ENTITY_NAME = "fam_family";

	private static final String COLUMN_FAMILY_NR = "family_nr";

	public final static String SQL_RELATION_USER = "IC_USER";

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
		addManyToOneRelationship(SQL_RELATION_USER, User.class);
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
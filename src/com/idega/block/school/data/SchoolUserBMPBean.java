package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

/**
 * @author Gimmi
 */
public class SchoolUserBMPBean extends GenericEntity implements SchoolUser{

	private final String COLUMN_NAME_USER_TYPE = "USER_TYPE";
	private final String COLUMN_NAME_SCHOOL_ID = "SCH_SCHOOL_ID";
	private final String COLUMN_NAME_USER_ID = "IC_USER_ID";
	private final String TABLE_NAME = "sch_school_user";

	public static final int USER_TYPE_HEADMASTER = 0;
	public static final int USER_TYPE_ASSISTANT_HEADMASTER = 1;
	public static final int USER_TYPE_TEACHER = 2;
	public static final int USER_TYPE_WEB_ADMIN = 3;


	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return TABLE_NAME;
	}

	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		this.addAttribute(getIDColumnName());
		this.addAttribute(COLUMN_NAME_SCHOOL_ID, "school id", true, true, Integer.class, this.ONE_TO_MANY, School.class);
		this.addAttribute(COLUMN_NAME_USER_ID, "user id", true, true, Integer.class, ONE_TO_MANY, User.class);
		this.addAttribute(COLUMN_NAME_USER_TYPE, "user type", true, true, Integer.class);
	}

	public void setSchoolId(int schoolId) {
		this.setColumn(COLUMN_NAME_SCHOOL_ID, schoolId);	
	}
	
	public int getSchoolId() {
		return getIntColumnValue(COLUMN_NAME_SCHOOL_ID);
	}
	
	public void setUserId(int userId) {
		setColumn(COLUMN_NAME_USER_ID, userId);	
	}
	
	public int getUserId() {
		return getIntColumnValue(COLUMN_NAME_USER_ID);	
	}
	
	public User getUser() {
		return (User) getColumnValue(COLUMN_NAME_USER_ID);	
	}
	
	public void setUserType(int userType) {
		setColumn(COLUMN_NAME_USER_TYPE, userType);	
	}
	
	public int getUserType() {
		return getIntColumnValue(COLUMN_NAME_USER_TYPE);	
	}

	/**
	 * Returns a Collection of UserPrimaryKeys NOT SchoolUserPrimaryKeys	 * @param school School	 * @param userType User type	 * @return Collection	 * @throws FinderException	 * @throws RemoteException
	 */
	public Collection ejbHomeFindBySchoolAndType(School school, int userType) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
			sql.appendSelect()
			 .append("*")
			 .appendFrom()
			 .append(TABLE_NAME)
			 .appendWhere()
			 .append(COLUMN_NAME_SCHOOL_ID)
			 .appendEqualSign()
			 .append(school.getPrimaryKey().toString())
			 .appendAnd()
			 .append(COLUMN_NAME_USER_TYPE)
			 .appendEqualSign()
			 .append(userType);
			 
			 /** THARF AD SKILA USERUM ...*/
		Collection coll = this.idoFindIDsBySQL(sql.toString());
		Collection userPks = new Vector();
		
		SchoolUser sUser;
		Iterator iter = coll.iterator();
		while (iter.hasNext()) {
			sUser = getHome().findByPrimaryKey(iter.next());
			userPks.add(new Integer(sUser.getUserId()));
		}
				
		return userPks;
	}
	
	public Collection ejbHomeFindByUser(User user) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this)
		.appendWhere()
		.append(COLUMN_NAME_USER_ID)
		.appendEqualSign()
		.append(user.getPrimaryKey().toString());	
		
		return this.idoFindIDsBySQL(sql.toString());
	}
	
	public Collection ejbHomeFindBySchoolAndUser(School school, User user) throws FinderException {
		IDOQuery sql = idoQuery();
			sql.appendSelect()
			 .append("*")
			 .appendFrom()
			 .append(TABLE_NAME)
			 .appendWhere()
			 .append(COLUMN_NAME_SCHOOL_ID)
			 .appendEqualSign()
			 .append(school.getPrimaryKey().toString())
			 .appendAnd()
			 .append(COLUMN_NAME_USER_ID)
			 .appendEqualSign()
			 .append(user.getPrimaryKey().toString());
		return this.idoFindIDsBySQL(sql.toString());		
	}
	
	public Collection ejbHomeFindBySchool(School school) throws FinderException {
		IDOQuery sql = idoQuery();
			sql.appendSelect()
			 .append("*")
			 .appendFrom()
			 .append(TABLE_NAME)
			 .appendWhere()
			 .append(COLUMN_NAME_SCHOOL_ID)
			 .appendEqualSign()
			 .append(school.getPrimaryKey().toString());
		return this.idoFindIDsBySQL(sql.toString());			
	}
	
	public Object ejbHomeGetSchoolUserId(School school, User user, int userType) throws FinderException{
		IDOQuery sql = idoQuery();
			sql.appendSelect()
			 .append("*")
			 .appendFrom()
			 .append(TABLE_NAME)
			 .appendWhere()
			 .append(COLUMN_NAME_SCHOOL_ID)
			 .appendEqualSign()
			 .append(school.getPrimaryKey().toString())
			 .appendAnd()
			 .append(COLUMN_NAME_USER_TYPE)
			 .appendEqualSign()
			 .append(userType)
			 .appendAnd()
			 .append(COLUMN_NAME_USER_ID)
			 .appendEqualSign()
			 .append(user.getPrimaryKey().toString());
		return this.idoFindOnePKByQuery(sql);
	}

	public SchoolUserHome getHome() throws RemoteException {
		return (SchoolUserHome) IDOLookup.getHome(SchoolUser.class);	
	}
	
}

package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.User;

/**
 * @author Gimmi
 */
public class SchoolUserBMPBean extends GenericEntity implements SchoolUser{

	private final String COLUMN_NAME_USER_TYPE = "USER_TYPE";
	private final String COLUMN_NAME_SCHOOL_ID = "SCH_SCHOOL_ID";
	private final String COLUMN_NAME_USER_ID = "IC_USER_ID";
	private final String TABLE_NAME = "sch_school_user";
	private final String COLUMN_NAME_SHOW_IN_CONTACTS = "show_in_contact";
	private final String COLUMN_NAME_MAIN_HEADMASTER = "main_headmaster";
	private final String COLUMN_NAME_ECONOMY_RESP = "economy_resp";
	

	public static final int USER_TYPE_HEADMASTER = 0;
	public static final int USER_TYPE_ASSISTANT_HEADMASTER = 1;
	public static final int USER_TYPE_TEACHER = 2;
	public static final int USER_TYPE_WEB_ADMIN = 3;
	public static final int USER_TYPE_IB_COORDINATOR = 4;
	public static final int USER_TYPE_STUDY_AND_WORK_COUNCEL = 5;
	public static final int USER_TYPE_SCHOOL_MASTER = 6;
	public static final int USER_TYPE_CONTACT_PERSON = 7;
	public static final int USER_TYPE_EXPEDITION = 8;
	public static final int USER_TYPE_PROJECT_MANAGER = 9;


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
		this.addAttribute(COLUMN_NAME_SCHOOL_ID, "school id", true, true, Integer.class, ONE_TO_MANY, School.class);
		this.addAttribute(COLUMN_NAME_USER_ID, "user id", true, true, Integer.class, ONE_TO_MANY, User.class);
		this.addAttribute(COLUMN_NAME_USER_TYPE, "user type", true, true, Integer.class);
		this.addAttribute(COLUMN_NAME_SHOW_IN_CONTACTS, "show in contacts", true, true, Boolean.class);
		this.addAttribute(COLUMN_NAME_MAIN_HEADMASTER, "main headmaster", true, true, Boolean.class);	
		this.addAttribute(COLUMN_NAME_ECONOMY_RESP, "economical responsible", true, true, Boolean.class);
		
		this.addManyToManyRelationShip(School.class);
	}
	
	public Collection getSchools() throws IDORelationshipException {
		return idoGetRelatedEntities(School.class);
	}
	
	public void addSchools(Collection schools) throws IDOAddRelationshipException {
		Iterator iter = schools.iterator();
		while (iter.hasNext()) {
			School school = (School) iter.next();
			idoAddTo(school);
		}
	}
	
	public void removeSchool(School school) throws IDORemoveRelationshipException {
		idoRemoveFrom(school);
	}
	
	public void removeSchools() throws IDORemoveRelationshipException {
		idoRemoveFrom(School.class);
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

	public void setMainHeadmaster(boolean mainHead) {
		setColumn(COLUMN_NAME_MAIN_HEADMASTER, mainHead);	
	}
	
	public boolean getMainHeadmaster() {
		return getBooleanColumnValue(COLUMN_NAME_MAIN_HEADMASTER);	
	}
	
	public void setIsEconomicalResponsible(boolean b) {
		setColumn(COLUMN_NAME_ECONOMY_RESP, b);	
	}
	
	public boolean isEconomicalResponsible() {
		return getBooleanColumnValue(COLUMN_NAME_ECONOMY_RESP, false);	
	}
	
	public void setShowInContact(boolean showinContacts) {
			setColumn(COLUMN_NAME_SHOW_IN_CONTACTS, showinContacts);	
		}
	
	public boolean getShowInContact() {
		return getBooleanColumnValue(COLUMN_NAME_SHOW_IN_CONTACTS);	
	}

	/**
	 * Returns a Collection of SchoolUsers	 * @param school School	 * @param userType User type	 * @return Collection	 * @throws FinderException
	 */
	public Collection ejbFindBySchoolAndType(School school, int userType) throws FinderException {
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
		return coll;
	}
    
    /**
     * Returns a Collection of SchoolUsers
     * @param school School
     * @param userType User type
     * @return Collection
     * @throws FinderException
     */
    public Collection ejbFindByTypes(int []userTypes) throws FinderException {
        StringBuffer query = new StringBuffer();
        query.append("select * ");
        query.append("from   sch_school_user, ic_user ");
        query.append("where ");
        query.append("      ic_user.ic_user_id = sch_school_user.ic_user_id ");        
        
        if (userTypes.length > 0) {
            query.append(" and (");
            for (int i = 0; i < userTypes.length; i++) {
                if(i != 0) {
                    query.append(" or ");                   
                }
                query.append(" sch_school_user.user_type = " + userTypes[i]);
            }
            query.append(") ");
        }
        
        query.append("order by ");
        query.append("  ic_user.last_name asc, ic_user.first_name asc ");            

        Collection coll = this.idoFindIDsBySQL(query.toString());       
        return coll;

    }    
	
	public Collection ejbFindBySchoolAndTypes(School school, int[] userTypes) throws FinderException {
		StringBuffer query = new StringBuffer();
		query.append("select * ");
		query.append("from   sch_school_user, ic_user ");
		query.append("where ");
		query.append("		ic_user.ic_user_id = sch_school_user.ic_user_id ");
		query.append("	and sch_school_user.sch_school_id=" + school.getPrimaryKey().toString() + " ");
		
		if (userTypes.length > 0) {
			query.append(" and (");
			for (int i = 0; i < userTypes.length; i++) {
				if(i != 0) {
					query.append(" or ");					
				}
				query.append(" sch_school_user.user_type = " + userTypes[i]);
			}
			query.append(") ");
		}
		
		query.append("order by ");
		query.append("	ic_user.last_name asc, ic_user.first_name asc ");			 

		Collection coll = this.idoFindIDsBySQL(query.toString());		
		return coll;
	}

	public Collection ejbFindBySchoolAndIsEconomicalResponsible (School school)
		throws FinderException {
		final IDOQuery sql = idoQuery ();
		sql.appendSelectAllFrom (TABLE_NAME);
		sql.appendWhereEquals (COLUMN_NAME_SCHOOL_ID, school);
		sql.appendAndIsNotNull (COLUMN_NAME_ECONOMY_RESP);
		sql.appendAndEquals (COLUMN_NAME_ECONOMY_RESP, true);
		return idoFindIDsBySQL (sql.toString());
	}

	/**
	 * Returns a Collection of SchoolUsers
	 * @param school School
	 * @param userType User type
	 * @param departmentID int
	 * @return Collection
	 * @throws FinderException
	 */
	public Collection ejbFindBySchoolAndTypeAndDepartment(School school, int userType, int departmentID) throws FinderException {
		IDOQuery sql = idoQuery();
			//sql.appendSelect()
			 //.append("*")
			 //.appendFrom()
			 //.append(TABLE_NAME)
			sql.appendSelectAllFrom(TABLE_NAME + " su , " 
													  + SchoolDepartmentBMPBean.ENTITY_NAME + " sd, " + SchoolDepartmentBMPBean.ENTITY_NAME + "_USER sdu")
			 .appendWhere()
			 .append("su." + COLUMN_NAME_SCHOOL_ID)
			 .appendEqualSign()
			 .append(school.getPrimaryKey().toString())
			 .appendAnd()
			 .append("su." + COLUMN_NAME_USER_TYPE)
			 .appendEqualSign()
			 .append(userType)
			 .appendAnd()
			 .append("sd." + SchoolDepartmentBMPBean.DEPARTMENT_ID)
			 .appendEqualSign()
			 .append(departmentID)
			 .appendAnd()
			 .append("sd." + SchoolDepartmentBMPBean.DEPARTMENT_ID)
			 .appendEqualSign()
			 .append("sdu." + SchoolDepartmentBMPBean.DEPARTMENT_ID)
			 .appendAnd()
			 .append("su."  + getIDColumnName())
			 .appendEqualSign()
			 .append("sdu." + getIDColumnName());
			 
			 	 
			 /** THARF AD SKILA USERUM ...*/
		Collection coll = this.idoFindIDsBySQL(sql.toString());
		return coll;
	}
	
	/**
		 * Returns a Collection of SchoolUsers
		 * @param school School
		 * @param departmentID int
		 * @return Collection
		 * @throws FinderException
		 */
		public Collection ejbFindBySchoolAndDepartment(School school, int departmentID) throws FinderException {
			IDOQuery sql = idoQuery();
				
				sql.appendSelectAllFrom(TABLE_NAME + " su , " 
														  + SchoolDepartmentBMPBean.ENTITY_NAME + " sd, " + SchoolDepartmentBMPBean.ENTITY_NAME + "_USER sdu")
				 .appendWhere()
				 .append("su." + COLUMN_NAME_SCHOOL_ID)
				 .appendEqualSign()
				 .append(school.getPrimaryKey().toString())
				 .appendAnd()
				 .append("sd." + SchoolDepartmentBMPBean.DEPARTMENT_ID)
				 .appendEqualSign()
				 .append(departmentID)
				 .appendAnd()
				 .append("sd." + SchoolDepartmentBMPBean.DEPARTMENT_ID)
				 .appendEqualSign()
				 .append("sdu." + SchoolDepartmentBMPBean.DEPARTMENT_ID)
				 .appendAnd()
				 .append("su."  + getIDColumnName())
				 .appendEqualSign()
				 .append("sdu." + getIDColumnName());
			 			 
				 ///////
				 
				 /** THARF AD SKILA USERUM ...*/
			Collection coll = this.idoFindIDsBySQL(sql.toString());
			return coll;
		}
	
	/**
		 * Returns a Collection of SchoolUsers
		 * @param school School
		 * @param userType User type
		 * @param departmentID int
		 * @return Collection
		 * @throws FinderException
		 */
		public Collection ejbFindBySchoolAndMainHeadmaster(School school, int userType, boolean main_headmaster) throws FinderException {
			IDOQuery sql = idoQuery();
			String strMain_headmaster = "N";
			if (main_headmaster) {
				strMain_headmaster = "Y";
			} 

				sql.appendSelect()
				 .append("*")
				 .appendFrom()
				 .append(TABLE_NAME)
				 .appendWhere()
				 .append(COLUMN_NAME_SCHOOL_ID)
				 .appendEqualSign()
				 .append(school.getPrimaryKey().toString())
				 .appendAnd()
				 .append(COLUMN_NAME_MAIN_HEADMASTER)
				 .appendEqualSign()
				 .appendWithinSingleQuotes(strMain_headmaster)
				 .appendAnd()
				 .append(COLUMN_NAME_USER_TYPE)
				 .appendEqualSign()
				 .append(userType);
			return this.idoFindIDsBySQL(sql.toString());		
		}
	
	
	public Collection ejbFindByUser(User user) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this)
		.appendWhere()
		.append(COLUMN_NAME_USER_ID)
		.appendEqualSign()
		.append(user.getPrimaryKey().toString());	
		
		return this.idoFindIDsBySQL(sql.toString());
	}
	
	public Object ejbFindForUser(User user) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this)
		.appendWhere()
		.append(COLUMN_NAME_USER_ID)
		.appendEqualSign()
		.append(user.getPrimaryKey().toString());	
		
		return this.idoFindOnePKBySQL(sql.toString());
	}
	
	public Collection ejbFindBySchoolAndUser(School school, User user) throws FinderException {
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
	
	public Collection ejbFindBySchool(School school) throws FinderException {
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

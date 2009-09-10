/*
 * Created on 2003-nov-03
 *Author - Malin
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.school.data;

import java.util.Collection;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolDepartmentBMPBean extends GenericEntity implements SchoolDepartment{

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	 
	public static final String ENTITY_NAME = "sch_school_department";

	public static final String DEPARTMENT = "department";
	public static final String DEPARTMENT_ID = "department_id";
	public static final String SCHOOL_ID = "school_id";
	public static final String DESCRIPTION = "description";
	public static final String PHONE = "phone";
	
	
	public String getEntityName() {
		// TODO Auto-generated method stub
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		this.addAttribute(DEPARTMENT_ID, "Department_id", true, true, Integer.class);
		this.setAsPrimaryKey(DEPARTMENT_ID, true);
		
		addManyToOneRelationship(SCHOOL_ID,"School",School.class);
		addManyToManyRelationShip(SchoolUser.class, "sch_school_department_user");
				
		this.addAttribute(DESCRIPTION, "Description", true, true, String.class, 4000);
		this.addAttribute(PHONE, "Phone", true, true, String.class, 50);
						
	}

	public Collection findRelatedUsers() throws IDORelationshipException {
		return super.idoGetRelatedEntities(SchoolUser.class);
	}

	public void removeSchoolUser(SchoolUser user) throws IDORemoveRelationshipException {
		super.idoRemoveFrom(user);
	}

	public void removeAllSchoolUsers() throws IDORemoveRelationshipException {
		super.idoRemoveFrom(SchoolUser.class);
	}

	public void addSchoolUser(SchoolUser user) throws IDOAddRelationshipException {
		super.idoAddTo(user);
	}


	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getIDColumnName()
	 */
	public String getIDColumnName() {
		return DEPARTMENT_ID;
	}


	public int getDepartmentID(){
		return getIntColumnValue(DEPARTMENT_ID);
	  }

	public String getDepartment(){
		return getStringColumnValue(DESCRIPTION);
	  }

	public void setDepartment(String department) {
		this.setColumn(DESCRIPTION, department);
	}

	public String getDepartmentPhone(){
		return getStringColumnValue(PHONE);
	 }

	public void setSchoolID(int school_id) {
		this.setColumn(SCHOOL_ID, school_id);
	}
	
	public String getSchoolID(){
		return getStringColumnValue(SCHOOL_ID);
	 }

	public void setDepartmentPhone(String phone) {
		this.setColumn(PHONE, phone);
	}
	
	public Collection ejbFindAllDepartmentsBySchool(int schoolID) throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(SCHOOL_ID, schoolID);
		return idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindAllDepartmentsBySchool(School school) throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(SCHOOL_ID, school.getPrimaryKey());
		return idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindAllDepartmentsByUser(SchoolUser schuser) {
		try {
			IDOQuery query = idoQuery();
			query.append("Select sd.* FROM SCH_SCHOOL_DEPARTMENT sd, SCH_SCHOOL_DEPARTMENT_USER sdu ");
			query.append("WHERE sd.DEPARTMENT_ID = sdu.DEPARTMENT_ID");
			query.append(" AND sdu.SCH_SCHOOL_USER_ID = " + schuser.getPrimaryKey());
			return idoFindPKsByQuery(query);
		}
		catch (FinderException e) {
			return new Vector();
		}
		
	}
		

}

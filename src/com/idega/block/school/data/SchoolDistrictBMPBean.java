/*
 * $Id: SchoolDistrictBMPBean.java,v 1.1 2005/08/09 16:32:21 laddi Exp $
 * Created on Aug 1, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;


/**
 * Last modified: $Date: 2005/08/09 16:32:21 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class SchoolDistrictBMPBean extends GenericEntity  implements SchoolDistrict{

	private static final String ENTITY_NAME = "sch_district";
	
	private static final String COLUMN_SCHOOL = "sch_school_id";
	private static final String COLUMN_ADDRESS = "address";
	private static final String COLUMN_STREET_NUMBER = "street_number";
	private static final String COLUMN_HOUSE_NUMBER = "house_number";
	private static final String COLUMN_DISTRICT = "district";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addManyToOneRelationship(COLUMN_SCHOOL, School.class);
		
		addAttribute(COLUMN_ADDRESS, "Address", String.class);
		addAttribute(COLUMN_STREET_NUMBER, "Street number", String.class, 4);
		addAttribute(COLUMN_HOUSE_NUMBER, "House number", String.class, 4);
		addAttribute(COLUMN_DISTRICT, "District", String.class);
	}
	
	//Getters
	public School getSchool() {
		return (School) getColumnValue(COLUMN_SCHOOL);
	}
	
	public Object getSchoolPK() {
		return getIntegerColumnValue(COLUMN_SCHOOL);
	}
	
	public String getDistrict() {
		return getStringColumnValue(COLUMN_DISTRICT);
	}
	
	//Setters
	public void setSchool(School school) {
		setColumn(COLUMN_SCHOOL, school);
	}
	
	public void setSchool(Object schoolPK) {
		setColumn(COLUMN_SCHOOL, schoolPK);
	}
	
	public void setAddress(String address) {
		setColumn(COLUMN_ADDRESS, address.toUpperCase());
	}
	
	public void setStreetNumber(String streetNumber) {
		setColumn(COLUMN_STREET_NUMBER, streetNumber);
	}
	
	public void setHouseNumber(String houseNumber) {
		setColumn(COLUMN_HOUSE_NUMBER, houseNumber);
	}
	
	public void setDistrict(String district) {
		setColumn(COLUMN_DISTRICT, district);
	}
	
	//Finders
	public Object ejbFindByStreetAndHouseNumber(String streetNumber, String houseNumber) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_STREET_NUMBER, MatchCriteria.EQUALS, streetNumber));
		query.addCriteria(new MatchCriteria(table, COLUMN_HOUSE_NUMBER, MatchCriteria.EQUALS, houseNumber));
		
		return idoFindOnePKByQuery(query);
	}
	
	public Object ejbFindByAddress(String address) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_ADDRESS, MatchCriteria.EQUALS, address.toUpperCase()));
		
		return idoFindOnePKByQuery(query);
	}
	
	public Collection ejbFindAllByDistrict(String district) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table, COLUMN_DISTRICT, MatchCriteria.EQUALS, district));
		
		return idoFindPKsByQuery(query);
	}
}
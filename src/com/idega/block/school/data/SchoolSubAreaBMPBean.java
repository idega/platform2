package com.idega.block.school.data;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

import java.util.Collection;

/**
 * @author Roar
 */

public class SchoolSubAreaBMPBean extends GenericEntity implements SchoolSubArea {

	public final static String SCHOOLSUBAREA = "SCH_SCHOOL_SUB_AREA";
	public final static String SCHOOLAREA = "SCH_SCHOOL_AREA_ID";
	public final static String NAME = "SUB_AREA_NAME";
	public final static String AREA = "AREA_NAME";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(NAME, "Schoolsubareaname", true, true, String.class);
		addAttribute(SCHOOLAREA, "Schoolarea", true, true, Integer.class, MANY_TO_ONE, SchoolArea.class);
	}
	public String getEntityName() {
		return SCHOOLSUBAREA;
	}
	public String getName() {
		return getSchoolSubAreaName();
	}
	public String getSchoolSubAreaName() {
		return this.getStringColumnValue(NAME);
	}
	public void setSchoolSubAreaName(String name) {
		this.setColumn(NAME, name);
	}


	public Collection ejbFindAllSchoolSubAreas() throws javax.ejb.FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getEntityName()).appendOrderBy(NAME);
		return super.idoFindPKsBySQL(sql.toString());
	}
	
	public Integer ejbFindSchoolAreaByAreaName(String name) throws javax.ejb.FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getEntityName()).appendWhereEqualsQuoted(NAME,name);
		
		return (Integer)super.idoFindOnePKByQuery(sql);
	}

	public void setSchoolAreaId(int id) {
		this.setColumn(SCHOOLAREA, id);
	}
	
	public Collection ejbFindAllByArea(SchoolArea area) throws javax.ejb.FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getEntityName())
		.appendWhereEquals(SCHOOLAREA, area.getPrimaryKey())
		.appendOrderBy(NAME);
		return super.idoFindPKsBySQL(sql.toString());
	}

}
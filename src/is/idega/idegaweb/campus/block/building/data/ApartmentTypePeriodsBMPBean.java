/*
<<<<<<< ApartmentTypePeriodsBMPBean.java
 * $Id: ApartmentTypePeriodsBMPBean.java,v 1.5 2004/06/05 07:41:10 aron Exp $
=======

 * $Id: ApartmentTypePeriodsBMPBean.java,v 1.5 2004/06/05 07:41:10 aron Exp $

>>>>>>> 1.4
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.building.data;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.util.IWTimestamp;
/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class ApartmentTypePeriodsBMPBean
	extends com.idega.data.GenericEntity
	implements ApartmentTypePeriods {
	public final static int ZEROYEAR = 2000;
	public ApartmentTypePeriodsBMPBean() {
		super();
	}
	public ApartmentTypePeriodsBMPBean(int id) throws SQLException {
		super(id);
	}
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(
			getApartmentTypeIdColumnName(),
			"ApartmenttypeId",
			true,
			true,
			"java.lang.Integer",
			"one-to-one",
			"com.idega.block.building.data.ApartmentType");
		addAttribute(getNameColumnName(), "Name", true, true, "java.lang.String");
		addAttribute(getFirstDateColumnName(), "First date", true, true, "java.sql.Date");
		addAttribute(getSecondDateColumnName(), "Second date", true, true, "java.sql.Date");
	}
	public static String getEntityTableName() {
		return "CAM_CONTRACT_DATE";
	}
	public static String getApartmentTypeIdColumnName() {
		return "BU_APRT_TYPE_ID";
	}
	public static String getNameColumnName() {
		return "NAME";
	}
	public static String getFirstDateColumnName() {
		return "FIRSTDATE";
	}
	public static String getSecondDateColumnName() {
		return "SECONDDATE";
	}
	public String getEntityName() {
		return getEntityTableName();
	}
	public int getApartmentTypeId() {
		return getIntColumnValue(getApartmentTypeIdColumnName());
	}
	public void setApartmentTypeId(int id) {
		setColumn(getApartmentTypeIdColumnName(), id);
	}
	public void setApartmentTypeId(Integer id) {
		setColumn(getApartmentTypeIdColumnName(), id);
	}
	public String getName() {
		return getStringColumnValue(getNameColumnName());
	}
	public void setName(String name) {
		setColumn(getNameColumnName(), name);
	}
	public java.sql.Date getFirstDate() {
		return (java.sql.Date) getColumnValue(getFirstDateColumnName());
	}
	public void setFirstDate(java.sql.Date use_date) {
		setColumn(getFirstDateColumnName(), use_date);
	}
	public void setFirstDate(int day, int month) {
		int year = ZEROYEAR;
		if (day > 0 && month > 0)
			year = IWTimestamp.RightNow().getYear();
		else {
			day = 1;
			month = 1;
		}
		IWTimestamp it = new IWTimestamp(day, month, year);
		setFirstDate(it.getSQLDate());
	}
	public java.sql.Date getSecondDate() {
		return (java.sql.Date) getColumnValue(getSecondDateColumnName());
	}
	public void setSecondDate(java.sql.Date use_date) {
		setColumn(getSecondDateColumnName(), use_date);
	}
	public void setSecondDate(int day, int month) {
		int year = ZEROYEAR;
		if (day > 0 && month > 0)
			year = IWTimestamp.RightNow().getYear();
		else {
			day = 1;
			month = 1;
		}
		IWTimestamp it = new IWTimestamp(day, month, year);
		setSecondDate(it.getSQLDate());
	}
	public int getFirstDateDay() {
		IWTimestamp it = new IWTimestamp(getFirstDate());
		return (it.getYear() != ZEROYEAR) ? it.getDay() : 0;
	}
	public int getFirstDateMonth() {
		IWTimestamp it = new IWTimestamp(getFirstDate());
		return (it.getYear() != ZEROYEAR) ? it.getMonth() : 0;
	}
	public int getSecondDateDay() {
		IWTimestamp it = new IWTimestamp(getSecondDate());
		return (it.getYear() != ZEROYEAR) ? it.getDay() : 0;
	}
	public int getSecondDateMonth() {
		IWTimestamp it = new IWTimestamp(getSecondDate());
		return (it.getYear() != ZEROYEAR) ? it.getMonth() : 0;
	}
	public boolean hasFirstPeriod() {
		IWTimestamp it = new IWTimestamp(getFirstDate());
		return (it.getYear() != ZEROYEAR) ? true : false;
	}
	public boolean hasSecondPeriod() {
		IWTimestamp it = new IWTimestamp(getSecondDate());
		return (it.getYear() != ZEROYEAR) ? true : false;
	}
	
	public Collection ejbFindByApartmentType(Integer ID)throws FinderException{
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApartmentTypeIdColumnName(),ID.intValue()));
	}
	
	public Collection ejbFindAll()throws FinderException{
		return super.idoFindAllIDsBySQL();
	}
}

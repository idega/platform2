/*
<<<<<<< BuildingApartmentBMPBean.java
 * 
 * $Id: BuildingApartmentBMPBean.java,v 1.3 2004/06/05 07:35:17 aron Exp $
 * 
 * 
 * 
=======

 * $Id: BuildingApartmentBMPBean.java,v 1.3 2004/06/05 07:35:17 aron Exp $

 *

>>>>>>> 1.2
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 * 
 * 
 * 
 * This software is the proprietary information of Idega hf.
 * 
 * Use is subject to license terms.
 * 
 * 
 *  
 */
package is.idega.idegaweb.campus.data;

import java.sql.SQLException;
/**
 * 
 * Title:
 * 
 * Description:
 * 
 * Copyright: Copyright (c) 2000-2001 idega.is All Rights Reserved
 * 
 * Company: idega
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * 
 * @version 1.1
 *  
 */
public class BuildingApartmentBMPBean
	extends com.idega.data.GenericView
	implements is.idega.idegaweb.campus.data.BuildingApartment {
	/*
	 * 
	 * CREATE VIEW "V_BUILDING_APARTMENTS" (
	 * 
	 * "BU_APARTMENT_ID",
	 * 
	 * "BU_BUILDING_ID",
	 * 
	 * "APARTMENT_NAME",
	 * 
	 * "BUILDING_NAME"
	 *  ) AS
	 * 
	 * 
	 * 
	 * select A.BU_APARTMENT_ID,B.BU_BUILDING_ID,A.NAME,B.NAME
	 * 
	 * from BU_BUILDING B,BU_FLOOR F,BU_APARTMENT A
	 * 
	 * WHERE A.bu_floor_id = F.BU_FLOOR_ID
	 * 
	 * AND F.BU_BUILDING_ID = B.BU_BUILDING_ID
	 *  
	 */
	public static String getEntityTableName() {
		return "V_BUILDING_APARTMENTS";
	}
	public static String getColumnNameApartmentId() {
		return "BU_APARTMENT_ID";
	}
	public static String getColumnNameBuildingId() {
		return "BU_BUILDING_ID";
	}
	public static String getColumnNameApartmentName() {
		return "APARTMENT_NAME";
	}
	public static String getColumnNameBuildingName() {
		return "BUILDING_NAME";
	}
	public BuildingApartmentBMPBean() {
	}
	public BuildingApartmentBMPBean(int id) throws SQLException {
	}
	public void initializeAttributes() {
		addAttribute(getColumnNameApartmentId(), "Apartment id", true, true, java.lang.Integer.class);
		addAttribute(getColumnNameBuildingId(), "Building id", true, true, java.lang.Integer.class);
		addAttribute(getColumnNameApartmentName(), "Apartment name", true, true, java.lang.String.class);
		addAttribute(getColumnNameBuildingName(), "Building name", true, true, java.lang.String.class);
	}
	public String getEntityName() {
		return (getEntityTableName());
	}
	public String getBuildingName() {
		return getStringColumnValue(getColumnNameBuildingName());
	}
	public String getApartmentName() {
		return getStringColumnValue(getColumnNameApartmentName());
	}
	public int getApartmentId() {
		return getIntColumnValue(getColumnNameApartmentId());
	}
	public int getBuildingId() {
		return getIntColumnValue(getColumnNameBuildingId());
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.IDOView#getCreationSQL()
	 */
	public String getCreationSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE VIEW V_BUILDING_APARTMENTS (");
		sql.append("BU_APARTMENT_ID, ");
		sql.append("BU_BUILDING_ID, ");
		sql.append("APARTMENT_NAME, ");
		sql.append("BUILDING_NAME ");
		sql.append(") AS ");
		sql.append(" SELECT A.BU_APARTMENT_ID,B.BU_BUILDING_ID,A.NAME,B.NAME ");
		sql.append(" FROM BU_BUILDING B,BU_FLOOR F,BU_APARTMENT A ");
		sql.append(" WHERE A.bu_floor_id = F.BU_FLOOR_ID ");
		sql.append(" AND F.BU_BUILDING_ID = B.BU_BUILDING_ID ");
		return sql.toString();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericView#getViewName()
	 */
	public String getViewName() {
		
		return getEntityTableName();
	}
}

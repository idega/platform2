/*
 * $Id: AppliedBMPBean.java,v 1.5.4.2 2007/05/31 17:07:52 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentSubcategory;
import com.idega.block.building.data.Complex;
import com.idega.data.GenericEntity;

/**
 * 
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class AppliedBMPBean extends GenericEntity implements Applied {
	private static final String ENTITY_NAME = "cam_applied";
	private static final String COLUMN_COMPLEX = "bu_complex_id";
	//private static final String COLUMN_APARTMENT_TYPE = "bu_aprt_type_id";
	private static final String COLUMN_SUBCATEGORY = "bu_subcategory_id";
	private static final String COLUMN_APPLICATION = "cam_application_id";
	private static final String COLUMN_ORDER = "ordered";
	private static final String COLUMN_APARTMENT = "bu_apartment_id";

	public AppliedBMPBean() {
		super();
	}

	public AppliedBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_COMPLEX, Complex.class);
		//addManyToOneRelationship(COLUMN_APARTMENT_TYPE, ApartmentType.class);
		addManyToOneRelationship(COLUMN_SUBCATEGORY, ApartmentSubcategory.class);
		addManyToOneRelationship(COLUMN_APPLICATION, CampusApplication.class);
		addManyToOneRelationship(COLUMN_APARTMENT, Apartment.class);
		addAttribute(COLUMN_ORDER, "Order", Integer.class);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getComplexIdColumnName() {
		return COLUMN_COMPLEX;
	}

	/*public String getApartmentTypeIdColumnName() {
		return COLUMN_APARTMENT_TYPE;
	}*/
	
	public String getSubcategoryColumnName() {
		return COLUMN_SUBCATEGORY;
	}

	public String getApplicationIdColumnName() {
		return COLUMN_APPLICATION;
	}

	public String getOrderColumnName() {
		return COLUMN_ORDER;
	}

	public void setComplexId(int id) {
		setColumn(COLUMN_COMPLEX, id);
	}

	public void setComplexId(Integer id) {
		setColumn(COLUMN_COMPLEX, id);
	}

	public Integer getComplexId() {
		return getIntegerColumnValue(COLUMN_COMPLEX);
	}

	/*public void setApartmentTypeId(int id) {
		setColumn(COLUMN_APARTMENT_TYPE, id);
	}*/
	
	public void setSubcategoryID(int id) {
		setColumn(COLUMN_SUBCATEGORY, id);
	}

	/*public void setApartmentTypeId(Integer id) {
		setColumn(COLUMN_APARTMENT_TYPE, id);
	}*/

	public void setSubcategoryID(Integer id) {
		setColumn(COLUMN_SUBCATEGORY, id);
	}
	
	/*public Integer getApartmentTypeId() {
		return getIntegerColumnValue(COLUMN_APARTMENT_TYPE);
	}*/
	
	public int getSubcategoryID() {
		return getIntColumnValue(COLUMN_SUBCATEGORY);
	}
	
	public ApartmentSubcategory getSubcategory() {
		return (ApartmentSubcategory) getColumnValue(COLUMN_SUBCATEGORY);
	}

	public void setApplicationId(int id) {
		setColumn(COLUMN_APPLICATION, id);
	}

	public void setApplicationId(Integer id) {
		setColumn(COLUMN_APPLICATION, id);
	}

	public Integer getApplicationId() {
		return getIntegerColumnValue(COLUMN_APPLICATION);
	}

	public void setOrder(int order) {
		setColumn(COLUMN_ORDER, order);
	}

	public void setOrder(Integer order) {
		setColumn(COLUMN_ORDER, order);
	}

	public Integer getOrder() {
		return getIntegerColumnValue(COLUMN_ORDER);
	}
	
	public void setApartment(Apartment apartment) {
		setColumn(COLUMN_APARTMENT, apartment);
	}
	
	public void setApartmentID(int apartmentID) {
		setColumn(COLUMN_APARTMENT, apartmentID);
	}
	
	public Apartment getApartment() {
		return (Apartment) getColumnValue(COLUMN_APARTMENT);
	}
	
	public int getApartmentID() {
		return getIntColumnValue(COLUMN_APARTMENT);
	}

	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindByApplicationID(Integer ID) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect()
				.appendWhereEquals(COLUMN_APPLICATION, ID.intValue()));
	}

	public Collection ejbFindBySQL(String sql) throws FinderException {
		return super.idoFindPKsBySQL(sql);
	}
}
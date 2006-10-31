/*
 * Created on Jan 22, 2004
 *
 */
package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.GenericView;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * ComplexTypeViewBMPBean
 * 
 * @author aron
 * @version 1.0
 */
public class ComplexTypeViewBMPBean extends GenericView implements
		ComplexTypeView {

	protected static final String APARTMENT_COUNT = "APARTMENT_COUNT";

	protected static final String COMPLEX_NAME = "COMPLEX_NAME";

	protected static final String TYPE_NAME = "TYPE_NAME";

	protected static final String BU_APRT_TYPE_ID = "BU_APRT_TYPE_ID";

	protected static final String BU_COMPLEX_ID = "BU_COMPLEX_ID";

	protected static final String BU_APRT_CAT_ID = "BU_APRT_CAT_ID";

	protected static final String V_COMPLEX_TYPES = "V_COMPLEX_TYPES";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.IDOEntityBean#getPrimaryKeyClass()
	 */
	public Class getPrimaryKeyClass() {
		return ComplexTypeViewKey.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(BU_APRT_CAT_ID, "Apartment category id", true, true,
				java.lang.Integer.class);
		//addAttribute(BU_APRT_TYPE_ID, "Type id", true, true,
		//		java.lang.Integer.class);
		//addAttribute(BU_COMPLEX_ID, "Building id", true, true,
		//		java.lang.Integer.class);
		addAttribute(TYPE_NAME, "Type name", true, true, java.lang.String.class);
		addAttribute(COMPLEX_NAME, "Complex name", true, true,
				java.lang.String.class);
		
		
		addManyToOneRelationship(BU_COMPLEX_ID, Complex.class);
		addManyToOneRelationship(BU_APRT_TYPE_ID, ApartmentType.class);

		setAsPrimaryKey(BU_COMPLEX_ID, true);
		setAsPrimaryKey(BU_APRT_TYPE_ID, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return (V_COMPLEX_TYPES);
	}

	public String getComplexName() {
		return getStringColumnValue(COMPLEX_NAME);
	}

	public String getApartmentTypeName() {
		return getStringColumnValue(TYPE_NAME);
	}

	public Integer getApartmentTypeID() {
		return getIntegerColumnValue(BU_APRT_TYPE_ID);
	}

	public ApartmentType getApartmentType() {
		return (ApartmentType) getColumnValue(BU_APRT_TYPE_ID);
	}

	public Integer getComplexID() {
		return getIntegerColumnValue(BU_COMPLEX_ID);
	}

	public Complex getComplex() {
		return (Complex) getColumnValue(BU_COMPLEX_ID);
	}

	public Object ejbFindByPrimaryKey(ComplexTypeViewKey primaryKey)
			throws FinderException {
		return super.ejbFindByPrimaryKey(primaryKey);
	}

	public Object ejbCreate(ComplexTypeViewKey primaryKey)
			throws CreateException {
		setPrimaryKey(primaryKey);
		return super.ejbCreate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.IDOView#getCreationSQL()
	 */
	public String getCreationSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append(" CREATE VIEW ").append(V_COMPLEX_TYPES).append(" ( ");
		sql.append(BU_APRT_CAT_ID).append(", ");
		sql.append(BU_COMPLEX_ID).append(", ");
		sql.append(BU_APRT_TYPE_ID).append(", ");
		sql.append(TYPE_NAME).append(", ");
		sql.append(COMPLEX_NAME).append(", ");
		sql.append(APARTMENT_COUNT);
		sql.append("  ) AS ");
		sql
				.append("  SELECT  a.bu_aprt_cat_id,c.bu_complex_id, a.bu_aprt_type_id, a.name,  c.name, count(apa.bu_apartment_id) apartment_count");
		sql
				.append("  FROM bu_aprt_type a, bu_complex c, bu_building bu, bu_floor fl, bu_apartment apa");
		sql.append("  WHERE a.bu_aprt_type_id = apa.bu_aprt_type_id");
		sql.append("  AND apa.bu_floor_id = fl.bu_floor_id ");
		sql.append("  AND fl.bu_building_id = bu.bu_building_id ");
		sql.append("  AND bu.bu_complex_id = c.bu_complex_id");
		sql
				.append(" GROUP BY a.bu_aprt_cat_id, a.bu_aprt_type_id, a.name, c.bu_complex_id, c.name");
		return sql.toString();
	}

	public Collection ejbFindAll() throws FinderException {
		return idoFindPKsByQuery(super.idoQueryGetSelect());
	}

	public Collection ejbFindByCategory(Integer categoryID)
			throws FinderException {
		Table apartment = new Table(this);
		SelectQuery query = new SelectQuery(apartment);
		query.addColumn(new WildCardColumn(apartment));
		query.addCriteria(new MatchCriteria(apartment, BU_APRT_CAT_ID,
				MatchCriteria.EQUALS, categoryID.intValue()));
		return idoFindPKsBySQL(query.toString());
	}

}

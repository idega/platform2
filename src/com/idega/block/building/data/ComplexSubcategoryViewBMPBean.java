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
public class ComplexSubcategoryViewBMPBean extends GenericView implements
		ComplexSubcategoryView {

	protected static final String COLUMN_APARTMENT_COUNT = "apartment_count";

	protected static final String COLUMN_COMPLEX_NAME = "complex_name";

	protected static final String COLUMN_TYPE_NAME = "type_name";

	//protected static final String BU_APRT_TYPE_ID = "BU_APRT_TYPE_ID";
	protected static final String COLUMN_SUBCATEGORY = "bu_subcategory_id";

	protected static final String COLUMN_COMPLEX = "bu_complex_id";

	protected static final String COLUMN_CATEGORY = "bu_aprt_cat_id";

	protected static final String ENTITY_NAME = "v_complex_subcat";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.IDOEntityBean#getPrimaryKeyClass()
	 */
	public Class getPrimaryKeyClass() {
		return ComplexSubcategoryViewKey.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COLUMN_CATEGORY, "Apartment category id", Integer.class);
		addAttribute(COLUMN_TYPE_NAME, "Type name", String.class);
		addAttribute(COLUMN_COMPLEX_NAME, "Complex name", String.class);
		
		addManyToOneRelationship(COLUMN_COMPLEX, Complex.class);
		addManyToOneRelationship(COLUMN_SUBCATEGORY, ApartmentSubcategory.class);

		setAsPrimaryKey(COLUMN_COMPLEX, true);
		setAsPrimaryKey(COLUMN_SUBCATEGORY, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return (ENTITY_NAME);
	}

	public String getComplexName() {
		return getStringColumnValue(COLUMN_COMPLEX_NAME);
	}

	public String getSubcategoryName() {
		return getStringColumnValue(COLUMN_TYPE_NAME);
	}

	public Integer getSubcategoryID() {
		return getIntegerColumnValue(COLUMN_SUBCATEGORY);
	}

	public ApartmentSubcategory getSubcategory() {
		return (ApartmentSubcategory) getColumnValue(COLUMN_SUBCATEGORY);
	}

	public Integer getComplexID() {
		return getIntegerColumnValue(COLUMN_COMPLEX);
	}

	public Complex getComplex() {
		return (Complex) getColumnValue(COLUMN_COMPLEX);
	}

	public Object ejbFindByPrimaryKey(ComplexSubcategoryViewKey primaryKey)
			throws FinderException {
		return super.ejbFindByPrimaryKey(primaryKey);
	}

	public Object ejbCreate(ComplexSubcategoryViewKey primaryKey)
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
		sql.append(" CREATE VIEW ").append(ENTITY_NAME).append(" ( ");
		sql.append(COLUMN_CATEGORY).append(", ");
		sql.append(COLUMN_COMPLEX).append(", ");
		sql.append(COLUMN_SUBCATEGORY).append(", ");
		sql.append(COLUMN_TYPE_NAME).append(", ");
		sql.append(COLUMN_COMPLEX_NAME).append(", ");
		sql.append(COLUMN_APARTMENT_COUNT);
		sql.append("  ) AS ");
		sql
				.append("  SELECT  sub.aprt_cat, c.bu_complex_id, sub.bu_aprt_sub_cat_id, sub.name,  c.name, count(apa.bu_apartment_id) apartment_count");
		sql
				.append("  FROM bu_aprt_sub_cat sub, bu_aprt_type a, bu_complex c, bu_building bu, bu_floor fl, bu_apartment apa");
		sql.append("  WHERE a.bu_aprt_type_id = apa.bu_aprt_type_id");
		sql.append("  AND apa.bu_floor_id = fl.bu_floor_id ");
		sql.append("  AND fl.bu_building_id = bu.bu_building_id ");
		sql.append("  AND bu.bu_complex_id = c.bu_complex_id");
		sql.append("  AND sub.bu_aprt_sub_cat_id = a.bu_aprt_subcat");
		sql
				.append(" GROUP BY sub.aprt_cat, sub.bu_aprt_sub_cat_id, sub.name, c.bu_complex_id, c.name");
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
		query.addCriteria(new MatchCriteria(apartment, COLUMN_CATEGORY,
				MatchCriteria.EQUALS, categoryID.intValue()));
		return idoFindPKsBySQL(query.toString());
	}

}

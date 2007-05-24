/*
 * Created on Jan 26, 2004
 *
 */
package is.idega.idegaweb.campus.block.allocation.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Complex;
import com.idega.data.GenericView;

/**
 * AllocationViewBMPBean
 * 
 * @author aron
 * @version 1.0
 */
public class AllocationViewBMPBean extends GenericView {

	protected final static String ENTITY_NAME = "V_ALLOCATION_VIEW";

	protected final static String COLUMN_CATEGORY = "BU_APRT_CAT_ID";

	protected final static String APARTMENT_TYPE_ID = "BU_APRT_TYPE_ID";

	protected final static String COLUMN_COMPLEX = "BU_COMPLEX_ID";

	protected final static String TYPE_NAME = "TYPE_NAME";

	protected final static String COLUMN_COMPLEX_NAME = "COMPLEX_NAME";

	protected final static String COLUMN_TOTAL_APARTMENT_COUNT = "TOTAL_APRT";

	protected final static String COLUMN_AVAILABLE_APARTMENT_COUNT = "AVAIL_APRT";

	protected final static String COLUMN_CHOICE_ONE = "CHOICE1";

	protected final static String COLUMN_CHOICE_TWO = "CHOICE2";

	protected final static String COLUMN_CHOICE_THREE = "CHOICE3";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COLUMN_CATEGORY, "Apartment category", true, true,
				Integer.class);
		// addAttribute(APARTMENT_TYPE_ID,"Apartment
		// type",true,true,Integer.class);
		// addAttribute(COMPLEX_ID,"Complex name",true,true,Integer.class);
		addAttribute(TYPE_NAME, "Type name", true, true, String.class);
		addAttribute(COLUMN_COMPLEX_NAME, "Category name", true, true, String.class);
		addAttribute(COLUMN_TOTAL_APARTMENT_COUNT, "Total apartment count", true,
				true, Integer.class);
		addAttribute(COLUMN_AVAILABLE_APARTMENT_COUNT, "Available apartment count",
				true, true, Integer.class);
		addAttribute(COLUMN_CHOICE_ONE, "First choice", true, true, Integer.class);
		addAttribute(COLUMN_CHOICE_TWO, "Second choice", true, true, Integer.class);
		addAttribute(COLUMN_CHOICE_THREE, "Third choice", true, true, Integer.class);
		addManyToOneRelationship(APARTMENT_TYPE_ID, ApartmentType.class);
		addManyToOneRelationship(COLUMN_COMPLEX, Complex.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.IDOView#getCreationSQL()
	 */
	public String getCreationSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE VIEW ").append(getViewName()).append(" (");
		sql.append(COLUMN_CATEGORY).append(" ,");
		sql.append(APARTMENT_TYPE_ID).append(" ,");
		sql.append(COLUMN_COMPLEX).append(" ,");
		sql.append(TYPE_NAME).append(" ,");
		sql.append(COLUMN_COMPLEX_NAME).append(" ,");
		sql.append(COLUMN_TOTAL_APARTMENT_COUNT).append(" ,");
		sql.append(COLUMN_AVAILABLE_APARTMENT_COUNT).append(" ,");
		sql.append(COLUMN_CHOICE_ONE).append(" ,");
		sql.append(COLUMN_CHOICE_TWO).append(" ,");
		sql.append(COLUMN_CHOICE_THREE);
		sql.append(") AS ");
		sql
				.append("select v.bu_aprt_cat_id, v.bu_aprt_type_id,  v.bu_complex_id, ");
		sql
				.append(" v.aprt_type_name, v.complex_name, avail.total_aprt, avail.total_aprt - rented.rented_aprt, ");
		sql.append(" l1.applied1, l2.applied2, l3.applied3 ");
		sql
				.append(" from v_cam_aprt_type_complex v, v_available_aprt avail, v_rented_aprt rented, v_waiting_list1 l1, ");
		sql.append(" v_waiting_list2 l2, v_waiting_list3 l3 ");
		sql.append(" where avail.bu_complex_id = v.bu_complex_id ");
		sql.append(" and avail.bu_aprt_type_id = v.bu_aprt_type_id ");
		sql.append("and rented.bu_complex_id = v.bu_complex_id ");
		sql.append(" and rented.bu_aprt_type_id = v.bu_aprt_type_id ");
		sql.append("and l1.bu_complex_id = v.bu_complex_id ");
		sql.append(" and l1.bu_aprt_type_id = v.bu_aprt_type_id ");
		sql.append("and l2.bu_complex_id = v.bu_complex_id ");
		sql.append(" and l2.bu_aprt_type_id = v.bu_aprt_type_id ");
		sql.append("and l3.bu_complex_id = v.bu_complex_id ");
		sql.append(" and l3.bu_aprt_type_id = v.bu_aprt_type_id ");
		return null;
	}

	public Integer getApartmentCategoryID() {
		return getIntegerColumnValue(COLUMN_CATEGORY);
	}

	public Integer getApartmentTypeID() {
		return getIntegerColumnValue(APARTMENT_TYPE_ID);
	}
	
	public ApartmentType getApartmentType() {
		return (ApartmentType) getColumnValue(APARTMENT_TYPE_ID);
	}

	public Integer getComplexID() {
		return getIntegerColumnValue(COLUMN_COMPLEX);
	}
	
	public Complex getComplex() {
		return (Complex) getColumnValue(COLUMN_COMPLEX);
	}

	public String getComplexName() {
		return getStringColumnValue(COLUMN_COMPLEX_NAME);
	}

	public String getApartmentTypeName() {
		return getStringColumnValue(TYPE_NAME);
	}

	public Integer getTotalApartmentCount() {
		return getIntegerColumnValue(COLUMN_TOTAL_APARTMENT_COUNT);
	}

	public Integer getAvailableApartmentCount() {
		return getIntegerColumnValue(COLUMN_AVAILABLE_APARTMENT_COUNT);
	}

	public Integer getChoiceOne() {
		return getIntegerColumnValue(COLUMN_CHOICE_ONE);
	}

	public Integer getChoiceTwo() {
		return getIntegerColumnValue(COLUMN_CHOICE_ONE);
	}

	public Integer getChoiceThree() {
		return getIntegerColumnValue(COLUMN_CHOICE_ONE);
	}

	public Collection ejbFindAll() throws FinderException {
		return idoFindAllIDsBySQL();
	}

	public Collection ejbFindByCategory(Integer categoryID)
			throws FinderException {
		return idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(
				COLUMN_CATEGORY, categoryID));
	}
}

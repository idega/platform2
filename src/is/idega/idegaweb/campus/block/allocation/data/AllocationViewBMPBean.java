/*
 * Created on Jan 26, 2004
 *
 */
package is.idega.idegaweb.campus.block.allocation.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericView;

/**
 * AllocationViewBMPBean
 * @author aron 
 * @version 1.0
 */
public class AllocationViewBMPBean extends GenericView {
	
	protected final static String VIEW_NAME = "V_ALLOCATION_VIEW";
	protected final static String APARTMENT_CATEGORY_ID = "BU_APRT_CAT_ID";
	protected final static String APARTMENT_TYPE_ID = "BU_APRT_TYPE_ID";
	protected final static String COMPLEX_ID = "BU_COMPLEX_ID";
	protected final static String TYPE_NAME = "TYPE_NAME";
	protected final static String COMPLEX_NAME="COMPLEX_NAME";
	protected final static String TOTAL_APARTMENT_COUNT = "TOTAL_APRT";
	protected final static String AVAILABLE_APARTMENT_COUNT = "AVAIL_APRT";
	protected final static String CHOICE_ONE = "CHOICE1";
	protected final static String CHOICE_TWO = "CHOICE2";
	protected final static String CHOICE_THREE = "CHOICE3";
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return VIEW_NAME;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(APARTMENT_CATEGORY_ID,"Apartment category",true,true,Integer.class);
		addAttribute(APARTMENT_TYPE_ID,"Apartment type",true,true,Integer.class);
		addAttribute(COMPLEX_ID,"Complex name",true,true,Integer.class);
		addAttribute(TYPE_NAME,"Type name",true,true,String.class);
		addAttribute(COMPLEX_NAME,"Category name",true,true,String.class);
		addAttribute(TOTAL_APARTMENT_COUNT,"Total apartment count",true,true,Integer.class);
		addAttribute(AVAILABLE_APARTMENT_COUNT,"Available apartment count",true,true,Integer.class);
		addAttribute(CHOICE_ONE,"First choice",true,true,Integer.class);
		addAttribute(CHOICE_TWO,"Second choice",true,true,Integer.class);
		addAttribute(CHOICE_THREE,"Third choice",true,true,Integer.class);
	}
	/* (non-Javadoc)
	 * @see com.idega.data.IDOView#getCreationSQL()
	 */
	public String getCreationSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE VIEW ").append(getViewName()).append(" (");
		sql.append(APARTMENT_CATEGORY_ID).append(" ,");
		sql.append(APARTMENT_TYPE_ID).append(" ,");
		sql.append(COMPLEX_ID).append(" ,");
		sql.append(TYPE_NAME).append(" ,");
		sql.append(COMPLEX_NAME).append(" ,");
		sql.append(TOTAL_APARTMENT_COUNT).append(" ,");
		sql.append(AVAILABLE_APARTMENT_COUNT).append(" ,");
		sql.append(CHOICE_ONE).append(" ,");
		sql.append(CHOICE_TWO).append(" ,");
		sql.append(CHOICE_THREE);
		sql.append(") AS ");
		sql.append("select v.bu_aprt_cat_id, v.bu_aprt_type_id,  v.bu_complex_id, ");
		sql.append(" v.aprt_type_name, v.complex_name, avail.total_aprt, avail.total_aprt - rented.rented_aprt, ");
		sql.append(" l1.applied1, l2.applied2, l3.applied3 ");
		sql.append(" from v_cam_aprt_type_complex v, v_available_aprt avail, v_rented_aprt rented, v_waiting_list1 l1, ");
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
	
	public Integer getApartmentCategoryID(){
		return getIntegerColumnValue(APARTMENT_CATEGORY_ID);
	}
	public Integer getApartmentTypeID(){
		return getIntegerColumnValue(APARTMENT_TYPE_ID);
	}
	public Integer getComplexID(){
		return getIntegerColumnValue(COMPLEX_ID);
	}
	public String getComplexName(){
		return getStringColumnValue(COMPLEX_NAME);
	}
	public String getApartmentTypeName(){
		return getStringColumnValue(TYPE_NAME);
	}
	public Integer getTotalApartmentCount(){
		return getIntegerColumnValue(TOTAL_APARTMENT_COUNT);	
	}
	public Integer getAvailableApartmentCount(){
		return getIntegerColumnValue(AVAILABLE_APARTMENT_COUNT);
	}
	public Integer getChoiceOne(){
		return getIntegerColumnValue(CHOICE_ONE);
	}
	public Integer getChoiceTwo(){
		return getIntegerColumnValue(CHOICE_ONE);
	}
	public Integer getChoiceThree(){
		return getIntegerColumnValue(CHOICE_ONE);
	}
	
	public Collection ejbFindAll()throws FinderException{
		return idoFindAllIDsBySQL();
	}
	
	public Collection ejbFindByCategory(Integer categoryID)throws FinderException{
		return idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(APARTMENT_CATEGORY_ID,categoryID));
	}
}

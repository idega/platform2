/*
 * Created on Jan 26, 2004
 *
 */
package is.idega.idegaweb.campus.block.allocation.data;

import com.idega.data.GenericView;

/**
 * AvailableApartmentBMPBEan
 * @author aron 
 * @version 1.0
 */
public class AvailableApartmentsBMPBean extends GenericView {
	
	protected final static String VIEW_NAME = "V_AVAILABLE_APARTMENTS";
	protected final static String COMPLEX_ID ="BU_COMPLEX_ID";
	protected final static String TYPE_ID = "BU_APRT_TYPE_ID";
	protected final static String APARTMENT_COUNT ="TOTAL_APRT";
	
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
		addAttribute(TYPE_ID,"Apartment type",true,true,Integer.class);
		addAttribute(COMPLEX_ID,"Complex name",true,true,Integer.class);
		addAttribute(APARTMENT_COUNT,"Apartment count",true,true,Integer.class);
	}
	/* (non-Javadoc)
	 * @see com.idega.data.IDOView#getCreationSQL()
	 */
	public String getCreationSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append("create view v_available_aprt (bu_complex_id, bu_aprt_type_id, total_aprt) as ");
		sql.append(" select c.bu_complex_id, t.bu_aprt_type_id, count(*) from bu_apartment a,bu_floor f,bu_building b,bu_complex c,bu_aprt_type t "); 
		sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id "); 
		sql.append(" and a.bu_floor_id = f.bu_floor_id ");
		sql.append(" and f.bu_building_id = b.bu_building_id ");
		sql.append(" and b.bu_complex_id = c.bu_complex_id ");
		sql.append(" group by c.bu_complex_id, t.bu_aprt_type_id; ");
		return sql.toString();
	}
}

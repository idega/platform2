/*
 * Copyright (C) 2001 Idega hf. 
 * All Rights Reserved. 
 * This software is the proprietary
 * information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.campus.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega.is
 * 
 * @author 2000 - idega team -<br>
 *         <a href="mailto:aron@idega.is">Aron Birkir </a><br>
 * @version 1.
 */
public class HabitantBMPBean extends com.idega.data.GenericView implements
		is.idega.idegaweb.campus.data.Habitant {
	/*
	 * create view V_HABITANTS (APP_APPLICANT_ID, IC_USER_ID,CAM_CONTRACT_ID
	 * ,BU_COMPLEX_ID,FULL_NAME,ADDRESS,APARTMENT,FLOOR,NUMBER_,EMAIL) as select
	 * a.app_applicant_id, c.ic_user_id,c.cam_contract_id,x.bu_complex_id,
	 * a.full_name,b.name address,g.name apartment,f.name floor,p.phone_number
	 * number//, e.email from app_applicant a,app_applicant_tree t, cam_contract
	 * c, bu_building b, bu_floor f, bu_apartment g ,bu_complex x ,cam_phone
	 * p//, v_emails e where a.app_applicant_id = t.child_app_applicant_id and
	 * g.bu_floor_id = f.bu_floor_id and f.bu_building_id = b.bu_building_id and
	 * t.app_applicant_id = c.app_applicant_id and g.bu_apartment_id =
	 * c.bu_apartment_id and b.bu_complex_id = x.bu_complex_id and
	 * p.bu_apartment_id = g.bu_apartment_id //and c.ic_user_id = e.ic_user_id
	 * and c.rented = 'Y'
	 * 
	 * 
	 * create view v_emails(ic_user_id, email) as select c.ic_user_id, ca.email
	 * from cam_contract c, app_application app, cam_application ca where
	 * c.app_applicant_id = app.app_applicant_id and app.app_application_id =
	 * ca.app_application_id and c.rented = 'Y' and app.status = 'C'
	 */

	public static String getEntityTableName() {
		return "V_HABITANTS";
	}

	public static String getColumnApplicantId() {
		return "APP_APPLICANT_ID";
	}

	public static String getColumnUserId() {
		return "IC_USER_ID";
	}

	public static String getColumnContractId() {
		return "CAM_CONTRACT_ID";
	}

	public static String getColumnComplexId() {
		return "BU_COMPLEX_ID";
	}

	public static String getColumnFullname() {
		return "FULL_NAME";
	}

	public static String getColumnAddress() {
		return "ADDRESS";
	}

	public static String getColumnApartment() {
		return "APARTMENT";
	}

	public static String getColumnFloor() {
		return "FLOOR";
	}

	public static String getColumnNumber() {
		return "NUMBER_";
	}

	// public static String getColumnEmail() {
	// return "EMAIL";
	// }
	public HabitantBMPBean() {
	}

	public HabitantBMPBean(int id) throws SQLException {
	}

	public void initializeAttributes() {
		addAttribute(getColumnApplicantId(), "Applicant", true, true,
				Integer.class);
		addAttribute(getColumnUserId(), "User ", true, true, Integer.class);
		addAttribute(getColumnContractId(), "ContractId", true, true,
				Integer.class);
		addAttribute(getColumnComplexId(), "ComplexId", true, true,
				Integer.class);
		addAttribute(getColumnFullname(), "Fullname", true, true, String.class);
		addAttribute(getColumnAddress(), "Address", true, true, String.class);
		addAttribute(getColumnApartment(), "Apartment", true, true,
				String.class);
		addAttribute(getColumnFloor(), "Floor", true, true, String.class);
		addAttribute(getColumnNumber(), "Phone number", true, true,
				String.class);
		// addAttribute(getColumnEmail(), "Email", true, true, String.class);
		setAsPrimaryKey(getColumnApplicantId(), true);
	}

	public String getEntityName() {
		return (getEntityTableName());
	}

	public int getUserId() {
		return getIntColumnValue(getColumnUserId());
	}

	public int getContractId() {
		return getIntColumnValue(getColumnContractId());
	}

	public int getComplexId() {
		return getIntColumnValue(getColumnComplexId());
	}

	public String getFullName() {
		return getStringColumnValue(getColumnFullname());
	}

	public String getAddress() {
		return getStringColumnValue(getColumnAddress());
	}

	public String getApartment() {
		return getStringColumnValue(getColumnApartment());
	}

	public String getFloor() {
		return getStringColumnValue(getColumnFloor());
	}

	public String getPhoneNumber() {
		return getStringColumnValue(getColumnNumber());
	}

	// public String getEmail() {
	// return getStringColumnValue(getColumnEmail());
	// }
	public void insert() throws SQLException {
	}

	public void delete() throws SQLException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.data.IDOView#getCreationSQL()
	 */
	public String getCreationSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append(" create view V_HABITANTS ( APP_APPLICANT_ID, ");
		sql.append(" IC_USER_ID, ");
		sql.append(" CAM_CONTRACT_ID , ");
		sql.append(" BU_COMPLEX_ID, ");
		sql.append(" FULL_NAME, ");
		sql.append(" ADDRESS, ");
		sql.append(" APARTMENT, ");
		sql.append(" FLOOR, ");
		sql.append(" NUMBER_) ");
		// sql.append(" EMAIL) ");
		sql.append(" as ");
		sql
				.append(" select a.app_applicant_id,c.ic_user_id,c.cam_contract_id,x.bu_complex_id, a.full_name, ");
		sql.append(" b.name address, ");
		sql.append(" g.name apartment,f.name floor,p.phone_number number_ ");// ,
																			// ca.email
																			// ");
		sql
				.append(" from app_applicant a,app_applicant_tree t, cam_contract c, ");
		sql
				.append(" bu_building b, bu_floor f, bu_apartment g ,bu_complex x ,cam_phone p, cam_application ca, app_application app ");
		sql.append(" where a.app_applicant_id = t.child_app_applicant_id ");
		sql.append(" and g.bu_floor_id = f.bu_floor_id ");
		sql.append(" and f.bu_building_id = b.bu_building_id ");
		sql.append(" and t.app_applicant_id = c.app_applicant_id ");
		sql.append(" and g.bu_apartment_id = c.bu_apartment_id ");
		sql.append(" and b.bu_complex_id = x.bu_complex_id ");
		sql.append(" and p.bu_apartment_id = g.bu_apartment_id ");
		sql.append(" and a.app_applicant_id = app.app_applicant_id ");
		sql.append(" and app.app_application_id = ca.app_application_id ");
		sql.append(" and c.rented = 'Y' ");
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

	public Collection ejbFindByComplex(Integer complexID)
			throws FinderException {
		return idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(
				getColumnComplexId(), complexID));
	}
}
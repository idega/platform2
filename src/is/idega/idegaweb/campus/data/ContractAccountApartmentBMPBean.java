/*
 * $Id: ContractAccountApartmentBMPBean.java,v 1.2 2002/11/20 14:01:05 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.data;

import com.idega.data.GenericEntity;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class ContractAccountApartmentBMPBean extends GenericEntity implements ContractAccountApartment {
	/*
	 CREATE VIEW "V_CONT_ACCT_APRT" (
	 "CAM_CONTRACT_ID",
	 "IC_USER_ID",
	 "BU_APARTMENT_ID",
	 "FIN_ACCOUNT_ID",
	 "FIN_ACCOUNT_TYPE",
	 "BALANCE",
	 "ACCOUNT_NAME",
	 "VALID_FROM",
	 "VALID_TO",
	 "DELIVER_DATE",
	 "RETURN_DATE",
	 "STATUS",
	 "RENTED",
	 "BU_APRT_TYPE_ID",
	 "BU_APRT_CAT_ID",
	 "BU_FLOOR_ID",
	 "BU_BUILDING_ID",
	 "BU_COMPLEX_ID"
	) AS
		
	select cc.cam_contract_id,cc.ic_user_id,cc.bu_apartment_id,
	fa.fin_account_id,fa.account_type, fa.balance,fa.name,
	cc.valid_from,cc.valid_to,cc.deliver_date,cc.return_date,cc.status,cc.rented
	ba.bu_aprt_type_id , bc.bu_aprt_cat_id,
	bf.bu_floor_id,bb.bu_building_id,bx.bu_complex_id
	from cam_contract cc,fin_account fa,bu_apartment ba,
	bu_floor bf,bu_building bb,bu_complex bx,bu_aprt_cat bc,bu_aprt_type bt
	where cc.ic_user_id = fa.ic_user_id
	and cc.bu_apartment_id = ba.bu_apartment_id
	and ba.bu_floor_id = bf.bu_floor_id
	and bf.bu_building_id = bb.bu_building_id
	and bb.bu_complex_id = bx.bu_complex_id
	and ba.bu_aprt_type_id = bt.bu_aprt_type_id
	and bt.bu_aprt_cat_id = bc.bu_aprt_cat_id
	;
	*/
		
	public static String getEntityTableName() {
		return "V_CONT_ACCT_APRT";
	}
	
	public static String getContractIdColumnName() {
		return "CAM_CONTRACT_ID";
	}
	
	public static String getColumnValidFrom() {
		return "VALID_FROM";
	}
	
	public static String getColumnValidTo() {
		return "VALID_TO";
	}
	
	public static String getColumnReturnDate() {
		return "RETURN_DATE";
	}
	
	public static String getColumnDeliverdate() {
		return "DELIVERDATE";
	}
	
	public static String getColumnStatus() {
		return "STATUS";
	}
	
	public static String getColumnRented() {
		return "RENTED";
	}
	
	public static String getUserIdColumnName() {
		return "IC_USER_ID";
	}
	
	public static String getApartmentIdColumnName() {
		return "BU_APARTMENT_ID";
	}
	
	public static String getAccountIdColumnName() {
		return "FIN_ACCOUNT_ID";
	}
	
	public static String getAccountTypeColumnName() {
		return "FIN_ACCOUNT_TYPE";
	}
	
	public static String getBalanceColumnName() {
		return "BALANCE";
	}
	
	public static String getAccountNameColumnName() {
		return "ACCOUNT_NAME";
	}
	
	public static String getApartmentTypeIdColumnName() {
		return "BU_APRT_TYPE_ID";
	}
	
	public static String getApartmentCategoryIdColumnName() {
		return "BU_APRT_CAT_ID";
	}
	
	public static String getFloorIdColumnName() {
		return "BU_FLOOR_ID";
	}
	
	public static String getBuildingIdColumnName() {
		return "BU_BUILDING_ID";
	}
	
	public static String getComplexIdColumnName() {
		return "BU_COMPLEX_ID";
	}

	public ContractAccountApartmentBMPBean() {
	}
	
	public ContractAccountApartmentBMPBean(int id) throws SQLException {
	}
	
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getContractIdColumnName(), "Contract id", true, true, java.lang.Integer.class);
		addAttribute(getUserIdColumnName(), "User id", true, true, java.lang.Integer.class);
		addAttribute(getApartmentIdColumnName(), "Apartment id", true, true, java.lang.Integer.class);
		addAttribute(getAccountIdColumnName(), "Account id", true, true, java.lang.Integer.class);
		addAttribute(getAccountTypeColumnName(), "Account type", true, true, java.lang.String.class);
		addAttribute(getBalanceColumnName(), "Balance", true, true, java.lang.Integer.class);
		addAttribute(getAccountNameColumnName(), "Account name", true, true, java.lang.String.class);

		addAttribute(getColumnValidFrom(), "Valid from", true, true, Date.class);
		addAttribute(getColumnValidTo(), "Valid to", true, true, Date.class);
		addAttribute(getColumnDeliverdate(), "Deliver date", true, true, Timestamp.class);
		addAttribute(getColumnReturnDate(), "Return date", true, true, Timestamp.class);
		addAttribute(getColumnStatus(), "status", true, true, String.class, 1);
		addAttribute(getColumnRented(), "rented", true, true, Boolean.class);

		addAttribute(getApartmentTypeIdColumnName(), "Apartmenttype id", true, true, java.lang.Integer.class);
		addAttribute(getApartmentCategoryIdColumnName(), "Apartmentcategory id", true, true, java.lang.Integer.class);
		addAttribute(getFloorIdColumnName(), "Floor id", true, true, java.lang.Integer.class);
		addAttribute(getBuildingIdColumnName(), "Building id", true, true, java.lang.Integer.class);
		addAttribute(getComplexIdColumnName(), "Complex id", true, true, java.lang.Integer.class);
	}
	
	public String getEntityName() {
		return getEntityTableName();
	}
	
	public int getContractId() {
		return getIntColumnValue(getContractIdColumnName());
	}
	
	public int getUserId() {
		return getIntColumnValue(getUserIdColumnName());
	}
	
	public int getApartmentId() {
		return getIntColumnValue(getApartmentIdColumnName());
	}
	
	public int getApartmentTypeId() {
		return getIntColumnValue(getApartmentTypeIdColumnName());
	}
	
	public int getAccountId() {
		return getIntColumnValue(getAccountIdColumnName());
	}
	
	public int getAccountType() {
		return getIntColumnValue(getAccountTypeColumnName());
	}
	
	public int getBalance() {
		return getIntColumnValue(getBalanceColumnName());
	}

	public String getAccountName() {
		return getStringColumnValue(getAccountNameColumnName());
	}
	
	public int getApartmentCategoryId() {
		return getIntColumnValue(getApartmentCategoryIdColumnName());
	}
	
	public int getFloorId() {
		return getIntColumnValue(getFloorIdColumnName());
	}
	
	public int getBuildingId() {
		return getIntColumnValue(getBuildingIdColumnName());
	}
	
	public int getComplexId() {
		return getIntColumnValue(getComplexIdColumnName());
	}

	public Date getValidFrom() {
		return ((Date) getColumnValue(getColumnValidFrom()));
	}
	
	public Date getValidTo() {
		return ((Date) getColumnValue(getColumnValidTo()));
	}

	public Timestamp getDeliverTime() {
		return ((Timestamp) getColumnValue(getColumnDeliverdate()));
	}

	public Timestamp getReturnTime() {
		return ((Timestamp) getColumnValue(getColumnReturnDate()));
	}

	public boolean getIsRented() {
		return getBooleanColumnValue(getColumnRented());
	}

	public String getStatus() {
		return getStringColumnValue(getColumnStatus());
	}

	public void insert() throws SQLException {
	}
	
	public void delete() throws SQLException {
	}
}
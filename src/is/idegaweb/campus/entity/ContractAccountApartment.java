/*
 * $Id: ContractAccountApartment.java,v 1.4 2001/11/08 15:40:39 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.entity;


import com.idega.data.GenericEntity;
import java.sql.Date;
import java.lang.IllegalStateException;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ContractAccountApartment extends GenericEntity {
 /*
  "CAM_CONTRACT_ID",
  "IC_USER_ID",
  "BU_APARTMENT_ID",
  "FIN_ACCOUNT_ID",
  "FIN_CASHIER_ID",
  "BALANCE",
  "BU_APRT_TYPE_ID";
*/

/*
 CREATE VIEW "V_CONT_ACCT_APRT" (
  "CAM_CONTRACT_ID",
  "IC_USER_ID",
  "BU_APARTMENT_ID",
  "FIN_ACCOUNT_ID",
  "FIN_ACCOUNT_TYPE",
  "BALANCE",
  "BU_APRT_TYPE_ID",
  "BU_APRT_CAT_ID",
  "BU_FLOOR_ID",
  "BU_BUILDING_ID",
  "BU_COMPLEX_ID"
) AS


 select cc.cam_contract_id,cc.ic_user_id,cc.bu_apartment_id,
fa.fin_account_id,fa.account_type, fa.balance,ba.bu_aprt_type_id , bc.bu_aprt_cat_id,
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
and cc.status = 'S'
;
*/
  public static String getEntityTableName(){return "V_CONT_ACCT_APRT";}
  public static String getContractIdColumnName(){return "CAM_CONTRACT_ID";}
  public static String getUserIdColumnName(){return  "IC_USER_ID";}
  public static String getApartmentIdColumnName(){return "BU_APARTMENT_ID";}
  public static String getAccountIdColumnName(){return "FIN_ACCOUNT_ID";}
  public static String getAccountTypeColumnName(){return "FIN_ACCOUNT_TYPE";}
  public static String getBalanceColumnName(){return "BALANCE";}
  public static String getApartmentTypeIdColumnName(){return "BU_APRT_TYPE_ID";}
  public static String getApartmentCategoryIdColumnName(){return "BU_APRT_CAT_ID";}
  public static String getFloorIdColumnName(){return "BU_FLOOR_ID";}
  public static String getBuildingIdColumnName(){return "BU_BUILDING_ID";}
  public static String getComplexIdColumnName(){return "BU_COMPLEX_ID";}

  public ContractAccountApartment() {
  }
  public ContractAccountApartment(int id) throws SQLException {

  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getContractIdColumnName(),"Contract id",true,true,"java.lang.Integer");
    addAttribute(getUserIdColumnName(),"User id",true,true,"java.lang.Integer");
    addAttribute(getApartmentIdColumnName(),"Apartment id",true,true,"java.lang.Integer");
    addAttribute(getAccountIdColumnName(),"Account id",true,true,"java.lang.Integer");
    addAttribute(getAccountTypeColumnName(),"Account type",true,true,"java.lang.String");
    addAttribute(getBalanceColumnName(),"Balance",true,true,"java.lang.Integer");
    addAttribute(getApartmentTypeIdColumnName(),"Apartmenttype id",true,true,"java.lang.Integer");
    addAttribute(getApartmentCategoryIdColumnName(),"Apartmentcategory id",true,true,"java.lang.Integer");
    addAttribute(getFloorIdColumnName(),"Floor id",true,true,"java.lang.Integer");
    addAttribute(getBuildingIdColumnName(),"Building id",true,true,"java.lang.Integer");
    addAttribute(getComplexIdColumnName(),"Complex id",true,true,"java.lang.Integer");
  }
  public String getEntityName() {
    return(getEntityTableName());
  }
  public int getContractId(){
   return getIntColumnValue(getContractIdColumnName());
  }
  public int getUserId(){
    return getIntColumnValue(getUserIdColumnName());
  }
  public int getApartmentId(){
    return getIntColumnValue(getApartmentIdColumnName());
  }
  public int getApartmentTypeId(){
    return getIntColumnValue(getApartmentTypeIdColumnName());
  }
  public int getAccountId(){
    return getIntColumnValue(getAccountIdColumnName());
  }
  public int getAccountType(){
    return getIntColumnValue(getAccountTypeColumnName());
  }
  public int getBalanceId(){
    return getIntColumnValue(getBalanceColumnName());
  }
  public int getApartmentCategoryId(){
    return getIntColumnValue(getApartmentCategoryIdColumnName());
  }
  public int getFloorId(){
    return getIntColumnValue(getFloorIdColumnName());
  }
  public int getBuildingId(){
    return getIntColumnValue(getBuildingIdColumnName());
  }
  public int getComplexId(){
    return getIntColumnValue(getComplexIdColumnName());
  }
  public void insert()throws SQLException{

  }
  public void delete()throws SQLException{

  }
}

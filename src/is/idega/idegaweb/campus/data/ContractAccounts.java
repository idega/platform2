/*
 * $Id: ContractAccounts.java,v 1.2 2001/12/05 21:57:25 aron Exp $
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
import java.lang.IllegalStateException;
import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */
public class ContractAccounts extends GenericEntity {

/*

CREATE VIEW  "V_CONTRACT_ACCOUNTS" (
"CAM_CONTRACT_ID",
"IC_USER_ID",
"BU_APARTMENT_ID",
"FIN_ACCOUNT_ID",
"PHONE_ACCOUNT_ID",
"FIN_BALANCE",
"PHONE_BALANCE",
"BU_APRT_TYPE_ID",
"BU_APRT_CAT_ID",
"BU_FLOOR_ID",
"BU_BUILDING_ID",
"BU_COMPLEX_ID",
"CONTRACT_STATUS"
) AS

select
cc.cam_contract_id,
cc.ic_user_id,
cc.bu_apartment_id,
fa.fin_account_id,
fa2.fin_account_id phone_account_id,
fa.balance fin_balance,
fa2.balance phone_balance,
ba.bu_aprt_type_id ,
bc.bu_aprt_cat_id,
bf.bu_floor_id,
bb.bu_building_id,
bx.bu_complex_id,
cc.status contract_status

from cam_contract cc,fin_account fa, fin_account fa2,bu_apartment ba,
bu_floor bf,bu_building bb,bu_complex bx,bu_aprt_cat bc,bu_aprt_type bt
where cc.ic_user_id = fa.ic_user_id
and cc.bu_apartment_id = ba.bu_apartment_id
and ba.bu_floor_id = bf.bu_floor_id
and bf.bu_building_id = bb.bu_building_id
and bb.bu_complex_id = bx.bu_complex_id
and ba.bu_aprt_type_id = bt.bu_aprt_type_id
and bt.bu_aprt_cat_id = bc.bu_aprt_cat_id
and fa.ic_user_id = fa2.ic_user_id
and fa.account_type = 'FINANCE'
and fa2.account_type = 'PHONE'
and cc.status = 'S';
*/

  public static String getEntityTableName(){return "V_CONTRACT_ACCOUNTS";}
  public static String getColumnNameContractId(){return "CAM_CONTRACT_ID";}
  public static String getColumnNameUserId(){return  "IC_USER_ID";}
  public static String getColumnNameApartmentId(){return "BU_APARTMENT_ID";}
  public static String getColumnNameFinanceAccountId(){return "FIN_ACCOUNT_ID";}
  public static String getColumnNamePhoneAccountId(){return "PHONE_ACCOUNT_ID";}
  public static String getColumnNameFinanceBalance(){return "FIN_BALANCE";}
  public static String getColumnNamePhoneBalance(){return "PHONE_BALANCE";}
  public static String getColumnNameApartmentTypeId(){return "BU_APRT_TYPE_ID";}
  public static String getColumnNameApartmentCategoryId(){return "BU_APRT_CAT_ID";}
  public static String getColumnNameFloorId(){return "BU_FLOOR_ID";}
  public static String getColumnNameBuildingId(){return "BU_BUILDING_ID";}
  public static String getColumnNameComplexId(){return "BU_COMPLEX_ID";}
  public static String getColumnNameContractStatus(){return "CONTRACT_STATUS";}

  public ContractAccounts() {
  }
  public ContractAccounts(int id) throws SQLException {

  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameContractId(),"Contract id",true,true,Integer.class);
    addAttribute(getColumnNameUserId(),"User id",true,true,Integer.class);
    addAttribute(getColumnNameApartmentId(),"Apartment id",true,true,Integer.class);
    addAttribute(getColumnNameFinanceAccountId(),"Finance account id",true,true,Integer.class);
    addAttribute(getColumnNamePhoneAccountId(),"Phone account id",true,true,Integer.class);
    addAttribute(getColumnNameFinanceBalance(),"Finance Balance",true,true,Float.class);
    addAttribute(getColumnNamePhoneBalance(),"Phone Balance",true,true,Float.class);
    addAttribute(getColumnNameApartmentTypeId(),"Apartmenttype id",true,true,Integer.class);
    addAttribute(getColumnNameApartmentCategoryId(),"Apartmentcategory id",true,true,Integer.class);
    addAttribute(getColumnNameFloorId(),"Floor id",true,true,Integer.class);
    addAttribute(getColumnNameBuildingId(),"Building id",true,true,Integer.class);
    addAttribute(getColumnNameComplexId(),"Complex id",true,true,Integer.class);
    addAttribute(getColumnNameContractStatus(),"Complex id",true,true,String.class);
  }
  public String getEntityName() {
    return(getEntityTableName());
  }
  public int getContractId(){
   return getIntColumnValue(getColumnNameContractId());
  }
  public int getUserId(){
    return getIntColumnValue(getColumnNameUserId());
  }
  public int getApartmentId(){
    return getIntColumnValue(getColumnNameApartmentId());
  }
  public int getApartmentTypeId(){
    return getIntColumnValue(getColumnNameApartmentTypeId());
  }
  public int getFinanceAccountId(){
    return getIntColumnValue(getColumnNameFinanceAccountId());
  }
  public int getPhoneAccountId(){
    return getIntColumnValue(getColumnNamePhoneAccountId());
  }
  public float getFinanceBalance(){
    return getFloatColumnValue(getColumnNameFinanceBalance());
  }
  public float getPhoneBalance(){
    return getFloatColumnValue(getColumnNamePhoneBalance());
  }
  public int getApartmentCategoryId(){
    return getIntColumnValue(getColumnNameApartmentCategoryId());
  }
  public int getFloorId(){
    return getIntColumnValue(getColumnNameFloorId());
  }
  public int getBuildingId(){
    return getIntColumnValue(getColumnNameBuildingId());
  }
  public int getComplexId(){
    return getIntColumnValue(getColumnNameComplexId());
  }
  public String getContracStatus(){
    return getStringColumnValue(getColumnNameContractStatus());
  }
  public void insert()throws SQLException{

  }
  public void delete()throws SQLException{

  }
}

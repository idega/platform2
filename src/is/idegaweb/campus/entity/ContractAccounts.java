/*
 * $Id: ContractAccounts.java,v 1.2 2001/11/08 15:40:39 aron Exp $
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
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */
public class ContractAccounts extends GenericEntity {

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

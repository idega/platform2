/*
 * $Id: ContractAccountApartment.java,v 1.1 2001/07/23 10:05:31 aron Exp $
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
  public static String getEntityTableName(){return "V_CONT_ACCT_APRT";}
  public static String getContractIdColumnName(){return "CAM_CONTRACT_ID";}
  public static String getUserIdColumnName(){return  "IC_USER_ID";}
  public static String getApartmentIdColumnName(){return "BU_APARTMENT_ID";}
  public static String getAccountIdColumnName(){return "FIN_ACCOUNT_ID";}
  public static String getBalanceColumnName(){return "BALANCE";}
  public static String getApartmentTypeIdColumnName(){return "BU_APRT_TYPE_ID";}

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
    addAttribute(getBalanceColumnName(),"Balance",true,true,"java.lang.Integer");
    addAttribute(getApartmentTypeIdColumnName(),"Apartmenttype id",true,true,"java.lang.Integer");
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
  public int getBalanceId(){
    return getIntColumnValue(getBalanceColumnName());
  }
  public void insert()throws SQLException{

  }
  public void delete()throws SQLException{

  }
}
/*
 * $Id: ContractBMPBean.java,v 1.4 2003/04/03 07:24:44 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.contract.data;


import java.sql.Date;
import java.sql.SQLException;

import com.idega.util.IWTimestamp;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class ContractBMPBean extends com.idega.data.GenericEntity implements com.idega.block.contract.data.Contract {
  private static final String name_ = "con_contract";
	private static final String category_ = "con_category_id";
  private static final String userId_ = "ic_user_id";
  private static final String validFrom_ = "valid_from";
  private static final String validTo_ = "valid_to";
  private static final String status_ = "status";
  private static final String created_ = "created";
  private static final String statusDate_ = "status_date";

  public static final String statusCreated = "C";
  public static final String statusPrinted = "P";
  public static final String statusSigned = "S";
  public static final String statusRejected = "R";
  public static final String statusTerminated = "T";
  public static final String statusEnded = "E";
  public static final String statusResigned = "U";

  public static String getColumnNameStatus(){return status_;}
  public static String getColumnNameValidTo(){return validTo_;}
  public static String getColumnNameValidFrom(){return validFrom_;}
  public static String getColumnNameUserId(){return userId_;}
  public static String getColumnNameStatusDate(){return statusDate_ ;}
	public static String getColumnNameCreationDate(){return created_ ;}
  public static String getColumnNameContractEntityName(){return name_;}
	public static String getColumnNameCategoryId(){return category_;}

  public ContractBMPBean() {
  }
  public ContractBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
		addAttribute(category_,"Category",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.contract.data.ContractCategory.class);
    addAttribute(userId_,"Contract maker",true,true,java.lang.Integer.class,"one-to-many",com.idega.core.user.data.User.class);
    addAttribute(validFrom_,"Valid from",true,true,java.sql.Date.class);
    addAttribute(validTo_,"Valid to",true,true,java.sql.Date.class);
		addAttribute(statusDate_,"Status changed",true,true,java.sql.Date.class);
    addAttribute(status_,"Status",true,true,java.lang.String.class,1);
		addAttribute(created_,"Created",true,true,java.sql.Date.class);
		addManyToManyRelationShip(com.idega.core.data.ICFile.class);
		addMetaDataRelationship();

  }

  public String getEntityName() {
    return(name_);
  }

  public void setUserId(int id) {
    setColumn(userId_,id);
  }

  public void setUserId(Integer id) {
    setColumn(userId_,id);
  }

  public Integer getUserId() {
    return(getIntegerColumnValue(userId_));
  }

  public void setValidFrom(Date date) {
    setColumn(validFrom_,date);
  }

  public Date getValidFrom() {
    return((Date)getColumnValue(validFrom_));
  }

  public void setValidTo(Date date) {
    setColumn(validTo_,date);
  }

  public Date getValidTo() {
    return((Date)getColumnValue(validTo_));
  }

  public void setCreationDate(Date date) {
    setColumn(created_,date);
  }

  public Date getCreationDate(){
    return (Date) getColumnValue(created_ );
  }

  public void setStatusDate(Date date) {
    setColumn(statusDate_,date);
  }

  public Date getStatusDate(){
    return (Date) getColumnValue(statusDate_ );
  }

	public void setCategoryId(int id){
	  setColumn(category_,id);
	}

	public void setCategoryId(Integer id){
	  setColumn(category_,id);
	}

	public Integer getCategoryId(){
	  return getIntegerColumnValue(category_);
	}

  public void setStatus(String status) throws IllegalStateException {
    if ((status.equalsIgnoreCase(statusCreated)) ||
        (status.equalsIgnoreCase(statusEnded)) ||
        (status.equalsIgnoreCase(statusRejected)) ||
        (status.equalsIgnoreCase(statusSigned)) ||
        (status.equalsIgnoreCase(statusTerminated))||
        (status.equalsIgnoreCase(statusResigned))||
        (status.equalsIgnoreCase(statusPrinted))){
      setColumn(status_,status);
      setStatusDate(IWTimestamp.RightNow().getSQLDate());
    }
    else
      throw new IllegalStateException("Undefined state : " + status);
  }
  public String getStatus() {
    return((String)getColumnValue(status_));
  }
  public void setStatusCreated() {
    setStatus(statusCreated);
  }
  public void setStatusEnded() {
    setStatus(statusEnded);
  }
  public void setStatusRejected() {
    setStatus(statusRejected);
  }
  public void setStatusSigned() {
    setStatus(statusSigned);
  }
  public void setStatusTerminated() {
    setStatus(statusTerminated);
  }
  public void setStatusPrinted() {
    setStatus(statusPrinted);
  }
  public void setStatusResigned(){
    setStatus(statusResigned);
  }
}

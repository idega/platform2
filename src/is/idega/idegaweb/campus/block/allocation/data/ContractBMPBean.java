/*
 * $Id: ContractBMPBean.java,v 1.8 2004/05/24 14:21:41 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.allocation.data;


import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.ejb.FinderException;

import com.idega.util.IWTimestamp;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ContractBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.campus.block.allocation.data.Contract {
  private static final String name_ = "cam_contract";
  private static final String userId_ = "ic_user_id";
  private static final String apartmentId_ = "bu_apartment_id";
  private static final String validFrom_ = "valid_from";
  private static final String validTo_ = "valid_to";
  private static final String status_ = "status";
  private static final String rented_ = "rented";
  private static final String applicantId_ = "app_applicant_id";
  private static final String resignInfo_ = "resign_info";
  private static final String statusDate_ = "status_date";
  private static final String movingDate_ = "moving_date";
  private static final String deliverDate_ = "deliver_date";
  private static final String returnDate_ = "return_date";
  private static final String file_ = "ic_file_id";

  public static final String statusCreated = "C";
  public static final String statusPrinted = "P";
  public static final String statusSigned = "S";
  public static final String statusRejected = "R";
  public static final String statusTerminated = "T";
  public static final String statusEnded = "E";
  public static final String statusResigned = "U";
  public static final String statusGarbage = "G";
  public static final String statusStorage = "Z";
  public static final String statusDenied = "D";

  public static String getStatusColumnName(){return status_;}
  public static String getApplicantIdColumnName(){return applicantId_;}
  public static String getValidToColumnName(){return validTo_;}
  public static String getValidFromColumnName(){return validFrom_;}
  public static String getApartmentIdColumnName(){return apartmentId_;}
  public static String getUserIdColumnName(){return userId_;}
  public static String getResignInfoColumnName(){return resignInfo_ ;}
  public static String getStatusDateColumnName(){return movingDate_ ;}
  public static String getRentedColumnName(){return rented_ ;}
  public static String getMovingDateColumnName(){return movingDate_ ;}
  public static String getColumnReturnDate(){return returnDate_ ;}
  public static String getColumnDeliverDate(){return deliverDate_ ;}
  public static String getFileColumnName(){return file_;}
  public static String getContractEntityName(){return name_;}


  public ContractBMPBean() {
  }
  public ContractBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(userId_,"User id",true,true,java.lang.Integer.class,"one-to-many",com.idega.core.user.data.User.class);
    addAttribute(apartmentId_,"Apartment id",true,true,java.lang.Integer.class,"one-to-many",com.idega.block.building.data.Apartment.class);
    addAttribute(applicantId_,"Applicant id",true,true,java.lang.Integer.class,"one-to-one",com.idega.block.application.data.Applicant.class);
    addAttribute(validFrom_,"Valid from",true,true,java.sql.Date.class);
    addAttribute(validTo_,"Valid to",true,true,java.sql.Date.class);
    addAttribute(statusDate_,"Resign date",true,true,java.sql.Date.class);
    addAttribute(movingDate_,"Moving date",true,true,java.sql.Date.class);
    addAttribute(deliverDate_,"Deliver date",true,true,java.sql.Timestamp.class);
    addAttribute(returnDate_,"Return date",true,true,java.sql.Timestamp.class);
    addAttribute(status_,"Status",true,true,java.lang.String.class,1);
    addAttribute(rented_,"Rented",true,true,java.lang.Boolean.class,1);
    addAttribute(resignInfo_,"Resign info",true,true,java.lang.String.class,4000);
    addAttribute(file_,"File id",true,true,java.lang.Integer.class);
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
  public void setApplicantId(int id) {
    setColumn(applicantId_,id);
  }

  public void setApplicantId(Integer id) {
    setColumn(applicantId_,id);
  }

	public Integer getFileId() {
    return(getIntegerColumnValue(file_));
  }
  public void setFileId(int id) {
    setColumn(file_,id);
  }

  public void setFileId(Integer id) {
    setColumn(file_,id);
  }

  public Integer getApplicantId() {
    return(getIntegerColumnValue(applicantId_));
  }

  public void setApartmentId(int id) {
    setColumn(apartmentId_,id);
  }

  public void setApartmentId(Integer id) {
    setColumn(apartmentId_,id);
  }

  public Integer getApartmentId() {
    return(getIntegerColumnValue(apartmentId_));
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

  public void setMovingDate(Date date) {
    setColumn(movingDate_,date);
  }

  public Date getMovingDate(){
    return (Date) getColumnValue(movingDate_ );
  }

  public void setStatusDate(Date date) {
    setColumn(statusDate_,date);
  }

  public Date getStatusDate(){
    return (Date) getColumnValue(statusDate_ );
  }

   public void setDeliverTime(Timestamp stamp) {
    setColumn(deliverDate_,stamp);
  }

  public Timestamp getDeliverTime() {
    return((Timestamp)getColumnValue(deliverDate_));
  }

   public void setReturnTime(Timestamp stamp) {
    setColumn(returnDate_,stamp);
  }

  public Timestamp getReturnTime() {
    return((Timestamp)getColumnValue(returnDate_));
  }

  public String getResignInfo(){
    return getStringColumnValue( resignInfo_);
  }

  public void setResignInfo(String info){
     setColumn(resignInfo_ , info);
  }

  public boolean  getIsRented(){
    return getBooleanColumnValue( rented_);
  }

  public void setIsRented(boolean rented){
     setColumn(rented_ , rented);
  }

  public void setEnded(){
    setIsRented(false);
    setReturnTime(IWTimestamp.getTimestampRightNow());
  }

   public void setStarted(){
//    setIsRented(true);
    setStarted(IWTimestamp.getTimestampRightNow());
  }

   public void setStarted(Timestamp when){
    setIsRented(true);
    setDeliverTime(when);
  }

  public void setStatus(String status) throws IllegalStateException {
    if ((status.equalsIgnoreCase(statusCreated)) ||
        (status.equalsIgnoreCase(statusEnded)) ||
        (status.equalsIgnoreCase(statusRejected)) ||
        (status.equalsIgnoreCase(statusSigned)) ||
        (status.equalsIgnoreCase(statusTerminated))||
        (status.equalsIgnoreCase(statusResigned))||
        (status.equalsIgnoreCase(statusGarbage))||
		(status.equalsIgnoreCase(statusStorage))||
		(status.equalsIgnoreCase(statusDenied))||
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
  public void setStatusGarbage(){
    setStatus(statusGarbage);
  }
  public void setStatusDenied(){
	 setStatus(statusDenied);
   }
  public void setStatusStorage(){
	  setStatus(statusStorage);
 }
 public java.util.Collection ejbFindByApplicant(Integer ID) throws FinderException{
 	return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),ID.intValue()));
 }
}

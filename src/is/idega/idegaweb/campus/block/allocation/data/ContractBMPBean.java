/*
 * $Id: ContractBMPBean.java,v 1.18 2004/07/30 14:04:16 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.allocation.data;



import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.data.Apartment;
import com.idega.data.IDOBoolean;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.User;
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
  public static final String statusFinalized = "F";

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
    addAttribute(userId_,"User id",true,true,java.lang.Integer.class,"one-to-many",User.class);
    addAttribute(apartmentId_,"Apartment id",true,true,java.lang.Integer.class,"one-to-many",Apartment.class);
    addAttribute(applicantId_,"Applicant id",true,true,java.lang.Integer.class,"one-to-one",Applicant.class);
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
  
  public User getUser(){
  		return (User) getColumnValue(userId_);
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
  
  public Applicant getApplicant(){
  	return (Applicant)getColumnValue(applicantId_);
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
  
  public Apartment getApartment(){
  	return (Apartment)getColumnValue(apartmentId_);
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
        (status.equalsIgnoreCase(statusPrinted))||
		(status.equalsIgnoreCase(statusFinalized))
		){
      setColumn(status_,status);
      setStatusDate(new Date(System.currentTimeMillis()));
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
  public void setStatusFinalized(){
	  setStatus(statusFinalized);
}

 
 public Collection ejbFindByApplicantID(Integer ID) throws FinderException{
 	return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),ID.intValue()));
 }
 
 public Collection ejbFindByUserID(Integer ID) throws FinderException{
	 return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getUserIdColumnName(),ID.intValue()));
  }
  
  public Collection ejbFindByApartmentAndUser(Integer AID,Integer UID) throws FinderException{
	  return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getUserIdColumnName(),UID.intValue()).appendAndEquals(getApartmentIdColumnName(),AID.intValue()));
   }
  
  public Collection ejbFindByUserAndRented(Integer ID,Boolean rented) throws FinderException{
	   return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getUserIdColumnName(),ID.intValue()).appendAndEqualsQuoted(getRentedColumnName(),IDOBoolean.toString(rented.booleanValue())));
	}
  
  	public Collection ejbFindByApartmentID(Integer ID) throws FinderException{
	   return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApartmentIdColumnName(),ID.intValue()).appendOrderByDescending(getValidToColumnName()));
	}
	
	public Collection ejbFindByApartmentAndStatus(Integer ID,String status)throws FinderException{
			return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApartmentIdColumnName(),ID.intValue()).appendAndEqualsQuoted(getStatusColumnName(),status).appendOrderByDescending(getValidToColumnName()));
		}
	
	public Collection ejbFindByApartmentAndStatus(Integer ID,String[] status)throws FinderException{
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApartmentIdColumnName(),ID.intValue()).appendAnd().append(getStatusColumnName()).appendInArrayWithSingleQuotes(status).appendOrderByDescending(getValidToColumnName()));
	}

	public Collection ejbFindByApplicantAndStatus(Integer ID,String status)throws FinderException{
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),ID.intValue()).appendAndEqualsQuoted(getStatusColumnName(),status));
	}
	
	public Collection ejbFindByApplicantAndRented(Integer ID,Boolean rented)throws FinderException{
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),ID.intValue()).appendAndEqualsQuoted(getRentedColumnName(),IDOBoolean.toString(rented.booleanValue())));
	}
	
	public Collection ejbFindByApartmentAndRented(Integer ID,Boolean rented)throws FinderException{
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApartmentIdColumnName(),ID.intValue()).appendAndEqualsQuoted(getRentedColumnName(),IDOBoolean.toString(rented.booleanValue())));
	}
	
	public Collection ejbFindByStatus(String status)throws FinderException{
		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEqualsQuoted(getStatusColumnName(),status));
	}
	public Collection ejbFindAll() throws FinderException{
		return super.idoFindAllIDsBySQL();
	}
	
	public Collection ejbFindBySQL(String sql) throws FinderException{
		return super.idoFindPKsBySQL(sql);
	}

	 public java.util.Collection ejbFindByApplicant(Integer ID) throws FinderException{
	 	return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getApplicantIdColumnName(),ID.intValue()));
	 }
	 
	 public Collection ejbFindByApplicantInCreatedStatus(Integer applicant)throws FinderException{
	 	return ejbFindByApplicantAndStatus(applicant,statusCreated);
	 }
	 
	 public Date ejbHomeGetLastValidToForApartment(Integer apartment )throws FinderException{
	 	if(apartment!=null){
		 	try {
				return getDateTableValue("select max(c.valid_to) from cam_contract c where c.bu_apartment_id =  "+apartment);
			} catch (SQLException e) {
				throw new FinderException(e.getMessage());
			}
	 	}
	 	return null;
	 }
	 
	 public Date ejbHomeGetLastValidFromForApartment(Integer apartment )throws FinderException{
	 	if(apartment!=null){
		 	try {
				return getDateTableValue("select max(c.valid_from) from cam_contract c where c.bu_apartment_id =  "+apartment);
			} catch (SQLException e) {
				throw new FinderException(e.getMessage());
			}
	 	}
	 	return null;
	 }
	 
	 public Collection ejbFindBySearchConditions(String status,Integer complexId,Integer buildingId,Integer floorId,Integer typeId,Integer categoryId,int order,int returnResultSize,int startingIndex)throws FinderException{
	 	 String sql = getSearchConditionSQL(status, complexId, buildingId, floorId, typeId, categoryId, order,false);
	     return super.idoFindPKsBySQL(sql.toString(),returnResultSize,startingIndex);
	 }
	 
	 public int ejbHomeCountBySearchConditions(String status,Integer complexId,Integer buildingId,Integer floorId,Integer typeId,Integer categoryId,int order)throws IDOException{
	 	String sql = getSearchConditionSQL(status, complexId, buildingId, floorId, typeId, categoryId, order,true);
	 	return idoGetNumberOfRecords(sql);
	 }
	private String getSearchConditionSQL(String status, Integer complexId, Integer buildingId, Integer floorId, Integer typeId, Integer categoryId, int order,boolean count) {
		StringBuffer sql = new StringBuffer("select ");
		if(count)
			sql.append(" count( * )");
		else 
			sql.append(" con.* ");
	     sql.append(" from bu_apartment a,bu_floor f,bu_building b,app_applicant p ");
	     sql.append(",bu_complex c,bu_aprt_type t,bu_aprt_cat y,cam_contract con ");
	     sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
	     sql.append(" and t.bu_aprt_cat_id = y.bu_aprt_cat_id");
	     sql.append(" and a.bu_floor_id = f.bu_floor_id ");
	     sql.append(" and f.bu_building_id = b.bu_building_id ");
	     sql.append(" and b.bu_complex_id = c.bu_complex_id ");
	     sql.append(" and a.bu_apartment_id = con.bu_apartment_id");
	     sql.append(" and con.app_applicant_id = p.app_applicant_id");
	     if(status !=null && !"".equals(status)){
	       sql.append(" and con.status = '");
	       sql.append(status);
	       sql.append("' ");
	     }
	     if(complexId !=null && complexId.intValue()>0){
	       sql.append(" and bu_complex_id  = ");
	       sql.append(complexId);
	     }
	     if(buildingId !=null && buildingId.intValue()>0){
	       sql.append(" and bu_building_id = ");
	       sql.append(buildingId);
	     }
	     if(floorId !=null && floorId.intValue()>0){
	       sql.append(" and bu_floor_id = ");
	       sql.append(floorId);
	     }
	     if(typeId !=null && typeId.intValue()>0){
	       sql.append(" and bu_aprt_type_id = ");
	       sql.append(typeId);
	     }
	     if(categoryId !=null && categoryId.intValue()>0){
	       sql.append(" and bu_aprt_cat_id = ");
	       sql.append(categoryId);
	     }
	     if(!count && order>=0){
	       sql.append(" order by ");
	       sql.append(ContractFinder.getOrderString(order));
	     }
		return sql.toString();
	}
	
	public Collection ejbFindByComplexAndBuildingAndApartmentName(Integer complexID,Integer buildingID,String apartmentName) throws FinderException{
		 StringBuffer sql = new StringBuffer("select con.* ");
		    sql.append(" from bu_apartment a, bu_floor f, bu_building b, cam_contract con ");
		    sql.append(" where a.bu_floor_id = f.bu_floor_id ");
		    sql.append(" and f.bu_building_id = b.bu_building_id ");
		    sql.append(" and a.bu_apartment_id = con.bu_apartment_id");
		    sql.append(" and b.bu_complex_id  = ");
		    sql.append(complexID);
		    sql.append(" and b.bu_building_id = ");
		    sql.append(buildingID);
		    sql.append(" and a.name = '");
				sql.append(apartmentName);
				sql.append("'");
			return super.idoFindPKsBySQL(sql.toString());
	}
	public Collection ejbFindByPersonalID(String ID)throws FinderException{
		StringBuffer sql = new StringBuffer("select c.* ");
	    sql.append(" from cam_contract c, app_applicant a where ");
	    sql.append(" c.app_applicant_id = a.app_applicant_id and a.ssn like '");
	    sql.append(ID);
	    sql.append("'");
	    return super.idoFindPKsBySQL(sql.toString());
	}
	
	public Collection ejbHomeGetUnsignedApplicants(String personalID)throws FinderException{
		 try {
			StringBuffer sql = new StringBuffer("select a.* ");
			 sql.append(" from app_applicant a ");
			 sql.append(" where a.app_applicant_id ");
			 sql.append(" not in (select c.app_applicant_id from cam_contract c) ");
			 sql.append(" and ssn like '");
			 sql.append(personalID);
			 sql.append("'");
			 return idoGetRelatedEntitiesBySQL(Applicant.class,sql.toString());
		} catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}  
		
	}

	public Collection ejbFindByStatusAndValidBeforeDate(String status, Date date)throws FinderException{
		return idoFindPKsByQuery( super.idoQueryGetSelect().appendWhereEquals(getStatusDateColumnName(),status).appendAnd().append(getValidToColumnName()).appendLessThanOrEqualsSign().append(date));
	}
	public Collection ejbFindByStatusAndChangeDate(String status, Date date)throws FinderException{
		return idoFindPKsByQuery( super.idoQueryGetSelect().appendWhereEquals(getStatusDateColumnName(),status).appendAnd().append(getStatusDateColumnName()).appendLessThanOrEqualsSign().append(date));
	}
	private IDOQuery getQueryByStatusAndOverlapPeriod(String[] status,Date from,Date to){
		IDOQuery query = super.idoQueryGetSelect().appendWhere();
		query.append(getStatusColumnName());
		query.appendInArrayWithSingleQuotes(status);
		query.appendAnd();
		query.appendOverlapPeriod(getValidFromColumnName(),getValidToColumnName(),from,to);
		return query;
	}
	
	public Collection ejbFindByStatusAndOverLapPeriodMultiples(String[] status,Date from,Date to)throws FinderException{
		IDOQuery outerQuery = getQueryByStatusAndOverlapPeriod(status,from,to);
		IDOQuery innerQuery= 	super.idoQuery().appendSelect().append(getUserIdColumnName());
		innerQuery.appendFrom().append(this.getTableName());
		innerQuery.appendWhere();
		innerQuery.append(getStatusColumnName());
		innerQuery.appendInArrayWithSingleQuotes(status);
		innerQuery.appendAnd();
		innerQuery.appendOverlapPeriod(getValidFromColumnName(),getValidToColumnName(),from,to);
		innerQuery.appendGroupBy(getUserIdColumnName());
		innerQuery.appendHaving().appendCount(getIDColumnName()).appendGreaterThanSign().append(1);
		outerQuery.appendAnd().append(getUserIdColumnName()).appendIn(innerQuery);
		
		outerQuery.appendOrderBy(getUserIdColumnName()+","+getIDColumnName());
		//System.out.println(outerQuery.toString());
				
		return super.idoFindPKsByQuery(outerQuery);
	}
	public Collection ejbFindByUserAndStatus(Integer userId,String[] status)throws FinderException{
		return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(getUserIdColumnName(),userId).appendAnd().append(getStatusColumnName()).appendInArrayWithSingleQuotes(status));
	}
	public Collection ejbFindByUserAndStatus(Integer userId,String status)throws FinderException{
		return super.idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(getUserIdColumnName(),userId).appendAndEqualsQuoted(getStatusColumnName(),status));
	}
}

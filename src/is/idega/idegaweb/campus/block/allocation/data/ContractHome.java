package is.idega.idegaweb.campus.block.allocation.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;


public interface ContractHome extends com.idega.data.IDOHome
{
 public Contract create() throws javax.ejb.CreateException;
 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByApartmentAndRented(java.lang.Integer p0,java.lang.Boolean p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApplicantAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySearchConditions(java.lang.String p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,java.lang.Integer p5,java.lang.String p6,int p7,int p8)throws javax.ejb.FinderException;
 public int countBySearchConditions(java.lang.String p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,java.lang.Integer p5,java.lang.String p6)throws com.idega.data.IDOException;
 public java.sql.Date getLastValidFromForApartment(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.sql.Date getLastValidToForApartment(java.lang.Integer p0)throws javax.ejb.FinderException;
 public Collection findByApplicantID(Integer ID) throws FinderException;
 public Collection findByUserID(Integer ID) throws FinderException;
  public Collection findByApartmentAndUser(Integer AID,Integer UID) throws FinderException;
  public Collection findByUserAndRented(Integer ID,Boolean rented) throws FinderException;
  	public Collection findByApartmentID(Integer ID) throws FinderException;
	public Collection findByApartmentAndStatus(Integer ID,String status)throws FinderException;
	
	public Collection findByApplicantAndRented(Integer ID,Boolean rented)throws FinderException;
	
	public Collection findByStatus(String status)throws FinderException;
	public Collection findAll() throws FinderException;
	public Collection findBySQL(String sql) throws FinderException;
 public java.util.Collection findByApplicant(Integer ID) throws FinderException;
 public Collection findByApplicantInCreatedStatus(Integer applicant)throws FinderException;
 public Collection findByComplexAndBuildingAndApartmentName(Integer complexID,Integer buildingID,String apartmentName) throws FinderException;
 public Collection findByPersonalID(String ID)throws FinderException;
 public Collection getUnsignedApplicants(String personalID)throws FinderException;
 public Collection findByStatusAndBeforeDate(String status, Date date)throws FinderException;
}
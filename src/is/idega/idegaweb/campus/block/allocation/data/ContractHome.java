package is.idega.idegaweb.campus.block.allocation.data;


public interface ContractHome extends com.idega.data.IDOHome
{
 public Contract create() throws javax.ejb.CreateException;
 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByApartmentAndRented(java.lang.Integer p0,java.lang.Boolean p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApartmentAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApartmentAndStatus(java.lang.Integer p0,java.lang.String[] p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApartmentAndUser(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApartmentID(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByApplicant(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByApplicantAndRented(java.lang.Integer p0,java.lang.Boolean p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApplicantAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApplicantID(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByApplicantInCreatedStatus(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByComplexAndBuildingAndApartmentName(java.lang.Integer p0,java.lang.Integer p1,java.lang.String p2)throws javax.ejb.FinderException;
 public java.util.Collection findByPersonalID(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySearchConditions(java.lang.String p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,java.lang.Integer p5,int p6,int p7,int p8)throws javax.ejb.FinderException;
 public java.util.Collection findByStatus(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findByStatusAndChangeDate(java.lang.String p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public java.util.Collection findByStatusAndOverLapPeriodMultiples(java.lang.String[] p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException;
 public java.util.Collection findByStatusAndValidBeforeDate(java.lang.String p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public java.util.Collection findByUserAndRented(java.lang.Integer p0,java.lang.Boolean p1)throws javax.ejb.FinderException;
 public java.util.Collection findByUserID(java.lang.Integer p0)throws javax.ejb.FinderException;
 public int countBySearchConditions(java.lang.String p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,java.lang.Integer p5,int p6)throws com.idega.data.IDOException;
 public java.sql.Date getLastValidFromForApartment(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.sql.Date getLastValidToForApartment(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection getUnsignedApplicants(java.lang.String p0)throws javax.ejb.FinderException;

}
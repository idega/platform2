package is.idega.idegaweb.campus.block.allocation.data;


public interface ContractHome extends com.idega.data.IDOHome
{
 public Contract create() throws javax.ejb.CreateException;
 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByApartmentAndRented(java.lang.Integer p0,java.lang.Boolean p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApplicant(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByApplicantAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApplicantInCreatedStatus(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySearchConditions(java.lang.String p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,java.lang.Integer p5,java.lang.String p6,int p7,int p8)throws javax.ejb.FinderException;
 public int countBySearchConditions(java.lang.String p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,java.lang.Integer p5,java.lang.String p6)throws com.idega.data.IDOException;
 public java.sql.Date getLastValidFromForApartment(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.sql.Date getLastValidToForApartment(java.lang.Integer p0)throws javax.ejb.FinderException;

}
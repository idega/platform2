package is.idega.idegaweb.campus.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;


public interface ContractAccountApartmentHome extends com.idega.data.IDOHome
{
 public ContractAccountApartment create() throws javax.ejb.CreateException;
 public ContractAccountApartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByAccount(java.lang.Integer p0)throws javax.ejb.FinderException;
 public ContractAccountApartment findByAccountAndRented(java.lang.Integer p0,boolean p1)throws javax.ejb.FinderException;
 public java.util.Collection findByAccountAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApartment(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByAssessmentRound(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findByType(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findByTypeAndStatusAndOverlapPeriod(java.lang.String p0,java.lang.String[] p1,java.sql.Date p2,java.sql.Date p3)throws javax.ejb.FinderException;
 public java.util.Collection findByTypeAndStatusAndOverlapPeriodAndNotInRound(java.lang.String p0,java.lang.String[] p1,java.sql.Date p2,java.sql.Date p3,java.lang.Integer p4)throws javax.ejb.FinderException;
 public Collection findByTypeAndStatusAndOverLapPeriodMultiples(String type,String[] status,Date from,Date to)throws FinderException;
 public ContractAccountApartment findByUser(java.lang.Integer p0)throws javax.ejb.FinderException;

}
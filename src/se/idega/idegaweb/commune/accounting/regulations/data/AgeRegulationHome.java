package se.idega.idegaweb.commune.accounting.regulations.data;


public interface AgeRegulationHome extends com.idega.data.IDOHome
{
 public AgeRegulation create() throws javax.ejb.CreateException;
 public AgeRegulation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByPeriod(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException;

}
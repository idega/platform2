package se.idega.idegaweb.commune.accounting.regulations.data;


public interface RegulationHome extends com.idega.data.IDOHome
{
 public Regulation create() throws javax.ejb.CreateException;
 public Regulation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllRegulations()throws javax.ejb.FinderException;
 public Regulation findRegulation(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByPeriode(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException;

}
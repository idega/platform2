package se.idega.idegaweb.commune.accounting.regulations.data;


public interface RegulationHome extends com.idega.data.IDOHome
{
 public Regulation create() throws javax.ejb.CreateException;
 public Regulation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllBy()throws javax.ejb.FinderException;
 public java.util.Collection findAllByMainRule(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllByRegulationSpecType(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllRegulations()throws javax.ejb.FinderException;
 public Regulation findRegulation(int p0)throws javax.ejb.FinderException;
 public Regulation findRegulationOverlap(java.lang.String p0,java.sql.Date p1,java.sql.Date p2,se.idega.idegaweb.commune.accounting.regulations.data.Regulation p3)throws javax.ejb.FinderException;
 public java.util.Collection findRegulations(java.sql.Date p0,java.sql.Date p1,java.lang.String p2,int p3,int p4,int p5,int p6)throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByNameNoCase(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByNameNoCaseAndCategory(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByNameNoCaseAndDate(java.lang.String p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByNameNoCaseDateAndCategory(java.lang.String p0,java.sql.Date p1,java.lang.String p2)throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByPeriod(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByPeriod(java.sql.Date p0,java.sql.Date p1,java.lang.String p2,int p3,int p4)throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByPeriodAndOperationId(java.sql.Date p0,java.lang.String p1)throws javax.ejb.FinderException;

}
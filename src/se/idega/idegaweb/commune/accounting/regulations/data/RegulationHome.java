package se.idega.idegaweb.commune.accounting.regulations.data;

import java.sql.Date;


public interface RegulationHome extends com.idega.data.IDOHome
{
 public Regulation create() throws javax.ejb.CreateException;
 public Regulation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllBy()throws javax.ejb.FinderException;
 public java.util.Collection findAllRegulations()throws javax.ejb.FinderException;
 public Regulation findRegulation(int p0)throws javax.ejb.FinderException;
 public Regulation findRegulationOverlap(java.lang.String p0,java.sql.Date p1,java.sql.Date p2,se.idega.idegaweb.commune.accounting.regulations.data.Regulation p3)throws javax.ejb.FinderException;
 public java.util.Collection findRegulations(java.sql.Date p0,java.sql.Date p1,java.lang.String p2,int p3,int p4,int p5)throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByPeriod(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByPeriod(java.sql.Date p0,java.sql.Date p1,java.lang.String p2,int p3,int p4)throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByNameNoCase(String name) throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByNameNoCaseAndDate(String string, Date validDate) throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByNameNoCaseAndCategory(String name, String catId) throws javax.ejb.FinderException;
 public java.util.Collection findRegulationsByNameNoCaseDateAndCategory(String string, Date validDate, String catId) throws javax.ejb.FinderException;



}
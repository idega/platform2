package se.idega.idegaweb.commune.accounting.regulations.data;


public interface VATRegulationHome extends com.idega.data.IDOHome
{
 public VATRegulation create() throws javax.ejb.CreateException;
 public VATRegulation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByPeriod(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException;

}
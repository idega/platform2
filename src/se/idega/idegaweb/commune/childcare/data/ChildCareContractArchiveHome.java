package se.idega.idegaweb.commune.childcare.data;


public interface ChildCareContractArchiveHome extends com.idega.data.IDOHome
{
 public ChildCareContractArchive create() throws javax.ejb.CreateException;
 public ChildCareContractArchive findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByApplication(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByChild(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByChildAndProvider(int p0,int p1)throws javax.ejb.FinderException;
 public ChildCareContractArchive findByContractFileID(int p0)throws javax.ejb.FinderException;
 public ChildCareContractArchive findValidContractByApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException;

}
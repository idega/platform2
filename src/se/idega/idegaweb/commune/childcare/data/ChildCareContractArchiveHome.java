package se.idega.idegaweb.commune.childcare.data;


public interface ChildCareContractArchiveHome extends com.idega.data.IDOHome
{
 public ChildCareContractArchive create() throws javax.ejb.CreateException;
 public ChildCareContractArchive findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByApplication(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByChild(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByChildAndProvider(int p0,int p1)throws javax.ejb.FinderException;
 public ChildCareContractArchive findByContractFileID(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findFutureContractsByApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public ChildCareContractArchive findLatestContractByChild(int p0)throws javax.ejb.FinderException;
 public ChildCareContractArchive findLatestTerminatedContractByChild(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public ChildCareContractArchive findValidContractByApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public ChildCareContractArchive findApplicationByContract(int p0)throws javax.ejb.FinderException;
 public ChildCareContractArchive findValidContractByChild(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public int getFutureContractsCountByApplication(int p0,java.sql.Date p1)throws com.idega.data.IDOException;
 public int getContractsCountByApplication(int p0)throws com.idega.data.IDOException;
 public int getNumberOfActiveForApplication(int p0,java.sql.Date p1)throws com.idega.data.IDOException;
 public int getNumberOfActiveNotWithProvider(int p0,int p1)throws com.idega.data.IDOException;
 public int getNumberOfTerminatedLaterNotWithProvider(int p0,int p1,java.sql.Date p2)throws com.idega.data.IDOException;

}
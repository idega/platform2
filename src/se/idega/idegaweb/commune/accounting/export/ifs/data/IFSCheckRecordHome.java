package se.idega.idegaweb.commune.accounting.export.ifs.data;


public interface IFSCheckRecordHome extends com.idega.data.IDOHome
{
 public IFSCheckRecord create() throws javax.ejb.CreateException;
 public IFSCheckRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findAllByHeader(se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckHeader p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllByHeaderId(int p0)throws javax.ejb.FinderException;

}
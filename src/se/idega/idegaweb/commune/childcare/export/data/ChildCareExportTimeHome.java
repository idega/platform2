package se.idega.idegaweb.commune.childcare.export.data;


public interface ChildCareExportTimeHome extends com.idega.data.IDOHome
{
 public ChildCareExportTime create() throws javax.ejb.CreateException;
 public ChildCareExportTime findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ChildCareExportTime findByFileName(java.lang.String p0)throws javax.ejb.FinderException;
 public ChildCareExportTime findLatest(int p0)throws javax.ejb.FinderException;

}
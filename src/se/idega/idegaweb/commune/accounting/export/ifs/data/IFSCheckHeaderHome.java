package se.idega.idegaweb.commune.accounting.export.ifs.data;


public interface IFSCheckHeaderHome extends com.idega.data.IDOHome
{
 public IFSCheckHeader create() throws javax.ejb.CreateException;
 public IFSCheckHeader findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public IFSCheckHeader findBySchoolCategory(java.lang.String p0)throws javax.ejb.FinderException;
 public IFSCheckHeader findBySchoolCategory(com.idega.block.school.data.SchoolCategory p0)throws javax.ejb.FinderException;

}
package se.idega.idegaweb.commune.accounting.export.ifs.data;


public interface JournalLogHome extends com.idega.data.IDOHome
{
 public JournalLog create() throws javax.ejb.CreateException;
 public JournalLog findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolCategory(com.idega.block.school.data.SchoolCategory p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolCategory(java.lang.String p0)throws javax.ejb.FinderException;

}
package se.idega.idegaweb.commune.accounting.invoice.data;


public interface BatchRunHome extends com.idega.data.IDOHome
{
 public BatchRun create() throws javax.ejb.CreateException;
 public BatchRun findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public BatchRun findBySchoolCategory(com.idega.block.school.data.SchoolCategory p0, boolean p1)throws javax.ejb.FinderException;

}
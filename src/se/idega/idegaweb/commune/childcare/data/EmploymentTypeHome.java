package se.idega.idegaweb.commune.childcare.data;


public interface EmploymentTypeHome extends com.idega.data.IDOHome
{
 public EmploymentType create() throws javax.ejb.CreateException;
 public EmploymentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}
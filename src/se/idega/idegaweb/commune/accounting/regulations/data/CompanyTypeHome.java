package se.idega.idegaweb.commune.accounting.regulations.data;


public interface CompanyTypeHome extends com.idega.data.IDOHome
{
 public CompanyType create() throws javax.ejb.CreateException;
 public CompanyType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllCompanyTypes()throws javax.ejb.FinderException;
 public CompanyType findCompanyType(int p0)throws javax.ejb.FinderException;

}
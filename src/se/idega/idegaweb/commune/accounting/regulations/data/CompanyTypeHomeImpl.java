package se.idega.idegaweb.commune.accounting.regulations.data;


public class CompanyTypeHomeImpl extends com.idega.data.IDOFactory implements CompanyTypeHome
{
 protected Class getEntityInterfaceClass(){
  return CompanyType.class;
 }


 public CompanyType create() throws javax.ejb.CreateException{
  return (CompanyType) super.createIDO();
 }


public java.util.Collection findAllCompanyTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CompanyTypeBMPBean)entity).ejbFindAllCompanyTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public CompanyType findCompanyType(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((CompanyTypeBMPBean)entity).ejbFindCompanyType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public CompanyType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CompanyType) super.findByPrimaryKeyIDO(pk);
 }



}
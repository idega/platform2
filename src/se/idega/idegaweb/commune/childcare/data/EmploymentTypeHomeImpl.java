package se.idega.idegaweb.commune.childcare.data;


public class EmploymentTypeHomeImpl extends com.idega.data.IDOFactory implements EmploymentTypeHome
{
 protected Class getEntityInterfaceClass(){
  return EmploymentType.class;
 }


 public EmploymentType create() throws javax.ejb.CreateException{
  return (EmploymentType) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((EmploymentTypeBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public EmploymentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (EmploymentType) super.findByPrimaryKeyIDO(pk);
 }



}
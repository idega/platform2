package se.idega.idegaweb.commune.accounting.regulations.data;


public class SpecialCalculationTypeHomeImpl extends com.idega.data.IDOFactory implements SpecialCalculationTypeHome
{
 protected Class getEntityInterfaceClass(){
  return SpecialCalculationType.class;
 }


 public SpecialCalculationType create() throws javax.ejb.CreateException{
  return (SpecialCalculationType) super.createIDO();
 }


public java.util.Collection findAllConditionTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SpecialCalculationTypeBMPBean)entity).ejbFindAllConditionTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public SpecialCalculationType findConditionType(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SpecialCalculationTypeBMPBean)entity).ejbFindConditionType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SpecialCalculationType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SpecialCalculationType) super.findByPrimaryKeyIDO(pk);
 }



}
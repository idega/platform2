package se.idega.idegaweb.commune.accounting.regulations.data;


public class ConditionTypeHomeImpl extends com.idega.data.IDOFactory implements ConditionTypeHome
{
 protected Class getEntityInterfaceClass(){
  return ConditionType.class;
 }


 public ConditionType create() throws javax.ejb.CreateException{
  return (ConditionType) super.createIDO();
 }


public java.util.Collection findAllConditionTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ConditionTypeBMPBean)entity).ejbFindAllConditionTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ConditionType findConditionType(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ConditionTypeBMPBean)entity).ejbFindConditionType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ConditionType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ConditionType) super.findByPrimaryKeyIDO(pk);
 }



}
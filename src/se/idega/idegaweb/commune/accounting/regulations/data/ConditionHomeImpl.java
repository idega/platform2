package se.idega.idegaweb.commune.accounting.regulations.data;


public class ConditionHomeImpl extends com.idega.data.IDOFactory implements ConditionHome
{
 protected Class getEntityInterfaceClass(){
  return Condition.class;
 }


 public Condition create() throws javax.ejb.CreateException{
  return (Condition) super.createIDO();
 }


public java.util.Collection findAllConditions()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ConditionBMPBean)entity).ejbFindAllConditions();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllConditionsByRegulation(se.idega.idegaweb.commune.accounting.regulations.data.Regulation p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ConditionBMPBean)entity).ejbFindAllConditionsByRegulation(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Condition findCondition(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ConditionBMPBean)entity).ejbFindCondition(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public Condition findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Condition) super.findByPrimaryKeyIDO(pk);
 }



}
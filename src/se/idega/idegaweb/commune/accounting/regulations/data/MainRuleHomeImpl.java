package se.idega.idegaweb.commune.accounting.regulations.data;


public class MainRuleHomeImpl extends com.idega.data.IDOFactory implements MainRuleHome
{
 protected Class getEntityInterfaceClass(){
  return MainRule.class;
 }


 public MainRule create() throws javax.ejb.CreateException{
  return (MainRule) super.createIDO();
 }


public java.util.Collection findAllMainRules()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((MainRuleBMPBean)entity).ejbFindAllMainRules();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public MainRule findMainRule(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((MainRuleBMPBean)entity).ejbFindMainRule(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public MainRule findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MainRule) super.findByPrimaryKeyIDO(pk);
 }



}
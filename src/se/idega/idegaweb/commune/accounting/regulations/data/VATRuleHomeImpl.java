package se.idega.idegaweb.commune.accounting.regulations.data;


public class VATRuleHomeImpl extends com.idega.data.IDOFactory implements VATRuleHome
{
 protected Class getEntityInterfaceClass(){
  return VATRule.class;
 }


 public VATRule create() throws javax.ejb.CreateException{
  return (VATRule) super.createIDO();
 }


public java.util.Collection findAllVATRules()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((VATRuleBMPBean)entity).ejbFindAllVATRules();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public VATRule findVATRule(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((VATRuleBMPBean)entity).ejbFindVATRule(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public VATRule findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (VATRule) super.findByPrimaryKeyIDO(pk);
 }



}
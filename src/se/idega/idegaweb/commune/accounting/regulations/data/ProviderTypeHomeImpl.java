package se.idega.idegaweb.commune.accounting.regulations.data;


public class ProviderTypeHomeImpl extends com.idega.data.IDOFactory implements ProviderTypeHome
{
 protected Class getEntityInterfaceClass(){
  return ProviderType.class;
 }


 public ProviderType create() throws javax.ejb.CreateException{
  return (ProviderType) super.createIDO();
 }


public java.util.Collection findAllPaymentFlowTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ProviderTypeBMPBean)entity).ejbFindAllPaymentFlowTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ProviderType findRegulationSpecType(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ProviderTypeBMPBean)entity).ejbFindRegulationSpecType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ProviderType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProviderType) super.findByPrimaryKeyIDO(pk);
 }



}
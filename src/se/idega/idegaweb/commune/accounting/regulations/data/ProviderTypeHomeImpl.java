package se.idega.idegaweb.commune.accounting.regulations.data;


public class ProviderTypeHomeImpl extends com.idega.data.IDOFactory implements ProviderTypeHome
{
 protected Class getEntityInterfaceClass(){
  return ProviderType.class;
 }


 public ProviderType create() throws javax.ejb.CreateException{
  return (ProviderType) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ProviderTypeBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ProviderType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProviderType) super.findByPrimaryKeyIDO(pk);
 }



}
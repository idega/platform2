package se.idega.idegaweb.commune.care.data;


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

public ProviderType findCommuneType()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ProviderTypeBMPBean)entity).ejbFindCommuneType();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ProviderType findPrivateType()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ProviderTypeBMPBean)entity).ejbFindPrivateType();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ProviderType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProviderType) super.findByPrimaryKeyIDO(pk);
 }



}
package se.idega.idegaweb.commune.care.data;


public class ProviderStatisticsTypeHomeImpl extends com.idega.data.IDOFactory implements ProviderStatisticsTypeHome
{
 protected Class getEntityInterfaceClass(){
  return ProviderStatisticsType.class;
 }


 public ProviderStatisticsType create() throws javax.ejb.CreateException{
  return (ProviderStatisticsType) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ProviderStatisticsTypeBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ProviderStatisticsType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProviderStatisticsType) super.findByPrimaryKeyIDO(pk);
 }



}
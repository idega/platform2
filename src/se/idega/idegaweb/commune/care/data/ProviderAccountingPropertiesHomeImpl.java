package se.idega.idegaweb.commune.care.data;


public class ProviderAccountingPropertiesHomeImpl extends com.idega.data.IDOFactory implements ProviderAccountingPropertiesHome
{
 protected Class getEntityInterfaceClass(){
  return ProviderAccountingProperties.class;
 }


 public ProviderAccountingProperties create() throws javax.ejb.CreateException{
  return (ProviderAccountingProperties) super.createIDO();
 }


 public ProviderAccountingProperties findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProviderAccountingProperties) super.findByPrimaryKeyIDO(pk);
 }

public java.util.Collection findAllIdsByPaymentByInvoice (boolean p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = idoCheckOutPooledEntity();
	java.util.Collection ids = ((ProviderAccountingPropertiesBMPBean)entity).ejbFindAllByPaymentByInvoice(p0);
	this.idoCheckInPooledEntity(entity);
	return ids;
}


}

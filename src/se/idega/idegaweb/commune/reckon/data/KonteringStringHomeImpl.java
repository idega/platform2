package se.idega.idegaweb.commune.reckon.data;


public class KonteringStringHomeImpl extends com.idega.data.IDOFactory implements KonteringStringHome
{
 protected Class getEntityInterfaceClass(){
  return KonteringString.class;
 }


 public KonteringString create() throws javax.ejb.CreateException{
  return (KonteringString) super.createIDO();
 }


public java.util.Collection findKonterignStrings()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((KonteringStringBMPBean)entity).ejbFindKonterignStrings();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public KonteringString findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (KonteringString) super.findByPrimaryKeyIDO(pk);
 }



}
package se.idega.idegaweb.commune.reckon.data;


public class KonteringFieldHomeImpl extends com.idega.data.IDOFactory implements KonteringFieldHome
{
 protected Class getEntityInterfaceClass(){
  return KonteringField.class;
 }


 public KonteringField create() throws javax.ejb.CreateException{
  return (KonteringField) super.createIDO();
 }


public java.util.Collection findAllFieldsByKonteringString(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((KonteringFieldBMPBean)entity).ejbFindAllFieldsByKonteringString(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public KonteringField findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (KonteringField) super.findByPrimaryKeyIDO(pk);
 }



}
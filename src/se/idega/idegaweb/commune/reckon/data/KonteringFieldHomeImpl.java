package se.idega.idegaweb.commune.reckon.data;


public class KonteringFieldHomeImpl extends com.idega.data.IDOFactory implements KonteringFieldHome
{
 protected Class getEntityInterfaceClass(){
  return KonteringField.class;
 }


 public KonteringField create() throws javax.ejb.CreateException{
  return (KonteringField) super.createIDO();
 }


 public KonteringField findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (KonteringField) super.findByPrimaryKeyIDO(pk);
 }



}
package se.idega.idegaweb.commune.accounting.regulations.data;


public class YesNoHomeImpl extends com.idega.data.IDOFactory implements YesNoHome
{
 protected Class getEntityInterfaceClass(){
  return YesNo.class;
 }


 public YesNo create() throws javax.ejb.CreateException{
  return (YesNo) super.createIDO();
 }


public java.util.Collection findAllYesNoValues()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((YesNoBMPBean)entity).ejbFindAllYesNoValues();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public YesNo findYesNoValue(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((YesNoBMPBean)entity).ejbFindYesNoValue(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public YesNo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (YesNo) super.findByPrimaryKeyIDO(pk);
 }



}
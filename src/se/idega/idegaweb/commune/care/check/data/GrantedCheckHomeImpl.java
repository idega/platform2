package se.idega.idegaweb.commune.care.check.data;


public class GrantedCheckHomeImpl extends com.idega.data.IDOFactory implements GrantedCheckHome
{
 protected Class getEntityInterfaceClass(){
  return GrantedCheck.class;
 }


 public GrantedCheck create() throws javax.ejb.CreateException{
  return (GrantedCheck) super.createIDO();
 }


public java.util.Collection findChecks()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GrantedCheckBMPBean)entity).ejbFindChecks();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public GrantedCheck findChecksByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Integer id = ((GrantedCheckBMPBean)entity).ejbFindChecksByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(id);
}

public GrantedCheck findChecksByUser(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Integer id = ((GrantedCheckBMPBean)entity).ejbFindChecksByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(id);
}

 public GrantedCheck findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (GrantedCheck) super.findByPrimaryKeyIDO(pk);
 }



}
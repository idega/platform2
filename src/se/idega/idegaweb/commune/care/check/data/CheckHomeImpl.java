package se.idega.idegaweb.commune.care.check.data;


public class CheckHomeImpl extends com.idega.data.IDOFactory implements CheckHome
{
 protected Class getEntityInterfaceClass(){
  return Check.class;
 }


 public Check create() throws javax.ejb.CreateException{
  return (Check) super.createIDO();
 }


public java.util.Collection findAllCasesByStatus(com.idega.block.process.data.CaseStatus p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CheckBMPBean)entity).ejbFindAllCasesByStatus(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findApprovedChecksByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CheckBMPBean)entity).ejbFindApprovedChecksByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByStatusAndChild(java.lang.String p0,java.lang.Integer p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CheckBMPBean)entity).ejbFindByStatusAndChild(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByStatusAndUserAndChild(java.lang.String p0,com.idega.user.data.User p1,java.lang.Integer p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CheckBMPBean)entity).ejbFindByStatusAndUserAndChild(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByUserAndChild(com.idega.user.data.User p0,java.lang.Integer p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CheckBMPBean)entity).ejbFindByUserAndChild(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Check findCheckForChild(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((CheckBMPBean)entity).ejbFindCheckForChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findChecks()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CheckBMPBean)entity).ejbFindChecks();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findChecksByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CheckBMPBean)entity).ejbFindChecksByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findChecksByUserAndStatus(com.idega.user.data.User p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CheckBMPBean)entity).ejbFindChecksByUserAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findNonApprovedChecks()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CheckBMPBean)entity).ejbFindNonApprovedChecks();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Check findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Check) super.findByPrimaryKeyIDO(pk);
 }



}
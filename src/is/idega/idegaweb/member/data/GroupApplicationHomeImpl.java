package is.idega.idegaweb.member.data;


public class GroupApplicationHomeImpl extends com.idega.data.IDOFactory implements GroupApplicationHome
{
 protected Class getEntityInterfaceClass(){
  return GroupApplication.class;
 }


 public GroupApplication create() throws javax.ejb.CreateException{
  return (GroupApplication) super.createIDO();
 }

public java.util.Collection findAllApplicationsByStatusOrderedByCreationDate(java.lang.String p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GroupApplicationBMPBean)entity).ejbFindAllApplicationsByStatusOrderedByCreationDate(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllApplicationsByStatusAndApplicationGroupOrderedByCreationDate(java.lang.String p0,com.idega.user.data.Group p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GroupApplicationBMPBean)entity).ejbFindAllApplicationsByStatusAndApplicationGroupOrderedByCreationDate(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllApplicationsByStatusAndApplicationGroup(java.lang.String p0,com.idega.user.data.Group p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GroupApplicationBMPBean)entity).ejbFindAllApplicationsByStatusAndApplicationGroup(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllApplicationsByStatus(java.lang.String p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GroupApplicationBMPBean)entity).ejbFindAllApplicationsByStatus(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllApplications()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GroupApplicationBMPBean)entity).ejbFindAllApplications();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public GroupApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (GroupApplication) super.findByPrimaryKeyIDO(pk);
 }


public java.lang.String getPendingStatusString()throws java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((GroupApplicationBMPBean)entity).ejbHomeGetPendingStatusString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.String getApprovedStatusString()throws java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((GroupApplicationBMPBean)entity).ejbHomeGetApprovedStatusString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.String getDeniedStatusString()throws java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((GroupApplicationBMPBean)entity).ejbHomeGetDeniedStatusString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}
package se.idega.idegaweb.commune.complaint.data;


public class ComplaintHomeImpl extends com.idega.data.IDOFactory implements ComplaintHome
{
 protected Class getEntityInterfaceClass(){
  return Complaint.class;
 }


 public Complaint create() throws javax.ejb.CreateException{
  return (Complaint) super.createIDO();
 }


public java.util.Collection findAllComplaints()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ComplaintBMPBean)entity).ejbFindAllComplaints();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllComplaintsByManager(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ComplaintBMPBean)entity).ejbFindAllComplaintsByManager(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllComplaintsByStatus(com.idega.block.process.data.CaseStatus p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ComplaintBMPBean)entity).ejbFindAllComplaintsByStatus(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllComplaintsByType(com.idega.block.process.data.CaseCode p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ComplaintBMPBean)entity).ejbFindAllComplaintsByType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllComplaintsByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ComplaintBMPBean)entity).ejbFindAllComplaintsByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Complaint findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Complaint) super.findByPrimaryKeyIDO(pk);
 }



}
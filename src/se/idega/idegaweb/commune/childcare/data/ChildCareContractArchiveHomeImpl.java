package se.idega.idegaweb.commune.childcare.data;


public class ChildCareContractArchiveHomeImpl extends com.idega.data.IDOFactory implements ChildCareContractArchiveHome
{
 protected Class getEntityInterfaceClass(){
  return ChildCareContractArchive.class;
 }


 public ChildCareContractArchive create() throws javax.ejb.CreateException{
  return (ChildCareContractArchive) super.createIDO();
 }


public java.util.Collection findByApplication(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractArchiveBMPBean)entity).ejbFindByApplication(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChild(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractArchiveBMPBean)entity).ejbFindByChild(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildAndProvider(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ChildCareContractArchiveBMPBean)entity).ejbFindByChildAndProvider(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ChildCareContractArchive findByContractFileID(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ChildCareContractArchiveBMPBean)entity).ejbFindByContractFileID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ChildCareContractArchive findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ChildCareContractArchive) super.findByPrimaryKeyIDO(pk);
 }



}
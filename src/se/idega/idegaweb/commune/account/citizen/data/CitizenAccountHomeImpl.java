package se.idega.idegaweb.commune.account.citizen.data;


public class CitizenAccountHomeImpl extends com.idega.data.IDOFactory implements CitizenAccountHome
{
 protected Class getEntityInterfaceClass(){
  return CitizenAccount.class;
 }


 public CitizenAccount create() throws javax.ejb.CreateException{
  return (CitizenAccount) super.createIDO();
 }


public java.util.Collection findAllCasesByStatus(com.idega.block.process.data.CaseStatus p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CitizenAccountBMPBean)entity).ejbFindAllCasesByStatus(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByStatus(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CitizenAccountBMPBean)entity).ejbFindAllCasesByStatus(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public CitizenAccount findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CitizenAccount) super.findByPrimaryKeyIDO(pk);
 }


public int getTotalCount()throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((CitizenAccountBMPBean)entity).ejbHomeGetTotalCount();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}
package is.idega.idegaweb.campus.block.application.data;


public class WaitingListHomeImpl extends com.idega.data.IDOFactory implements WaitingListHome
{
 protected Class getEntityInterfaceClass(){
  return WaitingList.class;
 }


 public WaitingList create() throws javax.ejb.CreateException{
  return (WaitingList) super.createIDO();
 }


public java.util.Collection findByApartmentTypeAndComplex(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WaitingListBMPBean)entity).ejbFindByApartmentTypeAndComplex(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApartmentTypeAndComplexForApplicationType(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WaitingListBMPBean)entity).ejbFindByApartmentTypeAndComplexForApplicationType(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApartmentTypeAndComplexForTransferType(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WaitingListBMPBean)entity).ejbFindByApartmentTypeAndComplexForTransferType(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByApplicantID(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WaitingListBMPBean)entity).ejbFindByApplicantID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WaitingListBMPBean)entity).ejbFindBySQL(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findNextForTransferByApartmentTypeAndComplex(int p0,int p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WaitingListBMPBean)entity).ejbFindNextForTransferByApartmentTypeAndComplex(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public WaitingList findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WaitingList) super.findByPrimaryKeyIDO(pk);
 }



}
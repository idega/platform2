package is.idega.idegaweb.campus.block.application.data;


public class WaitingListHomeImpl extends com.idega.data.IDOFactory implements WaitingListHome
{
 protected Class getEntityInterfaceClass(){
  return WaitingList.class;
 }

 public WaitingList create() throws javax.ejb.CreateException{
  return (WaitingList) super.idoCreate();
 }

 public WaitingList createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public WaitingList findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (WaitingList) super.idoFindByPrimaryKey(id);
 }

 public WaitingList findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WaitingList) super.idoFindByPrimaryKey(pk);
 }

 public WaitingList findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }

public java.util.Collection findByApartmentTypeAndComplexForTransferType(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WaitingListBMPBean)entity).ejbFindByApartmentTypeAndComplexForTransferType(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
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


}
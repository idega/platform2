package is.idega.idegaweb.campus.block.application.data;

import java.util.Collection;

import javax.ejb.FinderException;


public class CampusApplicationHomeImpl extends com.idega.data.IDOFactory implements CampusApplicationHome
{
 protected Class getEntityInterfaceClass(){
  return CampusApplication.class;
 }


 public CampusApplication create() throws javax.ejb.CreateException{
  return (CampusApplication) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CampusApplicationBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByApplicationId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CampusApplicationBMPBean)entity).ejbFindAllByApplicationId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CampusApplicationBMPBean)entity).ejbFindBySQL(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySubjectAndStatus(java.lang.Integer p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CampusApplicationBMPBean)entity).ejbFindBySubjectAndStatus(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySubjectAndStatus(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,int p3,int p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CampusApplicationBMPBean)entity).ejbFindBySubjectAndStatus(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public CampusApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CampusApplication) super.findByPrimaryKeyIDO(pk);
 }


public int getCountBySubjectAndStatus(java.lang.Integer p0,java.lang.String p1)throws com.idega.data.IDORelationshipException,com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((CampusApplicationBMPBean)entity).ejbHomeGetCountBySubjectAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}



	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationHome#ejbFindByApartmentTypeAndComplex(java.lang.Integer, java.lang.Integer)
	 */
	public Collection findByApartmentTypeAndComplex(Integer typeId,	Integer complexID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CampusApplicationBMPBean)entity).ejbFindByApartmentTypeAndComplex(typeId,complexID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
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

 public CampusApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CampusApplication) super.findByPrimaryKeyIDO(pk);
 }

 /* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.application.data.CampusApplicationHome#findBySubjectAndStatus(java.lang.Integer, java.lang.String)
	 */
	public Collection findBySubjectAndStatus(Integer subjectID, String status,String order)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CampusApplicationBMPBean)entity).ejbFindBySubjectAndStatus(subjectID,status,order);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
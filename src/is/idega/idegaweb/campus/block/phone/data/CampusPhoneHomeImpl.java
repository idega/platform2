package is.idega.idegaweb.campus.block.phone.data;

import java.util.Collection;

import javax.ejb.FinderException;


public class CampusPhoneHomeImpl extends com.idega.data.IDOFactory implements CampusPhoneHome
{
 protected Class getEntityInterfaceClass(){
  return CampusPhone.class;
 }

 public CampusPhone create() throws javax.ejb.CreateException{
  return (CampusPhone) super.createIDO();
 }

 public CampusPhone findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (CampusPhone) super.idoFindByPrimaryKey(id);
 }

 public CampusPhone findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CampusPhone) super.findByPrimaryKeyIDO(pk);
 }

 


	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.phone.data.CampusPhoneHome#findAll()
	 */
	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CampusPhoneBMPBean)entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.block.phone.data.CampusPhoneHome#findByPhoneNumber(java.lang.String)
	 */
	public Collection findByPhoneNumber(String number) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CampusPhoneBMPBean)entity).ejbFindByPhoneNumber(number);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
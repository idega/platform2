package is.idega.idegaweb.campus.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;


public class AccountPhoneHomeImpl extends com.idega.data.IDOFactory implements AccountPhoneHome
{
 protected Class getEntityInterfaceClass(){
  return AccountPhone.class;
 }


 public AccountPhone create() throws javax.ejb.CreateException{
  return (AccountPhone) super.createIDO();
 }


 public AccountPhone createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public AccountPhone findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountPhone) super.findByPrimaryKeyIDO(pk);
 }


 public AccountPhone findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (AccountPhone) super.findByPrimaryKeyIDO(id);
 }


 public AccountPhone findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.data.AccountPhoneHome#findByPhoneNumber(java.lang.String)
	 */
	public Collection findByPhoneNumber(String number) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountPhoneBMPBean)entity).ejbFindByPhoneNumber(number);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.data.AccountPhoneHome#findAll()
	 */
	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountPhoneBMPBean)entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.campus.data.AccountPhoneHome#findValid(java.sql.Date)
	 */
	public Collection findValid(Date toDate) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AccountPhoneBMPBean)entity).ejbFindValid(toDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
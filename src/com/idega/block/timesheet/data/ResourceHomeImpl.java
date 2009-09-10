package com.idega.block.timesheet.data;

import java.util.Collection;

import javax.ejb.FinderException;


public class ResourceHomeImpl extends com.idega.data.IDOFactory implements ResourceHome
{
 protected Class getEntityInterfaceClass(){
  return Resource.class;
 }

 public Resource create() throws javax.ejb.CreateException{
  return (Resource) super.idoCreate();
 }

 public Resource createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Resource findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Resource) super.idoFindByPrimaryKey(id);
 }

 public Resource findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Resource) super.idoFindByPrimaryKey(pk);
 }

 public Resource findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }
 
 


	/* (non-Javadoc)
	 * @see com.idega.block.timesheet.data.ResourceHome#findAllClosed()
	 */
	public Collection findAllClosed() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ResourceBMPBean) entity).ejbFindAllClosed();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	/* (non-Javadoc)
	 * @see com.idega.block.timesheet.data.ResourceHome#findAllOpen()
	 */
	public Collection findAllOpen() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ResourceBMPBean) entity).ejbFindAllOpen();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	/* (non-Javadoc)
	 * @see com.idega.block.timesheet.data.ResourceHome#findByClosure(boolean)
	 */
	public Collection findByClosure(boolean closed) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ResourceBMPBean) entity).ejbFindByClosure(closed);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
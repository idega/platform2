package com.idega.block.creditcard.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.util.IWTimestamp;


public class TPosAuthorisationEntriesBeanHomeImpl extends com.idega.data.IDOFactory implements TPosAuthorisationEntriesBeanHome
{
 protected Class getEntityInterfaceClass(){
  return TPosAuthorisationEntriesBean.class;
 }


 public TPosAuthorisationEntriesBean create() throws javax.ejb.CreateException{
  return (TPosAuthorisationEntriesBean) super.createIDO();
 }


public TPosAuthorisationEntriesBean findByAuthorisationIdRsp(java.lang.String p0, IWTimestamp stamp)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((TPosAuthorisationEntriesBeanBMPBean)entity).ejbFindByAuthorisationIdRsp(p0, stamp);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public Collection findRefunds(IWTimestamp from, IWTimestamp to) throws FinderException {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TPosAuthorisationEntriesBeanBMPBean)entity).ejbFindRefunds(from, to);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}


 public TPosAuthorisationEntriesBean findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TPosAuthorisationEntriesBean) super.findByPrimaryKeyIDO(pk);
 }



}
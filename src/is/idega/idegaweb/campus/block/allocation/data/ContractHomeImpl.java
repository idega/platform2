package is.idega.idegaweb.campus.block.allocation.data;

import java.util.Collection;

import javax.ejb.FinderException;


public class ContractHomeImpl extends com.idega.data.IDOFactory implements ContractHome
{
 protected Class getEntityInterfaceClass(){
  return Contract.class;
 }

 public Contract create() throws javax.ejb.CreateException{
  return (Contract) super.idoCreate();
 }

 public Contract createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Contract findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Contract) super.idoFindByPrimaryKey(id);
 }

 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Contract) super.idoFindByPrimaryKey(pk);
 }

 public Contract findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }
 
 public Collection findByApplicant(Integer ID)throws FinderException{
	 com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	 java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByApplicant(ID);
	 this.idoCheckInPooledEntity(entity);
	 return this.getEntityCollectionForPrimaryKeys(ids);
 }


}
package is.idega.idegaweb.travel.data;


public class ContractHomeImpl extends com.idega.data.IDOFactory implements ContractHome
{
 protected Class getEntityInterfaceClass(){
  return Contract.class;
 }


 public Contract create() throws javax.ejb.CreateException{
  return (Contract) super.createIDO();
 }


public Contract findByProductAndReseller(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ContractBMPBean)entity).ejbFindByProductAndReseller(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findByProductId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByProductId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByResellerId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindByResellerId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Contract) super.findByPrimaryKeyIDO(pk);
 }



}
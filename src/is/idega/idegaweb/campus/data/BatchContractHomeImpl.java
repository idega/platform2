package is.idega.idegaweb.campus.data;


public class BatchContractHomeImpl extends com.idega.data.IDOFactory implements BatchContractHome
{
 protected Class getEntityInterfaceClass(){
  return BatchContract.class;
 }


public BatchContract create(is.idega.idegaweb.campus.data.BatchContractKey p0)throws javax.ejb.CreateException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((BatchContractBMPBean)entity).ejbCreate(p0);
	this.idoCheckInPooledEntity(entity);
	try{
		return this.findByPrimaryKey(pk);
	}
	catch(javax.ejb.FinderException fe){
		throw new com.idega.data.IDOCreateException(fe);
	}
	catch(Exception e){
		throw new com.idega.data.IDOCreateException(e);
	}
}

 public BatchContract create() throws javax.ejb.CreateException{
  return (BatchContract) super.createIDO();
 }


public BatchContract findByPrimaryKey(is.idega.idegaweb.campus.data.BatchContractKey p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((BatchContractBMPBean)entity).ejbFindByPrimaryKey(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public BatchContract findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BatchContract) super.findByPrimaryKeyIDO(pk);
 }



}
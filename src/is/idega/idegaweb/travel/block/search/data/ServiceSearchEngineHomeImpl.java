package is.idega.idegaweb.travel.block.search.data;


public class ServiceSearchEngineHomeImpl extends com.idega.data.IDOFactory implements ServiceSearchEngineHome
{
 protected Class getEntityInterfaceClass(){
  return ServiceSearchEngine.class;
 }


 public ServiceSearchEngine create() throws javax.ejb.CreateException{
  return (ServiceSearchEngine) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ServiceSearchEngineBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ServiceSearchEngine findByCode(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ServiceSearchEngineBMPBean)entity).ejbFindByCode(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ServiceSearchEngine findByGroupID(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ServiceSearchEngineBMPBean)entity).ejbFindByGroupID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public ServiceSearchEngine findByName(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ServiceSearchEngineBMPBean)entity).ejbFindByName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ServiceSearchEngine findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ServiceSearchEngine) super.findByPrimaryKeyIDO(pk);
 }



}
package is.idega.idegaweb.golf.entity;


public class StartingtimeFieldConfigHomeImpl extends com.idega.data.IDOFactory implements StartingtimeFieldConfigHome
{
 protected Class getEntityInterfaceClass(){
  return StartingtimeFieldConfig.class;
 }


 public StartingtimeFieldConfig create() throws javax.ejb.CreateException{
  return (StartingtimeFieldConfig) super.createIDO();
 }


 public StartingtimeFieldConfig createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


public java.util.Collection findAllActiveTeetimeFieldConfigurations(com.idega.util.IWTimestamp p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((StartingtimeFieldConfigBMPBean)entity).ejbFindAllActiveTeetimeFieldConfigurations(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public StartingtimeFieldConfig findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (StartingtimeFieldConfig) super.findByPrimaryKeyIDO(pk);
 }


 public StartingtimeFieldConfig findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (StartingtimeFieldConfig) super.findByPrimaryKeyIDO(id);
 }


 public StartingtimeFieldConfig findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}
package is.idega.idegaweb.golf.entity;


public class StartingtimeFieldConfigHomeImpl extends com.idega.data.IDOFactory implements StartingtimeFieldConfigHome
{
 protected Class getEntityInterfaceClass(){
  return StartingtimeFieldConfig.class;
 }

 public StartingtimeFieldConfig create() throws javax.ejb.CreateException{
  return (StartingtimeFieldConfig) super.idoCreate();
 }

 public StartingtimeFieldConfig createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public StartingtimeFieldConfig findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (StartingtimeFieldConfig) super.idoFindByPrimaryKey(id);
 }

 public StartingtimeFieldConfig findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (StartingtimeFieldConfig) super.idoFindByPrimaryKey(pk);
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
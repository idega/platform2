package is.idega.idegaweb.golf.entity;


public class Poll_resultHomeImpl extends com.idega.data.IDOFactory implements Poll_resultHome
{
 protected Class getEntityInterfaceClass(){
  return Poll_result.class;
 }

 public Poll_result create() throws javax.ejb.CreateException{
  return (Poll_result) super.idoCreate();
 }

 public Poll_result createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Poll_result findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Poll_result) super.idoFindByPrimaryKey(id);
 }

 public Poll_result findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Poll_result) super.idoFindByPrimaryKey(pk);
 }

 public Poll_result findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}
package is.idega.idegaweb.golf.entity;


public class PollHomeImpl extends com.idega.data.IDOFactory implements PollHome
{
 protected Class getEntityInterfaceClass(){
  return Poll.class;
 }

 public Poll create() throws javax.ejb.CreateException{
  return (Poll) super.idoCreate();
 }

 public Poll createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Poll findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Poll) super.idoFindByPrimaryKey(id);
 }

 public Poll findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Poll) super.idoFindByPrimaryKey(pk);
 }

 public Poll findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}
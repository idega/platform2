package is.idega.idegaweb.golf.entity;


public class StartingtimeHomeImpl extends com.idega.data.IDOFactory implements StartingtimeHome
{
 protected Class getEntityInterfaceClass(){
  return Startingtime.class;
 }

 public Startingtime create() throws javax.ejb.CreateException{
  return (Startingtime) super.idoCreate();
 }

 public Startingtime createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Startingtime findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Startingtime) super.idoFindByPrimaryKey(id);
 }

 public Startingtime findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Startingtime) super.idoFindByPrimaryKey(pk);
 }

 public Startingtime findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}
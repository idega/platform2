package is.idega.idegaweb.golf.startingtime.data;


public class TeeTimeHomeImpl extends com.idega.data.IDOFactory implements TeeTimeHome
{
 protected Class getEntityInterfaceClass(){
  return TeeTime.class;
 }

 public TeeTime create() throws javax.ejb.CreateException{
  return (TeeTime) super.idoCreate();
 }

 public TeeTime createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TeeTime findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TeeTime) super.idoFindByPrimaryKey(id);
 }

 public TeeTime findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TeeTime) super.idoFindByPrimaryKey(pk);
 }

 public TeeTime findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}
package is.idega.idegaweb.golf.block.boxoffice.data;


public class IssuesHomeImpl extends com.idega.data.IDOFactory implements IssuesHome
{
 protected Class getEntityInterfaceClass(){
  return Issues.class;
 }


 public Issues create() throws javax.ejb.CreateException{
  return (Issues) super.createIDO();
 }


 public Issues createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public Issues findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Issues) super.findByPrimaryKeyIDO(pk);
 }


 public Issues findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Issues) super.findByPrimaryKeyIDO(id);
 }


 public Issues findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}
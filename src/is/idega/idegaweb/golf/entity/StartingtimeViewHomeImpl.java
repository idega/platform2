package is.idega.idegaweb.golf.entity;


public class StartingtimeViewHomeImpl extends com.idega.data.IDOFactory implements StartingtimeViewHome
{
 protected Class getEntityInterfaceClass(){
  return StartingtimeView.class;
 }

 public StartingtimeView create() throws javax.ejb.CreateException{
  return (StartingtimeView) super.idoCreate();
 }

 public StartingtimeView createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public StartingtimeView findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (StartingtimeView) super.idoFindByPrimaryKey(id);
 }

 public StartingtimeView findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (StartingtimeView) super.idoFindByPrimaryKey(pk);
 }

 public StartingtimeView findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}
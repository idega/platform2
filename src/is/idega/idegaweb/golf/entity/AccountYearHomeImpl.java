package is.idega.idegaweb.golf.entity;


public class AccountYearHomeImpl extends com.idega.data.IDOFactory implements AccountYearHome
{
 protected Class getEntityInterfaceClass(){
  return AccountYear.class;
 }

 public AccountYear create() throws javax.ejb.CreateException{
  return (AccountYear) super.idoCreate();
 }

 public AccountYear createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public AccountYear findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (AccountYear) super.idoFindByPrimaryKey(id);
 }

 public AccountYear findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountYear) super.idoFindByPrimaryKey(pk);
 }

 public AccountYear findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}